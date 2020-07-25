package com.example.eatitadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eatitadmin.Adapters.TransactionAdapter;
import com.example.eatitadmin.Model.Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TransactionsActivity extends AppCompatActivity {


    ImageView backBtn;
    RecyclerView recyclerView;
    TransactionAdapter transactionAdapter;
    List<Request> requestList;
    DatabaseReference reference;
    TextView txt_total;
    int total=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        txt_total=findViewById(R.id.tx_total);

        reference= FirebaseDatabase.getInstance().getReference().child("Request");

        recyclerView=findViewById(R.id.transaction_recyclerview);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        requestList=new ArrayList<>();
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        transactionAdapter=new TransactionAdapter(requestList,this);
        recyclerView.setAdapter(transactionAdapter);
        getTransactions();



        backBtn=findViewById(R.id.back_transaction);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backBtn.setImageTintList(getResources().getColorStateList(R.color.green));
                finish();
            }
        });




    }

    private void getTransactions() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                requestList.clear();

           for (DataSnapshot snapshot:dataSnapshot.getChildren()){

               Request request=snapshot.getValue(Request.class);

                   requestList.add(request);
                   total+=Integer.valueOf(request.getTotal())+Integer.valueOf(request.getTax());

           }

           txt_total.setText("Rs. "+String.valueOf(total));

                transactionAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }
}
