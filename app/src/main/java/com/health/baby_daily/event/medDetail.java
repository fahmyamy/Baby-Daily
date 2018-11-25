package com.health.baby_daily.event;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.health.baby_daily.MainActivity;
import com.health.baby_daily.R;
import com.health.baby_daily.model.MedicineD;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

public class medDetail extends AppCompatActivity {

    private static final String TAG = "medDetail";

    private Uri filePath;
    private static final int PICK_IMAGE_REQUEST = 1;

    private String bId, type, choosenDate, choosenTime, total, totalDisplay;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private ProgressDialog progressDialog;
    private DatabaseReference medicine;

    private TextView datetimepicker;
    private EditText medName, doseAmount;
    private Spinner units;
    private Button uploadImageButton, submitButton;
    private ImageView medImageView;

    public String image = "none", typeUnit, timestamp;
    private String[] list = {"Units", "ml", "piece", "dose", "oz", "tsp", "tbsp", "drop", "mg", "iu"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_detail);

        setTitle(" ");

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        SharedPreferences prefBaby = getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        Long timeStamp = System.currentTimeMillis()/1000;
        timestamp = timeStamp.toString();

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        medicine = db.getReference("MedicineD");

        datetimepicker = findViewById(R.id.datetimepicker);
        medName = findViewById(R.id.medName);
        doseAmount = findViewById(R.id.doseAmount);
        units = findViewById(R.id.units);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        medImageView = findViewById(R.id.medImageView);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        units.setAdapter(adapter);

        //pick date
        datetimepicker.setText("click here to pick date");
        datetimepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(medDetail.this,
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

                TimePickerDialog timeDialog = new TimePickerDialog(medDetail.this,
                        timeSetListener, hour, minute, DateFormat.is24HourFormat(medDetail.this));
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

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileOpen();
            }
        });


        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(medDetail.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();

                final String id = UUID.randomUUID().toString();

                if(datetimepicker.getText().toString() == "click here to pick date"){
                    Toast.makeText(medDetail.this,"Please Pick Date", Toast.LENGTH_SHORT).show();

                    return;
                }



                if(units.getSelectedItem().toString() == "Units"){
                    Toast.makeText(medDetail.this,"Please Select Units", Toast.LENGTH_SHORT).show();

                    return;
                }

                if(TextUtils.isEmpty(doseAmount.getText().toString())){
                    Toast.makeText(medDetail.this,"Please Insert Amount", Toast.LENGTH_SHORT).show();

                    return;
                }

                if (filePath != null) {
                    final StorageReference ref = storageReference.child("Medicine Details/" + bId);
                    ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    image = uri.toString();

                                    typeUnit = String.valueOf(units.getSelectedItem());
                                    MedicineD medicineD = new MedicineD(id, datetimepicker.getText().toString(), medName.getText().toString(), doseAmount.getText().toString(), typeUnit, image, timestamp+"_"+bId, bId);
                                    medicine.child(id).setValue(medicineD);
                                    Toast.makeText(medDetail.this, "New Medicine Details Added!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();

                                }
                            });
                        }
                    });
                } else if(!datetimepicker.getText().equals("click here to pick date")) {
                    typeUnit = String.valueOf(units.getSelectedItem());
                    MedicineD medicineD = new MedicineD(id, datetimepicker.getText().toString(), medName.getText().toString(), doseAmount.getText().toString(), typeUnit, image, timestamp+"_"+bId, bId);
                    medicine.child(id).setValue(medicineD);
                    Toast.makeText(medDetail.this, "New Medicine Details Added!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void fileOpen(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // Log.d(TAG, String.valueOf(bitmap));

                medImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
