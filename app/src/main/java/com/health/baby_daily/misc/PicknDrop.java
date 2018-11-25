package com.health.baby_daily.misc;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.model.Chat;
import com.health.baby_daily.model.Chatroom;
import com.health.baby_daily.model.Notify;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class PicknDrop extends AppCompatActivity {

    private DatabaseReference table_sendChat, table_setChatroom, table_notify, table_chatroom;
    private FirebaseAuth firebaseAuth;

    private ImageView dropoff, pickup;
    private String bId, id, chatroom_id, timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickn_drop);

        setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefBaby = getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        id = user.getUid().toString();

        dropoff = findViewById(R.id.dropoff);
        pickup = findViewById(R.id.pickup);

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        table_sendChat = db.getReference("Chat");

        final FirebaseDatabase db2 = FirebaseDatabase.getInstance();
        table_setChatroom = db2.getReference("Chatroom");

        final FirebaseDatabase db3 = FirebaseDatabase.getInstance();
        table_notify = db3.getReference("Notifications");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        final String currentDate = dateFormat.format(date);

        DatabaseReference table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
        table_baby.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    final String caregiver_id = Objects.requireNonNull(dataSnapshot.child("caregiver_id").getValue()).toString();
                    final String bName = Objects.requireNonNull(dataSnapshot.child("fullName").getValue()).toString();

                    if (caregiver_id.equals("none")){
                        Toast.makeText(PicknDrop.this, "This Baby not Assigned to any Caregiver", Toast.LENGTH_LONG).show();
                    }else if (!caregiver_id.equals("none")){
                        table_chatroom = FirebaseDatabase.getInstance().getReference("Chatroom");
                        table_chatroom.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        Chat chat = snapshot.getValue(Chat.class);
                                        if ((chat.getUserA().equals(id) && chat.getUserB().equals(caregiver_id))
                                                || (chat.getUserA().equals(caregiver_id) && chat.getUserB().equals(id))) {
                                            chatroom_id = snapshot.getKey().toString();
                                        }
                                    }
                                }

                                dropoff.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Long timeStamp = System.currentTimeMillis()/1000;
                                        timestamp = timeStamp.toString();

                                        if (chatroom_id == null){
                                            chatroom_id = UUID.randomUUID().toString();
                                            Chat chat1 = new Chat(chatroom_id, id, caregiver_id);
                                            table_setChatroom.child(chatroom_id).setValue(chat1);
                                        }

                                        String content = "On my way to drop " + bName;
                                        final String newid = UUID.randomUUID().toString();
                                        Chatroom chatroom = new Chatroom(newid, id, caregiver_id, content, timestamp, currentDate, false, chatroom_id);
                                        table_sendChat.child(newid).setValue(chatroom);
                                        Notify notify = new Notify(id, "chat_message", content);
                                        table_notify.child(caregiver_id).push().setValue(notify);

                                        Toast.makeText(PicknDrop.this, "Message Delivered to Caregiver", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                pickup.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Long timeStamp = System.currentTimeMillis()/1000;
                                        timestamp = timeStamp.toString();

                                        if (chatroom_id == null){
                                            chatroom_id = UUID.randomUUID().toString();
                                            Chat chat1 = new Chat(chatroom_id, id, caregiver_id);
                                            table_setChatroom.child(chatroom_id).setValue(chat1);
                                        }

                                        String content = "On my way to pick " + bName;
                                        final String newid = UUID.randomUUID().toString();
                                        Chatroom chatroom = new Chatroom(newid, id, caregiver_id, content, timestamp, currentDate, false, chatroom_id);
                                        table_sendChat.child(newid).setValue(chatroom);
                                        Notify notify = new Notify(id, "chat_message", content);
                                        table_notify.child(caregiver_id).push().setValue(notify);

                                        Toast.makeText(PicknDrop.this, "Message Delivered to Caregiver", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
