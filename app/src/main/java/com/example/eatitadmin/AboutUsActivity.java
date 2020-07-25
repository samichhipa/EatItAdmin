package com.example.eatitadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.eatitadmin.Adapters.AboutAdapter;
import com.example.eatitadmin.Model.About;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class AboutUsActivity extends AppCompatActivity {


    List<About> aboutList;
    AboutAdapter adapter;
    SliderView sliderView;


    ImageView backBtn,chef1_img,chef2_img,chef3_img;

    String chef1="https://firebasestorage.googleapis.com/v0/b/eatitserver-3fac9.appspot.com/o/chef1.jpg?alt=media&token=4b7e20ab-de98-4810-b8f6-2bc036b92ac9";
    String chef2="https://firebasestorage.googleapis.com/v0/b/eatitserver-3fac9.appspot.com/o/chef2.jpg?alt=media&token=d9a98e2a-ace2-4329-b50a-b642fc866a0b";
    String chef3="https://firebasestorage.googleapis.com/v0/b/eatitserver-3fac9.appspot.com/o/chef3.jpg?alt=media&token=47a3bfec-67db-45b2-beb3-abe522cd2813";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        backBtn = findViewById(R.id.back_aboutUs);
        chef1_img=findViewById(R.id.chef_img_1);
        chef2_img=findViewById(R.id.chef_img_2);
        chef3_img=findViewById(R.id.chef_img_3);

        Glide.with(this).load(chef1).into(chef1_img);
        Glide.with(this).load(chef2).into(chef2_img);
        Glide.with(this).load(chef3).into(chef3_img);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backBtn.setImageTintList(getResources().getColorStateList(R.color.green));
                finish();
            }
        });


        sliderView = findViewById(R.id.aboutus_imageSlider);
        aboutList = new ArrayList<>();
        getImages();

        sliderView.setIndicatorAnimation(IndicatorAnimations.DROP);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
    }

    private void getImages() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("About");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    About about = snapshot.getValue(About.class);
                    aboutList.add(about);

                }
                adapter = new AboutAdapter(aboutList, AboutUsActivity.this);
                sliderView.setSliderAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Glide.with(this).load(chef1).into(chef1_img);
        Glide.with(this).load(chef2).into(chef2_img);
        Glide.with(this).load(chef3).into(chef3_img);
    }
}
