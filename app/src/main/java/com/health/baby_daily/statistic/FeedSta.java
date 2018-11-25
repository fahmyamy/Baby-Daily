package com.health.baby_daily.statistic;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.contact.ContactList;
import com.health.baby_daily.model.Baby;

import java.util.Objects;

public class FeedSta extends AppCompatActivity {
    private Button btnList, btnChart;
    private String bId;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference table_check;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_sta);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Uri deepLink = this.getIntent().getData();
        if (deepLink != null && deepLink.isHierarchical()){
            String babyId = deepLink.getQueryParameter("baby_id");

            SharedPreferences pref = getSharedPreferences("selectedBaby", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("babyId", babyId);
        }else {
            SharedPreferences prefBaby = getSharedPreferences("selectedBaby", 0);
            bId = prefBaby.getString("babyId", null);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        uid = user.getUid();

        setTitle("Feed Statistic");

        btnList = findViewById(R.id.btnList);
        btnChart = findViewById(R.id.btnChart);

        table_check = FirebaseDatabase.getInstance().getReference("Baby").child(bId);

        FragmentTransaction frag = getSupportFragmentManager().beginTransaction();
        frag.replace(R.id.fragmentLayout, new FeedStaFragList());
        frag.commit();

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Bundle bundle = new Bundle();
                //bundle.putString("bId", bId);
                Fragment list = new FeedStaFragList();
                //list.setArguments(bundle);

                if (list != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragmentLayout, list);
                    ft.commit();
                }
            }
        });

        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Bundle bundle = new Bundle();
                //bundle.putString("bId", bId);
                Fragment chart = new FeedStaFragChart();
                //chart.setArguments(bundle);

                if (chart != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragmentLayout, chart);
                    ft.commit();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_reminder).setVisible(false);
        menu.findItem(R.id.action_misc_menu).setVisible(false);
        menu.findItem(R.id.action_profile).setVisible(false);
        table_check.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String parent_id = Objects.requireNonNull(dataSnapshot.child("parent_id").getValue()).toString();
                    if (parent_id.equals(uid)){
                        menu.findItem(R.id.action_share).setVisible(true);
                    }else if (!parent_id.equals(uid)) {
                        menu.findItem(R.id.action_share).setVisible(false);
                    }
                }else if (!dataSnapshot.exists()){
                    menu.findItem(R.id.action_share).setVisible(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //capable for sharing link and url scheme
        if (id == R.id.action_share){
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View promptView = layoutInflater.inflate(R.layout.alertdialog_custom, null);

            final AlertDialog alertD = new AlertDialog.Builder(this).create();

            ImageView inAppShare = (ImageView) promptView.findViewById(R.id.inAppShare);

            ImageView otherAppShare = (ImageView) promptView.findViewById(R.id.otherAppShare);

            inAppShare.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //inAppShare
                    String shareBody = "https://babydaily-a6e5a.firebaseapp.com/feed/index.html?baby_id="+ bId + System.lineSeparator() +"Hey try this out!!";
                    Intent shareIntent = new Intent(FeedSta.this, ContactList.class);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra("share_content", shareBody);
                    startActivity(shareIntent);
                    alertD.dismiss();
                }
            });

            otherAppShare.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String shareBody = "https://babydaily-a6e5a.firebaseapp.com/feed/index.html?baby_id="+ bId + System.lineSeparator() +"Hey try this out!!";
                    Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Baby-Daily");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(shareIntent,"Share via"));
                    alertD.dismiss();
                }
            });
            alertD.setView(promptView);
            alertD.show();
        }else if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
