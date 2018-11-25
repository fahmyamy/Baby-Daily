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
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.health.baby_daily.model.Feed;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class feedEvent extends AppCompatActivity {

    private static final String TAG = "feedEvent";

    private ProgressDialog progressDialog;
    private DatabaseReference feed;
    private FirebaseAuth firebaseAuth;

    private int hour, minute;
    private String bId, type, choosenDate, choosenTime, total, totalDisplay, typeFood;
    private Spinner typeSpinner;
    private EditText amount;
    private TextView units, sUnits;
    private String unit, amt, currId;
    private Switch switchUnit;
    private Button submitButton;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;

    String[] list = {"Please Select Type", "Fruit", "Vegetables", "Meats & Proteins", "Grains", "Dairy", "Bread", "Cereal"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_event);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefBaby = getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        //assigning store location in firebase
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        feed = db.getReference("Feed");

        setTitle(" ");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        currId = user.getUid().toString();

        typeSpinner = findViewById(R.id.typeSpinner);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        final TextView datetimepicker = findViewById(R.id.datetimepicker);
        datetimepicker.setText("click here to pick date");
        datetimepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(feedEvent.this,
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
                String newday, newmonth;
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

                TimePickerDialog timeDialog = new TimePickerDialog(feedEvent.this,
                        timeSetListener, hour, minute, DateFormat.is24HourFormat(feedEvent.this));
                timeDialog.show();

            }
        };

        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                int jam = h;
                int minit = m;
                String hour, min;

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
                    Toast.makeText(feedEvent.this,"Please Pick Date", Toast.LENGTH_SHORT).show();

                    return;
                }

                if(TextUtils.isEmpty(amount.getText().toString())){
                    Toast.makeText(feedEvent.this,"Please Insert Amount", Toast.LENGTH_SHORT).show();

                    return;
                }

                if(TextUtils.isEmpty(typeSpinner.getSelectedItem().toString())){
                    Toast.makeText(feedEvent.this,"Please Select Type", Toast.LENGTH_SHORT).show();

                    return;
                }

                typeFood = String.valueOf(typeSpinner.getSelectedItem());
                String id = UUID.randomUUID().toString();
                Feed feedValue = new Feed(id, datetimepicker.getText().toString(), amount.getText().toString(), typeFood, unit, choosenDate, currId, bId);
                feed.child(id).setValue(feedValue);
                Toast.makeText(feedEvent.this, "New Feed Event Added!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
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
