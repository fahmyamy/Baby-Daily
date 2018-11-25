package com.health.baby_daily.event;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.health.baby_daily.MainActivity;
import com.health.baby_daily.R;
import com.health.baby_daily.model.Bottle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class bottleEvent extends AppCompatActivity {

    private static final String TAG = "bottleEvent";

    private ProgressDialog progressDialog;
    private DatabaseReference bottle;
    private FirebaseAuth firebaseAuth;

    private int hour, minute;
    private String bId, bName, bImage, type, choosenDate, choosenTime, total, totalDisplay, currId;
    private TextView formulaType, sUnits;
    private EditText amount;
    private TextView units;
    private String unit, amt;
    private Switch switchUnit;
    private Button submitButton;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottle_event);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        currId = user.getUid().toString();

        SharedPreferences prefBaby = getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        //assigning store location in firebase
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        bottle = db.getReference("Bottle");

        setTitle(" ");

        formulaType = findViewById(R.id.formulaType);
        formulaType.setText(type + " Milk");

        final TextView datetimepicker = findViewById(R.id.datetimepicker);
        datetimepicker.setText("click here to pick date");
        datetimepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                hour = cal.get(Calendar.HOUR_OF_DAY);
                minute = cal.get(Calendar.MINUTE);

                DatePickerDialog dialog = new DatePickerDialog(bottleEvent.this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                        dateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String newmonth, newday;
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                if(day <= 9){
                    newday = "0" + day;
                }else {
                    newday = String.valueOf(day);
                }
                if(month <= 9){
                    newmonth = "0" + month;
                }else{
                    newmonth = String.valueOf(month);
                }
                 choosenDate = newday + "/" + newmonth + "/" + year;

                Calendar cal = Calendar.getInstance();
                hour = cal.get(Calendar.HOUR_OF_DAY);
                minute = cal.get(Calendar.MINUTE);

                TimePickerDialog timeDialog = new TimePickerDialog(bottleEvent.this,
                        timeSetListener, hour, minute, DateFormat.is24HourFormat(bottleEvent.this));
                timeDialog.show();

            }
        };

        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                int jam = h;
                int minit = m;
                String min = null, hour;

                if(jam <= 9){
                    hour = "0" + jam;
                }else{
                    hour = String.valueOf(jam);
                }
                if(minit <= 9){
                    min = "0" + minit;
                }else{
                    min = String.valueOf(minit);
                }
                choosenTime = hour + ":" + min;

                total = choosenDate + " " + choosenTime;
                totalDisplay = choosenDate + " " + choosenTime;
                datetimepicker.setText(totalDisplay);
            }
        };

        amount = findViewById(R.id.amount);
        amt = amount.getText().toString();

        switchUnit = findViewById(R.id.switchUnit);
        sUnits = findViewById(R.id.sUnits);
        units = findViewById(R.id.unit);

        if(switchUnit.isChecked()){
            unit = switchUnit.getTextOn().toString();
        }else{
            unit = switchUnit.getTextOff().toString();
        }

        switchUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switchUnit.isChecked()){
                    units.setText(switchUnit.getTextOn().toString());
                    sUnits.setText(switchUnit.getTextOn().toString());
                    unit = switchUnit.getTextOn().toString();
                }else{
                    units.setText(switchUnit.getTextOff().toString());
                    sUnits.setText(switchUnit.getTextOff().toString());
                    unit = switchUnit.getTextOff().toString();
                }
            }
        });

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(datetimepicker.getText().toString() == "click here to pick date"){
                    Toast.makeText(bottleEvent.this,"Please Pick Date", Toast.LENGTH_SHORT).show();

                    return;
                }

                if(TextUtils.isEmpty(amount.getText().toString())){
                    Toast.makeText(bottleEvent.this,"Please Insert Amount", Toast.LENGTH_SHORT).show();

                    return;
                }

                progressDialog = new ProgressDialog(bottleEvent.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();

                progressDialog.dismiss();

                String id = UUID.randomUUID().toString();
                Bottle bottleValue = new Bottle(id, total, amount.getText().toString(), type, unit, bId, choosenDate, currId);
                bottle.child(id).setValue(bottleValue);
                Toast.makeText(bottleEvent.this, "New Bottle Event Added!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}
