package com.health.baby_daily.chatroom;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.health.baby_daily.model.Chat;
import com.squareup.picasso.Picasso;

public class ChatInfo extends AppCompatActivity {

    private ImageView userImage;
    private TextView userName, userEmail, deleteChat;
    private DatabaseReference table_user;
    private DatabaseReference table_chat;
    private DatabaseReference table_chatroom;
    private FirebaseAuth firebaseAuth;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Chat Information");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        id = user.getUid().toString();

        Intent intent = getIntent();
        final String second_id = intent.getStringExtra("second_id");

        userImage = findViewById(R.id.userImage);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        deleteChat = findViewById(R.id.deleteChat);

        //set username
        table_user = FirebaseDatabase.getInstance().getReference("User").child(second_id);
        table_user.keepSynced(true);
        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String username = dataSnapshot.child("username").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();

                    if (email.equals("none")){
                        Picasso.get().load(R.drawable.user_image).into(userImage);
                    }else {
                        Picasso.get().load(image).into(userImage);
                    }

                    userName.setText(username);
                    userEmail.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //get chat id
        table_chat = FirebaseDatabase.getInstance().getReference("Chatroom");
        table_chat.keepSynced(true);
        table_chat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Chat chat = snapshot.getValue(Chat.class);
                        if ((chat.getUserA().equals(id) && chat.getUserB().equals(second_id))
                            ||(chat.getUserA().equals(second_id) && chat.getUserB().equals(id))){

                            final String keyvalue = snapshot.getKey();

                            deleteChat.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    table_chatroom = FirebaseDatabase.getInstance().getReference("Chat");
                                    table_chatroom.keepSynced(true);
                                    Query query = table_chatroom.orderByChild("chat_id").equalTo(keyvalue);
                                    query.addValueEventListener(new ValueEventListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                for (DataSnapshot snapshot1: dataSnapshot.getChildren()){
                                                    String chatkey = snapshot1.getKey();

                                                    final DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Chat").child(chatkey);
                                                    dR.removeValue();
                                                }
                                            }

                                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chatroom").child(keyvalue);
                                            databaseReference.removeValue();
                                            ChatInfo.super.finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
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
