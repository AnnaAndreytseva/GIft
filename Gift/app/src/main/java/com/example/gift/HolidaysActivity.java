package com.example.gift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.gift.Adapters.HolidayAdapter;
import com.example.gift.Adapters.HolllidaysAdapter;
import com.example.gift.Models.HolidaysModel;
import com.example.gift.Models.HolllidayModel;
import com.example.gift.databinding.ActivityHolidaysBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HolidaysActivity extends AppCompatActivity {

    ActivityHolidaysBinding binding;
    FirebaseDatabase database;
    ArrayList<HolllidayModel> list;
    HolllidaysAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHolidaysBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();

        list = new ArrayList<>();


        int setNum = getIntent().getIntExtra("setNum",0);
        String categoryName = getIntent().getStringExtra("categoryName");



        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyHolidays.setLayoutManager(layoutManager);


        adapter = new HolllidaysAdapter(this, list, categoryName, new HolllidaysAdapter.DeleteListener() {
            @Override
            public void onLongClick(int position, String id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(HolidaysActivity.this);
                builder.setTitle("Удалить категорию подарков?");
                builder.setMessage("Вы уверены, что хотите удалить данную категорию подарков?");

                builder.setPositiveButton("Да",(dialog, which) -> {

                    database.getReference().child("Sets").child(categoryName).child("questions")
                            .child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    Toast.makeText(HolidaysActivity.this, "Категория подарков удалена!", Toast.LENGTH_SHORT).show();

                                }
                            });

                });

                builder.setNegativeButton("Нет",(dialog, which) -> {

                    dialog.dismiss();

                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        binding.recyHolidays.setAdapter(adapter);

        database.getReference().child("Sets").child(categoryName).child("questions")
                .orderByChild("setNum").equalTo(setNum)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()){

                            for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                                HolllidayModel model = dataSnapshot.getValue(HolllidayModel.class);
                                model.setKey(dataSnapshot.getKey());
                                list.add(model);

                            }

                            adapter.notifyDataSetChanged();


                        }
                        else {

                            Toast.makeText(HolidaysActivity.this, "Подарков не существует!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.addHolidays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HolidaysActivity.this,AddHolidaysActivity.class);
                intent.putExtra("category",categoryName);
                intent.putExtra("setNum",setNum);
                startActivity(intent);

            }
        });

    }
}