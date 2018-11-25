package com.health.baby_daily.docmenu.uploadVideo;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.text.WordUtils;

import java.util.Objects;

public class ViewVideo extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference table_video;
    private DatabaseReference table_user;

    private String uid, video_id;
    private boolean isPlaying;

    private VideoView videoView;
    private ImageView playBtn, fullscreenBtn, videoImage;
    private TextView textTitle, textUser, textDate, textDesc;
    private Button editBtn;
    private CardView cardFunction;
    private FrameLayout fullscreenFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);

        setTitle("Video");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        uid = user.getUid();

        Intent intent = getIntent();
        video_id = intent.getStringExtra("video_id");

        isPlaying = false;
        videoView = findViewById(R.id.videoView);
        playBtn = findViewById(R.id.playBtn);
        textTitle = findViewById(R.id.textTitle);
        textDate = findViewById(R.id.textDate);
        textDesc = findViewById(R.id.textDesc);
        editBtn = findViewById(R.id.editBtn);
        cardFunction = findViewById(R.id.cardFunction);
        fullscreenFrame = findViewById(R.id.fullscreenFrame);
        fullscreenBtn = findViewById(R.id.fullscreenBtn);

        //user
        textUser = findViewById(R.id.textUser);
        videoImage = findViewById(R.id.videoImage);

        table_video = FirebaseDatabase.getInstance().getReference("Video").child(video_id);
        table_video.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String title = Objects.requireNonNull(dataSnapshot.child("title").getValue()).toString();
                    String date = Objects.requireNonNull(dataSnapshot.child("created").getValue()).toString();
                    String desc = Objects.requireNonNull(dataSnapshot.child("desc").getValue()).toString();
                    final String video = Objects.requireNonNull(dataSnapshot.child("video").getValue()).toString();
                    final String user_id = Objects.requireNonNull(dataSnapshot.child("uid").getValue()).toString();

                    if (uid.equals(user_id)){
                        cardFunction.setVisibility(View.VISIBLE);
                    }else {
                        cardFunction.setVisibility(View.GONE);
                    }

                    table_user = FirebaseDatabase.getInstance().getReference("User").child(user_id);
                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                String username = Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString();
                                String image = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();

                                if (!image.equals("none")){
                                    Picasso.get().load(image).into(videoImage);
                                }else {
                                    Picasso.get().load(R.drawable.user_image).into(videoImage);
                                }

                                textUser.setText(username);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    textTitle.setText(WordUtils.capitalize(title));
                    textDate.setText(date);
                    textDesc.setText(desc);

                    videoView.setVideoURI(Uri.parse(video));
                    videoView.requestFocus();
                    videoView.start();
                    isPlaying = true;
                    playBtn.setImageResource(R.drawable.ic_pause_circle);
                    fullscreenFrame.setVisibility(View.VISIBLE);

                    playBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isPlaying){
                                videoView.pause();
                                isPlaying = false;
                                playBtn.setImageResource(R.drawable.ic_play_circle);
                                fullscreenFrame.setVisibility(View.GONE);
                            }else {
                                videoView.start();
                                isPlaying = true;
                                playBtn.setImageResource(R.drawable.ic_pause_circle);
                                fullscreenFrame.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    fullscreenBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            videoView.pause();
                            Intent intent1 = new Intent(ViewVideo.this, FullscreenVideo.class);
                            intent1.putExtra("video_id", video);
                            startActivity(intent1);
                        }
                    });

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
