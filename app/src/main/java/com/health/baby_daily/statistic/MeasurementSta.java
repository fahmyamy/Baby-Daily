package com.health.baby_daily.statistic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.health.baby_daily.R;

public class MeasurementSta extends AppCompatActivity {

    private Button btnList, btnW, btnH, btnHc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_sta);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Measure Statistic");

        btnList = findViewById(R.id.btnList);
        btnW = findViewById(R.id.btnWeight);
        btnH = findViewById(R.id.btnHeight);
        btnHc = findViewById(R.id.btnHead);

        FragmentTransaction frag = getSupportFragmentManager().beginTransaction();
        frag.replace(R.id.fragmentLayout, new MeasurementStaFragList());
        frag.commit();

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Bundle bundle = new Bundle();
                //bundle.putString("bId", bId);
                Fragment list = new MeasurementStaFragList();
                //list.setArguments(bundle);

                if (list != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragmentLayout, list);
                    ft.commit();
                }
            }
        });

        btnW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Bundle bundle = new Bundle();
                //bundle.putString("bId", bId);
                Fragment weight = new MeasurementStaFragWeight();
                //chart.setArguments(bundle);

                if (weight != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragmentLayout, weight);
                    ft.commit();
                }
            }
        });

        btnH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Bundle bundle = new Bundle();
                //bundle.putString("bId", bId);
                Fragment height = new MeasurementStaFragHeight();
                //chart.setArguments(bundle);

                if (height != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragmentLayout, height);
                    ft.commit();
                }
            }
        });

        btnHc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Bundle bundle = new Bundle();
                //bundle.putString("bId", bId);
                Fragment head = new MeasurementStaFragHead();
                //chart.setArguments(bundle);

                if (head != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragmentLayout, head);
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
