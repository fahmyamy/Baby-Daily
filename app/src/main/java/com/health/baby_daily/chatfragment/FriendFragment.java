package com.health.baby_daily.chatfragment;


import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.adapter.FriendAdapter;
import com.health.baby_daily.chatroom.ChatRoom;
import com.health.baby_daily.model.Follow;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendFragment extends Fragment {

    private FirebaseAuth firebaseAuth;

    private RecyclerView entry_friend;
    private FriendAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView no_friend_text;
    private DatabaseReference table_friend;
    private Query query;

    private List<Follow> followList;

    private String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        entry_friend = view.findViewById(R.id.entry_friend);
        entry_friend.setHasFixedSize(true);
        no_friend_text = view.findViewById(R.id.no_friend_text);
        followList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        id = user.getUid().toString();

        mLayoutManager = new LinearLayoutManager(view.getContext());
        mAdapter = new FriendAdapter(followList);

        entry_friend.setLayoutManager(mLayoutManager);
        entry_friend.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new FriendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String second_id = followList.get(position).getFollow_id();
                Intent intent = new Intent(getContext(), ChatRoom.class);
                intent.putExtra("second_id", second_id);
                intent.putExtra("share_content", "none");
                startActivity(intent);
            }
        });

        table_friend = FirebaseDatabase.getInstance().getReference("Follow");
        table_friend.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followList.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Follow follow = snapshot.getValue(Follow.class);
                        if (follow.getUser_id().equals(id)){
                            followList.add(follow);
                        }
                    }

                    if (followList.isEmpty()){
                        no_friend_text.setVisibility(View.VISIBLE);
                    }else {
                        no_friend_text.setVisibility(View.GONE);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
