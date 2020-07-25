package com.example.eatitadmin.ui.Orders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitadmin.Adapters.OrderStatusAdapter;
import com.example.eatitadmin.Common.Common;
import com.example.eatitadmin.FoodListActivity;
import com.example.eatitadmin.Model.Foods;
import com.example.eatitadmin.Model.Request;
import com.example.eatitadmin.Model.Token;
import com.example.eatitadmin.Notifications.APIService;
import com.example.eatitadmin.Notifications.DataMessage;
import com.example.eatitadmin.Notifications.MyResponse;
import com.example.eatitadmin.Notifications.Notification;
import com.example.eatitadmin.Notifications.Sender;
import com.example.eatitadmin.OrderDetailActivity;
import com.example.eatitadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends Fragment implements OrderStatusAdapter.onItemClickListener{

    RecyclerView order_recyclerview;
    OrderStatusAdapter adapter;
    List<Request> requestList;
    DatabaseReference reference;
    Spinner spinner;
    ArrayAdapter<String> spinnerAdapter;
    List<String> tableList;
    String id;
    APIService mService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_orders, container, false);


        mService=Common.getFCMService();


        order_recyclerview=root.findViewById(R.id.orderStatus_recyclerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        order_recyclerview.setLayoutManager(linearLayoutManager);
        requestList=new ArrayList<>();
        adapter=new OrderStatusAdapter(requestList,getActivity());
        order_recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(OrdersFragment.this);


        loadStatusData(Common.currentUser.getPhone());

        return root;
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

    @Override
    public void onItemClick(int position) {

        Toast.makeText(getActivity(),"Cliced",Toast.LENGTH_SHORT).show();
        
    }

    @Override
    public void onUpdateClick(int position) {


    }




    @Override
    public void onDeleteClick(int position) {


    }

    @Override
    public void onShowDetailClick(int position) {

    }
}