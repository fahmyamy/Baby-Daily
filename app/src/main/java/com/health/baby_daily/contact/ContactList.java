package com.health.baby_daily.contact;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.adapter.CaregiverAdapter;
import com.health.baby_daily.adapter.DoctorAdapter;
import com.health.baby_daily.adapter.FriendAdapter;
import com.health.baby_daily.chatroom.ChatRoom;
import com.health.baby_daily.model.Follow;
import com.health.baby_daily.model.ParentnCaregiverList;
import com.health.baby_daily.model.ParentnDoctorList;

import java.util.ArrayList;
import java.util.List;

public class ContactList extends AppCompatActivity {

    private RecyclerView entry_caregiver;
    private CaregiverAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView no_caregiver_text, countRequest;
    private DatabaseReference table_caregiver;
    private Query query;

    private RecyclerView entry_doctor;
    private DoctorAdapter mAdapterD;
    private RecyclerView.LayoutManager mLayoutManagerD;
    private TextView no_doctor_text, countRequestD;
    private DatabaseReference table_doctor;
    private Query queryD;

    private RecyclerView entry_friend;
    private FriendAdapter mAdapterF;
    private RecyclerView.LayoutManager mLayoutManagerF;
    private TextView no_friend_text;
    private DatabaseReference table_friend;
    private Query queryF;

    private FirebaseAuth firebaseAuth;
    private String id, role;
    private TextView roleText, roleText2;
    private String content;

    private LinearLayout lineA, lineB;

    private List<ParentnCaregiverList> requestList;
    private List<ParentnDoctorList> doctorList;
    private List<Follow> followList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefRole = getSharedPreferences("currRole", 0);
        role = prefRole.getString("role", null);

        setTitle("Select Contact");

        Intent intent = getIntent();
        if (intent != null){
            content = intent.getStringExtra("share_content");
        }else {
            content = "none";
        }

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        id = user.getUid().toString();

        requestList = new ArrayList<>();
        doctorList = new ArrayList<>();

        lineA = findViewById(R.id.lineA);
        lineB = findViewById(R.id.lineB);

        //caregiver
        table_caregiver = FirebaseDatabase.getInstance().getReference().child("ParentnCaregiverList");
        table_caregiver.keepSynced(true);
        query = table_caregiver.orderByChild("id");
        query.keepSynced(true);

        roleText = findViewById(R.id.roleText);
        roleText2 = findViewById(R.id.roleText2);
        entry_caregiver= findViewById(R.id.entry_caregiver);
        entry_caregiver.setHasFixedSize(true);
        no_caregiver_text = findViewById(R.id.no_caregiver_text);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new CaregiverAdapter(requestList);

        entry_caregiver.setLayoutManager(mLayoutManager);
        entry_caregiver.setAdapter(mAdapter);

        //doctor
        table_doctor = FirebaseDatabase.getInstance().getReference().child("ParentnDoctorList");
        table_doctor.keepSynced(true);
        queryD = table_doctor.orderByChild("id");
        queryD.keepSynced(true);

        entry_doctor = findViewById(R.id.entry_doctor);
        entry_doctor.setHasFixedSize(true);
        no_doctor_text = findViewById(R.id.no_doctor_text);

        mLayoutManagerD = new LinearLayoutManager(this);
        mAdapterD = new DoctorAdapter(doctorList);

        entry_doctor.setLayoutManager(mLayoutManagerD);
        entry_doctor.setAdapter(mAdapterD);

        if (role.equals("Mother") || role.equals("Father")){
            roleText.setText("Caregiver");
            mAdapter.setOnItemClickListener(new CaregiverAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    String second_id = requestList.get(position).getCaregiver_id();
                    Intent intent = new Intent(ContactList.this, ChatRoom.class);
                    intent.putExtra("second_id", second_id);
                    intent.putExtra("share_content", content);
                    startActivity(intent);
                    finish();
                }
            });

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    requestList.clear();
                    if(dataSnapshot.exists()){
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            ParentnCaregiverList request = snapshot.getValue(ParentnCaregiverList.class);
                            if (request.getParent_id().equals(id) & request.getStatus().equals("1")) {
                                requestList.add(request);
                            }
                        }

                        if (requestList.isEmpty()){
                            no_caregiver_text.setVisibility(View.VISIBLE);
                        }else {
                            no_caregiver_text.setVisibility(View.GONE);
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            mAdapterD.setOnItemClickListener(new DoctorAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    String second_id = doctorList.get(position).getDoctor_id();
                    Intent intent = new Intent(ContactList.this, ChatRoom.class);
                    intent.putExtra("second_id", second_id);
                    intent.putExtra("share_content", content);
                    startActivity(intent);
                    finish();
                }
            });

            queryD.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    doctorList.clear();
                    if(dataSnapshot.exists()){
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            ParentnDoctorList request = snapshot.getValue(ParentnDoctorList.class);
                            if (request.getParent_id().equals(id) & request.getStatus().equals("1")) {
                                doctorList.add(request);
                            }
                        }

                        if (doctorList.isEmpty()){
                            no_doctor_text.setVisibility(View.VISIBLE);
                        }else {
                            no_doctor_text.setVisibility(View.GONE);
                        }

                        mAdapterD.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }else if (role.equals("Caregiver")){
            roleText.setText("Parent");
            lineB.setVisibility(View.GONE);
            mAdapter.setOnItemClickListener(new CaregiverAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    String second_id = requestList.get(position).getParent_id();
                    Intent intent = new Intent(ContactList.this, ChatRoom.class);
                    intent.putExtra("second_id", second_id);
                    intent.putExtra("share_content", content);
                    startActivity(intent);
                    finish();
                }
            });

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    requestList.clear();
                    if(dataSnapshot.exists()){
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            ParentnCaregiverList request = snapshot.getValue(ParentnCaregiverList.class);
                            if (request.getCaregiver_id().equals(id) & request.getStatus().equals("1")) {
                                requestList.add(request);
                            }
                        }

                        if (requestList.isEmpty()){
                            no_caregiver_text.setVisibility(View.VISIBLE);
                        }else {
                            no_caregiver_text.setVisibility(View.GONE);
                            no_caregiver_text.setText("No Parent Yet!");
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else if (role.equals("Doctor")){
            lineA.setVisibility(View.GONE);
            roleText2.setText("Parent");
            mAdapterD.setOnItemClickListener(new DoctorAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    String second_id = doctorList.get(position).getParent_id();
                    Intent intent = new Intent(ContactList.this, ChatRoom.class);
                    intent.putExtra("second_id", second_id);
                    intent.putExtra("share_content", content);
                    startActivity(intent);
                    finish();
                }
            });

            queryD.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    doctorList.clear();
                    if(dataSnapshot.exists()){
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            ParentnDoctorList request = snapshot.getValue(ParentnDoctorList.class);
                            if (request.getDoctor_id().equals(id) & request.getStatus().equals("1")) {
                                doctorList.add(request);
                            }
                        }

                        if (doctorList.isEmpty()){
                            no_doctor_text.setVisibility(View.VISIBLE);
                        }else {
                            no_doctor_text.setVisibility(View.GONE);
                        }

                        mAdapterD.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            LinearLayout lineC = findViewById(R.id.lineC);
            lineC.setVisibility(View.VISIBLE);
            entry_friend = findViewById(R.id.entry_friend);
            entry_friend.setHasFixedSize(true);
            no_friend_text = findViewById(R.id.no_friend_text);
            followList = new ArrayList<>();

            mLayoutManagerF = new LinearLayoutManager(ContactList.this);
            mAdapterF = new FriendAdapter(followList);

            entry_friend.setLayoutManager(mLayoutManagerF);
            entry_friend.setAdapter(mAdapterF);

            mAdapterF.setOnItemClickListener(new FriendAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    String second_id = followList.get(position).getFollow_id();
                    Intent intent = new Intent(ContactList.this, ChatRoom.class);
                    intent.putExtra("second_id", second_id);
                    intent.putExtra("share_content", content);
                    startActivity(intent);
                }
            });

            table_friend = FirebaseDatabase.getInstance().getReference("Follow");
            table_friend.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    followList.clear();
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Follow follow = snapshot.getValue(Follow.class);
                            if (follow.getUser_id().equals(id)){
                                followList.add(follow);
                            }
                        }

                        if (followList.isEmpty()){
                            no_friend_text.setVisibility(View.VISIBLE);
                        }else {
                            no_friend_text.setVisibility(View.GONE);
                        }
                        mAdapterF.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
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
