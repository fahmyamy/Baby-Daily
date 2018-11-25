package com.health.baby_daily.misc;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Measure;
import android.net.ParseException;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.model.Bottle;
import com.health.baby_daily.model.Diaper;
import com.health.baby_daily.model.Feed;
import com.health.baby_daily.model.Measurement;
import com.health.baby_daily.model.Medicine;
import com.health.baby_daily.model.Sleep;
import com.health.baby_daily.model.Vaccine;
import com.health.baby_daily.statistic.DiaperStaFragChart;
import com.health.baby_daily.statistic.FeedStaFragChart;
import com.health.baby_daily.statistic.SleepStaFragChart;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class OverallD extends AppCompatActivity {

    private ImageView onem, twom, threem, fourm, fivem, sixm, sevenm, eightm, ninem, tenm, elevenm, oney,
                        oneoney, onetwoy, onethreey, onefoury, onefivey, onesixy, oneseveny, oneeighty, oneniney, oneteny,
                        oneeleveny, twoy;

    private ImageView btnRight, btnLeft;

    private HorizontalScrollView hsview;

    private CardView cardBottle, cardSleep, cardDiaper, cardFeed, cardMeasure, cardVaccine;
    private TextView totalBm, totalFm, totalSleep, totalPee, totalPoop, totalFood, totalW, totalH, totalHc, vaccineName;
    private ImageView vaccineImage;

    private int checker = 1;
    private float bottle_countBm = 0.0f, bottle_countFm = 0.0f, count_hours = 0.0f, countFood = 0.0f;
    int countP = 0, countPo = 0;
    private String bId;

    private DatabaseReference table_bottle, table_sleep, table_diaper, table_feed, table_measure, table_vaccine;
    private Query query_bottle, query_sleep, query_diaper, query_feed, query_measure, query_vaccine;
    private Date dateRecord;

    private static DecimalFormat df = new DecimalFormat(".##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overall_d);

        setTitle("Overall Development");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefBaby = getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        hsview = findViewById(R.id.hsview);
        hsview.setHorizontalScrollBarEnabled(false);

        //declaration
        totalBm = findViewById(R.id.totalBm);
        totalFm = findViewById(R.id.totalFm);
        totalSleep = findViewById(R.id.totalSleep);
        totalPee = findViewById(R.id.totalPee);
        totalPoop = findViewById(R.id.totalPoop);
        totalFood = findViewById(R.id.totalFood);
        totalH = findViewById(R.id.totalH);
        totalW = findViewById(R.id.totalW);
        totalHc = findViewById(R.id.totalHc);
        vaccineName = findViewById(R.id.vaccineNmae);
        vaccineImage = findViewById(R.id.vaccineImage);

        cardFeed = findViewById(R.id.cardFeed);

        table_bottle = FirebaseDatabase.getInstance().getReference().child("Bottle");
        query_bottle = table_bottle.orderByChild("babyId").equalTo(bId);

        table_sleep = FirebaseDatabase.getInstance().getReference().child("Sleep");
        query_sleep = table_sleep.orderByChild("babyId").equalTo(bId);

        table_diaper = FirebaseDatabase.getInstance().getReference().child("Diaper");
        query_diaper = table_diaper.orderByChild("babyId").equalTo(bId);

        table_feed = FirebaseDatabase.getInstance().getReference().child("Feed");
        query_feed = table_feed.orderByChild("baby_id").equalTo(bId);

        table_measure = FirebaseDatabase.getInstance().getReference().child("Measurement");
        query_measure = table_measure.orderByChild("baby_id").equalTo(bId);

        table_vaccine = FirebaseDatabase.getInstance().getReference().child("Vaccine");
        query_vaccine = table_vaccine.orderByChild("baby_id").equalTo(bId);

        btnRight = findViewById(R.id.btnRight);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hsview.pageScroll(View.FOCUS_RIGHT);
                btnRight.setImageResource(R.drawable.click_scroll_right);
                btnLeft.setImageResource(R.drawable.scroll_left);
            }
        });

        btnLeft = findViewById(R.id.btnLeft);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hsview.pageScroll(View.FOCUS_LEFT);
                btnRight.setImageResource(R.drawable.scroll_right);
                btnLeft.setImageResource(R.drawable.click_scroll_left);
            }
        });

        onem = findViewById(R.id.onem);
        Glide.with(getApplicationContext()).load(R.drawable.clickonem).into(onem);

        twom = findViewById(R.id.twom);
        Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);

        threem = findViewById(R.id.threem);
        Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);

        fourm = findViewById(R.id.fourm);
        Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);

        fivem = findViewById(R.id.fivem);
        Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);

        sixm = findViewById(R.id.sixm);
        Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);

        sevenm = findViewById(R.id.sevenm);
        Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);

        eightm = findViewById(R.id.eightm);
        Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);

        ninem = findViewById(R.id.ninem);
        Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);

        tenm = findViewById(R.id.tenm);
        Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);

        elevenm = findViewById(R.id.elevenm);
        Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);

        oney = findViewById(R.id.oney);
        Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);

        oneoney = findViewById(R.id.oneoney);
        Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);

        onetwoy = findViewById(R.id.onetwoy);
        Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);

        onethreey = findViewById(R.id.onethreey);
        Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);

        onefoury = findViewById(R.id.onefoury);
        Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);

        onefivey = findViewById(R.id.onefivey);
        Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);

        onesixy = findViewById(R.id.onesixy);
        Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);

        oneseveny = findViewById(R.id.oneseveny);
        Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);

        oneeighty = findViewById(R.id.oneeighty);
        Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);

        oneniney = findViewById(R.id.oneniney);
        Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);

        oneteny = findViewById(R.id.oneteny);
        Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);

        oneeleveny = findViewById(R.id.oneeleveny);
        Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);

        twoy = findViewById(R.id.twoy);
        Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

        DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
        table_baby.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                    contentGenerator(1, dob);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        onem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.clickonem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(1, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        twom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.onem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.clicktwom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(2, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        threem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.onem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.clickthreem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(3, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        fourm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.onem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.clickfourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(4, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        fivem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.onem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.clickfivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(5, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        sixm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.onem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.clicksixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(6, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        sevenm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.onem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.clicksevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(7, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        eightm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.onem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.clickeightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(8, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        ninem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.onem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.clickninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(9, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        tenm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.onem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.clicktenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(10, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        elevenm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.onem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.clickelevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(11, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        oney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.onem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.clickoney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(12, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        oneoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.onem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.clickoneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(13, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        onetwoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.onem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.clickonetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(14, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        onethreey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.onem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.clickonethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(15, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        onefoury.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.onem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.clickonefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(16, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        onefivey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.onem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.clickonefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(17, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        onesixy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.onem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.clickonesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(18, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        oneseveny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.onem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.clickoneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(19, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        oneeighty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.onem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.clickoneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(20, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        oneniney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.onem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.clickoneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(21, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        oneteny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.onem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.clickoneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(22, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        oneeleveny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.onem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.clickoneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.twoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(23, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        twoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(R.drawable.onem).into(onem);
                Glide.with(getApplicationContext()).load(R.drawable.twom).into(twom);
                Glide.with(getApplicationContext()).load(R.drawable.threem).into(threem);
                Glide.with(getApplicationContext()).load(R.drawable.fourm).into(fourm);
                Glide.with(getApplicationContext()).load(R.drawable.fivem).into(fivem);
                Glide.with(getApplicationContext()).load(R.drawable.sixm).into(sixm);
                Glide.with(getApplicationContext()).load(R.drawable.sevenm).into(sevenm);
                Glide.with(getApplicationContext()).load(R.drawable.eightm).into(eightm);
                Glide.with(getApplicationContext()).load(R.drawable.ninem).into(ninem);
                Glide.with(getApplicationContext()).load(R.drawable.tenm).into(tenm);
                Glide.with(getApplicationContext()).load(R.drawable.elevenm).into(elevenm);
                Glide.with(getApplicationContext()).load(R.drawable.oney).into(oney);
                Glide.with(getApplicationContext()).load(R.drawable.oneoney).into(oneoney);
                Glide.with(getApplicationContext()).load(R.drawable.onetwoy).into(onetwoy);
                Glide.with(getApplicationContext()).load(R.drawable.onethreey).into(onethreey);
                Glide.with(getApplicationContext()).load(R.drawable.onefoury).into(onefoury);
                Glide.with(getApplicationContext()).load(R.drawable.onefivey).into(onefivey);
                Glide.with(getApplicationContext()).load(R.drawable.onesixy).into(onesixy);
                Glide.with(getApplicationContext()).load(R.drawable.oneseveny).into(oneseveny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeighty).into(oneeighty);
                Glide.with(getApplicationContext()).load(R.drawable.oneniney).into(oneniney);
                Glide.with(getApplicationContext()).load(R.drawable.oneteny).into(oneteny);
                Glide.with(getApplicationContext()).load(R.drawable.oneeleveny).into(oneeleveny);
                Glide.with(getApplicationContext()).load(R.drawable.clicktwoy).into(twoy);

                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                table_baby.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String dob = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();

                            contentGenerator(24, dob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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

    public void contentGenerator(final int checker, String dob){
        bottle_countBm = 0.0f;
        bottle_countFm = 0.0f;
        totalBm.setText("0.0 ml");
        totalFm.setText("0.0 ml");
        totalSleep.setText("0.0 hours");
        totalPee.setText("0 times");
        totalPoop.setText("0 times");
        totalFood.setText("0.0 g");
        totalW.setText("0 kg");
        totalH.setText("0 kg");
        totalHc.setText("0 kg");
        vaccineName.setText("No Record");

        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date convert2Date = new Date();
        try {
            convert2Date = dateFormat.parse(dob);
        }catch (java.text.ParseException e) {

            e.printStackTrace();
        }

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(convert2Date);
        calendar.add(Calendar.MONTH, checker);
        String endDate = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.MONTH, -1);
        String startDate = dateFormat.format(calendar.getTime());

        Date dateStart = new Date();
        Date dateEnd = new Date();

        try {
            dateStart = dateFormat.parse(startDate);
            dateEnd = dateFormat.parse(endDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        //Bottle
        final Date finalDateStart = dateStart;
        final Date finalDateEnd = dateEnd;
        query_bottle.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    float mLValueB = 0.0f;
                    float ozValueB = 0.0f;
                    float mLValueF = 0.0f;
                    float ozValueF = 0.0f;
                    float ozAmtB = 0.0f;
                    float ozAmtF = 0.0f;
                    for (DataSnapshot snapshot_bottle : dataSnapshot.getChildren()){
                        Bottle bottle = snapshot_bottle.getValue(Bottle.class);
                        String dateTime = bottle.getDateTime();

                        String[] currsplit = dateTime.split(" ");
                        String recordDate = currsplit[0];

                        calendar.setTime(finalDateStart);
                        while (calendar.getTime().before(finalDateEnd)){
                            calendar.add(Calendar.DATE, 1);
                            String inDate = dateFormat.format(calendar.getTime());

                            if (recordDate.equals(dateFormat.format(finalDateStart))){
                                if (bottle.getType().equals("Breast")){
                                    if(bottle.getUnits().equals("mL")){
                                        mLValueB = mLValueB + Float.valueOf(bottle.getAmount());
                                    }else if(bottle.getUnits().equals("Oz")){
                                        ozAmtB = Float.valueOf(bottle.getAmount())*29.57f;
                                        ozValueB = ozValueB + ozAmtB;
                                    }

                                    bottle_countBm = mLValueB+ozValueB;
                                }else if (bottle.getType().equals("Formula")){
                                    if(bottle.getUnits().equals("mL")){
                                        mLValueF = mLValueF + Float.valueOf(bottle.getAmount());
                                    }else if(bottle.getUnits().equals("Oz")){
                                        ozAmtF = Float.valueOf(bottle.getAmount())*29.57f;
                                        ozValueF = ozValueF + ozAmtF;
                                    }

                                    bottle_countFm = mLValueF+ozValueF;
                                }
                            }else if (recordDate.equals(inDate)){
                                if (bottle.getType().equals("Breast")){
                                    if(bottle.getUnits().equals("mL")){
                                        mLValueB = mLValueB + Float.valueOf(bottle.getAmount());
                                    }else if(bottle.getUnits().equals("Oz")){
                                        ozAmtB = Float.valueOf(bottle.getAmount())*29.57f;
                                        ozValueB = ozValueB + ozAmtB;
                                    }

                                    bottle_countBm = mLValueB+ozValueB;
                                }else if (bottle.getType().equals("Formula")){
                                    if(bottle.getUnits().equals("mL")){
                                        mLValueF = mLValueF + Float.valueOf(bottle.getAmount());
                                    }else if(bottle.getUnits().equals("Oz")){
                                        ozAmtF = Float.valueOf(bottle.getAmount())*29.57f;
                                        ozValueF = ozValueF + ozAmtF;
                                    }

                                    bottle_countFm = mLValueF+ozValueF;
                                }
                            }
                        }
                    }

                    totalBm.setText(df.format(bottle_countBm) + " ml");
                    totalFm.setText(df.format(bottle_countFm) + " ml");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Sleep
        final DateFormat subPattern = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        query_sleep.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count_hours = 0.0f;
                Date record_dateStart = null;
                Date record_dateEnd = null;
                float total = 0f;
                float toHours = 0f;
                long diffMinutes = 0, diffHours = 0, toMin = 0;

                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        total = 0;
                        toHours = 0;
                        Sleep sleep = snapshot.getValue(Sleep.class);
                        calendar.setTime(finalDateStart);
                        while (calendar.getTime().before(finalDateEnd)){
                            calendar.add(Calendar.DATE, 1);
                            String inDate = dateFormat.format(calendar.getTime());
                            if (sleep.getCreated().equals(dateFormat.format(finalDateStart))){
                                String startDate = sleep.getStartTime().toString();
                                String endDate = sleep.getEndTime().toString();

                                try {
                                    record_dateStart = subPattern.parse(startDate);
                                    record_dateEnd = subPattern.parse(endDate);

                                    long diff = record_dateEnd.getTime() - record_dateStart.getTime();
                                    diffHours = diff / 3600000;
                                    diffMinutes = (diff % 3600000) / 60000;
                                    if(diffHours>0){
                                        toMin = diffHours*60;
                                    }
                                } catch (java.text.ParseException e) {
                                    e.printStackTrace();
                                }
                                total = toMin + diffMinutes;
                                toHours = total * 0.0166667f;
                                count_hours = count_hours + toHours;
                            }else if (sleep.getCreated().equals(inDate)){
                                String startDate = sleep.getStartTime().toString();
                                String endDate = sleep.getEndTime().toString();

                                try {
                                    record_dateStart = subPattern.parse(startDate);
                                    record_dateEnd = subPattern.parse(endDate);

                                    long diff = record_dateEnd.getTime() - record_dateStart.getTime();
                                    diffHours = diff / 3600000;
                                    diffMinutes = (diff % 3600000) / 60000;
                                    if(diffHours>0){
                                        toMin = diffHours*60;
                                    }
                                } catch (java.text.ParseException e) {
                                    e.printStackTrace();
                                }
                                total = toMin + diffMinutes;
                                toHours = total * 0.0166667f;
                                count_hours = count_hours + toHours;
                            }
                        }
                    }
                    totalSleep.setText(df.format(count_hours) + " hours");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Diaper
        query_diaper.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                countP = 0;
                countPo = 0;
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Diaper diaper = snapshot.getValue(Diaper.class);
                        String dateTime = diaper.getDateTime();

                        String[] currsplit = dateTime.split(" ");
                        String recordDate = currsplit[0];

                        calendar.setTime(finalDateStart);
                        while (calendar.getTime().before(finalDateEnd)){
                            calendar.add(Calendar.DATE, 1);
                            String inDate = dateFormat.format(calendar.getTime());

                            if (recordDate.equals(dateFormat.format(finalDateStart))){
                                if (diaper.getType().equals("Pee")) {
                                    countP++;
                                }

                                if (diaper.getType().equals("Poop")) {
                                    countPo++;
                                }

                                if (diaper.getType().equals("Peep and Poop")) {
                                    countP++;
                                    countPo++;
                                }
                            }else if (recordDate.equals(inDate)){
                                if (diaper.getType().equals("Pee")) {
                                    countP++;
                                }

                                if (diaper.getType().equals("Poop")) {
                                    countPo++;
                                }

                                if (diaper.getType().equals("Peep and Poop")) {
                                    countP++;
                                    countPo++;
                                }
                            }
                        }
                    }

                    totalPee.setText(countP + " times");
                    totalPoop.setText(countPo + " times");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Feed
        query_feed.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                countFood = 0.0f;
                if(dataSnapshot.exists()){
                    float mLValue = 0.0f;  float ozAmt = 0.0f;
                    float ozValue = 0.0f;

                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Feed feed = snapshot.getValue(Feed.class);
                        String dateTime = feed.getDateTime();

                        String[] currsplit = dateTime.split(" ");
                        String recordDate = currsplit[0];

                        calendar.setTime(finalDateStart);
                        while (calendar.getTime().before(finalDateEnd)) {
                            calendar.add(Calendar.DATE, 1);
                            String inDate = dateFormat.format(calendar.getTime());

                            if (recordDate.equals(dateFormat.format(finalDateStart))){
                                if(feed.getUnits().equals("g")){
                                    mLValue = mLValue + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmt = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValue = ozValue + ozAmt;
                                }

                                countFood = mLValue+ozValue;
                            }else if (recordDate.equals(inDate)){
                                if(feed.getUnits().equals("g")){
                                    mLValue = mLValue + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmt = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValue = ozValue + ozAmt;
                                }

                                countFood = mLValue+ozValue;
                            }
                        }
                    }

                    totalFood.setText(df.format(countFood) + " g");
                }else if (!dataSnapshot.exists()){
                    cardFeed.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Measurement
        query_measure.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String weight = null, height = null, head = null;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Measurement measurement = snapshot.getValue(Measurement.class);
                        String dateTime = measurement.getDateTime();

                        String[] currsplit = dateTime.split(" ");
                        String recordDate = currsplit[0];

                        calendar.setTime(finalDateStart);
                        while (calendar.getTime().before(finalDateEnd)) {
                            calendar.add(Calendar.DATE, 1);
                            String inDate = dateFormat.format(calendar.getTime());

                            if (recordDate.equals(dateFormat.format(finalDateStart))){
                                weight = Objects.requireNonNull(snapshot.child("weight").getValue()).toString();
                                height = Objects.requireNonNull(snapshot.child("height").getValue()).toString();
                                head = Objects.requireNonNull(snapshot.child("headC").getValue()).toString();

                                totalW.setText(weight + " kg");
                                totalH.setText(height + " cm");
                                totalHc.setText(head + " cm");
                            }else if (recordDate.equals(inDate)){
                                weight = Objects.requireNonNull(snapshot.child("weight").getValue()).toString();
                                height = Objects.requireNonNull(snapshot.child("height").getValue()).toString();
                                head = Objects.requireNonNull(snapshot.child("headC").getValue()).toString();

                                totalW.setText(weight + " kg");
                                totalH.setText(height + " cm");
                                totalHc.setText(head + " cm");
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Vaccine
        query_vaccine.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String vaccName = null, image = null;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Vaccine vaccine = snapshot.getValue(Vaccine.class);
                        String dateTime = vaccine.getDateTime();

                        String[] currsplit = dateTime.split(" ");
                        String recordDate = currsplit[0];

                        calendar.setTime(finalDateStart);
                        while (calendar.getTime().before(finalDateEnd)) {
                            calendar.add(Calendar.DATE, 1);
                            String inDate = dateFormat.format(calendar.getTime());

                            if (recordDate.equals(dateFormat.format(finalDateStart))){
                                vaccName = snapshot.child("vaccineName").getValue().toString();
                                image = snapshot.child("image").getValue().toString();

                                vaccineName.setText(vaccName);
                                Glide.with(OverallD.this).load(image).into(vaccineImage);
                            }else if (recordDate.equals(inDate)){
                                vaccName = snapshot.child("vaccineName").getValue().toString();
                                image = snapshot.child("image").getValue().toString();

                                vaccineName.setText(vaccName);
                                Glide.with(OverallD.this).load(image).into(vaccineImage);
                            }
                        }
                    }
                }else if (!dataSnapshot.exists()){
                    vaccineName.setText("No Record");
                    vaccineImage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
