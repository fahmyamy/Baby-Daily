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

public class MeasurementStaFragHead extends Fragment {
    private View view;
    private String bId;
    private LineChart chartA, chartB;

    private DatabaseReference table_baby, table_measure, table_measureB;
    private Query queryBaby, queryBabyB;

    private LineDataSet negative3sd, negative2sd, zerosd, positive2sd, positive3sd;
    private LineDataSet negative3sdB, negative2sdB, zerosdB, positive2sdB, positive3sdB;
    private LineDataSet negative3sdF, negative2sdF, zerosdF, positive2sdF, positive3sdF;
    private LineDataSet negative3sdBF, negative2sdBF, zerosdBF, positive2sdBF, positive3sdBF;
    private LineDataSet headData, headDataB, headDataF, headDataBF;
    private int totalday = 0, totalmonth = 0, totalyear = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.measurement_head_fragment, container, false);
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

        Entry entry1 = new Entry(0f, 32.1f);
        Entry entry2 = new Entry(1f, 35.1f);
        Entry entry3 = new Entry(2f, 36.9f);
        Entry entry4 = new Entry(3f, 38.3f);
        Entry entry5 = new Entry(4f, 39.4f);
        Entry entry6 = new Entry(5f, 40.3f);
        Entry entry7 = new Entry(6f, 41f);
        Entry entry8 = new Entry(7f, 41.7f);
        Entry entry9 = new Entry(8f, 42.2f);
        Entry entry10 = new Entry(9f, 42.6f);
        Entry entry11 = new Entry(10f, 43f);
        Entry entry12 = new Entry(11f, 43.4f);
        Entry entry13 = new Entry(12f, 43.6f);
        Entry entry14 = new Entry(13f, 43.9f);
        Entry entry15 = new Entry(14f, 44.1f);
        Entry entry16 = new Entry(15f, 44.3f);
        Entry entry17 = new Entry(16f, 44.5f);
        Entry entry18 = new Entry(17f, 44.7f);
        Entry entry19 = new Entry(18f, 44.9f);
        Entry entry20 = new Entry(19f, 45f);
        Entry entry21 = new Entry(20f, 45.2f);
        Entry entry22 = new Entry(21f, 45.3f);
        Entry entry23 = new Entry(22f, 45.4f);
        Entry entry24 = new Entry(23f, 45.6f);
        Entry entry25 = new Entry(24f, 45.7f);

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

        Entry entry1 = new Entry(0f, 33.1f);
        Entry entry2 = new Entry(1f, 36.1f);
        Entry entry3 = new Entry(2f, 37.9f);
        Entry entry4 = new Entry(3f, 39.3f);
        Entry entry5 = new Entry(4f, 40.4f);
        Entry entry6 = new Entry(5f, 41.3f);
        Entry entry7 = new Entry(6f, 42.1f);
        Entry entry8 = new Entry(7f, 42.7f);
        Entry entry9 = new Entry(8f, 43.2f);
        Entry entry10 = new Entry(9f, 43.7f);
        Entry entry11 = new Entry(10f, 44.1f);
        Entry entry12 = new Entry(11f, 44.4f);
        Entry entry13 = new Entry(12f, 44.7f);
        Entry entry14 = new Entry(13f, 45f);
        Entry entry15 = new Entry(14f, 45.2f);
        Entry entry16 = new Entry(15f, 45.5f);
        Entry entry17 = new Entry(16f, 45.6f);
        Entry entry18 = new Entry(17f, 45.8f);
        Entry entry19 = new Entry(18f, 46f);
        Entry entry20 = new Entry(19f, 46.2f);
        Entry entry21 = new Entry(20f, 46.3f);
        Entry entry22 = new Entry(21f, 46.4f);
        Entry entry23 = new Entry(22f, 46.6f);
        Entry entry24 = new Entry(23f, 46.7f);
        Entry entry25 = new Entry(24f, 46.8f);

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

        Entry entry1 = new Entry(0f, 34.5f);
        Entry entry2 = new Entry(1f, 37.3f);
        Entry entry3 = new Entry(2f, 39.1f);
        Entry entry4 = new Entry(3f, 40.5f);
        Entry entry5 = new Entry(4f, 41.6f);
        Entry entry6 = new Entry(5f, 42.6f);
        Entry entry7 = new Entry(6f, 43.3f);
        Entry entry8 = new Entry(7f, 44f);
        Entry entry9 = new Entry(8f, 44.5f);
        Entry entry10 = new Entry(9f, 45f);
        Entry entry11 = new Entry(10f, 45.4f);
        Entry entry12 = new Entry(11f, 45.8f);
        Entry entry13 = new Entry(12f, 46.1f);
        Entry entry14 = new Entry(13f, 46.3f);
        Entry entry15 = new Entry(14f, 46.6f);
        Entry entry16 = new Entry(15f, 46.8f);
        Entry entry17 = new Entry(16f, 47f);
        Entry entry18 = new Entry(17f, 47.2f);
        Entry entry19 = new Entry(18f, 47.4f);
        Entry entry20 = new Entry(19f, 47.5f);
        Entry entry21 = new Entry(20f, 47.7f);
        Entry entry22 = new Entry(21f, 47.8f);
        Entry entry23 = new Entry(22f, 48f);
        Entry entry24 = new Entry(23f, 48.1f);
        Entry entry25 = new Entry(24f, 48.3f);

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

        Entry entry1 = new Entry(0f, 35.8f);
        Entry entry2 = new Entry(1f, 38.5f);
        Entry entry3 = new Entry(2f, 40.3f);
        Entry entry4 = new Entry(3f, 41.7f);
        Entry entry5 = new Entry(4f, 42.9f);
        Entry entry6 = new Entry(5f, 43.8f);
        Entry entry7 = new Entry(6f, 44.6f);
        Entry entry8 = new Entry(7f, 45.3f);
        Entry entry9 = new Entry(8f, 45.8f);
        Entry entry10 = new Entry(9f, 46.3f);
        Entry entry11 = new Entry(10f, 46.7f);
        Entry entry12 = new Entry(11f, 47.1f);
        Entry entry13 = new Entry(12f, 47.4f);
        Entry entry14 = new Entry(13f, 47.7f);
        Entry entry15 = new Entry(14f, 47.9f);
        Entry entry16 = new Entry(15f, 48.2f);
        Entry entry17 = new Entry(16f, 48.4f);
        Entry entry18 = new Entry(17f, 48.6f);
        Entry entry19 = new Entry(18f, 48.7f);
        Entry entry20 = new Entry(19f, 48.9f);
        Entry entry21 = new Entry(20f, 49.1f);
        Entry entry22 = new Entry(21f, 49.2f);
        Entry entry23 = new Entry(22f, 49.4f);
        Entry entry24 = new Entry(23f, 49.5f);
        Entry entry25 = new Entry(24f, 49.7f);

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

        Entry entry1 = new Entry(0f, 36.9f);
        Entry entry2 = new Entry(1f, 39.5f);
        Entry entry3 = new Entry(2f, 41.3f);
        Entry entry4 = new Entry(3f, 42.7f);
        Entry entry5 = new Entry(4f, 43.9f);
        Entry entry6 = new Entry(5f, 44.8f);
        Entry entry7 = new Entry(6f, 45.6f);
        Entry entry8 = new Entry(7f, 46.3f);
        Entry entry9 = new Entry(8f, 46.9f);
        Entry entry10 = new Entry(9f, 47.4f);
        Entry entry11 = new Entry(10f, 47.8f);
        Entry entry12 = new Entry(11f, 48.2f);
        Entry entry13 = new Entry(12f, 48.5f);
        Entry entry14 = new Entry(13f, 48.8f);
        Entry entry15 = new Entry(14f, 49f);
        Entry entry16 = new Entry(15f, 49.3f);
        Entry entry17 = new Entry(16f, 49.5f);
        Entry entry18 = new Entry(17f, 49.7f);
        Entry entry19 = new Entry(18f, 49.9f);
        Entry entry20 = new Entry(19f, 50f);
        Entry entry21 = new Entry(20f, 50.2f);
        Entry entry22 = new Entry(21f, 50.4f);
        Entry entry23 = new Entry(22f, 50.5f);
        Entry entry24 = new Entry(23f, 50.7f);
        Entry entry25 = new Entry(24f, 50.8f);

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

        Entry entry1 = new Entry(0f, 45.7f);
        Entry entry2 = new Entry(1f, 45.9f);
        Entry entry3 = new Entry(2f, 46.1f);
        Entry entry4 = new Entry(3f, 46.3f);
        Entry entry5 = new Entry(4f, 46.5f);
        Entry entry6 = new Entry(5f, 46.6f);
        Entry entry7 = new Entry(6f, 46.8f);
        Entry entry8 = new Entry(7f, 46.9f);
        Entry entry9 = new Entry(8f, 47f);
        Entry entry10 = new Entry(9f, 47.2f);
        Entry entry11 = new Entry(10f, 47.3f);
        Entry entry12 = new Entry(11f, 47.4f);
        Entry entry13 = new Entry(12f, 47.5f);
        Entry entry14 = new Entry(13f, 47.5f);
        Entry entry15 = new Entry(14f, 47.6f);
        Entry entry16 = new Entry(15f, 47.7f);
        Entry entry17 = new Entry(16f, 47.8f);
        Entry entry18 = new Entry(17f, 47.9f);
        Entry entry19 = new Entry(18f, 47.9f);

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

        Entry entry1 = new Entry(0f, 46.8f);
        Entry entry2 = new Entry(1f, 47.1f);
        Entry entry3 = new Entry(2f, 47.3f);
        Entry entry4 = new Entry(3f, 47.5f);
        Entry entry5 = new Entry(4f, 47.7f);
        Entry entry6 = new Entry(5f, 47.8f);
        Entry entry7 = new Entry(6f, 48f);
        Entry entry8 = new Entry(7f, 48.1f);
        Entry entry9 = new Entry(8f, 48.3f);
        Entry entry10 = new Entry(9f, 48.4f);
        Entry entry11 = new Entry(10f, 48.5f);
        Entry entry12 = new Entry(11f, 48.6f);
        Entry entry13 = new Entry(12f, 48.7f);
        Entry entry14 = new Entry(13f, 48.8f);
        Entry entry15 = new Entry(14f, 48.9f);
        Entry entry16 = new Entry(15f, 49f);
        Entry entry17 = new Entry(16f, 49f);
        Entry entry18 = new Entry(17f, 49.1f);
        Entry entry19 = new Entry(18f, 49.2f);

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

        Entry entry1 = new Entry(0f, 48.3f);
        Entry entry2 = new Entry(1f, 48.5f);
        Entry entry3 = new Entry(2f, 48.7f);
        Entry entry4 = new Entry(3f, 48.9f);
        Entry entry5 = new Entry(4f, 49.1f);
        Entry entry6 = new Entry(5f, 49.3f);
        Entry entry7 = new Entry(6f, 49.5f);
        Entry entry8 = new Entry(7f, 49.6f);
        Entry entry9 = new Entry(8f, 49.7f);
        Entry entry10 = new Entry(9f, 49.9f);
        Entry entry11 = new Entry(10f, 50f);
        Entry entry12 = new Entry(11f, 50.1f);
        Entry entry13 = new Entry(12f, 50.2f);
        Entry entry14 = new Entry(13f, 50.3f);
        Entry entry15 = new Entry(14f, 50.4f);
        Entry entry16 = new Entry(15f, 50.5f);
        Entry entry17 = new Entry(16f, 50.6f);
        Entry entry18 = new Entry(17f, 50.7f);
        Entry entry19 = new Entry(18f, 50.7f);

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

        Entry entry1 = new Entry(0f, 49.7f);
        Entry entry2 = new Entry(1f, 49.9f);
        Entry entry3 = new Entry(2f, 50.2f);
        Entry entry4 = new Entry(3f, 50.4f);
        Entry entry5 = new Entry(4f, 50.6f);
        Entry entry6 = new Entry(5f, 50.8f);
        Entry entry7 = new Entry(6f, 50.9f);
        Entry entry8 = new Entry(7f, 51.1f);
        Entry entry9 = new Entry(8f, 51.2f);
        Entry entry10 = new Entry(9f, 51.4f);
        Entry entry11 = new Entry(10f, 51.5f);
        Entry entry12 = new Entry(11f, 51.6f);
        Entry entry13 = new Entry(12f, 51.7f);
        Entry entry14 = new Entry(13f, 51.8f);
        Entry entry15 = new Entry(14f, 51.9f);
        Entry entry16 = new Entry(15f, 52f);
        Entry entry17 = new Entry(16f, 52.1f);
        Entry entry18 = new Entry(17f, 52.2f);
        Entry entry19 = new Entry(18f, 52.3f);

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

        Entry entry1 = new Entry(0f, 50.8f);
        Entry entry2 = new Entry(1f, 51.1f);
        Entry entry3 = new Entry(2f, 51.3f);
        Entry entry4 = new Entry(3f, 51.6f);
        Entry entry5 = new Entry(4f, 51.8f);
        Entry entry6 = new Entry(5f, 52f);
        Entry entry7 = new Entry(6f, 52.1f);
        Entry entry8 = new Entry(7f, 52.3f);
        Entry entry9 = new Entry(8f, 52.4f);
        Entry entry10 = new Entry(9f, 52.6f);
        Entry entry11 = new Entry(10f, 52.7f);
        Entry entry12 = new Entry(11f, 52.8f);
        Entry entry13 = new Entry(12f, 53f);
        Entry entry14 = new Entry(13f, 53.1f);
        Entry entry15 = new Entry(14f, 53.2f);
        Entry entry16 = new Entry(15f, 53.3f);
        Entry entry17 = new Entry(16f, 53.4f);
        Entry entry18 = new Entry(17f, 53.5f);
        Entry entry19 = new Entry(18f, 53.5f);

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

        Entry entry1 = new Entry(0f, 31.7f);
        Entry entry2 = new Entry(1f, 34.3f);
        Entry entry3 = new Entry(2f, 36.0f);
        Entry entry4 = new Entry(3f, 37.2f);
        Entry entry5 = new Entry(4f, 38.2f);
        Entry entry6 = new Entry(5f, 39.0f);
        Entry entry7 = new Entry(6f, 39.7f);
        Entry entry8 = new Entry(7f, 40.4f);
        Entry entry9 = new Entry(8f, 40.9f);
        Entry entry10 = new Entry(9f, 41.3f);
        Entry entry11 = new Entry(10f, 41.7f);
        Entry entry12 = new Entry(11f, 42.0f);
        Entry entry13 = new Entry(12f, 42.3f);
        Entry entry14 = new Entry(13f, 42.6f);
        Entry entry15 = new Entry(14f, 42.9f);
        Entry entry16 = new Entry(15f, 43.1f);
        Entry entry17 = new Entry(16f, 43.3f);
        Entry entry18 = new Entry(17f, 43.5f);
        Entry entry19 = new Entry(18f, 43.6f);
        Entry entry20 = new Entry(19f, 43.8f);
        Entry entry21 = new Entry(20f, 44.0f);
        Entry entry22 = new Entry(21f, 44.1f);
        Entry entry23 = new Entry(22f, 44.3f);
        Entry entry24 = new Entry(23f, 44.4f);
        Entry entry25 = new Entry(24f, 44.6f);

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

        Entry entry1 = new Entry(0f, 32.7f);
        Entry entry2 = new Entry(1f, 35.3f);
        Entry entry3 = new Entry(2f, 37.0f);
        Entry entry4 = new Entry(3f, 38.2f);
        Entry entry5 = new Entry(4f, 39.3f);
        Entry entry6 = new Entry(5f, 40.1f);
        Entry entry7 = new Entry(6f, 40.8f);
        Entry entry8 = new Entry(7f, 41.5f);
        Entry entry9 = new Entry(8f, 42.0f);
        Entry entry10 = new Entry(9f, 42.4f);
        Entry entry11 = new Entry(10f, 42.8f);
        Entry entry12 = new Entry(11f, 43.2f);
        Entry entry13 = new Entry(12f, 43.5f);
        Entry entry14 = new Entry(13f, 43.8f);
        Entry entry15 = new Entry(14f, 44.0f);
        Entry entry16 = new Entry(15f, 44.2f);
        Entry entry17 = new Entry(16f, 44.4f);
        Entry entry18 = new Entry(17f, 44.6f);
        Entry entry19 = new Entry(18f, 44.8f);
        Entry entry20 = new Entry(19f, 45.0f);
        Entry entry21 = new Entry(20f, 45.1f);
        Entry entry22 = new Entry(21f, 45.3f);
        Entry entry23 = new Entry(22f, 45.4f);
        Entry entry24 = new Entry(23f, 45.6f);
        Entry entry25 = new Entry(24f, 45.7f);

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

        Entry entry1 = new Entry(0f, 33.9f);
        Entry entry2 = new Entry(1f, 36.5f);
        Entry entry3 = new Entry(2f, 38.3f);
        Entry entry4 = new Entry(3f, 39.5f);
        Entry entry5 = new Entry(4f, 40.6f);
        Entry entry6 = new Entry(5f, 41.5f);
        Entry entry7 = new Entry(6f, 42.2f);
        Entry entry8 = new Entry(7f, 42.8f);
        Entry entry9 = new Entry(8f, 43.4f);
        Entry entry10 = new Entry(9f, 43.8f);
        Entry entry11 = new Entry(10f, 44.2f);
        Entry entry12 = new Entry(11f, 44.6f);
        Entry entry13 = new Entry(12f, 44.9f);
        Entry entry14 = new Entry(13f, 45.2f);
        Entry entry15 = new Entry(14f, 45.4f);
        Entry entry16 = new Entry(15f, 45.7f);
        Entry entry17 = new Entry(16f, 45.9f);
        Entry entry18 = new Entry(17f, 46.1f);
        Entry entry19 = new Entry(18f, 46.2f);
        Entry entry20 = new Entry(19f, 46.4f);
        Entry entry21 = new Entry(20f, 46.6f);
        Entry entry22 = new Entry(21f, 46.7f);
        Entry entry23 = new Entry(22f, 46.9f);
        Entry entry24 = new Entry(23f, 47.0f);
        Entry entry25 = new Entry(24f, 47.2f);

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

        Entry entry1 = new Entry(0f, 35.1f);
        Entry entry2 = new Entry(1f, 37.8f);
        Entry entry3 = new Entry(2f, 39.5f);
        Entry entry4 = new Entry(3f, 40.8f);
        Entry entry5 = new Entry(4f, 41.9f);
        Entry entry6 = new Entry(5f, 42.8f);
        Entry entry7 = new Entry(6f, 43.5f);
        Entry entry8 = new Entry(7f, 44.2f);
        Entry entry9 = new Entry(8f, 44.7f);
        Entry entry10 = new Entry(9f, 45.2f);
        Entry entry11 = new Entry(10f, 45.6f);
        Entry entry12 = new Entry(11f, 46.0f);
        Entry entry13 = new Entry(12f, 46.3f);
        Entry entry14 = new Entry(13f, 46.6f);
        Entry entry15 = new Entry(14f, 46.8f);
        Entry entry16 = new Entry(15f, 47.1f);
        Entry entry17 = new Entry(16f, 47.3f);
        Entry entry18 = new Entry(17f, 47.5f);
        Entry entry19 = new Entry(18f, 47.7f);
        Entry entry20 = new Entry(19f, 47.8f);
        Entry entry21 = new Entry(20f, 48.0f);
        Entry entry22 = new Entry(21f, 48.2f);
        Entry entry23 = new Entry(22f, 48.3f);
        Entry entry24 = new Entry(23f, 48.5f);
        Entry entry25 = new Entry(24f, 48.6f);

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

        Entry entry1 = new Entry(0f, 36.1f);
        Entry entry2 = new Entry(1f, 38.8f);
        Entry entry3 = new Entry(2f, 40.5f);
        Entry entry4 = new Entry(3f, 41.9f);
        Entry entry5 = new Entry(4f, 43.0f);
        Entry entry6 = new Entry(5f, 43.9f);
        Entry entry7 = new Entry(6f, 44.6f);
        Entry entry8 = new Entry(7f, 45.3f);
        Entry entry9 = new Entry(8f, 45.9f);
        Entry entry10 = new Entry(9f, 46.3f);
        Entry entry11 = new Entry(10f, 46.8f);
        Entry entry12 = new Entry(11f, 47.1f);
        Entry entry13 = new Entry(12f, 47.5f);
        Entry entry14 = new Entry(13f, 47.7f);
        Entry entry15 = new Entry(14f, 48.0f);
        Entry entry16 = new Entry(15f, 48.2f);
        Entry entry17 = new Entry(16f, 48.5f);
        Entry entry18 = new Entry(17f, 48.7f);
        Entry entry19 = new Entry(18f, 48.8f);
        Entry entry20 = new Entry(19f, 49.0f);
        Entry entry21 = new Entry(20f, 49.2f);
        Entry entry22 = new Entry(21f, 49.4f);
        Entry entry23 = new Entry(22f, 49.5f);
        Entry entry24 = new Entry(23f, 49.7f);
        Entry entry25 = new Entry(24f, 49.8f);

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

        Entry entry1 = new Entry(0f, 44.6f);
        Entry entry2 = new Entry(1f, 44.8f);
        Entry entry3 = new Entry(2f, 45.1f);
        Entry entry4 = new Entry(3f, 45.3f);
        Entry entry5 = new Entry(4f, 45.5f);
        Entry entry6 = new Entry(5f, 45.7f);
        Entry entry7 = new Entry(6f, 45.9f);
        Entry entry8 = new Entry(7f, 46.0f);
        Entry entry9 = new Entry(8f, 46.2f);
        Entry entry10 = new Entry(9f, 46.3f);
        Entry entry11 = new Entry(10f, 46.4f);
        Entry entry12 = new Entry(11f, 46.5f);
        Entry entry13 = new Entry(12f, 46.7f);
        Entry entry14 = new Entry(13f, 46.8f);
        Entry entry15 = new Entry(14f, 46.9f);
        Entry entry16 = new Entry(15f, 47.0f);
        Entry entry17 = new Entry(16f, 47.1f);
        Entry entry18 = new Entry(17f, 47.2f);
        Entry entry19 = new Entry(18f, 47.2f);

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

        Entry entry1 = new Entry(0f, 45.7f);
        Entry entry2 = new Entry(1f, 46.0f);
        Entry entry3 = new Entry(2f, 46.3f);
        Entry entry4 = new Entry(3f, 46.5f);
        Entry entry5 = new Entry(4f, 46.7f);
        Entry entry6 = new Entry(5f, 46.9f);
        Entry entry7 = new Entry(6f, 47f);
        Entry entry8 = new Entry(7f, 47.2f);
        Entry entry9 = new Entry(8f, 47.4f);
        Entry entry10 = new Entry(9f, 47.5f);
        Entry entry11 = new Entry(10f, 47.6f);
        Entry entry12 = new Entry(11f, 47.7f);
        Entry entry13 = new Entry(12f, 47.9f);
        Entry entry14 = new Entry(13f, 48.0f);
        Entry entry15 = new Entry(14f, 48.1f);
        Entry entry16 = new Entry(15f, 48.2f);
        Entry entry17 = new Entry(16f, 48.3f);
        Entry entry18 = new Entry(17f, 48.4f);
        Entry entry19 = new Entry(18f, 48.4f);

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

        Entry entry1 = new Entry(0f, 47.2f);
        Entry entry2 = new Entry(1f, 47.5f);
        Entry entry3 = new Entry(2f, 47.7f);
        Entry entry4 = new Entry(3f, 47.9f);
        Entry entry5 = new Entry(4f, 48.1f);
        Entry entry6 = new Entry(5f, 48.3f);
        Entry entry7 = new Entry(6f, 48.5f);
        Entry entry8 = new Entry(7f, 48.7f);
        Entry entry9 = new Entry(8f, 48.8f);
        Entry entry10 = new Entry(9f, 49.0f);
        Entry entry11 = new Entry(10f, 49.1f);
        Entry entry12 = new Entry(11f, 49.2f);
        Entry entry13 = new Entry(12f, 49.3f);
        Entry entry14 = new Entry(13f, 49.4f);
        Entry entry15 = new Entry(14f, 49.5f);
        Entry entry16 = new Entry(15f, 49.6f);
        Entry entry17 = new Entry(16f, 49.7f);
        Entry entry18 = new Entry(17f, 49.8f);
        Entry entry19 = new Entry(18f, 49.9f);

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

        Entry entry1 = new Entry(0f, 48.6f);
        Entry entry2 = new Entry(1f, 48.9f);
        Entry entry3 = new Entry(2f, 49.2f);
        Entry entry4 = new Entry(3f, 49.4f);
        Entry entry5 = new Entry(4f, 49.6f);
        Entry entry6 = new Entry(5f, 49.8f);
        Entry entry7 = new Entry(6f, 50.0f);
        Entry entry8 = new Entry(7f, 50.1f);
        Entry entry9 = new Entry(8f, 50.3f);
        Entry entry10 = new Entry(9f, 50.4f);
        Entry entry11 = new Entry(10f, 50.6f);
        Entry entry12 = new Entry(11f, 50.7f);
        Entry entry13 = new Entry(12f, 50.8f);
        Entry entry14 = new Entry(13f, 50.9f);
        Entry entry15 = new Entry(14f, 51.0f);
        Entry entry16 = new Entry(15f, 51.1f);
        Entry entry17 = new Entry(16f, 51.2f);
        Entry entry18 = new Entry(17f, 51.3f);
        Entry entry19 = new Entry(18f, 51.4f);

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

        Entry entry1 = new Entry(0f, 49.8f);
        Entry entry2 = new Entry(1f, 50.1f);
        Entry entry3 = new Entry(2f, 50.3f);
        Entry entry4 = new Entry(3f, 50.6f);
        Entry entry5 = new Entry(4f, 50.8f);
        Entry entry6 = new Entry(5f, 51f);
        Entry entry7 = new Entry(6f, 51.2f);
        Entry entry8 = new Entry(7f, 51.3f);
        Entry entry9 = new Entry(8f, 51.5f);
        Entry entry10 = new Entry(9f, 51.6f);
        Entry entry11 = new Entry(10f, 51.8f);
        Entry entry12 = new Entry(11f, 51.9f);
        Entry entry13 = new Entry(12f, 52f);
        Entry entry14 = new Entry(13f, 52.1f);
        Entry entry15 = new Entry(14f, 52.2f);
        Entry entry16 = new Entry(15f, 52.3f);
        Entry entry17 = new Entry(16f, 52.4f);
        Entry entry18 = new Entry(17f, 52.5f);
        Entry entry19 = new Entry(18f, 52.6f);

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
                                                    newborn = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 0 && totalmonth == 1) {
                                                    firstMonth = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 0 && totalmonth == 2) {
                                                    secondMonth = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 0 && totalmonth == 3) {
                                                    thirdMonth = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 0 && totalmonth == 4) {
                                                    forthMonth = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 0 && totalmonth == 5) {
                                                    fifthMonth = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 0 && totalmonth == 6) {
                                                    sixthMonth = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 0 && totalmonth == 7) {
                                                    seventhMonth = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 0 && totalmonth == 8) {
                                                    eightMonth = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 0 && totalmonth == 9) {
                                                    ninethMonth = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 0 && totalmonth == 10) {
                                                    tenthMonth = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 0 && totalmonth == 11) {
                                                    eleventhMonth = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 0) {
                                                    oneyear = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 1) {
                                                    oneyearone = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 2) {
                                                    oneyeartwo = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 3) {
                                                    oneyearthree = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 4) {
                                                    oneyearfour = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 5) {
                                                    oneyearfive = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 6) {
                                                    oneyearsix = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 7) {
                                                    oneyearseven = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 8) {
                                                    oneyeareight = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 9) {
                                                    oneyearnine = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 10) {
                                                    oneyearten = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 11) {
                                                    oneyeareleven = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 2 && totalmonth == 0) {
                                                    twoyear = Float.parseFloat(measurement.getHeadC());
                                                }

                                                final ArrayList<Entry> headList = new ArrayList<>();

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

                                                headList.add(entry1);
                                                headList.add(entry2);
                                                headList.add(entry3);
                                                headList.add(entry4);
                                                headList.add(entry5);
                                                headList.add(entry6);
                                                headList.add(entry7);
                                                headList.add(entry8);
                                                headList.add(entry9);
                                                headList.add(entry10);
                                                headList.add(entry11);
                                                headList.add(entry12);
                                                headList.add(entry13);
                                                headList.add(entry14);
                                                headList.add(entry15);
                                                headList.add(entry16);
                                                headList.add(entry17);
                                                headList.add(entry18);
                                                headList.add(entry19);
                                                headList.add(entry20);
                                                headList.add(entry21);
                                                headList.add(entry22);
                                                headList.add(entry23);
                                                headList.add(entry24);
                                                headList.add(entry25);

                                                headData = new LineDataSet(headList, "Baby Head Cir.");
                                                headData.setDrawCircles(false);
                                                headData.setDrawValues(true);
                                                headData.setColor(Color.BLUE);
                                            }
                                        }

                                        LineData data = new LineData(negative3sd, negative2sd, zerosd, positive2sd, positive3sd, headData);
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
                                                    newborn = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 0 && totalmonth == 1) {
                                                    firstMonth = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 0 && totalmonth == 2) {
                                                    secondMonth = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 0 && totalmonth == 3) {
                                                    thirdMonth = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 0 && totalmonth == 4) {
                                                    forthMonth = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 0 && totalmonth == 5) {
                                                    fifthMonth = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 0 && totalmonth == 6) {
                                                    sixthMonth = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 0 && totalmonth == 7) {
                                                    seventhMonth = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 0 && totalmonth == 8) {
                                                    eightMonth = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 0 && totalmonth == 9) {
                                                    ninethMonth = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 0 && totalmonth == 10) {
                                                    tenthMonth = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 0 && totalmonth == 11) {
                                                    eleventhMonth = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 0) {
                                                    oneyear = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 1) {
                                                    oneyearone = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 2) {
                                                    oneyeartwo = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 3) {
                                                    oneyearthree = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 4) {
                                                    oneyearfour = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 5) {
                                                    oneyearfive = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 6) {
                                                    oneyearsix = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 7) {
                                                    oneyearseven = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 8) {
                                                    oneyeareight = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 9) {
                                                    oneyearnine = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 10) {
                                                    oneyearten = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 1 && totalmonth == 11) {
                                                    oneyeareleven = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 2 && totalmonth == 0) {
                                                    twoyear = Float.parseFloat(measurement.getHeadC());
                                                }

                                                final ArrayList<Entry> headList = new ArrayList<>();

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

                                                headList.add(entry1);
                                                headList.add(entry2);
                                                headList.add(entry3);
                                                headList.add(entry4);
                                                headList.add(entry5);
                                                headList.add(entry6);
                                                headList.add(entry7);
                                                headList.add(entry8);
                                                headList.add(entry9);
                                                headList.add(entry10);
                                                headList.add(entry11);
                                                headList.add(entry12);
                                                headList.add(entry13);
                                                headList.add(entry14);
                                                headList.add(entry15);
                                                headList.add(entry16);
                                                headList.add(entry17);
                                                headList.add(entry18);
                                                headList.add(entry19);
                                                headList.add(entry20);
                                                headList.add(entry21);
                                                headList.add(entry22);
                                                headList.add(entry23);
                                                headList.add(entry24);
                                                headList.add(entry25);

                                                headDataF = new LineDataSet(headList, "Baby Head Cir.");
                                                headDataF.setDrawCircles(false);
                                                headDataF.setDrawValues(true);
                                                headDataF.setColor(Color.BLUE);
                                            }
                                        }

                                        LineData data = new LineData(negative3sdF, negative2sdF, zerosdF, positive2sdF, positive3sdF, headDataF);
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
                                                    twoyear = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 2 && (totalmonth == 2 || totalmonth == 3)) {
                                                    twoyeartwo = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 2 && (totalmonth == 4 || totalmonth == 5)) {
                                                    twoyearfour = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 2 && (totalmonth == 6 || totalmonth == 7)) {
                                                    twoyearsix = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 2 && (totalmonth == 8 || totalmonth == 9)) {
                                                    twoyeareight = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 2 && (totalmonth == 10 || totalmonth == 11 )) {
                                                    twoyearten = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 3 && (totalmonth == 0 || totalmonth == 1 )) {
                                                    threeyear = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 3 && (totalmonth == 2 || totalmonth == 3 )) {
                                                    threeyeartwo = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 3 && (totalmonth == 4 || totalmonth == 5 )) {
                                                    threeyearfour = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 3 && (totalmonth == 6 || totalmonth == 7 )) {
                                                    threeyearsix = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 3 && (totalmonth == 8 || totalmonth == 9 )) {
                                                    threeyeareight = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 3 && (totalmonth == 10 || totalmonth == 11 )) {
                                                    threeyearten = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 4 && (totalmonth == 0 || totalmonth == 1 )) {
                                                    fouryear = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 4 && (totalmonth == 2 || totalmonth == 3 )) {
                                                    fouryeartwo = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 4 && (totalmonth == 4 || totalmonth == 5 )) {
                                                    fouryearfour = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 4 && (totalmonth == 6 || totalmonth == 7 )) {
                                                    fouryearsix = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 4 && (totalmonth == 8 || totalmonth == 9 )) {
                                                    fouryeareight = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 4 && (totalmonth == 10 || totalmonth == 11 )) {
                                                    fouryearten = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 5 && (totalmonth == 0 || totalmonth == 1 )) {
                                                    fiveyear = Float.parseFloat(measurement.getHeadC());
                                                }

                                                final ArrayList<Entry> headList = new ArrayList<>();

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

                                                headList.add(entry1);
                                                headList.add(entry2);
                                                headList.add(entry3);
                                                headList.add(entry4);
                                                headList.add(entry5);
                                                headList.add(entry6);
                                                headList.add(entry7);
                                                headList.add(entry8);
                                                headList.add(entry9);
                                                headList.add(entry10);
                                                headList.add(entry11);
                                                headList.add(entry12);
                                                headList.add(entry13);
                                                headList.add(entry14);
                                                headList.add(entry15);
                                                headList.add(entry16);
                                                headList.add(entry17);
                                                headList.add(entry18);
                                                headList.add(entry19);

                                                headDataB = new LineDataSet(headList, "Baby Head Cir.");
                                                headDataB.setDrawCircles(false);
                                                headDataB.setDrawValues(true);
                                                headDataB.setColor(Color.BLUE);
                                            }
                                        }

                                        LineData dataB = new LineData(negative3sdB, negative2sdB, zerosdB, positive2sdB, positive3sdB, headDataB);
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
                                                    twoyear = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 2 && (totalmonth == 2 || totalmonth == 3)) {
                                                    twoyeartwo = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 2 && (totalmonth == 4 || totalmonth == 5)) {
                                                    twoyearfour = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 2 && (totalmonth == 6 || totalmonth == 7)) {
                                                    twoyearsix = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 2 && (totalmonth == 8 || totalmonth == 9)) {
                                                    twoyeareight = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 2 && (totalmonth == 10 || totalmonth == 11 )) {
                                                    twoyearten = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 3 && (totalmonth == 0 || totalmonth == 1 )) {
                                                    threeyear = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 3 && (totalmonth == 2 || totalmonth == 3 )) {
                                                    threeyeartwo = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 3 && (totalmonth == 4 || totalmonth == 5 )) {
                                                    threeyearfour = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 3 && (totalmonth == 6 || totalmonth == 7 )) {
                                                    threeyearsix = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 3 && (totalmonth == 8 || totalmonth == 9 )) {
                                                    threeyeareight = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 3 && (totalmonth == 10 || totalmonth == 11 )) {
                                                    threeyearten = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 4 && (totalmonth == 0 || totalmonth == 1 )) {
                                                    fouryear = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 4 && (totalmonth == 2 || totalmonth == 3 )) {
                                                    fouryeartwo = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 4 && (totalmonth == 4 || totalmonth == 5 )) {
                                                    fouryearfour = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 4 && (totalmonth == 6 || totalmonth == 7 )) {
                                                    fouryearsix = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 4 && (totalmonth == 8 || totalmonth == 9 )) {
                                                    fouryeareight = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 4 && (totalmonth == 10 || totalmonth == 11 )) {
                                                    fouryearten = Float.parseFloat(measurement.getHeadC());
                                                }

                                                if (totalyear == 5 && (totalmonth == 0 || totalmonth == 1 )) {
                                                    fiveyear = Float.parseFloat(measurement.getHeadC());
                                                }

                                                final ArrayList<Entry> headList = new ArrayList<>();

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

                                                headList.add(entry1);
                                                headList.add(entry2);
                                                headList.add(entry3);
                                                headList.add(entry4);
                                                headList.add(entry5);
                                                headList.add(entry6);
                                                headList.add(entry7);
                                                headList.add(entry8);
                                                headList.add(entry9);
                                                headList.add(entry10);
                                                headList.add(entry11);
                                                headList.add(entry12);
                                                headList.add(entry13);
                                                headList.add(entry14);
                                                headList.add(entry15);
                                                headList.add(entry16);
                                                headList.add(entry17);
                                                headList.add(entry18);
                                                headList.add(entry19);

                                                headDataBF = new LineDataSet(headList, "Baby Head Cir.");
                                                headDataBF.setDrawCircles(false);
                                                headDataBF.setDrawValues(true);
                                                headDataBF.setColor(Color.BLUE);
                                            }
                                        }

                                        LineData dataB = new LineData(negative3sdBF, negative2sdBF, zerosdBF, positive2sdBF, positive3sdBF, headDataBF);
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
