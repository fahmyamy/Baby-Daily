package com.health.baby_daily.event;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.health.baby_daily.R;

public class sleepEvent extends AppCompatActivity {

    private Button autoButton, manualButton;
    private String bId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_event);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(" ");

        SharedPreferences prefBaby = getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        autoButton = findViewById(R.id.autoButton);
        manualButton = findViewById(R.id.manualButton);

        Bundle bundle = new Bundle();
        bundle.putString("bId", bId);
        Fragment fragmentA = new sleepFragmentA();
        fragmentA.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragmentA);
        ft.commit();

        autoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("bId", bId);
                Fragment fragmentA = new sleepFragmentA();
                fragmentA.setArguments(bundle);

                if (fragmentA != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frameLayout, fragmentA);
                    ft.commit();
                }
            }
        });
        manualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("bId", bId);
                Fragment fragmentB = new sleepFragmentB();
                fragmentB.setArguments(bundle);

                if (fragmentB != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frameLayout, fragmentB);
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
