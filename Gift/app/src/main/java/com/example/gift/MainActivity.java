package com.example.gift;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gift.Adapters.HolidayAdapter;
import com.example.gift.Models.HolidaysModel;
import com.example.gift.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    FirebaseStorage storage;
    CircleImageView categoryImage;
    EditText inputCategoryName;
    Button uploadCategory;
    View fetchImage;

    Dialog dialog;
    Uri imageUri;
    int i = 0;
    ArrayList<HolidaysModel>list;
    HolidayAdapter adapter;

    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        list = new ArrayList<>();

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_add_category_dialog);

        if (dialog.getWindow()!= null){

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);

        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Выполняется загрузка праздников!");
        progressDialog.setMessage("Пожалуйста, подождите! :)");

        uploadCategory = dialog.findViewById(R.id.btnUpload);
        inputCategoryName = dialog.findViewById(R.id.inputCategoryName);
        categoryImage = dialog.findViewById(R.id.categoryImage);
        fetchImage = dialog.findViewById(R.id.fetchImage);



        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.recyHoliday.setLayoutManager(layoutManager);

        adapter = new HolidayAdapter(this,list);
        binding.recyHoliday.setAdapter(adapter);



        database.getReference().child("categories").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    list.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                        list.add(new HolidaysModel(

                                dataSnapshot.child("categoryName").getValue().toString(),
                                dataSnapshot.child("categoryImage").getValue().toString(),
                                dataSnapshot.getKey(),
                                Integer.parseInt(dataSnapshot.child("setNum").getValue().toString())
                        ));

                    }

                    adapter.notifyDataSetChanged();

                }
                else{

                    Toast.makeText(MainActivity.this,"Праздника нет в базе данных!",Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });





        binding.addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });


        fetchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,1);


            }
        });

        uploadCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = inputCategoryName.getText().toString();
                if (imageUri == null){

                    Toast.makeText(MainActivity.this,"Пожалуйста, загрузите изображение прадзника!",Toast.LENGTH_SHORT).show();

                }
                else if (name.isEmpty()){

                    inputCategoryName.setError("Введите название праздника!");

                }
                else {

                    progressDialog.show();
                    uploadData();


                }

            }
        });


    }

    private void uploadData() {
    final StorageReference reference = storage.getReference().child("category")
            .child(new Date().getTime()+"");

        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    HolidaysModel holidaysModel= new HolidaysModel();
                    holidaysModel.setCategoryName(inputCategoryName.getText().toString());
                    holidaysModel.setSetNum(0);
                    holidaysModel.setCategoryImage(uri.toString());

                    database.getReference().child("categories").push()
                            .setValue(holidaysModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    Toast.makeText(MainActivity.this,"Праздники загружены!",Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                                    progressDialog.dismiss();


                                }
                            });


                }
            });


        }
    });

}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1){

            if(data != null){

                imageUri = data.getData();
                categoryImage.setImageURI(imageUri);

            }
        }



    }


}

