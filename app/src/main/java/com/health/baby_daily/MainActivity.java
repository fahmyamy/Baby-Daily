package com.health.baby_daily;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.health.baby_daily.contact.CaregiverActivity;
import com.health.baby_daily.contact.DoctorActivity;
import com.health.baby_daily.docmenu.Gallery;
import com.health.baby_daily.fragment.ChatFrag;
import com.health.baby_daily.fragment.GuideFrag;
import com.health.baby_daily.fragment.HomeFrag;
import com.health.baby_daily.fragment.StatisticFrag;
import com.health.baby_daily.misc.CalorieActivity;
import com.health.baby_daily.misc.DiaryActivity;
import com.health.baby_daily.misc.OverallD;
import com.health.baby_daily.misc.PicknDrop;
import com.health.baby_daily.reminder.reminder_main;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private DataSnapshot dataSnapshot;
    private FirebaseStorage firebaseStorage;
    private FirebaseUser user;

    private TextView textUsername;
    private ImageView imageView;

    private Intent intent;
    String userName, image, role, user_id;

    private FrameLayout menuFrame;
    private BottomNavigationView navMenu;
    private NavigationView navigationView;
    private String bId, bName, bImage;

    private String user_role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        bId = intent.getStringExtra("babyId");
        bName = intent.getStringExtra("babyName");
        bImage = intent.getStringExtra("babyImage");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        }else {

            final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            View headerView = navigationView.getHeaderView(0);

            textUsername = (TextView) headerView.findViewById(R.id.textUsername);
            imageView = (ImageView) headerView.findViewById(R.id.imageView);
            final TextView textView = (TextView) headerView.findViewById(R.id.textView);

            user = firebaseAuth.getCurrentUser();

            //bottom menu
            menuFrame = (FrameLayout) findViewById(R.id.main_frame);
            navMenu = (BottomNavigationView) findViewById(R.id.main_nav);

            final ProgressDialog mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("Please Wait!!");
            mProgressDialog.setMessage("Loading Interface for User ...");
            mProgressDialog.show();
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);

            databaseReference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        userName = Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString();
                        textUsername.setText(userName);
                        textView.setText(Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString());
                        String imageUrl = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();
                        if(!imageUrl.equals("none")){
                            Picasso.get().load(imageUrl).into(imageView);
                        }else{
                            Picasso.get().load(R.drawable.user_image).into(imageView);
                        }

                        role = Objects.requireNonNull(dataSnapshot.child("role").getValue()).toString();
                        user_id = Objects.requireNonNull(dataSnapshot.child("id").getValue()).toString();

                        user_role = role;
                        //for another used
                        SharedPreferences pref = getSharedPreferences("currRole", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("role", role);
                        editor.putString("user_id", user_id);
                        editor.apply();

                        mProgressDialog.dismiss();
                    }else if ((!dataSnapshot.exists()) && (firebaseAuth.getCurrentUser() != null)){
                        startActivity(new Intent(MainActivity.this, UserDetailActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    mProgressDialog.dismiss();
                }
            });

            textView.setText(user.getEmail().toString());

            readData(new MyCallback() {
                @Override
                public void onCallback(String user_role) {
                    Log.d("TAG", user_role);
                    if (user_role.equals("Mother")){
                        navigationView.inflateMenu(R.menu.activity_main_drawer);
                        navMenu.inflateMenu(R.menu.nav_items);
                    }else if (user_role.equals("Father")){
                        navigationView.inflateMenu(R.menu.activity_main_drawer);
                        navMenu.inflateMenu(R.menu.nav_items);
                    }else if (user_role.equals("Caregiver")){
                        navigationView.inflateMenu(R.menu.activity_main_drawer_2);
                        navMenu.inflateMenu(R.menu.nav_items_2);
                    }else if (user_role.equals("Doctor")){
                        navigationView.inflateMenu(R.menu.activity_main_drawer_3);
                        navMenu.inflateMenu(R.menu.nav_items_3);
                    }
                }
            });

            //difference interface for difference users
            navMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    int id = menuItem.getItemId();

                    displaySelectedScreen(id);

                    return true;
                }
            });

            displaySelectedScreen(R.id.nav_home);

        }
    }

    public interface MyCallback {
        void onCallback(String value);
    }

    public void readData(final MyCallback myCallback) {
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String role = Objects.requireNonNull(dataSnapshot.child("role").getValue()).toString();
                    myCallback.onCallback(role);
                }else if ((!dataSnapshot.exists()) && (firebaseAuth.getCurrentUser() != null)){
                    startActivity(new Intent(MainActivity.this, UserDetailActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void displaySelectedScreen(int id) {
        Fragment fragment = null;

        if (id == R.id.nav_home) {
            fragment = new HomeFrag();
        } else if (id == R.id.nav_statistic) {
            fragment = new StatisticFrag();
        } else if (id == R.id.nav_guide) {
            fragment = new GuideFrag();
        } else if (id == R.id.nav_chating) {
            fragment = new ChatFrag();
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_frame, fragment);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        readData(new MyCallback() {
            @Override
            public void onCallback(String role) {
                if (role.equals("Mother")){
                    menu.findItem(R.id.action_profile).setVisible(false);
                    menu.findItem(R.id.action_share).setVisible(false);
                    menu.findItem(R.id.action_settings).setVisible(false);
                }else if (role.equals("Father")){
                    menu.findItem(R.id.action_profile).setVisible(false);
                    menu.findItem(R.id.action_share).setVisible(false);
                    menu.findItem(R.id.action_settings).setVisible(false);
                }else if (role.equals("Caregiver")){
                    menu.findItem(R.id.action_profile).setVisible(false);
                    menu.findItem(R.id.action_reminder).setVisible(false);
                    menu.findItem(R.id.action_share).setVisible(false);
                    menu.findItem(R.id.action_settings).setVisible(false);
                }else if (role.equals("Doctor")){
                    menu.findItem(R.id.action_profile).setVisible(false);
                    menu.findItem(R.id.action_reminder).setVisible(false);
                    menu.findItem(R.id.action_misc_menu).setVisible(false);
                    menu.findItem(R.id.action_share).setVisible(false);
                    menu.findItem(R.id.action_settings).setVisible(false);
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        //check pref
        SharedPreferences prefBaby = getSharedPreferences("selectedBaby", 0);
        String bId = prefBaby.getString("babyId", null);
        if(!bId.equals("none")){
            //noinspection SimplifiableIfStatement
            if (id == R.id.action_reminder){
                startActivity(new Intent(this, reminder_main.class));
                return true;
            }else if(id == R.id.action_misc_menu){
                startActivity(new Intent(this, MiscActivity.class));
                return true;
            }
        }else{
            Toast.makeText(this, "Please Select Baby to Proceed!!", Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //check pref
        SharedPreferences prefBaby = getSharedPreferences("selectedBaby", 0);
        String bId = prefBaby.getString("babyId", null);

        if (id == R.id.nav_profile) {
            startActivity(new Intent(this, UserProfile.class));
        }else if(id ==R.id.nav_babies) {
            startActivity(new Intent(this, BabyActivity.class));
        }else if (id == R.id.nav_caregiver){
            startActivity(new Intent(this, CaregiverActivity.class));
        }else if (id == R.id.nav_doctor){
            startActivity(new Intent(this, DoctorActivity.class));
        }else if(id ==R.id.nav_calorie) {
            if (!bId.equals("none")) {
                startActivity(new Intent(this, CalorieActivity.class));
            }else {
                Toast.makeText(this, "Please Select Baby to Proceed!!", Toast.LENGTH_SHORT).show();
            }
        }else if(id ==R.id.nav_diary) {
            if (!bId.equals("none")) {
                startActivity(new Intent(this, DiaryActivity.class));
            }else {
                Toast.makeText(this, "Please Select Baby to Proceed!!", Toast.LENGTH_SHORT).show();
            }
        }else if (id == R.id.nav_license){
            Toast.makeText(this, "Under Contruction", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_gallery){
            startActivity(new Intent(this, Gallery.class));
        }else if(id ==R.id.nav_pickNDrop) {
            if (!bId.equals("none")) {
                startActivity(new Intent(this, PicknDrop.class));
            }else {
                Toast.makeText(this, "Please Select Baby to Proceed!!", Toast.LENGTH_SHORT).show();
            }
        }else if(id ==R.id.nav_OD) {
            if (!bId.equals("none")) {
                startActivity(new Intent(this, OverallD.class));
            }else {
                Toast.makeText(this, "Please Select Baby to Proceed!!", Toast.LENGTH_SHORT).show();
            }
        }else if(id ==R.id.nav_setting) {

        }else if(id ==R.id.nav_about){
            startActivity(new Intent(this, AboutUsActivity.class));
        }else if (id == R.id.nav_logout) {
            firebaseAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
