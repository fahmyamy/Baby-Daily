package com.health.baby_daily.statistic;

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
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.health.baby_daily.R;
import com.health.baby_daily.model.Vaccine;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

public class VaccineStaEdit extends AppCompatActivity {

    private static final String TAG = "vaccineEvent";

    private Uri filePath;
    private static final int PICK_IMAGE_REQUEST = 1;

    private String bId, type, choosenDate, choosenTime, total, totalDisplay, id;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private ProgressDialog progressDialog;
    private DatabaseReference vaccine;

    private TextView datetimepicker;
    private EditText vaccineName, descNote;
    private Button uploadImageButton, submitButton;
    private ImageView vaccineImageView;

    public String image = "none", imageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine_sta_edit);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        setTitle(" ");

        SharedPreferences prefBaby = getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        imageId = intent.getStringExtra("imageId");

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        vaccine = db.getReference("Vaccine");

        datetimepicker = findViewById(R.id.datetimepicker);
        vaccineName = findViewById(R.id.vaccineName);
        descNote = findViewById(R.id.descNote);
        vaccineImageView = findViewById(R.id.vaccineImageView);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        submitButton = findViewById(R.id.submitButton);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccine").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    datetimepicker.setText(Objects.requireNonNull(dataSnapshot.child("dateTime").getValue()).toString());
                    vaccineName.setText(Objects.requireNonNull(dataSnapshot.child("vaccineName").getValue()).toString());
                    String imageUrl = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();
                    Picasso.get().load(imageUrl).into(vaccineImageView);
                    descNote.setText(Objects.requireNonNull(dataSnapshot.child("desc").getValue()).toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //pick date
        datetimepicker.setText("click here to pick date");
        datetimepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(VaccineStaEdit.this,
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

                TimePickerDialog timeDialog = new TimePickerDialog(VaccineStaEdit.this,
                        timeSetListener, hour, minute, DateFormat.is24HourFormat(VaccineStaEdit.this));
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

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileOpen();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(VaccineStaEdit.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();

                if(datetimepicker.getText().toString() == "click here to pick date"){
                    Toast.makeText(VaccineStaEdit.this,"Please Pick Date", Toast.LENGTH_SHORT).show();

                    return;
                }

                if(TextUtils.isEmpty(vaccineName.getText().toString())){
                    Toast.makeText(VaccineStaEdit.this,"Please Select Type", Toast.LENGTH_SHORT).show();

                    return;
                }

                if (filePath != null) {
                    final StorageReference ref = storageReference.child("diapers/" + bId);
                    ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    image = uri.toString();

                                    Vaccine vaccineValue = new Vaccine(id, datetimepicker.getText().toString(), vaccineName.getText().toString(), descNote.getText().toString(), image, bId);
                                    vaccine.child(id).setValue(vaccineValue);
                                    Toast.makeText(VaccineStaEdit.this, "Vaccine Event Updated!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), VaccineSta.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();

                                }
                            });
                        }
                    });
                } else if(!datetimepicker.getText().equals("click here to pick date")) {
                    Vaccine vaccineValue = new Vaccine(id, datetimepicker.getText().toString(), vaccineName.getText().toString(), descNote.getText().toString(), imageId, bId);
                    vaccine.child(id).setValue(vaccineValue);
                    Toast.makeText(VaccineStaEdit.this, "Vaccine Event Updated!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), VaccineSta.class);
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

                vaccineImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
