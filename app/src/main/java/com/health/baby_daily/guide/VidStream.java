package com.health.baby_daily.guide;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.adapter.VTimelineAdapter;
import com.health.baby_daily.contact.ContactList;
import com.health.baby_daily.model.Follow;
import com.health.baby_daily.model.Video;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VidStream extends AppCompatActivity{

    private FirebaseAuth firebaseAuth;

    private RecyclerView recyclerView;
    private VTimelineAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView no_video_text;
    private DatabaseReference table_video, table_follow, table_searchVideo;
    private Query query, queryA;

    private List<Video> videoList;
    private String uid;

    private boolean checkFirst = false, checkMedInfo = false, checkTreat = false, checkFood = false, checkOther = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vid_stream);

        setTitle("Video Streaming");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        uid = user.getUid();

        videoList = new ArrayList<>();

        table_video = FirebaseDatabase.getInstance().getReference().child("Video");
        query = table_video.orderByChild("created");
        query.keepSynced(true);

        table_follow = FirebaseDatabase.getInstance().getReference().child("Follow");
        table_follow.keepSynced(true);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        no_video_text = findViewById(R.id.no_video_text);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new VTimelineAdapter(videoList);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);


        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                table_follow.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Follow follow = snapshot.getValue(Follow.class);
                                if (follow.getUser_id().equals(uid)){
                                    final String user_id = follow.getFollow_id();

                                    query.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            videoList.clear();
                                            if (dataSnapshot.exists()){
                                                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                                    Video video = snapshot.getValue(Video.class);
                                                    if (video.getUid().equals(user_id) || video.getUid().equals(uid)){
                                                        videoList.add(video);
                                                    }

                                                }

                                                Collections.reverse(videoList);
                                                mAdapter.notifyDataSetChanged();
                                            }

                                            if (!videoList.isEmpty()){
                                                no_video_text.setVisibility(View.GONE);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

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
        });

        table_searchVideo = FirebaseDatabase.getInstance().getReference().child("Video");
        table_searchVideo.keepSynced(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        MenuItem searchFilterItem = menu.findItem(R.id.app_bar_filter);
        searchFilterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                LayoutInflater layoutInflater = LayoutInflater.from(VidStream.this);
                View promptView = layoutInflater.inflate(R.layout.filter_dialog, null);

                final AlertDialog alertD = new AlertDialog.Builder(VidStream.this).create();

                final Button firstAidBtn = promptView.findViewById(R.id.firstAidBtn);
                final Button medInfoBtn = promptView.findViewById(R.id.medInfoBtn);
                final Button treatmentBtn = promptView.findViewById(R.id.treatmentBtn);
                final Button foodBtn = promptView.findViewById(R.id.foodBtn);
                final Button allBtn = promptView.findViewById(R.id.allBtn);
                final Button submitBtn = promptView.findViewById(R.id.submitBtn);

                firstAidBtn.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View view) {
                        if (!checkFirst){
                            firstAidBtn.setBackgroundResource(R.drawable.selected_filter);
                            firstAidBtn.setTextColor(Color.WHITE);
                            checkFirst = true;
                        }else if (checkFirst){
                            firstAidBtn.setBackgroundResource(R.drawable.border_filter);
                            firstAidBtn.setTextColor(R.color.colorPrimary);
                            checkFirst = false;
                        }
                    }
                });

                medInfoBtn.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View view) {
                        if (!checkMedInfo){
                            medInfoBtn.setBackgroundResource(R.drawable.selected_filter);
                            medInfoBtn.setTextColor(Color.WHITE);
                            checkMedInfo = true;
                        }else if (checkMedInfo){
                            medInfoBtn.setBackgroundResource(R.drawable.border_filter);
                            medInfoBtn.setTextColor(R.color.colorPrimary);
                            checkMedInfo = false;
                        }
                    }
                });

                treatmentBtn.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View view) {
                        if (!checkTreat){
                            treatmentBtn.setBackgroundResource(R.drawable.selected_filter);
                            treatmentBtn.setTextColor(Color.WHITE);
                            checkTreat = true;
                        }else if (checkTreat){
                            treatmentBtn.setBackgroundResource(R.drawable.border_filter);
                            treatmentBtn.setTextColor(R.color.colorPrimary);
                            checkTreat = false;
                        }
                    }
                });

                foodBtn.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View view) {
                        if (!checkFood){
                            foodBtn.setBackgroundResource(R.drawable.selected_filter);
                            foodBtn.setTextColor(Color.WHITE);
                            checkFood = true;
                        }else if (checkFood){
                            foodBtn.setBackgroundResource(R.drawable.border_filter);
                            foodBtn.setTextColor(R.color.colorPrimary);
                            checkFood = false;
                        }
                    }
                });

                allBtn.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View view) {
                        if (!checkOther){
                            allBtn.setBackgroundResource(R.drawable.selected_filter);
                            allBtn.setTextColor(Color.WHITE);
                            checkOther = true;
                        }else if (checkOther){
                            allBtn.setBackgroundResource(R.drawable.border_filter);
                            allBtn.setTextColor(R.color.colorPrimary);
                            checkOther = false;
                        }
                    }
                });

                submitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        table_video.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                videoList.clear();
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Video video = snapshot.getValue(Video.class);
                                        String type = video.getType();
                                        if (checkFirst && checkMedInfo && checkTreat && checkFood && checkOther){
                                            videoList.add(video);
                                        }else if (checkFirst && checkMedInfo && checkTreat && checkFood){
                                            if (type.equals("First Aid") || type.equals("Medical Info") || type.equals("Treatment") || type.equals("Food")){
                                                videoList.add(video);
                                            }
                                        }else if (checkFirst && checkMedInfo && checkTreat && checkOther){
                                            if (type.equals("First Aid") || type.equals("Medical Info") || type.equals("Treatment") || type.equals("Other")){
                                                videoList.add(video);
                                            }
                                        }else if (checkFirst && checkMedInfo && checkFood && checkOther){
                                            if (type.equals("First Aid") || type.equals("Medical Info") || type.equals("Food") || type.equals("Other")){
                                                videoList.add(video);
                                            }
                                        }else if (checkFirst && checkTreat && checkFood && checkOther){
                                            if (type.equals("First Aid") || type.equals("Food") || type.equals("Treatment") || type.equals("Other")){
                                                videoList.add(video);
                                            }
                                        }else if (checkMedInfo && checkTreat && checkFood && checkOther){
                                            if (type.equals("Food") || type.equals("Medical Info") || type.equals("Treatment") || type.equals("Other")){
                                                videoList.add(video);
                                            }
                                        }else if (checkFirst && checkMedInfo && checkTreat){
                                            if (type.equals("First Aid") || type.equals("Medical Info") || type.equals("Treatment")){
                                                videoList.add(video);
                                            }
                                        }else if (checkFirst && checkMedInfo && checkFood){
                                            if (type.equals("First Aid") || type.equals("Medical Info") || type.equals("Food")){
                                                videoList.add(video);
                                            }
                                        }else if (checkFirst && checkMedInfo && checkOther){
                                            if (type.equals("First Aid") || type.equals("Medical Info") || type.equals("Other")){
                                                videoList.add(video);
                                            }
                                        }else if (checkFirst && checkTreat && checkFood){
                                            if (type.equals("First Aid") || type.equals("Food") || type.equals("Treatment")){
                                                videoList.add(video);
                                            }
                                        }else if (checkFirst && checkTreat && checkOther){
                                            if (type.equals("First Aid") || type.equals("Food") || type.equals("Treatment")){
                                                videoList.add(video);
                                            }
                                        }else if (checkFirst && checkFood && checkOther){
                                            if (type.equals("First Aid") || type.equals("Food") || type.equals("Other")){
                                                videoList.add(video);
                                            }
                                        }else if (checkMedInfo && checkTreat && checkFood){
                                            if (type.equals("Food") || type.equals("Medical Info") || type.equals("Treatment")){
                                                videoList.add(video);
                                            }
                                        }else if (checkMedInfo && checkTreat && checkOther){
                                            if (type.equals("Other") || type.equals("Medical Info") || type.equals("Treatment")){
                                                videoList.add(video);
                                            }
                                        }else if (checkMedInfo && checkFood && checkOther){
                                            if (type.equals("Food") || type.equals("Medical Info") || type.equals("Other")){
                                                videoList.add(video);
                                            }
                                        }else if (checkTreat && checkFood && checkOther){
                                            if (type.equals("Food") || type.equals("Other") || type.equals("Treatment")){
                                                videoList.add(video);
                                            }
                                        }else if (checkFirst && checkMedInfo){
                                            if (type.equals("First Aid") || type.equals("Medical Info")){
                                                videoList.add(video);
                                            }
                                        }else if (checkFirst && checkTreat){
                                            if (type.equals("First Aid") || type.equals("Treatment")){
                                                videoList.add(video);
                                            }
                                        }else if (checkFirst && checkFood){
                                            if (type.equals("First Aid") || type.equals("Food")){
                                                videoList.add(video);
                                            }
                                        }else if (checkFirst && checkOther){
                                            if (type.equals("First Aid") || type.equals("Other")){
                                                videoList.add(video);
                                            }
                                        }else if (checkMedInfo && checkTreat){
                                            if (type.equals("Medical Info") || type.equals("Treatment")){
                                                videoList.add(video);
                                            }
                                        }else if (checkMedInfo && checkFood){
                                            if (type.equals("Medical Info") || type.equals("Food")){
                                                videoList.add(video);
                                            }
                                        }else if (checkMedInfo && checkOther){
                                            if (type.equals("Medical Info") || type.equals("Other")){
                                                videoList.add(video);
                                            }
                                        }else if (checkTreat && checkFood){
                                            if (type.equals("Food") || type.equals("Treatment")){
                                                videoList.add(video);
                                            }
                                        }else if (checkTreat && checkOther){
                                            if (type.equals("Other") || type.equals("Treatment")){
                                                videoList.add(video);
                                            }
                                        }else if (checkFood && checkOther){
                                            if (type.equals("Food") || type.equals("Other")){
                                                videoList.add(video);
                                            }
                                        }else if (checkFirst){
                                            if (type.equals("First Aid")){
                                                videoList.add(video);
                                            }
                                        } else if (checkMedInfo){
                                            if (type.equals("Medical Info")){
                                                videoList.add(video);
                                            }
                                        }else if (checkTreat){
                                            if (type.equals("Treatment")){
                                                videoList.add(video);
                                            }
                                        }else if (checkFood){
                                            if (type.equals("Food")){
                                                videoList.add(video);
                                            }
                                        }else if (checkOther){
                                            if (type.equals("Other")){
                                                videoList.add(video);
                                            }
                                        }
                                    }

                                    Collections.reverse(videoList);
                                    mAdapter.notifyDataSetChanged();
                                    alertD.dismiss();
                                }

                                if (!videoList.isEmpty()){
                                    no_video_text.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });

                alertD.setView(promptView);
                alertD.show();
                return false;
            }
        });

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                if (!s.equals("")){
                    String searchWord = WordUtils.uncapitalize(s);
                    queryA = table_searchVideo.orderByChild("title").startAt(searchWord).endAt(searchWord + "\uf8ff");
                    queryA.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            videoList.clear();
                            if (dataSnapshot.exists()){
                                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                    Video video = snapshot.getValue(Video.class);
                                    videoList.add(video);
                                }

                                Collections.reverse(videoList);
                                mAdapter.notifyDataSetChanged();
                            }

                            if (!videoList.isEmpty()){
                                no_video_text.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else {
                    table_follow.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            videoList.clear();
                            if (dataSnapshot.exists()){
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    Follow follow = snapshot.getValue(Follow.class);
                                    if (follow.getUser_id().equals(uid)){
                                        final String user_id = follow.getFollow_id();

                                        query.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                videoList.clear();
                                                if (dataSnapshot.exists()){
                                                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                                        Video video = snapshot.getValue(Video.class);
                                                        if (video.getUid().equals(user_id) || video.getUid().equals(uid)){
                                                            videoList.add(video);
                                                        }

                                                    }

                                                    Collections.reverse(videoList);
                                                    mAdapter.notifyDataSetChanged();
                                                }

                                                if (!videoList.isEmpty()){
                                                    no_video_text.setVisibility(View.GONE);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
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
