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
import com.health.baby_daily.adapter.CaregiverAdapter;
import com.health.baby_daily.adapter.RequestCaregiverAdapter;
import com.health.baby_daily.chatroom.ChatRoom;
import com.health.baby_daily.model.ParentnCaregiverList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class CGFragment extends Fragment {

    private RecyclerView entry_request;
    private RequestCaregiverAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView no_request_text, countRequest;
    private DatabaseReference table_request;
    private Query query;

    private RecyclerView entry_caregiver;
    private CaregiverAdapter mAdapterB;
    private RecyclerView.LayoutManager mLayoutManagerB;
    private TextView no_caregiver_text;
    private DatabaseReference table_caregiver;
    private Query queryB;

    private FirebaseAuth firebaseAuth;
    private String id, role;

    private List<ParentnCaregiverList> requestList;
    private List<ParentnCaregiverList> requestListB;

    private TextView textRequest, textRole;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cg, container, false);
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

        //requestlist caregiver
        table_request = FirebaseDatabase.getInstance().getReference().child("ParentnCaregiverList");
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
        mAdapter = new RequestCaregiverAdapter(requestList);

        entry_request.setLayoutManager(mLayoutManager);
        entry_request.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RequestCaregiverAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });

        //caregiver
        table_caregiver = FirebaseDatabase.getInstance().getReference().child("ParentnCaregiverList");
        table_caregiver.keepSynced(true);
        queryB = table_caregiver.orderByChild("id");
        queryB.keepSynced(true);

        entry_caregiver = view.findViewById(R.id.entry_caregiver);
        entry_caregiver.setHasFixedSize(true);
        no_caregiver_text = view.findViewById(R.id.no_caregiver_text);

        mLayoutManagerB = new LinearLayoutManager(view.getContext());
        mAdapterB = new CaregiverAdapter(requestListB);

        entry_caregiver.setLayoutManager(mLayoutManagerB);
        entry_caregiver.setAdapter(mAdapterB);

        if (role.equals("Mother") || role.equals("Father")) {
            mAdapterB.setOnItemClickListener(new CaregiverAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    String second_id = requestListB.get(position).getCaregiver_id();
                    Intent intent = new Intent(getContext(), ChatRoom.class);
                    intent.putExtra("second_id", second_id);
                    intent.putExtra("share_content", "none");
                    startActivity(intent);
                }
            });

            textRequest.setText("Caregiver Requests");
            textRole.setText("Caregivers");
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
                                    ParentnCaregiverList request = snapshot.getValue(ParentnCaregiverList.class);
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
                                    ParentnCaregiverList request = snapshot.getValue(ParentnCaregiverList.class);
                                    if (request.getParent_id().equals(id) & request.getStatus().equals("1")) {
                                        requestListB.add(request);
                                    }
                                }

                                if (requestListB.isEmpty()) {
                                    no_caregiver_text.setVisibility(View.VISIBLE);
                                } else {
                                    no_caregiver_text.setVisibility(View.GONE);
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
        }else if (role.equals("Caregiver")){
            mAdapterB.setOnItemClickListener(new CaregiverAdapter.OnItemClickListener() {
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
                                    ParentnCaregiverList request = snapshot.getValue(ParentnCaregiverList.class);
                                    if (request.getCaregiver_id().equals(id) & request.getStatus().equals("2")) {
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
                                    ParentnCaregiverList request = snapshot.getValue(ParentnCaregiverList.class);
                                    if (request.getCaregiver_id().equals(id) & request.getStatus().equals("1")) {
                                        requestListB.add(request);
                                    }
                                }

                                if (requestListB.isEmpty()) {
                                    no_caregiver_text.setVisibility(View.VISIBLE);
                                } else {
                                    no_caregiver_text.setVisibility(View.GONE);
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
