package com.health.baby_daily.docmenu;


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
import com.health.baby_daily.adapter.VTimelineAdapter;
import com.health.baby_daily.model.Video;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoPostTimeline extends Fragment {

    private FirebaseAuth firebaseAuth;

    private RecyclerView recyclerView;
    private VTimelineAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView no_post_text;
    private DatabaseReference table_video;
    private Query query;

    private List<Video> videoList;
    private String uid;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_post_timeline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        uid = user.getUid();

        videoList = new ArrayList<>();

        table_video = FirebaseDatabase.getInstance().getReference().child("Video");
        query = table_video.orderByChild("created");
        query.keepSynced(true);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        no_post_text = view.findViewById(R.id.no_post_text);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new VTimelineAdapter(videoList);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        videoList.clear();
                        if (dataSnapshot.exists()){
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                Video video = snapshot.getValue(Video.class);
                                if (video.getUid().equals(uid)){
                                    videoList.add(video);
                                }

                            }

                            Collections.reverse(videoList);
                            mAdapter.notifyDataSetChanged();
                        }

                        if (!videoList.isEmpty()){
                            no_post_text.setVisibility(View.GONE);
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
