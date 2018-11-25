package com.health.baby_daily.chatroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.adapter.ChatroomAdapter;
import com.health.baby_daily.model.Chat;
import com.health.baby_daily.model.Chatroom;
import com.health.baby_daily.model.Notify;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ChatRoom extends AppCompatActivity {
    private RecyclerView entry_chat;
    private ChatroomAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference table_chat;
    private DatabaseReference table_sendChat;
    private DatabaseReference table_chatroom;
    private DatabaseReference table_setChatroom;
    private DatabaseReference table_notify;
    private Query query, queryB;

    private FirebaseAuth firebaseAuth;
    private String id, role, chatroom_id, content;

    private List<Chatroom> chatroomList;
    private String timestamp;
    private String second_id;
    private DatabaseReference table_user;

    private EditText edittext_chatbox;
    private Button button_chatbox_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        id = user.getUid().toString();

        Intent intent = getIntent();
        second_id =  intent.getStringExtra("second_id");
        content = intent.getStringExtra("share_content");

        if (second_id == null){
            second_id = intent.getStringExtra("id");
            content = "none";
        }

        edittext_chatbox = findViewById(R.id.edittext_chatbox);
        if (!content.equals("none")){
            edittext_chatbox.setText(content);
        }

        //set username
        table_user = FirebaseDatabase.getInstance().getReference("User").child(second_id);
        table_user.keepSynced(true);
        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String username = dataSnapshot.child("username").getValue().toString();

                    setTitle(username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //declaration
        entry_chat = findViewById(R.id.entry_chat);
        button_chatbox_send = findViewById(R.id.button_chatbox_send);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        final String currentDate = dateFormat.format(date);

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        table_sendChat = db.getReference("Chat");

        final FirebaseDatabase db2 = FirebaseDatabase.getInstance();
        table_setChatroom = db2.getReference("Chatroom");

        final FirebaseDatabase db3 = FirebaseDatabase.getInstance();
        table_notify = db3.getReference("Notifications");

        //get chat key id
        table_chatroom = FirebaseDatabase.getInstance().getReference("Chatroom");
        table_chatroom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Chat chat = snapshot.getValue(Chat.class);
                        if ((chat.getUserA().equals(id) && chat.getUserB().equals(second_id))
                                || (chat.getUserA().equals(second_id) && chat.getUserB().equals(id))) {
                            chatroom_id = snapshot.getKey().toString();
                        }
                    }
                }

                button_chatbox_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Long timeStamp = System.currentTimeMillis()/1000;
                        timestamp = timeStamp.toString();

                        if (chatroom_id == null){
                            chatroom_id = UUID.randomUUID().toString();
                            Chat chat1 = new Chat(chatroom_id, id, second_id);
                            table_setChatroom.child(chatroom_id).setValue(chat1);
                        }

                        String content = edittext_chatbox.getText().toString();
                        final String newid = UUID.randomUUID().toString();
                        Chatroom chatroom = new Chatroom(newid, id, second_id, edittext_chatbox.getText().toString(), timestamp, currentDate, false, chatroom_id);
                        table_sendChat.child(newid).setValue(chatroom);
                        Notify notify = new Notify(id, "chat_message", content);
                        table_notify.child(second_id).push().setValue(notify);

                        mAdapter.addItem(chatroom);
                        entry_chat.scrollToPosition(mAdapter.getItemCount()-1);
                        mAdapter.notifyDataSetChanged();
                        edittext_chatbox.getText().clear();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //get prev chat
        chatroomList = new ArrayList<>();

        table_chat = FirebaseDatabase.getInstance().getReference().child("Chat");
        table_chat.keepSynced(true);
        query = table_chat.orderByChild("timestamp");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatroomList.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Chatroom chatroom = snapshot.getValue(Chatroom.class);
                        if (chatroom.getSender_id().equals(id) || chatroom.getReceiver_id().equals(id)){
                            if (chatroom.getSender_id().equals(second_id) || chatroom.getReceiver_id().equals(second_id)){
                                chatroomList.add(chatroom);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        mAdapter.notifyItemChanged(chatroomList.size());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        query.keepSynced(true);

        entry_chat.hasFixedSize();
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setStackFromEnd(true);
        mAdapter = new ChatroomAdapter(chatroomList);

        entry_chat.setLayoutManager(mLayoutManager);
        entry_chat.setAdapter(mAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_reminder).setVisible(false);
        menu.findItem(R.id.action_misc_menu).setVisible(false);
        menu.findItem(R.id.action_share).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //not profile but chatroom configuration including setting and delete chat
        if (id == R.id.action_profile){
            Intent intent = new Intent(this, ChatInfo.class);
            intent.putExtra("second_id", second_id);
            startActivity(intent);
        }else if (id == android.R.id.home){
            finish();
        }



        return super.onOptionsItemSelected(item);
    }
}
