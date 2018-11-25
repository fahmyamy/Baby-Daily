package com.health.baby_daily.docmenu.createPost;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.health.baby_daily.R;
import com.health.baby_daily.docmenu.MedicalPost;
import com.health.baby_daily.model.Post;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class PostPreview extends AppCompatActivity {

    private Button cancelBtn, submitBtn;
    private WebView webPreview;

    private String titleValue, created, modified, uid;
    private String html_content;

    private DatabaseReference table_post;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_preview);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        uid = user.getUid();

        Intent intent = getIntent();
        html_content = intent.getStringExtra("html_content");

        cancelBtn = findViewById(R.id.cancelBtn);
        submitBtn = findViewById(R.id.submitBtn);

        webPreview = findViewById(R.id.webPreview);

        webPreview.setWebViewClient(new WebViewClient());
        webPreview.loadDataWithBaseURL(null, html_content, "text/html", "utf-8", null);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!html_content.equals(null)){
                    final EditText input = new EditText(PostPreview.this);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(PostPreview.this);
                    alertDialog.setTitle("Insert Title");
                    alertDialog.setView(input);

                    alertDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            titleValue = input.getText().toString();

                            final FirebaseDatabase db = FirebaseDatabase.getInstance();
                            table_post = db.getReference("Post");

                            Calendar calendar = Calendar.getInstance();
                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            created = dateFormat.format(calendar.getTime());
                            modified = created;

                            String id = UUID.randomUUID().toString();
                            Post post = new Post(id, StringUtils.lowerCase(titleValue), html_content, "Other", created, modified, uid);
                            table_post.child(id).setValue(post);
                            Intent intent = new Intent(PostPreview.this, MedicalPost.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });

                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(PostPreview.this, "Uploading Post Failed!!" , Toast.LENGTH_SHORT).show();
                        }
                    });

                    alertDialog.show();
                }else {
                    Toast.makeText(PostPreview.this, "Post Empty!" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
