package com.health.baby_daily.event;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.MainActivity;
import com.health.baby_daily.R;
import com.health.baby_daily.model.Baby;
import com.health.baby_daily.model.Measurement;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class measurementEvent extends AppCompatActivity {

    private static final String TAG = "measurementEvent";

    private ProgressDialog progressDialog;
    private DatabaseReference measurement;

    private String bId, type, choosenDate, choosenTime, total, totalDisplay;

    private TextView datetimepicker;
    private EditText heightValue, weightValue, headValue, descNote;
    private Button submitButton;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;

    private String fullname, image, gender, dob, created, modified, parent_id, timestamp, caregiver_id, doctor_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_event);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefBaby = getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        setTitle(" ");

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        measurement = db.getReference("Measurement");

        Long timeStamp = System.currentTimeMillis()/1000;
        timestamp = timeStamp.toString();

        datetimepicker = findViewById(R.id.datetimepicker);
        heightValue = findViewById(R.id.heightValue);
        weightValue = findViewById(R.id.weightValue);
        headValue = findViewById(R.id.headValue);
        descNote = findViewById(R.id.descNote);
        submitButton = findViewById(R.id.submitButton);

        datetimepicker.setText("click here to pick date");
        datetimepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(measurementEvent.this,
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
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog timeDialog = new TimePickerDialog(measurementEvent.this,
                        timeSetListener, hour, minute, DateFormat.is24HourFormat(measurementEvent.this));
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

        //fetch baby data for updating baby height n weight
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    fullname = dataSnapshot.child("fullName").getValue().toString();
                    dob = dataSnapshot.child("dob").getValue().toString();
                    gender =dataSnapshot.child("gender").getValue().toString();
                    created = dataSnapshot.child("created").getValue().toString();
                    image = dataSnapshot.child("image").getValue().toString();
                    parent_id = dataSnapshot.child("parent_id").getValue().toString();
                    caregiver_id = dataSnapshot.child("caregiver_id").getValue().toString();
                    doctor_id = dataSnapshot.child("doctor_id").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(datetimepicker.getText().toString() == "click here to pick date"){
                    Toast.makeText(measurementEvent.this,"Please Pick Date", Toast.LENGTH_SHORT).show();

                    return;
                }

                if(TextUtils.isEmpty(heightValue.getText().toString())){
                    Toast.makeText(measurementEvent.this,"Please Insert Height Value", Toast.LENGTH_SHORT).show();

                    return;
                }

                if(TextUtils.isEmpty(weightValue.getText().toString())){
                    Toast.makeText(measurementEvent.this,"Please Insert Weight Value", Toast.LENGTH_SHORT).show();

                    return;
                }

                if(TextUtils.isEmpty(headValue.getText().toString())){
                    Toast.makeText(measurementEvent.this,"Please Insert Head Value", Toast.LENGTH_SHORT).show();

                    return;
                }

                String id = UUID.randomUUID().toString();
                Measurement measurementValue = new Measurement(id, datetimepicker.getText().toString(), heightValue.getText().toString(), weightValue.getText().toString(), headValue.getText().toString(), descNote.getText().toString(), choosenDate, timestamp+"_"+bId, bId);
                measurement.child(id).setValue(measurementValue);
                DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                Baby babyValue = new Baby(bId, fullname, image, gender, dob, heightValue.getText().toString(), weightValue.getText().toString(), headValue.getText().toString(), created, modified, parent_id, caregiver_id, doctor_id);
                table_baby.setValue(babyValue);
                Toast.makeText(measurementEvent.this, "New Measurement Event Added!", Toast.LENGTH_SHORT).show();
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
