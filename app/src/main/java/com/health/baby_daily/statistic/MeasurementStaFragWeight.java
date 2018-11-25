package com.health.baby_daily.statistic;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.model.Baby;
import com.health.baby_daily.model.Measurement;

import java.util.ArrayList;

public class MeasurementStaFragWeight extends Fragment {
    private View view;
    private String bId;
    private LineChart chartA, chartB;

    private DatabaseReference table_baby, table_measure, table_measureB;
    private Query queryBaby, queryBabyB, queryB;

    private LineDataSet negative3sd, negative2sd, zerosd, positive2sd, positive3sd;
    private LineDataSet negative3sdB, negative2sdB, zerosdB, positive2sdB, positive3sdB;
    private LineDataSet negative3sdF, negative2sdF, zerosdF, positive2sdF, positive3sdF;
    private LineDataSet negative3sdBF, negative2sdBF, zerosdBF, positive2sdBF, positive3sdBF;
    private LineDataSet weightData, weightDataB, weightDataF, weightDataBF;
    private int totalday = 0, totalmonth = 0, totalyear = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.measurement_weight_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefBaby = getContext().getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        //born to 2y
        chartA = view.findViewById(R.id.chartA);
        chartA.getDescription().setText("Measure Event");
        chartA.setScaleEnabled(true);
        chartA.getXAxis().setDrawLabels(true);
        chartA.getAxisRight().setDrawLabels(false);
        chartA.getLegend().setEnabled(false);
        chartA.getXAxis().setAxisMinimum(0);
        chartA.getAxisLeft().setAxisMinimum(0);
        chartA.getXAxis().setLabelCount(12, false);
        chartA.getAxisRight().setDrawGridLines(false);
        chartA.animateXY(3000,3000);

        Legend legend = chartA.getLegend();
        legend.setEnabled(true);
        legend.setWordWrapEnabled(true);

        //2-5y
        chartB = view.findViewById(R.id.chartB);
        chartB.getDescription().setText("Measure Event");
        chartB.setScaleEnabled(true);
        chartB.getXAxis().setDrawLabels(true);
        chartB.getAxisRight().setDrawLabels(false);
        chartB.getLegend().setEnabled(false);
        chartB.getXAxis().setAxisMinimum(0);
        chartB.getAxisLeft().setAxisMinimum(0);
        chartB.getXAxis().setLabelCount(12, false);
        chartB.getAxisRight().setDrawGridLines(false);
        chartB.animateXY(3000,3000);

        Legend legendB = chartB.getLegend();
        legendB.setEnabled(true);
        legendB.setWordWrapEnabled(true);
    }

    //Male
    //born to 2y
    public LineDataSet negative3SD(){
        final ArrayList<Entry> negative3sd = new ArrayList<>();
        LineDataSet neg3data;

        Entry entry1 = new Entry(0f, 2.5f);
        Entry entry2 = new Entry(1f, 3.4f);
        Entry entry3 = new Entry(2f, 4.4f);
        Entry entry4 = new Entry(3f, 5.1f);
        Entry entry5 = new Entry(4f, 5.6f);
        Entry entry6 = new Entry(5f, 6.1f);
        Entry entry7 = new Entry(6f, 6.4f);
        Entry entry8 = new Entry(7f, 6.7f);
        Entry entry9 = new Entry(8f, 7.0f);
        Entry entry10 = new Entry(9f, 7.2f);
        Entry entry11 = new Entry(10f, 7.5f);
        Entry entry12 = new Entry(11f, 7.7f);
        Entry entry13 = new Entry(12f, 7.8f);
        Entry entry14 = new Entry(13f, 8.0f);
        Entry entry15 = new Entry(14f, 8.2f);
        Entry entry16 = new Entry(15f, 8.4f);
        Entry entry17 = new Entry(16f, 8.5f);
        Entry entry18 = new Entry(17f, 8.7f);
        Entry entry19 = new Entry(18f, 8.9f);
        Entry entry20 = new Entry(19f, 9.0f);
        Entry entry21 = new Entry(20f, 9.2f);
        Entry entry22 = new Entry(21f, 9.3f);
        Entry entry23 = new Entry(22f, 9.5f);
        Entry entry24 = new Entry(23f, 9.7f);
        Entry entry25 = new Entry(24f, 9.8f);

        negative3sd.add(entry1);
        negative3sd.add(entry2);
        negative3sd.add(entry3);
        negative3sd.add(entry4);
        negative3sd.add(entry5);
        negative3sd.add(entry6);
        negative3sd.add(entry7);
        negative3sd.add(entry8);
        negative3sd.add(entry9);
        negative3sd.add(entry10);
        negative3sd.add(entry11);
        negative3sd.add(entry12);
        negative3sd.add(entry13);
        negative3sd.add(entry14);
        negative3sd.add(entry15);
        negative3sd.add(entry16);
        negative3sd.add(entry17);
        negative3sd.add(entry18);
        negative3sd.add(entry19);
        negative3sd.add(entry20);
        negative3sd.add(entry21);
        negative3sd.add(entry22);
        negative3sd.add(entry23);
        negative3sd.add(entry24);
        negative3sd.add(entry25);

        neg3data = new LineDataSet(negative3sd, "-3SD");
        neg3data.setDrawCircles(false);
        neg3data.setDrawValues(false);
        neg3data.enableDashedLine(10, 10, 2);
        neg3data.setColor(Color.RED);

        return neg3data;
    }

    public LineDataSet negative2SD(){
        final ArrayList<Entry> negative2sd = new ArrayList<>();
        LineDataSet neg2data;

        Entry entry1 = new Entry(0f, 2.9f);
        Entry entry2 = new Entry(1f, 3.9f);
        Entry entry3 = new Entry(2f, 4.9f);
        Entry entry4 = new Entry(3f, 5.6f);
        Entry entry5 = new Entry(4f, 6.2f);
        Entry entry6 = new Entry(5f, 6.7f);
        Entry entry7 = new Entry(6f, 7.1f);
        Entry entry8 = new Entry(7f, 7.4f);
        Entry entry9 = new Entry(8f, 7.7f);
        Entry entry10 = new Entry(9f, 7.9f);
        Entry entry11 = new Entry(10f, 8.2f);
        Entry entry12 = new Entry(11f, 8.4f);
        Entry entry13 = new Entry(12f, 8.6f);
        Entry entry14 = new Entry(13f, 8.8f);
        Entry entry15 = new Entry(14f, 9.0f);
        Entry entry16 = new Entry(15f, 9.2f);
        Entry entry17 = new Entry(16f, 9.4f);
        Entry entry18 = new Entry(17f, 9.6f);
        Entry entry19 = new Entry(18f, 9.7f);
        Entry entry20 = new Entry(19f, 9.9f);
        Entry entry21 = new Entry(20f, 10.1f);
        Entry entry22 = new Entry(21f, 10.3f);
        Entry entry23 = new Entry(22f, 10.5f);
        Entry entry24 = new Entry(23f, 10.6f);
        Entry entry25 = new Entry(24f, 10.8f);

        negative2sd.add(entry1);
        negative2sd.add(entry2);
        negative2sd.add(entry3);
        negative2sd.add(entry4);
        negative2sd.add(entry5);
        negative2sd.add(entry6);
        negative2sd.add(entry7);
        negative2sd.add(entry8);
        negative2sd.add(entry9);
        negative2sd.add(entry10);
        negative2sd.add(entry11);
        negative2sd.add(entry12);
        negative2sd.add(entry13);
        negative2sd.add(entry14);
        negative2sd.add(entry15);
        negative2sd.add(entry16);
        negative2sd.add(entry17);
        negative2sd.add(entry18);
        negative2sd.add(entry19);
        negative2sd.add(entry20);
        negative2sd.add(entry21);
        negative2sd.add(entry22);
        negative2sd.add(entry23);
        negative2sd.add(entry24);
        negative2sd.add(entry25);

        neg2data = new LineDataSet(negative2sd, "-2SD");
        neg2data.setDrawCircles(false);
        neg2data.setDrawValues(false);
        neg2data.enableDashedLine(10, 10, 2);
        neg2data.setColor(Color.YELLOW);

        return neg2data;
    }

    public LineDataSet zeroSD(){
        final ArrayList<Entry> zerosd = new ArrayList<>();
        LineDataSet zerodata;

        Entry entry1 = new Entry(0f, 3.3f);
        Entry entry2 = new Entry(1f, 4.5f);
        Entry entry3 = new Entry(2f, 5.6f);
        Entry entry4 = new Entry(3f, 6.4f);
        Entry entry5 = new Entry(4f, 7f);
        Entry entry6 = new Entry(5f, 7.5f);
        Entry entry7 = new Entry(6f, 7.9f);
        Entry entry8 = new Entry(7f, 8.3f);
        Entry entry9 = new Entry(8f, 8.6f);
        Entry entry10 = new Entry(9f, 8.9f);
        Entry entry11 = new Entry(10f, 9.2f);
        Entry entry12 = new Entry(11f, 9.4f);
        Entry entry13 = new Entry(12f, 9.6f);
        Entry entry14 = new Entry(13f, 9.9f);
        Entry entry15 = new Entry(14f, 10.1f);
        Entry entry16 = new Entry(15f, 10.3f);
        Entry entry17 = new Entry(16f, 10.5f);
        Entry entry18 = new Entry(17f, 10.7f);
        Entry entry19 = new Entry(18f, 10.9f);
        Entry entry20 = new Entry(19f, 11.1f);
        Entry entry21 = new Entry(20f, 11.3f);
        Entry entry22 = new Entry(21f, 11.5f);
        Entry entry23 = new Entry(22f, 11.8f);
        Entry entry24 = new Entry(23f, 12f);
        Entry entry25 = new Entry(24f, 12.2f);

        zerosd.add(entry1);
        zerosd.add(entry2);
        zerosd.add(entry3);
        zerosd.add(entry4);
        zerosd.add(entry5);
        zerosd.add(entry6);
        zerosd.add(entry7);
        zerosd.add(entry8);
        zerosd.add(entry9);
        zerosd.add(entry10);
        zerosd.add(entry11);
        zerosd.add(entry12);
        zerosd.add(entry13);
        zerosd.add(entry14);
        zerosd.add(entry15);
        zerosd.add(entry16);
        zerosd.add(entry17);
        zerosd.add(entry18);
        zerosd.add(entry19);
        zerosd.add(entry20);
        zerosd.add(entry21);
        zerosd.add(entry22);
        zerosd.add(entry23);
        zerosd.add(entry24);
        zerosd.add(entry25);

        zerodata = new LineDataSet(zerosd, "0SD");
        zerodata.setDrawCircles(false);
        zerodata.setDrawValues(false);
        zerodata.setColor(Color.BLACK);

        return zerodata;
    }

    public LineDataSet positive2SD(){
        final ArrayList<Entry> positive2sd = new ArrayList<>();
        LineDataSet positive2data;

        Entry entry1 = new Entry(0f, 3.9f);
        Entry entry2 = new Entry(1f, 5.1f);
        Entry entry3 = new Entry(2f, 6.3f);
        Entry entry4 = new Entry(3f, 7.2f);
        Entry entry5 = new Entry(4f, 7.9f);
        Entry entry6 = new Entry(5f, 8.4f);
        Entry entry7 = new Entry(6f, 8.9f);
        Entry entry8 = new Entry(7f, 9.3f);
        Entry entry9 = new Entry(8f, 9.6f);
        Entry entry10 = new Entry(9f, 10f);
        Entry entry11 = new Entry(10f, 10.3f);
        Entry entry12 = new Entry(11f, 10.5f);
        Entry entry13 = new Entry(12f, 10.8f);
        Entry entry14 = new Entry(13f, 11.1f);
        Entry entry15 = new Entry(14f, 11.3f);
        Entry entry16 = new Entry(15f, 11.6f);
        Entry entry17 = new Entry(16f, 11.8f);
        Entry entry18 = new Entry(17f, 12f);
        Entry entry19 = new Entry(18f, 12.3f);
        Entry entry20 = new Entry(19f, 12.5f);
        Entry entry21 = new Entry(20f, 12.7f);
        Entry entry22 = new Entry(21f, 13f);
        Entry entry23 = new Entry(22f, 13.2f);
        Entry entry24 = new Entry(23f, 13.4f);
        Entry entry25 = new Entry(24f, 13.7f);

        positive2sd.add(entry1);
        positive2sd.add(entry2);
        positive2sd.add(entry3);
        positive2sd.add(entry4);
        positive2sd.add(entry5);
        positive2sd.add(entry6);
        positive2sd.add(entry7);
        positive2sd.add(entry8);
        positive2sd.add(entry9);
        positive2sd.add(entry10);
        positive2sd.add(entry11);
        positive2sd.add(entry12);
        positive2sd.add(entry13);
        positive2sd.add(entry14);
        positive2sd.add(entry15);
        positive2sd.add(entry16);
        positive2sd.add(entry17);
        positive2sd.add(entry18);
        positive2sd.add(entry19);
        positive2sd.add(entry20);
        positive2sd.add(entry21);
        positive2sd.add(entry22);
        positive2sd.add(entry23);
        positive2sd.add(entry24);
        positive2sd.add(entry25);

        positive2data = new LineDataSet(positive2sd, "+2SD");
        positive2data.setDrawCircles(false);
        positive2data.setDrawValues(false);
        positive2data.enableDashedLine(10, 10, 2);
        positive2data.setColor(Color.YELLOW);

        return positive2data;
    }

    public LineDataSet positive3SD(){
        final ArrayList<Entry> positive3sd = new ArrayList<>();
        LineDataSet positive3data;

        Entry entry1 = new Entry(0f, 4.3f);
        Entry entry2 = new Entry(1f, 5.7f);
        Entry entry3 = new Entry(2f, 7f);
        Entry entry4 = new Entry(3f, 7.9f);
        Entry entry5 = new Entry(4f, 8.6f);
        Entry entry6 = new Entry(5f, 9.2f);
        Entry entry7 = new Entry(6f, 9.7f);
        Entry entry8 = new Entry(7f, 10.2f);
        Entry entry9 = new Entry(8f, 10.5f);
        Entry entry10 = new Entry(9f, 10.9f);
        Entry entry11 = new Entry(10f, 11.2f);
        Entry entry12 = new Entry(11f, 11.5f);
        Entry entry13 = new Entry(12f, 11.8f);
        Entry entry14 = new Entry(13f, 12.1f);
        Entry entry15 = new Entry(14f, 12.4f);
        Entry entry16 = new Entry(15f, 12.7f);
        Entry entry17 = new Entry(16f, 12.9f);
        Entry entry18 = new Entry(17f, 13.2f);
        Entry entry19 = new Entry(18f, 13.5f);
        Entry entry20 = new Entry(19f, 13.7f);
        Entry entry21 = new Entry(20f, 14f);
        Entry entry22 = new Entry(21f, 14.3f);
        Entry entry23 = new Entry(22f, 14.5f);
        Entry entry24 = new Entry(23f, 14.8f);
        Entry entry25 = new Entry(24f, 15.1f);

        positive3sd.add(entry1);
        positive3sd.add(entry2);
        positive3sd.add(entry3);
        positive3sd.add(entry4);
        positive3sd.add(entry5);
        positive3sd.add(entry6);
        positive3sd.add(entry7);
        positive3sd.add(entry8);
        positive3sd.add(entry9);
        positive3sd.add(entry10);
        positive3sd.add(entry11);
        positive3sd.add(entry12);
        positive3sd.add(entry13);
        positive3sd.add(entry14);
        positive3sd.add(entry15);
        positive3sd.add(entry16);
        positive3sd.add(entry17);
        positive3sd.add(entry18);
        positive3sd.add(entry19);
        positive3sd.add(entry20);
        positive3sd.add(entry21);
        positive3sd.add(entry22);
        positive3sd.add(entry23);
        positive3sd.add(entry24);
        positive3sd.add(entry25);

        positive3data = new LineDataSet(positive3sd, "+3SD");
        positive3data.setDrawCircles(false);
        positive3data.setDrawValues(false);
        positive3data.enableDashedLine(10, 10, 2);
        positive3data.setColor(Color.RED);

        return positive3data;
    }

    //2-5y
    public LineDataSet negative3SDB(){
        final ArrayList<Entry> negative3sd = new ArrayList<>();
        LineDataSet neg3data;

        Entry entry1 = new Entry(0f, 9.8f);
        Entry entry2 = new Entry(1f, 10.1f);
        Entry entry3 = new Entry(2f, 10.4f);
        Entry entry4 = new Entry(3f, 10.7f);
        Entry entry5 = new Entry(4f, 10.9f);
        Entry entry6 = new Entry(5f, 11.2f);
        Entry entry7 = new Entry(6f, 11.4f);
        Entry entry8 = new Entry(7f, 11.7f);
        Entry entry9 = new Entry(8f, 11.9f);
        Entry entry10 = new Entry(9f, 12.2f);
        Entry entry11 = new Entry(10f, 12.4f);
        Entry entry12 = new Entry(11f, 12.7f);
        Entry entry13 = new Entry(12f, 12.9f);
        Entry entry14 = new Entry(13f, 13.1f);
        Entry entry15 = new Entry(14f, 13.4f);
        Entry entry16 = new Entry(15f, 13.6f);
        Entry entry17 = new Entry(16f, 13.8f);
        Entry entry18 = new Entry(17f, 14.1f);
        Entry entry19 = new Entry(18f, 14.3f);

        negative3sd.add(entry1);
        negative3sd.add(entry2);
        negative3sd.add(entry3);
        negative3sd.add(entry4);
        negative3sd.add(entry5);
        negative3sd.add(entry6);
        negative3sd.add(entry7);
        negative3sd.add(entry8);
        negative3sd.add(entry9);
        negative3sd.add(entry10);
        negative3sd.add(entry11);
        negative3sd.add(entry12);
        negative3sd.add(entry13);
        negative3sd.add(entry14);
        negative3sd.add(entry15);
        negative3sd.add(entry16);
        negative3sd.add(entry17);
        negative3sd.add(entry18);
        negative3sd.add(entry19);

        neg3data = new LineDataSet(negative3sd, "-3SD");
        neg3data.setDrawCircles(false);
        neg3data.setDrawValues(false);
        neg3data.enableDashedLine(10, 10, 2);
        neg3data.setColor(Color.RED);

        return neg3data;
    }

    public LineDataSet negative2SDB(){
        final ArrayList<Entry> negative2sd = new ArrayList<>();
        LineDataSet neg2data;

        Entry entry1 = new Entry(0f, 10.8f);
        Entry entry2 = new Entry(1f, 11.1f);
        Entry entry3 = new Entry(2f, 11.5f);
        Entry entry4 = new Entry(3f, 11.8f);
        Entry entry5 = new Entry(4f, 12.1f);
        Entry entry6 = new Entry(5f, 12.4f);
        Entry entry7 = new Entry(6f, 12.7f);
        Entry entry8 = new Entry(7f, 12.9f);
        Entry entry9 = new Entry(8f, 13.2f);
        Entry entry10 = new Entry(9f, 13.5f);
        Entry entry11 = new Entry(10f, 13.8f);
        Entry entry12 = new Entry(11f, 14.1f);
        Entry entry13 = new Entry(12f, 14.3f);
        Entry entry14 = new Entry(13f, 14.6f);
        Entry entry15 = new Entry(14f, 14.9f);
        Entry entry16 = new Entry(15f, 15.2f);
        Entry entry17 = new Entry(16f, 15.4f);
        Entry entry18 = new Entry(17f, 15.7f);
        Entry entry19 = new Entry(18f, 16f);

        negative2sd.add(entry1);
        negative2sd.add(entry2);
        negative2sd.add(entry3);
        negative2sd.add(entry4);
        negative2sd.add(entry5);
        negative2sd.add(entry6);
        negative2sd.add(entry7);
        negative2sd.add(entry8);
        negative2sd.add(entry9);
        negative2sd.add(entry10);
        negative2sd.add(entry11);
        negative2sd.add(entry12);
        negative2sd.add(entry13);
        negative2sd.add(entry14);
        negative2sd.add(entry15);
        negative2sd.add(entry16);
        negative2sd.add(entry17);
        negative2sd.add(entry18);
        negative2sd.add(entry19);

        neg2data = new LineDataSet(negative2sd, "-2SD");
        neg2data.setDrawCircles(false);
        neg2data.setDrawValues(false);
        neg2data.enableDashedLine(10, 10, 2);
        neg2data.setColor(Color.YELLOW);

        return neg2data;
    }

    public LineDataSet zeroSDB(){
        final ArrayList<Entry> zerosd = new ArrayList<>();
        LineDataSet zerodata;

        Entry entry1 = new Entry(0f, 12.2f);
        Entry entry2 = new Entry(1f, 12.5f);
        Entry entry3 = new Entry(2f, 12.9f);
        Entry entry4 = new Entry(3f, 13.3f);
        Entry entry5 = new Entry(4f, 13.7f);
        Entry entry6 = new Entry(5f, 14);
        Entry entry7 = new Entry(6f, 14.3f);
        Entry entry8 = new Entry(7f, 14.7f);
        Entry entry9 = new Entry(8f, 15f);
        Entry entry10 = new Entry(9f, 15.3f);
        Entry entry11 = new Entry(10f, 15.7f);
        Entry entry12 = new Entry(11f, 16f);
        Entry entry13 = new Entry(12f, 16.3f);
        Entry entry14 = new Entry(13f, 16.7f);
        Entry entry15 = new Entry(14f, 17f);
        Entry entry16 = new Entry(15f, 17.3f);
        Entry entry17 = new Entry(16f, 17.7f);
        Entry entry18 = new Entry(17f, 18f);
        Entry entry19 = new Entry(18f, 18.3f);

        zerosd.add(entry1);
        zerosd.add(entry2);
        zerosd.add(entry3);
        zerosd.add(entry4);
        zerosd.add(entry5);
        zerosd.add(entry6);
        zerosd.add(entry7);
        zerosd.add(entry8);
        zerosd.add(entry9);
        zerosd.add(entry10);
        zerosd.add(entry11);
        zerosd.add(entry12);
        zerosd.add(entry13);
        zerosd.add(entry14);
        zerosd.add(entry15);
        zerosd.add(entry16);
        zerosd.add(entry17);
        zerosd.add(entry18);
        zerosd.add(entry19);

        zerodata = new LineDataSet(zerosd, "0SD");
        zerodata.setDrawCircles(false);
        zerodata.setDrawValues(false);
        zerodata.setColor(Color.BLACK);

        return zerodata;
    }

    public LineDataSet positive2SDB(){
        final ArrayList<Entry> positive2sd = new ArrayList<>();
        LineDataSet positive2data;

        Entry entry1 = new Entry(0f, 13.7f);
        Entry entry2 = new Entry(1f, 14.1f);
        Entry entry3 = new Entry(2f, 14.6f);
        Entry entry4 = new Entry(3f, 15f);
        Entry entry5 = new Entry(4f, 15.5f);
        Entry entry6 = new Entry(5f, 15.9f);
        Entry entry7 = new Entry(6f, 16.3f);
        Entry entry8 = new Entry(7f, 16.7f);
        Entry entry9 = new Entry(8f, 17.1f);
        Entry entry10 = new Entry(9f, 17.5f);
        Entry entry11 = new Entry(10f, 17.9f);
        Entry entry12 = new Entry(11f, 18.3f);
        Entry entry13 = new Entry(12f, 18.7f);
        Entry entry14 = new Entry(13f, 19.1f);
        Entry entry15 = new Entry(14f, 19.5f);
        Entry entry16 = new Entry(15f, 19.9f);
        Entry entry17 = new Entry(16f, 20.3f);
        Entry entry18 = new Entry(17f, 20.7f);
        Entry entry19 = new Entry(18f, 21.1f);

        positive2sd.add(entry1);
        positive2sd.add(entry2);
        positive2sd.add(entry3);
        positive2sd.add(entry4);
        positive2sd.add(entry5);
        positive2sd.add(entry6);
        positive2sd.add(entry7);
        positive2sd.add(entry8);
        positive2sd.add(entry9);
        positive2sd.add(entry10);
        positive2sd.add(entry11);
        positive2sd.add(entry12);
        positive2sd.add(entry13);
        positive2sd.add(entry14);
        positive2sd.add(entry15);
        positive2sd.add(entry16);
        positive2sd.add(entry17);
        positive2sd.add(entry18);
        positive2sd.add(entry19);

        positive2data = new LineDataSet(positive2sd, "+2SD");
        positive2data.setDrawCircles(false);
        positive2data.setDrawValues(false);
        positive2data.enableDashedLine(10, 10, 2);
        positive2data.setColor(Color.YELLOW);

        return positive2data;
    }

    public LineDataSet positive3SDB(){
        final ArrayList<Entry> positive3sd = new ArrayList<>();
        LineDataSet positive3data;

        Entry entry1 = new Entry(0f, 15.1f);
        Entry entry2 = new Entry(1f, 15.6f);
        Entry entry3 = new Entry(2f, 16.1f);
        Entry entry4 = new Entry(3f, 16.6f);
        Entry entry5 = new Entry(4f, 17.1f);
        Entry entry6 = new Entry(5f, 17.6f);
        Entry entry7 = new Entry(6f, 18f);
        Entry entry8 = new Entry(7f, 18.5f);
        Entry entry9 = new Entry(8f, 19f);
        Entry entry10 = new Entry(9f, 19.4f);
        Entry entry11 = new Entry(10f, 19.9f);
        Entry entry12 = new Entry(11f, 20.4f);
        Entry entry13 = new Entry(12f, 20.9f);
        Entry entry14 = new Entry(13f, 21.3f);
        Entry entry15 = new Entry(14f, 21.8f);
        Entry entry16 = new Entry(15f, 22.3f);
        Entry entry17 = new Entry(16f, 22.8f);
        Entry entry18 = new Entry(17f, 23.3f);
        Entry entry19 = new Entry(18f, 23.8f);

        positive3sd.add(entry1);
        positive3sd.add(entry2);
        positive3sd.add(entry3);
        positive3sd.add(entry4);
        positive3sd.add(entry5);
        positive3sd.add(entry6);
        positive3sd.add(entry7);
        positive3sd.add(entry8);
        positive3sd.add(entry9);
        positive3sd.add(entry10);
        positive3sd.add(entry11);
        positive3sd.add(entry12);
        positive3sd.add(entry13);
        positive3sd.add(entry14);
        positive3sd.add(entry15);
        positive3sd.add(entry16);
        positive3sd.add(entry17);
        positive3sd.add(entry18);
        positive3sd.add(entry19);

        positive3data = new LineDataSet(positive3sd, "+3SD");
        positive3data.setDrawCircles(false);
        positive3data.setDrawValues(false);
        positive3data.enableDashedLine(10, 10, 2);
        positive3data.setColor(Color.RED);

        return positive3data;
    }

    //Female
    //born to 2y
    public LineDataSet negative3SDF(){
        final ArrayList<Entry> negative3sd = new ArrayList<>();
        LineDataSet neg3data;

        Entry entry1 = new Entry(0f, 2.4f);
        Entry entry2 = new Entry(1f, 3.2f);
        Entry entry3 = new Entry(2f, 4.0f);
        Entry entry4 = new Entry(3f, 4.6f);
        Entry entry5 = new Entry(4f, 5.1f);
        Entry entry6 = new Entry(5f, 5.5f);
        Entry entry7 = new Entry(6f, 5.8f);
        Entry entry8 = new Entry(7f, 6.1f);
        Entry entry9 = new Entry(8f, 6.3f);
        Entry entry10 = new Entry(9f, 6.6f);
        Entry entry11 = new Entry(10f, 6.8f);
        Entry entry12 = new Entry(11f, 7.0f);
        Entry entry13 = new Entry(12f, 7.1f);
        Entry entry14 = new Entry(13f, 7.3f);
        Entry entry15 = new Entry(14f, 7.5f);
        Entry entry16 = new Entry(15f, 7.7f);
        Entry entry17 = new Entry(16f, 7.8f);
        Entry entry18 = new Entry(17f, 8.0f);
        Entry entry19 = new Entry(18f, 8.2f);
        Entry entry20 = new Entry(19f, 8.3f);
        Entry entry21 = new Entry(20f, 8.5f);
        Entry entry22 = new Entry(21f, 8.7f);
        Entry entry23 = new Entry(22f, 8.8f);
        Entry entry24 = new Entry(23f, 9.0f);
        Entry entry25 = new Entry(24f, 9.2f);

        negative3sd.add(entry1);
        negative3sd.add(entry2);
        negative3sd.add(entry3);
        negative3sd.add(entry4);
        negative3sd.add(entry5);
        negative3sd.add(entry6);
        negative3sd.add(entry7);
        negative3sd.add(entry8);
        negative3sd.add(entry9);
        negative3sd.add(entry10);
        negative3sd.add(entry11);
        negative3sd.add(entry12);
        negative3sd.add(entry13);
        negative3sd.add(entry14);
        negative3sd.add(entry15);
        negative3sd.add(entry16);
        negative3sd.add(entry17);
        negative3sd.add(entry18);
        negative3sd.add(entry19);
        negative3sd.add(entry20);
        negative3sd.add(entry21);
        negative3sd.add(entry22);
        negative3sd.add(entry23);
        negative3sd.add(entry24);
        negative3sd.add(entry25);

        neg3data = new LineDataSet(negative3sd, "-3SD");
        neg3data.setDrawCircles(false);
        neg3data.setDrawValues(false);
        neg3data.enableDashedLine(10, 10, 2);
        neg3data.setColor(Color.RED);

        return neg3data;
    }

    public LineDataSet negative2SDF(){
        final ArrayList<Entry> negative2sd = new ArrayList<>();
        LineDataSet neg2data;

        Entry entry1 = new Entry(0f, 2.8f);
        Entry entry2 = new Entry(1f, 3.6f);
        Entry entry3 = new Entry(2f, 4.5f);
        Entry entry4 = new Entry(3f, 5.1f);
        Entry entry5 = new Entry(4f, 5.6f);
        Entry entry6 = new Entry(5f, 6.1f);
        Entry entry7 = new Entry(6f, 6.4f);
        Entry entry8 = new Entry(7f, 6.7f);
        Entry entry9 = new Entry(8f, 7.0f);
        Entry entry10 = new Entry(9f, 7.3f);
        Entry entry11 = new Entry(10f, 7.5f);
        Entry entry12 = new Entry(11f, 7.7f);
        Entry entry13 = new Entry(12f, 7.9f);
        Entry entry14 = new Entry(13f, 8.1f);
        Entry entry15 = new Entry(14f, 8.3f);
        Entry entry16 = new Entry(15f, 8.5f);
        Entry entry17 = new Entry(16f, 8.7f);
        Entry entry18 = new Entry(17f, 8.8f);
        Entry entry19 = new Entry(18f, 9.0f);
        Entry entry20 = new Entry(19f, 9.2f);
        Entry entry21 = new Entry(20f, 9.4f);
        Entry entry22 = new Entry(21f, 9.6f);
        Entry entry23 = new Entry(22f, 9.8f);
        Entry entry24 = new Entry(23f, 9.9f);
        Entry entry25 = new Entry(24f, 10.1f);

        negative2sd.add(entry1);
        negative2sd.add(entry2);
        negative2sd.add(entry3);
        negative2sd.add(entry4);
        negative2sd.add(entry5);
        negative2sd.add(entry6);
        negative2sd.add(entry7);
        negative2sd.add(entry8);
        negative2sd.add(entry9);
        negative2sd.add(entry10);
        negative2sd.add(entry11);
        negative2sd.add(entry12);
        negative2sd.add(entry13);
        negative2sd.add(entry14);
        negative2sd.add(entry15);
        negative2sd.add(entry16);
        negative2sd.add(entry17);
        negative2sd.add(entry18);
        negative2sd.add(entry19);
        negative2sd.add(entry20);
        negative2sd.add(entry21);
        negative2sd.add(entry22);
        negative2sd.add(entry23);
        negative2sd.add(entry24);
        negative2sd.add(entry25);

        neg2data = new LineDataSet(negative2sd, "-2SD");
        neg2data.setDrawCircles(false);
        neg2data.setDrawValues(false);
        neg2data.enableDashedLine(10, 10, 2);
        neg2data.setColor(Color.YELLOW);

        return neg2data;
    }

    public LineDataSet zeroSDF(){
        final ArrayList<Entry> zerosd = new ArrayList<>();
        LineDataSet zerodata;

        Entry entry1 = new Entry(0f, 3.2f);
        Entry entry2 = new Entry(1f, 4.2f);
        Entry entry3 = new Entry(2f, 5.1f);
        Entry entry4 = new Entry(3f, 5.8f);
        Entry entry5 = new Entry(4f, 6.4f);
        Entry entry6 = new Entry(5f, 6.9f);
        Entry entry7 = new Entry(6f, 7.3f);
        Entry entry8 = new Entry(7f, 7.6f);
        Entry entry9 = new Entry(8f, 7.9f);
        Entry entry10 = new Entry(9f, 8.2f);
        Entry entry11 = new Entry(10f, 8.5f);
        Entry entry12 = new Entry(11f, 8.7f);
        Entry entry13 = new Entry(12f, 8.9f);
        Entry entry14 = new Entry(13f, 9.2f);
        Entry entry15 = new Entry(14f, 9.4f);
        Entry entry16 = new Entry(15f, 9.6f);
        Entry entry17 = new Entry(16f, 9.8f);
        Entry entry18 = new Entry(17f, 10f);
        Entry entry19 = new Entry(18f, 10.2f);
        Entry entry20 = new Entry(19f, 10.4f);
        Entry entry21 = new Entry(20f, 10.6f);
        Entry entry22 = new Entry(21f, 10.9f);
        Entry entry23 = new Entry(22f, 11.1f);
        Entry entry24 = new Entry(23f, 11.3f);
        Entry entry25 = new Entry(24f, 11.5f);

        zerosd.add(entry1);
        zerosd.add(entry2);
        zerosd.add(entry3);
        zerosd.add(entry4);
        zerosd.add(entry5);
        zerosd.add(entry6);
        zerosd.add(entry7);
        zerosd.add(entry8);
        zerosd.add(entry9);
        zerosd.add(entry10);
        zerosd.add(entry11);
        zerosd.add(entry12);
        zerosd.add(entry13);
        zerosd.add(entry14);
        zerosd.add(entry15);
        zerosd.add(entry16);
        zerosd.add(entry17);
        zerosd.add(entry18);
        zerosd.add(entry19);
        zerosd.add(entry20);
        zerosd.add(entry21);
        zerosd.add(entry22);
        zerosd.add(entry23);
        zerosd.add(entry24);
        zerosd.add(entry25);

        zerodata = new LineDataSet(zerosd, "0SD");
        zerodata.setDrawCircles(false);
        zerodata.setDrawValues(false);
        zerodata.setColor(Color.BLACK);

        return zerodata;
    }

    public LineDataSet positive2SDF(){
        final ArrayList<Entry> positive2sd = new ArrayList<>();
        LineDataSet positive2data;

        Entry entry1 = new Entry(0f, 3.7f);
        Entry entry2 = new Entry(1f, 4.8f);
        Entry entry3 = new Entry(2f, 5.9f);
        Entry entry4 = new Entry(3f, 6.7f);
        Entry entry5 = new Entry(4f, 7.3f);
        Entry entry6 = new Entry(5f, 7.8f);
        Entry entry7 = new Entry(6f, 8.3f);
        Entry entry8 = new Entry(7f, 8.7f);
        Entry entry9 = new Entry(8f, 9f);
        Entry entry10 = new Entry(9f, 9.3f);
        Entry entry11 = new Entry(10f, 9.6f);
        Entry entry12 = new Entry(11f, 9.9f);
        Entry entry13 = new Entry(12f, 10.2f);
        Entry entry14 = new Entry(13f, 10.4f);
        Entry entry15 = new Entry(14f, 10.7f);
        Entry entry16 = new Entry(15f, 10.9f);
        Entry entry17 = new Entry(16f, 11.2f);
        Entry entry18 = new Entry(17f, 11.4f);
        Entry entry19 = new Entry(18f, 11.6f);
        Entry entry20 = new Entry(19f, 11.9f);
        Entry entry21 = new Entry(20f, 12.1f);
        Entry entry22 = new Entry(21f, 12.4f);
        Entry entry23 = new Entry(22f, 12.6f);
        Entry entry24 = new Entry(23f, 12.8f);
        Entry entry25 = new Entry(24f, 13.1f);

        positive2sd.add(entry1);
        positive2sd.add(entry2);
        positive2sd.add(entry3);
        positive2sd.add(entry4);
        positive2sd.add(entry5);
        positive2sd.add(entry6);
        positive2sd.add(entry7);
        positive2sd.add(entry8);
        positive2sd.add(entry9);
        positive2sd.add(entry10);
        positive2sd.add(entry11);
        positive2sd.add(entry12);
        positive2sd.add(entry13);
        positive2sd.add(entry14);
        positive2sd.add(entry15);
        positive2sd.add(entry16);
        positive2sd.add(entry17);
        positive2sd.add(entry18);
        positive2sd.add(entry19);
        positive2sd.add(entry20);
        positive2sd.add(entry21);
        positive2sd.add(entry22);
        positive2sd.add(entry23);
        positive2sd.add(entry24);
        positive2sd.add(entry25);

        positive2data = new LineDataSet(positive2sd, "+2SD");
        positive2data.setDrawCircles(false);
        positive2data.setDrawValues(false);
        positive2data.enableDashedLine(10, 10, 2);
        positive2data.setColor(Color.YELLOW);

        return positive2data;
    }

    public LineDataSet positive3SDF(){
        final ArrayList<Entry> positive3sd = new ArrayList<>();
        LineDataSet positive3data;

        Entry entry1 = new Entry(0f, 4.2f);
        Entry entry2 = new Entry(1f, 5.4f);
        Entry entry3 = new Entry(2f, 6.5f);
        Entry entry4 = new Entry(3f, 7.4f);
        Entry entry5 = new Entry(4f, 8.1f);
        Entry entry6 = new Entry(5f, 8.7f);
        Entry entry7 = new Entry(6f, 9.2f);
        Entry entry8 = new Entry(7f, 9.6f);
        Entry entry9 = new Entry(8f, 10f);
        Entry entry10 = new Entry(9f, 10.4f);
        Entry entry11 = new Entry(10f, 10.7f);
        Entry entry12 = new Entry(11f, 11f);
        Entry entry13 = new Entry(12f, 11.3f);
        Entry entry14 = new Entry(13f, 11.6f);
        Entry entry15 = new Entry(14f, 11.9f);
        Entry entry16 = new Entry(15f, 12.2f);
        Entry entry17 = new Entry(16f, 12.5f);
        Entry entry18 = new Entry(17f, 12.7f);
        Entry entry19 = new Entry(18f, 13.0f);
        Entry entry20 = new Entry(19f, 13.3f);
        Entry entry21 = new Entry(20f, 13.5f);
        Entry entry22 = new Entry(21f, 13.8f);
        Entry entry23 = new Entry(22f, 14.1f);
        Entry entry24 = new Entry(23f, 14.3f);
        Entry entry25 = new Entry(24f, 14.6f);

        positive3sd.add(entry1);
        positive3sd.add(entry2);
        positive3sd.add(entry3);
        positive3sd.add(entry4);
        positive3sd.add(entry5);
        positive3sd.add(entry6);
        positive3sd.add(entry7);
        positive3sd.add(entry8);
        positive3sd.add(entry9);
        positive3sd.add(entry10);
        positive3sd.add(entry11);
        positive3sd.add(entry12);
        positive3sd.add(entry13);
        positive3sd.add(entry14);
        positive3sd.add(entry15);
        positive3sd.add(entry16);
        positive3sd.add(entry17);
        positive3sd.add(entry18);
        positive3sd.add(entry19);
        positive3sd.add(entry20);
        positive3sd.add(entry21);
        positive3sd.add(entry22);
        positive3sd.add(entry23);
        positive3sd.add(entry24);
        positive3sd.add(entry25);

        positive3data = new LineDataSet(positive3sd, "+3SD");
        positive3data.setDrawCircles(false);
        positive3data.setDrawValues(false);
        positive3data.enableDashedLine(10, 10, 2);
        positive3data.setColor(Color.RED);

        return positive3data;
    }

    //2-5y
    public LineDataSet negative3SDBF(){
        final ArrayList<Entry> negative3sd = new ArrayList<>();
        LineDataSet neg3data;

        Entry entry1 = new Entry(0f, 9.2f);
        Entry entry2 = new Entry(1f, 9.5f);
        Entry entry3 = new Entry(2f, 9.8f);
        Entry entry4 = new Entry(3f, 10.1f);
        Entry entry5 = new Entry(4f, 10.4f);
        Entry entry6 = new Entry(5f, 10.7f);
        Entry entry7 = new Entry(6f, 11f);
        Entry entry8 = new Entry(7f, 11.2f);
        Entry entry9 = new Entry(8f, 11.5f);
        Entry entry10 = new Entry(9f, 11.8f);
        Entry entry11 = new Entry(10f, 12f);
        Entry entry12 = new Entry(11f, 12.2f);
        Entry entry13 = new Entry(12f, 12.5f);
        Entry entry14 = new Entry(13f, 12.8f);
        Entry entry15 = new Entry(14f, 13.0f);
        Entry entry16 = new Entry(15f, 13.2f);
        Entry entry17 = new Entry(16f, 13.5f);
        Entry entry18 = new Entry(17f, 13.7f);
        Entry entry19 = new Entry(18f, 14.0f);

        negative3sd.add(entry1);
        negative3sd.add(entry2);
        negative3sd.add(entry3);
        negative3sd.add(entry4);
        negative3sd.add(entry5);
        negative3sd.add(entry6);
        negative3sd.add(entry7);
        negative3sd.add(entry8);
        negative3sd.add(entry9);
        negative3sd.add(entry10);
        negative3sd.add(entry11);
        negative3sd.add(entry12);
        negative3sd.add(entry13);
        negative3sd.add(entry14);
        negative3sd.add(entry15);
        negative3sd.add(entry16);
        negative3sd.add(entry17);
        negative3sd.add(entry18);
        negative3sd.add(entry19);

        neg3data = new LineDataSet(negative3sd, "-3SD");
        neg3data.setDrawCircles(false);
        neg3data.setDrawValues(false);
        neg3data.enableDashedLine(10, 10, 2);
        neg3data.setColor(Color.RED);

        return neg3data;
    }

    public LineDataSet negative2SDBF(){
        final ArrayList<Entry> negative2sd = new ArrayList<>();
        LineDataSet neg2data;

        Entry entry1 = new Entry(0f, 10.1f);
        Entry entry2 = new Entry(1f, 10.5f);
        Entry entry3 = new Entry(2f, 10.8f);
        Entry entry4 = new Entry(3f, 11.2f);
        Entry entry5 = new Entry(4f, 11.5f);
        Entry entry6 = new Entry(5f, 11.8f);
        Entry entry7 = new Entry(6f, 12.1f);
        Entry entry8 = new Entry(7f, 12.5f);
        Entry entry9 = new Entry(8f, 12.8f);
        Entry entry10 = new Entry(9f, 13.1f);
        Entry entry11 = new Entry(10f, 13.4f);
        Entry entry12 = new Entry(11f, 13.7f);
        Entry entry13 = new Entry(12f, 14.0f);
        Entry entry14 = new Entry(13f, 14.3f);
        Entry entry15 = new Entry(14f, 14.5f);
        Entry entry16 = new Entry(15f, 14.8f);
        Entry entry17 = new Entry(16f, 15.1f);
        Entry entry18 = new Entry(17f, 15.4f);
        Entry entry19 = new Entry(18f, 15.7f);

        negative2sd.add(entry1);
        negative2sd.add(entry2);
        negative2sd.add(entry3);
        negative2sd.add(entry4);
        negative2sd.add(entry5);
        negative2sd.add(entry6);
        negative2sd.add(entry7);
        negative2sd.add(entry8);
        negative2sd.add(entry9);
        negative2sd.add(entry10);
        negative2sd.add(entry11);
        negative2sd.add(entry12);
        negative2sd.add(entry13);
        negative2sd.add(entry14);
        negative2sd.add(entry15);
        negative2sd.add(entry16);
        negative2sd.add(entry17);
        negative2sd.add(entry18);
        negative2sd.add(entry19);

        neg2data = new LineDataSet(negative2sd, "-2SD");
        neg2data.setDrawCircles(false);
        neg2data.setDrawValues(false);
        neg2data.enableDashedLine(10, 10, 2);
        neg2data.setColor(Color.YELLOW);

        return neg2data;
    }

    public LineDataSet zeroSDBF(){
        final ArrayList<Entry> zerosd = new ArrayList<>();
        LineDataSet zerodata;

        Entry entry1 = new Entry(0f, 11.5f);
        Entry entry2 = new Entry(1f, 11.9f);
        Entry entry3 = new Entry(2f, 12.3f);
        Entry entry4 = new Entry(3f, 12.7f);
        Entry entry5 = new Entry(4f, 13.1f);
        Entry entry6 = new Entry(5f, 13.5f);
        Entry entry7 = new Entry(6f, 13.9f);
        Entry entry8 = new Entry(7f, 14.2f);
        Entry entry9 = new Entry(8f, 14.6f);
        Entry entry10 = new Entry(9f, 15f);
        Entry entry11 = new Entry(10f, 15.3f);
        Entry entry12 = new Entry(11f, 15.7f);
        Entry entry13 = new Entry(12f, 16.1f);
        Entry entry14 = new Entry(13f, 16.4f);
        Entry entry15 = new Entry(14f, 16.8f);
        Entry entry16 = new Entry(15f, 17.2f);
        Entry entry17 = new Entry(16f, 17.5f);
        Entry entry18 = new Entry(17f, 17.9f);
        Entry entry19 = new Entry(18f, 18.2f);

        zerosd.add(entry1);
        zerosd.add(entry2);
        zerosd.add(entry3);
        zerosd.add(entry4);
        zerosd.add(entry5);
        zerosd.add(entry6);
        zerosd.add(entry7);
        zerosd.add(entry8);
        zerosd.add(entry9);
        zerosd.add(entry10);
        zerosd.add(entry11);
        zerosd.add(entry12);
        zerosd.add(entry13);
        zerosd.add(entry14);
        zerosd.add(entry15);
        zerosd.add(entry16);
        zerosd.add(entry17);
        zerosd.add(entry18);
        zerosd.add(entry19);

        zerodata = new LineDataSet(zerosd, "0SD");
        zerodata.setDrawCircles(false);
        zerodata.setDrawValues(false);
        zerodata.setColor(Color.BLACK);

        return zerodata;
    }

    public LineDataSet positive2SDBF(){
        final ArrayList<Entry> positive2sd = new ArrayList<>();
        LineDataSet positive2data;

        Entry entry1 = new Entry(0f, 13.1f);
        Entry entry2 = new Entry(1f, 13.6f);
        Entry entry3 = new Entry(2f, 14.0f);
        Entry entry4 = new Entry(3f, 14.5f);
        Entry entry5 = new Entry(4f, 15.0f);
        Entry entry6 = new Entry(5f, 15.4f);
        Entry entry7 = new Entry(6f, 15.9f);
        Entry entry8 = new Entry(7f, 16.3f);
        Entry entry9 = new Entry(8f, 16.8f);
        Entry entry10 = new Entry(9f, 17.3f);
        Entry entry11 = new Entry(10f, 17.7f);
        Entry entry12 = new Entry(11f, 18.2f);
        Entry entry13 = new Entry(12f, 18.6f);
        Entry entry14 = new Entry(13f, 19.1f);
        Entry entry15 = new Entry(14f, 19.5f);
        Entry entry16 = new Entry(15f, 20.0f);
        Entry entry17 = new Entry(16f, 20.4f);
        Entry entry18 = new Entry(17f, 20.9f);
        Entry entry19 = new Entry(18f, 21.3f);

        positive2sd.add(entry1);
        positive2sd.add(entry2);
        positive2sd.add(entry3);
        positive2sd.add(entry4);
        positive2sd.add(entry5);
        positive2sd.add(entry6);
        positive2sd.add(entry7);
        positive2sd.add(entry8);
        positive2sd.add(entry9);
        positive2sd.add(entry10);
        positive2sd.add(entry11);
        positive2sd.add(entry12);
        positive2sd.add(entry13);
        positive2sd.add(entry14);
        positive2sd.add(entry15);
        positive2sd.add(entry16);
        positive2sd.add(entry17);
        positive2sd.add(entry18);
        positive2sd.add(entry19);

        positive2data = new LineDataSet(positive2sd, "+2SD");
        positive2data.setDrawCircles(false);
        positive2data.setDrawValues(false);
        positive2data.enableDashedLine(10, 10, 2);
        positive2data.setColor(Color.YELLOW);

        return positive2data;
    }

    public LineDataSet positive3SDBF(){
        final ArrayList<Entry> positive3sd = new ArrayList<>();
        LineDataSet positive3data;

        Entry entry1 = new Entry(0f, 14.6f);
        Entry entry2 = new Entry(1f, 15.2f);
        Entry entry3 = new Entry(2f, 15.7f);
        Entry entry4 = new Entry(3f, 16.2f);
        Entry entry5 = new Entry(4f, 16.8f);
        Entry entry6 = new Entry(5f, 17.3f);
        Entry entry7 = new Entry(6f, 17.8f);
        Entry entry8 = new Entry(7f, 18.4f);
        Entry entry9 = new Entry(8f, 18.9f);
        Entry entry10 = new Entry(9f, 19.5f);
        Entry entry11 = new Entry(10f, 20.0f);
        Entry entry12 = new Entry(11f, 20.6f);
        Entry entry13 = new Entry(12f, 21.1f);
        Entry entry14 = new Entry(13f, 21.7f);
        Entry entry15 = new Entry(14f, 22.2f);
        Entry entry16 = new Entry(15f, 22.8f);
        Entry entry17 = new Entry(16f, 23.3f);
        Entry entry18 = new Entry(17f, 23.9f);
        Entry entry19 = new Entry(18f, 24.4f);

        positive3sd.add(entry1);
        positive3sd.add(entry2);
        positive3sd.add(entry3);
        positive3sd.add(entry4);
        positive3sd.add(entry5);
        positive3sd.add(entry6);
        positive3sd.add(entry7);
        positive3sd.add(entry8);
        positive3sd.add(entry9);
        positive3sd.add(entry10);
        positive3sd.add(entry11);
        positive3sd.add(entry12);
        positive3sd.add(entry13);
        positive3sd.add(entry14);
        positive3sd.add(entry15);
        positive3sd.add(entry16);
        positive3sd.add(entry17);
        positive3sd.add(entry18);
        positive3sd.add(entry19);

        positive3data = new LineDataSet(positive3sd, "+3SD");
        positive3data.setDrawCircles(false);
        positive3data.setDrawValues(false);
        positive3data.enableDashedLine(10, 10, 2);
        positive3data.setColor(Color.RED);

        return positive3data;
    }

    //baby weight
    @Override
    public void onStart() {
        super.onStart();

        //predefined Male Value
        negative3sd = negative3SD();
        negative2sd = negative2SD();
        zerosd = zeroSD();
        positive2sd = positive2SD();
        positive3sd = positive3SD();

        negative3sdB = negative3SDB();
        negative2sdB = negative2SDB();
        zerosdB = zeroSDB();
        positive2sdB = positive2SDB();
        positive3sdB = positive3SDB();

        //predefined Female Value
        negative3sdF = negative3SDF();
        negative2sdF = negative2SDF();
        zerosdF = zeroSDF();
        positive2sdF = positive2SDF();
        positive3sdF = positive3SDF();

        negative3sdBF = negative3SDBF();
        negative2sdBF = negative2SDBF();
        zerosdBF = zeroSDBF();
        positive2sdBF = positive2SDBF();
        positive3sdBF = positive3SDBF();

        //born to 2 years
        table_baby = FirebaseDatabase.getInstance().getReference().child("Baby");
        queryBaby = table_baby.orderByChild("bId").equalTo(bId);
        queryBaby.keepSynced(true);
        queryBaby.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Baby baby = snapshot.getValue(Baby.class);
                        String gender = baby.getGender().toString();
                        if (gender.equals("Male")){
                            String dob = baby.getDob().toString();

                            String[] datesplit = dob.split("/");
                            final int day = Integer.parseInt(datesplit[0]);
                            final int month = Integer.parseInt(datesplit[1]);
                            final int year = Integer.parseInt(datesplit[2]);

                            table_measure = FirebaseDatabase.getInstance().getReference().child("Measurement");
                            queryBaby = table_measure.orderByChild("timestamp_babyId").endAt("_"+ bId);
                            queryBaby.keepSynced(true);
                            queryBaby.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    float newborn = 0f, firstMonth = 0f, secondMonth = 0f, thirdMonth = 0f, forthMonth = 0f;
                                    float fifthMonth = 0f, sixthMonth = 0f, seventhMonth = 0f, eightMonth = 0f, ninethMonth = 0f;
                                    float tenthMonth = 0f, eleventhMonth = 0f, oneyear = 0f, oneyearone = 0f, oneyeartwo = 0f;
                                    float oneyearthree = 0f, oneyearfour = 0f, oneyearfive = 0f, oneyearsix = 0f, oneyearseven = 0f;
                                    float oneyeareight = 0f, oneyearnine = 0f, oneyearten = 0f, oneyeareleven = 0f, twoyear = 0f;
                                    if (dataSnapshot.exists()){
                                        for (DataSnapshot snapshot1: dataSnapshot.getChildren()){
                                            Measurement measurement = snapshot1.getValue(Measurement.class);
                                            if(measurement.getBaby_id().equals(bId)) {
                                                String created = measurement.getCreated();

                                                String[] datesplit2 = created.split("/");
                                                int dayC = Integer.parseInt(datesplit2[0]);
                                                int monthC = Integer.parseInt(datesplit2[1]);
                                                int yearC = Integer.parseInt(datesplit2[2]);

                                                totalday = 0; totalmonth = 0; totalyear = 0;
                                                //subtract date
                                                if((dayC == day) || (dayC > day)){
                                                    totalday = dayC - day;
                                                }else if(dayC < day){
                                                    dayC = dayC + 31;
                                                    totalday = dayC - day;
                                                    monthC = monthC - 1;
                                                }

                                                if((monthC == month) || (monthC > month)){
                                                    totalmonth = monthC - month;
                                                    totalyear = yearC - year;
                                                }else if(monthC < month){
                                                    monthC = monthC + 12;
                                                    totalmonth = monthC - month;
                                                    yearC = yearC - 1;
                                                    totalyear = yearC - year;
                                                }

                                                if (totalyear == 0 && totalmonth == 0) {
                                                    newborn = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 1) {
                                                    firstMonth = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 2) {
                                                    secondMonth = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 3) {
                                                    thirdMonth = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 4) {
                                                    forthMonth = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 5) {
                                                    fifthMonth = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 6) {
                                                    sixthMonth = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 7) {
                                                    seventhMonth = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 8) {
                                                    eightMonth = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 9) {
                                                    ninethMonth = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 10) {
                                                    tenthMonth = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 11) {
                                                    eleventhMonth = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 0) {
                                                    oneyear = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 1) {
                                                    oneyearone = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 2) {
                                                    oneyeartwo = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 3) {
                                                    oneyearthree = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 4) {
                                                    oneyearfour = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 5) {
                                                    oneyearfive = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 6) {
                                                    oneyearsix = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 7) {
                                                    oneyearseven = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 8) {
                                                    oneyeareight = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 9) {
                                                    oneyearnine = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 10) {
                                                    oneyearten = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 11) {
                                                    oneyeareleven = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 2 && totalmonth == 0) {
                                                    twoyear = Float.parseFloat(measurement.getWeight());
                                                }

                                                final ArrayList<Entry> weightList = new ArrayList<>();

                                                Entry entry1 = new Entry(0f, newborn);
                                                Entry entry2 = new Entry(1f, firstMonth);
                                                Entry entry3 = new Entry(2f, secondMonth);
                                                Entry entry4 = new Entry(3f, thirdMonth);
                                                Entry entry5 = new Entry(4f, forthMonth);
                                                Entry entry6 = new Entry(5f, fifthMonth);
                                                Entry entry7 = new Entry(6f, sixthMonth);
                                                Entry entry8 = new Entry(7f, seventhMonth);
                                                Entry entry9 = new Entry(8f, eightMonth);
                                                Entry entry10 = new Entry(9f, ninethMonth);
                                                Entry entry11 = new Entry(10f, tenthMonth);
                                                Entry entry12 = new Entry(11f, eleventhMonth);
                                                Entry entry13 = new Entry(12f, oneyear);
                                                Entry entry14 = new Entry(13f, oneyearone);
                                                Entry entry15 = new Entry(14f, oneyeartwo);
                                                Entry entry16 = new Entry(15f, oneyearthree);
                                                Entry entry17 = new Entry(16f, oneyearfour);
                                                Entry entry18 = new Entry(17f, oneyearfive);
                                                Entry entry19 = new Entry(18f, oneyearsix);
                                                Entry entry20 = new Entry(19f, oneyearseven);
                                                Entry entry21 = new Entry(20f, oneyeareight);
                                                Entry entry22 = new Entry(21f, oneyearnine);
                                                Entry entry23 = new Entry(22f, oneyearten);
                                                Entry entry24 = new Entry(23f, oneyeareleven);
                                                Entry entry25 = new Entry(24f, twoyear);

                                                weightList.add(entry1);
                                                weightList.add(entry2);
                                                weightList.add(entry3);
                                                weightList.add(entry4);
                                                weightList.add(entry5);
                                                weightList.add(entry6);
                                                weightList.add(entry7);
                                                weightList.add(entry8);
                                                weightList.add(entry9);
                                                weightList.add(entry10);
                                                weightList.add(entry11);
                                                weightList.add(entry12);
                                                weightList.add(entry13);
                                                weightList.add(entry14);
                                                weightList.add(entry15);
                                                weightList.add(entry16);
                                                weightList.add(entry17);
                                                weightList.add(entry18);
                                                weightList.add(entry19);
                                                weightList.add(entry20);
                                                weightList.add(entry21);
                                                weightList.add(entry22);
                                                weightList.add(entry23);
                                                weightList.add(entry24);
                                                weightList.add(entry25);

                                                weightData = new LineDataSet(weightList, "Baby Weight");
                                                weightData.setDrawCircles(false);
                                                weightData.setDrawValues(true);
                                                weightData.setColor(Color.BLUE);
                                            }
                                        }

                                        LineData data = new LineData(negative3sd, negative2sd, zerosd, positive2sd, positive3sd, weightData);
                                        chartA.setData(data);

                                        final String[] months = new String[]{"Birth", "1", "2", "3", "4", "5", "6", "7", "8", "9","10","11",
                                                "1Year", "1", "2", "3", "4", "5", "6", "7", "8", "9","10","11",
                                                "2Year"};

                                        XAxis xAxis = chartA.getXAxis();
                                        xAxis.setGranularity(1f);
                                        xAxis.setValueFormatter(new MyXAxisValueFormatter(months));

                                        data.notifyDataChanged();
                                        chartA.notifyDataSetChanged();
                                        chartA.invalidate();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }else if (gender.equals("Female")){
                            String dob = baby.getDob().toString();

                            String[] datesplit = dob.split("/");
                            final int day = Integer.parseInt(datesplit[0]);
                            final int month = Integer.parseInt(datesplit[1]);
                            final int year = Integer.parseInt(datesplit[2]);

                            table_measure = FirebaseDatabase.getInstance().getReference().child("Measurement");
                            queryBaby = table_measure.orderByChild("timestamp_babyId").endAt("_"+ bId);
                            queryBaby.keepSynced(true);
                            queryBaby.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    float newborn = 0f, firstMonth = 0f, secondMonth = 0f, thirdMonth = 0f, forthMonth = 0f;
                                    float fifthMonth = 0f, sixthMonth = 0f, seventhMonth = 0f, eightMonth = 0f, ninethMonth = 0f;
                                    float tenthMonth = 0f, eleventhMonth = 0f, oneyear = 0f, oneyearone = 0f, oneyeartwo = 0f;
                                    float oneyearthree = 0f, oneyearfour = 0f, oneyearfive = 0f, oneyearsix = 0f, oneyearseven = 0f;
                                    float oneyeareight = 0f, oneyearnine = 0f, oneyearten = 0f, oneyeareleven = 0f, twoyear = 0f;
                                    if (dataSnapshot.exists()){
                                        for (DataSnapshot snapshot1: dataSnapshot.getChildren()){
                                            Measurement measurement = snapshot1.getValue(Measurement.class);
                                            if(measurement.getBaby_id().equals(bId)) {
                                                String created = measurement.getCreated();

                                                String[] datesplit2 = created.split("/");
                                                int dayC = Integer.parseInt(datesplit2[0]);
                                                int monthC = Integer.parseInt(datesplit2[1]);
                                                int yearC = Integer.parseInt(datesplit2[2]);

                                                totalday = 0; totalmonth = 0; totalyear = 0;
                                                //subtract date
                                                if((dayC == day) || (dayC > day)){
                                                    totalday = dayC - day;
                                                }else if(dayC < day){
                                                    dayC = dayC + 31;
                                                    totalday = dayC - day;
                                                    monthC = monthC - 1;
                                                }

                                                if((monthC == month) || (monthC > month)){
                                                    totalmonth = monthC - month;
                                                    totalyear = yearC - year;
                                                }else if(monthC < month){
                                                    monthC = monthC + 12;
                                                    totalmonth = monthC - month;
                                                    yearC = yearC - 1;
                                                    totalyear = yearC - year;
                                                }

                                                if (totalyear == 0 && totalmonth == 0) {
                                                    newborn = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 1) {
                                                    firstMonth = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 2) {
                                                    secondMonth = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 3) {
                                                    thirdMonth = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 4) {
                                                    forthMonth = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 5) {
                                                    fifthMonth = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 6) {
                                                    sixthMonth = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 7) {
                                                    seventhMonth = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 8) {
                                                    eightMonth = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 9) {
                                                    ninethMonth = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 10) {
                                                    tenthMonth = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 11) {
                                                    eleventhMonth = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 0) {
                                                    oneyear = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 1) {
                                                    oneyearone = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 2) {
                                                    oneyeartwo = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 3) {
                                                    oneyearthree = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 4) {
                                                    oneyearfour = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 5) {
                                                    oneyearfive = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 6) {
                                                    oneyearsix = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 7) {
                                                    oneyearseven = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 8) {
                                                    oneyeareight = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 9) {
                                                    oneyearnine = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 10) {
                                                    oneyearten = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 11) {
                                                    oneyeareleven = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 2 && totalmonth == 0) {
                                                    twoyear = Float.parseFloat(measurement.getWeight());
                                                }

                                                final ArrayList<Entry> weightList = new ArrayList<>();

                                                Entry entry1 = new Entry(0f, newborn);
                                                Entry entry2 = new Entry(1f, firstMonth);
                                                Entry entry3 = new Entry(2f, secondMonth);
                                                Entry entry4 = new Entry(3f, thirdMonth);
                                                Entry entry5 = new Entry(4f, forthMonth);
                                                Entry entry6 = new Entry(5f, fifthMonth);
                                                Entry entry7 = new Entry(6f, sixthMonth);
                                                Entry entry8 = new Entry(7f, seventhMonth);
                                                Entry entry9 = new Entry(8f, eightMonth);
                                                Entry entry10 = new Entry(9f, ninethMonth);
                                                Entry entry11 = new Entry(10f, tenthMonth);
                                                Entry entry12 = new Entry(11f, eleventhMonth);
                                                Entry entry13 = new Entry(12f, oneyear);
                                                Entry entry14 = new Entry(13f, oneyearone);
                                                Entry entry15 = new Entry(14f, oneyeartwo);
                                                Entry entry16 = new Entry(15f, oneyearthree);
                                                Entry entry17 = new Entry(16f, oneyearfour);
                                                Entry entry18 = new Entry(17f, oneyearfive);
                                                Entry entry19 = new Entry(18f, oneyearsix);
                                                Entry entry20 = new Entry(19f, oneyearseven);
                                                Entry entry21 = new Entry(20f, oneyeareight);
                                                Entry entry22 = new Entry(21f, oneyearnine);
                                                Entry entry23 = new Entry(22f, oneyearten);
                                                Entry entry24 = new Entry(23f, oneyeareleven);
                                                Entry entry25 = new Entry(24f, twoyear);

                                                weightList.add(entry1);
                                                weightList.add(entry2);
                                                weightList.add(entry3);
                                                weightList.add(entry4);
                                                weightList.add(entry5);
                                                weightList.add(entry6);
                                                weightList.add(entry7);
                                                weightList.add(entry8);
                                                weightList.add(entry9);
                                                weightList.add(entry10);
                                                weightList.add(entry11);
                                                weightList.add(entry12);
                                                weightList.add(entry13);
                                                weightList.add(entry14);
                                                weightList.add(entry15);
                                                weightList.add(entry16);
                                                weightList.add(entry17);
                                                weightList.add(entry18);
                                                weightList.add(entry19);
                                                weightList.add(entry20);
                                                weightList.add(entry21);
                                                weightList.add(entry22);
                                                weightList.add(entry23);
                                                weightList.add(entry24);
                                                weightList.add(entry25);

                                                weightDataF = new LineDataSet(weightList, "Baby Weight");
                                                weightDataF.setDrawCircles(false);
                                                weightDataF.setDrawValues(true);
                                                weightDataF.setColor(Color.BLUE);
                                            }
                                        }

                                        LineData data = new LineData(negative3sdF, negative2sdF, zerosdF, positive2sdF, positive3sdF, weightDataF);
                                        chartA.setData(data);

                                        final String[] months = new String[]{"Birth", "1", "2", "3", "4", "5", "6", "7", "8", "9","10","11",
                                                "1Year", "1", "2", "3", "4", "5", "6", "7", "8", "9","10","11",
                                                "2Year"};

                                        XAxis xAxis = chartA.getXAxis();
                                        xAxis.setGranularity(1f);
                                        xAxis.setValueFormatter(new MyXAxisValueFormatter(months));

                                        data.notifyDataChanged();
                                        chartA.notifyDataSetChanged();
                                        chartA.invalidate();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

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

        //2 years to 5 years
        queryBabyB = table_baby.orderByChild("bId").equalTo(bId);
        queryBabyB.keepSynced(true);
        queryBabyB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Baby baby = snapshot.getValue(Baby.class);
                        if (baby.getGender().equals("Male")){
                            String dob = baby.getDob().toString();

                            String[] datesplit = dob.split("/");
                            final int day = Integer.parseInt(datesplit[0]);
                            final int month = Integer.parseInt(datesplit[1]);
                            final int year = Integer.parseInt(datesplit[2]);

                            table_measure = FirebaseDatabase.getInstance().getReference().child("Measurement");
                            queryBaby = table_measure.orderByChild("timestamp_babyId").endAt("_"+ bId);
                            queryBaby.keepSynced(true);
                            queryBaby.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    float twoyear = 0f, twoyeartwo = 0f, twoyearfour = 0f, twoyearsix = 0f, twoyeareight = 0f,
                                            twoyearten = 0f, threeyear = 0f, threeyeartwo = 0f, threeyearfour = 0f, threeyearsix = 0f,
                                            threeyeareight = 0f, threeyearten = 0f, fouryear = 0f, fouryeartwo = 0f, fouryearfour = 0f,
                                            fouryearsix = 0f, fouryeareight = 0f, fouryearten = 0f, fiveyear = 0f;
                                    if (dataSnapshot.exists()){
                                        for (DataSnapshot snapshot1: dataSnapshot.getChildren()){
                                            Measurement measurement = snapshot1.getValue(Measurement.class);
                                            if(measurement.getBaby_id().equals(bId)) {
                                                String created = measurement.getCreated();

                                                String[] datesplit2 = created.split("/");
                                                int dayC = Integer.parseInt(datesplit2[0]);
                                                int monthC = Integer.parseInt(datesplit2[1]);
                                                int yearC = Integer.parseInt(datesplit2[2]);

                                                totalday = 0; totalmonth = 0; totalyear = 0;
                                                //subtract date
                                                if((dayC == day) || (dayC > day)){
                                                    totalday = dayC - day;
                                                }else if(dayC < day){
                                                    dayC = dayC + 31;
                                                    totalday = dayC - day;
                                                    monthC = monthC - 1;
                                                }

                                                if((monthC == month) || (monthC > month)){
                                                    totalmonth = monthC - month;
                                                    totalyear = yearC - year;
                                                }else if(monthC < month){
                                                    monthC = monthC + 12;
                                                    totalmonth = monthC - month;
                                                    yearC = yearC - 1;
                                                    totalyear = yearC - year;
                                                }

                                                if (totalyear == 2 && (totalmonth == 0 || totalmonth == 1)) {
                                                    twoyear = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 2 && (totalmonth == 2 || totalmonth == 3)) {
                                                    twoyeartwo = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 2 && (totalmonth == 4 || totalmonth == 5)) {
                                                    twoyearfour = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 2 && (totalmonth == 6 || totalmonth == 7)) {
                                                    twoyearsix = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 2 && (totalmonth == 8 || totalmonth == 9)) {
                                                    twoyeareight = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 2 && (totalmonth == 10 || totalmonth == 11 )) {
                                                    twoyearten = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 0 || totalmonth == 1 )) {
                                                    threeyear = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 2 || totalmonth == 3 )) {
                                                    threeyeartwo = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 4 || totalmonth == 5 )) {
                                                    threeyearfour = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 6 || totalmonth == 7 )) {
                                                    threeyearsix = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 8 || totalmonth == 9 )) {
                                                    threeyeareight = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 10 || totalmonth == 11 )) {
                                                    threeyearten = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 0 || totalmonth == 1 )) {
                                                    fouryear = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 2 || totalmonth == 3 )) {
                                                    fouryeartwo = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 4 || totalmonth == 5 )) {
                                                    fouryearfour = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 6 || totalmonth == 7 )) {
                                                    fouryearsix = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 8 || totalmonth == 9 )) {
                                                    fouryeareight = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 10 || totalmonth == 11 )) {
                                                    fouryearten = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 5 && (totalmonth == 0 || totalmonth == 1 )) {
                                                    fiveyear = Float.parseFloat(measurement.getWeight());
                                                }

                                                final ArrayList<Entry> weightList = new ArrayList<>();

                                                Entry entry1 = new Entry(0f, twoyear);
                                                Entry entry2 = new Entry(1f, twoyeartwo);
                                                Entry entry3 = new Entry(2f, twoyearfour);
                                                Entry entry4 = new Entry(3f, twoyearsix);
                                                Entry entry5 = new Entry(4f, twoyeareight);
                                                Entry entry6 = new Entry(5f, twoyearten);
                                                Entry entry7 = new Entry(6f, threeyear);
                                                Entry entry8 = new Entry(7f, threeyeartwo);
                                                Entry entry9 = new Entry(8f, threeyearfour);
                                                Entry entry10 = new Entry(9f, threeyearsix);
                                                Entry entry11 = new Entry(10f, threeyeareight);
                                                Entry entry12 = new Entry(11f, threeyearten);
                                                Entry entry13 = new Entry(12f, fouryear);
                                                Entry entry14 = new Entry(13f, fouryeartwo);
                                                Entry entry15 = new Entry(14f, fouryearfour);
                                                Entry entry16 = new Entry(15f, fouryearsix);
                                                Entry entry17 = new Entry(16f, fouryeareight);
                                                Entry entry18 = new Entry(17f, fouryearten);
                                                Entry entry19 = new Entry(18f, fiveyear);

                                                weightList.add(entry1);
                                                weightList.add(entry2);
                                                weightList.add(entry3);
                                                weightList.add(entry4);
                                                weightList.add(entry5);
                                                weightList.add(entry6);
                                                weightList.add(entry7);
                                                weightList.add(entry8);
                                                weightList.add(entry9);
                                                weightList.add(entry10);
                                                weightList.add(entry11);
                                                weightList.add(entry12);
                                                weightList.add(entry13);
                                                weightList.add(entry14);
                                                weightList.add(entry15);
                                                weightList.add(entry16);
                                                weightList.add(entry17);
                                                weightList.add(entry18);
                                                weightList.add(entry19);

                                                weightDataB = new LineDataSet(weightList, "Baby Weight");
                                                weightDataB.setDrawCircles(false);
                                                weightDataB.setDrawValues(true);
                                                weightDataB.setColor(Color.BLUE);
                                            }
                                        }

                                        LineData dataB = new LineData(negative3sdB, negative2sdB, zerosdB, positive2sdB, positive3sdB, weightDataB);
                                        chartB.setData(dataB);

                                        final String[] monthsB = new String[]{"2Year", "2", "4", "6", "8", "10",
                                                "3Year", "2", "4", "6","8", "10",
                                                "4Year", "2", "4", "6", "8", "10",
                                                "5Year"};

                                        XAxis xAxisB = chartB.getXAxis();
                                        xAxisB.setGranularity(1f);
                                        xAxisB.setValueFormatter(new MyXAxisValueFormatter(monthsB));

                                        dataB.notifyDataChanged();
                                        chartB.notifyDataSetChanged();
                                        chartB.invalidate();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }else if (baby.getGender().equals("Female")){
                            String dob = baby.getDob().toString();

                            String[] datesplit = dob.split("/");
                            final int day = Integer.parseInt(datesplit[0]);
                            final int month = Integer.parseInt(datesplit[1]);
                            final int year = Integer.parseInt(datesplit[2]);

                            table_measure = FirebaseDatabase.getInstance().getReference().child("Measurement");
                            queryBaby = table_measure.orderByChild("timestamp_babyId").endAt("_"+ bId);
                            queryBaby.keepSynced(true);
                            queryBaby.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    float twoyear = 0f, twoyeartwo = 0f, twoyearfour = 0f, twoyearsix = 0f, twoyeareight = 0f,
                                            twoyearten = 0f, threeyear = 0f, threeyeartwo = 0f, threeyearfour = 0f, threeyearsix = 0f,
                                            threeyeareight = 0f, threeyearten = 0f, fouryear = 0f, fouryeartwo = 0f, fouryearfour = 0f,
                                            fouryearsix = 0f, fouryeareight = 0f, fouryearten = 0f, fiveyear = 0f;
                                    if (dataSnapshot.exists()){
                                        for (DataSnapshot snapshot1: dataSnapshot.getChildren()){
                                            Measurement measurement = snapshot1.getValue(Measurement.class);
                                            if(measurement.getBaby_id().equals(bId)) {
                                                String created = measurement.getCreated();

                                                String[] datesplit2 = created.split("/");
                                                int dayC = Integer.parseInt(datesplit2[0]);
                                                int monthC = Integer.parseInt(datesplit2[1]);
                                                int yearC = Integer.parseInt(datesplit2[2]);

                                                totalday = 0; totalmonth = 0; totalyear = 0;
                                                //subtract date
                                                if((dayC == day) || (dayC > day)){
                                                    totalday = dayC - day;
                                                }else if(dayC < day){
                                                    dayC = dayC + 31;
                                                    totalday = dayC - day;
                                                    monthC = monthC - 1;
                                                }

                                                if((monthC == month) || (monthC > month)){
                                                    totalmonth = monthC - month;
                                                    totalyear = yearC - year;
                                                }else if(monthC < month){
                                                    monthC = monthC + 12;
                                                    totalmonth = monthC - month;
                                                    yearC = yearC - 1;
                                                    totalyear = yearC - year;
                                                }

                                                if (totalyear == 2 && (totalmonth == 0 || totalmonth == 1)) {
                                                    twoyear = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 2 && (totalmonth == 2 || totalmonth == 3)) {
                                                    twoyeartwo = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 2 && (totalmonth == 4 || totalmonth == 5)) {
                                                    twoyearfour = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 2 && (totalmonth == 6 || totalmonth == 7)) {
                                                    twoyearsix = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 2 && (totalmonth == 8 || totalmonth == 9)) {
                                                    twoyeareight = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 2 && (totalmonth == 10 || totalmonth == 11 )) {
                                                    twoyearten = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 0 || totalmonth == 1 )) {
                                                    threeyear = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 2 || totalmonth == 3 )) {
                                                    threeyeartwo = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 4 || totalmonth == 5 )) {
                                                    threeyearfour = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 6 || totalmonth == 7 )) {
                                                    threeyearsix = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 8 || totalmonth == 9 )) {
                                                    threeyeareight = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 10 || totalmonth == 11 )) {
                                                    threeyearten = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 0 || totalmonth == 1 )) {
                                                    fouryear = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 2 || totalmonth == 3 )) {
                                                    fouryeartwo = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 4 || totalmonth == 5 )) {
                                                    fouryearfour = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 6 || totalmonth == 7 )) {
                                                    fouryearsix = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 8 || totalmonth == 9 )) {
                                                    fouryeareight = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 10 || totalmonth == 11 )) {
                                                    fouryearten = Float.parseFloat(measurement.getWeight());
                                                }

                                                if (totalyear == 5 && (totalmonth == 0 || totalmonth == 1 )) {
                                                    fiveyear = Float.parseFloat(measurement.getWeight());
                                                }

                                                final ArrayList<Entry> weightList = new ArrayList<>();

                                                Entry entry1 = new Entry(0f, twoyear);
                                                Entry entry2 = new Entry(1f, twoyeartwo);
                                                Entry entry3 = new Entry(2f, twoyearfour);
                                                Entry entry4 = new Entry(3f, twoyearsix);
                                                Entry entry5 = new Entry(4f, twoyeareight);
                                                Entry entry6 = new Entry(5f, twoyearten);
                                                Entry entry7 = new Entry(6f, threeyear);
                                                Entry entry8 = new Entry(7f, threeyeartwo);
                                                Entry entry9 = new Entry(8f, threeyearfour);
                                                Entry entry10 = new Entry(9f, threeyearsix);
                                                Entry entry11 = new Entry(10f, threeyeareight);
                                                Entry entry12 = new Entry(11f, threeyearten);
                                                Entry entry13 = new Entry(12f, fouryear);
                                                Entry entry14 = new Entry(13f, fouryeartwo);
                                                Entry entry15 = new Entry(14f, fouryearfour);
                                                Entry entry16 = new Entry(15f, fouryearsix);
                                                Entry entry17 = new Entry(16f, fouryeareight);
                                                Entry entry18 = new Entry(17f, fouryearten);
                                                Entry entry19 = new Entry(18f, fiveyear);

                                                weightList.add(entry1);
                                                weightList.add(entry2);
                                                weightList.add(entry3);
                                                weightList.add(entry4);
                                                weightList.add(entry5);
                                                weightList.add(entry6);
                                                weightList.add(entry7);
                                                weightList.add(entry8);
                                                weightList.add(entry9);
                                                weightList.add(entry10);
                                                weightList.add(entry11);
                                                weightList.add(entry12);
                                                weightList.add(entry13);
                                                weightList.add(entry14);
                                                weightList.add(entry15);
                                                weightList.add(entry16);
                                                weightList.add(entry17);
                                                weightList.add(entry18);
                                                weightList.add(entry19);

                                                weightDataBF = new LineDataSet(weightList, "Baby Weight");
                                                weightDataBF.setDrawCircles(false);
                                                weightDataBF.setDrawValues(true);
                                                weightDataBF.setColor(Color.BLUE);
                                            }
                                        }

                                        LineData dataB = new LineData(negative3sdBF, negative2sdBF, zerosdBF, positive2sdBF, positive3sdBF, weightDataBF);
                                        chartB.setData(dataB);

                                        final String[] monthsB = new String[]{"2Year", "2", "4", "6", "8", "10",
                                                "3Year", "2", "4", "6","8", "10",
                                                "4Year", "2", "4", "6", "8", "10",
                                                "5Year"};

                                        XAxis xAxisB = chartB.getXAxis();
                                        xAxisB.setGranularity(1f);
                                        xAxisB.setValueFormatter(new MyXAxisValueFormatter(monthsB));

                                        dataB.notifyDataChanged();
                                        chartB.notifyDataSetChanged();
                                        chartB.invalidate();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

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

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            int intValue = (int) value;

            if (mValues.length > intValue && intValue >= 0) return mValues[intValue];

            return "";
        }

        /** this is only needed if numbers are returned, else return 0 */
        public int getDecimalDigits() { return 0; }
    }
}
