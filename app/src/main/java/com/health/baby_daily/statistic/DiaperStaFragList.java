package com.health.baby_daily.statistic;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.adapter.DiaperStatisticAdapter;
import com.health.baby_daily.model.Diaper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DiaperStaFragList extends Fragment {
    private View view;
    private String bId;

    private RecyclerView recyclerTable;
    ProgressDialog mPrograssBar;
    private TextView no_data;
    private Button btnFilter;
    private RadioButton today_radio, month_radio, week_radio;
    private CheckBox pee_box, poop_box, peenpoop_box;

    private DiaperStatisticAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private DatabaseReference table_diaper;
    private Query query;

    private CardView tableCard;

    private List<Diaper> diaperList;
    private String startDate, endDate;
    private Date dateStart, dateEnd, dateA,dateB,dateC;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.diaper_list_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefBaby = getContext().getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        no_data = view.findViewById(R.id.no_data);
        tableCard = view.findViewById(R.id.tableCard);

        diaperList = new ArrayList<>();

        pee_box = view.findViewById(R.id.pee_box);
        poop_box = view.findViewById(R.id.poo_box);
        peenpoop_box = view.findViewById(R.id.peenpoo_box);

        today_radio = view.findViewById(R.id.today_radio);
        week_radio = view.findViewById(R.id.week_radio);
        month_radio = view.findViewById(R.id.month_radio);
        btnFilter = view.findViewById(R.id.btnFilter);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        final String currentDate = dateFormat.format(date);

        DateFormat dateFormat2 = new SimpleDateFormat("/MM/yyyy");
        Date date2 = new Date();
        final String currentMonth = dateFormat2.format(date);

        // Get calendar set to current date and time
        Calendar c = GregorianCalendar.getInstance();

        //System.out.println("Current week = " + Calendar.DAY_OF_WEEK);

        // Set the calendar to monday of the current week
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        //System.out.println("Current week = " + Calendar.DAY_OF_WEEK);

        // Print dates of the current week starting on Monday
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        startDate = df.format(c.getTime());
        c.add(Calendar.DATE, 6);
        endDate = df.format(c.getTime());

        mPrograssBar = new ProgressDialog(getContext());
        mPrograssBar.setMessage("Please Wait..");

        table_diaper = FirebaseDatabase.getInstance().getReference().child("Diaper");
        query = table_diaper.orderByChild("dateTime");
        query.keepSynced(true);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                diaperList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Diaper diaper = snapshot.getValue(Diaper.class);
                        if(diaper.getBabyId().equals(bId)) {
                            if (diaper.getDateTime().startsWith(currentDate)) {
                                diaperList.add(diaper);
                            }
                        }
                    }
                    if (diaperList.isEmpty()) {
                        no_data.setVisibility(View.VISIBLE);
                        tableCard.setVisibility(View.GONE);
                    }else {
                        tableCard.setVisibility(View.VISIBLE);
                        no_data.setVisibility(View.GONE);
                    }
                    Collections.reverse(diaperList);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (today_radio.isChecked()){
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            diaperList.clear();
                            if(dataSnapshot.exists()){
                                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                    Diaper diaper = snapshot.getValue(Diaper.class);
                                    if(diaper.getBabyId().equals(bId)) {
                                        if (diaper.getDateTime().startsWith(currentDate)) {
                                            diaperList.add(diaper);
                                        }
                                    }
                                }
                                if (diaperList.isEmpty()) {
                                    no_data.setVisibility(View.VISIBLE);
                                    tableCard.setVisibility(View.GONE);
                                }else {
                                    tableCard.setVisibility(View.VISIBLE);
                                    no_data.setVisibility(View.GONE);
                                }
                                Collections.reverse(diaperList);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    if (pee_box.isChecked() && poop_box.isChecked() && peenpoop_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                diaperList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Diaper diaper = snapshot.getValue(Diaper.class);
                                        if(diaper.getBabyId().equals(bId)) {
                                            if (diaper.getDateTime().startsWith(currentDate)) {
                                                diaperList.add(diaper);
                                            }
                                        }
                                    }
                                    if (diaperList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                    }
                                    Collections.reverse(diaperList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if(pee_box.isChecked() && poop_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                diaperList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Diaper diaper = snapshot.getValue(Diaper.class);
                                        if(diaper.getBabyId().equals(bId)) {
                                            if (diaper.getDateTime().startsWith(currentDate) && !diaper.getType().equals("Peep and Poop")) {
                                                diaperList.add(diaper);
                                            }
                                        }
                                    }
                                    if (diaperList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                    }
                                    Collections.reverse(diaperList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if(pee_box.isChecked() && peenpoop_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                diaperList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Diaper diaper = snapshot.getValue(Diaper.class);
                                        if(diaper.getBabyId().equals(bId)) {
                                            if (diaper.getDateTime().startsWith(currentDate) && !diaper.getType().equals("Poop")) {
                                                diaperList.add(diaper);
                                            }
                                        }
                                    }
                                    if (diaperList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                    }
                                    Collections.reverse(diaperList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if(poop_box.isChecked() && peenpoop_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                diaperList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Diaper diaper = snapshot.getValue(Diaper.class);
                                        if(diaper.getBabyId().equals(bId)) {
                                            if (diaper.getDateTime().startsWith(currentDate) && !diaper.getType().equals("Pee")) {
                                                diaperList.add(diaper);
                                            }
                                        }
                                    }
                                    if (diaperList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                    }
                                    Collections.reverse(diaperList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if(pee_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                diaperList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Diaper diaper = snapshot.getValue(Diaper.class);
                                        if(diaper.getBabyId().equals(bId)) {
                                            if (diaper.getDateTime().startsWith(currentDate) && diaper.getType().equals("Pee")) {
                                                diaperList.add(diaper);
                                            }
                                        }
                                    }
                                    if (diaperList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                    }
                                    Collections.reverse(diaperList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if(poop_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                diaperList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Diaper diaper = snapshot.getValue(Diaper.class);
                                        if(diaper.getBabyId().equals(bId)) {
                                            if (diaper.getDateTime().startsWith(currentDate) && diaper.getType().equals("Poop")) {
                                                diaperList.add(diaper);
                                            }
                                        }
                                    }
                                    if (diaperList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                    }
                                    Collections.reverse(diaperList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if(peenpoop_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                diaperList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Diaper diaper = snapshot.getValue(Diaper.class);
                                        if(diaper.getBabyId().equals(bId)) {
                                            if (diaper.getDateTime().startsWith(currentDate) && diaper.getType().equals("Peep and Poop")) {
                                                diaperList.add(diaper);
                                            }
                                        }
                                    }
                                    if (diaperList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                    }
                                    Collections.reverse(diaperList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }else if (week_radio.isChecked()){
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            diaperList.clear();
                            if(dataSnapshot.exists()){
                                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                    Diaper diaper = snapshot.getValue(Diaper.class);
                                    if(diaper.getBabyId().equals(bId)) {
                                        String created = diaper.getCreated();

                                        try {
                                            dateA = new SimpleDateFormat("dd/MM/yyyy").parse(created);
                                            dateB = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                                            dateC = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        if (dateA.after(dateB) && dateA.before(dateC) || dateA.equals(dateB) || dateA.equals(dateC)) {
                                            diaperList.add(diaper);
                                        }
                                    }

                                }
                                if (diaperList.isEmpty()) {
                                    no_data.setVisibility(View.VISIBLE);
                                    tableCard.setVisibility(View.GONE);
                                }else {
                                    tableCard.setVisibility(View.VISIBLE);
                                    no_data.setVisibility(View.GONE);
                                }
                                Collections.reverse(diaperList);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    if (pee_box.isChecked() && poop_box.isChecked() && peenpoop_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                diaperList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Diaper diaper = snapshot.getValue(Diaper.class);
                                        if(diaper.getBabyId().equals(bId)) {
                                            String created = diaper.getCreated();

                                            try {
                                                dateA = new SimpleDateFormat("dd/MM/yyyy").parse(created);
                                                dateB = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                                                dateC = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            if (dateA.after(dateB) && dateA.before(dateC) || dateA.equals(dateB) || dateA.equals(dateC)) {
                                                diaperList.add(diaper);
                                            }
                                        }
                                    }
                                    if (diaperList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                    }
                                    Collections.reverse(diaperList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if(pee_box.isChecked() && poop_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                diaperList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Diaper diaper = snapshot.getValue(Diaper.class);
                                        if(diaper.getBabyId().equals(bId)) {
                                            if (!diaper.getType().equals("Peep and Poop")) {
                                                String created = diaper.getCreated();

                                                try {
                                                    dateA = new SimpleDateFormat("dd/MM/yyyy").parse(created);
                                                    dateB = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                                                    dateC = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                if (dateA.after(dateB) && dateA.before(dateC) || dateA.equals(dateB) || dateA.equals(dateC)) {
                                                    diaperList.add(diaper);
                                                }
                                            }
                                        }
                                    }
                                    if (diaperList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                    }
                                    Collections.reverse(diaperList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if(pee_box.isChecked() && peenpoop_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                diaperList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Diaper diaper = snapshot.getValue(Diaper.class);
                                        if(diaper.getBabyId().equals(bId)) {
                                            if (!diaper.getType().equals("Poop")) {
                                                String created = diaper.getCreated();

                                                try {
                                                    dateA = new SimpleDateFormat("dd/MM/yyyy").parse(created);
                                                    dateB = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                                                    dateC = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                if (dateA.after(dateB) && dateA.before(dateC) || dateA.equals(dateB) || dateA.equals(dateC)) {
                                                    diaperList.add(diaper);
                                                }
                                            }
                                        }
                                    }
                                    if (diaperList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                    }
                                    Collections.reverse(diaperList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if(poop_box.isChecked() && peenpoop_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                diaperList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Diaper diaper = snapshot.getValue(Diaper.class);
                                        if(diaper.getBabyId().equals(bId)) {
                                            if (!diaper.getType().equals("Pee")) {
                                                String created = diaper.getCreated();

                                                try {
                                                    dateA = new SimpleDateFormat("dd/MM/yyyy").parse(created);
                                                    dateB = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                                                    dateC = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                if (dateA.after(dateB) && dateA.before(dateC) || dateA.equals(dateB) || dateA.equals(dateC)) {
                                                    diaperList.add(diaper);
                                                }
                                            }
                                        }
                                    }
                                    if (diaperList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                    }
                                    Collections.reverse(diaperList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if(pee_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                diaperList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Diaper diaper = snapshot.getValue(Diaper.class);
                                        if(diaper.getBabyId().equals(bId)) {
                                            if (diaper.getType().equals("Pee")) {
                                                String created = diaper.getCreated();

                                                try {
                                                    dateA = new SimpleDateFormat("dd/MM/yyyy").parse(created);
                                                    dateB = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                                                    dateC = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                if (dateA.after(dateB) && dateA.before(dateC) || dateA.equals(dateB) || dateA.equals(dateC)) {
                                                    diaperList.add(diaper);
                                                }
                                            }
                                        }
                                    }
                                    if (diaperList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                    }
                                    Collections.reverse(diaperList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if(poop_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                diaperList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Diaper diaper = snapshot.getValue(Diaper.class);
                                        if(diaper.getBabyId().equals(bId)) {
                                            if (diaper.getType().equals("Poop")) {
                                                String created = diaper.getCreated();

                                                try {
                                                    dateA = new SimpleDateFormat("dd/MM/yyyy").parse(created);
                                                    dateB = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                                                    dateC = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                if (dateA.after(dateB) && dateA.before(dateC) || dateA.equals(dateB) || dateA.equals(dateC)) {
                                                    diaperList.add(diaper);
                                                }
                                            }
                                        }
                                    }
                                    if (diaperList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                    }
                                    Collections.reverse(diaperList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if(peenpoop_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                diaperList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Diaper diaper = snapshot.getValue(Diaper.class);
                                        if(diaper.getBabyId().equals(bId)) {
                                            if (diaper.getType().equals("Peep and Poop")) {
                                                String created = diaper.getCreated();

                                                try {
                                                    dateA = new SimpleDateFormat("dd/MM/yyyy").parse(created);
                                                    dateB = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                                                    dateC = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                if (dateA.after(dateB) && dateA.before(dateC) || dateA.equals(dateB) || dateA.equals(dateC)) {
                                                    diaperList.add(diaper);
                                                }
                                            }
                                        }
                                    }
                                    if (diaperList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                    }
                                    Collections.reverse(diaperList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }else if (month_radio.isChecked()){
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            diaperList.clear();
                            if(dataSnapshot.exists()){
                                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                    Diaper diaper = snapshot.getValue(Diaper.class);
                                    if(diaper.getBabyId().equals(bId)) {
                                        if (diaper.getCreated().contains(currentMonth)) {
                                            diaperList.add(diaper);
                                        }
                                    }
                                }
                                if (diaperList.isEmpty()) {
                                    no_data.setVisibility(View.VISIBLE);
                                    tableCard.setVisibility(View.GONE);
                                }else {
                                    tableCard.setVisibility(View.VISIBLE);
                                    no_data.setVisibility(View.GONE);
                                }
                                Collections.reverse(diaperList);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    if (pee_box.isChecked() && poop_box.isChecked() && peenpoop_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                diaperList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Diaper diaper = snapshot.getValue(Diaper.class);
                                        if(diaper.getBabyId().equals(bId)) {
                                            if (diaper.getCreated().contains(currentMonth)) {
                                                diaperList.add(diaper);
                                            }
                                        }
                                    }
                                    if (diaperList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                    }
                                    Collections.reverse(diaperList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if(pee_box.isChecked() && poop_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                diaperList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Diaper diaper = snapshot.getValue(Diaper.class);
                                        if(diaper.getBabyId().equals(bId)) {
                                            if (diaper.getCreated().contains(currentMonth) && !diaper.getType().equals("Peep and Poop")) {
                                                diaperList.add(diaper);
                                            }
                                        }
                                    }
                                    if (diaperList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                    }
                                    Collections.reverse(diaperList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if(pee_box.isChecked() && peenpoop_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                diaperList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Diaper diaper = snapshot.getValue(Diaper.class);
                                        if(diaper.getBabyId().equals(bId)) {
                                            if (diaper.getCreated().contains(currentMonth) && !diaper.getType().equals("Poop")) {
                                                diaperList.add(diaper);
                                            }
                                        }
                                    }
                                    if (diaperList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                    }
                                    Collections.reverse(diaperList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if(poop_box.isChecked() && peenpoop_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                diaperList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Diaper diaper = snapshot.getValue(Diaper.class);
                                        if(diaper.getBabyId().equals(bId)) {
                                            if (diaper.getCreated().contains(currentMonth) && !diaper.getType().equals("Pee")) {
                                                diaperList.add(diaper);
                                            }
                                        }
                                    }
                                    if (diaperList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                    }
                                    Collections.reverse(diaperList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if(pee_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                diaperList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Diaper diaper = snapshot.getValue(Diaper.class);
                                        if(diaper.getBabyId().equals(bId)) {
                                            if (diaper.getCreated().contains(currentMonth) && diaper.getType().equals("Pee")) {
                                                diaperList.add(diaper);
                                            }
                                        }
                                    }
                                    if (diaperList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                    }
                                    Collections.reverse(diaperList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if(poop_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                diaperList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Diaper diaper = snapshot.getValue(Diaper.class);
                                        if(diaper.getBabyId().equals(bId)) {
                                            if (diaper.getCreated().contains(currentMonth) && diaper.getType().equals("Poop")) {
                                                diaperList.add(diaper);
                                            }
                                        }
                                    }
                                    if (diaperList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                    }
                                    Collections.reverse(diaperList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if(peenpoop_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                diaperList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Diaper diaper = snapshot.getValue(Diaper.class);
                                        if(diaper.getBabyId().equals(bId)) {
                                            if (diaper.getCreated().contains(currentMonth) && diaper.getType().equals("Peep and Poop")) {
                                                diaperList.add(diaper);
                                            }
                                        }
                                    }
                                    if (diaperList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                    }
                                    Collections.reverse(diaperList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
        });

        recyclerTable = view.findViewById(R.id.recyclerTable);
        recyclerTable.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new DiaperStatisticAdapter(diaperList);

        recyclerTable.setLayoutManager(mLayoutManager);
        recyclerTable.setAdapter(mAdapter);
    }
}
