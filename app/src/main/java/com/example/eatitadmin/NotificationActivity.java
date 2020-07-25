package com.example.eatitadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eatitadmin.Common.Common;
import com.example.eatitadmin.Model.Request;
import com.example.eatitadmin.Model.Token;
import com.example.eatitadmin.Notifications.APIService;
import com.example.eatitadmin.Notifications.Data;
import com.example.eatitadmin.Notifications.DataMessage;
import com.example.eatitadmin.Notifications.MyResponse;
import com.example.eatitadmin.Notifications.Notification;
import com.example.eatitadmin.Notifications.RetrofitClient;
import com.example.eatitadmin.Notifications.Sender;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.hoang8f.widget.FButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    String body, notification_type, title, sent_id, user_id;
    FButton btnConfirm, btnCancel;

    TextView txt_body, txt_title, txt_view;
    List<String> tableList;
    Spinner spinner;
    SpinnerAdapter spinnerAdapter;
    DatabaseReference reference;
    APIService apiService;
    String userPhone;
    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        reference = FirebaseDatabase.getInstance().getReference().child("Request");


        apiService = RetrofitClient.getClient("https://fcm.googleapis.com/").create(APIService.class);


        txt_body = findViewById(R.id.body_not);
        txt_title = findViewById(R.id.not_title);

        btnConfirm = findViewById(R.id.confirm_not);
        btnCancel = findViewById(R.id.cancel_not);
        txt_view = findViewById(R.id.view_all);


        mediaPlayer = MediaPlayer.create(this, R.raw.ringtone);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        if (getIntent() != null) {


            title = getIntent().getStringExtra("title");
            body = getIntent().getStringExtra("body");
            sent_id = getIntent().getStringExtra("sent_id");
            user_id = getIntent().getStringExtra("user_id");
            notification_type = getIntent().getStringExtra("notification_type");



            if (notification_type.equals("Order")) {

                getReqData();
                btnConfirm.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                btnCancel.setText("Cancel");
                txt_view.setVisibility(View.VISIBLE);
                txt_body.setText("Order ID" + ":" + body);


            } else if (notification_type.equals("Call")) {

                btnConfirm.setVisibility(View.GONE);
                btnCancel.setVisibility(View.VISIBLE);
                btnCancel.setText("Ok");
                txt_view.setVisibility(View.GONE);
                txt_body.setText("Table No : " + body + "Calling Waiter");

            } else if (notification_type.equals("Billing")) {

                btnConfirm.setVisibility(View.GONE);
                btnCancel.setVisibility(View.VISIBLE);
                btnCancel.setText("Ok");
                txt_view.setVisibility(View.GONE);
                txt_body.setText("Table No : " + body + "Calling For Bill");

            } else if (notification_type.equals("OrderChanging")) {

                btnConfirm.setVisibility(View.GONE);
                btnCancel.setVisibility(View.VISIBLE);
                btnCancel.setText("Cancel");
                txt_view.setVisibility(View.GONE);
                txt_body.setText("body");

            }

        }
        txt_title.setText(title);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showUpdateDialog(body);

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnCancel.getText().equals("Cancel")) {
                    finish();
                } else if (btnCancel.getText().equals("Ok")) {

                    sendNotificationToUser(user_id);
                    finish();
                }

            }
        });

        txt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NotificationActivity.this, OrderDetailActivity.class);
                intent.putExtra("OID", body);
                startActivity(intent);
            }
        });
    }

    private void sendNotificationToUser(final String body) {

        notify = true;

        DatabaseReference token_ref = FirebaseDatabase.getInstance().getReference().child("Token");
        token_ref.orderByKey().equalTo(body)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                            Token token = snapshot.getValue(Token.class);

                            if (notify == true) {


                                Data data1 = new Data(token.getUserPhone(), "Table Pay", "Waiter is Coming...", Common.currentUser.getPhone(), "Call");

                                if (!token.getTokenID().isEmpty()) {
                                    Sender sender = new Sender(token.getTokenID(), data1);

                                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                            notify = false;
                                            Toast.makeText(NotificationActivity.this, response.message(), Toast.LENGTH_LONG).show();

                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Toast.makeText(NotificationActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

                                        }
                                    });

                                } else {

                                    Toast.makeText(NotificationActivity.this, " Chef Not Available...", Toast.LENGTH_LONG).show();
                                }


                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    private void getReqData() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Request request = snapshot.getValue(Request.class);
                    if (request.getOrderID().equals(body)) {

                        userPhone = request.getPhone();
                        Common.currentRequest = request;

                        // Toast.makeText(NotificationActivity.this, userPhone,Toast.LENGTH_LONG).show();

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void showUpdateDialog(final String selectedID) {

        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(NotificationActivity.this).setCancelable(false);
        alertBuilder.setTitle("Order Confirmation");
        alertBuilder.setMessage("Please Choose Status");

        View view = LayoutInflater.from(NotificationActivity.this).inflate(R.layout.update_order_layout, null);

        spinner = view.findViewById(R.id.status);

        tableList = new ArrayList<>();
        tableList.add("Confirm,Order Served in just 15 minutes");
        tableList.add("Confirm,Order Served in just 30 minutes");
        tableList.add("Confirm");
        tableList.add("Cancel");
        spinnerAdapter = new ArrayAdapter<>(NotificationActivity.this, android.R.layout.simple_spinner_dropdown_item, tableList);
        spinner.setAdapter(this.spinnerAdapter);

        alertBuilder.setView(view);

        final String local_id = selectedID;

        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                String selectedItem = spinner.getSelectedItem().toString();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("status", selectedItem);

                reference.child(local_id).updateChildren(hashMap);


                sendOrderStatusToUser(local_id);
                dialog.dismiss();
                finish();

            }
        });


        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                finish();

            }
        });

        alertBuilder.show();

    }

    private void sendOrderStatusToUser(final String key) {

        notify = true;

        DatabaseReference token_ref = FirebaseDatabase.getInstance().getReference().child("Token");
        token_ref.orderByKey().equalTo(user_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                            Token token = snapshot.getValue(Token.class);

                            if (notify == true) {

                                Data data1 = new Data(token.getUserPhone(), "Table Pay", key + "_ \n" + spinner.getSelectedItem().toString(), Common.currentUser.getPhone(), "OrderChange");

                                if (!token.getTokenID().isEmpty()) {
                                    Sender sender = new Sender(token.getTokenID(), data1);

                                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                            notify = false;
                                            Toast.makeText(NotificationActivity.this, response.message(), Toast.LENGTH_LONG).show();

                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Toast.makeText(NotificationActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

                                        }
                                    });

                                } else {

                                    Toast.makeText(NotificationActivity.this, " Chef Not Available...", Toast.LENGTH_LONG).show();
                                }


                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
}
