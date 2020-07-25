package com.example.eatitadmin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitadmin.Model.Order;
import com.example.eatitadmin.R;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

    List<Order> orderList;
    Context context;

    public OrderDetailAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_detail_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Order order=orderList.get(position);

        holder.item_name.setText(order.getProductName());
        holder.item_price.setText(order.getPrice());
        holder.item_quantity.setText(order.getQuantity());
        holder.item_discount.setText(order.getDiscount());

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_name, item_price, item_discount, item_quantity;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_name = itemView.findViewById(R.id.order_item_name);
            item_price=itemView.findViewById(R.id.order_item_price);
            item_discount=itemView.findViewById(R.id.order_item_discount);
            item_quantity=itemView.findViewById(R.id.order_item_quantity);
        }
    }
}
