package com.health.baby_daily;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.health.baby_daily.other.ImageFullscreen;
import com.squareup.picasso.Picasso;

public class UserProfile extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;

    private TextView textUsername, textRole, textEmail;
    private ImageView editProfileImage;
    private Button buttonEditProfile;
    String user_id, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        }else {

            textUsername = (TextView) findViewById(R.id.textUsername);
            textRole = (TextView) findViewById(R.id.textRole);
            textEmail = (TextView) findViewById(R.id.textEmail);
            buttonEditProfile = (Button) findViewById(R.id.buttonEditProfile);

            editProfileImage = (ImageView) findViewById(R.id.editProfileImage);

            user_id = firebaseAuth.getCurrentUser().getUid().toString();

            //fetch data from firebase using user id
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(user_id);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                    textUsername.setText(dataSnapshot.child("username").getValue().toString().toUpperCase());
                    textRole.setText(dataSnapshot.child("role").getValue().toString());
                    textEmail.setText(dataSnapshot.child("email").getValue().toString());
                    final String imageUrl = dataSnapshot.child("image").getValue().toString();
                    if(!imageUrl.equals("none")){
                        Picasso.get().load(imageUrl).into(editProfileImage);

                        editProfileImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(UserProfile.this, ImageFullscreen.class);
                                intent.putExtra("image_url", imageUrl);
                                startActivity(intent);
                            }
                        });

                    }else{
                        Picasso.get().load(R.drawable.user_image).into(editProfileImage);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            databaseReference.keepSynced(true);

            buttonEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(UserProfile.this, UserEditProfile.class));
                }
            });


        }
    }
}
