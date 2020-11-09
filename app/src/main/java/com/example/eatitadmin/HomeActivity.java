package com.example.eatitadmin;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.example.eatitadmin.Adapters.CategoryAdapter;
import com.example.eatitadmin.Common.Common;
import com.example.eatitadmin.Model.Admin;
import com.example.eatitadmin.Model.Category;
import com.example.eatitadmin.Model.Token;
import com.example.eatitadmin.Model.Users;
import com.example.eatitadmin.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import info.hoang8f.widget.FButton;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    TextView txtname;
    NavigationView navigationView;
    String tokenID;
    DatabaseReference token_ref;
    SharedPreferences.Editor editor;

    MaterialEditText txt_name, txt_pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        // toolbar.setTitle("Table Pay Admin");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //  getSupportActionBar().setDisplayShowTitleEnabled(true);
        //getSupportActionBar().setTitle("Menu");

     /*   DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_menu, R.id.nav_cart, R.id.nav_orders,
                R.id.nav_sign_out)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

      */
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        /*

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_menu, R.id.nav_cart, R.id.nav_orders,R.id.nav_cart, R.id.nav_sign_out)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.container_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



         */


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(HomeActivity.this);
        navigationView.getMenu().getItem(0).setChecked(true);

/*


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(HomeActivity.this);
        navigationView.getMenu().getItem(0).setChecked(true);


 */
        View headerView = navigationView.getHeaderView(0);
        // txtname = headerView.findViewById(R.id.home_txt_name);
        //txtname.setText(Common.currentUser.getName());


        token_ref = FirebaseDatabase.getInstance().getReference().child("Token");

        tokenID = FirebaseInstanceId.getInstance().getToken();

        Token token = new Token(tokenID, Common.currentUser.getPhone(), true);
        token_ref.child(Common.currentUser.getPhone()).setValue(token);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.nav_banner) {

            Intent intent = new Intent(HomeActivity.this, BannerActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_about) {

            Intent intent = new Intent(HomeActivity.this, AboutUsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_transactions) {

            CredentialDialog();

        } else if (id == R.id.nav_extras) {

            Intent intent = new Intent(HomeActivity.this, ExtraActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_orders) {

            Intent intent = new Intent(HomeActivity.this, OrderStatusActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_sign_out) {

            token_ref.child(Common.currentUser.getPhone()).child("tokenID").setValue("");

            editor = this.getSharedPreferences("ADMIN_LOGIN", MODE_PRIVATE).edit();
            editor.putString("Phone", "");
            editor.putString("Password", "");
            editor.commit();

            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }


        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);


        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences.Editor editor = this.getSharedPreferences("USER_INFO", MODE_PRIVATE).edit();
        editor.putString("current_id", Common.currentUser.getPhone());
        editor.apply();

    }

    private void CredentialDialog() {


        final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.credential_layout, null);

        txt_name = view.findViewById(R.id.auth_name);
        txt_pass = view.findViewById(R.id.auth_pass);
        Button btnAuth, btnCancel;
        btnAuth = view.findViewById(R.id.authBtn);
        btnCancel = view.findViewById(R.id.authBtnCancel);


        builder.setView(view);

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);


        btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name, pass;

                name = txt_name.getText().toString();
                pass = txt_pass.getText().toString();

                if (TextUtils.isEmpty(name)) {

                    txt_name.setError("Enter Username");

                } else if (TextUtils.isEmpty(pass)) {

                    txt_pass.setError("Enter Password");

                } else {

                    AdminLogin(name, pass);
                    alertDialog.dismiss();


                }


            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }

    private void AdminLogin(final String name, final String pass) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Admin");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Admin admin = dataSnapshot.child("111").getValue(Admin.class);


                if (admin.getName().equals(name) && admin.getPassword().equals(pass)) {

                    Intent intent = new Intent(HomeActivity.this, TransactionsActivity.class);
                    startActivity(intent);
                    txt_name.setText("");
                    txt_pass.setText("");


                } else {

                    Toast.makeText(HomeActivity.this, "Wrong UserName or Password", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
