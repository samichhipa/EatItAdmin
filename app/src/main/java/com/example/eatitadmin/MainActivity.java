package com.example.eatitadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eatitadmin.Common.Common;
import com.example.eatitadmin.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import info.hoang8f.widget.FButton;

public class MainActivity extends AppCompatActivity {

    FButton signIn, signUp;
    ImageView slogan;
    String shared_phone, shared_pass;
    SharedPreferences preferences;
    DatabaseReference reference;
    ImageView logo;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        signIn = findViewById(R.id.signInBtn);


        slogan = findViewById(R.id.slogan);
        logo = findViewById(R.id.main_logo);


        slogan.setVisibility(View.VISIBLE);
        logo.setVisibility(View.VISIBLE);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  Intent intent=new Intent(MainActivity.this, SignInActivity.class);
                // startActivity(intent);


                slogan.setVisibility(View.GONE);
                logo.setVisibility(View.GONE);

                final Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                dialog.setContentView(R.layout.login_dialog);
                dialog.setCancelable(true);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
                window.setGravity(Gravity.CENTER);


                final MaterialEditText txt_table_id_L = dialog.findViewById(R.id.dialog_tableID_L);
                final MaterialEditText txt_password_L = dialog.findViewById(R.id.dialog_password_L);
                Button btnSignIn = dialog.findViewById(R.id.dialog_login_btn);
                ImageView cancelBtn = dialog.findViewById(R.id.login_cancel);


                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        dialog.dismiss();
                        slogan.setVisibility(View.VISIBLE);
                        logo.setVisibility(View.VISIBLE);

                    }
                });

                btnSignIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (Common.isConnectedToInternet(getBaseContext())) {

                            if (TextUtils.isEmpty(txt_table_id_L.getText().toString())) {

                                txt_table_id_L.setError("Enter Phone No.");
                            } else if (TextUtils.isEmpty(txt_password_L.getText().toString())) {

                                txt_password_L.setError("Enter Password");
                            } else {


                                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                                progressDialog.setMessage("Please Wait...");
                                progressDialog.show();
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                        if (dataSnapshot.child(txt_table_id_L.getText().toString()).exists()) {


                                            Users users = dataSnapshot.child(txt_table_id_L.getText().toString()).getValue(Users.class);

                                            users.setPhone(txt_table_id_L.getText().toString());

                                            if (Boolean.parseBoolean(users.getIsStaff())) {

                                                if (users.getPassword().equals(txt_password_L.getText().toString())) {


                                                    editor = getSharedPreferences("ADMIN_LOGIN", MODE_PRIVATE).edit();
                                                    editor.putString("Phone", txt_table_id_L.getText().toString());
                                                    editor.putString("Password", txt_password_L.getText().toString());
                                                    editor.commit();

                                                    progressDialog.dismiss();
                                                    dialog.dismiss();
                                                    slogan.setVisibility(View.VISIBLE);
                                                    logo.setVisibility(View.VISIBLE);
                                                    // Toast.makeText(SignInActivity.this, "Sign in Succesfull..",Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                                    Common.currentUser = users;
                                                    startActivity(intent);
                                                    finish();


                                                } else {
                                                    progressDialog.dismiss();
                                                    dialog.dismiss();
                                                    slogan.setVisibility(View.VISIBLE);
                                                    logo.setVisibility(View.VISIBLE);
                                                    Toast.makeText(MainActivity.this, "Invalid Password..", Toast.LENGTH_SHORT).show();

                                                }

                                            } else {


                                                progressDialog.dismiss();
                                                dialog.dismiss();
                                                slogan.setVisibility(View.VISIBLE);
                                                logo.setVisibility(View.VISIBLE);
                                                Toast.makeText(MainActivity.this, "Please Login with staff account..", Toast.LENGTH_SHORT).show();

                                            }


                                        } else {
                                            progressDialog.dismiss();

                                            dialog.dismiss();
                                            slogan.setVisibility(View.VISIBLE);
                                            logo.setVisibility(View.VISIBLE);
                                            Toast.makeText(MainActivity.this, "User not exists..", Toast.LENGTH_SHORT).show();


                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                        }else{

                            dialog.dismiss();
                            slogan.setVisibility(View.VISIBLE);
                            logo.setVisibility(View.VISIBLE);
                            Toast.makeText(MainActivity.this,"Please Check Internet Connection",Toast.LENGTH_SHORT).show();
                            return;


                        }
                    }
                });

                txt_password_L.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_GO) {

                            if (Common.isConnectedToInternet(getBaseContext())) {

                                if (TextUtils.isEmpty(txt_table_id_L.getText().toString())) {

                                    txt_table_id_L.setError("Enter Table ID");
                                } else {

                                    final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                                    progressDialog.setMessage("Please Wait...");
                                    progressDialog.show();
                                    reference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                            if (dataSnapshot.child(txt_table_id_L.getText().toString()).exists()) {

                                                Users users = dataSnapshot.child(txt_table_id_L.getText().toString()).getValue(Users.class);

                                                users.setPhone(txt_table_id_L.getText().toString());
                                                if (users.getPassword().equals(txt_password_L.getText().toString())) {


                                                    editor = getSharedPreferences("ADMIN_LOGIN", MODE_PRIVATE).edit();
                                                    editor.putString("Phone", txt_table_id_L.getText().toString());
                                                    editor.putString("Password", txt_password_L.getText().toString());
                                                    editor.commit();


                                                    progressDialog.dismiss();
                                                    dialog.dismiss();
                                                    slogan.setVisibility(View.VISIBLE);
                                                    logo.setVisibility(View.VISIBLE);


                                                    // Toast.makeText(SignInActivity.this, "Sign in Succesfull..",Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                                    Common.currentUser = users;
                                                    startActivity(intent);
                                                    finish();


                                                } else {

                                                    progressDialog.dismiss();
                                                    dialog.dismiss();
                                                    slogan.setVisibility(View.VISIBLE);
                                                    logo.setVisibility(View.VISIBLE);
                                                    Toast.makeText(MainActivity.this, "Invalid Password..", Toast.LENGTH_SHORT).show();

                                                }
                                            } else {

                                                progressDialog.dismiss();
                                                dialog.dismiss();
                                                slogan.setVisibility(View.VISIBLE);
                                                logo.setVisibility(View.VISIBLE);
                                                Toast.makeText(MainActivity.this, "User not exists..", Toast.LENGTH_SHORT).show();


                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            } else {


                                dialog.dismiss();
                                slogan.setVisibility(View.VISIBLE);
                                logo.setVisibility(View.VISIBLE);
                                Toast.makeText(MainActivity.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();

                            }


                        }


                        return false;
                    }
                });


                dialog.show();


            }
        });

    }

    private void sharedPref() {


        preferences = getSharedPreferences("ADMIN_LOGIN", MODE_PRIVATE);
        shared_phone = preferences.getString("Phone", "");
        shared_pass = preferences.getString("Password", "");

        if (!shared_phone.equals("") && !shared_pass.equals("")) {

            if (!shared_phone.equals(null) && !shared_pass.equals(null)) {


                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        if (dataSnapshot.child(shared_phone).exists()) {

                            Users users = dataSnapshot.child(shared_phone).getValue(Users.class);

                            users.setPhone(shared_phone);

                            if (Boolean.parseBoolean(users.getIsStaff())) {

                                if (users.getPassword().equals(shared_pass)) {


                                    progressDialog.dismiss();
                                    // Toast.makeText(SignInActivity.this, "Sign in Succesfull..",Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                    Common.currentUser = users;
                                    startActivity(intent);
                                    finish();


                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Invalid Password..", Toast.LENGTH_SHORT).show();

                                }

                            } else {


                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Please Login with staff account..", Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "User not exists..", Toast.LENGTH_SHORT).show();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }


        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        sharedPref();

    }
}
