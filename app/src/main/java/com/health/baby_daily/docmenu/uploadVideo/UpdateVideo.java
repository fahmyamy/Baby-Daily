package com.health.baby_daily.docmenu.uploadVideo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.health.baby_daily.R;
import com.health.baby_daily.docmenu.VideoPost;
import com.health.baby_daily.model.Video;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Objects;

public class UpdateVideo extends AppCompatActivity {

    private String video_id;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference table_video;
    private DatabaseReference table_videoCheck;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private ProgressDialog progressDialog;

    private static final int PICK_VIDEO_REQUEST = 3;
    private EditText videoTitle, videoDesc;
    private Spinner spinnerType;
    private Button uploadBtn, updateBtn;
    private ImageView playBtn;
    private VideoView videoLoad;
    private Uri filePath;
    private boolean isPlaying;
    private String uid, videoUrl, created;

    String[] list = { "Select Video Type", "First Aid", "Medical Info", "Treatment", "Food", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_video);

        setTitle("Update Video");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        video_id = intent.getStringExtra("video_id");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        uid = user.getUid();

        table_video = FirebaseDatabase.getInstance().getReference("Video");

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        isPlaying = false;
        videoTitle = findViewById(R.id.videoTitle);
        videoDesc = findViewById(R.id.videoDesc);
        spinnerType = findViewById(R.id.spinnerType);
        uploadBtn = findViewById(R.id.uploadBtn);
        updateBtn = findViewById(R.id.updateBtn);
        playBtn = findViewById(R.id.playBtn);
        videoLoad = findViewById(R.id.videoLoad);

        //spinner content
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        table_videoCheck = FirebaseDatabase.getInstance().getReference("Video").child(video_id);
        table_videoCheck.keepSynced(true);
        table_videoCheck.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String title = Objects.requireNonNull(dataSnapshot.child("title").getValue()).toString();
                    String type = Objects.requireNonNull(dataSnapshot.child("type").getValue()).toString();
                    String desc = Objects.requireNonNull(dataSnapshot.child("desc").getValue()).toString();
                    final String created = Objects.requireNonNull(dataSnapshot.child("created").getValue()).toString();
                    String user_id = Objects.requireNonNull(dataSnapshot.child("uid").getValue()).toString();
                    videoUrl = Objects.requireNonNull(dataSnapshot.child("video").getValue()).toString();

                    videoTitle.setText(WordUtils.capitalize(title));
                    int position = adapter.getPosition(type);
                    spinnerType.setSelection(position);
                    videoDesc.setText(desc);

                    videoLoad.setVideoURI(Uri.parse(videoUrl));
                    videoLoad.requestFocus();
                    videoLoad.start();
                    isPlaying = true;
                    playBtn.setImageResource(R.drawable.ic_pause_circle);

                    playBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isPlaying){
                                videoLoad.pause();
                                isPlaying = false;
                                playBtn.setImageResource(R.drawable.ic_play_circle);
                            }else {
                                videoLoad.start();
                                isPlaying = true;
                                playBtn.setImageResource(R.drawable.ic_pause_circle);
                            }
                        }
                    });

                    updateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            progressDialog = new ProgressDialog(UpdateVideo.this);
                            progressDialog.setMessage("Please Wait...");
                            progressDialog.show();

                            final String title = videoTitle.getText().toString();
                            final String type = String.valueOf(spinnerType.getSelectedItem());
                            final String desc = videoDesc.getText().toString();

                            if (title.isEmpty()){
                                Toast.makeText(UpdateVideo.this, "Please Insert Title", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (type.equals("Select Video Type")){
                                Toast.makeText(UpdateVideo.this, "Please Select Video Type", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (filePath == null){
                                Video video = new Video(video_id, StringUtils.lowerCase(title), type, desc, videoUrl, created, uid);
                                table_video.child(video_id).setValue(video);
                                progressDialog.dismiss();
                                Toast.makeText(UpdateVideo.this, "Video Post Updated!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UpdateVideo.this, VideoPost.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }else {

                                String fileName = getFileName(filePath);
                                final StorageReference ref = storageReference.child("Video/" + uid).child(fileName);
                                ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                videoUrl = uri.toString();

                                                Video video = new Video(video_id, StringUtils.lowerCase(title), type, desc, videoUrl, created, uid);
                                                table_video.child(video_id).setValue(video);
                                                progressDialog.dismiss();
                                                Toast.makeText(UpdateVideo.this, "Video Post Updated!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(UpdateVideo.this, VideoPost.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST);
            }
        });
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();

            videoLoad.setVideoURI(filePath);
            videoLoad.requestFocus();
            videoLoad.start();
            isPlaying = true;
            playBtn.setImageResource(R.drawable.ic_pause_circle);

            playBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isPlaying){
                        videoLoad.pause();
                        isPlaying = false;
                        playBtn.setImageResource(R.drawable.ic_play_circle);
                    }else {
                        videoLoad.start();
                        isPlaying = true;
                        playBtn.setImageResource(R.drawable.ic_pause_circle);
                    }
                }
            });
        }
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
