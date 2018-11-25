package com.health.baby_daily.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.adapter.SearchParentAdapter;
import com.health.baby_daily.guide.PostProfile;
import com.health.baby_daily.model.User;

import java.util.ArrayList;
import java.util.List;

public class SearchFriend extends AppCompatActivity {

    private DatabaseReference table_user;
    private FirebaseAuth firebaseAuth;
    private Query query;

    private RecyclerView friendView;
    private SearchParentAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<User> userList;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);

        setTitle("Search Friends");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        uid = user.getUid().toString();

        userList = new ArrayList<>();

        friendView = findViewById(R.id.friendView);
        friendView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new SearchParentAdapter(userList);

        friendView.setLayoutManager(mLayoutManager);
        friendView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new SearchParentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String id = userList.get(position).getId();
                Intent intent = new Intent(SearchFriend.this, PostProfile.class);
                intent.putExtra("user_id", id);
                startActivity(intent);
            }
        });

        table_user = FirebaseDatabase.getInstance().getReference().child("User");
        table_user.keepSynced(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        MenuItem searchFilterItem = menu.findItem(R.id.app_bar_filter);
        searchFilterItem.setVisible(false);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                if (!s.equals("")) {
                    query = table_user.orderByChild("username").startAt(s).endAt(s + "\uf8ff");
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            userList.clear();
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    User user = snapshot.getValue(User.class);
                                    if (user.getRole().equals("Doctor") && !user.getId().equals(uid)) {
                                        userList.add(user);
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else {
                    userList.clear();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!s.equals("")) {
                    query = table_user.orderByChild("username").startAt(s).endAt(s + "\uf8ff");
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            userList.clear();
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    User user = snapshot.getValue(User.class);
                                    if (user.getRole().equals("Doctor") && !user.getId().equals(uid)) {
                                        userList.add(user);
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else {
                    userList.clear();
                }

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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
