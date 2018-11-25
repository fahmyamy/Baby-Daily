package com.health.baby_daily.contact;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.adapter.DoctorAdapter;
import com.health.baby_daily.adapter.RequestDoctorAdapter;
import com.health.baby_daily.chatroom.ChatRoom;
import com.health.baby_daily.model.ParentnDoctorList;

import java.util.ArrayList;
import java.util.List;

public class DoctorActivity extends AppCompatActivity {

    private FloatingActionButton add_doctor;
    private RecyclerView entry_request;
    private RequestDoctorAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView no_request_text, countRequest;
    private DatabaseReference table_request;
    private Query query;

    private RecyclerView entry_doctor;
    private DoctorAdapter mAdapterB;
    private RecyclerView.LayoutManager mLayoutManagerB;
    private TextView no_doctor_text;
    private DatabaseReference table_doctor;
    private Query queryB;

    private FirebaseAuth firebaseAuth;
    private String id;

    private List<ParentnDoctorList> requestList;
    private List<ParentnDoctorList> requestListB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        setTitle("Doctor");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        id = user.getUid().toString();

        add_doctor = findViewById(R.id.add_doctor);
        add_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorActivity.this, SearchParentV2.class));
            }
        });

        requestList = new ArrayList<>();
        requestListB = new ArrayList<>();

        //requestlist doctor
        table_request = FirebaseDatabase.getInstance().getReference().child("ParentnDoctorList");
        table_request.keepSynced(true);
        query = table_request.orderByChild("id");
        query.keepSynced(true);

        entry_request= findViewById(R.id.entry_request);
        entry_request.setHasFixedSize(true);
        no_request_text = findViewById(R.id.no_request_text);
        countRequest = findViewById(R.id.countRequest);
        countRequest.setText("0");

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RequestDoctorAdapter(requestList);

        entry_request.setLayoutManager(mLayoutManager);
        entry_request.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RequestDoctorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });

        //doctor
        table_doctor = FirebaseDatabase.getInstance().getReference().child("ParentnDoctorList");
        table_doctor.keepSynced(true);
        queryB = table_doctor.orderByChild("id");
        queryB.keepSynced(true);

        entry_doctor= findViewById(R.id.entry_doctor);
        entry_doctor.setHasFixedSize(true);
        no_doctor_text = findViewById(R.id.no_doctor_text);

        mLayoutManagerB = new LinearLayoutManager(this);
        mAdapterB = new DoctorAdapter(requestListB);

        entry_doctor.setLayoutManager(mLayoutManagerB);
        entry_doctor.setAdapter(mAdapterB);

        mAdapterB.setOnItemClickListener(new DoctorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                final String[] menu = {"View Profile", "Chat", "Unfriend"};

                AlertDialog.Builder builder = new AlertDialog.Builder(DoctorActivity.this);
                builder.setItems(menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (menu[which].equals("View Profile")){
                            Intent intent = new Intent(DoctorActivity.this, SearchedProfileV2.class);
                            intent.putExtra("id", requestListB.get(position).getDoctor_id());
                            startActivity(intent);
                        }else if (menu[which].equals("Chat")){
                            Intent intent = new Intent(DoctorActivity.this, ChatRoom.class);
                            intent.putExtra("second_id", requestListB.get(position).getDoctor_id());
                            intent.putExtra("share_content", "none");
                            startActivity(intent);
                        }if (menu[which].equals("Unfriend")){
                            DatabaseReference table_doctor = FirebaseDatabase.getInstance().getReference("ParentnDoctorList");
                            table_doctor.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                            ParentnDoctorList doctorList = snapshot.getValue(ParentnDoctorList.class);
                                            if (doctorList.getParent_id().equals(id)
                                                    && doctorList.getDoctor_id().equals(requestListB.get(position).getDoctor_id())
                                                    && doctorList.getStatus().equals("1")){

                                                String newid = doctorList.getId();
                                                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ParentnDoctorList").child(newid);
                                                databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        mAdapterB.notifyItemRemoved(position);
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
                        }
                    }
                });
                builder.show();
            }
        });

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        requestList.clear();
                        if(dataSnapshot.exists()){
                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                ParentnDoctorList request = snapshot.getValue(ParentnDoctorList.class);
                                if (request.getParent_id().equals(id) & request.getStatus().equals("0")) {
                                    requestList.add(request);
                                }
                            }
                            int count = requestList.size();
                            countRequest.setText(String.valueOf(count));

                            if (requestList.isEmpty()){
                                no_request_text.setVisibility(View.VISIBLE);
                                countRequest.setText("0");
                            }else {
                                no_request_text.setVisibility(View.GONE);
                            }

                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                queryB.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        requestListB.clear();
                        if(dataSnapshot.exists()){
                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                ParentnDoctorList request = snapshot.getValue(ParentnDoctorList.class);
                                if (request.getParent_id().equals(id) & request.getStatus().equals("1")) {
                                    requestListB.add(request);
                                }
                            }

                            if (requestListB.isEmpty()){
                                no_doctor_text.setVisibility(View.VISIBLE);
                            }else {
                                no_doctor_text.setVisibility(View.GONE);
                            }

                            mAdapterB.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
