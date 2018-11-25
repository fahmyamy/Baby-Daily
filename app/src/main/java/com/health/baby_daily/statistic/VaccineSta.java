package com.health.baby_daily.statistic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.adapter.VaccineLogAdapter;
import com.health.baby_daily.event.vaccineEvent;
import com.health.baby_daily.model.Vaccine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VaccineSta extends AppCompatActivity {

    private FloatingActionButton add_vaccine;
    private RecyclerView entry_vaccineList;
    private VaccineLogAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView no_vaccine_text;
    private DatabaseReference table_vaccine;
    private Query query;

    private String bId;

    private List<Vaccine> mVaccineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine_sta);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Vaccine Log");

        SharedPreferences prefBaby = getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        Intent intent = getIntent();
        if (intent != null){
            bId = intent.getStringExtra("bId");
        }

        add_vaccine = findViewById(R.id.add_vaccine);
        add_vaccine.setVisibility(View.GONE);
        add_vaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VaccineSta.this, vaccineEvent.class));
            }
        });

        mVaccineList = new ArrayList<>();

        table_vaccine = FirebaseDatabase.getInstance().getReference().child("Vaccine");
        query = table_vaccine.orderByChild("dateTime");
        query.keepSynced(true);

        entry_vaccineList = findViewById(R.id.entry_vaccineList);
        entry_vaccineList.setHasFixedSize(true);
        no_vaccine_text = findViewById(R.id.no_vaccine_text);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new VaccineLogAdapter(mVaccineList);

        entry_vaccineList.setLayoutManager(mLayoutManager);
        entry_vaccineList.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new VaccineLogAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String id = mVaccineList.get(position).getId();
                String image = mVaccineList.get(position).getImage();
                Intent intent = new Intent(VaccineSta.this, VaccineStaEdit.class);
                intent.putExtra("id", id);
                intent.putExtra("imageId", image);
                startActivity(intent);
            }
        });

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mVaccineList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Vaccine vaccine = snapshot.getValue(Vaccine.class);
                        if (vaccine.getBaby_id().equals(bId)){
                            mVaccineList.add(vaccine);
                        }
                    }
                    no_vaccine_text.setVisibility(View.GONE);
                    Collections.reverse(mVaccineList);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
