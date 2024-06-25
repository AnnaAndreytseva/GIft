package com.example.gift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.gift.Models.HolidaysModel;
import com.example.gift.Models.HolllidayModel;
import com.example.gift.databinding.ActivityAddHolidaysBinding;
import com.example.gift.databinding.ActivityHolidaysBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class AddHolidaysActivity extends AppCompatActivity {

    ActivityAddHolidaysBinding binding;
    int set;
    String categoryName;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddHolidaysBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        set = getIntent().getIntExtra("setNum",-1);
        categoryName = getIntent().getStringExtra("category");

        database = FirebaseDatabase.getInstance();


        if (set==-1){

            finish();
            return;
        }

        binding.btnUploadQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int correct = -1;

                for (int i=0; i<binding.optionContainer.getChildCount();i++){

                    EditText answer = (EditText) binding.answerContainer.getChildAt(i);

                    if (answer.getText().toString().isEmpty()){
                        answer.setError("Не найден");
                        return;
                    }


                    RadioButton radioButton = (RadioButton) binding.optionContainer.getChildAt(i);

                    if (radioButton.isChecked()){

                        correct = i;
                        break;
                    }

                }




                HolllidayModel model = new HolllidayModel();


                model.setHoliday(binding.inputHolidays.getText().toString());
                model.setOption1(((EditText)binding.answerContainer.getChildAt(0)).getText().toString());
                model.setOption2(((EditText)binding.answerContainer.getChildAt(1)).getText().toString());
                model.setOption3(((EditText)binding.answerContainer.getChildAt(2)).getText().toString());
                model.setOption4(((EditText)binding.answerContainer.getChildAt(3)).getText().toString());
                model.setOption5(((EditText)binding.answerContainer.getChildAt(4)).getText().toString());
                model.setOption6(((EditText)binding.answerContainer.getChildAt(5)).getText().toString());
                model.setOption7(((EditText)binding.answerContainer.getChildAt(6)).getText().toString());
                model.setOption8(((EditText)binding.answerContainer.getChildAt(7)).getText().toString());
                model.setSetNum(set);


                database.getReference().child("Sets").child(categoryName).child("questions")
                        .push()
                        .setValue(model)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(AddHolidaysActivity.this, "Все подароки добавлены!", Toast.LENGTH_SHORT).show();

                            }
                        });


            }
        });

    }
}