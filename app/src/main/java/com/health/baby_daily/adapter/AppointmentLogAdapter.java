package com.health.baby_daily.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.model.Appointment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class AppointmentLogAdapter extends RecyclerView.Adapter<AppointmentLogAdapter.AppointmentViewHolder> {
    private List<Appointment> appointmentList;
    private OnItemClickListener mListener;
    private DatabaseReference table_baby;
    private int totalday, totalmonth, totalyear;
    private String age = null, agenull = null;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        public TextView circleAge, babyAge, doctorName, dateTime, typeVisit, descNote;

        public AppointmentViewHolder(View itemView, final OnItemClickListener listener){
            super(itemView);

            circleAge = itemView.findViewById(R.id.circleAge);
            babyAge = itemView.findViewById(R.id.babyAge);
            doctorName = itemView.findViewById(R.id.doctorName);
            dateTime = itemView.findViewById(R.id.dateTime);
            descNote = itemView.findViewById(R.id.descNote);
            typeVisit = itemView.findViewById(R.id.typeVisit);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public AppointmentLogAdapter(List<Appointment> appointmentList){
        this.appointmentList = appointmentList;
    }

    @Override
    public AppointmentViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_appointment, parent, false);
        AppointmentViewHolder avh = new AppointmentViewHolder(view, mListener);
        return avh;
    }

    @Override
    public void onBindViewHolder(final AppointmentViewHolder holder, final int position) {
        final Appointment appointment = appointmentList.get(position);

        String babyId = appointment.getBaby_id();
        table_baby = FirebaseDatabase.getInstance().getReference("Baby").child(babyId);
        table_baby.keepSynced(true);
        table_baby.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String dob = dataSnapshot.child("dob").getValue().toString();

                    //age counting
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
                    if((currday == day) || (currday > day)){
                        totalday = currday - day;
                    }else if(currday < day){
                        currday = currday + 31;
                        totalday = currday - day;
                        currmonth = currmonth - 1;
                    }

                    if((currmonth == month) || (currmonth > month)){
                        totalmonth = currmonth - month;
                        totalyear = curryear - year;
                    }else if(currmonth < month){
                        currmonth = currmonth + 12;
                        totalmonth = currmonth - month;
                        curryear = curryear - 1;
                        totalyear = curryear - year;
                    }

                    if (totalyear == 0 && totalmonth != 0 && totalday != 0){
                        age = totalmonth + " Months " + totalday + " Days Old";
                        agenull = String.valueOf(totalmonth);
                    }else if (totalyear == 0 && totalmonth == 0 && totalday != 0){
                        age = totalday + " Days Old";
                        agenull = String.valueOf(totalday);
                    }else if (totalyear != 0 && totalmonth != 0 && totalday != 0) {
                        age = totalyear + " Years " + totalmonth + " Months " + totalday + " Days Old";
                        agenull = String.valueOf(totalyear);
                    }else if (totalyear != 0 && totalmonth == 0 && totalday == 0){
                        age = totalyear + " Years Old";
                        agenull = String.valueOf(totalyear);
                    }
                }
                holder.circleAge.setText(agenull);
                holder.babyAge.setText(age);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        ((GradientDrawable) holder.circleAge.getBackground()).setColor(color);
        holder.circleAge.setTextColor(Color.WHITE);

        holder.doctorName.setText(appointment.getDoctorName());
        holder.typeVisit.setText(appointment.getType());
        holder.descNote.setText(appointment.getDesc());
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }
}