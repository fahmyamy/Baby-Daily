package com.health.baby_daily.statistic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.health.baby_daily.R;

public class MedDSta extends AppCompatActivity {
    private Button btnInventory, btnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_dsta);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Medicine Log");

        btnInventory = findViewById(R.id.btnInventory);
        btnList = findViewById(R.id.btnList);

        FragmentTransaction frag = getSupportFragmentManager().beginTransaction();
        frag.replace(R.id.fragmentLayout, new MedStatisticFragList());
        frag.commit();

        btnInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Bundle bundle = new Bundle();
                //bundle.putString("bId", bId);
                Fragment inventory = new MedStatisticFragInventory();
                //list.setArguments(bundle);

                if (inventory != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragmentLayout, inventory);
                    ft.commit();
                }
            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Bundle bundle = new Bundle();
                //bundle.putString("bId", bId);
                Fragment list = new MedStatisticFragList();
                //list.setArguments(bundle);

                if (list != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragmentLayout, list);
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
