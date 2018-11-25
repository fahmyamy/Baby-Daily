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
import com.health.baby_daily.adapter.ChatAdapter;
import com.health.baby_daily.chatroom.ChatRoom;
import com.health.baby_daily.model.Chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerChat;
    private ChatAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView no_chat_text;
    private DatabaseReference table_chat;
    private Query query;

    private DatabaseReference table_chatB;
    private Query queryB;

    private FirebaseAuth firebaseAuth;

    private List<Chat> chatList;
    private String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        id = user.getUid().toString();

        chatList = new ArrayList<>();

        no_chat_text = view.findViewById(R.id.no_chat_text);
        recyclerChat = view.findViewById(R.id.recyclerChat);

        mAdapter = new ChatAdapter(chatList);
        mLayoutManager = new LinearLayoutManager(getContext());

        recyclerChat.setLayoutManager(mLayoutManager);
        recyclerChat.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ChatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String second_id = null;
                if (chatList.get(position).getUserA().equals(id)){
                    second_id = chatList.get(position).getUserB();
                }else if (chatList.get(position).getUserB().equals(id)){
                    second_id = chatList.get(position).getUserA();
                }
                Intent intent = new Intent(getContext(), ChatRoom.class);
                intent.putExtra("second_id", second_id);
                intent.putExtra("share_content", "none");
                getContext().startActivity(intent);
            }
        });

        table_chat = FirebaseDatabase.getInstance().getReference().child("Chatroom");
        table_chat.keepSynced(true);;
        table_chat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Chat chat = snapshot.getValue(Chat.class);
                        if (chat.getUserA().equals(id) || chat.getUserB().equals((id))){
                            chatList.add(chat);
                        }
                    }
                }

                if (!chatList.isEmpty()){
                    no_chat_text.setVisibility(View.GONE);
                }else {
                    no_chat_text.setVisibility(View.VISIBLE);
                }

                Collections.reverse(chatList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
