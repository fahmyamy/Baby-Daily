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

public class MeasurementStaFragHeight extends Fragment {
    private View view;
    private String bId;
    private LineChart chartA, chartB;

    private DatabaseReference table_baby, table_measure, table_measureB;
    private Query queryBaby, queryBabyB, queryB;

    private LineDataSet negative3sd, negative2sd, zerosd, positive2sd, positive3sd;
    private LineDataSet negative3sdB, negative2sdB, zerosdB, positive2sdB, positive3sdB;
    private LineDataSet negative3sdF, negative2sdF, zerosdF, positive2sdF, positive3sdF;
    private LineDataSet negative3sdBF, negative2sdBF, zerosdBF, positive2sdBF, positive3sdBF;
    private LineDataSet heightData, heightDataB, heightDataF, heightDataBF;
    private int totalday = 0, totalmonth = 0, totalyear = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.measurement_height_fragment, container, false);
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

        Entry entry1 = new Entry(0f, 46.3f);
        Entry entry2 = new Entry(1f, 51.1f);
        Entry entry3 = new Entry(2f, 54.7f);
        Entry entry4 = new Entry(3f, 57.6f);
        Entry entry5 = new Entry(4f, 60.0f);
        Entry entry6 = new Entry(5f, 61.9f);
        Entry entry7 = new Entry(6f, 63.6f);
        Entry entry8 = new Entry(7f, 65.1f);
        Entry entry9 = new Entry(8f, 66.5f);
        Entry entry10 = new Entry(9f, 67.7f);
        Entry entry11 = new Entry(10f, 69f);
        Entry entry12 = new Entry(11f, 70.2f);
        Entry entry13 = new Entry(12f, 71.3f);
        Entry entry14 = new Entry(13f, 72.4f);
        Entry entry15 = new Entry(14f, 73.4f);
        Entry entry16 = new Entry(15f, 74.4f);
        Entry entry17 = new Entry(16f, 75.4f);
        Entry entry18 = new Entry(17f, 76.3f);
        Entry entry19 = new Entry(18f, 77.2f);
        Entry entry20 = new Entry(19f, 78.1f);
        Entry entry21 = new Entry(20f, 78.9f);
        Entry entry22 = new Entry(21f, 79.7f);
        Entry entry23 = new Entry(22f, 80.5f);
        Entry entry24 = new Entry(23f, 81.3f);
        Entry entry25 = new Entry(24f, 82.1f);

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

        Entry entry1 = new Entry(0f, 47.9f);
        Entry entry2 = new Entry(1f, 52.7f);
        Entry entry3 = new Entry(2f, 56.4f);
        Entry entry4 = new Entry(3f, 59.3f);
        Entry entry5 = new Entry(4f, 61.7f);
        Entry entry6 = new Entry(5f, 63.7f);
        Entry entry7 = new Entry(6f, 65.4f);
        Entry entry8 = new Entry(7f, 66.9f);
        Entry entry9 = new Entry(8f, 68.3f);
        Entry entry10 = new Entry(9f, 69.6f);
        Entry entry11 = new Entry(10f, 70.9f);
        Entry entry12 = new Entry(11f, 72.1f);
        Entry entry13 = new Entry(12f, 73.3f);
        Entry entry14 = new Entry(13f, 74.4f);
        Entry entry15 = new Entry(14f, 75.5f);
        Entry entry16 = new Entry(15f, 76.5f);
        Entry entry17 = new Entry(16f, 77.5f);
        Entry entry18 = new Entry(17f, 78.5f);
        Entry entry19 = new Entry(18f, 79.5f);
        Entry entry20 = new Entry(19f, 80.4f);
        Entry entry21 = new Entry(20f, 81.3f);
        Entry entry22 = new Entry(21f, 82.2f);
        Entry entry23 = new Entry(22f, 83.0f);
        Entry entry24 = new Entry(23f, 83.8f);
        Entry entry25 = new Entry(24f, 84.6f);

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

        Entry entry1 = new Entry(0f, 49.9f);
        Entry entry2 = new Entry(1f, 54.7f);
        Entry entry3 = new Entry(2f, 58.4f);
        Entry entry4 = new Entry(3f, 61.4f);
        Entry entry5 = new Entry(4f, 63.9f);
        Entry entry6 = new Entry(5f, 65.9f);
        Entry entry7 = new Entry(6f, 67.6f);
        Entry entry8 = new Entry(7f, 69.2f);
        Entry entry9 = new Entry(8f, 70.6f);
        Entry entry10 = new Entry(9f, 72f);
        Entry entry11 = new Entry(10f, 73.3f);
        Entry entry12 = new Entry(11f, 74.5f);
        Entry entry13 = new Entry(12f, 75.7f);
        Entry entry14 = new Entry(13f, 76.9f);
        Entry entry15 = new Entry(14f, 78.0f);
        Entry entry16 = new Entry(15f, 79.1f);
        Entry entry17 = new Entry(16f, 80.2f);
        Entry entry18 = new Entry(17f, 81.2f);
        Entry entry19 = new Entry(18f, 82.3f);
        Entry entry20 = new Entry(19f, 83.2f);
        Entry entry21 = new Entry(20f, 84.2f);
        Entry entry22 = new Entry(21f, 85.1f);
        Entry entry23 = new Entry(22f, 86f);
        Entry entry24 = new Entry(23f, 86.9f);
        Entry entry25 = new Entry(24f, 87.8f);

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

        Entry entry1 = new Entry(0f, 51.8f);
        Entry entry2 = new Entry(1f, 56.7f);
        Entry entry3 = new Entry(2f, 60.5f);
        Entry entry4 = new Entry(3f, 63.5f);
        Entry entry5 = new Entry(4f, 66f);
        Entry entry6 = new Entry(5f, 68.1f);
        Entry entry7 = new Entry(6f, 69.8f);
        Entry entry8 = new Entry(7f, 71.4f);
        Entry entry9 = new Entry(8f, 72.9f);
        Entry entry10 = new Entry(9f, 74.3f);
        Entry entry11 = new Entry(10f, 74.6f);
        Entry entry12 = new Entry(11f, 77f);
        Entry entry13 = new Entry(12f, 78.2f);
        Entry entry14 = new Entry(13f, 79.4f);
        Entry entry15 = new Entry(14f, 80.6f);
        Entry entry16 = new Entry(15f, 81.8f);
        Entry entry17 = new Entry(16f, 82.9f);
        Entry entry18 = new Entry(17f, 84f);
        Entry entry19 = new Entry(18f, 85.1f);
        Entry entry20 = new Entry(19f, 86.1f);
        Entry entry21 = new Entry(20f, 87.1f);
        Entry entry22 = new Entry(21f, 88.1f);
        Entry entry23 = new Entry(22f, 89.1f);
        Entry entry24 = new Entry(23f, 90.0f);
        Entry entry25 = new Entry(24f, 91f);

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

        Entry entry1 = new Entry(0f, 53.4f);
        Entry entry2 = new Entry(1f, 58.4f);
        Entry entry3 = new Entry(2f, 62.2f);
        Entry entry4 = new Entry(3f, 65.3f);
        Entry entry5 = new Entry(4f, 67.8f);
        Entry entry6 = new Entry(5f, 69.9f);
        Entry entry7 = new Entry(6f, 71.6f);
        Entry entry8 = new Entry(7f, 73.2f);
        Entry entry9 = new Entry(8f, 74.7f);
        Entry entry10 = new Entry(9f, 76.2f);
        Entry entry11 = new Entry(10f, 77.6f);
        Entry entry12 = new Entry(11f, 78.9f);
        Entry entry13 = new Entry(12f, 80.2f);
        Entry entry14 = new Entry(13f, 81.5f);
        Entry entry15 = new Entry(14f, 82.7f);
        Entry entry16 = new Entry(15f, 83.9f);
        Entry entry17 = new Entry(16f, 85.1f);
        Entry entry18 = new Entry(17f, 86.2f);
        Entry entry19 = new Entry(18f, 87.3f);
        Entry entry20 = new Entry(19f, 88.4f);
        Entry entry21 = new Entry(20f, 89.5f);
        Entry entry22 = new Entry(21f, 90.5f);
        Entry entry23 = new Entry(22f, 91.6f);
        Entry entry24 = new Entry(23f, 92.6f);
        Entry entry25 = new Entry(24f, 93.6f);

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

    //2-5y(undone)
    public LineDataSet negative3SDB(){
        final ArrayList<Entry> negative3sd = new ArrayList<>();
        LineDataSet neg3data;

        Entry entry1 = new Entry(0f, 82.1f);
        Entry entry2 = new Entry(1f, 82.8f);
        Entry entry3 = new Entry(2f, 84.2f);
        Entry entry4 = new Entry(3f, 85.5f);
        Entry entry5 = new Entry(4f, 86.8f);
        Entry entry6 = new Entry(5f, 88f);
        Entry entry7 = new Entry(6f, 89.1f);
        Entry entry8 = new Entry(7f, 90.2f);
        Entry entry9 = new Entry(8f, 91.3f);
        Entry entry10 = new Entry(9f, 92.4f);
        Entry entry11 = new Entry(10f, 93.4f);
        Entry entry12 = new Entry(11f, 94.4f);
        Entry entry13 = new Entry(12f, 95.4f);
        Entry entry14 = new Entry(13f, 96.4f);
        Entry entry15 = new Entry(14f, 97.4f);
        Entry entry16 = new Entry(15f, 98.4f);
        Entry entry17 = new Entry(16f, 99.3f);
        Entry entry18 = new Entry(17f, 100.3f);
        Entry entry19 = new Entry(18f, 101.2f);

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

        Entry entry1 = new Entry(0f, 84.6f);
        Entry entry2 = new Entry(1f, 85.5f);
        Entry entry3 = new Entry(2f, 87f);
        Entry entry4 = new Entry(3f, 88.4f);
        Entry entry5 = new Entry(4f, 89.7f);
        Entry entry6 = new Entry(5f, 91f);
        Entry entry7 = new Entry(6f, 92.2f);
        Entry entry8 = new Entry(7f, 93.4f);
        Entry entry9 = new Entry(8f, 94.6f);
        Entry entry10 = new Entry(9f, 95.7f);
        Entry entry11 = new Entry(10f, 96.8f);
        Entry entry12 = new Entry(11f, 97.9f);
        Entry entry13 = new Entry(12f, 99f);
        Entry entry14 = new Entry(13f, 100f);
        Entry entry15 = new Entry(14f, 101.1f);
        Entry entry16 = new Entry(15f, 102.1f);
        Entry entry17 = new Entry(16f, 103.1f);
        Entry entry18 = new Entry(17f, 104.1f);
        Entry entry19 = new Entry(18f, 105.2f);

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

        Entry entry1 = new Entry(0f, 87.8f);
        Entry entry2 = new Entry(1f, 88.8f);
        Entry entry3 = new Entry(2f, 90.4f);
        Entry entry4 = new Entry(3f, 91.9f);
        Entry entry5 = new Entry(4f, 93.4f);
        Entry entry6 = new Entry(5f, 94.8f);
        Entry entry7 = new Entry(6f, 96.1f);
        Entry entry8 = new Entry(7f, 97.4f);
        Entry entry9 = new Entry(8f, 98.6f);
        Entry entry10 = new Entry(9f, 99.9f);
        Entry entry11 = new Entry(10f, 101f);
        Entry entry12 = new Entry(11f, 102.2f);
        Entry entry13 = new Entry(12f, 103.3f);
        Entry entry14 = new Entry(13f, 104.4f);
        Entry entry15 = new Entry(14f, 105.6f);
        Entry entry16 = new Entry(15f, 106.7f);
        Entry entry17 = new Entry(16f, 107.8f);
        Entry entry18 = new Entry(17f, 108.9f);
        Entry entry19 = new Entry(18f, 110f);

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

        Entry entry1 = new Entry(0f, 91f);
        Entry entry2 = new Entry(1f, 92.1f);
        Entry entry3 = new Entry(2f, 93.8f);
        Entry entry4 = new Entry(3f, 95.5f);
        Entry entry5 = new Entry(4f, 97f);
        Entry entry6 = new Entry(5f, 98.5f);
        Entry entry7 = new Entry(6f, 99.9f);
        Entry entry8 = new Entry(7f, 101.3f);
        Entry entry9 = new Entry(8f, 102.7f);
        Entry entry10 = new Entry(9f, 104f);
        Entry entry11 = new Entry(10f, 105.2f);
        Entry entry12 = new Entry(11f, 106.5f);
        Entry entry13 = new Entry(12f, 107.7f);
        Entry entry14 = new Entry(13f, 108.9f);
        Entry entry15 = new Entry(14f, 110.1f);
        Entry entry16 = new Entry(15f, 111.2f);
        Entry entry17 = new Entry(16f, 112.4f);
        Entry entry18 = new Entry(17f, 113.6f);
        Entry entry19 = new Entry(18f, 114.8f);

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

        Entry entry1 = new Entry(0f, 93.6f);
        Entry entry2 = new Entry(1f, 94.8f);
        Entry entry3 = new Entry(2f, 96.6f);
        Entry entry4 = new Entry(3f, 98.3f);
        Entry entry5 = new Entry(4f, 100f);
        Entry entry6 = new Entry(5f, 101.5f);
        Entry entry7 = new Entry(6f, 103.1f);
        Entry entry8 = new Entry(7f, 104.5f);
        Entry entry9 = new Entry(8f, 105.9f);
        Entry entry10 = new Entry(9f, 107.3f);
        Entry entry11 = new Entry(10f, 108.6f);
        Entry entry12 = new Entry(11f, 109.9f);
        Entry entry13 = new Entry(12f, 111.2f);
        Entry entry14 = new Entry(13f, 112.5f);
        Entry entry15 = new Entry(14f, 113.7f);
        Entry entry16 = new Entry(15f, 115f);
        Entry entry17 = new Entry(16f, 116.2f);
        Entry entry18 = new Entry(17f, 117.4f);
        Entry entry19 = new Entry(18f, 118.7f);

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

        Entry entry1 = new Entry(0f, 45.6f);
        Entry entry2 = new Entry(1f, 50.0f);
        Entry entry3 = new Entry(2f, 53.2f);
        Entry entry4 = new Entry(3f, 55.8f);
        Entry entry5 = new Entry(4f, 58.0f);
        Entry entry6 = new Entry(5f, 59.9f);
        Entry entry7 = new Entry(6f, 61.5f);
        Entry entry8 = new Entry(7f, 62.9f);
        Entry entry9 = new Entry(8f, 64.3f);
        Entry entry10 = new Entry(9f, 65.6f);
        Entry entry11 = new Entry(10f, 66.8f);
        Entry entry12 = new Entry(11f, 68.0f);
        Entry entry13 = new Entry(12f, 69.2f);
        Entry entry14 = new Entry(13f, 70.3f);
        Entry entry15 = new Entry(14f, 71.3f);
        Entry entry16 = new Entry(15f, 72.4f);
        Entry entry17 = new Entry(16f, 73.3f);
        Entry entry18 = new Entry(17f, 74.3f);
        Entry entry19 = new Entry(18f, 75.2f);
        Entry entry20 = new Entry(19f, 76.2f);
        Entry entry21 = new Entry(20f, 77.0f);
        Entry entry22 = new Entry(21f, 77.9f);
        Entry entry23 = new Entry(22f, 78.7f);
        Entry entry24 = new Entry(23f, 79.6f);
        Entry entry25 = new Entry(24f, 80.3f);

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

        Entry entry1 = new Entry(0f, 47.2f);
        Entry entry2 = new Entry(1f, 51.7f);
        Entry entry3 = new Entry(2f, 55.0f);
        Entry entry4 = new Entry(3f, 57.6f);
        Entry entry5 = new Entry(4f, 59.8f);
        Entry entry6 = new Entry(5f, 61.7f);
        Entry entry7 = new Entry(6f, 63.4f);
        Entry entry8 = new Entry(7f, 64.9f);
        Entry entry9 = new Entry(8f, 66.3f);
        Entry entry10 = new Entry(9f, 67.6f);
        Entry entry11 = new Entry(10f, 68.9f);
        Entry entry12 = new Entry(11f, 70.2f);
        Entry entry13 = new Entry(12f, 71.3f);
        Entry entry14 = new Entry(13f, 72.5f);
        Entry entry15 = new Entry(14f, 73.6f);
        Entry entry16 = new Entry(15f, 74.7f);
        Entry entry17 = new Entry(16f, 76.7f);
        Entry entry18 = new Entry(17f, 77.7f);
        Entry entry19 = new Entry(18f, 77.8f);
        Entry entry20 = new Entry(19f, 78.7f);
        Entry entry21 = new Entry(20f, 79.6f);
        Entry entry22 = new Entry(21f, 80.5f);
        Entry entry23 = new Entry(22f, 81.4f);
        Entry entry24 = new Entry(23f, 82.2f);
        Entry entry25 = new Entry(24f, 83.1f);

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

        Entry entry1 = new Entry(0f, 49.1f);
        Entry entry2 = new Entry(1f, 53.7f);
        Entry entry3 = new Entry(2f, 57.1f);
        Entry entry4 = new Entry(3f, 59.8f);
        Entry entry5 = new Entry(4f, 62.1f);
        Entry entry6 = new Entry(5f, 64.0f);
        Entry entry7 = new Entry(6f, 65.7f);
        Entry entry8 = new Entry(7f, 67.3f);
        Entry entry9 = new Entry(8f, 68.7f);
        Entry entry10 = new Entry(9f, 70.1f);
        Entry entry11 = new Entry(10f, 71.5f);
        Entry entry12 = new Entry(11f, 72.8f);
        Entry entry13 = new Entry(12f, 74.0f);
        Entry entry14 = new Entry(13f, 75.2f);
        Entry entry15 = new Entry(14f, 76.4f);
        Entry entry16 = new Entry(15f, 77.5f);
        Entry entry17 = new Entry(16f, 78.6f);
        Entry entry18 = new Entry(17f, 79.7f);
        Entry entry19 = new Entry(18f, 80.7f);
        Entry entry20 = new Entry(19f, 81.7f);
        Entry entry21 = new Entry(20f, 82.7f);
        Entry entry22 = new Entry(21f, 83.7f);
        Entry entry23 = new Entry(22f, 84.6f);
        Entry entry24 = new Entry(23f, 85.5f);
        Entry entry25 = new Entry(24f, 86.4f);

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

        Entry entry1 = new Entry(0f, 51.1f);
        Entry entry2 = new Entry(1f, 55.7f);
        Entry entry3 = new Entry(2f, 59.2f);
        Entry entry4 = new Entry(3f, 62.0f);
        Entry entry5 = new Entry(4f, 64.3f);
        Entry entry6 = new Entry(5f, 66.3f);
        Entry entry7 = new Entry(6f, 68.1f);
        Entry entry8 = new Entry(7f, 69.7f);
        Entry entry9 = new Entry(8f, 71.2f);
        Entry entry10 = new Entry(9f, 72.6f);
        Entry entry11 = new Entry(10f, 74.0f);
        Entry entry12 = new Entry(11f, 75.4f);
        Entry entry13 = new Entry(12f, 76.7f);
        Entry entry14 = new Entry(13f, 77.9f);
        Entry entry15 = new Entry(14f, 79.2f);
        Entry entry16 = new Entry(15f, 80.3f);
        Entry entry17 = new Entry(16f, 81.5f);
        Entry entry18 = new Entry(17f, 82.6f);
        Entry entry19 = new Entry(18f, 83.7f);
        Entry entry20 = new Entry(19f, 84.8f);
        Entry entry21 = new Entry(20f, 85.8f);
        Entry entry22 = new Entry(21f, 86.8f);
        Entry entry23 = new Entry(22f, 87.8f);
        Entry entry24 = new Entry(23f, 88.8f);
        Entry entry25 = new Entry(24f, 89.8f);

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

        Entry entry1 = new Entry(0f, 52.7f);
        Entry entry2 = new Entry(1f, 57.4f);
        Entry entry3 = new Entry(2f, 60.9f);
        Entry entry4 = new Entry(3f, 63.8f);
        Entry entry5 = new Entry(4f, 66.2f);
        Entry entry6 = new Entry(5f, 68.2f);
        Entry entry7 = new Entry(6f, 70.0f);
        Entry entry8 = new Entry(7f, 71.6f);
        Entry entry9 = new Entry(8f, 73.2f);
        Entry entry10 = new Entry(9f, 74.7f);
        Entry entry11 = new Entry(10f, 76.1f);
        Entry entry12 = new Entry(11f, 77.5f);
        Entry entry13 = new Entry(12f, 78.9f);
        Entry entry14 = new Entry(13f, 80.2f);
        Entry entry15 = new Entry(14f, 81.4f);
        Entry entry16 = new Entry(15f, 82.7f);
        Entry entry17 = new Entry(16f, 83.9f);
        Entry entry18 = new Entry(17f, 85.0f);
        Entry entry19 = new Entry(18f, 86.2f);
        Entry entry20 = new Entry(19f, 87.3f);
        Entry entry21 = new Entry(20f, 88.4f);
        Entry entry22 = new Entry(21f, 89.4f);
        Entry entry23 = new Entry(22f, 90.5f);
        Entry entry24 = new Entry(23f, 91.5f);
        Entry entry25 = new Entry(24f, 92.5f);

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

    //2-5y(undone)
    public LineDataSet negative3SDBF(){
        final ArrayList<Entry> negative3sd = new ArrayList<>();
        LineDataSet neg3data;

        Entry entry1 = new Entry(0f, 80.3f);
        Entry entry2 = new Entry(1f, 81.2f);
        Entry entry3 = new Entry(2f, 82.6f);
        Entry entry4 = new Entry(3f, 84.0f);
        Entry entry5 = new Entry(4f, 85.4f);
        Entry entry6 = new Entry(5f, 86.7f);
        Entry entry7 = new Entry(6f, 87.9f);
        Entry entry8 = new Entry(7f, 89.1f);
        Entry entry9 = new Entry(8f, 90.3f);
        Entry entry10 = new Entry(9f, 91.4f);
        Entry entry11 = new Entry(10f, 92.5f);
        Entry entry12 = new Entry(11f, 93.6f);
        Entry entry13 = new Entry(12f, 94.6f);
        Entry entry14 = new Entry(13f, 95.7f);
        Entry entry15 = new Entry(14f, 96.7f);
        Entry entry16 = new Entry(15f, 97.6f);
        Entry entry17 = new Entry(16f, 98.6f);
        Entry entry18 = new Entry(17f, 99.6f);
        Entry entry19 = new Entry(18f, 100.5f);

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

        Entry entry1 = new Entry(0f, 83.1f);
        Entry entry2 = new Entry(1f, 84.0f);
        Entry entry3 = new Entry(2f, 85.5f);
        Entry entry4 = new Entry(3f, 87.0f);
        Entry entry5 = new Entry(4f, 88.4f);
        Entry entry6 = new Entry(5f, 89.8f);
        Entry entry7 = new Entry(6f, 91.1f);
        Entry entry8 = new Entry(7f, 92.4f);
        Entry entry9 = new Entry(8f, 93.6f);
        Entry entry10 = new Entry(9f, 94.8f);
        Entry entry11 = new Entry(10f, 96.0f);
        Entry entry12 = new Entry(11f, 97.2f);
        Entry entry13 = new Entry(12f, 98.3f);
        Entry entry14 = new Entry(13f, 99.4f);
        Entry entry15 = new Entry(14f, 100.4f);
        Entry entry16 = new Entry(15f, 101.5f);
        Entry entry17 = new Entry(16f, 102.5f);
        Entry entry18 = new Entry(17f, 103.5f);
        Entry entry19 = new Entry(18f, 104.5f);

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

        Entry entry1 = new Entry(0f, 86.4f);
        Entry entry2 = new Entry(1f, 87.4f);
        Entry entry3 = new Entry(2f, 89.1f);
        Entry entry4 = new Entry(3f, 90.7f);
        Entry entry5 = new Entry(4f, 92.2f);
        Entry entry6 = new Entry(5f, 93.6f);
        Entry entry7 = new Entry(6f, 95.1f);
        Entry entry8 = new Entry(7f, 96.4f);
        Entry entry9 = new Entry(8f, 97.7f);
        Entry entry10 = new Entry(9f, 99.0f);
        Entry entry11 = new Entry(10f, 100.3f);
        Entry entry12 = new Entry(11f, 101.5f);
        Entry entry13 = new Entry(12f, 102.7f);
        Entry entry14 = new Entry(13f, 103.9f);
        Entry entry15 = new Entry(14f, 105.0f);
        Entry entry16 = new Entry(15f, 106.2f);
        Entry entry17 = new Entry(16f, 107.3f);
        Entry entry18 = new Entry(17f, 108.4f);
        Entry entry19 = new Entry(18f, 109.4f);

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

        Entry entry1 = new Entry(0f, 89.8f);
        Entry entry2 = new Entry(1f, 90.9f);
        Entry entry3 = new Entry(2f, 92.7f);
        Entry entry4 = new Entry(3f, 94.3f);
        Entry entry5 = new Entry(4f, 95.9f);
        Entry entry6 = new Entry(5f, 97.5f);
        Entry entry7 = new Entry(6f, 99.0f);
        Entry entry8 = new Entry(7f, 100.5f);
        Entry entry9 = new Entry(8f, 101.9f);
        Entry entry10 = new Entry(9f, 103.3f);
        Entry entry11 = new Entry(10f, 104.6f);
        Entry entry12 = new Entry(11f, 105.9f);
        Entry entry13 = new Entry(12f, 107.2f);
        Entry entry14 = new Entry(13f, 108.4f);
        Entry entry15 = new Entry(14f, 109.7f);
        Entry entry16 = new Entry(15f, 110.9f);
        Entry entry17 = new Entry(16f, 112.1f);
        Entry entry18 = new Entry(17f, 113.2f);
        Entry entry19 = new Entry(18f, 114.4f);

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

        Entry entry1 = new Entry(0f, 92.5f);
        Entry entry2 = new Entry(1f, 93.7f);
        Entry entry3 = new Entry(2f, 95.6f);
        Entry entry4 = new Entry(3f, 97.3f);
        Entry entry5 = new Entry(4f, 99.0f);
        Entry entry6 = new Entry(5f, 100.6f);
        Entry entry7 = new Entry(6f, 102.2f);
        Entry entry8 = new Entry(7f, 103.7f);
        Entry entry9 = new Entry(8f, 105.2f);
        Entry entry10 = new Entry(9f, 106.7f);
        Entry entry11 = new Entry(10f, 108.1f);
        Entry entry12 = new Entry(11f, 109.5f);
        Entry entry13 = new Entry(12f, 110.8f);
        Entry entry14 = new Entry(13f, 112.1f);
        Entry entry15 = new Entry(14f, 113.4f);
        Entry entry16 = new Entry(15f, 114.7f);
        Entry entry17 = new Entry(16f, 116.0f);
        Entry entry18 = new Entry(17f, 117.2f);
        Entry entry19 = new Entry(18f, 118.4f);

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
        zerosdF = zeroSD();
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
                                                    newborn = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 1) {
                                                    firstMonth = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 2) {
                                                    secondMonth = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 3) {
                                                    thirdMonth = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 4) {
                                                    forthMonth = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 5) {
                                                    fifthMonth = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 6) {
                                                    sixthMonth = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 7) {
                                                    seventhMonth = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 8) {
                                                    eightMonth = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 9) {
                                                    ninethMonth = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 10) {
                                                    tenthMonth = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 11) {
                                                    eleventhMonth = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 0) {
                                                    oneyear = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 1) {
                                                    oneyearone = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 2) {
                                                    oneyeartwo = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 3) {
                                                    oneyearthree = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 4) {
                                                    oneyearfour = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 5) {
                                                    oneyearfive = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 6) {
                                                    oneyearsix = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 7) {
                                                    oneyearseven = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 8) {
                                                    oneyeareight = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 9) {
                                                    oneyearnine = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 10) {
                                                    oneyearten = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 11) {
                                                    oneyeareleven = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 2 && totalmonth == 0) {
                                                    twoyear = Float.parseFloat(measurement.getHeight());
                                                }

                                                final ArrayList<Entry> heightList = new ArrayList<>();

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

                                                heightList.add(entry1);
                                                heightList.add(entry2);
                                                heightList.add(entry3);
                                                heightList.add(entry4);
                                                heightList.add(entry5);
                                                heightList.add(entry6);
                                                heightList.add(entry7);
                                                heightList.add(entry8);
                                                heightList.add(entry9);
                                                heightList.add(entry10);
                                                heightList.add(entry11);
                                                heightList.add(entry12);
                                                heightList.add(entry13);
                                                heightList.add(entry14);
                                                heightList.add(entry15);
                                                heightList.add(entry16);
                                                heightList.add(entry17);
                                                heightList.add(entry18);
                                                heightList.add(entry19);
                                                heightList.add(entry20);
                                                heightList.add(entry21);
                                                heightList.add(entry22);
                                                heightList.add(entry23);
                                                heightList.add(entry24);
                                                heightList.add(entry25);

                                                heightData = new LineDataSet(heightList, "Baby Height");
                                                heightData.setDrawCircles(false);
                                                heightData.setDrawValues(true);
                                                heightData.setColor(Color.BLUE);
                                            }
                                        }

                                        LineData data = new LineData(negative3sd, negative2sd, zerosd, positive2sd, positive3sd, heightData);
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
                                                    newborn = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 1) {
                                                    firstMonth = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 2) {
                                                    secondMonth = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 3) {
                                                    thirdMonth = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 4) {
                                                    forthMonth = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 5) {
                                                    fifthMonth = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 6) {
                                                    sixthMonth = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 7) {
                                                    seventhMonth = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 8) {
                                                    eightMonth = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 9) {
                                                    ninethMonth = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 10) {
                                                    tenthMonth = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 0 && totalmonth == 11) {
                                                    eleventhMonth = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 0) {
                                                    oneyear = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 1) {
                                                    oneyearone = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 2) {
                                                    oneyeartwo = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 3) {
                                                    oneyearthree = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 4) {
                                                    oneyearfour = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 5) {
                                                    oneyearfive = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 6) {
                                                    oneyearsix = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 7) {
                                                    oneyearseven = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 8) {
                                                    oneyeareight = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 9) {
                                                    oneyearnine = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 10) {
                                                    oneyearten = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 1 && totalmonth == 11) {
                                                    oneyeareleven = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 2 && totalmonth == 0) {
                                                    twoyear = Float.parseFloat(measurement.getHeight());
                                                }

                                                final ArrayList<Entry> heightList = new ArrayList<>();

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

                                                heightList.add(entry1);
                                                heightList.add(entry2);
                                                heightList.add(entry3);
                                                heightList.add(entry4);
                                                heightList.add(entry5);
                                                heightList.add(entry6);
                                                heightList.add(entry7);
                                                heightList.add(entry8);
                                                heightList.add(entry9);
                                                heightList.add(entry10);
                                                heightList.add(entry11);
                                                heightList.add(entry12);
                                                heightList.add(entry13);
                                                heightList.add(entry14);
                                                heightList.add(entry15);
                                                heightList.add(entry16);
                                                heightList.add(entry17);
                                                heightList.add(entry18);
                                                heightList.add(entry19);
                                                heightList.add(entry20);
                                                heightList.add(entry21);
                                                heightList.add(entry22);
                                                heightList.add(entry23);
                                                heightList.add(entry24);
                                                heightList.add(entry25);

                                                heightDataF = new LineDataSet(heightList, "Baby Height");
                                                heightDataF.setDrawCircles(false);
                                                heightDataF.setDrawValues(true);
                                                heightDataF.setColor(Color.BLUE);
                                            }
                                        }

                                        LineData data = new LineData(negative3sdF, negative2sdF, zerosdF, positive2sdF, positive3sdF, heightDataF);
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
                                                    twoyear = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 2 && (totalmonth == 2 || totalmonth == 3)) {
                                                    twoyeartwo = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 2 && (totalmonth == 4 || totalmonth == 5)) {
                                                    twoyearfour = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 2 && (totalmonth == 6 || totalmonth == 7)) {
                                                    twoyearsix = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 2 && (totalmonth == 8 || totalmonth == 9)) {
                                                    twoyeareight = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 2 && (totalmonth == 10 || totalmonth == 11 )) {
                                                    twoyearten = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 0 || totalmonth == 1 )) {
                                                    threeyear = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 2 || totalmonth == 3 )) {
                                                    threeyeartwo = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 4 || totalmonth == 5 )) {
                                                    threeyearfour = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 6 || totalmonth == 7 )) {
                                                    threeyearsix = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 8 || totalmonth == 9 )) {
                                                    threeyeareight = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 10 || totalmonth == 11 )) {
                                                    threeyearten = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 0 || totalmonth == 1 )) {
                                                    fouryear = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 2 || totalmonth == 3 )) {
                                                    fouryeartwo = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 4 || totalmonth == 5 )) {
                                                    fouryearfour = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 6 || totalmonth == 7 )) {
                                                    fouryearsix = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 8 || totalmonth == 9 )) {
                                                    fouryeareight = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 10 || totalmonth == 11 )) {
                                                    fouryearten = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 5 && (totalmonth == 0 || totalmonth == 1 )) {
                                                    fiveyear = Float.parseFloat(measurement.getHeight());
                                                }

                                                final ArrayList<Entry> heightList = new ArrayList<>();

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

                                                heightList.add(entry1);
                                                heightList.add(entry2);
                                                heightList.add(entry3);
                                                heightList.add(entry4);
                                                heightList.add(entry5);
                                                heightList.add(entry6);
                                                heightList.add(entry7);
                                                heightList.add(entry8);
                                                heightList.add(entry9);
                                                heightList.add(entry10);
                                                heightList.add(entry11);
                                                heightList.add(entry12);
                                                heightList.add(entry13);
                                                heightList.add(entry14);
                                                heightList.add(entry15);
                                                heightList.add(entry16);
                                                heightList.add(entry17);
                                                heightList.add(entry18);
                                                heightList.add(entry19);

                                                heightDataB = new LineDataSet(heightList, "Baby Height");
                                                heightDataB.setDrawCircles(false);
                                                heightDataB.setDrawValues(true);
                                                heightDataB.setColor(Color.BLUE);
                                            }
                                        }

                                        LineData dataB = new LineData(negative3sdB, negative2sdB, zerosdB, positive2sdB, positive3sdB, heightDataB);
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
                                                    twoyear = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 2 && (totalmonth == 2 || totalmonth == 3)) {
                                                    twoyeartwo = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 2 && (totalmonth == 4 || totalmonth == 5)) {
                                                    twoyearfour = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 2 && (totalmonth == 6 || totalmonth == 7)) {
                                                    twoyearsix = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 2 && (totalmonth == 8 || totalmonth == 9)) {
                                                    twoyeareight = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 2 && (totalmonth == 10 || totalmonth == 11 )) {
                                                    twoyearten = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 0 || totalmonth == 1 )) {
                                                    threeyear = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 2 || totalmonth == 3 )) {
                                                    threeyeartwo = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 4 || totalmonth == 5 )) {
                                                    threeyearfour = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 6 || totalmonth == 7 )) {
                                                    threeyearsix = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 8 || totalmonth == 9 )) {
                                                    threeyeareight = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 3 && (totalmonth == 10 || totalmonth == 11 )) {
                                                    threeyearten = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 0 || totalmonth == 1 )) {
                                                    fouryear = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 2 || totalmonth == 3 )) {
                                                    fouryeartwo = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 4 || totalmonth == 5 )) {
                                                    fouryearfour = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 6 || totalmonth == 7 )) {
                                                    fouryearsix = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 8 || totalmonth == 9 )) {
                                                    fouryeareight = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 4 && (totalmonth == 10 || totalmonth == 11 )) {
                                                    fouryearten = Float.parseFloat(measurement.getHeight());
                                                }

                                                if (totalyear == 5 && (totalmonth == 0 || totalmonth == 1 )) {
                                                    fiveyear = Float.parseFloat(measurement.getHeight());
                                                }

                                                final ArrayList<Entry> heightList = new ArrayList<>();

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

                                                heightList.add(entry1);
                                                heightList.add(entry2);
                                                heightList.add(entry3);
                                                heightList.add(entry4);
                                                heightList.add(entry5);
                                                heightList.add(entry6);
                                                heightList.add(entry7);
                                                heightList.add(entry8);
                                                heightList.add(entry9);
                                                heightList.add(entry10);
                                                heightList.add(entry11);
                                                heightList.add(entry12);
                                                heightList.add(entry13);
                                                heightList.add(entry14);
                                                heightList.add(entry15);
                                                heightList.add(entry16);
                                                heightList.add(entry17);
                                                heightList.add(entry18);
                                                heightList.add(entry19);

                                                heightDataBF = new LineDataSet(heightList, "Baby Height");
                                                heightDataBF.setDrawCircles(false);
                                                heightDataBF.setDrawValues(true);
                                                heightDataBF.setColor(Color.BLUE);
                                            }
                                        }

                                        LineData dataB = new LineData(negative3sdBF, negative2sdBF, zerosdBF, positive2sdBF, positive3sdBF, heightDataBF);
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
