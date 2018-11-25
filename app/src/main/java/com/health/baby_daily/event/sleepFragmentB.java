package com.health.baby_daily.event;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
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

public class sleepFragmentB extends Fragment {
    private View view;

    private static final String TAG = "sleepFragmentB";

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    private TextView startDate, endDate;
    private Button submitButton;
    private String bId, currId;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;
    private DatePickerDialog.OnDateSetListener dateSetListenerE;
    private TimePickerDialog.OnTimeSetListener timeSetListenerE;

    private String choosenDate, choosenTime, total, totalDisplay, choosenDateE, choosenTimeE, totalE, totalDisplayE, timestamp, date;


    private DatabaseReference sleep;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sleep_fragment_b, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startDate = view.findViewById(R.id.startDate);
        endDate = view.findViewById(R.id.endDate);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        currId = user.getUid().toString();

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        sleep = db.getReference("Sleep");

        bId = getArguments().getString("bId");

        startDate.setText("click here to pick date");
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                        dateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                String newday, newmonth;
                if(day <= 9){
                    newday = "0" + day;
                }else {
                    newday = String.valueOf(day);
                }
                if(month <= 9){
                    newmonth = "0" + month;
                }else{
                    newmonth = String.valueOf(month);
                }

                choosenDate = newday + "/" + newmonth + "/" + year;

                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog timeDialog = new TimePickerDialog(getActivity(),
                        timeSetListener, hour, minute, DateFormat.is24HourFormat(getActivity()));
                timeDialog.show();

            }
        };

        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                int jam = h;
                int minit = m;

                String min = null, hour;

                if(jam <= 9){
                    hour = "0" + jam;
                }else{
                    hour = String.valueOf(jam);
                }
                if(minit <= 9){
                    min = "0" + minit;
                }else{
                    min = String.valueOf(minit);
                }
                choosenTime = hour + ":" + min;

                total = choosenDate + " " + choosenTime;
                totalDisplay = choosenDate + " " + choosenTime;
                startDate.setText(totalDisplay);
            }
        };

        endDate.setText("click here to pick date");
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                        dateSetListenerE,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListenerE = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                String newday, newmonth;
                if(day <= 9){
                    newday = "0" + day;
                }else {
                    newday = String.valueOf(day);
                }
                if(month <= 9){
                    newmonth = "0" + month;
                }else{
                    newmonth = String.valueOf(month);
                }

                choosenDateE = newday + "/" + newmonth + "/" + year;

                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog timeDialog = new TimePickerDialog(getActivity(),
                        timeSetListenerE, hour, minute, DateFormat.is24HourFormat(getActivity()));
                timeDialog.show();

            }
        };

        timeSetListenerE = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                int jam = h;
                int minit = m;

                String min = null, hour;

                if(jam <= 9){
                    hour = "0" + jam;
                }else{
                    hour = String.valueOf(jam);
                }
                if(minit <= 9){
                    min = "0" + minit;
                }else{
                    min = String.valueOf(minit);
                }
                choosenTimeE = hour + ":" + min;

                totalE = choosenDateE + " " + choosenTimeE;
                totalDisplayE = choosenDateE + " " + choosenTimeE;
                endDate.setText(totalDisplayE);
            }
        };


        submitButton = view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(startDate.getText().toString() == "click here to pick date"){
                    Toast.makeText(getActivity(),"Please Pick Date", Toast.LENGTH_SHORT).show();

                    return;
                }

                if(endDate.getText().toString() == "click here to pick date"){
                    Toast.makeText(getActivity(),"Please Pick Date", Toast.LENGTH_SHORT).show();

                    return;
                }

                Calendar calendar = Calendar.getInstance();
                java.text.DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                date = dateFormat.format(calendar.getTime());

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();

                progressDialog.dismiss();

                String id = UUID.randomUUID().toString();
                Sleep sleepValue = new Sleep(id, startDate.getText().toString(), endDate.getText().toString(), currId, date, bId);
                sleep.child(id).setValue(sleepValue);
                Toast.makeText(getActivity(), "New Sleep Event Added!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


    }
}
