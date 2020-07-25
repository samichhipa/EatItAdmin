package com.example.eatitadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
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

public class SignInActivity extends AppCompatActivity {


    MaterialEditText phone, pass;
    Button signIn;
    DatabaseReference reference;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        phone = findViewById(R.id.txt_phone);
        pass = findViewById(R.id.txt_pass);
        signIn = findViewById(R.id.login_signInBtn);


        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {

                    if (Common.isConnectedToInternet(getBaseContext())) {

                        if (TextUtils.isEmpty(phone.getText().toString())) {

                            phone.setError("Enter Phone No.");
                        } else {

                            final ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this);
                            progressDialog.setMessage("Please Wait...");
                            progressDialog.show();
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                    if (dataSnapshot.child(phone.getText().toString()).exists()) {

                                        Users users = dataSnapshot.child(phone.getText().toString()).getValue(Users.class);

                                        users.setPhone(phone.getText().toString());
                                        if (users.getPassword().equals(pass.getText().toString())) {


                                            editor = getSharedPreferences("ADMIN_LOGIN", MODE_PRIVATE).edit();
                                            editor.putString("Phone", phone.getText().toString());
                                            editor.putString("Password", pass.getText().toString());
                                            editor.commit();


                                            progressDialog.dismiss();


                                            // Toast.makeText(SignInActivity.this, "Sign in Succesfull..",Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                            Common.currentUser = users;
                                            startActivity(intent);
                                            finish();


                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(SignInActivity.this, "Invalid Password..", Toast.LENGTH_SHORT).show();

                                        }
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignInActivity.this, "User not exists..", Toast.LENGTH_SHORT).show();


                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    } else {

                        Toast.makeText(SignInActivity.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();

                    }


                }


                return false;
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        if (dataSnapshot.child(phone.getText().toString()).exists()) {


                            Users users = dataSnapshot.child(phone.getText().toString()).getValue(Users.class);

                            users.setPhone(phone.getText().toString());

                            if (Boolean.parseBoolean(users.getIsStaff())) {

                                if (users.getPassword().equals(pass.getText().toString())) {


                                    editor = getSharedPreferences("ADMIN_LOGIN", MODE_PRIVATE).edit();
                                    editor.putString("Phone", phone.getText().toString());
                                    editor.putString("Password", pass.getText().toString());
                                    editor.commit();

                                    progressDialog.dismiss();
                                    // Toast.makeText(SignInActivity.this, "Sign in Succesfull..",Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                    Common.currentUser = users;
                                    startActivity(intent);
                                    finish();


                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(SignInActivity.this, "Invalid Password..", Toast.LENGTH_SHORT).show();

                                }

                            } else {


                                progressDialog.dismiss();
                                Toast.makeText(SignInActivity.this, "Please Login with staff account..", Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(SignInActivity.this, "User not exists..", Toast.LENGTH_SHORT).show();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
