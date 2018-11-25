package com.health.baby_daily.docmenu.statistic;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.model.Post;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostStaFragChart extends Fragment {

    private FirebaseAuth firebaseAuth;
    private String uid;

    private LineChart chart;
    private DatabaseReference table_view;
    private DatabaseReference table_post;
    private DatabaseReference table_sta;
    private Query query_view, query_post, query_sta;
    private LineDataSet setJan, setFeb, setMar, setApr, setMay, setJun, setJul, setAug, setSep, setOct, setNov, setDec;
    float countJan = 0, countFeb = 0, countMar = 0, countApr = 0, countMay = 0, countJun = 0, countJul = 0;
    float countAug = 0, countSep = 0, countOct = 0, countNov = 0, countDec = 0;
    private String curJan, curFeb, curMar, curApr, curMay, curJun, curJul, curAug, curSep, curOct, curNov, curDec;
    private int count;

    private TextView totalViews, totalPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_sta_frag_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        uid = user.getUid();

        totalViews = view.findViewById(R.id.totalView);
        totalPost = view.findViewById(R.id.totalPost);

        table_post = FirebaseDatabase.getInstance().getReference().child("Post");
        query_post = table_post.orderByChild("uid").equalTo(uid);
        query_post.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String ttlPost = String.valueOf(dataSnapshot.getChildrenCount());
                    totalPost.setText(ttlPost);

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Post post = snapshot.getValue(Post.class);
                        String pid = post.getId();

                        table_view = FirebaseDatabase.getInstance().getReference().child("PostCounter");
                        query_view = table_view.orderByChild("postId").equalTo(pid);
                        query_view.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    int vidViews = (int) dataSnapshot.getChildrenCount();
                                    count = count + vidViews;
                                }

                                totalViews.setText(String.valueOf(count));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //monthly
        chart = view.findViewById(R.id.chart);
        chart.getDescription().setText("Post");
        chart.setScaleEnabled(true);
        chart.getXAxis().setDrawLabels(true);
        chart.getAxisRight().setDrawLabels(false);
        chart.getLegend().setEnabled(false);
        chart.getXAxis().setAxisMinimum(0);
        chart.getAxisLeft().setAxisMinimum(0);
        chart.getXAxis().setLabelCount(12, false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.animateXY(3000,3000);

        YAxis left = chart.getAxisLeft();
        left.setValueFormatter(new MyYAxisValueFormatter());
        left.setGranularity(1);

        Legend legend = chart.getLegend();
        legend.setWordWrapEnabled(true);

        final ArrayList<Entry> yVals_year = new ArrayList<>();

        Calendar c = GregorianCalendar.getInstance();
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

        table_sta = FirebaseDatabase.getInstance().getReference().child("Post");
        query_sta = table_sta.orderByChild("created");
        query_sta.keepSynced(true);
        query_sta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                countJan = 0; countFeb = 0; countMar = 0; countApr = 0; countMay = 0; countJun = 0;
                countJul = 0; countAug = 0; countSep = 0; countOct = 0; countNov = 0; countDec = 0;

                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Post post = snapshot.getValue(Post.class);
                        if (post.getCreated().contains(curJan)){
                            countJan++;
                        }else if (post.getCreated().contains(curFeb)){
                            countFeb++;
                        }else if (post.getCreated().contains(curMar)){
                            countMar++;
                        }else if (post.getCreated().contains(curApr)){
                            countApr++;
                        }else if (post.getCreated().contains(curMay)){
                            countMay++;
                        }else if (post.getCreated().contains(curJun)){
                            countJun++;
                        }else if (post.getCreated().contains(curJul)){
                            countJul++;
                        }else if (post.getCreated().contains(curAug)){
                            countAug++;
                        }else if (post.getCreated().contains(curSep)){
                            countSep++;
                        }else if (post.getCreated().contains(curOct)){
                            countOct++;
                        }else if (post.getCreated().contains(curNov)){
                            countNov++;
                        }else if (post.getCreated().contains(curDec)){
                            countDec++;
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
                    chart.setData(data);

                    final String[] months = new String[]{year, "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
                            "Sep", "Oct", "Nov", "Dec"};

                    XAxis xAxis = chart.getXAxis();
                    xAxis.setGranularity(1f);
                    xAxis.setValueFormatter(new MyXAxisValueFormatter(months));

                    setJan.setColor(Color.GRAY);
                    setFeb.setColor(Color.GRAY);
                    setMar.setColor(Color.GRAY);
                    setApr.setColor(Color.GRAY);
                    setMay.setColor(Color.GRAY);
                    setJun.setColor(Color.GRAY);
                    setJul.setColor(Color.GRAY);
                    setAug.setColor(Color.GRAY);
                    setSep.setColor(Color.GRAY);
                    setOct.setColor(Color.GRAY);
                    setNov.setColor(Color.GRAY);
                    setDec.setColor(Color.GRAY);

                    setJan.setCircleColors(Color.DKGRAY);
                    setFeb.setCircleColors(Color.DKGRAY);
                    setMar.setCircleColors(Color.DKGRAY);
                    setApr.setCircleColors(Color.DKGRAY);
                    setMay.setCircleColors(Color.DKGRAY);
                    setJun.setCircleColors(Color.DKGRAY);
                    setJul.setCircleColors(Color.DKGRAY);
                    setAug.setCircleColors(Color.DKGRAY);
                    setSep.setCircleColors(Color.DKGRAY);
                    setOct.setCircleColors(Color.DKGRAY);
                    setNov.setCircleColors(Color.DKGRAY);
                    setDec.setCircleColors(Color.DKGRAY);

                    setJan.setCircleColorHole(Color.DKGRAY);
                    setFeb.setCircleColorHole(Color.DKGRAY);
                    setMar.setCircleColorHole(Color.DKGRAY);
                    setApr.setCircleColorHole(Color.DKGRAY);
                    setMay.setCircleColorHole(Color.DKGRAY);
                    setJun.setCircleColorHole(Color.DKGRAY);
                    setJul.setCircleColorHole(Color.DKGRAY);
                    setAug.setCircleColorHole(Color.DKGRAY);
                    setSep.setCircleColorHole(Color.DKGRAY);
                    setOct.setCircleColorHole(Color.DKGRAY);
                    setNov.setCircleColorHole(Color.DKGRAY);
                    setDec.setCircleColorHole(Color.DKGRAY);

                    data.notifyDataChanged();
                    chart.notifyDataSetChanged();
                    chart.invalidate();
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
