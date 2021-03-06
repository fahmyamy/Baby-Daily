package com.health.baby_daily.guide.userprofile;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.adapter.TimelineAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Post extends Fragment {

    private FirebaseAuth firebaseAuth;

    private RecyclerView recyclerView;
    private TimelineAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView no_post_text;
    private DatabaseReference table_post;
    private Query query;

    private List<com.health.baby_daily.model.Post> postList;
    private String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent intent = Objects.requireNonNull(getActivity()).getIntent();
        uid = intent.getStringExtra("user_id");

        postList = new ArrayList<>();

        table_post = FirebaseDatabase.getInstance().getReference().child("Post");
        query = table_post.orderByChild("modified");
        query.keepSynced(true);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        no_post_text = view.findViewById(R.id.no_post_text);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new TimelineAdapter(postList);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);


        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postList.clear();
                        if (dataSnapshot.exists()){
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                com.health.baby_daily.model.Post post = snapshot.getValue(com.health.baby_daily.model.Post.class);
                                if (post.getUid().equals(uid))
                                    postList.add(post);
                            }

                            Collections.reverse(postList);
                            mAdapter.notifyDataSetChanged();
                        }

                        if (!postList.isEmpty()){
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
