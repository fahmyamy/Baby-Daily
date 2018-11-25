package com.health.baby_daily.docmenu.createPost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.health.baby_daily.R;
import com.health.baby_daily.docmenu.MedicalPost;
import com.health.baby_daily.model.Post;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdatePostPreview extends AppCompatActivity {

    private DatabaseReference table_post;

    private String post_id, title, html_content, type, created, uid, modified;
    private TextView textTitle;
    private Button cancelBtn, updateBtn;
    private WebView webPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_post_preview);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        post_id = intent.getStringExtra("post_id");
        title = intent.getStringExtra("title");
        html_content = intent.getStringExtra("html_content");
        type = intent.getStringExtra("type");
        created = intent.getStringExtra("created");
        uid = intent.getStringExtra("uid");

        textTitle = findViewById(R.id.textTitle);
        webPreview = findViewById(R.id.webPreview);
        cancelBtn = findViewById(R.id.cancelBtn);
        updateBtn = findViewById(R.id.updateBtn);

        textTitle.setText(WordUtils.capitalize(title));
        webPreview.setWebViewClient(new WebViewClient());
        webPreview.loadDataWithBaseURL(null, html_content, "text/html", "utf-8", null);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseDatabase db = FirebaseDatabase.getInstance();
                table_post = db.getReference("Post");

                Calendar calendar = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                modified = dateFormat.format(calendar.getTime());

                Post post = new Post(post_id, StringUtils.lowerCase(textTitle.getText().toString()), html_content, type, created, modified, uid);
                table_post.child(post_id).setValue(post);
                Intent intent = new Intent(UpdatePostPreview.this, MedicalPost.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}
