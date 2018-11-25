package com.health.baby_daily.statistic;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.health.baby_daily.model.Appointment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class AppointmentStaEdit extends AppCompatActivity {

    private static final String TAG = "bottleEvent";

    private ProgressDialog progressDialog;
    private DatabaseReference appointment;

    private String bId, id, type, choosenDate, choosenTime, total, totalDisplay;

    private TextView datetimepicker;
    private EditText doctorName, descNote;
    private Spinner typeSpinner;
    private Button submitButton;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;

    String[] list = { "Select Reason","Routine check", "Well visit", "Sick visit", "Immunisation", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_sta_edit);

        //assigning store location in firebase
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        appointment = db.getReference("Appointment");

        setTitle(" ");

        SharedPreferences prefBaby = getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        datetimepicker = findViewById(R.id.datetimepicker);
        doctorName = findViewById(R.id.doctorName);
        descNote = findViewById(R.id.descNote);
        typeSpinner = findViewById(R.id.type);
        submitButton = findViewById(R.id.submitButton);

        //spinner content
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Appointment").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    datetimepicker.setText(Objects.requireNonNull(dataSnapshot.child("dateTime").getValue()).toString());
                    doctorName.setText(Objects.requireNonNull(dataSnapshot.child("doctorName").getValue()).toString());
                    String selectedType = Objects.requireNonNull(dataSnapshot.child("type").getValue()).toString();
                    int position = adapter.getPosition(selectedType);
                    typeSpinner.setSelection(position);
                    descNote.setText(Objects.requireNonNull(dataSnapshot.child("desc").getValue()).toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        datetimepicker.setText("click here to pick date");
        datetimepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AppointmentStaEdit.this,
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
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                choosenDate = day + "/" + month + "/" + year;

                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog timeDialog = new TimePickerDialog(AppointmentStaEdit.this,
                        timeSetListener, hour, minute, DateFormat.is24HourFormat(AppointmentStaEdit.this));
                timeDialog.show();

            }
        };

        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                int jam = h;
                int minit = m;
                choosenTime = jam + ":" + minit;

                total = choosenDate + " " + choosenTime;
                totalDisplay = choosenDate + " " + choosenTime;
                datetimepicker.setText(totalDisplay);
            }
        };

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(AppointmentStaEdit.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();

                type = String.valueOf(typeSpinner.getSelectedItem());

                if(datetimepicker.getText().toString() == "click here to pick date"){
                    Toast.makeText(AppointmentStaEdit.this,"Please Pick Date", Toast.LENGTH_SHORT).show();

                    return;
                }

                if(TextUtils.isEmpty(doctorName.getText().toString())){
                    Toast.makeText(AppointmentStaEdit.this,"Please Insert Doctor Name", Toast.LENGTH_SHORT).show();

                    return;
                }

                if (type.equals("Select Reason")){
                    Toast.makeText(AppointmentStaEdit.this,"Please Select Appointment Type", Toast.LENGTH_SHORT).show();

                    return;
                }

                Appointment appointmentValue = new Appointment(id, datetimepicker.getText().toString(), doctorName.getText().toString(), type, descNote.getText().toString(), bId);
                appointment.child(id).setValue(appointmentValue);
                Toast.makeText(AppointmentStaEdit.this, "Appointment Event Updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}
