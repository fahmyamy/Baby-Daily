package com.health.baby_daily.misc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.health.baby_daily.R;
import com.health.baby_daily.model.Diary;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class DiaryAddActivity extends AppCompatActivity {

    private StorageReference firebaseStorage;
    private DatabaseReference table_diary;

    private Uri mImageUri = null;
    private static final int GALLERY_REQUEST = 1;

    private ProgressDialog progressDialog;

    private ImageView imageUpload;
    private TextView setBabyName, setDateTime;
    private EditText titleText, contentText;
    private Button saveButton;

    private String bId, bName, downloadUri, timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_add);

        firebaseStorage = FirebaseStorage.getInstance().getReference();
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        table_diary = db.getReference("Diary");

        SharedPreferences prefBaby = getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);
        bName = prefBaby.getString("babyName", null);

        imageUpload = findViewById(R.id.imageUpload);
        setBabyName = findViewById(R.id.setBabyName);
        setDateTime = findViewById(R.id.setDate);
        titleText = findViewById(R.id.titleText);
        contentText = findViewById(R.id.contentText);
        saveButton = findViewById(R.id.saveButton);

        Long timeStamp = System.currentTimeMillis()/1000;
        timestamp = timeStamp.toString();

        DateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy");
        Date date = new Date();
        setBabyName.setText(bName);
        setDateTime.setText(dateFormat.format(date));

        progressDialog = new ProgressDialog(this);

        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Adding to Diary.....");
                final String id = UUID.randomUUID().toString();
                final String title = titleText.getText().toString();
                final String content = contentText.getText().toString();
                final String date = setDateTime.getText().toString();

                if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content) && mImageUri != null){
                    progressDialog.show();
                    final StorageReference filepath = firebaseStorage.child("Diary").child(id);
                    filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUri = uri.toString();

                                    Diary diary = new Diary(id, title, content, downloadUri, timestamp+"_"+bId, date, bId);
                                    table_diary.child(id).setValue(diary);
                                }
                            });

                            progressDialog.dismiss();

                            Intent intent = new Intent(DiaryAddActivity.this, DiaryActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            mImageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                // Log.d(TAG, String.valueOf(bitmap));

                imageUpload.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
