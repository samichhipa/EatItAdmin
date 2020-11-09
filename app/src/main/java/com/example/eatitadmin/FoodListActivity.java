package com.example.eatitadmin;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitadmin.Adapters.FoodAdapter;
import com.example.eatitadmin.Model.Category;
import com.example.eatitadmin.Model.Foods;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.widget.FButton;

public class FoodListActivity extends AppCompatActivity implements FoodAdapter.onItemClickListener {

    DatabaseReference reference;
    StorageReference storageReference;
    MaterialEditText txtdialogname, txtdialog_desc, txtdialog_price, txtdialog_discount;
    FButton btnUpload, btnSelect;
    Uri saveUri;
    private final int PICK_IMAGE_REQ = 71;
    Foods newFoods;
    String FoodID;
    String categoryID,catName;


    RecyclerView foodlist_recyclerview;
    List<Foods> foodsList;
    // CategoryAdapter adapter;
    ProgressDialog progressDialog;
    FoodAdapter adapter;
    ImageView backBtn;
    TextView menu_NAME;
    Spinner spinner;
    ArrayAdapter spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);


        //SharedPreferences preferences = this.getSharedPreferences("cat", MODE_PRIVATE);
        //categoryID = preferences.getString("CategoryID", "");
        categoryID=getIntent().getStringExtra("catID");
        catName=getIntent().getStringExtra("catName");

        menu_NAME=findViewById(R.id.menu_name);

        menu_NAME.setText(catName);




        Toast.makeText(FoodListActivity.this, categoryID,Toast.LENGTH_LONG).show();


        progressDialog = new ProgressDialog(FoodListActivity.this);

        reference = FirebaseDatabase.getInstance().getReference().child("Foods");
        storageReference = FirebaseStorage.getInstance().getReference();



        foodlist_recyclerview = findViewById(R.id.foodlist_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FoodListActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        foodlist_recyclerview.setLayoutManager(linearLayoutManager);
        foodsList = new ArrayList<>();
        adapter = new FoodAdapter(foodsList, FoodListActivity.this);
        foodlist_recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(FoodListActivity.this);

        getMenu();


        FloatingActionButton fab = findViewById(R.id.foodlist_cart_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                showDialog();
            }
        });

        backBtn=findViewById(R.id.back_foodlist);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backBtn.setImageTintList(getResources().getColorStateList(R.color.green));
                finish();
            }
        });



    }

    private void showDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(FoodListActivity.this);
        builder.setTitle("Add New Food Item");
        builder.setMessage("Please Fill Full Information");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_food_layout = layoutInflater.inflate(R.layout.add_new_food, null);

        txtdialogname = add_food_layout.findViewById(R.id.foodlist_add_name);
        txtdialog_desc = add_food_layout.findViewById(R.id.foodlist_add_description);
        txtdialog_price = add_food_layout.findViewById(R.id.foodlist_add_price);
        txtdialog_discount = add_food_layout.findViewById(R.id.foodlist_add_discount);
        btnSelect = add_food_layout.findViewById(R.id.food_selectBtn);
        btnUpload = add_food_layout.findViewById(R.id.food_uploadBtn);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseImage();

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();

            }
        });

        builder.setView(add_food_layout);
        builder.setIcon(R.drawable.item);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {





                if (newFoods != null) {

                    reference.child(FoodID).setValue(newFoods);
                    txtdialogname.setText("");
                    txtdialog_desc.setText("");
                    txtdialog_price.setText("");
                    txtdialog_discount.setText("");
                    saveUri = null;
                    FoodID=null;


                }
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        builder.show();

    }

    private void uploadImage() {

        FoodID=reference.push().getKey();


        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        if (saveUri != null) {


            if (TextUtils.isEmpty(txtdialogname.getText().toString())){
                txtdialogname.setError("Enter Name");
                progressDialog.dismiss();

            }else if (TextUtils.isEmpty(txtdialog_desc.getText().toString())){

                txtdialog_desc.setError("Enter Description");
            }else if (TextUtils.isEmpty(txtdialog_price.getText().toString())){

                txtdialog_price.setError("Enter Price");
            }

            else {

                        final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                                + "." + getExtensionFile(saveUri));

                        UploadTask uploadTask = fileReference.putFile(saveUri);
                        uploadTask.continueWithTask(new Continuation() {
                            @Override
                            public Object then(@NonNull Task task) throws Exception {

                                if (!task.isComplete()) {

                                    throw task.getException();
                                }
                                return fileReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {

                                    Uri downloadUri = task.getResult();
                                    String myUrl = downloadUri.toString();

                                    newFoods = new Foods();
                                    newFoods.setName(txtdialogname.getText().toString());
                                    newFoods.setDescription(txtdialog_desc.getText().toString());
                                    newFoods.setPrice(txtdialog_price.getText().toString());
                                    newFoods.setDiscount("0");
                                    newFoods.setImage(myUrl);
                                    newFoods.setMenuID(categoryID);
                                    newFoods.setFoodID(FoodID);
                                    newFoods.setMenuName(catName);
                                    newFoods.setStatus("Enabled");

                                    progressDialog.dismiss();
                                    Toast.makeText(FoodListActivity.this, categoryID, Toast.LENGTH_SHORT).show();


                                } else {

                                    // pd.dismiss();
                                    progressDialog.dismiss();
                                    Toast.makeText(FoodListActivity.this, "Failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                progressDialog.dismiss();
                                Toast.makeText(FoodListActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        });
                    }

                } else {
                    //pd.dismiss();
                    progressDialog.dismiss();
                    Toast.makeText(FoodListActivity.this, "No Image Selected", Toast.LENGTH_LONG).show();

                }
    }

    private String getExtensionFile(Uri uri) {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void chooseImage() {

        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(FoodListActivity.this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 203) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == -1) {
                saveUri = result.getUri();
                //Glide.with(getContext()).load(this.imageUri).into(this.item_image);
            } else if (resultCode == 204) {
                Toast.makeText(getApplicationContext(), result.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void getMenu() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foodsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Foods foods = snapshot.getValue(Foods.class);
                    if (foods.getMenuID().equals(categoryID)) {

                        foods.setFoodID(snapshot.getKey());

                        foodsList.add(foods);
                    }

                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onItemClick(int position) {


    }


    @Override
    public void onUpdateClick(int position) {

        Foods selectedItem = foodsList.get(position);
        String selectedID = selectedItem.getFoodID();
        showUpdateDialog(selectedID, selectedItem);

    }

    private void showUpdateDialog(final String selectedID, final Foods selectedItem) {

        AlertDialog.Builder builder = new AlertDialog.Builder(FoodListActivity.this);
        builder.setTitle("Update Food Item");
        builder.setMessage("Please Fill Full Information");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_menu_layout = layoutInflater.inflate(R.layout.add_new_food, null);

        txtdialogname = add_menu_layout.findViewById(R.id.foodlist_add_name);
        txtdialog_desc = add_menu_layout.findViewById(R.id.foodlist_add_description);
        txtdialog_price = add_menu_layout.findViewById(R.id.foodlist_add_price);
        txtdialog_discount = add_menu_layout.findViewById(R.id.foodlist_add_discount);
        btnSelect = add_menu_layout.findViewById(R.id.food_selectBtn);
        btnUpload = add_menu_layout.findViewById(R.id.food_uploadBtn);

        txtdialogname.setText(selectedItem.getName());
        txtdialog_desc.setText(selectedItem.getDescription());
        txtdialog_price.setText(selectedItem.getPrice());
        txtdialog_discount.setText(selectedItem.getDiscount());

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseImage();

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeImage(selectedItem);

            }
        });

        builder.setView(add_menu_layout);
        builder.setIcon(R.drawable.item);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {




               // if (newFoods != null) {
                    selectedItem.setName(txtdialogname.getText().toString());
                    selectedItem.setDescription(txtdialog_desc.getText().toString());
                    selectedItem.setPrice(txtdialog_price.getText().toString());
                    selectedItem.setDiscount("0");
                    selectedItem.setStatus("Enabled");

                    reference.child(selectedID).setValue(selectedItem);


//                }
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        builder.show();
    }


    private void changeImage(final Foods item) {

        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        if (saveUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getExtensionFile(saveUri));

            UploadTask uploadTask = fileReference.putFile(saveUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if (!task.isComplete()) {

                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        Uri downloadUri = task.getResult();
                        String myUrl = downloadUri.toString();

                        //newCategory = new Category(txtdialogname.getText().toString(), myUrl, ID);
                        item.setImage(myUrl);


                        progressDialog.dismiss();


                        //pd.dismiss();


                    } else {

                        // pd.dismiss();
                        progressDialog.dismiss();
                        Toast.makeText(FoodListActivity.this, "Failed", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressDialog.dismiss();
                    Toast.makeText(FoodListActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });


        } else {
            //pd.dismiss();
            progressDialog.dismiss();
            Toast.makeText(FoodListActivity.this, "No Image Selected", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onDeleteClick(int position) {

        Foods selectedItem = foodsList.get(position);

        String selectedID = selectedItem.getFoodID();

        //Toast.makeText(FoodListActivity.this, "delete" + selectedID, Toast.LENGTH_SHORT).show();

       reference.child(selectedID).removeValue();
    }

    @Override
    public void onStatusChange(int position) {

        Foods selectedItem = foodsList.get(position);
        String selectedID = selectedItem.getFoodID();
        UpdateFoodStatusDialog(selectedID,selectedItem);

    }

    private void UpdateFoodStatusDialog(String selectedID, final Foods selectedItem) {

        final android.app.AlertDialog.Builder alertBuilder=new android.app.AlertDialog.Builder(this);
        alertBuilder.setTitle("Food Status");
        alertBuilder.setMessage("Please Choose Status");

        View view= LayoutInflater.from(this).inflate(R.layout.update_order_layout,null);


        spinner=view.findViewById(R.id.status);

        List<String> statusList=new ArrayList<>();
        statusList.add("Enabled");
        statusList.add("Disabled");

        spinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, statusList);
        spinner.setAdapter(this.spinnerAdapter);

        alertBuilder.setView(view);

        final String local_id=selectedID;

        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                selectedItem.setStatus(spinner.getSelectedItem().toString());

                reference.child(local_id).setValue(selectedItem);


            }
        });

        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        alertBuilder.show();
    }
}
