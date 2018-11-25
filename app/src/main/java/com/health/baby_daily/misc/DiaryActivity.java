package com.health.baby_daily.misc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.adapter.DiaryAdapter;
import com.health.baby_daily.model.Diary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiaryActivity extends AppCompatActivity {

    private FloatingActionButton add_diary;
    private RecyclerView entry_diaryList;
    private DiaryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView no_baby_text;
    private DatabaseReference table_diary;
    private Query query;

    private String bId;

    private List<Diary> mDiaryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        SharedPreferences prefBaby = getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        add_diary = findViewById(R.id.add_diary);
        add_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DiaryActivity.this, DiaryAddActivity.class));
            }
        });

        mDiaryList = new ArrayList<>();

        table_diary = FirebaseDatabase.getInstance().getReference().child("Diary");
        query = table_diary.orderByChild("timestamp_babyId").endAt("_"+ bId);
        query.keepSynced(true);

        entry_diaryList = findViewById(R.id.entry_diaryList);
        entry_diaryList.setHasFixedSize(true);
        no_baby_text = findViewById(R.id.no_baby_text);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new DiaryAdapter(mDiaryList);

        entry_diaryList.setLayoutManager(mLayoutManager);
        entry_diaryList.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new DiaryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String id = mDiaryList.get(position).getId();
                String title = mDiaryList.get(position).getTitle();
                String image = mDiaryList.get(position).getImage();
                String content = mDiaryList.get(position).getContent();
                String date = mDiaryList.get(position).getDateTime();
                Intent intent = new Intent(DiaryActivity.this, DiaryViewActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("diaryTitle", title);
                intent.putExtra("image", image);
                intent.putExtra("content", content);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDiaryList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Diary diary = snapshot.getValue(Diary.class);
                        if (diary.getBabyId().equals(bId)){
                            mDiaryList.add(diary);
                        }else {
                            no_baby_text.setVisibility(View.VISIBLE);
                        }
                    }
                    no_baby_text.setVisibility(View.GONE);
                    Collections.reverse(mDiaryList);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
