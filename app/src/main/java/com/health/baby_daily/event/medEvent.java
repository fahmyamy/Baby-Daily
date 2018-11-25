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
import android.view.View;
import android.widget.AdapterView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.health.baby_daily.MainActivity;
import com.health.baby_daily.R;
import com.health.baby_daily.model.MedItem;
import com.health.baby_daily.model.Medicine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class medEvent extends AppCompatActivity {

    private static final String TAG = "medEvent";

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private String choosenDate, choosenTime, total, totalDisplay;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;

    private ProgressDialog progressDialog;
    private DatabaseReference medicine;

    private TextView datetimepicker;
    private Spinner medName;
    private EditText descNote;
    private String bId, medId, timestamp;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_event);

        setTitle(" ");

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        descNote = findViewById(R.id.descNote);

        SharedPreferences prefBaby = getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        Long timeStamp = System.currentTimeMillis()/1000;
        timestamp = timeStamp.toString();

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        medicine = db.getReference("Medicine");

        //pick date
        datetimepicker = findViewById(R.id.datetimepicker);
        datetimepicker.setText("click here to pick date");
        datetimepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(medEvent.this,
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

                TimePickerDialog timeDialog = new TimePickerDialog(medEvent.this,
                        timeSetListener, hour, minute, DateFormat.is24HourFormat(medEvent.this));
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

        final ArrayList<MedItem> medList = new ArrayList<MedItem>();
        medList.add(new MedItem("Select Medicine...", null));

        final Query query = FirebaseDatabase.getInstance().getReference("MedicineD")
                .orderByChild("baby_id")
                .equalTo(bId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String name = snapshot.child("medName").getValue().toString();
                        String id = snapshot.child("id").getValue().toString();
                        medList.add(new MedItem(name,id));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        medName = findViewById(R.id.medName);
        ArrayAdapter<MedItem> adapter = new ArrayAdapter<MedItem>(this, android.R.layout.simple_spinner_item, medList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        medName.setAdapter(adapter);

        medName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                MedItem clickedItem = (MedItem) adapterView.getItemAtPosition(position);
                medId = clickedItem.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(medEvent.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();

                if(datetimepicker.getText().toString() == "click here to pick date"){
                    Toast.makeText(medEvent.this,"Please Pick Date", Toast.LENGTH_SHORT).show();

                    return;
                }

                if(TextUtils.isEmpty(medName.getSelectedItem().toString())){
                    Toast.makeText(medEvent.this,"Select Medicine...", Toast.LENGTH_SHORT).show();

                    return;
                }

                final String id = UUID.randomUUID().toString();
                Medicine medValue = new Medicine(id, datetimepicker.getText().toString(), medId, descNote.getText().toString(), choosenDate, timestamp+"_"+bId, bId);
                medicine.child(id).setValue(medValue);
                Toast.makeText(medEvent.this, "New Medicine Event Added!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });


    }
}
