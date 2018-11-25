package com.health.baby_daily.statistic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.health.baby_daily.R;

public class AppointmentSta extends AppCompatActivity {

    private Button btnLog, btnQN;
    private String bId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_sta);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Hosp. Appointment");

        SharedPreferences prefBaby = getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        Intent intent = getIntent();
        if (intent != null){
            bId = intent.getStringExtra("bId");
            SharedPreferences pref = getSharedPreferences("selectedBaby", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("babyId", bId);
            editor.commit();
        }

        btnLog = findViewById(R.id.btnLog);
        btnQN = findViewById(R.id.btnQN);

        FragmentTransaction frag = getSupportFragmentManager().beginTransaction();
        frag.replace(R.id.fragmentLayout, new AppointmentStaLog());
        frag.commit();

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Bundle bundle = new Bundle();
                //bundle.putString("bId", bId);
                Fragment log = new AppointmentStaLog();
                //list.setArguments(bundle);

                if (log != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragmentLayout, log);
                    ft.commit();
                }
            }
        });

        btnQN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Bundle bundle = new Bundle();
                //bundle.putString("bId", bId);
                Fragment que = new AppointmentStaQuestions();
                //chart.setArguments(bundle);

                if (que != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragmentLayout, que);
                    ft.commit();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
