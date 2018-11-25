package com.health.baby_daily.docmenu.createPost;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.health.baby_daily.R;
import com.health.baby_daily.adapter.UploadImageAdapter;
import com.health.baby_daily.model.PostGallery;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImageUpload extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private Button mSelectBtn;
    private RecyclerView mUploadList;

    private List<String> fileNameList;
    private List<String> fileDoneList;

    private UploadImageAdapter uploadListAdapter;

    private StorageReference mStorage;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference table_post;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        setTitle("Upload Image & Video");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        id = user.getUid();

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        table_post = db.getReference("Post_Image");

        mStorage = FirebaseStorage.getInstance().getReference();

        mSelectBtn = findViewById(R.id.selectBtn);
        mUploadList = findViewById(R.id.uploadRecyler);

        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();

        uploadListAdapter = new UploadImageAdapter(fileNameList, fileDoneList);

        //RecyclerView

        mUploadList.setLayoutManager(new LinearLayoutManager(this));
        mUploadList.setHasFixedSize(true);
        mUploadList.setAdapter(uploadListAdapter);

        mSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), RESULT_LOAD_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){

            if(data.getClipData() != null){

                int totalItemsSelected = data.getClipData().getItemCount();

                for(int i = 0; i < totalItemsSelected; i++){

                    final Uri fileUri = data.getClipData().getItemAt(i).getUri();

                    String fileName = getFileName(fileUri);

                    InputStream imageStream = null;
                    Bitmap selectedImage = null;
                    try {
                        imageStream = getContentResolver().openInputStream(fileUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);

                        selectedImage = getResizedBitmap(selectedImage, 280);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

                    fileNameList.add(fileName);
                    fileDoneList.add("uploading");
                    uploadListAdapter.notifyDataSetChanged();

                    final StorageReference fileToUpload = mStorage.child("Post/" + id).child(fileName);

                    final int finalI = i;
                    fileToUpload.putStream(bs).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileDoneList.remove(finalI);
                            fileDoneList.add(finalI, "done");

                            fileToUpload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String image = uri.toString() ;

                                    String ranId = UUID.randomUUID().toString();
                                    PostGallery post = new PostGallery(ranId, image, id);
                                    table_post.child(ranId).setValue(post);
                                }
                            });

                            uploadListAdapter.notifyDataSetChanged();

                        }
                    });

                }

                //Toast.makeText(MainActivity.this, "Selected Multiple Files", Toast.LENGTH_SHORT).show();

            } else if (data.getData() != null){

                final Uri fileUri = data.getData();

                String fileName = getFileName(fileUri);

                InputStream imageStream = null;
                Bitmap selectedImage = null;
                try {
                    imageStream = getContentResolver().openInputStream(fileUri);
                    selectedImage = BitmapFactory.decodeStream(imageStream);

                    selectedImage = getResizedBitmap(selectedImage, 280);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();
                ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

                fileNameList.add(fileName);
                fileDoneList.add("uploading");
                uploadListAdapter.notifyDataSetChanged();

                final StorageReference fileToUpload = mStorage.child("Post/" + id).child(fileName);

                final int finalI = 0;
                fileToUpload.putStream(bs).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        fileDoneList.remove(finalI);
                        fileDoneList.add(finalI, "done");

                        fileToUpload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String image = uri.toString() ;

                                String ranId = UUID.randomUUID().toString();
                                PostGallery post = new PostGallery(ranId, image, id);
                                table_post.child(ranId).setValue(post);
                            }
                        });

                        uploadListAdapter.notifyDataSetChanged();

                    }
                });


            }

        }

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

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = 220;
        } else if (bitmapRatio == 1){
            height = maxSize;
            width = maxSize;
        }else if (bitmapRatio < 1){
            height = maxSize;
            width = 220;
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}
