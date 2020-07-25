package com.example.eatitadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitadmin.Common.Common;
import com.example.eatitadmin.Model.Request;
import com.example.eatitadmin.OrderDetailActivity;
import com.example.eatitadmin.R;
import com.example.eatitadmin.TrackingOrder;

import java.util.List;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.ViewHolder> {


    List<Request> requestList;
    Context context;
    OrderStatusAdapter.onItemClickListener mListener;

    public OrderStatusAdapter(List<Request> requestList, Context context) {
        this.requestList = requestList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        final Request request = requestList.get(position);
        holder.txt_order_id.setText(request.getOrderID());
        holder.txt_order_status.setText(request.getStatus());
        holder.txt_tableNo.setText(request.getTableNo());
        holder.txt_time.setText(Common.getDate(Long.parseLong(request.getOrderID())));
        holder.txt_table_id.setText(request.getTableNo());





      //  orderStatus(request.getStatus());

    }
/*
    private void orderStatus(String status) {


        if (status.equals("0")) {

            return status.setText("Placed");
        }
        else if (status.getText().toString().equals("1"))
            return "on my way";
        else
            return "Shipped";

    }

 */

    @Override
    public int getItemCount() {
        return requestList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {


        TextView txt_order_id, txt_order_status, txt_phone, txt_tableNo,txt_time,txt_table_id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_order_id = itemView.findViewById(R.id.order_id);
            txt_order_status = itemView.findViewById(R.id.order_status);
            txt_phone = itemView.findViewById(R.id.order_phone);
            txt_tableNo = itemView.findViewById(R.id.order_tableno);
            txt_time=itemView.findViewById(R.id.order_timing);
            txt_table_id=itemView.findViewById(R.id.order_phone);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            if (mListener != null) {

                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {

                        case 1:
                            mListener.onUpdateClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;

                        case 3:
                            mListener.onShowDetailClick(position);
                            return true;
                    }

                }

            }
            return false;
        }

        @Override
        public void onClick(View v) {

            if (mListener != null) {

                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {


                    mListener.onItemClick(position);

                }

            }

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("Select Option");

            MenuItem update = menu.add(Menu.NONE, 1, 1, "Update");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");
            MenuItem showDetails = menu.add(Menu.NONE, 3, 3, "Show Details");

            update.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
            showDetails.setOnMenuItemClickListener(this);

        }
    }
    public interface onItemClickListener {

        void onItemClick(int position);

        void onUpdateClick(int position);

        void onDeleteClick(int position);

        void onShowDetailClick(int position);

    }
    public void setOnItemClickListener(OrderStatusAdapter.onItemClickListener listener) {


        mListener = listener;


    }
}
