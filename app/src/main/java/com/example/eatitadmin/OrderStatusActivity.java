package com.example.eatitadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eatitadmin.Adapters.OrderStatusAdapter;
import com.example.eatitadmin.Common.Common;
import com.example.eatitadmin.Model.Request;
import com.example.eatitadmin.Model.Token;
import com.example.eatitadmin.Notifications.APIService;
import com.example.eatitadmin.Notifications.Data;
import com.example.eatitadmin.Notifications.DataMessage;
import com.example.eatitadmin.Notifications.MyResponse;
import com.example.eatitadmin.Notifications.RetrofitClient;
import com.example.eatitadmin.Notifications.Sender;
import com.example.eatitadmin.ui.Orders.OrdersFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStatusActivity extends AppCompatActivity implements OrderStatusAdapter.onItemClickListener {


    RecyclerView order_recyclerview;
    OrderStatusAdapter adapter;
    List<Request> requestList;
    DatabaseReference reference;
    Spinner spinner;
    ArrayAdapter<String> spinnerAdapter;
    List<String> tableList;
    String id;
    APIService apiService;
    ImageView backBtn;
    boolean notify=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);


        apiService= RetrofitClient.getClient("https://fcm.googleapis.com/").create(APIService.class);


        order_recyclerview=findViewById(R.id.orderStatus_recyclerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        order_recyclerview.setLayoutManager(linearLayoutManager);
        requestList=new ArrayList<>();
        adapter=new OrderStatusAdapter(requestList,this);
        order_recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(this);


        loadStatusData(Common.currentUser.getPhone());


        backBtn=findViewById(R.id.back_status);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backBtn.setImageTintList(getResources().getColorStateList(R.color.green));
                finish();
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onUpdateClick(int position) {

        Request selectedItem = requestList.get(position);
        String selectedID = selectedItem.getOrderID();
        showUpdateDialog(selectedID, selectedItem);
    }

    @Override
    public void onDeleteClick(int position) {


        Request selectedItem = requestList.get(position);

        String selectedID = selectedItem.getOrderID();

        //Toast.makeText(FoodListActivity.this, "delete" + selectedID, Toast.LENGTH_SHORT).show();

        reference.child(selectedID).removeValue();

    }

    @Override
    public void onShowDetailClick(int position) {


        Request selectedItem = requestList.get(position);
        Intent intent=new Intent(this, OrderDetailActivity.class);
        Common.currentRequest=selectedItem;
        intent.putExtra("OID",selectedItem.getOrderID());
        startActivity(intent);


    }

    private void showUpdateDialog(String selectedID, final Request selectedItem) {

        final AlertDialog.Builder alertBuilder=new AlertDialog.Builder(OrderStatusActivity.this);
        alertBuilder.setTitle("Order Confirmation");
        alertBuilder.setMessage("Please Choose Status");

        View view= LayoutInflater.from(OrderStatusActivity.this).inflate(R.layout.update_order_layout,null);

        spinner=view.findViewById(R.id.status);

        tableList=new ArrayList<>();
        tableList.add("Order Served in just 15 minutes");
        tableList.add("Order Served in just 30 minutes");
        tableList.add("Your Order has been served, Enjoy your meal Thank you");
        tableList.add("Cancel");
        spinnerAdapter = new ArrayAdapter<>(OrderStatusActivity.this,android.R.layout.simple_spinner_dropdown_item, tableList);
        spinner.setAdapter(this.spinnerAdapter);

        alertBuilder.setView(view);

        final String local_id=selectedID;

        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                selectedItem.setStatus(spinner.getSelectedItem().toString());

                reference.child(local_id).setValue(selectedItem);


                sendOrderStatusToUser(local_id,selectedItem);

            }
        });

        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        alertBuilder.show();
    }


    private void  sendOrderStatusToUser(final String key, final Request selectedItem) {

        notify=true;

        DatabaseReference token_ref=FirebaseDatabase.getInstance().getReference().child("Token");
        token_ref.orderByKey().equalTo(selectedItem.getPhone())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot:dataSnapshot.getChildren()) {


                            Token ServerToken = snapshot.getValue(Token.class);


                            if (notify == true) {

                                Data data1 = new Data(ServerToken.getUserPhone(), "Table Pay", key+"_ \n"+spinner.getSelectedItem().toString(), Common.currentUser.getPhone(), "OrderChange");

                                if (!ServerToken.getTokenID().isEmpty()) {
                                    Sender sender = new Sender(ServerToken.getTokenID(), data1);

                                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                            notify=false;
                                            Toast.makeText(OrderStatusActivity.this, response.message(), Toast.LENGTH_LONG).show();

                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Toast.makeText(OrderStatusActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

                                        }
                                    });

                                }else{

                                    Toast.makeText(OrderStatusActivity.this," Chef Not Available...", Toast.LENGTH_LONG).show();
                                }


                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void loadStatusData(final String phone) {
        reference= FirebaseDatabase.getInstance().getReference().child("Request");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                requestList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                    Request request=snapshot.getValue(Request.class);

                    requestList.add(request);

                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
