package com.health.baby_daily;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.adapter.BabyAdapter;
import com.health.baby_daily.adapter.PageAdapter;
import com.health.baby_daily.contact.SearchParent;
import com.health.baby_daily.contact.SearchParentV2;
import com.health.baby_daily.model.Baby;

import java.util.ArrayList;
import java.util.List;

public class BabyActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PageAdapter pageAdapter;
    private RecyclerView recyclerView;
    private BabyAdapter adapter;
    private TextView no_baby_text;
    private MultiSelector mMultiSelector = new MultiSelector();

    private List<Baby> babyList;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference table_baby;
    private FirebaseAuth firebaseAuth;

    private Query query;
    private int totalday, totalmonth, totalyear;
    private String role;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_activity);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefRole = getSharedPreferences("currRole", 0);
        role = prefRole.getString("role", null);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        no_baby_text = findViewById(R.id.no_baby_text);
        if (role.equals("Mother")){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(BabyActivity.this, BabyAddDetail.class));
                }
            });
        }else if (role.equals("Father")){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(BabyActivity.this, BabyAddDetail.class));
                }
            });
        }else if (role.equals("Caregiver")){
            fab.setImageResource(R.drawable.ic_add_person);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(BabyActivity.this, SearchParent.class));
                }
            });
            no_baby_text.setVisibility(View.GONE);
        }else if (role.equals("Doctor")){
            fab.setImageResource(R.drawable.ic_add_person);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(BabyActivity.this, SearchParentV2.class));
                }
            });
            no_baby_text.setVisibility(View.GONE);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        if (role.equals("Mother") || role.equals("Father")){
            tabLayout.setVisibility(View.GONE);
            babyList = new ArrayList<>();

            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            adapter = new BabyAdapter(this, babyList);
            recyclerView.setAdapter(adapter);

            table_baby = FirebaseDatabase.getInstance().getReference().child("Baby");
            table_baby.keepSynced(true);

            query = table_baby.orderByChild("parent_id")
                    .equalTo(firebaseAuth.getUid().toString());

            query.addListenerForSingleValueEvent(valueEventListener);
        }else if (role.equals("Caregiver") || role.equals("Doctor")){
            if (role.equals("Doctor")){
                setTitle("Patients");
            }else if (role.equals("Caregiver")){
                setTitle("Baby Management");
            }

            tabLayout.setVisibility(View.VISIBLE);

            pageAdapter = new PageAdapter(this, getSupportFragmentManager());
            viewPager.setAdapter(pageAdapter);

            tabLayout.setupWithViewPager(viewPager);
        }
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            babyList.clear();
            if(dataSnapshot.exists()){
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Baby baby = snapshot.getValue(Baby.class);
                    babyList.add(baby);
                }
                no_baby_text.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
