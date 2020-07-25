package com.example.eatitadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eatitadmin.Adapters.OrderDetailAdapter;
import com.example.eatitadmin.Common.Common;
import com.example.eatitadmin.Model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {


    TextView order_id,order_phone,order_tableNo,order_total;
    RecyclerView orderlistRecyclerview;
    List<Order> orderList;
    OrderDetailAdapter adapter;
    String OID;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        order_id=findViewById(R.id.prod_order_id);
        order_phone=findViewById(R.id.prod_order_phone);
        order_tableNo=findViewById(R.id.prod_order_tableNo);
        order_total=findViewById(R.id.prod_order_total);
        orderlistRecyclerview=findViewById(R.id.orderlist_recyclerview);
        orderList=new ArrayList<>();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        orderlistRecyclerview.setLayoutManager(linearLayoutManager);



        if (getIntent()!=null){

            OID=getIntent().getStringExtra("OID");
            order_id.setText(OID);
            order_phone.setText(Common.currentRequest.getPhone());
            order_tableNo.setText(Common.currentRequest.getTableNo());
            order_total.setText(Common.currentRequest.getTotal());
            adapter=new OrderDetailAdapter(Common.currentRequest.getOrderList(),this);
            orderlistRecyclerview.setAdapter(adapter);

        }

        backBtn=findViewById(R.id.back_order_detail);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backBtn.setImageTintList(getResources().getColorStateList(R.color.green));
                finish();
            }
        });


    }
}
