package com.health.baby_daily.statistic;

import android.annotation.SuppressLint;
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
import com.health.baby_daily.adapter.BottleStatisticAdapter;
import com.health.baby_daily.model.Bottle;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class BottleStaFragList extends Fragment{
    private View view;
    private RecyclerView recyclerTable;
    ProgressDialog mPrograssBar;
    private TextView no_data;
    private Button btnFilter;
    private RadioButton today_radio, month_radio, week_radio;

    private BottleStatisticAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private DatabaseReference table_bottle;
    private Query query;

    private CardView tableCard;

    private CheckBox bm_box;
    private CheckBox fm_box;
    private CheckBox unitA;
    private CheckBox unitB;

    private String[] contentData;
    private List<Bottle> bottleList;

    private Date dateA, dateB, dateC;

    private String bId, startDate, endDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bottle_list_fragment, container, false);
        return view;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefBaby = getContext().getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        no_data = view.findViewById(R.id.no_data);
        tableCard = view.findViewById(R.id.tableCard);

        bottleList = new ArrayList();

        //radiobutton
        today_radio = view.findViewById(R.id.today_radio);
        week_radio = view.findViewById(R.id.week_radio);
        month_radio = view.findViewById(R.id.month_radio);
        //checkbox
        bm_box = view.findViewById(R.id.bm_box);
        fm_box = view.findViewById(R.id.fm_box);
        unitA = view.findViewById(R.id.unitA);
        unitB = view.findViewById(R.id.unitB);
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

        table_bottle = FirebaseDatabase.getInstance().getReference().child("Bottle");
        query = table_bottle.orderByChild("dateTime");
        query.keepSynced(true);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bottleList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Bottle bottle = snapshot.getValue(Bottle.class);
                        if(bottle.getBabyId().equals(bId)) {
                            if (bottle.getDateTime().startsWith(currentDate)) {
                                bottleList.add(bottle);
                            }
                        }
                    }
                    if (bottleList.isEmpty()) {
                        no_data.setVisibility(View.VISIBLE);
                        tableCard.setVisibility(View.GONE);
                    }else {
                        tableCard.setVisibility(View.VISIBLE);
                        no_data.setVisibility(View.GONE);
                    }
                    Collections.reverse(bottleList);
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
                mPrograssBar.show();
                no_data.setVisibility(View.GONE);
                if(today_radio.isChecked()){
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            bottleList.clear();
                            if(dataSnapshot.exists()){
                                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                    Bottle bottle = snapshot.getValue(Bottle.class);
                                    if (bottle.getBabyId().equals(bId)) {
                                        if (bottle.getDateTime().startsWith(currentDate)) {
                                            bottleList.add(bottle);
                                        }
                                    }
                                }
                                if (bottleList.isEmpty()) {
                                    no_data.setVisibility(View.VISIBLE);
                                    tableCard.setVisibility(View.GONE);
                                }else {
                                    tableCard.setVisibility(View.VISIBLE);
                                }
                                Collections.reverse(bottleList);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    if(bm_box.isChecked() && fm_box.isChecked() && unitA.isChecked() && unitB.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getDateTime().startsWith(currentDate)) {
                                                bottleList.add(bottle);
                                            }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (bm_box.isChecked() && fm_box.isChecked() && unitA.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getDateTime().startsWith(currentDate) && bottle.getUnits().equals("mL")) {
                                                bottleList.add(bottle);
                                            }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (bm_box.isChecked() && fm_box.isChecked() && unitB.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getDateTime().startsWith(currentDate) && bottle.getUnits().equals("Oz")) {
                                                bottleList.add(bottle);
                                            }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (bm_box.isChecked() && unitA.isChecked() && unitB.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getDateTime().startsWith(currentDate) && bottle.getType().equals("Breast")) {
                                                bottleList.add(bottle);
                                            }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (fm_box.isChecked() && unitA.isChecked() && unitB.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getDateTime().startsWith(currentDate) && bottle.getType().equals("Formula")) {
                                                bottleList.add(bottle);
                                            }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (bm_box.isChecked() && fm_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getDateTime().startsWith(currentDate)) {
                                                bottleList.add(bottle);
                                            }
                                        }
                                    }

                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (bm_box.isChecked() && unitA.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getDateTime().startsWith(currentDate) && bottle.getType().equals("Breast") && bottle.getUnits().equals("mL")) {
                                                bottleList.add(bottle);
                                            }
                                        }
                                    }

                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (bm_box.isChecked() && unitB.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getDateTime().startsWith(currentDate) && bottle.getType().equals("Breast") && bottle.getUnits().equals("Oz")) {
                                                bottleList.add(bottle);
                                            }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (fm_box.isChecked() && unitA.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getDateTime().startsWith(currentDate) && bottle.getType().equals("Formula") && bottle.getUnits().equals("mL")) {
                                                bottleList.add(bottle);
                                            }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (fm_box.isChecked() && unitB.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getDateTime().startsWith(currentDate) && bottle.getType().equals("Formula") && bottle.getUnits().equals("Oz")) {
                                                bottleList.add(bottle);
                                            }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (unitA.isChecked() && unitB.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getDateTime().startsWith(currentDate) && (bottle.getUnits().equals("mL") || bottle.getUnits().equals("Oz"))) {
                                                bottleList.add(bottle);
                                            }
                                        }
                                    }

                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (bm_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getDateTime().startsWith(currentDate) && bottle.getType().equals("Breast")) {
                                                bottleList.add(bottle);
                                            }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (fm_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getDateTime().startsWith(currentDate) && bottle.getType().equals("Formula")) {
                                                bottleList.add(bottle);
                                            }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else if (unitA.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getDateTime().startsWith(currentDate) && bottle.getUnits().equals("mL")) {
                                                bottleList.add(bottle);
                                            }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else if (unitB.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getDateTime().startsWith(currentDate) && bottle.getUnits().equals("Oz")) {
                                                bottleList.add(bottle);
                                            }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }else if(week_radio.isChecked()){
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            bottleList.clear();
                            if(dataSnapshot.exists()){
                                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                    Bottle bottle = snapshot.getValue(Bottle.class);
                                    if(bottle.getBabyId().equals(bId)) {
                                        String created = bottle.getDateTime();
                                        try {
                                            dateA = new SimpleDateFormat("dd/MM/yyyy").parse(created);
                                            dateB = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                                            dateC = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        if (dateA.after(dateB) && dateA.before(dateC) || dateA.equals(dateB) || dateA.equals(dateC)) {
                                            bottleList.add(bottle);
                                        }
                                    }
                                }
                                if (bottleList.isEmpty()) {
                                    no_data.setVisibility(View.VISIBLE);
                                    tableCard.setVisibility(View.GONE);
                                }else {
                                    tableCard.setVisibility(View.VISIBLE);
                                }
                                Collections.reverse(bottleList);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    if(bm_box.isChecked() && fm_box.isChecked() && unitA.isChecked() && unitB.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            String created = bottle.getDateTime();
                                            try {
                                                dateA = new SimpleDateFormat("dd/MM/yyyy").parse(created);
                                                dateB = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                                                dateC = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            if (dateA.after(dateB) && dateA.before(dateC) || dateA.equals(dateB) || dateA.equals(dateC)) {
                                                bottleList.add(bottle);
                                            }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (bm_box.isChecked() && fm_box.isChecked() && unitA.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                        if( bottle.getUnits().equals("mL")) {
                                            String created = bottle.getDateTime();
                                            try {
                                                dateA = new SimpleDateFormat("dd/MM/yyyy").parse(created);
                                                dateB = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                                                dateC = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            if (dateA.after(dateB) && dateA.before(dateC) || dateA.equals(dateB) || dateA.equals(dateC)) {
                                                bottleList.add(bottle);
                                            }
                                        }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (bm_box.isChecked() && fm_box.isChecked() && unitB.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getUnits().equals("Oz")) {
                                                String created = bottle.getDateTime();
                                                try {
                                                    dateA = new SimpleDateFormat("dd/MM/yyyy").parse(created);
                                                    dateB = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                                                    dateC = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                if (dateA.after(dateB) && dateA.before(dateC) || dateA.equals(dateB) || dateA.equals(dateC)) {
                                                    bottleList.add(bottle);
                                                }
                                            }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (bm_box.isChecked() && unitA.isChecked() && unitB.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getType().equals("Breast")) {
                                                String created = bottle.getDateTime();
                                                try {
                                                    dateA = new SimpleDateFormat("dd/MM/yyyy").parse(created);
                                                    dateB = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                                                    dateC = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                if (dateA.after(dateB) && dateA.before(dateC) || dateA.equals(dateB) || dateA.equals(dateC)) {
                                                    bottleList.add(bottle);
                                                }
                                            }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (fm_box.isChecked() && unitA.isChecked() && unitB.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getType().equals("Formula")) {
                                            String created = bottle.getDateTime();
                                            try {
                                                dateA = new SimpleDateFormat("dd/MM/yyyy").parse(created);
                                                dateB = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                                                dateC = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            if(dateA.after(dateB) && dateA.before(dateC) || dateA.equals(dateB) || dateA.equals(dateC)){
                                                bottleList.add(bottle);
                                            }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (bm_box.isChecked() && fm_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        String created = bottle.getDateTime();
                                        try {
                                            dateA = new SimpleDateFormat("dd/MM/yyyy").parse(created);
                                            dateB = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                                            dateC = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        if(dateA.after(dateB) && dateA.before(dateC) || dateA.equals(dateB) || dateA.equals(dateC)){
                                            bottleList.add(bottle);
                                        }

                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (bm_box.isChecked() && unitA.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getType().equals("Breast") && bottle.getUnits().equals("mL")) {
                                            String created = bottle.getDateTime();
                                            try {
                                                dateA = new SimpleDateFormat("dd/MM/yyyy").parse(created);
                                                dateB = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                                                dateC = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            if(dateA.after(dateB) && dateA.before(dateC) || dateA.equals(dateB) || dateA.equals(dateC)){
                                                bottleList.add(bottle);
                                            }
                                        }
                                    }

                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (bm_box.isChecked() && unitB.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getType().equals("Breast") && bottle.getUnits().equals("Oz")) {
                                            String created = bottle.getDateTime();
                                            try {
                                                dateA = new SimpleDateFormat("dd/MM/yyyy").parse(created);
                                                dateB = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                                                dateC = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            if(dateA.after(dateB) && dateA.before(dateC) || dateA.equals(dateB) || dateA.equals(dateC)){
                                                bottleList.add(bottle);
                                            }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (fm_box.isChecked() && unitA.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getType().equals("Formula") && bottle.getUnits().equals("mL")) {
                                                String created = bottle.getDateTime();
                                                try {
                                                    dateA = new SimpleDateFormat("dd/MM/yyyy").parse(created);
                                                    dateB = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                                                    dateC = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                if (dateA.after(dateB) && dateA.before(dateC) || dateA.equals(dateB) || dateA.equals(dateC)) {
                                                    bottleList.add(bottle);
                                                }
                                            }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (fm_box.isChecked() && unitB.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getType().equals("Formula") && bottle.getUnits().equals("Oz")) {
                                                String created = bottle.getDateTime();
                                                try {
                                                    dateA = new SimpleDateFormat("dd/MM/yyyy").parse(created);
                                                    dateB = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                                                    dateC = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                if (dateA.after(dateB) && dateA.before(dateC) || dateA.equals(dateB) || dateA.equals(dateC)) {
                                                    bottleList.add(bottle);
                                                }
                                            }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (unitA.isChecked() && unitB.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getUnits().equals("mL") || bottle.getUnits().equals("Oz")) {
                                                String created = bottle.getDateTime();
                                                try {
                                                    dateA = new SimpleDateFormat("dd/MM/yyyy").parse(created);
                                                    dateB = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                                                    dateC = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                if (dateA.after(dateB) && dateA.before(dateC) || dateA.equals(dateB) || dateA.equals(dateC)) {
                                                    bottleList.add(bottle);
                                                }
                                            }
                                        }
                                    }

                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (bm_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getType().equals("Breast")) {
                                                String created = bottle.getDateTime();
                                                try {
                                                    dateA = new SimpleDateFormat("dd/MM/yyyy").parse(created);
                                                    dateB = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                                                    dateC = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                if (dateA.after(dateB) && dateA.before(dateC) || dateA.equals(dateB) || dateA.equals(dateC)) {
                                                    bottleList.add(bottle);
                                                }
                                            }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (fm_box.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getType().equals("Formula")) {
                                                bottleList.add(bottle);
                                            }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else if (unitA.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getUnits().equals("mL")) {
                                                bottleList.add(bottle);
                                            }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else if (unitB.isChecked()){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                bottleList.clear();
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Bottle bottle = snapshot.getValue(Bottle.class);
                                        if(bottle.getBabyId().equals(bId)) {
                                            if (bottle.getUnits().equals("Oz")) {
                                                bottleList.add(bottle);
                                            }
                                        }
                                    }
                                    if (bottleList.isEmpty()) {
                                        no_data.setVisibility(View.VISIBLE);
                                        tableCard.setVisibility(View.GONE);
                                    }else {
                                        tableCard.setVisibility(View.VISIBLE);
                                    }
                                    Collections.reverse(bottleList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }else if(month_radio.isChecked()) {
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            bottleList.clear();
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Bottle bottle = snapshot.getValue(Bottle.class);
                                    if(bottle.getBabyId().equals(bId)) {
                                        if (bottle.getDateTime().contains(currentMonth)) {
                                            bottleList.add(bottle);
                                        }
                                    }
                                }
                                if (bottleList.isEmpty()) {
                                    no_data.setVisibility(View.VISIBLE);
                                    tableCard.setVisibility(View.GONE);
                                } else {
                                    tableCard.setVisibility(View.VISIBLE);
                                }
                                Collections.reverse(bottleList);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                        if (bm_box.isChecked() && fm_box.isChecked() && unitA.isChecked() && unitB.isChecked()) {
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    bottleList.clear();
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Bottle bottle = snapshot.getValue(Bottle.class);
                                            if(bottle.getBabyId().equals(bId)) {
                                                if (bottle.getDateTime().contains(currentMonth)) {
                                                    bottleList.add(bottle);
                                                }
                                            }
                                        }
                                        if (bottleList.isEmpty()) {
                                            no_data.setVisibility(View.VISIBLE);
                                            tableCard.setVisibility(View.GONE);
                                        } else {
                                            tableCard.setVisibility(View.VISIBLE);
                                        }
                                        Collections.reverse(bottleList);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else if (bm_box.isChecked() && fm_box.isChecked() && unitA.isChecked()) {
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    bottleList.clear();
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Bottle bottle = snapshot.getValue(Bottle.class);
                                            if(bottle.getBabyId().equals(bId)) {
                                                if (bottle.getDateTime().contains(currentMonth) && bottle.getUnits().equals("mL")) {
                                                    bottleList.add(bottle);
                                                }
                                            }
                                        }
                                        if (bottleList.isEmpty()) {
                                            no_data.setVisibility(View.VISIBLE);
                                            tableCard.setVisibility(View.GONE);
                                        } else {
                                            tableCard.setVisibility(View.VISIBLE);
                                        }
                                        Collections.reverse(bottleList);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else if (bm_box.isChecked() && fm_box.isChecked() && unitB.isChecked()) {
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    bottleList.clear();
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Bottle bottle = snapshot.getValue(Bottle.class);
                                            if(bottle.getBabyId().equals(bId)) {
                                                if (bottle.getDateTime().contains(currentMonth) && bottle.getUnits().equals("Oz")) {
                                                    bottleList.add(bottle);
                                                }
                                            }
                                        }
                                        if (bottleList.isEmpty()) {
                                            no_data.setVisibility(View.VISIBLE);
                                            tableCard.setVisibility(View.GONE);
                                        } else {
                                            tableCard.setVisibility(View.VISIBLE);
                                        }
                                        Collections.reverse(bottleList);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else if (bm_box.isChecked() && unitA.isChecked() && unitB.isChecked()) {
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    bottleList.clear();
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Bottle bottle = snapshot.getValue(Bottle.class);
                                            if(bottle.getBabyId().equals(bId)) {
                                                if (bottle.getDateTime().contains(currentMonth) && bottle.getType().equals("Breast")) {
                                                    bottleList.add(bottle);
                                                }
                                            }
                                        }
                                        if (bottleList.isEmpty()) {
                                            no_data.setVisibility(View.VISIBLE);
                                            tableCard.setVisibility(View.GONE);
                                        } else {
                                            tableCard.setVisibility(View.VISIBLE);
                                        }
                                        Collections.reverse(bottleList);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else if (fm_box.isChecked() && unitA.isChecked() && unitB.isChecked()) {
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    bottleList.clear();
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Bottle bottle = snapshot.getValue(Bottle.class);
                                            if(bottle.getBabyId().equals(bId)) {
                                                if (bottle.getDateTime().contains(currentMonth) && bottle.getType().equals("Formula")) {
                                                    bottleList.add(bottle);
                                                }
                                            }
                                        }
                                        if (bottleList.isEmpty()) {
                                            no_data.setVisibility(View.VISIBLE);
                                            tableCard.setVisibility(View.GONE);
                                        } else {
                                            tableCard.setVisibility(View.VISIBLE);
                                        }
                                        Collections.reverse(bottleList);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else if (bm_box.isChecked() && fm_box.isChecked()) {
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    bottleList.clear();
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Bottle bottle = snapshot.getValue(Bottle.class);
                                            if(bottle.getBabyId().equals(bId)) {
                                                if (bottle.getDateTime().contains(currentMonth)) {
                                                    bottleList.add(bottle);
                                                }
                                            }
                                        }

                                        if (bottleList.isEmpty()) {
                                            no_data.setVisibility(View.VISIBLE);
                                            tableCard.setVisibility(View.GONE);
                                        } else {
                                            tableCard.setVisibility(View.VISIBLE);
                                        }
                                        Collections.reverse(bottleList);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else if (bm_box.isChecked() && unitA.isChecked()) {
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    bottleList.clear();
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Bottle bottle = snapshot.getValue(Bottle.class);
                                            if(bottle.getBabyId().equals(bId)) {
                                                if (bottle.getDateTime().contains(currentMonth) && bottle.getType().equals("Breast") && bottle.getUnits().equals("mL")) {
                                                    bottleList.add(bottle);
                                                }
                                            }
                                        }

                                        if (bottleList.isEmpty()) {
                                            no_data.setVisibility(View.VISIBLE);
                                            tableCard.setVisibility(View.GONE);
                                        } else {
                                            tableCard.setVisibility(View.VISIBLE);
                                        }
                                        Collections.reverse(bottleList);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else if (bm_box.isChecked() && unitB.isChecked()) {
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    bottleList.clear();
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Bottle bottle = snapshot.getValue(Bottle.class);
                                            if(bottle.getBabyId().equals(bId)) {
                                                if (bottle.getDateTime().contains(currentMonth) && bottle.getType().equals("Breast") && bottle.getUnits().equals("Oz")) {
                                                    bottleList.add(bottle);
                                                }
                                            }
                                        }
                                        if (bottleList.isEmpty()) {
                                            no_data.setVisibility(View.VISIBLE);
                                            tableCard.setVisibility(View.GONE);
                                        } else {
                                            tableCard.setVisibility(View.VISIBLE);
                                        }
                                        Collections.reverse(bottleList);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else if (fm_box.isChecked() && unitA.isChecked()) {
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    bottleList.clear();
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Bottle bottle = snapshot.getValue(Bottle.class);
                                            if(bottle.getBabyId().equals(bId)) {
                                                if (bottle.getDateTime().startsWith(currentDate) && bottle.getType().equals("Formula") && bottle.getUnits().equals("mL")) {
                                                    bottleList.add(bottle);
                                                }
                                            }
                                        }
                                        if (bottleList.isEmpty()) {
                                            no_data.setVisibility(View.VISIBLE);
                                            tableCard.setVisibility(View.GONE);
                                        } else {
                                            tableCard.setVisibility(View.VISIBLE);
                                        }
                                        Collections.reverse(bottleList);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else if (fm_box.isChecked() && unitB.isChecked()) {
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    bottleList.clear();
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Bottle bottle = snapshot.getValue(Bottle.class);
                                            if(bottle.getBabyId().equals(bId)) {
                                                if (bottle.getDateTime().contains(currentMonth) && bottle.getType().equals("Formula") && bottle.getUnits().equals("Oz")) {
                                                    bottleList.add(bottle);
                                                }
                                            }
                                        }
                                        if (bottleList.isEmpty()) {
                                            no_data.setVisibility(View.VISIBLE);
                                            tableCard.setVisibility(View.GONE);
                                        } else {
                                            tableCard.setVisibility(View.VISIBLE);
                                        }
                                        Collections.reverse(bottleList);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else if (unitA.isChecked() && unitB.isChecked()) {
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    bottleList.clear();
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Bottle bottle = snapshot.getValue(Bottle.class);
                                            if(bottle.getBabyId().equals(bId)) {
                                                if (bottle.getDateTime().contains(currentMonth) && (bottle.getUnits().equals("mL") || bottle.getUnits().equals("Oz"))) {
                                                    bottleList.add(bottle);
                                                }
                                            }
                                        }

                                        if (bottleList.isEmpty()) {
                                            no_data.setVisibility(View.VISIBLE);
                                            tableCard.setVisibility(View.GONE);
                                        } else {
                                            tableCard.setVisibility(View.VISIBLE);
                                        }
                                        Collections.reverse(bottleList);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else if (bm_box.isChecked()) {
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    bottleList.clear();
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Bottle bottle = snapshot.getValue(Bottle.class);
                                            if(bottle.getBabyId().equals(bId)) {
                                                if (bottle.getDateTime().contains(currentMonth) && bottle.getType().equals("Breast")) {
                                                    bottleList.add(bottle);
                                                }
                                            }
                                        }
                                        if (bottleList.isEmpty()) {
                                            no_data.setVisibility(View.VISIBLE);
                                            tableCard.setVisibility(View.GONE);
                                        } else {
                                            tableCard.setVisibility(View.VISIBLE);
                                        }
                                        Collections.reverse(bottleList);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else if (fm_box.isChecked()) {
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    bottleList.clear();
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Bottle bottle = snapshot.getValue(Bottle.class);
                                            if(bottle.getBabyId().equals(bId)) {
                                                if (bottle.getDateTime().contains(currentMonth) && bottle.getType().equals("Formula")) {
                                                    bottleList.add(bottle);
                                                }
                                            }
                                        }
                                        if (bottleList.isEmpty()) {
                                            no_data.setVisibility(View.VISIBLE);
                                            tableCard.setVisibility(View.GONE);
                                        } else {
                                            tableCard.setVisibility(View.VISIBLE);
                                        }
                                        Collections.reverse(bottleList);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else if (unitA.isChecked()) {
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    bottleList.clear();
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Bottle bottle = snapshot.getValue(Bottle.class);
                                            if(bottle.getBabyId().equals(bId)) {
                                                if (bottle.getDateTime().contains(currentMonth) && bottle.getUnits().equals("mL")) {
                                                    bottleList.add(bottle);
                                                }
                                            }
                                        }
                                        if (bottleList.isEmpty()) {
                                            no_data.setVisibility(View.VISIBLE);
                                            tableCard.setVisibility(View.GONE);
                                        } else {
                                            tableCard.setVisibility(View.VISIBLE);
                                        }
                                        Collections.reverse(bottleList);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else if (unitB.isChecked()) {
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    bottleList.clear();
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Bottle bottle = snapshot.getValue(Bottle.class);
                                            if(bottle.getBabyId().equals(bId)) {
                                                if (bottle.getDateTime().contains(currentMonth) && bottle.getUnits().equals("Oz")) {
                                                    bottleList.add(bottle);
                                                }
                                            }
                                        }
                                        if (bottleList.isEmpty()) {
                                            no_data.setVisibility(View.VISIBLE);
                                            tableCard.setVisibility(View.GONE);
                                        } else {
                                            tableCard.setVisibility(View.VISIBLE);
                                        }
                                        Collections.reverse(bottleList);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                mPrograssBar.dismiss();
            }
        });

        recyclerTable = view.findViewById(R.id.recyclerTable);
        recyclerTable.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new BottleStatisticAdapter(bottleList);

        recyclerTable.setLayoutManager(mLayoutManager);
        recyclerTable.setAdapter(mAdapter);

    }
}
