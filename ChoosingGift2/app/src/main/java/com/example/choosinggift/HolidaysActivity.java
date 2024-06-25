package com.example.choosinggift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.choosinggift.Adapters.HolidayAdapter;
import com.example.choosinggift.Models.HolidaysModel;
import com.example.choosinggift.Models.HolllidayModel;
import com.example.choosinggift.databinding.ActivityHolidaysBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HolidaysActivity extends AppCompatActivity {

    ActivityHolidaysBinding binding;
    private ArrayList<HolllidayModel> list;
    private int count = 0;
    private int position = 0;
    private int score = 0;


    FirebaseDatabase database;
    String categoryName;
    private int set;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHolidaysBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();

        categoryName = getIntent().getStringExtra("categoryName");
        set = getIntent().getIntExtra("setNum", 1);

        list = new ArrayList<>();





        database.getReference().child("Sets").child(categoryName).child("questions")
                .orderByChild("setNum").equalTo(set)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {



                        if (snapshot.exists()) {

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                HolllidayModel model = dataSnapshot.getValue(HolllidayModel.class);
                                list.add(model);


                            }

                            if (list.size() > 0) {


                                for (int i = 0; i < 8; i++) {

                                    binding.optionContainers.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {


                                        }
                                    });
                                }

                                playAnimation(binding.question, 0, list.get(position).getHoliday());




                            } else {

                                Toast.makeText(HolidaysActivity.this, "Подарков не существует!", Toast.LENGTH_SHORT).show();

                            }


                        } else {

                            Toast.makeText(HolidaysActivity.this, "Подарки еще не созданы!", Toast.LENGTH_SHORT).show();
                        }


                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(HolidaysActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


    }


    private void enableOption(boolean enable) {

        for (int i = 0; i < 8; i++) {
            binding.optionContainers.getChildAt(i).setEnabled(enable);

            if (enable) {
                binding.optionContainers.getChildAt(i).setBackgroundResource(R.drawable.btn_option_bacl);
            }

        }


    }



    private void playAnimation(View view, int value, String data) {

        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animator) {

                        if (value == 0 && count < 8) {
                            String option = "";

                            if (count == 0) {

                                option = list.get(position).getOption1();

                            } else if (count == 7) {

                                option = list.get(position).getOption2();

                            } else if (count == 6) {

                                option = list.get(position).getOption3();

                            } else if (count == 5) {

                                option = list.get(position).getOption4();

                            } else if (count == 4) {

                                option = list.get(position).getOption5();
                            } else if (count == 3) {

                                option = list.get(position).getOption6();
                            } else if (count == 2) {

                                option = list.get(position).getOption7();
                            } else if (count == 1) {

                                option = list.get(position).getOption8();
                            }

                            playAnimation(binding.optionContainers.getChildAt(count), 0, option);
                            count++;

                        }
                        binding.btnQuit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(HolidaysActivity.this,MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            }
                        });

                        binding.btnRetry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                finish();

                            }
                        });

                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {

                        if (value == 0) {
                            try {

                                ((TextView) view).setText(data);


                            } catch (Exception e) {

                                ((Button) view).setText(data);

                            }

                            view.setTag(data);
                            playAnimation(view, 1, data);
                        }

                    }


                    @Override
                    public void onAnimationCancel(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animator) {

                    }
                });







    }

}





