package com.health.baby_daily.statistic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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
import com.health.baby_daily.model.Bottle;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class BottleStaFragChart extends Fragment {

    private View view;
    private HorizontalBarChart chartA;
    private BarChart chartB;
    private LineChart chartC;
    private DatabaseReference table_bottle, table_bottleB, table_bottleC;
    private Query query, queryB, queryC;

    private String bId;

    private BarDataSet set1, set2, setA, setB, setC, setD, setE, setF, setG;
    private LineDataSet setJan, setFeb, setMar, setApr, setMay, setJun, setJul, setAug, setSep, setOct, setNov, setDec;
    float countB = 0, countF = 0;
    float count1 = 0, count2 = 0, count3 = 0, count4 = 0, count5 = 0, count6 = 0, count7 = 0;
    float countJan = 0, countFeb = 0, countMar = 0, countApr = 0, countMay = 0, countJun = 0, countJul = 0;
    float countAug = 0, countSep = 0, countOct = 0, countNov = 0, countDec = 0;
    private String mon, tue, wed, thu, fri, stu, sun;
    private String curJan, curFeb, curMar, curApr, curMay, curJun, curJul, curAug, curSep, curOct, curNov, curDec;
    private Date dateA, dateB, dateC, dateD, dateE, dateF, dateG, dateH;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bottle_chart_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefBaby = getContext().getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        chartA = view.findViewById(R.id.chartA);
        chartA.getDescription().setText("Bottle Event");
        chartA.getDescription().setTextSize(10);
        chartA.setScaleEnabled(false);
        chartA.setScaleXEnabled(false);
        chartA.setTouchEnabled(false);
        chartA.setFitBars(true);

        chartA.getAxisLeft().setDrawLabels(false);
        chartA.getXAxis().setDrawLabels(true);
        chartA.setDragOffsetY(0);
        chartA.setDragOffsetX(0);
        chartA.setDrawValueAboveBar(true);
        chartA.getXAxis().setLabelCount(2, false);
        chartA.animateXY(3000,3000);

        chartB = view.findViewById(R.id.chartB);
        chartB.getDescription().setText("Bottle Event");
        chartB.setTouchEnabled(true);
        chartB.setScaleEnabled(false);
        chartB.getXAxis().setDrawLabels(true);
        chartB.getAxisRight().setDrawLabels(false);
        chartB.setFitBars(true);
        chartB.getAxisLeft().setGranularity(1);
        chartB.getXAxis().setLabelCount(7, false);
        chartB.getAxisRight().setDrawGridLines(false);
        chartB.animateXY(3000,3000);

        chartC = view.findViewById(R.id.chartC);
        chartC.getDescription().setText("Bottle Event");
        chartC.setScaleEnabled(true);
        chartC.getXAxis().setDrawLabels(true);
        chartC.getAxisRight().setDrawLabels(false);
        chartC.getLegend().setEnabled(false);
        chartC.getXAxis().setAxisMinimum(0);
        chartC.getAxisLeft().setAxisMinimum(0);
        chartC.getXAxis().setLabelCount(12, false);
        chartC.getAxisRight().setDrawGridLines(false);
        chartC.animateXY(3000,3000);

        YAxis rightA = chartA.getAxisRight();
        rightA.setValueFormatter(new MyYAxisValueFormatter());
        rightA.setGranularity(1);

        //will be define value by baby weight
        LimitLine lineMax = new LimitLine(900f, "Max Limits");
        lineMax.enableDashedLine(10f,10f,0f);
        LimitLine lineMin = new LimitLine(500f, "Min Limits");
        lineMin.enableDashedLine(10f,10f,0f);

        YAxis leftB = chartB.getAxisLeft();
        leftB.setValueFormatter(new MyYAxisValueFormatter());
        leftB.setGranularity(1);
        leftB.addLimitLine(lineMax);
        leftB.addLimitLine(lineMin);

        YAxis leftC = chartC.getAxisLeft();
        leftC.setValueFormatter(new MyYAxisValueFormatter());
        leftC.setGranularity(1);

        Legend legend = chartC.getLegend();
        legend.setWordWrapEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        //today
        final ArrayList<BarEntry> yVals_Breast = new ArrayList<>();
        final ArrayList<BarEntry> yVals_Formula = new ArrayList<>();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        final String currentDate = dateFormat.format(date);

        table_bottle = FirebaseDatabase.getInstance().getReference().child("Bottle");
        query = table_bottle.orderByChild("dateTime");
        query.keepSynced(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                countB = 0;
                countF = 0;
                if(dataSnapshot.exists()){
                    float mLValueB = 0.0f;
                    float ozValueB = 0.0f;
                    float mLValueF = 0.0f;
                    float ozValueF = 0.0f;
                    float ozAmtB = 0.0f;
                    float ozAmtF = 0.0f;
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Bottle bottle = snapshot.getValue(Bottle.class);
                        if(bottle.getBabyId().equals(bId)) {
                            if (bottle.getDateTime().startsWith(currentDate) && bottle.getType().equals("Breast")) {
                                if(bottle.getUnits().equals("mL")){
                                    mLValueB = mLValueB + Float.valueOf(bottle.getAmount());
                                }else if(bottle.getUnits().equals("Oz")){
                                    ozAmtB = Float.valueOf(bottle.getAmount())*29.57f;
                                    ozValueB = ozValueB + ozAmtB;
                                }

                                countB = mLValueB+ozValueB;
                            }

                            if(bottle.getDateTime().startsWith(currentDate) && bottle.getType().equals("Formula")){
                                if(bottle.getUnits().equals("mL")){
                                    mLValueF = mLValueF + Float.valueOf(bottle.getAmount());
                                }else if(bottle.getUnits().equals("Oz")){
                                    ozAmtF = Float.valueOf(bottle.getAmount())*29.57f;
                                    ozValueF = ozValueF + ozAmtF;
                                }
                                countF = mLValueF+ozValueF;
                            }
                        }
                    }

                    yVals_Breast.add(new BarEntry(0f, countB));
                    set1 = new BarDataSet(yVals_Breast, "Breast");
                    set1.setDrawValues(true);

                    yVals_Formula.add(new BarEntry(1f, countF));
                    set2 = new BarDataSet(yVals_Formula, "Formula");
                    set2.setDrawValues(true);

                    set1.setColor(Color.BLUE);
                    set2.setColor(Color.YELLOW);

                    BarData data = new BarData(set1, set2);
                    chartA.setData(data);
                    final String[] type = new String[]{"Breast", "Formula"};

                    XAxis xAxis = chartA.getXAxis();
                    xAxis.setGranularity(1f);
                    xAxis.setValueFormatter(new MyXAxisValueFormatter(type));
                    chartA.setDrawValueAboveBar(true);
                    chartA.notifyDataSetChanged();
                    chartA.invalidate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //week
        final ArrayList<BarEntry> yVals_Monday = new ArrayList<>();
        final ArrayList<BarEntry> yVals_Tuesday = new ArrayList<>();
        final ArrayList<BarEntry> yVals_Wednesday = new ArrayList<>();
        final ArrayList<BarEntry> yVals_Thursday = new ArrayList<>();
        final ArrayList<BarEntry> yVals_Friday = new ArrayList<>();
        final ArrayList<BarEntry> yVals_Saturday = new ArrayList<>();
        final ArrayList<BarEntry> yVals_Sunday = new ArrayList<>();

        Calendar c = GregorianCalendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        mon = df.format(c.getTime());
        c.add(Calendar.DATE, 1);
        tue = df.format(c.getTime());
        c.add(Calendar.DATE, 1);
        wed = df.format(c.getTime());
        c.add(Calendar.DATE, 1);
        thu = df.format(c.getTime());
        c.add(Calendar.DATE, 1);
        fri = df.format(c.getTime());
        c.add(Calendar.DATE, 1);
        stu = df.format(c.getTime());
        c.add(Calendar.DATE, 1);
        sun = df.format(c.getTime());

        final String day = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));

        table_bottleB = FirebaseDatabase.getInstance().getReference().child("Bottle");
        queryB = table_bottleB.orderByChild("dateTime");
        queryB.keepSynced(true);
        queryB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count1 = 0;
                count2 = 0;
                count3 = 0;
                count4 = 0;
                count5 = 0;
                count6 = 0;
                count7 = 0;

                if(dataSnapshot.exists()){
                    float mLValueMon = 0.0f;   float ozValueMon = 0.0f;   float ozAmtMon = 0.0f;
                    float mLValueTue = 0.0f;   float ozValueTue = 0.0f;   float ozAmtTue = 0.0f;
                    float mLValueWed = 0.0f;   float ozValueWed = 0.0f;   float ozAmtWed = 0.0f;
                    float mLValueThu = 0.0f;   float ozValueThu = 0.0f;   float ozAmtThu = 0.0f;
                    float mLValueFri = 0.0f;   float ozValueFri = 0.0f;   float ozAmtFri = 0.0f;
                    float mLValueStu = 0.0f;   float ozValueStu = 0.0f;   float ozAmtStu = 0.0f;
                    float mLValueSun = 0.0f;   float ozValueSun = 0.0f;   float ozAmtSun = 0.0f;


                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Bottle bottle = snapshot.getValue(Bottle.class);
                        if(bottle.getBabyId().equals(bId)) {
                            String created = bottle.getCreated();

                            try {
                                dateA = new SimpleDateFormat("dd/MM/yyyy").parse(created);
                                dateB = new SimpleDateFormat("dd/MM/yyyy").parse(mon);
                                dateC = new SimpleDateFormat("dd/MM/yyyy").parse(tue);
                                dateD = new SimpleDateFormat("dd/MM/yyyy").parse(wed);
                                dateE = new SimpleDateFormat("dd/MM/yyyy").parse(thu);
                                dateF = new SimpleDateFormat("dd/MM/yyyy").parse(fri);
                                dateG = new SimpleDateFormat("dd/MM/yyyy").parse(stu);
                                dateH = new SimpleDateFormat("dd/MM/yyyy").parse(sun);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if(dateA.equals(dateB)){
                                if(bottle.getUnits().equals("mL")){
                                    mLValueMon = mLValueMon + Float.valueOf(bottle.getAmount());
                                }else if(bottle.getUnits().equals("Oz")){
                                    ozAmtMon = Float.valueOf(bottle.getAmount())*29.57f;
                                    ozValueMon = ozValueMon + ozAmtMon;
                                }
                                count1 = mLValueMon+ ozValueMon;
                            }else if(dateA.equals(dateC)){
                                if(bottle.getUnits().equals("mL")){
                                    mLValueTue = mLValueTue + Float.valueOf(bottle.getAmount());
                                }else if(bottle.getUnits().equals("Oz")){
                                    ozAmtTue = Float.valueOf(bottle.getAmount())*29.57f;
                                    ozValueTue = ozValueTue + ozAmtTue;
                                }
                                count2 = mLValueTue+ ozValueTue;
                            }else if(dateA.equals(dateD)){
                                if(bottle.getUnits().equals("mL")){
                                    mLValueWed = mLValueWed + Float.valueOf(bottle.getAmount());
                                }else if(bottle.getUnits().equals("Oz")){
                                    ozAmtWed = Float.valueOf(bottle.getAmount())*29.57f;
                                    ozValueWed = ozValueWed + ozAmtWed;
                                }
                                count3 = mLValueWed + ozValueWed;
                            }else if(dateA.equals(dateE)){
                                if(bottle.getUnits().equals("mL")){
                                    mLValueThu = mLValueThu + Float.valueOf(bottle.getAmount());
                                }else if(bottle.getUnits().equals("Oz")){
                                    ozAmtThu = Float.valueOf(bottle.getAmount())*29.57f;
                                    ozValueThu = ozValueThu + ozAmtThu;
                                }
                                count4 = mLValueThu + ozValueThu;
                            }else if(dateA.equals(dateF)){
                                if(bottle.getUnits().equals("mL")){
                                    mLValueFri = mLValueFri + Float.valueOf(bottle.getAmount());
                                }else if(bottle.getUnits().equals("Oz")){
                                    ozAmtFri = Float.valueOf(bottle.getAmount())*29.57f;
                                    ozValueFri = ozValueFri + ozAmtFri;
                                }
                                count5 = mLValueFri+ ozValueFri;
                            }else if(dateA.equals(dateG)){
                                if(bottle.getUnits().equals("mL")){
                                    mLValueStu = mLValueStu + Float.valueOf(bottle.getAmount());
                                }else if(bottle.getUnits().equals("Oz")){
                                    ozAmtStu = Float.valueOf(bottle.getAmount())*29.57f;
                                    ozValueStu = ozValueStu + ozAmtStu;
                                }
                                count6 = mLValueStu + ozValueStu;
                            }else if(dateA.equals(dateH)){
                                if(bottle.getUnits().equals("mL")){
                                    mLValueSun = mLValueSun + Float.valueOf(bottle.getAmount());
                                }else if(bottle.getUnits().equals("Oz")){
                                    ozAmtSun = Float.valueOf(bottle.getAmount())*29.57f;
                                    ozValueSun = ozValueSun + ozAmtSun;
                                }
                                count7 = mLValueSun + ozValueSun;
                            }

                        }
                    }

                    yVals_Monday.add(new BarEntry(1, count1));
                    setA = new BarDataSet(yVals_Monday, "Mon");

                    yVals_Tuesday.add(new BarEntry(2, count2));
                    setB = new BarDataSet(yVals_Tuesday, "Tue");

                    yVals_Wednesday.add(new BarEntry(3, count3));
                    setC = new BarDataSet(yVals_Wednesday, "Wed");

                    yVals_Thursday.add(new BarEntry(4, count4));
                    setD = new BarDataSet(yVals_Thursday, "Thu");

                    yVals_Friday.add(new BarEntry(5, count5));
                    setE = new BarDataSet(yVals_Friday, "Fri");

                    yVals_Saturday.add(new BarEntry(6, count6));
                    setF = new BarDataSet(yVals_Saturday, "Sat");

                    yVals_Sunday.add(new BarEntry(7, count7));
                    setG = new BarDataSet(yVals_Sunday, "Sun");

                    setA.setColor(Color.BLUE);
                    setB.setColor(Color.YELLOW);
                    setC.setColor(Color.CYAN);
                    setD.setColor(Color.RED);
                    setE.setColor(Color.GRAY);
                    setF.setColor(Color.GREEN);
                    setG.setColor(Color.MAGENTA);
                    BarData data = new BarData(setA, setB, setC, setD, setE, setF, setG);
                    chartB.setData(data);
                    final String[] day = new String[]{"", "Mon", "Tue", "Wed", "Thu", "Fri", "Stu", "Sun"};

                    XAxis xAxis = chartB.getXAxis();
                    xAxis.setGranularity(1f);
                    xAxis.setValueFormatter(new MyXAxisValueFormatter(day));
                    chartB.notifyDataSetChanged();
                    chartB.invalidate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //monthly
        final ArrayList<Entry> yVals_year = new ArrayList<>();

        c.set(Calendar.MONTH, Calendar.JANUARY);
        DateFormat df2 = new SimpleDateFormat("/MM/yyyy");
        curJan = df2.format(c.getTime());
        c.add(Calendar.MONTH, 1);
        curFeb = df2.format(c.getTime());
        c.add(Calendar.MONTH, 1);
        curMar = df2.format(c.getTime());
        c.add(Calendar.MONTH, 1);
        curApr = df2.format(c.getTime());
        c.add(Calendar.MONTH, 1);
        curMay = df2.format(c.getTime());
        c.add(Calendar.MONTH, 1);
        curJun = df2.format(c.getTime());
        c.add(Calendar.MONTH, 1);
        curJul = df2.format(c.getTime());
        c.add(Calendar.MONTH, 1);
        curAug = df2.format(c.getTime());
        c.add(Calendar.MONTH, 1);
        curSep = df2.format(c.getTime());
        c.add(Calendar.MONTH, 1);
        curOct = df2.format(c.getTime());
        c.add(Calendar.MONTH, 1);
        curNov = df2.format(c.getTime());
        c.add(Calendar.MONTH, 1);
        curDec = df2.format(c.getTime());

        final String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        table_bottleC = FirebaseDatabase.getInstance().getReference().child("Bottle");
        queryC = table_bottleC.orderByChild("dateTime");
        queryC.keepSynced(true);
        queryC.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                countJan = 0; countFeb = 0; countMar = 0; countApr = 0; countMay = 0; countJun = 0;
                countJul = 0; countAug = 0; countSep = 0; countOct = 0; countNov = 0; countDec = 0;

                float mLValue1 = 0.0f;   float ozValue1 = 0.0f;   float ozAmt1 = 0.0f;
                float mLValue2 = 0.0f;   float ozValue2 = 0.0f;   float ozAmt2 = 0.0f;
                float mLValue3 = 0.0f;   float ozValue3 = 0.0f;   float ozAmt3 = 0.0f;
                float mLValue4 = 0.0f;   float ozValue4 = 0.0f;   float ozAmt4 = 0.0f;
                float mLValue5 = 0.0f;   float ozValue5 = 0.0f;   float ozAmt5 = 0.0f;
                float mLValue6 = 0.0f;   float ozValue6 = 0.0f;   float ozAmt6 = 0.0f;
                float mLValue7 = 0.0f;   float ozValue7 = 0.0f;   float ozAmt7 = 0.0f;
                float mLValue8 = 0.0f;   float ozValue8 = 0.0f;   float ozAmt8 = 0.0f;
                float mLValue9 = 0.0f;   float ozValue9 = 0.0f;   float ozAmt9 = 0.0f;
                float mLValue10 = 0.0f;   float ozValue10 = 0.0f;   float ozAmt10 = 0.0f;
                float mLValue11 = 0.0f;   float ozValue11 = 0.0f;   float ozAmt11 = 0.0f;
                float mLValue12 = 0.0f;   float ozValue12 = 0.0f;   float ozAmt12 = 0.0f;

                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Bottle bottle = snapshot.getValue(Bottle.class);
                        if(bottle.getBabyId().equals(bId)) {
                            if(bottle.getDateTime().contains(curJan)){
                                if(bottle.getUnits().equals("mL")){
                                    mLValue1 = mLValue1 + Float.valueOf(bottle.getAmount());
                                }else if(bottle.getUnits().equals("Oz")){
                                    ozAmt1 = Float.valueOf(bottle.getAmount())*29.57f;
                                    ozValue1 = ozValue1 + ozAmt1;
                                }
                                countJan = mLValue1 + ozValue1;
                            }else if(bottle.getDateTime().contains(curFeb)){
                                if(bottle.getUnits().equals("mL")){
                                    mLValue2 = mLValue2 + Float.valueOf(bottle.getAmount());
                                }else if(bottle.getUnits().equals("Oz")){
                                    ozAmt2 = Float.valueOf(bottle.getAmount())*29.57f;
                                    ozValue2 = ozValue2 + ozAmt2;
                                }
                                countFeb = mLValue2 + ozValue2;
                            }else if(bottle.getDateTime().contains(curMar)){
                                if(bottle.getUnits().equals("mL")){
                                    mLValue3 = mLValue3 + Float.valueOf(bottle.getAmount());
                                }else if(bottle.getUnits().equals("Oz")){
                                    ozAmt3 = Float.valueOf(bottle.getAmount())*29.57f;
                                    ozValue3 = ozValue3 + ozAmt3;
                                }
                                countMar = mLValue3 + ozValue3;
                            }else if(bottle.getDateTime().contains(curApr)){
                                if(bottle.getUnits().equals("mL")){
                                    mLValue4 = mLValue4 + Float.valueOf(bottle.getAmount());
                                }else if(bottle.getUnits().equals("Oz")){
                                    ozAmt4 = Float.valueOf(bottle.getAmount())*29.57f;
                                    ozValue4 = ozValue4 + ozAmt4;
                                }
                                countApr = mLValue4 + ozValue4;
                            }else if(bottle.getDateTime().contains(curMay)){
                                if(bottle.getUnits().equals("mL")){
                                    mLValue5 = mLValue5 + Float.valueOf(bottle.getAmount());
                                }else if(bottle.getUnits().equals("Oz")){
                                    ozAmt5 = Float.valueOf(bottle.getAmount())*29.57f;
                                    ozValue5 = ozValue5 + ozAmt5;
                                }
                                countMay = mLValue5 + ozValue5;
                            }else if(bottle.getDateTime().contains(curJun)){
                                if(bottle.getUnits().equals("mL")){
                                    mLValue6 = mLValue6 + Float.valueOf(bottle.getAmount());
                                }else if(bottle.getUnits().equals("Oz")){
                                    ozAmt6 = Float.valueOf(bottle.getAmount())*29.57f;
                                    ozValue6 = ozValue6 + ozAmt6;
                                }
                                countJun = mLValue6 + ozValue6;
                            }else if(bottle.getDateTime().contains(curJul)){
                                if(bottle.getUnits().equals("mL")){
                                    mLValue7 = mLValue7 + Float.valueOf(bottle.getAmount());
                                }else if(bottle.getUnits().equals("Oz")){
                                    ozAmt7 = Float.valueOf(bottle.getAmount())*29.57f;
                                    ozValue7 = ozValue7 + ozAmt7;
                                }
                                countJul = mLValue7 + ozValue7;
                            }else if(bottle.getDateTime().contains(curAug)){
                                if(bottle.getUnits().equals("mL")){
                                    mLValue8 = mLValue8 + Float.valueOf(bottle.getAmount());
                                }else if(bottle.getUnits().equals("Oz")){
                                    ozAmt8 = Float.valueOf(bottle.getAmount())*29.57f;
                                    ozValue8 = ozValue8 + ozAmt8;
                                }
                                countAug = mLValue8 + ozValue8;
                            }else if(bottle.getDateTime().contains(curSep)){
                                if(bottle.getUnits().equals("mL")){
                                    mLValue9 = mLValue9 + Float.valueOf(bottle.getAmount());
                                }else if(bottle.getUnits().equals("Oz")){
                                    ozAmt9 = Float.valueOf(bottle.getAmount())*29.57f;
                                    ozValue9 = ozValue9 + ozAmt9;
                                }
                                countSep = mLValue9 + ozValue9;
                            }else if(bottle.getDateTime().contains(curOct)){
                                if(bottle.getUnits().equals("mL")){
                                    mLValue10 = mLValue10 + Float.valueOf(bottle.getAmount());
                                }else if(bottle.getUnits().equals("Oz")){
                                    ozAmt10 = Float.valueOf(bottle.getAmount())*29.57f;
                                    ozValue10 = ozValue10 + ozAmt10;
                                }
                                countOct = mLValue10 + ozValue10;
                            }else if(bottle.getDateTime().contains(curNov)){
                                if(bottle.getUnits().equals("mL")){
                                    mLValue11 = mLValue11 + Float.valueOf(bottle.getAmount());
                                }else if(bottle.getUnits().equals("Oz")){
                                    ozAmt11 = Float.valueOf(bottle.getAmount())*29.57f;
                                    ozValue11 = ozValue11 + ozAmt11;
                                }
                                countNov = mLValue11 + ozValue11;
                            }else if(bottle.getDateTime().contains(curDec)){
                                if(bottle.getUnits().equals("mL")){
                                    mLValue12 = mLValue12 + Float.valueOf(bottle.getAmount());
                                }else if(bottle.getUnits().equals("Oz")){
                                    ozAmt12 = Float.valueOf(bottle.getAmount())*29.57f;
                                    ozValue12 = ozValue12 + ozAmt12;
                                }
                                countDec = mLValue12 + ozValue12;
                            }

                        }
                    }

                    Entry entryJan = new Entry(1, countJan);
                    yVals_year.add(entryJan);
                    setJan = new LineDataSet(yVals_year, "Jan");

                    Entry entryFeb = new Entry(2, countFeb);
                    yVals_year.add(entryFeb);
                    setFeb = new LineDataSet(yVals_year, "Feb");

                    Entry entryMar = new Entry(3, countMar);
                    yVals_year.add(entryMar);
                    setMar = new LineDataSet(yVals_year, "Mar");

                    Entry entryApr = new Entry(4, countApr);
                    yVals_year.add(entryApr);
                    setApr = new LineDataSet(yVals_year, "Apr");

                    Entry entryMay = new Entry(5, countMay);
                    yVals_year.add(entryMay);
                    setMay = new LineDataSet(yVals_year, "May");

                    Entry entryJun = new Entry(6, countJun);
                    yVals_year.add(entryJun);
                    setJun = new LineDataSet(yVals_year, "Jun");

                    Entry entryJul = new Entry(7, countJul);
                    yVals_year.add(entryJul);
                    setJul = new LineDataSet(yVals_year, "Jul");

                    Entry entryAug = new Entry(8, countAug);
                    yVals_year.add(entryAug);
                    setAug = new LineDataSet(yVals_year, "Aug");

                    Entry entrySep = new Entry(9, countSep);
                    yVals_year.add(entrySep);
                    setSep = new LineDataSet(yVals_year, "Sep");

                    Entry entryOct = new Entry(10, countOct);
                    yVals_year.add(entryOct);
                    setOct = new LineDataSet(yVals_year, "Oct");

                    Entry entryNov = new Entry(11, countNov);
                    yVals_year.add(entryNov);
                    setNov = new LineDataSet(yVals_year, "Nov");

                    Entry entryDec = new Entry(12, countDec);
                    yVals_year.add(entryDec);
                    setDec = new LineDataSet(yVals_year, "Dec");


                    LineData data = new LineData(setJan, setFeb, setMar, setApr, setMay, setJun, setJul, setAug, setSep, setOct, setNov, setDec);
                    chartC.setData(data);

                    final String[] months = new String[]{year, "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
                            "Sep", "Oct", "Nov", "Dec"};

                    XAxis xAxis = chartC.getXAxis();
                    xAxis.setGranularity(1f);
                    xAxis.setValueFormatter(new MyXAxisValueFormatter(months));

                    setJan.setColor(Color.YELLOW);
                    setFeb.setColor(Color.YELLOW);
                    setMar.setColor(Color.YELLOW);
                    setApr.setColor(Color.YELLOW);
                    setMay.setColor(Color.YELLOW);
                    setJun.setColor(Color.YELLOW);
                    setJul.setColor(Color.YELLOW);
                    setAug.setColor(Color.YELLOW);
                    setSep.setColor(Color.YELLOW);
                    setOct.setColor(Color.YELLOW);
                    setNov.setColor(Color.YELLOW);
                    setDec.setColor(Color.YELLOW);

                    setJan.setCircleColors(Color.BLUE);
                    setFeb.setCircleColors(Color.BLUE);
                    setMar.setCircleColors(Color.BLUE);
                    setApr.setCircleColors(Color.BLUE);
                    setMay.setCircleColors(Color.BLUE);
                    setJun.setCircleColors(Color.BLUE);
                    setJul.setCircleColors(Color.BLUE);
                    setAug.setCircleColors(Color.BLUE);
                    setSep.setCircleColors(Color.BLUE);
                    setOct.setCircleColors(Color.BLUE);
                    setNov.setCircleColors(Color.BLUE);
                    setDec.setCircleColors(Color.BLUE);

                    setJan.setCircleColorHole(Color.BLUE);
                    setFeb.setCircleColorHole(Color.BLUE);
                    setMar.setCircleColorHole(Color.BLUE);
                    setApr.setCircleColorHole(Color.BLUE);
                    setMay.setCircleColorHole(Color.BLUE);
                    setJun.setCircleColorHole(Color.BLUE);
                    setJul.setCircleColorHole(Color.BLUE);
                    setAug.setCircleColorHole(Color.BLUE);
                    setSep.setCircleColorHole(Color.BLUE);
                    setOct.setCircleColorHole(Color.BLUE);
                    setNov.setCircleColorHole(Color.BLUE);
                    setDec.setCircleColorHole(Color.BLUE);

                    data.notifyDataChanged();
                    chartC.notifyDataSetChanged();
                    chartC.invalidate();

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

    public class MyYAxisValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;

        public MyYAxisValueFormatter() {

            // format values to 1 decimal digit
            mFormat = new DecimalFormat("###,###,##0" + "mL");
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            return mFormat.format(value);
        }

        /** this is only needed if numbers are returned, else return 0 */
        public int getDecimalDigits() { return 1; }
    }

}
