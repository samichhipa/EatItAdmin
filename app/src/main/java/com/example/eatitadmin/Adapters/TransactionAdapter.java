package com.example.eatitadmin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitadmin.Common.Common;
import com.example.eatitadmin.Model.Request;
import com.example.eatitadmin.R;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    List<Request> requestList;
    Context context;


    public TransactionAdapter(List<Request> requestList, Context context) {
        this.requestList = requestList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(context).inflate(R.layout.transaction_layout,parent,false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Request request=requestList.get(position);

        holder.txt_oid.setText(request.getOrderID());
        holder.txt_date_time.setText(Common.getDate(Long.parseLong(request.getOrderID())));
        String totalTax=String.valueOf(Integer.valueOf(request.getTotal())+Integer.parseInt(request.getTax()));
        holder.txt_total.setText("Rs."+totalTax+"(5% tax inc)");
        holder.txt_tableNo.setText(request.getTableNo());

    }


    @Override
    public int getItemCount() {
        return requestList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView txt_oid,txt_date_time,txt_total,txt_tableNo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_oid=itemView.findViewById(R.id.trans_order_id);
            txt_date_time=itemView.findViewById(R.id.trans_date_time);
            txt_total=itemView.findViewById(R.id.trans_order_total);
            txt_tableNo=itemView.findViewById(R.id.trans_order_table_no);
        }
    }
}
