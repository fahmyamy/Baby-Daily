package com.health.baby_daily.guide;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.guide.userprofile.Post;
import com.health.baby_daily.guide.userprofile.Video;
import com.health.baby_daily.model.Follow;
import com.health.baby_daily.model.Notify;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PostProfile extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference table_user, table_follower;

    private ImageView userImage;
    private TextView username, userEmail, userFollowing, userFollower;
    private Button btnFollow, btnUnFollow;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String uid;
    private boolean checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_profile);

        setTitle("Profile");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        uid = user.getUid();

        userImage = findViewById(R.id.userImage);
        username = findViewById(R.id.username);
        userEmail = findViewById(R.id.userEmail);
        userFollowing = findViewById(R.id.countFollowing);
        userFollower = findViewById(R.id.countFollower);
        btnFollow = findViewById(R.id.btnFollow);
        btnUnFollow = findViewById(R.id.btnUnFollow);

        Intent intent = getIntent();
        final String user_id = intent.getStringExtra("user_id");

        if (uid.equals(user_id)){
            btnFollow.setVisibility(View.GONE);
        }

        table_user = FirebaseDatabase.getInstance().getReference("User").child(user_id);
        table_user.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String userName = Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString();
                    String email = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();
                    String image = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();

                    username.setText(userName);
                    userEmail.setText(email);

                    if (image.equals("none")){
                        Picasso.get().load(R.drawable.user_image).into(userImage);
                    }else {
                        Picasso.get().load(image).into(userImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        table_follower = FirebaseDatabase.getInstance().getReference("Follow");
        table_follower.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    int countFollowing = 0, countFollower = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        final Follow follow = snapshot.getValue(Follow.class);
                        if (follow.getUser_id().equals(uid) && follow.getFollow_id().equals(user_id)){
                            btnFollow.setVisibility(View.GONE);
                            btnUnFollow.setVisibility(View.VISIBLE);
                            btnUnFollow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String follow_id = follow.getId();
                                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Follow").child(follow_id);
                                    databaseReference.removeValue();

                                    btnFollow.setVisibility(View.VISIBLE);
                                    btnUnFollow.setVisibility(View.GONE);
                                }
                            });
                        }

                        if (follow.getUser_id().equals(uid)){
                            countFollowing++;
                        }else if (follow.getFollow_id().equals(uid)){
                            countFollower++;
                        }
                    }

                    userFollowing.setText(String.valueOf(countFollowing));
                    userFollower.setText(String.valueOf(countFollower));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference table_follow = FirebaseDatabase.getInstance().getReference("Follow");
                checked = false;
                final DatabaseReference table_notify = FirebaseDatabase.getInstance().getReference("Notifications");
                table_follow.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            outerloop :
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Follow follow1 = snapshot.getValue(Follow.class);
                                if (follow1.getUser_id().equals(user_id) && follow1.getFollow_id().equals(uid)){
                                    checked = true;
                                    break outerloop;
                                }
                            }
                        }

                        if (checked){
                            Notify notify = new Notify(uid, "follow_back", null);
                            table_notify.child(user_id).push().setValue(notify);
                        }else if (!checked) {
                            Notify notify = new Notify(uid, "follow", null);
                            table_notify.child(user_id).push().setValue(notify);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                String table_id = UUID.randomUUID().toString();
                Follow follow = new Follow(table_id, uid, user_id, "none");
                table_follow.child(table_id).setValue(follow);


            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        PostProfile.Adapter adapter = new PostProfile.Adapter(getSupportFragmentManager());

        adapter.addFragment(new Post(), "Post");
        adapter.addFragment(new Video(), "Video");

        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
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
