package com.example.eatitadmin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.eatitadmin.Model.About;
import com.example.eatitadmin.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class AboutAdapter extends SliderViewAdapter<AboutAdapter.ViewHolder> {


    List<About> aboutList;
    Context context;

    public AboutAdapter(List<About> aboutList, Context context) {
        this.aboutList = aboutList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.image_slider_layout_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        About about=aboutList.get(position);
        Glide.with(context).load(about.getImage()).into(viewHolder.img);

    }

    @Override
    public int getCount() {
        return aboutList.size();
    }

    class ViewHolder extends SliderViewAdapter.ViewHolder{

        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);


            img=itemView.findViewById(R.id.slider_img);
        }
    }
}
