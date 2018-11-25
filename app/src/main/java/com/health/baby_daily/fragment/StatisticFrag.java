package com.health.baby_daily.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.health.baby_daily.R;
import com.health.baby_daily.misc.DiaryActivity;
import com.health.baby_daily.statistic.*;

public class StatisticFrag extends Fragment{
    private ImageView bottle, sleep, diaper, feed, medicine, measurement, vaccine, appointment, overall;
    private String bId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.statistic_fragment, null);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottle = view.findViewById(R.id.bottle);
        sleep = view.findViewById(R.id.sleep);
        diaper = view.findViewById(R.id.diaper);
        feed = view.findViewById(R.id.feed);
        medicine = view.findViewById(R.id.medicine);
        measurement = view.findViewById(R.id.measurement);
        vaccine = view.findViewById(R.id.vaccine);
        appointment = view.findViewById(R.id.calendar);
        overall = view.findViewById(R.id.overall);

        SharedPreferences prefBaby = getContext().getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        if (!bId.equals("none")) {
            bottle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), BottleSta.class));
                }
            });

            sleep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), SleepSta.class));
                }
            });

            diaper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), DiaperSta.class));
                }
            });

            feed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), FeedSta.class));
                }
            });

            medicine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), MedDSta.class));
                }
            });

            measurement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), MeasurementSta.class));
                }
            });

            vaccine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), VaccineSta.class));
                }
            });

            appointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), AppointmentSta.class));
                }
            });

            overall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), OverallSta.class));
                }
            });
        }else {
            Toast.makeText(getContext(), "Please Select Baby to Proceed!!", Toast.LENGTH_SHORT).show();
        }
    }
}
