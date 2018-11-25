package com.health.baby_daily;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import com.health.baby_daily.misc.CalorieActivity;
import com.health.baby_daily.misc.DiaryActivity;
import com.health.baby_daily.misc.OverallD;
import com.health.baby_daily.misc.PicknDrop;

public class MiscActivity extends AppCompatActivity {

    private CardView calorieView, diaryView, pickNDrop, overallView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misc);

        SharedPreferences prefBaby = getSharedPreferences("selectedBaby", 0);
        final int bAge = prefBaby.getInt("babyAge", -1);


        calorieView = findViewById(R.id.calorieView);
        calorieView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bAge > 1) {
                    startActivity(new Intent(MiscActivity.this, CalorieActivity.class));
                }else {
                    Toast.makeText(MiscActivity.this, "Unlock when baby reach 2 years old", Toast.LENGTH_SHORT).show();
                }
            }
        });


        diaryView = findViewById(R.id.diaryView);
        diaryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MiscActivity.this, DiaryActivity.class));
            }
        });


        pickNDrop = findViewById(R.id.pickNDrop);
        pickNDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MiscActivity.this, PicknDrop.class));
            }
        });

        overallView = findViewById(R.id.overallView);
        overallView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MiscActivity.this, OverallD.class));
            }
        });
    }
}
