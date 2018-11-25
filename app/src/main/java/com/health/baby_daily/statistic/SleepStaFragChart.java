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
import com.health.baby_daily.model.Sleep;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SleepStaFragChart extends Fragment {

    private View view;
    private HorizontalBarChart chartA;
    private BarChart chartB;
    private LineChart chartC;
    private DatabaseReference table_sleep, table_sleepB, table_sleepC;
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
        view = inflater.inflate(R.layout.sleep_chart_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefBaby = getContext().getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        chartA = view.findViewById(R.id.chartA);
        chartA.getDescription().setText("Sleep Event");
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
        chartA.getXAxis().setLabelCount(1, false);
        chartA.animateXY(3000,3000);

        chartB = view.findViewById(R.id.chartB);
        chartB.getDescription().setText("Sleep Event");
        chartB.setTouchEnabled(false);
        chartB.getXAxis().setDrawLabels(true);
        chartB.getAxisRight().setDrawLabels(false);
        chartB.setFitBars(true);
        chartB.getAxisLeft().setGranularity(1);
        chartB.getXAxis().setLabelCount(7, false);
        chartB.getAxisRight().setDrawGridLines(false);
        chartB.animateXY(3000,3000);

        chartC = view.findViewById(R.id.chartC);
        chartC.getDescription().setText("Sleep Event");
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

        YAxis leftB = chartB.getAxisLeft();
        leftB.setValueFormatter(new MyYAxisValueFormatter());
        leftB.setGranularity(1);

        YAxis left = chartC.getAxisLeft();
        left.setValueFormatter(new MyYAxisValueFormatter());
        left.setGranularity(1);

        Legend legend = chartC.getLegend();
        legend.setWordWrapEnabled(true);

    }

    @Override
    public void onStart() {
        super.onStart();
        //today
        final ArrayList<BarEntry> yVals_Today = new ArrayList<>();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        final String currentDate = dateFormat.format(date);

        final DateFormat subPattern = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        table_sleep = FirebaseDatabase.getInstance().getReference().child("Sleep");
        query = table_sleep.orderByChild("created");
        query.keepSynced(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                countB = 0;
                countF = 0;
                Date dateStart = null;
                Date dateEnd = null;
                float total = 0f;
                float toHours = 0f;
                long diffMinutes = 0, diffHours = 0, toMin = 0;

                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        total = 0;
                        toHours = 0;
                        Sleep sleep = snapshot.getValue(Sleep.class);
                        if(sleep.getBabyId().equals(bId)) {
                            if (sleep.getCreated().equals(currentDate)) {
                                String startDate = sleep.getStartTime().toString();
                                String endDate = sleep.getEndTime().toString();

                                try {
                                    dateStart = subPattern.parse(startDate);
                                    dateEnd = subPattern.parse(endDate);

                                    long diff = dateEnd.getTime() - dateStart.getTime();
                                    diffHours = diff / 3600000;
                                    diffMinutes = (diff % 3600000) / 60000;
                                    if(diffHours>0){
                                        toMin = diffHours*60;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                total = toMin + diffMinutes;
                                toHours = total * 0.0166667f;
                                countB = countB + toHours;
                            }
                        }
                    }

                    yVals_Today.add(new BarEntry(1f, countB));
                    set1 = new BarDataSet(yVals_Today, "Today");

                    set1.setColor(Color.BLUE);
                    BarData data = new BarData(set1);
                    chartA.setData(data);
                    String setValue = String.format("%.2f", countB);
                    final String[] type = new String[]{"", setValue};

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

        table_sleepB = FirebaseDatabase.getInstance().getReference().child("Sleep");
        queryB = table_sleepB.orderByChild("created");
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
                Date dateStartMon = null, dateEndMon = null;
                float totalMon = 0f;
                float toHoursMon = 0f;
                long diffMinutesMon = 0, diffHoursMon = 0, toMinMon = 0;

                Date dateStartTue = null, dateEndTue = null;
                float totalTue = 0f;
                float toHoursTue = 0f;
                long diffMinutesTue = 0, diffHoursTue = 0, toMinTue = 0;

                Date dateStartWed = null, dateEndWed = null;
                float totalWed = 0f;
                float toHoursWed = 0f;
                long diffMinutesWed = 0, diffHoursWed = 0, toMinWed = 0;

                Date dateStartThu = null, dateEndThu = null;
                float totalThu = 0f;
                float toHoursThu = 0f;
                long diffMinutesThu = 0, diffHoursThu = 0, toMinThu = 0;

                Date dateStartFri = null, dateEndFri = null;
                float totalFri = 0f;
                float toHoursFri = 0f;
                long diffMinutesFri = 0, diffHoursFri = 0, toMinFri = 0;

                Date dateStartStu = null, dateEndStu = null;
                float totalStu = 0f;
                float toHoursStu = 0f;
                long diffMinutesStu = 0, diffHoursStu = 0, toMinStu = 0;

                Date dateStartSun = null, dateEndSun = null;
                float totalSun = 0f;
                float toHoursSun = 0f;
                long diffMinutesSun = 0, diffHoursSun = 0, toMinSun = 0;

                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Sleep sleep = snapshot.getValue(Sleep.class);
                        if(sleep.getBabyId().equals(bId)) {
                            String created = sleep.getCreated();

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
                                totalMon = 0;
                                toHoursMon = 0;
                                String startDateMon = sleep.getStartTime().toString();
                                String endDateMon = sleep.getEndTime().toString();

                                try {
                                    dateStartMon = subPattern.parse(startDateMon);
                                    dateEndMon = subPattern.parse(endDateMon);

                                    long diff = dateEndMon.getTime() - dateStartMon.getTime();
                                    diffHoursMon = diff / 3600000;
                                    diffMinutesMon = (diff % 3600000) / 60000;
                                    if(diffHoursMon>0){
                                        toMinMon = diffHoursMon*60;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                totalMon = toMinMon + diffMinutesMon;
                                toHoursMon = totalMon * 0.0166667f;
                                count1 = count1 + toHoursMon;
                            }else if(dateA.equals(dateC)){
                                totalTue = 0;
                                toHoursTue = 0;
                                String startDateTue = sleep.getStartTime().toString();
                                String endDateTue = sleep.getEndTime().toString();

                                try {
                                    dateStartTue = subPattern.parse(startDateTue);
                                    dateEndTue = subPattern.parse(endDateTue);

                                    long diff = dateEndTue.getTime() - dateStartTue.getTime();
                                    diffHoursTue = diff / 3600000;
                                    diffMinutesTue = (diff % 3600000) / 60000;
                                    if(diffHoursTue>0){
                                        toMinTue = diffHoursTue*60;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                totalTue = toMinTue + diffMinutesTue;
                                toHoursTue = totalTue * 0.0166667f;
                                count2 = count2 + toHoursTue;
                            }else if(dateA.equals(dateD)){
                                totalWed = 0;
                                toHoursWed = 0;
                                String startDateWed = sleep.getStartTime().toString();
                                String endDateWed = sleep.getEndTime().toString();

                                try {
                                    dateStartWed = subPattern.parse(startDateWed);
                                    dateEndWed = subPattern.parse(endDateWed);

                                    long diff = dateEndWed.getTime() - dateStartWed.getTime();
                                    diffHoursWed = diff / 3600000;
                                    diffMinutesWed = (diff % 3600000) / 60000;
                                    if(diffHoursWed>0){
                                        toMinWed = diffHoursWed*60;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                totalWed = toMinWed + diffMinutesWed;
                                toHoursWed = totalWed * 0.0166667f;
                                count3 = count3 + toHoursWed;
                            }else if(dateA.equals(dateE)){
                                totalThu = 0;
                                toHoursThu = 0;
                                String startDateThu = sleep.getStartTime().toString();
                                String endDateThu = sleep.getEndTime().toString();

                                try {
                                    dateStartThu = subPattern.parse(startDateThu);
                                    dateEndThu = subPattern.parse(endDateThu);

                                    long diff = dateEndThu.getTime() - dateStartThu.getTime();
                                    diffHoursThu = diff / 3600000;
                                    diffMinutesThu = (diff % 3600000) / 60000;
                                    if(diffHoursThu>0){
                                        toMinThu = diffHoursThu*60;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                totalThu = toMinThu + diffMinutesThu;
                                toHoursThu = totalThu * 0.0166667f;
                                count4 = count4 + toHoursThu;
                            }else if(dateA.equals(dateF)){
                                totalFri = 0;
                                toHoursFri = 0;
                                String startDateFri = sleep.getStartTime().toString();
                                String endDateFri = sleep.getEndTime().toString();

                                try {
                                    dateStartFri = subPattern.parse(startDateFri);
                                    dateEndFri = subPattern.parse(endDateFri);

                                    long diff = dateEndFri.getTime() - dateStartFri.getTime();
                                    diffHoursFri = diff / 3600000;
                                    diffMinutesFri = (diff % 3600000) / 60000;
                                    if(diffHoursFri>0){
                                        toMinFri = diffHoursFri*60;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                totalFri = toMinFri + diffMinutesFri;
                                toHoursFri = totalFri * 0.0166667f;
                                count5 = count5 + toHoursFri;
                            }else if(dateA.equals(dateG)){
                                totalStu = 0;
                                toHoursStu = 0;
                                String startDateStu = sleep.getStartTime().toString();
                                String endDateStu = sleep.getEndTime().toString();

                                try {
                                    dateStartStu = subPattern.parse(startDateStu);
                                    dateEndStu = subPattern.parse(endDateStu);

                                    long diff = dateEndStu.getTime() - dateStartStu.getTime();
                                    diffHoursStu = diff / 3600000;
                                    diffMinutesStu = (diff % 3600000) / 60000;
                                    if(diffHoursStu>0){
                                        toMinStu = diffHoursStu*60;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                totalStu = toMinStu + diffMinutesStu;
                                toHoursStu = totalStu * 0.0166667f;
                                count6 = count6 + toHoursStu;
                            }else if(dateA.equals(dateH)){
                                totalSun = 0;
                                toHoursSun = 0;
                                String startDateSun = sleep.getStartTime().toString();
                                String endDateSun = sleep.getEndTime().toString();

                                try {
                                    dateStartSun = subPattern.parse(startDateSun);
                                    dateEndSun = subPattern.parse(endDateSun);

                                    long diff = dateEndSun.getTime() - dateStartSun.getTime();
                                    diffHoursSun = diff / 3600000;
                                    diffMinutesSun = (diff % 3600000) / 60000;
                                    if(diffHoursSun>0){
                                        toMinSun = diffHoursSun*60;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                totalSun = toMinSun + diffMinutesSun;
                                toHoursSun = totalSun * 0.0166667f;
                                count7 = count7 + toHoursSun;
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

        table_sleepC = FirebaseDatabase.getInstance().getReference().child("Sleep");
        queryC = table_sleepC.orderByChild("created");
        queryC.keepSynced(true);
        queryC.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                countJan = 0; countFeb = 0; countMar = 0; countApr = 0; countMay = 0; countJun = 0;
                countJul = 0; countAug = 0; countSep = 0; countOct = 0; countNov = 0; countDec = 0;

                Date dateStart1 = null, dateEnd1 = null;
                float total1 = 0f;
                float toHours1 = 0f;
                long diffMinutes1 = 0, diffHours1 = 0, toMin1 = 0;

                Date dateStart2 = null, dateEnd2 = null;
                float total2 = 0f;
                float toHours2 = 0f;
                long diffMinutes2 = 0, diffHours2 = 0, toMin2 = 0;

                Date dateStart3 = null, dateEnd3 = null;
                float total3 = 0f;
                float toHours3 = 0f;
                long diffMinutes3 = 0, diffHours3 = 0, toMin3 = 0;

                Date dateStart4 = null, dateEnd4 = null;
                float total4 = 0f;
                float toHours4 = 0f;
                long diffMinutes4 = 0, diffHours4 = 0, toMin4 = 0;

                Date dateStart5 = null, dateEnd5 = null;
                float total5 = 0f;
                float toHours5 = 0f;
                long diffMinutes5 = 0, diffHours5 = 0, toMin5 = 0;

                Date dateStart6 = null, dateEnd6 = null;
                float total6 = 0f;
                float toHours6 = 0f;
                long diffMinutes6 = 0, diffHours6 = 0, toMin6 = 0;

                Date dateStart7 = null, dateEnd7 = null;
                float total7 = 0f;
                float toHours7 = 0f;
                long diffMinutes7 = 0, diffHours7 = 0, toMin7 = 0;

                Date dateStart8 = null, dateEnd8 = null;
                float total8 = 0f;
                float toHours8 = 0f;
                long diffMinutes8 = 0, diffHours8 = 0, toMin8 = 0;

                Date dateStart9 = null, dateEnd9 = null;
                float total9 = 0f;
                float toHours9 = 0f;
                long diffMinutes9 = 0, diffHours9 = 0, toMin9 = 0;

                Date dateStart10 = null, dateEnd10 = null;
                float total10 = 0f;
                float toHours10 = 0f;
                long diffMinutes10 = 0, diffHours10 = 0, toMin10 = 0;

                Date dateStart11 = null, dateEnd11 = null;
                float total11 = 0f;
                float toHours11 = 0f;
                long diffMinutes11 = 0, diffHours11 = 0, toMin11 = 0;

                Date dateStart12 = null, dateEnd12 = null;
                float total12 = 0f;
                float toHours12 = 0f;
                long diffMinutes12 = 0, diffHours12 = 0, toMin12 = 0;

                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Sleep sleep = snapshot.getValue(Sleep.class);
                        if(sleep.getBabyId().equals(bId)) {
                            if(sleep.getStartTime().contains(curJan)){
                                total1 = 0;
                                toHours1 = 0;
                                String startDate1 = sleep.getStartTime().toString();
                                String endDate1 = sleep.getEndTime().toString();

                                try {
                                    dateStart1 = subPattern.parse(startDate1);
                                    dateEnd1 = subPattern.parse(endDate1);

                                    long diff = dateEnd1.getTime() - dateStart1.getTime();
                                    diffHours1 = diff / 3600000;
                                    diffMinutes1 = (diff % 3600000) / 60000;
                                    if(diffHours1>0){
                                        toMin1 = diffHours1*60;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                total1 = toMin1 + diffMinutes1;
                                toHours1 = total1 * 0.0166667f;
                                countJan = countJan + toHours1;
                            }else if(sleep.getStartTime().contains(curFeb)){
                                total2 = 0;
                                toHours2 = 0;
                                String startDate2 = sleep.getStartTime().toString();
                                String endDate2 = sleep.getEndTime().toString();

                                try {
                                    dateStart2 = subPattern.parse(startDate2);
                                    dateEnd2 = subPattern.parse(endDate2);

                                    long diff = dateEnd2.getTime() - dateStart2.getTime();
                                    diffHours2 = diff / 3600000;
                                    diffMinutes2 = (diff % 3600000) / 60000;
                                    if(diffHours2>0){
                                        toMin2 = diffHours2*60;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                total2 = toMin2 + diffMinutes2;
                                toHours2 = total2 * 0.0166667f;
                                countFeb = countFeb + toHours2;
                            }else if(sleep.getStartTime().contains(curMar)){
                                total3 = 0;
                                toHours3 = 0;
                                String startDate3 = sleep.getStartTime().toString();
                                String endDate3 = sleep.getEndTime().toString();

                                try {
                                    dateStart3 = subPattern.parse(startDate3);
                                    dateEnd3 = subPattern.parse(endDate3);

                                    long diff = dateEnd3.getTime() - dateStart3.getTime();
                                    diffHours3 = diff / 3600000;
                                    diffMinutes3 = (diff % 3600000) / 60000;
                                    if(diffHours3>0){
                                        toMin3 = diffHours3*60;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                total3 = toMin3 + diffMinutes3;
                                toHours3 = total3 * 0.0166667f;
                                countMar = countMar + toHours3;
                            }else if(sleep.getStartTime().contains(curApr)){
                                total4 = 0;
                                toHours4 = 0;
                                String startDate4 = sleep.getStartTime().toString();
                                String endDate4 = sleep.getEndTime().toString();

                                try {
                                    dateStart4 = subPattern.parse(startDate4);
                                    dateEnd4 = subPattern.parse(endDate4);

                                    long diff = dateEnd4.getTime() - dateStart4.getTime();
                                    diffHours4 = diff / 3600000;
                                    diffMinutes4 = (diff % 3600000) / 60000;
                                    if(diffHours4>0){
                                        toMin4 = diffHours4*60;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                total4 = toMin4 + diffMinutes4;
                                toHours4 = total4 * 0.0166667f;
                                countApr = countApr + toHours4;
                            }else if(sleep.getStartTime().contains(curMay)){
                                total5 = 0;
                                toHours5 = 0;
                                String startDate5 = sleep.getStartTime().toString();
                                String endDate5 = sleep.getEndTime().toString();

                                try {
                                    dateStart5 = subPattern.parse(startDate5);
                                    dateEnd5 = subPattern.parse(endDate5);

                                    long diff = dateEnd5.getTime() - dateStart5.getTime();
                                    diffHours5 = diff / 3600000;
                                    diffMinutes5 = (diff % 3600000) / 60000;
                                    if(diffHours5>0){
                                        toMin5 = diffHours5*60;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                total5 = toMin5 + diffMinutes5;
                                toHours5 = total5 * 0.0166667f;
                                countMay = countMay + toHours5;
                            }else if(sleep.getStartTime().contains(curJun)){
                                total6 = 0;
                                toHours6 = 0;
                                String startDate6 = sleep.getStartTime().toString();
                                String endDate6 = sleep.getEndTime().toString();

                                try {
                                    dateStart6 = subPattern.parse(startDate6);
                                    dateEnd6 = subPattern.parse(endDate6);

                                    long diff = dateEnd6.getTime() - dateStart6.getTime();
                                    diffHours6 = diff / 3600000;
                                    diffMinutes6 = (diff % 3600000) / 60000;
                                    if(diffHours6>0){
                                        toMin6 = diffHours6*60;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                total6 = toMin6 + diffMinutes6;
                                toHours6 = total6 * 0.0166667f;
                                countJun = countJun + toHours6;
                            }else if(sleep.getStartTime().contains(curJul)){
                                total7 = 0;
                                toHours7 = 0;
                                String startDate7 = sleep.getStartTime().toString();
                                String endDate7 = sleep.getEndTime().toString();

                                try {
                                    dateStart7 = subPattern.parse(startDate7);
                                    dateEnd7 = subPattern.parse(endDate7);

                                    long diff = dateEnd7.getTime() - dateStart7.getTime();
                                    diffHours7 = diff / 3600000;
                                    diffMinutes7 = (diff % 3600000) / 60000;
                                    if(diffHours7>0){
                                        toMin7 = diffHours7*60;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                total7 = toMin7 + diffMinutes7;
                                toHours7 = total7 * 0.0166667f;
                                countJul = countJul + toHours7;
                            }else if(sleep.getStartTime().contains(curAug)){
                                total8 = 0;
                                toHours8 = 0;
                                String startDate8 = sleep.getStartTime().toString();
                                String endDate8 = sleep.getEndTime().toString();

                                try {
                                    dateStart8 = subPattern.parse(startDate8);
                                    dateEnd8 = subPattern.parse(endDate8);

                                    long diff = dateEnd8.getTime() - dateStart8.getTime();
                                    diffHours8 = diff / 3600000;
                                    diffMinutes8 = (diff % 3600000) / 60000;
                                    if(diffHours8>0){
                                        toMin8 = diffHours8*60;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                total8 = toMin8 + diffMinutes8;
                                toHours8 = total8 * 0.0166667f;
                                countAug = countAug + toHours8;
                            }else if(sleep.getStartTime().contains(curSep)){
                                total9 = 0;
                                toHours9 = 0;
                                String startDate9 = sleep.getStartTime().toString();
                                String endDate9 = sleep.getEndTime().toString();

                                try {
                                    dateStart9 = subPattern.parse(startDate9);
                                    dateEnd9 = subPattern.parse(endDate9);

                                    long diff = dateEnd9.getTime() - dateStart9.getTime();
                                    diffHours9 = diff / 3600000;
                                    diffMinutes9 = (diff % 3600000) / 60000;
                                    if(diffHours9>0){
                                        toMin9 = diffHours9*60;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                total9 = toMin9 + diffMinutes9;
                                toHours9 = total9 * 0.0166667f;
                                countSep = countSep + toHours9;
                            }else if(sleep.getStartTime().contains(curOct)){
                                total10 = 0;
                                toHours10 = 0;
                                String startDate10 = sleep.getStartTime().toString();
                                String endDate10 = sleep.getEndTime().toString();

                                try {
                                    dateStart10 = subPattern.parse(startDate10);
                                    dateEnd10 = subPattern.parse(endDate10);

                                    long diff = dateEnd10.getTime() - dateStart10.getTime();
                                    diffHours10 = diff / 3600000;
                                    diffMinutes10 = (diff % 3600000) / 60000;
                                    if(diffHours10>0){
                                        toMin10 = diffHours1*60;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                total10 = toMin10 + diffMinutes10;
                                toHours10 = total10 * 0.0166667f;
                                countOct = countOct + toHours10;
                            }else if(sleep.getStartTime().contains(curNov)){
                                total1 = 0;
                                toHours1 = 0;
                                String startDate11 = sleep.getStartTime().toString();
                                String endDate11 = sleep.getEndTime().toString();

                                try {
                                    dateStart11 = subPattern.parse(startDate11);
                                    dateEnd11 = subPattern.parse(endDate11);

                                    long diff = dateEnd11.getTime() - dateStart11.getTime();
                                    diffHours11 = diff / 3600000;
                                    diffMinutes11 = (diff % 3600000) / 60000;
                                    if(diffHours11>0){
                                        toMin11 = diffHours11*60;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                total11 = toMin11 + diffMinutes11;
                                toHours11 = total11 * 0.0166667f;
                                countNov = countNov + toHours11;
                            }else if(sleep.getStartTime().contains(curDec)){
                                total12 = 0;
                                toHours12 = 0;
                                String startDate12 = sleep.getStartTime().toString();
                                String endDate12 = sleep.getEndTime().toString();

                                try {
                                    dateStart12 = subPattern.parse(startDate12);
                                    dateEnd12 = subPattern.parse(endDate12);

                                    long diff = dateEnd12.getTime() - dateStart12.getTime();
                                    diffHours12 = diff / 3600000;
                                    diffMinutes12 = (diff % 3600000) / 60000;
                                    if(diffHours12>0){
                                        toMin12 = diffHours12*60;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                total2 = toMin12 + diffMinutes12;
                                toHours12 = total2 * 0.0166667f;
                                countDec = countDec + toHours12;
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
            mFormat = new DecimalFormat("###,###,##0" + "Hrs");
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
