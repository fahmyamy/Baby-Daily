package com.health.baby_daily.event;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.health.baby_daily.MainActivity;
import com.health.baby_daily.R;
import com.health.baby_daily.model.Sleep;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class sleepFragmentA extends Fragment {
    private View view;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    private ImageView start, end;
    private Button resetButton, submitButton;
    private TextView duration;

    private long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    private Handler handler;
    private int Second, Minutes, Milliseconds;
    private String totalStartDate, totalEndDate, bId, currId, date;

    private DatabaseReference sleep;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sleep_fragment_a, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        start = view.findViewById(R.id.start);
        end = view.findViewById(R.id.end);
        duration = view.findViewById(R.id.duration);
        resetButton = view.findViewById(R.id.resetButton);
        submitButton = view.findViewById(R.id.submitButton);

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        sleep = db.getReference("Sleep");

        bId = getArguments().getString("bId");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        currId = user.getUid().toString();

        handler = new Handler();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);

                resetButton.setEnabled(false);
                start.setEnabled(false);

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                month = month + 1;
                String bulan, hari, jam, minit;

                if(month <= 9){
                    bulan = "0" + month;
                }else{
                    bulan = String.valueOf(month);
                }

                if(day <= 9){
                    hari = "0" + day;
                }else{
                    hari = String.valueOf(day);
                }

                if(hour <= 9){
                    jam = "0" + hour;
                }else{
                    jam = String.valueOf(hour);
                }

                if(minute <= 9){
                    minit = "0" + minute;
                }else{
                    minit = String.valueOf(minute);
                }

                totalStartDate = hari + "/" + bulan + "/" + year + " " + jam + ":" + minit;

            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeBuff += MillisecondTime;
                handler.removeCallbacks(runnable);

                resetButton.setEnabled(true);

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                String bulan, hari, jam, minit;
                month = month + 1;

                if(month <= 9){
                    bulan = "0" + month;
                }else{
                    bulan = String.valueOf(month);
                }

                if(day <= 9){
                    hari = "0" + day;
                }else{
                    hari = String.valueOf(day);
                }

                if(hour <= 9){
                    jam = "0" + hour;
                }else{
                    jam = String.valueOf(hour);
                }

                if(minute <= 9){
                    minit = "0" + minute;
                }else{
                    minit = String.valueOf(minute);
                }

                totalEndDate = hari + "/" + bulan + "/" + year + " " + jam + ":" + minit;
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MillisecondTime = 0L;
                StartTime = 0L;
                TimeBuff = 0L;
                UpdateTime = 0L;
                Second = 0;
                Minutes = 0;
                Milliseconds = 0;

                duration.setText("00:00:00");
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();

                progressDialog.dismiss();

                Calendar calendar = Calendar.getInstance();
                java.text.DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                date = dateFormat.format(calendar.getTime());

                String id = UUID.randomUUID().toString();
                Sleep sleepValue = new Sleep(id, totalStartDate, totalEndDate, currId, date, bId);
                sleep.child(id).setValue(sleepValue);
                Toast.makeText(getActivity(), "New Sleep Event Added!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Second = (int) (UpdateTime/1000);

            Minutes = Second/60;

            Second = Second % 60;

            Milliseconds = (int) (UpdateTime % 1000);

            duration.setText("" + Minutes + ":"
                    + String.format("%02d", Second) + ":"
                    + String.format("%03d", Milliseconds));

            handler.postDelayed(this, 0);
        }
    };
}
