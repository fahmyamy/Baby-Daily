package com.health.baby_daily.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.health.baby_daily.R;

public class bottleSelect extends AppCompatActivity {

    private String bId, bName, bImage;
    private ImageView breastMilk, formulaMilk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottle_select);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        breastMilk = findViewById(R.id.breastMilk);
        formulaMilk = findViewById(R.id.formulaMilk);

        breastMilk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(bottleSelect.this, bottleEvent.class);
                intent.putExtra("type", "Breast");
                startActivity(intent);
            }
        });

        formulaMilk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(bottleSelect.this, bottleEvent.class);
                intent.putExtra("type", "Formula");
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
