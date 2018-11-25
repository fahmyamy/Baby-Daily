package com.health.baby_daily.misc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.health.baby_daily.R;
import com.squareup.picasso.Picasso;

public class DiaryViewActivity extends AppCompatActivity {

    private String id, diaryTitle, content, date, image;

    private TextView titleText, dateText, contentText;
    private ImageView imageView;
    private Button buttonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_view);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        diaryTitle = intent.getStringExtra("diaryTitle");
        content = intent.getStringExtra("content");
        date = intent.getStringExtra("date");
        image = intent.getStringExtra("image");

        setTitle(" ");

        titleText = findViewById(R.id.titleText);
        dateText = findViewById(R.id.dateText);
        contentText = findViewById(R.id.contentText);
        imageView = findViewById(R.id.imageView);
        buttonDelete = findViewById(R.id.buttonDelete);

        titleText.setText(diaryTitle);
        dateText.setText(date);
        contentText.setText(content);
        Picasso.get().load(image).fit().centerCrop().into(imageView);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Diary").child(id);
                databaseReference.removeValue();
                Toast.makeText(DiaryViewActivity.this, "Diary Removed!!" , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), DiaryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


    }
}
