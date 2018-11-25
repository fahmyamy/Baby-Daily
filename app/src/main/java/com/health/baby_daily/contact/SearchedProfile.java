package com.health.baby_daily.contact;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.model.Notify;
import com.health.baby_daily.model.ParentnCaregiverList;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.UUID;

public class SearchedProfile extends AppCompatActivity {

    private String pid, cid, username, role, email, image;
    private String uid;
    private String currrole;
    private ImageView editProfileImage;
    private TextView textUsername, textRole, textEmail;
    private Button add_btn, unadd_btn;

    private DatabaseReference table_parent, table_check, table_notify;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_profile);

        editProfileImage = findViewById(R.id.editProfileImage);
        textUsername = findViewById(R.id.textUsername);
        textRole = findViewById(R.id.textRole);
        textEmail = findViewById(R.id.textEmail);
        add_btn = findViewById(R.id.add_btn);
        unadd_btn = findViewById(R.id.unAdd_btn);

        SharedPreferences prefRole = getSharedPreferences("currRole", 0);
        currrole = prefRole.getString("role", null);

        Intent intent = getIntent();
        if (currrole.equals("Mother") || currrole.equals("Father")){
            cid = intent.getStringExtra("id");
            add_btn.setText("Add Caregiver");
            unadd_btn.setText("Cancel Add Caregiver");

            final DatabaseReference dataParent = FirebaseDatabase.getInstance().getReference("User").child(cid);
            dataParent.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        username = Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString();
                        role = Objects.requireNonNull(dataSnapshot.child("role").getValue()).toString();
                        email = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();
                        image = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();

                        textUsername.setText(username);
                        textRole.setText(role);
                        textEmail.setText(email);
                        if(!image.equals("none")){
                            Picasso.get().load(image).into(editProfileImage);
                        }else{
                            Picasso.get().load(R.drawable.user_image).into(editProfileImage);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else if (currrole.equals("Caregiver")){
            pid = intent.getStringExtra("id");

            final DatabaseReference dataParent = FirebaseDatabase.getInstance().getReference("User").child(pid);
            dataParent.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        username = Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString();
                        role = Objects.requireNonNull(dataSnapshot.child("role").getValue()).toString();
                        email = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();
                        image = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();

                        textUsername.setText(username);
                        textRole.setText(role);
                        textEmail.setText(email);
                        if(!image.equals("none")){
                            Picasso.get().load(image).into(editProfileImage);
                        }else{
                            Picasso.get().load(R.drawable.user_image).into(editProfileImage);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        //assigning store location in firebase
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        table_parent = db.getReference("ParentnCaregiverList");
        table_check = db.getReference("ParentnCaregiverList");
        table_notify = db.getReference().child("Notifications");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        uid = user.getUid().toString();

        setTitle("");

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                table_parent.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                ParentnCaregiverList parent = snapshot.getValue(ParentnCaregiverList.class);
                                if (currrole.equals("Mother") || currrole.equals("Father")){
                                    if ((parent.getParent_id().equals(uid) && parent.getCaregiver_id().equals(cid)) && (parent.getStatus().equals("0") || parent.getStatus().equals("2"))) {
                                        unadd_btn = findViewById(R.id.unAdd_btn);
                                        unadd_btn.setVisibility(View.VISIBLE);
                                        unadd_btn.setText("Cancel Add Caregiver");
                                        add_btn = findViewById(R.id.add_btn);
                                        add_btn.setVisibility(View.GONE);

                                        final String newid = parent.getId().toString();
                                        unadd_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                progressDialog = new ProgressDialog(SearchedProfile.this);
                                                progressDialog.setMessage("Please Wait...");
                                                progressDialog.show();

                                                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ParentnCaregiverList").child(newid);
                                                databaseReference.removeValue();

                                                progressDialog.dismiss();
                                            }
                                        });

                                    }else if ((parent.getParent_id().equals(uid) && parent.getCaregiver_id().equals(cid)) && parent.getStatus().equals("1")) {
                                        unadd_btn = findViewById(R.id.unAdd_btn);
                                        unadd_btn.setVisibility(View.VISIBLE);
                                        unadd_btn.setText("Delete Caregiver");
                                        add_btn = findViewById(R.id.add_btn);
                                        add_btn.setVisibility(View.GONE);

                                        final String newid = parent.getId().toString();
                                        unadd_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                progressDialog = new ProgressDialog(SearchedProfile.this);
                                                progressDialog.setMessage("Please Wait...");
                                                progressDialog.show();

                                                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ParentnCaregiverList").child(newid);
                                                databaseReference.removeValue();

                                                progressDialog.dismiss();
                                            }
                                        });
                                    }
                                }else if (currrole.equals("Caregiver")){
                                    if (parent.getParent_id().equals(pid) && parent.getCaregiver_id().equals(uid) && !parent.getStatus().equals("1")) {
                                        unadd_btn = findViewById(R.id.unAdd_btn);
                                        unadd_btn.setVisibility(View.VISIBLE);
                                        add_btn = findViewById(R.id.add_btn);
                                        add_btn.setVisibility(View.GONE);

                                        final String newid = parent.getId().toString();
                                        unadd_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                progressDialog = new ProgressDialog(SearchedProfile.this);
                                                progressDialog.setMessage("Please Wait...");
                                                progressDialog.show();

                                                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ParentnCaregiverList").child(newid);
                                                databaseReference.removeValue();
                                                databaseReference.keepSynced(true);
                                                progressDialog.dismiss();
                                            }
                                        });

                                    }else if (parent.getParent_id().equals(pid) && parent.getCaregiver_id().equals(uid) && parent.getStatus().equals("1")) {
                                        unadd_btn = findViewById(R.id.unAdd_btn);
                                        unadd_btn.setVisibility(View.VISIBLE);
                                        unadd_btn.setText("Delete Parent");
                                        add_btn = findViewById(R.id.add_btn);
                                        add_btn.setVisibility(View.GONE);

                                        final String newid = parent.getId().toString();
                                        unadd_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                progressDialog = new ProgressDialog(SearchedProfile.this);
                                                progressDialog.setMessage("Please Wait...");
                                                progressDialog.show();

                                                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ParentnCaregiverList").child(newid);
                                                databaseReference.removeValue();

                                                progressDialog.dismiss();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(SearchedProfile.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();

                String table_id = UUID.randomUUID().toString();
                if (currrole.equals("Mother") || currrole.equals("Father")){
                    ParentnCaregiverList parent = new ParentnCaregiverList(table_id, uid, cid, "2", "none");
                    table_parent.child(table_id).setValue(parent);
                    //request from parent
                    Notify notify = new Notify(cid, "request_parent", null);
                    table_notify.child(uid).push().setValue(notify);
                }else if (currrole.equals("Caregiver")){
                    ParentnCaregiverList parent = new ParentnCaregiverList(table_id, pid, uid, "0", "none");
                    table_parent.child(table_id).setValue(parent);
                    //request from caregiver
                    Notify notify = new Notify(pid, "request_caregiver", null);
                    table_notify.child(uid).push().setValue(notify);
                }


                progressDialog.dismiss();
            }
        });
    }
}
