package com.health.baby_daily.chatfragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.health.baby_daily.adapter.DoctorAdapter;
import com.health.baby_daily.adapter.RequestDoctorAdapter;
import com.health.baby_daily.chatroom.ChatRoom;
import com.health.baby_daily.model.ParentnDoctorList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorFragment extends Fragment {
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
    private String id, role;

    private List<ParentnDoctorList> requestList;
    private List<ParentnDoctorList> requestListB;

    private TextView textRequest, textRole;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        id = user.getUid().toString();

        SharedPreferences prefRole = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            prefRole = Objects.requireNonNull(getActivity()).getSharedPreferences("currRole", 0);
            role = prefRole.getString("role", null);
        }

        requestList = new ArrayList<>();
        requestListB = new ArrayList<>();

        //requestlist doctor
        table_request = FirebaseDatabase.getInstance().getReference().child("ParentnDoctorList");
        table_request.keepSynced(true);
        query = table_request.orderByChild("id");
        query.keepSynced(true);

        textRequest = view.findViewById(R.id.textRequest);
        textRole = view.findViewById(R.id.textRole);

        entry_request = view.findViewById(R.id.entry_request);
        entry_request.setHasFixedSize(true);
        no_request_text = view.findViewById(R.id.no_request_text);
        countRequest = view.findViewById(R.id.countRequest);
        countRequest.setText("0");

        mLayoutManager = new LinearLayoutManager(view.getContext());
        mAdapter = new RequestDoctorAdapter(requestList);

        entry_request.setLayoutManager(mLayoutManager);
        entry_request.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RequestDoctorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });

        //caregiver
        table_doctor = FirebaseDatabase.getInstance().getReference().child("ParentnDoctorList");
        table_doctor.keepSynced(true);
        queryB = table_doctor.orderByChild("id");
        queryB.keepSynced(true);

        entry_doctor = view.findViewById(R.id.entry_doctor);
        entry_doctor.setHasFixedSize(true);
        no_doctor_text = view.findViewById(R.id.no_doctor_text);

        mLayoutManagerB = new LinearLayoutManager(view.getContext());
        mAdapterB = new DoctorAdapter(requestListB);

        entry_doctor.setLayoutManager(mLayoutManagerB);
        entry_doctor.setAdapter(mAdapterB);

        if (role.equals("Mother") || role.equals("Father")) {
            mAdapterB.setOnItemClickListener(new DoctorAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    String second_id = requestListB.get(position).getDoctor_id();
                    Intent intent = new Intent(getContext(), ChatRoom.class);
                    intent.putExtra("second_id", second_id);
                    intent.putExtra("share_content", "none");
                    startActivity(intent);
                }
            });

            textRequest.setText("Doctor Requests");
            textRole.setText("Doctors");
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            requestList.clear();
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    ParentnDoctorList request = snapshot.getValue(ParentnDoctorList.class);
                                    if (request.getParent_id().equals(id) & request.getStatus().equals("0")) {
                                        requestList.add(request);
                                    }
                                }
                                int count = requestList.size();
                                countRequest.setText(String.valueOf(count));

                                if (requestList.isEmpty()) {
                                    no_request_text.setVisibility(View.VISIBLE);
                                    countRequest.setText("0");
                                } else {
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
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    ParentnDoctorList request = snapshot.getValue(ParentnDoctorList.class);
                                    if (request.getParent_id().equals(id) & request.getStatus().equals("1")) {
                                        requestListB.add(request);
                                    }
                                }

                                if (requestListB.isEmpty()) {
                                    no_doctor_text.setVisibility(View.VISIBLE);
                                } else {
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
        }else if (role.equals("Doctor")) {
            mAdapterB.setOnItemClickListener(new DoctorAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    String second_id = requestListB.get(position).getParent_id();
                    Intent intent = new Intent(getContext(), ChatRoom.class);
                    intent.putExtra("second_id", second_id);
                    intent.putExtra("share_content", "none");
                    startActivity(intent);
                }
            });

            textRequest.setText("Parent Requests");
            textRole.setText("Parents");
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            requestList.clear();
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    ParentnDoctorList request = snapshot.getValue(ParentnDoctorList.class);
                                    if (request.getDoctor_id().equals(id) & request.getStatus().equals("2")) {
                                        requestList.add(request);
                                    }
                                }
                                int count = requestList.size();
                                countRequest.setText(String.valueOf(count));

                                if (requestList.isEmpty()) {
                                    no_request_text.setVisibility(View.VISIBLE);
                                    countRequest.setText("0");
                                } else {
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
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    ParentnDoctorList request = snapshot.getValue(ParentnDoctorList.class);
                                    if (request.getDoctor_id().equals(id) & request.getStatus().equals("1")) {
                                        requestListB.add(request);
                                    }
                                }

                                if (requestListB.isEmpty()) {
                                    no_doctor_text.setVisibility(View.VISIBLE);
                                    no_doctor_text.setText("No Parent Yet!");
                                } else {
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
}
