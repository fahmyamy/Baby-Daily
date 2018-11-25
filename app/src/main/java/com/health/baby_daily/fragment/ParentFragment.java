package com.health.baby_daily.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.health.baby_daily.adapter.CaregiverAdapter;
import com.health.baby_daily.adapter.DoctorAdapter;
import com.health.baby_daily.adapter.RequestCaregiverAdapter;
import com.health.baby_daily.adapter.RequestDoctorAdapter;
import com.health.baby_daily.chatroom.ChatRoom;
import com.health.baby_daily.contact.SearchedProfile;
import com.health.baby_daily.contact.SearchedProfileV2;
import com.health.baby_daily.model.ParentnCaregiverList;
import com.health.baby_daily.model.ParentnDoctorList;

import java.util.ArrayList;
import java.util.List;

public class ParentFragment extends Fragment {

    private RecyclerView entry_request;
    private RequestCaregiverAdapter mAdapter;
    private RequestDoctorAdapter mAdapter2;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView no_request_text, countRequest;
    private DatabaseReference table_request;
    private DatabaseReference table_request2;
    private Query query;
    private Query query2;

    private RecyclerView entry_caregiver;
    private CaregiverAdapter mAdapterB;
    private DoctorAdapter mAdapterB2;
    private RecyclerView.LayoutManager mLayoutManagerB;
    private TextView no_caregiver_text;
    private DatabaseReference table_caregiver;
    private DatabaseReference table_doctor;
    private Query queryB;
    private Query queryB2;

    private FirebaseAuth firebaseAuth;
    private String id, role;

    private List<ParentnCaregiverList> requestList;
    private List<ParentnCaregiverList> requestListB;

    private List<ParentnDoctorList> requestList2;
    private List<ParentnDoctorList> requestListB2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_parent, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        id = user.getUid().toString();

        SharedPreferences prefRole = getContext().getSharedPreferences("currRole", 0);
        role = prefRole.getString("role", null);

        entry_request= view.findViewById(R.id.entry_request);
        entry_request.setHasFixedSize(true);
        no_request_text = view.findViewById(R.id.no_request_text);
        countRequest = view.findViewById(R.id.countRequest);
        countRequest.setText("0");

        entry_caregiver= view.findViewById(R.id.entry_caregiver);
        entry_caregiver.setHasFixedSize(true);
        no_caregiver_text = view.findViewById(R.id.no_caregiver_text);

        if (role.equals("Caregiver")){

            requestList = new ArrayList<>();
            requestListB = new ArrayList<>();

            //requestlist caregiver
            table_request = FirebaseDatabase.getInstance().getReference().child("ParentnCaregiverList");
            table_request.keepSynced(true);
            query = table_request.orderByChild("id");
            query.keepSynced(true);

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

            mLayoutManagerB = new LinearLayoutManager(view.getContext());
            mAdapterB = new CaregiverAdapter(requestListB);

            entry_caregiver.setLayoutManager(mLayoutManagerB);
            entry_caregiver.setAdapter(mAdapterB);

            mAdapterB.setOnItemClickListener(new CaregiverAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(final int position) {
                    final String[] menu = {"View Profile", "Chat", "Unfriend"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setItems(menu, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (menu[which].equals("View Profile")){
                                Intent intent = new Intent(getContext(), SearchedProfile.class);
                                intent.putExtra("id", requestListB.get(position).getParent_id());
                                startActivity(intent);
                            }else if (menu[which].equals("Chat")){
                                Intent intent = new Intent(getContext(), ChatRoom.class);
                                intent.putExtra("second_id", requestListB.get(position).getParent_id());
                                startActivity(intent);
                            }if (menu[which].equals("Unfriend")){
                                DatabaseReference table_doctor = FirebaseDatabase.getInstance().getReference("ParentnCaregiverList");
                                table_doctor.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                ParentnDoctorList doctorList = snapshot.getValue(ParentnDoctorList.class);
                                                if (doctorList.getParent_id().equals(id)
                                                        && doctorList.getDoctor_id().equals(requestListB.get(position).getParent_id())
                                                        && doctorList.getStatus().equals("1")){

                                                    String newid = doctorList.getId();
                                                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ParentnCaregiverList").child(newid);
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

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    requestList.clear();
                    if(dataSnapshot.exists()){
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            ParentnCaregiverList request = snapshot.getValue(ParentnCaregiverList.class);
                            if (request.getCaregiver_id().equals(id) & request.getStatus().equals("2")) {
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
                            ParentnCaregiverList request = snapshot.getValue(ParentnCaregiverList.class);
                            if (request.getCaregiver_id().equals(id) & request.getStatus().equals("1")) {
                                requestListB.add(request);
                            }
                        }

                        if (requestListB.isEmpty()){
                            no_caregiver_text.setVisibility(View.VISIBLE);
                        }else {
                            no_caregiver_text.setVisibility(View.GONE);
                        }

                        mAdapterB.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else if (role.equals("Doctor")){
            requestList2 = new ArrayList<>();
            requestListB2 = new ArrayList<>();

            //requestlist parent
            table_request2 = FirebaseDatabase.getInstance().getReference().child("ParentnDoctorList");
            table_request2.keepSynced(true);
            query2 = table_request2.orderByChild("id");
            query2.keepSynced(true);

            mLayoutManager = new LinearLayoutManager(view.getContext());
            mAdapter2 = new RequestDoctorAdapter(requestList2);

            entry_request.setLayoutManager(mLayoutManager);
            entry_request.setAdapter(mAdapter2);

            mAdapter2.setOnItemClickListener(new RequestDoctorAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {

                }
            });

            //parent
            table_doctor = FirebaseDatabase.getInstance().getReference().child("ParentnDoctorList");
            table_doctor.keepSynced(true);
            queryB2 = table_doctor.orderByChild("id");
            queryB2.keepSynced(true);

            mLayoutManagerB = new LinearLayoutManager(view.getContext());
            mAdapterB2 = new DoctorAdapter(requestListB2);

            entry_caregiver.setLayoutManager(mLayoutManagerB);
            entry_caregiver.setAdapter(mAdapterB2);

            mAdapterB2.setOnItemClickListener(new DoctorAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(final int position) {
                    final String[] menu = {"View Profile", "Chat", "Unfriend"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setItems(menu, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (menu[which].equals("View Profile")){
                                Intent intent = new Intent(getContext(), SearchedProfileV2.class);
                                intent.putExtra("id", requestListB2.get(position).getParent_id());
                                startActivity(intent);
                            }else if (menu[which].equals("Chat")){
                                Intent intent = new Intent(getContext(), ChatRoom.class);
                                intent.putExtra("second_id", requestListB2.get(position).getParent_id());
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
                                                        && doctorList.getDoctor_id().equals(requestListB2.get(position).getParent_id())
                                                        && doctorList.getStatus().equals("1")){

                                                    String newid = doctorList.getId();
                                                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ParentnDoctorList").child(newid);
                                                    databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            mAdapterB2.notifyItemRemoved(position);
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

            query2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    requestList2.clear();
                    if(dataSnapshot.exists()){
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            ParentnDoctorList request = snapshot.getValue(ParentnDoctorList.class);
                            if (request.getDoctor_id().equals(id) & request.getStatus().equals("2")) {
                                requestList2.add(request);
                            }
                        }
                        int count = requestList2.size();
                        countRequest.setText(String.valueOf(count));

                        if (requestList2.isEmpty()){
                            no_request_text.setVisibility(View.VISIBLE);
                            countRequest.setText("0");
                        }else {
                            no_request_text.setVisibility(View.GONE);
                        }

                        mAdapter2.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            queryB2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    requestListB2.clear();
                    if(dataSnapshot.exists()){
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            ParentnDoctorList request = snapshot.getValue(ParentnDoctorList.class);
                            if (request.getDoctor_id().equals(id) & request.getStatus().equals("1")) {
                                requestListB2.add(request);
                            }
                        }

                        if (requestListB2.isEmpty()){
                            no_caregiver_text.setVisibility(View.VISIBLE);
                        }else {
                            no_caregiver_text.setVisibility(View.GONE);
                        }

                        mAdapterB2.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



    }

}
