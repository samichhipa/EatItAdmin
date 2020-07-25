package com.example.eatitadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.eatitadmin.Model.Foods;
import com.example.eatitadmin.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    List<Foods> foodsList;
    Context context;
    FoodAdapter.onItemClickListener mListener;

    public FoodAdapter(List<Foods> foodsList, Context context) {
        this.foodsList = foodsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.food_item,parent,false);
       return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Foods foods=foodsList.get(position);
        Picasso.get().load(foods.getImage()).into(holder.img);
        holder.txt.setText(foods.getName());


        if (foods.getStatus().equals("Enabled")){

            holder.food_status.setText("Available");
            holder.food_status.setBackgroundColor(context.getResources().getColor(R.color.high_transparent_green));
        }
        if (foods.getStatus().equals("Disabled")){


            holder.food_status.setText("Unavailable");
            holder.food_status.setBackgroundColor(context.getResources().getColor(R.color.light_red));

        }

    }

    @Override
    public int getItemCount() {
        return foodsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        ImageView img;
        TextView txt,food_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            img=itemView.findViewById(R.id.foodlist_img);
            txt=itemView.findViewById(R.id.foodlist_txt);
            food_status=itemView.findViewById(R.id.food_status);

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
                            mListener.onStatusChange(position);
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
            MenuItem food_status = menu.add(Menu.NONE, 3, 3, "Food Status");

            update.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
            food_status.setOnMenuItemClickListener(this);
        }
    }

    public interface onItemClickListener {

        void onItemClick(int position);

        void onUpdateClick(int position);

        void onDeleteClick(int position);

        void onStatusChange(int position);

    }


    public void setOnItemClickListener(FoodAdapter.onItemClickListener listener) {


        mListener = listener;


    }
}
