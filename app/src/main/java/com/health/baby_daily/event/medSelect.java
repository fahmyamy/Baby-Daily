package com.health.baby_daily.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.health.baby_daily.R;

public class medSelect extends AppCompatActivity {

    private String bId;
    private ImageView medDetail, medEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_select);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        medDetail = findViewById(R.id.medDetail);
        medEvent = findViewById(R.id.medEvent);

        medDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(medSelect.this, medDetail.class);
                startActivity(intent);
            }
        });


        medEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(medSelect.this, medEvent.class);
                startActivity(intent);
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
