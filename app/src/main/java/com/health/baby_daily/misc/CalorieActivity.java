package com.health.baby_daily.misc;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.vikramezhil.dccv.CalorieCounterView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalorieActivity extends AppCompatActivity {

    private CalorieCounterView counterView;
    private TextView babyName, currentDate, bmiValue, proteinValue, consumeValue, totalRemaining;
    private String bId, height, weight, headC;
    private String amount, type, unit, dateTime, currDate;
    int totalAmountML, totalAmountG, totalAmountMLF, totalAmountGF, totalValue;
    int totalConsumedB, totalConsumedF, calLeft;
    double gramAmountB, gramAmountG;
    double proteinIntake, totalProtein, proteinNeed;

    private static DecimalFormat df2;

    private DatabaseReference databaseReference;

    private DecimalFormat numberFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie);

        SharedPreferences prefBaby = getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);
        String bName = prefBaby.getString("babyName", null);
        String bImage = prefBaby.getString("babyImage", null);

        babyName = findViewById(R.id.babyName);
        babyName.setText(bName);

        df2 = new DecimalFormat("#.##");

        currentDate = findViewById(R.id.currentDate);
        DateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy");
        Date date = new Date();
        currentDate.setText(dateFormat.format(date));

        DateFormat dateF= new SimpleDateFormat("dd/MM/yyyy");
        Date d= new Date();
        currDate = dateF.format(d);

        //initialize
        counterView = findViewById(R.id.calorieCounterView);
        counterView.setIgnoreMax(true);
        counterView.setDangerMaxWarning(true, Color.RED);

        bmiValue = findViewById(R.id.bmiValue);
        proteinValue = findViewById(R.id.proteinValue);
        consumeValue = findViewById(R.id.consumeValue);
        totalRemaining = findViewById(R.id.totalRemaining);
        numberFormat = new DecimalFormat("#.00");

        databaseReference = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    height = dataSnapshot.child("height").getValue().toString().trim();
                    weight = dataSnapshot.child("weight").getValue().toString().trim();
                    headC = dataSnapshot.child("headC").getValue().toString().trim();

                    countBMI(height, weight);
                    countProtein(weight);
                    countCalories(weight);
                    countConsumed();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void countBMI(String hValue, String wValue){
        double w = Double.parseDouble(wValue);
        double h = Double.parseDouble(hValue);
        double m = h/100;
        double hm = m*m;
        double bmiTotal = w/hm;
        String total = numberFormat.format(bmiTotal);
        bmiValue.setText(total);
    }

    public void countProtein(String wValue){
        double w = Double.parseDouble(wValue);
        double wP = w*2.2;
        proteinNeed = wP*0.55;
        proteinValue.setText("0g/" + numberFormat.format(proteinNeed) + "g");
    }

    public void countCalories(String wValue) {
        double w = Double.parseDouble(wValue);
        double total = w*120;
        counterView.setMaximum((int) total);
        totalValue = (int) total;
        counterView.setHeaderTxt(String.valueOf(totalValue));
    }

    public void countConsumed(){

        //for fetch breast milk calories
        Query query = FirebaseDatabase.getInstance().getReference("Bottle")
                .orderByChild("babyId")
                .equalTo(bId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        dateTime = snapshot.child("dateTime").getValue().toString();
                        if (dateTime.startsWith(currDate)) {
                            type = snapshot.child("type").getValue().toString();
                            unit = snapshot.child("units").getValue().toString();

                            if ((unit.equals("mL")) && (type.equals("Breast"))) {
                                amount = snapshot.child("amount").getValue().toString();
                                int newAmountML = Integer.parseInt(amount);
                                totalAmountML = totalAmountML + newAmountML;
                            }

                            if ((unit.equals("Oz")) && (type.equals("Breast"))) {
                                amount = snapshot.child("amount").getValue().toString();
                                int newAmountG = Integer.parseInt(amount);
                                totalAmountG = totalAmountG + newAmountG;
                            }

                            if ((unit.equals("mL")) && (type.equals("Formula"))) {
                                amount = snapshot.child("amount").getValue().toString();
                                int newAmountMLF = Integer.parseInt(amount);
                                totalAmountMLF = totalAmountMLF + newAmountMLF;
                                proteinIntake = 0.1;
                                totalProtein = totalProtein + proteinIntake;
                            }

                            if ((unit.equals("Oz")) && (type.equals("Formula"))) {
                                amount = snapshot.child("amount").getValue().toString();
                                int newAmountGF = Integer.parseInt(amount);
                                totalAmountGF = totalAmountGF + newAmountGF;
                                proteinIntake = 0.1;
                                totalProtein = totalProtein + proteinIntake;
                            }
                        }
                    }

                    //calculation for breastmilk
                    double toML = totalAmountG * 29.57;
                    double totalAmountB = totalAmountML + toML;
                    gramAmountB = totalAmountB * 1.03;
                    totalConsumedB = (int) Math.round(gramAmountB * 0.7);

                    //calculation for formulamilk
                    double toML2 = totalAmountGF*29.57;
                    double totalAmountG = totalAmountMLF + toML2;
                    gramAmountG = totalAmountG * 1.03;
                    totalConsumedF = (int) Math.round(gramAmountG * 0.78);

                    proteinValue.setText(df2.format(totalProtein) + "g/" + numberFormat.format(proteinNeed) + "g");
                    calLeft = totalValue - totalConsumedB - totalConsumedF;
                    counterView.setProgress(totalConsumedB + totalConsumedF);
                    counterView.setHeaderTxt(String.valueOf(calLeft));
                    counterView.setFooterTxt(String.valueOf(totalConsumedB + totalConsumedF));
                    consumeValue.setText(String.valueOf(totalConsumedB + totalConsumedF));
                    totalRemaining.setText(String.valueOf(calLeft));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
