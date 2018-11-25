package com.health.baby_daily.guide;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.statistic.AppointmentSta;
import com.health.baby_daily.statistic.BottleSta;
import com.health.baby_daily.statistic.DiaperSta;
import com.health.baby_daily.statistic.FeedSta;
import com.health.baby_daily.statistic.MeasurementSta;
import com.health.baby_daily.statistic.MedDSta;
import com.health.baby_daily.statistic.SleepSta;
import com.health.baby_daily.statistic.VaccineSta;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class BabyReport extends AppCompatActivity {

    private ImageView bottle, sleep, diaper, feed, medicine, measurement, vaccine, appointment, overall;
    private ImageView babyImage;
    private TextView babyName, babyAge;
    private int totalday, totalmonth, totalyear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_report);

        setTitle("Baby Report Menu");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        babyImage = findViewById(R.id.babyImage);
        babyName = findViewById(R.id.babyName);
        babyAge = findViewById(R.id.babyAge);

        SharedPreferences prefBaby = getSharedPreferences("selectedBaby", 0);
        String bId = prefBaby.getString("babyId", null);

        DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
        table_baby.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String username = Objects.requireNonNull(dataSnapshot.child("fullName").getValue()).toString();
                    String image = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();
                    String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                    if (image.equals("none")){
                        Picasso.get().load(R.drawable.user_image).into(babyImage);
                    }else {
                        Picasso.get().load(image).into(babyImage);
                    }

                    babyName.setText(username);

                    String[] datesplit = dob.split("/");
                    int day = Integer.parseInt(datesplit[0]);
                    int month = Integer.parseInt(datesplit[1]);
                    int year = Integer.parseInt(datesplit[2]);

                    Calendar calendar = Calendar.getInstance();
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String currentdate = dateFormat.format(calendar.getTime());
                    String[] currsplit = currentdate.split("/");
                    int currday = Integer.parseInt(currsplit[0]);
                    int currmonth = Integer.parseInt(currsplit[1]);
                    int curryear = Integer.parseInt(currsplit[2]);

                    //subtract date
                    if ((currday == day) || (currday > day)) {
                        totalday = currday - day;
                    } else if (currday < day) {
                        currday = currday + 31;
                        totalday = currday - day;
                        currmonth = currmonth - 1;
                    }

                    if ((currmonth == month) || (currmonth > month)) {
                        totalmonth = currmonth - month;
                        totalyear = curryear - year;
                    } else if (currmonth < month) {
                        currmonth = currmonth + 12;
                        totalmonth = currmonth - month;
                        curryear = curryear - 1;
                        totalyear = curryear - year;
                    }

                    final String age = totalyear + " Years " + totalmonth + " Months " + totalday + " Days ";
                    babyAge.setText(age);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bottle = findViewById(R.id.bottle);
        sleep = findViewById(R.id.sleep);
        diaper = findViewById(R.id.diaper);
        feed = findViewById(R.id.feed);
        medicine = findViewById(R.id.medicine);
        measurement = findViewById(R.id.measurement);
        vaccine = findViewById(R.id.vaccine);
        appointment = findViewById(R.id.calendar);
        overall = findViewById(R.id.overall);

        bottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BabyReport.this, BottleSta.class));
            }
        });

        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BabyReport.this, SleepSta.class));
            }
        });

        diaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BabyReport.this, DiaperSta.class));
            }
        });

        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BabyReport.this, FeedSta.class));
            }
        });

        medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BabyReport.this, MedDSta.class));
            }
        });

        measurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BabyReport.this, MeasurementSta.class));
            }
        });

        vaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BabyReport.this, VaccineSta.class));
            }
        });

        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BabyReport.this, AppointmentSta.class));
            }
        });

        overall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
