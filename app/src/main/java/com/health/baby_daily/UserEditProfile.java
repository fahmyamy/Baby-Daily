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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UserEditProfile extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference table_user;
    private ProgressDialog progressDialog;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;

    private Uri filePath;
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView editProfileImage;
    private EditText editTextUsername, editTextEmail;
    private Spinner editSpinnerRole;
    private Button buttonUpdateProfile;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    String role, selectedRole, uid, newEmail;
    String image = "none";
    String created;
    String modified;
    String[] list = { "Mother", "Father", "Doctor", "Caregiver"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        uid = firebaseAuth.getUid().toString();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        }

        editProfileImage = (ImageView) findViewById(R.id.editProfileImage);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        buttonUpdateProfile = (Button) findViewById(R.id.buttonUpdateProfile);

        editSpinnerRole = (Spinner) findViewById(R.id.editSpinnerRole);


        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editSpinnerRole.setAdapter(adapter);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        modified = dateFormat.format(calendar.getTime());

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseAuth.getUid().toString());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                editTextUsername.setText(dataSnapshot.child("username").getValue().toString());
                created = dataSnapshot.child("created").getValue().toString();
                selectedRole = dataSnapshot.child("role").getValue().toString();
                editTextEmail.setText(dataSnapshot.child("email").getValue().toString());
                int position = adapter.getPosition(selectedRole);
                editSpinnerRole.setSelection(position);
                String imageUrl = dataSnapshot.child("image").getValue().toString();
                image = imageUrl;
                if(!imageUrl.equals("none")){
                    Picasso.get().load(imageUrl).into(editProfileImage);
                }else{
                    Picasso.get().load(R.drawable.user_image).into(editProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        editProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileOpen();
            }
        });


        buttonUpdateProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        firebaseUser = firebaseAuth.getCurrentUser();

        role = String.valueOf(editSpinnerRole.getSelectedItem());
        final String uid = firebaseAuth.getUid().toString();
        table_user = FirebaseDatabase.getInstance().getReference("User").child(uid);
        final DatabaseReference mUserDb = FirebaseDatabase.getInstance().getReference().child("User");
        newEmail = editTextEmail.getText().toString();

        if(filePath != null) {
            final StorageReference ref = storageReference.child("User/" + uid.toString() + ".png");
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            image = uri.toString();

                            firebaseUser.updateEmail(newEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    User user = new User(uid, editTextUsername.getText().toString(), image, role, newEmail, created, modified);
                                    table_user.setValue(user);
                                    String deviceToken = FirebaseInstanceId.getInstance().getToken();
                                    mUserDb.child(uid).child("device_token").setValue(deviceToken);
                                }
                            });
                            finish();
                        }
                    });
                }
            });
        }else {
            User user = new User(uid, editTextUsername.getText().toString(), image, role, newEmail, created, modified);
            table_user.setValue(user);
            String deviceToken = FirebaseInstanceId.getInstance().getToken();
            mUserDb.child(uid).child("device_token").setValue(deviceToken);
            finish();
        }
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
