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
import com.health.baby_daily.model.Diaper;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DiaperStaFragChart extends Fragment {
    private View view;
    private HorizontalBarChart chartA;
    private BarChart chartB;
    private LineChart chartC;
    private DatabaseReference table_diaper, table_diaperB, table_diaperC;
    private Query query, queryB, queryC;

    private String bId;

    private BarDataSet set1, set2, set3, setA, setB, setC, setD, setE, setF, setG;
    private LineDataSet setJan, setFeb, setMar, setApr, setMay, setJun, setJul, setAug, setSep, setOct, setNov, setDec;
    int countP = 0, countPo = 0, countPnP;
    int count1 = 0, count2 = 0, count3 = 0, count4 = 0, count5 = 0, count6 = 0, count7 = 0;
    int countJan = 0, countFeb = 0, countMar = 0, countApr = 0, countMay = 0, countJun = 0, countJul = 0;
    int countAug = 0, countSep = 0, countOct = 0, countNov = 0, countDec = 0;
    private String mon, tue, wed, thu, fri, stu, sun;
    private String curJan, curFeb, curMar, curApr, curMay, curJun, curJul, curAug, curSep, curOct, curNov, curDec;
    private Date dateA, dateB, dateC, dateD, dateE, dateF, dateG, dateH;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.diaper_chart_fragment, container, false);
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
        chartA.setScaleXEnabled(true);
        chartA.setTouchEnabled(false);
        chartA.setFitBars(true);

        chartA.getAxisLeft().setDrawLabels(false);
        chartA.getXAxis().setDrawLabels(true);
        chartA.setDragOffsetY(0);
        chartA.setDragOffsetX(0);
        chartA.setDrawValueAboveBar(true);
        chartA.getXAxis().setLabelCount(3, false);
        chartA.getAxisRight().setGranularity(1);

        chartB = view.findViewById(R.id.chartB);
        chartB.getDescription().setText("Bottle Event");
        chartB.setTouchEnabled(false);
        chartB.getXAxis().setDrawLabels(true);
        chartB.getAxisRight().setDrawLabels(false);
        chartB.setFitBars(true);
        chartB.getAxisLeft().setGranularity(1);
        chartB.getXAxis().setLabelCount(7, false);
        chartB.getAxisRight().setDrawGridLines(false);

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
        final ArrayList<BarEntry> yVals_Pee = new ArrayList<>();
        final ArrayList<BarEntry> yVals_Poop = new ArrayList<>();
        final ArrayList<BarEntry> yVals_PnP = new ArrayList<>();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        final String currentDate = dateFormat.format(date);

        table_diaper = FirebaseDatabase.getInstance().getReference().child("Diaper");
        query = table_diaper.orderByChild("dateTime");
        query.keepSynced(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                countP = 0;
                countPo = 0;
                countPnP = 0;
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Diaper diaper = snapshot.getValue(Diaper.class);
                        if(diaper.getBabyId().equals(bId)) {
                            if (diaper.getDateTime().startsWith(currentDate) && diaper.getType().equals("Pee")) {
                                countP++;
                            }

                            if (diaper.getDateTime().startsWith(currentDate) && diaper.getType().equals("Poop")) {
                                countPo++;
                            }

                            if (diaper.getDateTime().startsWith(currentDate) && diaper.getType().equals("Peep and Poop")) {
                                countPnP++;
                            }
                        }
                    }

                    yVals_Pee.add(new BarEntry(0f, countP));
                    set1 = new BarDataSet(yVals_Pee, "Pee");

                    yVals_Poop.add(new BarEntry(1f, countPo));
                    set2 = new BarDataSet(yVals_Poop, "Poop");

                    yVals_PnP.add(new BarEntry(2f, countPnP));
                    set3 = new BarDataSet(yVals_PnP, "Pee & Poop");

                    set1.setColor(Color.BLUE);
                    set2.setColor(Color.YELLOW);
                    set3.setColor(Color.GREEN);
                    BarData data = new BarData(set1, set2);
                    chartA.setData(data);
                    final String[] type = new String[]{"Pee", "Poop", "Pee & Poop"};

                    XAxis xAxis = chartA.getXAxis();
                    xAxis.setGranularity(1f);
                    xAxis.setValueFormatter(new MyXAxisValueFormatter(type));
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

        table_diaperB = FirebaseDatabase.getInstance().getReference().child("Diaper");
        queryB = table_diaperB.orderByChild("dateTime");
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
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Diaper diaper = snapshot.getValue(Diaper.class);
                        if(diaper.getBabyId().equals(bId)) {
                            String created = diaper.getCreated();

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
                                count1++;
                            }else if(dateA.equals(dateC)){
                                count2++;
                            }else if(dateA.equals(dateD)){
                                count3++;
                            }else if(dateA.equals(dateE)){
                                count4++;
                            }else if(dateA.equals(dateF)){
                                count5++;
                            }else if(dateA.equals(dateG)){
                                count6++;
                            }else if(dateA.equals(dateH)){
                                count7++;
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

        table_diaperC = FirebaseDatabase.getInstance().getReference().child("Diaper");
        queryC = table_diaperC.orderByChild("dateTime");
        queryC.keepSynced(true);
        queryC.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                countJan = 0; countFeb = 0; countMar = 0; countApr = 0; countMay = 0; countJun = 0;
                countJul = 0; countAug = 0; countSep = 0; countOct = 0; countNov = 0; countDec = 0;

                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Diaper diaper = snapshot.getValue(Diaper.class);
                        if(diaper.getBabyId().equals(bId)) {
                            if(diaper.getDateTime().contains(curJan)){
                                countJan++;
                            }else if(diaper.getDateTime().contains(curFeb)){
                                countFeb++;
                            }else if(diaper.getDateTime().contains(curMar)){
                                countMar++;
                            }else if(diaper.getDateTime().contains(curApr)){
                                countApr++;
                            }else if(diaper.getDateTime().contains(curMay)){
                                countMay++;
                            }else if(diaper.getDateTime().contains(curJun)){
                                countJun++;
                            }else if(diaper.getDateTime().contains(curJul)){
                                countJul++;
                            }else if(diaper.getDateTime().contains(curAug)){
                                countAug++;
                            }else if(diaper.getDateTime().contains(curSep)){
                                countSep++;
                            }else if(diaper.getDateTime().contains(curOct)){
                                countOct++;
                            }else if(diaper.getDateTime().contains(curNov)){
                                countNov++;
                            }else if(diaper.getDateTime().contains(curDec)){
                                countDec++;
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
            mFormat = new DecimalFormat("###,###,##0");
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
