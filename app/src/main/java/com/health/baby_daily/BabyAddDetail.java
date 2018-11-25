package com.health.baby_daily;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.health.baby_daily.model.Baby;
import com.health.baby_daily.model.Measurement;
import com.health.baby_daily.model.ParentnCaregiverList;
import com.health.baby_daily.model.ParentnDoctorList;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class BabyAddDetail extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "BabyAddDetail";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private DatabaseReference table_baby;
    private DatabaseReference measurement;

    private EditText editTextFullName;
    private TextView editTextDOB;
    private EditText editTextHeight;
    private EditText editTextWeight;
    private EditText editTextHead;
    private Spinner spinnerGender, spinnerNany, spinnerDoctor;

    private Uri filePath;
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView editBabyImage;
    private Button buttonAddBaby;

    private Calendar calendar;
    private SimpleDateFormat dateFormat, df;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private Date date;

    String pID, gender, dob, currentDate, timestamp, caregiver_id, doctor_id;
    String image = "none";
    String created;
    String modified;
    String[] list = { "Male", "Female"};
    List<String> cname_list = new ArrayList<>();
    List<String> cid_list = new ArrayList<>();

    List<String> dname_list = new ArrayList<>();
    List<String> did_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_add_detail);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        }

        //user id for as foreign key
        FirebaseUser user_id = firebaseAuth.getCurrentUser();
        pID = user_id.getUid().toString();

        Long timeStamp = System.currentTimeMillis()/1000;
        timestamp = timeStamp.toString();

        editTextFullName = (EditText) findViewById(R.id.editTextFullName);
        editTextDOB = (TextView) findViewById(R.id.editTextDOB);
        editTextHeight = (EditText) findViewById(R.id.editTextHeight);
        editTextWeight = (EditText) findViewById(R.id.editTextWeight);
        editTextHead = (EditText) findViewById(R.id.editTextHead);
        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
        spinnerNany = (Spinner) findViewById(R.id.spinnerNany);
        spinnerDoctor = (Spinner) findViewById(R.id.spinnerDoctor);

        //caregiver
        cname_list.add("Select Caregiver");
        cid_list.add(null);
        cname_list.add("No Caregiver");
        cid_list.add("none");

        final ArrayAdapter adapterC = new ArrayAdapter(this, android.R.layout.simple_spinner_item, cname_list);
        adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerNany.setAdapter(adapterC);

        final Query queryPnG = FirebaseDatabase.getInstance().getReference("ParentnCaregiverList")
                .orderByChild("status")
                .equalTo("1");
        queryPnG.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ParentnCaregiverList caregiverList = snapshot.getValue(ParentnCaregiverList.class);
                        if (caregiverList.getStatus().equals("1") && caregiverList.getParent_id().equals(firebaseAuth.getUid())){
                            String id = caregiverList.getCaregiver_id();

                            final Query query = FirebaseDatabase.getInstance().getReference("User")
                                    .orderByChild("id")
                                    .equalTo(id);
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            String name = snapshot.child("username").getValue().toString();
                                            String id = snapshot.child("id").getValue().toString();

                                            cname_list.add(name);
                                            cid_list.add(id);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spinnerNany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                caregiver_id = cid_list.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //doctor
        dname_list.add("Select Doctor");
        did_list.add(null);
        dname_list.add("No Doctor");
        did_list.add("none");

        final ArrayAdapter adapterD = new ArrayAdapter(this, android.R.layout.simple_spinner_item, dname_list);
        adapterD.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerDoctor.setAdapter(adapterD);

        final Query queryPnD = FirebaseDatabase.getInstance().getReference("ParentnDoctorList")
                .orderByChild("status")
                .equalTo("1");
        queryPnD.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ParentnDoctorList doctorList = snapshot.getValue(ParentnDoctorList.class);
                        if (doctorList.getStatus().equals("1") && doctorList.getParent_id().equals(firebaseAuth.getUid())){
                            String id = doctorList.getDoctor_id();

                            final Query query = FirebaseDatabase.getInstance().getReference("User")
                                    .orderByChild("id")
                                    .equalTo(id);
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            String name = snapshot.child("username").getValue().toString();
                                            String id = snapshot.child("id").getValue().toString();

                                            dname_list.add(name);
                                            did_list.add(id);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                doctor_id = did_list.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //spinner content
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        editBabyImage = (ImageView) findViewById(R.id.editBabyImage);
        buttonAddBaby = (Button) findViewById(R.id.buttonAddBaby);

        //add image to firebase storage
        editBabyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        //assigning store location in firebase
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        table_baby = db.getReference("Baby");

        final FirebaseDatabase db2 = FirebaseDatabase.getInstance();
        measurement = db2.getReference("Measurement");

        //date picker
        editTextDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(BabyAddDetail.this,
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

                dob = day + "/" + month + "/" + year;
                editTextDOB.setText(dob);
            }
        };


        //system datetime
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        created = dateFormat.format(calendar.getTime());
        modified = created;

        df = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = df.format(calendar.getTime());

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        date = new Date();

        buttonAddBaby.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        gender = String.valueOf(spinnerGender.getSelectedItem());
        final String child_id = UUID.randomUUID().toString();

        if (caregiver_id == null || doctor_id == null){
            Toast.makeText(BabyAddDetail.this, "Please Select Caregiver or Doctor", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }

        //image upload
        if(filePath != null) {
            final StorageReference ref = storageReference.child("Baby/" + child_id);
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            image = uri.toString();

                            Baby baby = new Baby(child_id, editTextFullName.getText().toString(), image, gender, editTextDOB.getText().toString(), editTextHeight.getText().toString(), editTextWeight.getText().toString(), editTextHead.getText().toString(), created, modified, pID, caregiver_id, doctor_id);
                            table_baby.child(child_id).setValue(baby);
                            String id = UUID.randomUUID().toString();
                            Measurement measurementValue= new Measurement(id, dateFormat.format(date), editTextHeight.getText().toString(), editTextWeight.getText().toString(), editTextHead.getText().toString(), "none", currentDate, timestamp+"_"+child_id, child_id);
                            measurement.child(id).setValue(measurementValue);
                        }
                    });
                }
            });
        }else {
            Baby baby = new Baby(child_id, editTextFullName.getText().toString(), image, gender, editTextDOB.getText().toString(), editTextHeight.getText().toString(), editTextWeight.getText().toString(), editTextHead.getText().toString(), created, modified, pID, caregiver_id, doctor_id);
            table_baby.child(child_id).setValue(baby);
            String id = UUID.randomUUID().toString();
            Measurement measurementValue= new Measurement(id, dateFormat.format(date), editTextHeight.getText().toString(), editTextWeight.getText().toString(), editTextHead.getText().toString(), "none", currentDate, timestamp+"_"+child_id, child_id);
            measurement.child(id).setValue(measurementValue);
        }

        progressDialog.dismiss();

        Toast.makeText(BabyAddDetail.this, "New Baby Detail Added!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), BabyProfile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("babyId", child_id);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // Log.d(TAG, String.valueOf(bitmap));

                editBabyImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
