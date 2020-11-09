package com.example.eatitadmin.ui.Menu;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitadmin.Adapters.CategoryAdapter;
import com.example.eatitadmin.Common.Common;
import com.example.eatitadmin.FoodListActivity;
import com.example.eatitadmin.HomeActivity;
import com.example.eatitadmin.Model.Category;
import com.example.eatitadmin.R;
import com.example.eatitadmin.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.widget.FButton;

import static android.app.Activity.RESULT_OK;

public class MenuFragment extends Fragment implements CategoryAdapter.onItemClickListener {

    DatabaseReference reference;
    StorageReference storageReference;
    MaterialEditText txtdialogname;
    FButton btnUpload, btnSelect;
    Uri saveUri;
    private final int PICK_IMAGE_REQ = 71;
    Category newCategory;
    String ID;


    RecyclerView menu_recyclerview;
    List<Category> categoryList;
    // CategoryAdapter adapter;
    ProgressDialog progressDialog;
    CategoryAdapter adapter;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_menu, container, false);

        progressDialog = new ProgressDialog(getActivity());

        reference = FirebaseDatabase.getInstance().getReference().child("Category");
        storageReference = FirebaseStorage.getInstance().getReference();


        menu_recyclerview = root.findViewById(R.id.main_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        menu_recyclerview.setLayoutManager(linearLayoutManager);
        categoryList = new ArrayList<>();
        adapter= new CategoryAdapter(categoryList,getContext());
        menu_recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(MenuFragment.this);
        getMenu();


        FloatingActionButton fab = root.findViewById(R.id.home_cart_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                showDialog();
            }
        });
        return root;
    }
    private void showDialog() {

        ID = reference.push().getKey();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add New Category");
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
        builder.setIcon(R.drawable.item);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



                if (newCategory != null) {

                    reference.child(ID).setValue(newCategory);
                    txtdialogname.setText("");
                    saveUri = null;
                    ID=null;

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

                        newCategory = new Category(txtdialogname.getText().toString(), myUrl, ID);

                        progressDialog.dismiss();


                        //pd.dismiss();


                    } else {

                        // pd.dismiss();
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });


        } else {
            //pd.dismiss();
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "No Image Selected", Toast.LENGTH_LONG).show();


        }

    }

    private String getExtensionFile(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void chooseImage() {

        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(MenuFragment.this.getContext(), MenuFragment.this);

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
                Toast.makeText(getContext(), result.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void getMenu() {
        reference= FirebaseDatabase.getInstance().getReference().child("Category");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();

                for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                    Category category=snapshot.getValue(Category.class);
                    category.setId(snapshot.getKey());
                    categoryList.add(category);

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

        Category selectedItem=categoryList.get(position);
        String selectedID=selectedItem.getId();
        Intent intent = new Intent(getActivity(), FoodListActivity.class);
        intent.putExtra("catID",selectedItem.getId());
        intent.putExtra("catName",selectedItem.getName());
        startActivity(intent);

    }

    @Override
    public void onUpdateClick(int position) {
        //Toast.makeText(getActivity(),"update position"+position,Toast.LENGTH_SHORT).show();
        Category selectedItem=categoryList.get(position);
        String selectedID=selectedItem.getId();
        showUpdateDialog(selectedID,selectedItem);

    }

    private void showUpdateDialog(final String selectedID, final Category selectedItem) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update Category Category");
        builder.setMessage("Please Fill Full Information");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_menu_layout = layoutInflater.inflate(R.layout.add_new_menu, null);

        txtdialogname = add_menu_layout.findViewById(R.id.add_name);
        btnSelect = add_menu_layout.findViewById(R.id.selectBtn);
        btnUpload = add_menu_layout.findViewById(R.id.uploadBtn);

        txtdialogname.setText(selectedItem.getName());

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

                dialog.dismiss();

                selectedItem.setName(txtdialogname.getText().toString());
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

    @Override
    public void onDeleteClick(int position) {
       //

        Category selectedItem=categoryList.get(position);
        String selectedID=selectedItem.getId();
      //  Toast.makeText(getActivity(),"delete"+selectedID,Toast.LENGTH_SHORT).show();


        final DatabaseReference food_ref=FirebaseDatabase.getInstance().getReference().child("Foods");
        Query query=food_ref.orderByChild("menuID").equalTo(selectedID);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot:dataSnapshot.getChildren()){


                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       reference.child(selectedID).removeValue();
    }
    private void changeImage(final Category item) {

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
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });


        } else {
            //pd.dismiss();
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "No Image Selected", Toast.LENGTH_LONG).show();


        }

    }

}