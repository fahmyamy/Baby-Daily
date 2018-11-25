package com.health.baby_daily.docmenu.uploadVideo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.health.baby_daily.R;
import com.health.baby_daily.docmenu.VideoPost;
import com.health.baby_daily.model.Video;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class AddVideoPost extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference table_video;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private ProgressDialog progressDialog;

    private static final int PICK_VIDEO_REQUEST = 3;
    private EditText videoTitle, videoDesc;
    private Spinner spinnerType;
    private Button uploadBtn, submitBtn;
    private ImageView playBtn;
    private VideoView videoLoad;
    private Uri filePath;
    private boolean isPlaying;
    private String uid, videoUrl, created;

    String[] list = { "Select Video Type", "First Aid", "Medical Info", "Treatment", "Food", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video_post);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Add Video");

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
        submitBtn = findViewById(R.id.submitBtn);
        playBtn = findViewById(R.id.playBtn);
        videoLoad = findViewById(R.id.videoLoad);

        //spinner content
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                progressDialog = new ProgressDialog(AddVideoPost.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();

                final String title = videoTitle.getText().toString();
                final String type = String.valueOf(spinnerType.getSelectedItem());
                final String desc = videoDesc.getText().toString();

                String fileName = getFileName(filePath);

                if (title.isEmpty()){
                    Toast.makeText(AddVideoPost.this, "Please Insert Title", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (type.equals("Select Video Type")){
                    Toast.makeText(AddVideoPost.this, "Please Select Video Type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (filePath == null){
                    Toast.makeText(AddVideoPost.this, "Please Upload Video ", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Calendar calendar = Calendar.getInstance();
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    created = dateFormat.format(calendar.getTime());

                    final StorageReference ref = storageReference.child("Video/" + uid).child(fileName);
                    ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    videoUrl = uri.toString();

                                    String id = UUID.randomUUID().toString();
                                    Video video = new Video(id, StringUtils.lowerCase(title), type, desc, videoUrl, created, uid);
                                    table_video.child(id).setValue(video);
                                    progressDialog.dismiss();
                                    Toast.makeText(AddVideoPost.this, "New Video Post Added!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AddVideoPost.this, VideoPost.class);
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