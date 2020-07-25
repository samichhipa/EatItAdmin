package com.example.eatitadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitadmin.Common.Common;
import com.example.eatitadmin.FoodListActivity;
import com.example.eatitadmin.Model.Category;
import com.example.eatitadmin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    List<Category> categoryList;
    Context context;
    onItemClickListener mListener;

    public CategoryAdapter(List<Category> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Category category = categoryList.get(position);

        Picasso.get().load(category.getImage()).into(holder.img);
        holder.txt.setText(category.getName());



    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        ImageView img;
        TextView txt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            img = itemView.findViewById(R.id.menu_img);
            txt = itemView.findViewById(R.id.menu_txt);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
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

            update.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
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


                    }

                }

            }
            return false;
        }
    }

    public interface onItemClickListener {

        void onItemClick(int position);

        void onUpdateClick(int position);

        void onDeleteClick(int position);

    }

    public void setOnItemClickListener(onItemClickListener listener) {


        mListener = listener;


    }
}
