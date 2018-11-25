package com.health.baby_daily;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
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
import com.health.baby_daily.model.ParentnCaregiverList;
import com.health.baby_daily.model.ParentnDoctorList;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class BabyEdit extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "BabyEdit";

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;

    private EditText editTextFullName;
    private TextView editTextDOB;
    private Spinner spinnerGender, spinnerNany, spinnerDoctor;
    private ImageView editBabyImage;

    private DatePickerDialog.OnDateSetListener dateSetListener;

    private Uri filePath;
    private static final int PICK_IMAGE_REQUEST = 1;

    private String bId, cid, cname, did;
    String created, headC, height, weight, parent_id;
    String modified;
    String gender;
    String selectedCaregiver, selectedDoctor;
    private String image = "none";
    String[] list = { "Male", "Female"};
    List<String> cname_list = new ArrayList<>();
    List<String> cid_list = new ArrayList<>();
    List<String> dname_list = new ArrayList<>();
    List<String> did_list = new ArrayList<>();

    private ArrayAdapter<String> adapterC;
    private ArrayAdapter<String> adapterD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        }

        Intent intent = getIntent();
        bId = intent.getStringExtra("babyId");

        editTextFullName = findViewById(R.id.editTextFullName);
        editTextDOB = findViewById(R.id.editTextDOB);
        spinnerGender = findViewById(R.id.spinnerGender);
        editBabyImage = findViewById(R.id.editBabyImage);
        Button buttonEditBaby = findViewById(R.id.buttonEditBaby);
        spinnerNany = findViewById(R.id.spinnerNany);
        spinnerDoctor = findViewById(R.id.spinnerDoctor);

        cname_list.add("Select Caregiver");
        cid_list.add(null);
        cname_list.add("No Caregiver");
        cid_list.add("none");

        adapterC = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cname_list);
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

        //doctor
        dname_list.add("Select Doctor");
        did_list.add(null);
        dname_list.add("No Doctor");
        did_list.add("none");

        adapterD = new ArrayAdapter(this, android.R.layout.simple_spinner_item, dname_list);
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

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        modified = dateFormat.format(calendar.getTime());

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    editTextFullName.setText(Objects.requireNonNull(dataSnapshot.child("fullName").getValue()).toString());
                    editTextDOB.setText(Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString());
                    String selectedRole = Objects.requireNonNull(dataSnapshot.child("gender").getValue()).toString();
                    int position = adapter.getPosition(selectedRole);
                    spinnerGender.setSelection(position);
                    String imageUrl = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();
                    Picasso.get().load(imageUrl).into(editBabyImage);
                    image = imageUrl;
                    created = Objects.requireNonNull(dataSnapshot.child("created").getValue()).toString();
                    height = Objects.requireNonNull(dataSnapshot.child("height").getValue()).toString();
                    weight = Objects.requireNonNull(dataSnapshot.child("weight").getValue()).toString();
                    headC = Objects.requireNonNull(dataSnapshot.child("headC").getValue()).toString();
                    parent_id = Objects.requireNonNull(dataSnapshot.child("parent_id").getValue()).toString();
                    selectedCaregiver = Objects.requireNonNull(dataSnapshot.child("caregiver_id").getValue()).toString();
                    selectedDoctor = Objects.requireNonNull(dataSnapshot.child("doctor_id").getValue()).toString();

                    if (!selectedCaregiver.equals("none") || !selectedRole.equals(null)){
                        final DatabaseReference dR = FirebaseDatabase.getInstance().getReference("User").child(selectedCaregiver);
                        dR.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    String name = dataSnapshot.child("username").getValue().toString();
                                    int positionC = adapterC.getPosition(name);
                                    spinnerNany.setSelection(positionC);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (selectedCaregiver.equals("none")){
                        int positionD = adapterD.getPosition("No Caregiver");
                        spinnerNany.setSelection(positionD);
                    }

                    if (!selectedDoctor.equals("none") || !selectedDoctor.equals(null)){
                        final DatabaseReference dR2 = FirebaseDatabase.getInstance().getReference("User").child(selectedDoctor);
                        dR2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    String name = dataSnapshot.child("username").getValue().toString();
                                    int positionD = adapterD.getPosition(name);
                                    spinnerDoctor.setSelection(positionD);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (selectedDoctor.equals("none")){
                        int positionD = adapterD.getPosition("No Doctor");
                        spinnerDoctor.setSelection(positionD);
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
                cname = cname_list.get(i);
                cid = cid_list.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                did = did_list.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        editTextDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(BabyEdit.this,
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

                String dob = day + "/" + month + "/" + year;
                editTextDOB.setText(dob);
            }
        };

        editBabyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileOpen();
            }
        });

        buttonEditBaby.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        firebaseUser = firebaseAuth.getCurrentUser();

        gender = String.valueOf(spinnerGender.getSelectedItem());

        if (cid == null || did == null){
            Toast.makeText(BabyEdit.this, "Please Select Caregiver or Doctor", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }

        if(filePath != null) {
            final StorageReference ref = storageReference.child("Baby/" + bId);
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            image = uri.toString();
                            DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby");
                            Baby baby = new Baby (bId, editTextFullName.getText().toString(), image, gender, editTextDOB.getText().toString(), height, weight, headC, created, modified, parent_id, cid, did);
                            table_baby.child(bId).setValue(baby);
                        }
                    });
                }
            });
        }else {
            DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby");
            Baby baby = new Baby (bId, editTextFullName.getText().toString(), image, gender, editTextDOB.getText().toString(), height, weight, headC, created, modified, parent_id, cid, did);
            table_baby.child(bId).setValue(baby);
        }

        progressDialog.dismiss();

        Toast.makeText(this, "Profile Updated!!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), BabyProfile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("babyId", bId);
        startActivity(intent);
        finish();
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

                editBabyImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
