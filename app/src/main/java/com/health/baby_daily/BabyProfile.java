package com.health.baby_daily;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.health.baby_daily.guide.BabyReport;
import com.health.baby_daily.other.ImageFullscreen;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BabyProfile extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Intent intent;

    private String bId, babyAge, role;
    private ImageView viewBabyImage;
    private Button buttonEditBaby, buttonDeleteBaby, buttonEventBaby;

    private int totalday, totalmonth, totalyear;

    private TextView viewFullName, viewGender, viewDOB, viewAge, viewHeight, viewWeight, viewHeadC;

    private String fN, g, d, h, w, hC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_profile);

        SharedPreferences prefRole = getSharedPreferences("currRole", 0);
        role = prefRole.getString("role", null);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        }

        viewFullName = (TextView) findViewById(R.id.viewFullName);
        viewGender = (TextView) findViewById(R.id.viewGender);
        viewDOB = (TextView) findViewById(R.id.viewDOB);
        viewAge = (TextView) findViewById(R.id.viewAge);
        viewHeight = (TextView)findViewById(R.id.viewHeight);
        viewWeight = (TextView) findViewById(R.id.viewWeight);
        viewHeadC = (TextView) findViewById(R.id.viewHead);

        viewBabyImage = (ImageView) findViewById(R.id.viewBabyImage);
        buttonEditBaby = (Button) findViewById(R.id.buttonEditBaby);
        buttonDeleteBaby = (Button) findViewById(R.id.buttonDeleteBaby);
        buttonEventBaby = (Button) findViewById(R.id.buttonEventBaby);

        if (role.equals("Mother") || role.equals("Father")){
            buttonEventBaby.setVisibility(View.GONE);
        }else if (role.equals("Caregiver")){
            buttonEditBaby.setVisibility(View.GONE);
            buttonDeleteBaby.setVisibility(View.GONE);
            buttonEventBaby.setVisibility(View.GONE);
        }else if (role.equals("Doctor")){
            buttonEditBaby.setVisibility(View.GONE);
            buttonDeleteBaby.setVisibility(View.GONE);
        }

        if (role.equals("Mother") || role.equals("Father") || role.equals("Doctor")){
            Intent intent = getIntent();
            bId = intent.getStringExtra("babyId");

            if(bId.isEmpty()) {
                intent = new Intent(this, BabyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }else {
                databaseReference = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                databaseReference.keepSynced(true);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            fN = dataSnapshot.child("fullName").getValue().toString();
                            g = dataSnapshot.child("gender").getValue().toString();
                            d = dataSnapshot.child("dob").getValue().toString();
                            h = dataSnapshot.child("height").getValue().toString() + "cm";
                            w = dataSnapshot.child("weight").getValue().toString() + "kg";
                            hC = dataSnapshot.child("headC").getValue().toString() + "cm";
                            final String image = dataSnapshot.child("image").getValue().toString();

                            String[] datesplit = d.split("/");
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

                            viewFullName.setText(fN);
                            viewGender.setText(g);
                            viewDOB.setText(d);
                            viewAge.setText(age);
                            viewHeight.setText(h);
                            viewWeight.setText(w);
                            viewHeadC.setText(hC);
                            if(!image.equals("none")){
                                Picasso.get().load(image).into(viewBabyImage);

                                viewBabyImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(BabyProfile.this, ImageFullscreen.class);
                                        intent.putExtra("image_url", image);
                                        startActivity(intent);
                                    }
                                });
                            }else {
                                Picasso.get().load(R.drawable.user_image).into(viewBabyImage);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                buttonEditBaby.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(BabyProfile.this, BabyEdit.class);
                        intent.putExtra("babyId", bId);
                        startActivity(intent);
                    }
                });

                buttonDeleteBaby.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                        databaseReference.removeValue();
                        StorageReference storageReference = firebaseStorage.getReference();
                        StorageReference sR = storageReference.child("babies/" + bId);
                        sR.delete();
                        Toast.makeText(BabyProfile.this, "Baby Info Removed!!" , Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), BabyActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

                buttonEventBaby.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(BabyProfile.this, BabyReport.class);
                        startActivity(intent);

                        SharedPreferences pref = getSharedPreferences("selectedBaby", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("babyId", bId);
                        editor.apply();
                    }
                });
            }
        }
    }
}
