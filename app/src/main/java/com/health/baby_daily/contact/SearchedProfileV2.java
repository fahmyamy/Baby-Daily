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
import com.health.baby_daily.model.Follow;
import com.health.baby_daily.model.Notify;
import com.health.baby_daily.model.ParentnDoctorList;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.UUID;

public class SearchedProfileV2 extends AppCompatActivity {

    private String pid, did, username, role, email, image;
    private String uid;
    private String currrole;
    private ImageView editProfileImage;
    private TextView textUsername, textRole, textEmail;
    private Button add_btn, unadd_btn;

    private DatabaseReference table_parent, table_follow, table_notify;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_profile_v2);
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
            did = intent.getStringExtra("id");
            add_btn.setText("Add Doctor");
            unadd_btn.setText("Cancel Add Doctor");

            final DatabaseReference dataParent = FirebaseDatabase.getInstance().getReference("User").child(did);
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

        }else if (currrole.equals("Doctor")){
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
        table_parent = db.getReference("ParentnDoctorList");
        table_follow = db.getReference("Follow");
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
                                final ParentnDoctorList parent = snapshot.getValue(ParentnDoctorList.class);

                                if (currrole.equals("Mother") || currrole.equals("Father")){
                                    if ((parent.getParent_id().equals(uid) && parent.getDoctor_id().equals(did)) && (parent.getStatus().equals("0") || parent.getStatus().equals("2"))) {
                                        unadd_btn = findViewById(R.id.unAdd_btn);
                                        unadd_btn.setVisibility(View.VISIBLE);
                                        unadd_btn.setText("Cancel Add Doctor");
                                        add_btn = findViewById(R.id.add_btn);
                                        add_btn.setVisibility(View.GONE);

                                        final String newid = parent.getId().toString();
                                        unadd_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                progressDialog = new ProgressDialog(SearchedProfileV2.this);
                                                progressDialog.setMessage("Please Wait...");
                                                progressDialog.show();

                                                final DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Follow").child(newid);
                                                databaseReference2.removeValue();

                                                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ParentnDoctorList").child(newid);
                                                databaseReference.removeValue();

                                                unadd_btn.setVisibility(View.GONE);
                                                add_btn.setVisibility(View.VISIBLE);

                                                progressDialog.dismiss();
                                            }
                                        });

                                    }else if ((parent.getParent_id().equals(uid) && parent.getDoctor_id().equals(did)) && parent.getStatus().equals("1")) {
                                        unadd_btn = findViewById(R.id.unAdd_btn);
                                        unadd_btn.setVisibility(View.VISIBLE);
                                        unadd_btn.setText("Delete Doctor");
                                        add_btn = findViewById(R.id.add_btn);
                                        add_btn.setVisibility(View.GONE);

                                        final String newid = parent.getId().toString();
                                        unadd_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                progressDialog = new ProgressDialog(SearchedProfileV2.this);
                                                progressDialog.setMessage("Please Wait...");
                                                progressDialog.show();

                                                final DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Follow").child(newid);
                                                databaseReference2.removeValue();

                                                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ParentnDoctorList").child(newid);
                                                databaseReference.removeValue();

                                                unadd_btn.setVisibility(View.GONE);
                                                add_btn.setVisibility(View.VISIBLE);

                                                progressDialog.dismiss();
                                            }
                                        });
                                    }
                                }else if (currrole.equals("Doctor")){
                                    if ((parent.getParent_id().equals(pid) && parent.getDoctor_id().equals(uid)) && (parent.getStatus().equals("0") || parent.getStatus().equals("2"))) {
                                        unadd_btn = findViewById(R.id.unAdd_btn);
                                        unadd_btn.setVisibility(View.VISIBLE);
                                        unadd_btn.setText("Cancel Add Parent");
                                        add_btn = findViewById(R.id.add_btn);
                                        add_btn.setVisibility(View.GONE);

                                        final String newid = parent.getId().toString();
                                        unadd_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                progressDialog = new ProgressDialog(SearchedProfileV2.this);
                                                progressDialog.setMessage("Please Wait...");
                                                progressDialog.show();

                                                final DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Follow").child(newid);
                                                databaseReference2.removeValue();

                                                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ParentnDoctorList").child(newid);
                                                databaseReference.removeValue();
                                                databaseReference.keepSynced(true);

                                                unadd_btn.setVisibility(View.GONE);
                                                add_btn.setVisibility(View.VISIBLE);

                                                progressDialog.dismiss();
                                            }
                                        });

                                    }else if (parent.getParent_id().equals(pid) && parent.getDoctor_id().equals(uid) && parent.getStatus().equals("1")) {
                                        unadd_btn = findViewById(R.id.unAdd_btn);
                                        unadd_btn.setVisibility(View.VISIBLE);
                                        unadd_btn.setText("Delete Parent");
                                        add_btn = findViewById(R.id.add_btn);
                                        add_btn.setVisibility(View.GONE);

                                        final String newid = parent.getId().toString();
                                        unadd_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                progressDialog = new ProgressDialog(SearchedProfileV2.this);
                                                progressDialog.setMessage("Please Wait...");
                                                progressDialog.show();

                                                final DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Follow").child(newid);
                                                databaseReference2.removeValue();

                                                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ParentnDoctorList").child(newid);
                                                databaseReference.removeValue();

                                                unadd_btn.setVisibility(View.GONE);
                                                add_btn.setVisibility(View.VISIBLE);

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
                progressDialog = new ProgressDialog(SearchedProfileV2.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();

                String table_id = UUID.randomUUID().toString();
                if (currrole.equals("Mother") || currrole.equals("Father")){
                    ParentnDoctorList parent = new ParentnDoctorList(table_id, uid, did, "2", "none");
                    table_parent.child(table_id).setValue(parent);
                    Follow follow = new Follow(table_id, uid, did, "none");
                    table_follow.child(table_id).setValue(follow);
                    Notify notify = new Notify(uid, "request_pDoctor", null);
                    table_notify.child(did).push().setValue(notify);
                }else if (currrole.equals("Doctor")){
                    ParentnDoctorList parent = new ParentnDoctorList(table_id, pid, uid, "0", "none");
                    table_parent.child(table_id).setValue(parent);
                    Follow follow = new Follow(table_id, pid, uid, "none");
                    table_follow.child(table_id).setValue(follow);
                    Notify notify = new Notify(uid, "request_pParent", null);
                    table_notify.child(pid).push().setValue(notify);
                }

                progressDialog.dismiss();
            }
        });
    }
}
