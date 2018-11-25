package com.health.baby_daily.docmenu.createPost;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;

import org.apache.commons.lang3.text.WordUtils;

import java.util.Objects;

public class ViewPost extends AppCompatActivity {

    private String post_id, uid;
    private DatabaseReference table_post;
    private FirebaseAuth firebaseAuth;

    private Button updateBtn;
    private TextView textTitle;
    private WebView webDisplay;
    private CardView cardFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        setTitle("Post");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        uid = user.getUid();

        Intent intent = getIntent();
        post_id = intent.getStringExtra("post_id");

        updateBtn = findViewById(R.id.updateBtn);
        textTitle = findViewById(R.id.textTitle);
        webDisplay = findViewById(R.id.webDisplay);
        cardFunction = findViewById(R.id.cardFunction);

        table_post = FirebaseDatabase.getInstance().getReference("Post").child(post_id);
        table_post.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    final String id = Objects.requireNonNull(dataSnapshot.child("id").getValue()).toString();
                    String title = Objects.requireNonNull(dataSnapshot.child("title").getValue()).toString();
                    String content = Objects.requireNonNull(dataSnapshot.child("content").getValue()).toString();
                    String user_id = Objects.requireNonNull(dataSnapshot.child("uid").getValue()).toString();

                    if (!user_id.equals(uid)){
                        cardFunction.setVisibility(View.GONE);
                    }else {
                        cardFunction.setVisibility(View.VISIBLE);
                    }

                    textTitle.setText(WordUtils.capitalize(title));
                    webDisplay.setWebViewClient(new WebViewClient());
                    webDisplay.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);

                    updateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ViewPost.this, UpdatePost.class);
                            intent.putExtra("post_id", id);
                            startActivity(intent);
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
