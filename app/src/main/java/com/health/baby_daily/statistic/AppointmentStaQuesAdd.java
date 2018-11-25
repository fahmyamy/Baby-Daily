package com.health.baby_daily.statistic;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.health.baby_daily.R;
import com.health.baby_daily.model.Question;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class AppointmentStaQuesAdd extends AppCompatActivity {

    private static final String TAG = "AppointmentStaQuesAdd";

    private ProgressDialog progressDialog;
    private DatabaseReference question;

    private String bId, total, choosenDate, choosenTime, totalDisplay;

    private TextView datetimepicker;
    private EditText questionText, answerText;
    private Button submitButton;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_sta_ques_add);

        SharedPreferences prefBaby = getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        //assigning store location in firebase
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        question = db.getReference("Question");

        setTitle(" ");

        datetimepicker = findViewById(R.id.datetimepicker);
        questionText = findViewById(R.id.questionText);
        answerText = findViewById(R.id.answerText);
        submitButton = findViewById(R.id.submitButton);

        datetimepicker.setText("click here to pick date");
        datetimepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AppointmentStaQuesAdd.this,
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

                choosenDate = day + "/" + month + "/" + year;

                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog timeDialog = new TimePickerDialog(AppointmentStaQuesAdd.this,
                        timeSetListener, hour, minute, DateFormat.is24HourFormat(AppointmentStaQuesAdd.this));
                timeDialog.show();

            }
        };

        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                int jam = h;
                int minit = m;
                choosenTime = jam + ":" + minit;

                total = choosenDate + " " + choosenTime;
                totalDisplay = choosenDate + " " + choosenTime;
                datetimepicker.setText(totalDisplay);
            }
        };

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(AppointmentStaQuesAdd.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();

                if(datetimepicker.getText().toString() == "click here to pick date"){
                    Toast.makeText(AppointmentStaQuesAdd.this,"Please Pick Date", Toast.LENGTH_SHORT).show();

                    return;
                }

                if(TextUtils.isEmpty(questionText.getText().toString())){
                    Toast.makeText(AppointmentStaQuesAdd.this,"Please Insert Question", Toast.LENGTH_SHORT).show();

                    return;
                }

                String answer = "none";
                if (TextUtils.isEmpty(answerText.getText().toString())){
                    answer = "none";
                }else {
                    answer = answerText.getText().toString();
                }

                String id = UUID.randomUUID().toString();
                Question questionValue = new Question(id, datetimepicker.getText().toString(), questionText.getText().toString(), answer, bId);
                question.child(id).setValue(questionValue);
                Toast.makeText(AppointmentStaQuesAdd.this, "New Question Added!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AppointmentStaQuesAdd.this, AppointmentSta.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}
