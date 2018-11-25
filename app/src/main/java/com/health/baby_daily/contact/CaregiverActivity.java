package com.health.baby_daily.contact;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.adapter.CaregiverAdapter;
import com.health.baby_daily.adapter.RequestCaregiverAdapter;
import com.health.baby_daily.chatroom.ChatRoom;
import com.health.baby_daily.model.ParentnCaregiverList;

import java.util.ArrayList;
import java.util.List;

public class CaregiverActivity extends AppCompatActivity {

    private FloatingActionButton add_caregiver;
    private RecyclerView entry_request;
    private RequestCaregiverAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView no_request_text, countRequest;
    private DatabaseReference table_request;
    private Query query;

    private RecyclerView entry_caregiver;
    private CaregiverAdapter mAdapterB;
    private RecyclerView.LayoutManager mLayoutManagerB;
    private TextView no_caregiver_text;
    private DatabaseReference table_caregiver;
    private Query queryB;

    private FirebaseAuth firebaseAuth;
    private String id;

    private List<ParentnCaregiverList> requestList;
    private List<ParentnCaregiverList> requestListB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver);

        setTitle("Caregiver");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        id = user.getUid().toString();

        add_caregiver = findViewById(R.id.add_caregiver);
        add_caregiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CaregiverActivity.this, SearchParent.class));
            }
        });

        requestList = new ArrayList<>();
        requestListB = new ArrayList<>();

        //requestlist caregiver
        table_request = FirebaseDatabase.getInstance().getReference().child("ParentnCaregiverList");
        table_request.keepSynced(true);
        query = table_request.orderByChild("id");
        query.keepSynced(true);

        entry_request= findViewById(R.id.entry_request);
        entry_request.setHasFixedSize(true);
        no_request_text = findViewById(R.id.no_request_text);
        countRequest = findViewById(R.id.countRequest);
        countRequest.setText("0");

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RequestCaregiverAdapter(requestList);

        entry_request.setLayoutManager(mLayoutManager);
        entry_request.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RequestCaregiverAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }
        });

        //caregiver
        table_caregiver = FirebaseDatabase.getInstance().getReference().child("ParentnCaregiverList");
        table_caregiver.keepSynced(true);
        queryB = table_caregiver.orderByChild("id");
        queryB.keepSynced(true);

        entry_caregiver= findViewById(R.id.entry_caregiver);
        entry_caregiver.setHasFixedSize(true);
        no_caregiver_text = findViewById(R.id.no_caregiver_text);

        mLayoutManagerB = new LinearLayoutManager(this);
        mAdapterB = new CaregiverAdapter(requestListB);

        entry_caregiver.setLayoutManager(mLayoutManagerB);
        entry_caregiver.setAdapter(mAdapterB);

        mAdapterB.setOnItemClickListener(new CaregiverAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                final String[] menu = {"View Profile", "Chat", "Unfriend"};

                AlertDialog.Builder builder = new AlertDialog.Builder(CaregiverActivity.this);
                builder.setItems(menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (menu[which].equals("View Profile")){
                            Intent intent = new Intent(CaregiverActivity.this, SearchedProfile.class);
                            intent.putExtra("id", requestListB.get(position).getCaregiver_id());
                            startActivity(intent);
                        }else if (menu[which].equals("Chat")){
                            Intent intent = new Intent(CaregiverActivity.this, ChatRoom.class);
                            intent.putExtra("second_id", requestListB.get(position).getCaregiver_id());
                            intent.putExtra("share_content", "none");
                            startActivity(intent);
                        }if (menu[which].equals("Unfriend")){
                            DatabaseReference table_caregiver = FirebaseDatabase.getInstance().getReference("ParentnCaregiverList");
                            table_caregiver.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                            ParentnCaregiverList caregiverList = snapshot.getValue(ParentnCaregiverList.class);
                                            if (caregiverList.getParent_id().equals(id)
                                                    && caregiverList.getCaregiver_id().equals(requestListB.get(position).getCaregiver_id())
                                                    && caregiverList.getStatus().equals("1")){

                                                String newid = caregiverList.getId();
                                                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ParentnCaregiverList").child(newid);
                                                databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        mAdapterB.notifyItemRemoved(position);
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
                builder.show();
            }
        });

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        requestList.clear();
                        if(dataSnapshot.exists()){
                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                ParentnCaregiverList request = snapshot.getValue(ParentnCaregiverList.class);
                                if (request.getParent_id().equals(id) & request.getStatus().equals("0")) {
                                    requestList.add(request);
                                }
                            }
                            int count = requestList.size();
                            countRequest.setText(String.valueOf(count));

                            if (requestList.isEmpty()){
                                no_request_text.setVisibility(View.VISIBLE);
                                countRequest.setText("0");
                            }else {
                                no_request_text.setVisibility(View.GONE);
                            }

                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                queryB.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        requestListB.clear();
                        if(dataSnapshot.exists()){
                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                ParentnCaregiverList request = snapshot.getValue(ParentnCaregiverList.class);
                                if (request.getParent_id().equals(id) & request.getStatus().equals("1")) {
                                    requestListB.add(request);
                                }
                            }

                            if (requestListB.isEmpty()){
                                no_caregiver_text.setVisibility(View.VISIBLE);
                            }else {
                                no_caregiver_text.setVisibility(View.GONE);
                            }

                            mAdapterB.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }
}
