package com.health.baby_daily.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.health.baby_daily.BabyActivity;
import com.health.baby_daily.MainActivity;
import com.health.baby_daily.R;
import com.health.baby_daily.RegisterActivity;
import com.health.baby_daily.UserDetailActivity;
import com.health.baby_daily.docmenu.MedicalPost;
import com.health.baby_daily.docmenu.VideoPost;
import com.health.baby_daily.event.appointmentEvent;
import com.health.baby_daily.event.bottleSelect;
import com.health.baby_daily.event.diaperEvent;
import com.health.baby_daily.event.feedEvent;
import com.health.baby_daily.event.measurementEvent;
import com.health.baby_daily.event.medSelect;
import com.health.baby_daily.event.sleepEvent;
import com.health.baby_daily.event.vaccineEvent;
import com.health.baby_daily.statistic.AppointmentSta;
import com.health.baby_daily.statistic.VaccineSta;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class HomeFrag extends Fragment{

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;

    List<String> babyName = new ArrayList<>();
    List<String> babyID = new ArrayList<>();
    List<String> babyImage = new ArrayList<>();
    List<Integer> babyAge = new ArrayList<>();

    private CardView bottleView, sleepView, diaperView, feedView, medView, measureView, vaccineView, calendarView;
    private Spinner spinnerBaby;

    private String bId, bName, bImage, role;
    private String currId;
    private String clickedID, clickedName, clickedImage;
    private int totalday, totalmonth, totalyear, clickedAge;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, null);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*SharedPreferences prefRole = this.getActivity().getSharedPreferences("currRole", 0);
        role = prefRole.getString("role", null);*/

        final LinearLayout line3 = view.findViewById(R.id.line3row);
        final LinearLayout line4 = view.findViewById(R.id.line4row);
        final LinearLayout line2 = view.findViewById(R.id.line2row);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        currId = firebaseUser.getUid();

        babyName.add("Select Baby");
        babyImage.add("none");
        babyID.add("none");
        babyAge.add(0);

        spinnerBaby = (Spinner) view.findViewById(R.id.textSelectBaby);
        final ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, babyName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerBaby.setAdapter(adapter);

        SharedPreferences prefBaby = getActivity().getSharedPreferences("selectedBaby", 0);
        String bId = prefBaby.getString("babyId", null);
        final String bName = prefBaby.getString("babyName", null);
        String bImage = prefBaby.getString("babyImage", null);
        int bAge = prefBaby.getInt("babyAge", -1);

        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(currId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (dataSnapshot.exists()){
                        role = Objects.requireNonNull(dataSnapshot.child("role").getValue()).toString();



                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (role.equals("Caregiver")){
                                    line3.setVisibility(View.GONE);
                                    line4.setVisibility(View.GONE);

                                    final Query query = FirebaseDatabase.getInstance().getReference("Baby")
                                            .orderByChild("caregiver_id")
                                            .equalTo(firebaseAuth.getUid().toString());

                                    query.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    String name = snapshot.child("fullName").getValue().toString();
                                                    String image = snapshot.child("image").getValue().toString();
                                                    String id = snapshot.child("bId").getValue().toString();
                                                    String dob = snapshot.child("dob").getValue().toString();
                                                    babyName.add(name);
                                                    babyImage.add(image);
                                                    babyID.add(id);

                                                    String[] datesplit = dob.split("/");
                                                    int day = Integer.parseInt(datesplit[0]);
                                                    int month = Integer.parseInt(datesplit[1]);
                                                    int year = Integer.parseInt(datesplit[2]);

                                                    Calendar calendar = Calendar.getInstance();
                                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                                    String currentdate = dateFormat.format(calendar.getTime());
                                                    String[] currsplit = currentdate.split("/");
                                                    int currday = Integer.parseInt(currsplit[0]);
                                                    int currmonth = Integer.parseInt(currsplit[1]);
                                                    int curryear = Integer.parseInt(currsplit[2]);

                                                    //subtract date
                                                    if ((currday == day) || (currday > day)) {
                                                        totalday = currday - day;
                                                    } else if (currday < day) {
                                                        currday = currday + 31;
                                                        totalday = currday - day;
                                                        currmonth = currmonth - 1;
                                                    }

                                                    if ((currmonth == month) || (currmonth > month)) {
                                                        totalmonth = currmonth - month;
                                                        totalyear = curryear - year;
                                                    } else if (currmonth < month) {
                                                        currmonth = currmonth + 12;
                                                        totalmonth = currmonth - month;
                                                        curryear = curryear - 1;
                                                        totalyear = curryear - year;
                                                    }

                                                    babyAge.add(totalyear);

                                                    int position = adapter.getPosition(bName);
                                                    spinnerBaby.setSelection(position);

                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                }else if (role.equals("Mother") || role.equals("Father")){
                                    line3.setVisibility(View.VISIBLE);
                                    line4.setVisibility(View.VISIBLE);

                                    final Query query = FirebaseDatabase.getInstance().getReference("Baby")
                                            .orderByChild("parent_id")
                                            .equalTo(firebaseAuth.getUid().toString());

                                    query.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    String name = snapshot.child("fullName").getValue().toString();
                                                    String image = snapshot.child("image").getValue().toString();
                                                    String id = snapshot.child("bId").getValue().toString();
                                                    String dob = snapshot.child("dob").getValue().toString();
                                                    babyName.add(name);
                                                    babyImage.add(image);
                                                    babyID.add(id);

                                                    String[] datesplit = dob.split("/");
                                                    int day = Integer.parseInt(datesplit[0]);
                                                    int month = Integer.parseInt(datesplit[1]);
                                                    int year = Integer.parseInt(datesplit[2]);

                                                    Calendar calendar = Calendar.getInstance();
                                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                                    String currentdate = dateFormat.format(calendar.getTime());
                                                    String[] currsplit = currentdate.split("/");
                                                    int currday = Integer.parseInt(currsplit[0]);
                                                    int currmonth = Integer.parseInt(currsplit[1]);
                                                    int curryear = Integer.parseInt(currsplit[2]);

                                                    //subtract date
                                                    if ((currday == day) || (currday > day)) {
                                                        totalday = currday - day;
                                                    } else if (currday < day) {
                                                        currday = currday + 31;
                                                        totalday = currday - day;
                                                        currmonth = currmonth - 1;
                                                    }

                                                    if ((currmonth == month) || (currmonth > month)) {
                                                        totalmonth = currmonth - month;
                                                        totalyear = curryear - year;
                                                    } else if (currmonth < month) {
                                                        currmonth = currmonth + 12;
                                                        totalmonth = currmonth - month;
                                                        curryear = curryear - 1;
                                                        totalyear = curryear - year;
                                                    }

                                                    babyAge.add(totalyear);

                                                    int position = adapter.getPosition(bName);
                                                    spinnerBaby.setSelection(position);

                                                    //babyList.add(new BabyItem(name,image,id));
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                }else if (role.equals("Doctor")){
                                    line3.setVisibility(View.GONE);
                                    line4.setVisibility(View.GONE);
                                    line2.setGravity(Gravity.CENTER);
                                    CardView feedView = view.findViewById(R.id.feedView);
                                    feedView.setVisibility(View.GONE);

                                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                                    params.gravity = Gravity.CENTER;

                                    LinearLayout menuLine = view.findViewById(R.id.menuLine);
                                    menuLine.setLayoutParams(params);

                                    TextView testText = view.findViewById(R.id.testText);
                                    LinearLayout spinnerBaby = view.findViewById(R.id.selectBaby);
                                    TextView menuText = view.findViewById(R.id.menuText);

                                    testText.setVisibility(View.GONE);
                                    spinnerBaby.setVisibility(View.GONE);
                                    menuText.setVisibility(View.VISIBLE);

                                    //video upload
                                    ImageView bottleImage = view.findViewById(R.id.bottleImage);
                                    TextView bottleText = view.findViewById(R.id.bottleText);
                                    TextView bottleReminder = view.findViewById(R.id.bottleReminder);

                                    bottleImage.setImageResource(R.drawable.vidupload);
                                    bottleText.setText("Video Upload");
                                    bottleReminder.setText("Upload Video for Stream Capabilities");

                                    //medical post
                                    ImageView sleepImage = view.findViewById(R.id.sleepImage);
                                    TextView sleepText = view.findViewById(R.id.sleepText);
                                    TextView sleepReminder = view.findViewById(R.id.sleepReminder);

                                    sleepImage.setImageResource(R.drawable.vaccineinfo);
                                    sleepText.setText("Medical Post");
                                    sleepReminder.setText("Post Medical Information or Article about Baby ");

                                    //patient view
                                    ImageView diaperImage = view.findViewById(R.id.diaperImage);
                                    TextView diaperText = view.findViewById(R.id.diaperText);
                                    TextView diaperReminder = view.findViewById(R.id.diaperReminder);

                                    diaperImage.setImageResource(R.drawable.patient);
                                    diaperText.setText("Patients");
                                    diaperReminder.setText("View Patient Under Your Protection");

                                }
                            }
                        });
                    }else if ((!dataSnapshot.exists()) && (firebaseAuth.getCurrentUser() != null)){
                        startActivity(new Intent(getContext(), UserDetailActivity.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.keepSynced(true);

        bottleView = view.findViewById(R.id.bottleView);
        sleepView = view.findViewById(R.id.sleepView);
        diaperView = view.findViewById(R.id.diaperView);
        feedView = view.findViewById(R.id.feedView);
        medView = view.findViewById(R.id.medView);
        measureView = view.findViewById(R.id.measureView);
        vaccineView = view.findViewById(R.id.vaccineView);
        calendarView = view.findViewById(R.id.calendarView);


        if (firebaseAuth.getCurrentUser() == null) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), RegisterActivity.class));
        }

        spinnerBaby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, final View view2, int position, long l) {
                clickedID = babyID.get(position);
                clickedName = babyName.get(position);
                clickedImage = babyImage.get(position);
                clickedAge = babyAge.get(position);
                //Toast.makeText(getContext(), clickedID + " Selected!!", Toast.LENGTH_LONG).show();

                SharedPreferences pref = getActivity().getSharedPreferences("selectedBaby", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("babyId", clickedID);
                editor.putString("babyName", clickedName);
                editor.putString("babyImage", clickedImage);
                editor.putInt("babyAge", clickedAge);
                editor.commit();

                //fetch img from storage
                final ImageView viewBabyImageHome = view.findViewById(R.id.viewBabyImageHome);
                if (clickedID == null) {
                    Picasso.get().load(R.mipmap.ic_launcher).into(viewBabyImageHome);
                } else {
                    if(!clickedImage.equals("none")){
                        Picasso.get().load(clickedImage).into(viewBabyImageHome);
                    }else{
                        Picasso.get().load(R.mipmap.ic_launcher).into(viewBabyImageHome);
                    }
                }

                databaseReference = FirebaseDatabase.getInstance().getReference("User").child(currId);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            role = Objects.requireNonNull(dataSnapshot.child("role").getValue()).toString();

                            if (role.equals("Mother") || role.equals("Father") || role.equals("Caregiver")) {
                                if (clickedID.equals("none")) {
                                    bottleView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(getContext(), "Please Select Baby to Proceed!!", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    sleepView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(getContext(), "Please Select Baby to Proceed!!", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    diaperView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(getContext(), "Please Select Baby to Proceed!!", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    feedView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(getContext(), "Please Select Baby to Proceed!!", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    medView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(getContext(), "Please Select Baby to Proceed!!", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    measureView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(getContext(), "Please Select Baby to Proceed!!", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    vaccineView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(getContext(), "Please Select Baby to Proceed!!", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    calendarView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(getContext(), "Please Select Baby to Proceed!!", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } else {
                                    eventFunction(role);
                                }

                            }else {
                                eventFunction(role);
                            }
                        }else if ((!dataSnapshot.exists()) && (firebaseAuth.getCurrentUser() != null)){
                            startActivity(new Intent(getContext(), UserDetailActivity.class));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void eventFunction(String role){

        String roleUser = role;

        if (!roleUser.equals("Doctor")){
            bottleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), bottleSelect.class);
                    startActivity(intent);
                }
            });


            sleepView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), sleepEvent.class);
                    startActivity(intent);
                }
            });


            diaperView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), diaperEvent.class);
                    startActivity(intent);
                }
            });


            feedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickedAge > 1){
                        Intent intent = new Intent(getActivity(), feedEvent.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getActivity(), "Unlock when baby reach 2 years old", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            medView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), medSelect.class);
                    startActivity(intent);
                }
            });


            measureView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), measurementEvent.class);
                    startActivity(intent);
                }
            });


            vaccineView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), VaccineSta.class);
                    startActivity(intent);
                }
            });


            calendarView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), AppointmentSta.class);
                    startActivity(intent);
                }
            });
        }else {
            bottleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), VideoPost.class);
                    startActivity(intent);
                }
            });


            sleepView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), MedicalPost.class);
                    startActivity(intent);
                }
            });


            diaperView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), BabyActivity.class);
                    startActivity(intent);
                }
            });
        }

    }
}
