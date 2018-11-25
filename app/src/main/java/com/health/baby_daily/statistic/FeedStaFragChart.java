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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
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
import com.health.baby_daily.model.Feed;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FeedStaFragChart extends Fragment {

    private View view;
    private HorizontalBarChart chartA;
    private BarChart chartB;
    private LineChart chartC;
    private DatabaseReference table_feed, table_feedB, table_feedC;
    private Query query, queryB, queryC;

    private String bId;

    private BarDataSet setA, setB, setC, setD, setE, setF, setG;
    private BarDataSet set1, set2, set3, set4, set5, set6, set7;
    private LineDataSet setJan, setFeb, setMar, setApr, setMay, setJun, setJul, setAug, setSep, setOct, setNov, setDec;
    float countA = 0, countB = 0, countC = 0, countD = 0, countE = 0, countF = 0, countG = 0;
    float count1 = 0, count2 = 0, count3 = 0, count4 = 0, count5 = 0, count6 = 0, count7 = 0;
    float countJan = 0, countFeb = 0, countMar = 0, countApr = 0, countMay = 0, countJun = 0, countJul = 0;
    float countAug = 0, countSep = 0, countOct = 0, countNov = 0, countDec = 0;
    private String mon, tue, wed, thu, fri, stu, sun;
    private String curJan, curFeb, curMar, curApr, curMay, curJun, curJul, curAug, curSep, curOct, curNov, curDec;
    private Date dateA, dateB, dateC, dateD, dateE, dateF, dateG, dateH;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sleep_chart_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefBaby = getContext().getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        chartA = view.findViewById(R.id.chartA);
        chartA.getDescription().setText("Feed Event");
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
        chartA.getXAxis().setLabelCount(7, false);
        chartA.animateXY(3000,3000);

        YAxis rightA = chartA.getAxisRight();
        rightA.setValueFormatter(new MyYAxisValueFormatter());
        rightA.setGranularity(1);

        Legend legend = chartA.getLegend();
        legend.setWordWrapEnabled(true);

        chartB = view.findViewById(R.id.chartB);
        chartB.getDescription().setText("Feed Event");
        chartB.setTouchEnabled(true);
        chartB.setScaleEnabled(false);
        chartB.getXAxis().setDrawLabels(true);
        chartB.getAxisRight().setDrawLabels(false);
        chartB.setFitBars(true);
        chartB.getAxisLeft().setGranularity(1);
        chartB.getXAxis().setLabelCount(7, false);
        chartB.getAxisRight().setDrawGridLines(false);
        chartB.animateXY(3000,3000);

        YAxis leftB = chartB.getAxisLeft();
        leftB.setValueFormatter(new MyYAxisValueFormatter());
        leftB.setGranularity(1);

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

        YAxis leftC = chartC.getAxisLeft();
        leftC.setValueFormatter(new MyYAxisValueFormatter());
        leftC.setGranularity(1);

        Legend legendC = chartC.getLegend();
        legendC.setWordWrapEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        //today
        final ArrayList<BarEntry> yVals_A = new ArrayList<>();
        final ArrayList<BarEntry> yVals_B = new ArrayList<>();
        final ArrayList<BarEntry> yVals_C = new ArrayList<>();
        final ArrayList<BarEntry> yVals_D = new ArrayList<>();
        final ArrayList<BarEntry> yVals_E = new ArrayList<>();
        final ArrayList<BarEntry> yVals_F = new ArrayList<>();
        final ArrayList<BarEntry> yVals_G = new ArrayList<>();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        final String currentDate = dateFormat.format(date);

        table_feed = FirebaseDatabase.getInstance().getReference().child("Feed");
        query = table_feed.orderByChild("dateTime");
        query.keepSynced(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                countA = 0; countB = 0; countC = 0; countD = 0;
                countE = 0; countF = 0; countG = 0;

                if(dataSnapshot.exists()){
                    float mLValueA = 0.0f;  float ozAmtA = 0.0f;
                    float ozValueA = 0.0f;
                    float mLValueB = 0.0f;  float ozAmtB = 0.0f;
                    float ozValueB = 0.0f;
                    float mLValueC = 0.0f;  float ozAmtC = 0.0f;
                    float ozValueC = 0.0f;
                    float mLValueD = 0.0f;  float ozAmtD = 0.0f;
                    float ozValueD = 0.0f;
                    float mLValueE = 0.0f;  float ozAmtE = 0.0f;
                    float ozValueE = 0.0f;
                    float mLValueF = 0.0f;  float ozAmtF = 0.0f;
                    float ozValueF = 0.0f;
                    float mLValueG = 0.0f;  float ozAmtG = 0.0f;
                    float ozValueG = 0.0f;


                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Feed feed = snapshot.getValue(Feed.class);
                        if(feed.getBaby_id().equals(bId)) {
                            if (feed.getDateTime().startsWith(currentDate) && feed.getType().equals("Fruit")) {
                                if(feed.getUnits().equals("g")){
                                    mLValueA = mLValueA + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmtA = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValueA = ozValueA + ozAmtA;
                                }

                                countA = mLValueA+ozValueA;
                            }

                            if (feed.getDateTime().startsWith(currentDate) && feed.getType().equals("Vegetables")) {
                                if(feed.getUnits().equals("g")){
                                    mLValueB = mLValueB + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmtB = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValueB = ozValueB + ozAmtB;
                                }

                                countB = mLValueB+ozValueB;
                            }

                            if (feed.getDateTime().startsWith(currentDate) && feed.getType().equals("Meats & Proteins")) {
                                if(feed.getUnits().equals("g")){
                                    mLValueC = mLValueC + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmtC = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValueC = ozValueC + ozAmtC;
                                }

                                countC = mLValueC+ozValueC;
                            }

                            if (feed.getDateTime().startsWith(currentDate) && feed.getType().equals("Grains")) {
                                if(feed.getUnits().equals("g")){
                                    mLValueD = mLValueD + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmtD = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValueD = ozValueD + ozAmtD;
                                }

                                countD = mLValueD+ozValueD;
                            }

                            if (feed.getDateTime().startsWith(currentDate) && feed.getType().equals("Dairy")) {
                                if(feed.getUnits().equals("g")){
                                    mLValueE = mLValueE + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmtE = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValueE = ozValueE + ozAmtE;
                                }

                                countE = mLValueE+ozValueE;
                            }

                            if (feed.getDateTime().startsWith(currentDate) && feed.getType().equals("Bread")) {
                                if(feed.getUnits().equals("g")){
                                    mLValueF = mLValueF + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmtF = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValueF = ozValueF + ozAmtF;
                                }

                                countF = mLValueF+ozValueF;
                            }

                            if (feed.getDateTime().startsWith(currentDate) && feed.getType().equals("Cereal")) {
                                if(feed.getUnits().equals("g")){
                                    mLValueG = mLValueG + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmtG = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValueG = ozValueG + ozAmtG;
                                }

                                countG = mLValueG+ozValueG;
                            }
                        }
                    }

                    yVals_A.add(new BarEntry(0f, countA));
                    setA = new BarDataSet(yVals_A, "Fruit");
                    setA.setDrawValues(true);

                    yVals_B.add(new BarEntry(1f, countB));
                    setB = new BarDataSet(yVals_B, "Vegetables");
                    setB.setDrawValues(true);

                    yVals_C.add(new BarEntry(2f, countC));
                    setC = new BarDataSet(yVals_C, "Meats");
                    setC.setDrawValues(true);

                    yVals_D.add(new BarEntry(3f, countD));
                    setD = new BarDataSet(yVals_D, "Grains");
                    setD.setDrawValues(true);

                    yVals_E.add(new BarEntry(4f, countE));
                    setE = new BarDataSet(yVals_E, "Dairy");
                    setE.setDrawValues(true);

                    yVals_F.add(new BarEntry(5f, countF));
                    setF = new BarDataSet(yVals_F, "Bread");
                    setF.setDrawValues(true);

                    yVals_G.add(new BarEntry(6f, countG));
                    setG = new BarDataSet(yVals_G, "Cereal");
                    setG.setDrawValues(true);

                    setA.setColor(Color.BLUE);
                    setB.setColor(Color.YELLOW);
                    setC.setColor(Color.CYAN);
                    setD.setColor(Color.RED);
                    setE.setColor(Color.GRAY);
                    setF.setColor(Color.GREEN);
                    setG.setColor(Color.MAGENTA);

                    BarData data = new BarData(setA, setB, setC, setD, setE, setF, setG);
                    chartA.setData(data);
                    final String[] type = new String[]{"Fruit", "Vegetables", "Meats", "Grains", "Dairy", "Bread", "Cereal"};

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

        table_feedB = FirebaseDatabase.getInstance().getReference().child("Feed");
        queryB = table_feedB.orderByChild("dateTime");
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
                        Feed feed = snapshot.getValue(Feed.class);
                        if(feed.getBaby_id().equals(bId)) {
                            String created = feed.getCreated();

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
                                if(feed.getUnits().equals("g")){
                                    mLValueMon = mLValueMon + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmtMon = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValueMon = ozValueMon + ozAmtMon;
                                }
                                count1 = mLValueMon+ ozValueMon;
                            }else if(dateA.equals(dateC)){
                                if(feed.getUnits().equals("g")){
                                    mLValueTue = mLValueTue + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmtTue = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValueTue = ozValueTue + ozAmtTue;
                                }
                                count2 = mLValueTue+ ozValueTue;
                            }else if(dateA.equals(dateD)){
                                if(feed.getUnits().equals("g")){
                                    mLValueWed = mLValueWed + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmtWed = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValueWed = ozValueWed + ozAmtWed;
                                }
                                count3 = mLValueWed + ozValueWed;
                            }else if(dateA.equals(dateE)){
                                if(feed.getUnits().equals("g")){
                                    mLValueThu = mLValueThu + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmtThu = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValueThu = ozValueThu + ozAmtThu;
                                }
                                count4 = mLValueThu + ozValueThu;
                            }else if(dateA.equals(dateF)){
                                if(feed.getUnits().equals("g")){
                                    mLValueFri = mLValueFri + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmtFri = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValueFri = ozValueFri + ozAmtFri;
                                }
                                count5 = mLValueFri+ ozValueFri;
                            }else if(dateA.equals(dateG)){
                                if(feed.getUnits().equals("g")){
                                    mLValueStu = mLValueStu + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmtStu = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValueStu = ozValueStu + ozAmtStu;
                                }
                                count6 = mLValueStu + ozValueStu;
                            }else if(dateA.equals(dateH)){
                                if(feed.getUnits().equals("g")){
                                    mLValueSun = mLValueSun + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmtSun = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValueSun = ozValueSun + ozAmtSun;
                                }
                                count7 = mLValueSun + ozValueSun;
                            }

                        }
                    }

                    yVals_Monday.add(new BarEntry(1, count1));
                    set1 = new BarDataSet(yVals_Monday, "Mon");

                    yVals_Tuesday.add(new BarEntry(2, count2));
                    set2 = new BarDataSet(yVals_Tuesday, "Tue");

                    yVals_Wednesday.add(new BarEntry(3, count3));
                    set3 = new BarDataSet(yVals_Wednesday, "Wed");

                    yVals_Thursday.add(new BarEntry(4, count4));
                    set4 = new BarDataSet(yVals_Thursday, "Thu");

                    yVals_Friday.add(new BarEntry(5, count5));
                    set5 = new BarDataSet(yVals_Friday, "Fri");

                    yVals_Saturday.add(new BarEntry(6, count6));
                    set6 = new BarDataSet(yVals_Saturday, "Sat");

                    yVals_Sunday.add(new BarEntry(7, count7));
                    set7 = new BarDataSet(yVals_Sunday, "Sun");

                    set1.setColor(Color.BLUE);
                    set2.setColor(Color.YELLOW);
                    set3.setColor(Color.CYAN);
                    set4.setColor(Color.RED);
                    set5.setColor(Color.GRAY);
                    set6.setColor(Color.GREEN);
                    set7.setColor(Color.MAGENTA);
                    BarData data = new BarData(set1, set2, set3, set4, set5, set6, set7);
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

        table_feedC = FirebaseDatabase.getInstance().getReference().child("Feed");
        queryC = table_feedC.orderByChild("dateTime");
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
                        Feed feed = snapshot.getValue(Feed.class);
                        if(feed.getBaby_id().equals(bId)) {
                            if(feed.getDateTime().contains(curJan)){
                                if(feed.getUnits().equals("g")){
                                    mLValue1 = mLValue1 + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmt1 = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValue1 = ozValue1 + ozAmt1;
                                }
                                countJan = mLValue1 + ozValue1;
                            }else if(feed.getDateTime().contains(curFeb)){
                                if(feed.getUnits().equals("g")){
                                    mLValue2 = mLValue2 + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmt2 = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValue2 = ozValue2 + ozAmt2;
                                }
                                countFeb = mLValue2 + ozValue2;
                            }else if(feed.getDateTime().contains(curMar)){
                                if(feed.getUnits().equals("g")){
                                    mLValue3 = mLValue3 + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmt3 = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValue3 = ozValue3 + ozAmt3;
                                }
                                countMar = mLValue3 + ozValue3;
                            }else if(feed.getDateTime().contains(curApr)){
                                if(feed.getUnits().equals("g")){
                                    mLValue4 = mLValue4 + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmt4 = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValue4 = ozValue4 + ozAmt4;
                                }
                                countApr = mLValue4 + ozValue4;
                            }else if(feed.getDateTime().contains(curMay)){
                                if(feed.getUnits().equals("g")){
                                    mLValue5 = mLValue5 + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmt5 = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValue5 = ozValue5 + ozAmt5;
                                }
                                countMay = mLValue5 + ozValue5;
                            }else if(feed.getDateTime().contains(curJun)){
                                if(feed.getUnits().equals("g")){
                                    mLValue6 = mLValue6 + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmt6 = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValue6 = ozValue6 + ozAmt6;
                                }
                                countJun = mLValue6 + ozValue6;
                            }else if(feed.getDateTime().contains(curJul)){
                                if(feed.getUnits().equals("g")){
                                    mLValue7 = mLValue7 + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmt7 = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValue7 = ozValue7 + ozAmt7;
                                }
                                countJul = mLValue7 + ozValue7;
                            }else if(feed.getDateTime().contains(curAug)){
                                if(feed.getUnits().equals("g")){
                                    mLValue8 = mLValue8 + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmt8 = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValue8 = ozValue8 + ozAmt8;
                                }
                                countAug = mLValue8 + ozValue8;
                            }else if(feed.getDateTime().contains(curSep)){
                                if(feed.getUnits().equals("g")){
                                    mLValue9 = mLValue9 + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmt9 = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValue9 = ozValue9 + ozAmt9;
                                }
                                countSep = mLValue9 + ozValue9;
                            }else if(feed.getDateTime().contains(curOct)){
                                if(feed.getUnits().equals("g")){
                                    mLValue10 = mLValue10 + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmt10 = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValue10 = ozValue10 + ozAmt10;
                                }
                                countOct = mLValue10 + ozValue10;
                            }else if(feed.getDateTime().contains(curNov)){
                                if(feed.getUnits().equals("g")){
                                    mLValue11 = mLValue11 + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmt11 = Float.valueOf(feed.getAmount())*28.35f;
                                    ozValue11 = ozValue11 + ozAmt11;
                                }
                                countNov = mLValue11 + ozValue11;
                            }else if(feed.getDateTime().contains(curDec)){
                                if(feed.getUnits().equals("g")){
                                    mLValue12 = mLValue12 + Float.valueOf(feed.getAmount());
                                }else if(feed.getUnits().equals("Oz")){
                                    ozAmt12 = Float.valueOf(feed.getAmount())*28.35f;
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
            mFormat = new DecimalFormat("###,###,##0" + "g");
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
