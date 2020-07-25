package com.example.eatitadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eatitadmin.Adapters.BannerAdapter;
import com.example.eatitadmin.Adapters.CategoryAdapter;
import com.example.eatitadmin.Model.Banner;
import com.example.eatitadmin.Model.Category;
import com.example.eatitadmin.ui.Menu.MenuFragment;
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

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.widget.FButton;

public class BannerActivity extends AppCompatActivity implements BannerAdapter.onItemClickListener {

    DatabaseReference reference;
    StorageReference storageReference;
    MaterialEditText txtdialogname;
    FButton btnUpload, btnSelect;
    Uri saveUri;
    private final int PICK_IMAGE_REQ = 71;
    Banner newbanner;
    String ID;


    RecyclerView menu_recyclerview;
    List<Banner> bannerList;
    // CategoryAdapter adapter;
    ProgressDialog progressDialog;
    BannerAdapter adapter;
    ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);


        progressDialog = new ProgressDialog(this);

        reference = FirebaseDatabase.getInstance().getReference().child("Banner");
        storageReference = FirebaseStorage.getInstance().getReference();

        menu_recyclerview = findViewById(R.id.banner_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        menu_recyclerview.setLayoutManager(linearLayoutManager);
        bannerList = new ArrayList<>();
        adapter= new BannerAdapter(bannerList,this);
        menu_recyclerview.setAdapter(adapter);
        getMenu();
        adapter.setOnItemClickListener(BannerActivity.this);



        FloatingActionButton fab = findViewById(R.id.banner_add_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                showDialog();
            }
        });



        backBtn=findViewById(R.id.back_banner);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backBtn.setImageTintList(getResources().getColorStateList(R.color.green));
                finish();
            }
        });

    }

    private void getMenu() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bannerList.clear();

                for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                    Banner banner=snapshot.getValue(Banner.class);
                    banner.setBannerId(snapshot.getKey());
                    bannerList.add(banner);

                }
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void showDialog() {
        ID = reference.push().getKey();


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Banner");
        builder.setMessage("Please Fill Full Information");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_menu_layout = layoutInflater.inflate(R.layout.add_new_menu, null);

        txtdialogname = add_menu_layout.findViewById(R.id.add_name);
        btnSelect = add_menu_layout.findViewById(R.id.selectBtn);
        btnUpload = add_menu_layout.findViewById(R.id.uploadBtn);

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

        builder.setView(add_menu_layout);
        builder.setIcon(R.drawable.ic_banner);


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                if (newbanner != null) {

                    reference.child(ID).setValue(newbanner);
                    txtdialogname.setText("");
                    saveUri = null;
                    ID=null;


                }

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

                        newbanner = new Banner(txtdialogname.getText().toString(), myUrl, ID);

                        progressDialog.dismiss();


                        //pd.dismiss();


                    } else {

                        // pd.dismiss();
                        progressDialog.dismiss();
                        Toast.makeText(BannerActivity.this, "Failed", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressDialog.dismiss();
                    Toast.makeText(BannerActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });


        } else {
            //pd.dismiss();
            progressDialog.dismiss();
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_LONG).show();


        }

    }

    private String getExtensionFile(Uri uri) {
        ContentResolver contentResolver =getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void chooseImage() {

        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(BannerActivity.this);

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
                Toast.makeText(this, result.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onUpdateClick(int position) {

        Banner selectedItem=bannerList.get(position);
        String selectedID=selectedItem.getBannerId();
        showUpdateDialog(selectedID,selectedItem);

    }

    @Override
    public void onDeleteClick(int position) {

        Banner selectedItem=bannerList.get(position);
        String selectedID=selectedItem.getBannerId();

        reference.child(selectedID).removeValue();

        Toast.makeText(this,"delete banner"+selectedID,Toast.LENGTH_SHORT).show();


    }

    private void showUpdateDialog(final String selectedID, final Banner selectedItem) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Category Category");
        builder.setMessage("Please Fill Full Information");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_menu_layout = layoutInflater.inflate(R.layout.add_new_menu, null);

        txtdialogname = add_menu_layout.findViewById(R.id.add_name);
        btnSelect = add_menu_layout.findViewById(R.id.selectBtn);
        btnUpload = add_menu_layout.findViewById(R.id.uploadBtn);

        txtdialogname.setText(selectedItem.getBannerName());

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
        builder.setIcon(R.drawable.ic_banner);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                selectedItem.setBannerName(txtdialogname.getText().toString());
                reference.child(selectedID).setValue(selectedItem);

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

    private void changeImage(final Banner item) {

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
                        item.setBannerImage(myUrl);


                        progressDialog.dismiss();


                        //pd.dismiss();


                    } else {

                        // pd.dismiss();
                        progressDialog.dismiss();
                        Toast.makeText(BannerActivity.this, "Failed", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressDialog.dismiss();
                    Toast.makeText(BannerActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });


        } else {
            //pd.dismiss();
            progressDialog.dismiss();
            Toast.makeText(BannerActivity.this, "No Image Selected", Toast.LENGTH_LONG).show();


        }

    }

}
