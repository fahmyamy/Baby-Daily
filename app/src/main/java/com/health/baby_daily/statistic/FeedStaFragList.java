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
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.adapter.FeedStatisticAdapter;
import com.health.baby_daily.model.Feed;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class FeedStaFragList extends Fragment {
    private View view;
    private RecyclerView recyclerTable;
    ProgressDialog mPrograssBar;
    private TextView no_data;
    private Button btnFilter;
    private RadioButton today_radio, month_radio, week_radio;

    private FeedStatisticAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private DatabaseReference table_feed;
    private Query query;

    private CardView tableCard;

    private List<Feed> feedList;

    private Date dateA, dateB, dateC;

    private String bId, startDate, endDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.feed_list_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefBaby = getContext().getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        no_data = view.findViewById(R.id.no_data);
        tableCard = view.findViewById(R.id.tableCard);

        feedList = new ArrayList();

        //radiobutton
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

        table_feed = FirebaseDatabase.getInstance().getReference().child("Feed");
        query = table_feed.orderByChild("dateTime");
        query.keepSynced(true);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                feedList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Feed feed = snapshot.getValue(Feed.class);
                        if(feed.getBaby_id().equals(bId)) {
                            if (feed.getDateTime().startsWith(currentDate)) {
                                feedList.add(feed);
                            }
                        }
                    }
                    if (feedList.isEmpty()) {
                        no_data.setVisibility(View.VISIBLE);
                        tableCard.setVisibility(View.GONE);
                    }else {
                        tableCard.setVisibility(View.VISIBLE);
                        no_data.setVisibility(View.GONE);
                    }
                    Collections.reverse(feedList);
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
                no_data.setVisibility(View.GONE);
                if(today_radio.isChecked()){
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            feedList.clear();
                            if(dataSnapshot.exists()){
                                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                    Feed feed = snapshot.getValue(Feed.class);
                                    if(feed.getBaby_id().equals(bId)) {
                                        if (feed.getDateTime().startsWith(currentDate)) {
                                            feedList.add(feed);
                                        }
                                    }
                                }
                                if (feedList.isEmpty()) {
                                    no_data.setVisibility(View.VISIBLE);
                                    tableCard.setVisibility(View.GONE);
                                }else {
                                    tableCard.setVisibility(View.VISIBLE);
                                    no_data.setVisibility(View.GONE);
                                }
                                Collections.reverse(feedList);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else if(week_radio.isChecked()){
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            feedList.clear();
                            if(dataSnapshot.exists()){
                                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                    Feed feed = snapshot.getValue(Feed.class);
                                    if(feed.getBaby_id().equals(bId)) {
                                        String created = feed.getCreated();

                                        try {
                                            dateA = new SimpleDateFormat("dd/MM/yyyy").parse(created);
                                            dateB = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                                            dateC = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        if (dateA.after(dateB) && dateA.before(dateC) || dateA.equals(dateB) || dateA.equals(dateC)) {
                                            feedList.add(feed);
                                        }
                                    }
                                }
                                if (feedList.isEmpty()) {
                                    no_data.setVisibility(View.VISIBLE);
                                    tableCard.setVisibility(View.GONE);
                                }else {
                                    tableCard.setVisibility(View.VISIBLE);
                                }
                                Collections.reverse(feedList);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else if(month_radio.isChecked()){
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            feedList.clear();
                            if(dataSnapshot.exists()){
                                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                    Feed feed = snapshot.getValue(Feed.class);
                                    if(feed.getBaby_id().equals(bId)) {
                                        if (feed.getCreated().contains(currentMonth)) {
                                            feedList.add(feed);
                                        }
                                    }
                                }
                                if (feedList.isEmpty()) {
                                    no_data.setVisibility(View.VISIBLE);
                                    tableCard.setVisibility(View.GONE);
                                }else {
                                    tableCard.setVisibility(View.VISIBLE);
                                }
                                Collections.reverse(feedList);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        recyclerTable = view.findViewById(R.id.recyclerTable);
        recyclerTable.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new FeedStatisticAdapter(feedList);

        recyclerTable.setLayoutManager(mLayoutManager);
        recyclerTable.setAdapter(mAdapter);
    }
}
