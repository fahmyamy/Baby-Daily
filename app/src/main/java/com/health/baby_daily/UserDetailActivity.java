package com.health.baby_daily;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.health.baby_daily.model.User;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class UserDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private DatabaseReference table_user;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private EditText editTextUsername;
    private Spinner spinner;
    private ImageView editProfileImage;

    private Uri filePath;
    private static final int PICK_IMAGE_REQUEST = 1;

    private Button buttonUserProfile;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    String role, uid, email;
    String image = "none";
    String created;
    String modified;
    String[] list = { "Mother", "Father", "Doctor", "Caregiver"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        setTitle("Upload Image & Video");

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        if(firebaseAuth.getCurrentUser()== null){
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        }

        FirebaseUser user_id = firebaseAuth.getCurrentUser();
        uid = user_id.getUid().toString();
        email = user_id.getEmail().toString();

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        spinner = (Spinner) findViewById(R.id.spinner);
        editProfileImage = (ImageView) findViewById(R.id.editProfileImage);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        buttonUserProfile = (Button) findViewById(R.id.buttonUserProfile);
        buttonUserProfile.setOnClickListener(this);


        editProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileOpen();
            }
        });

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        table_user = db.getReference("User");

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        created = dateFormat.format(calendar.getTime());
        modified = created;

        buttonUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog = new ProgressDialog(UserDetailActivity.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();

                role = String.valueOf(spinner.getSelectedItem());

                //image upload
                if(filePath != null){
                    final StorageReference ref = storageReference.child("User/" + uid.toString() + ".png");
                    ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    image = uri.toString();

                                    table_user.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.child(uid).exists()){
                                                progressDialog.dismiss();
                                                Toast.makeText(UserDetailActivity.this, "Already Add User Details", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                progressDialog.dismiss();

                                                String deviceToken = FirebaseInstanceId.getInstance().getToken();

                                                User user = new User(uid, editTextUsername.getText().toString(), image, role, email, created, modified);
                                                table_user.child(uid).setValue(user);
                                                table_user.child(uid).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(UserDetailActivity.this, "User Detail Updated!", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                        }
                    });
                }else {
                    image = "none";
                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(uid).exists()){
                                progressDialog.dismiss();
                                Toast.makeText(UserDetailActivity.this, "Already Add User Details", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                progressDialog.dismiss();

                                String deviceToken = FirebaseInstanceId.getInstance().getToken();

                                User user = new User(uid, editTextUsername.getText().toString(), image, role, email, created, modified);
                                table_user.child(uid).setValue(user);
                                table_user.child(uid).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(UserDetailActivity.this, "User Detail Updated!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }

    @Override
    public void onClick(View view){
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please Fill All the Form First", Toast.LENGTH_SHORT).show();
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

                editProfileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
