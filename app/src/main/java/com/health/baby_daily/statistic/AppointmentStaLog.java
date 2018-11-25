package com.health.baby_daily.statistic;

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

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.adapter.AppointmentLogAdapter;
import com.health.baby_daily.event.appointmentEvent;
import com.health.baby_daily.model.Appointment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppointmentStaLog extends Fragment {
    private View view;
    private RecyclerView entry_appointmentList;
    private AppointmentLogAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView no_appointment_text;
    private DatabaseReference table_appointment;
    private Query query;
    private FloatingActionButton add_btn;

    private String bId;

    private List<Appointment> mAppointmentList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.appointment_sta_log, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefBaby = getContext().getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        mAppointmentList = new ArrayList<>();

        table_appointment = FirebaseDatabase.getInstance().getReference().child("Appointment");
        query = table_appointment.orderByChild("dateTime");
        query.keepSynced(true);

        entry_appointmentList = view.findViewById(R.id.entry_appointmentList);
        entry_appointmentList.setHasFixedSize(true);
        no_appointment_text = view.findViewById(R.id.no_appointment_text);
        add_btn = view.findViewById(R.id.add_btn);

        add_btn.setVisibility(View.GONE);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), appointmentEvent.class));
            }
        });

        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new AppointmentLogAdapter(mAppointmentList);

        entry_appointmentList.setLayoutManager(mLayoutManager);
        entry_appointmentList.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AppointmentLogAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String id = mAppointmentList.get(position).getId();
                Intent intent = new Intent(getActivity(), AppointmentStaEdit.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mAppointmentList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Appointment appointment = snapshot.getValue(Appointment.class);
                        if (appointment.getBaby_id().equals(bId)) {
                            mAppointmentList.add(appointment);
                        }
                    }
                    no_appointment_text.setVisibility(View.GONE);
                    Collections.reverse(mAppointmentList);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
