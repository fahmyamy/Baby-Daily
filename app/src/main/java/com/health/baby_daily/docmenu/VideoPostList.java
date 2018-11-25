package com.health.baby_daily.docmenu;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.health.baby_daily.R;
import com.health.baby_daily.docmenu.statistic.VideoStaFragChart;
import com.health.baby_daily.docmenu.statistic.VideoStaFragList;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoPostList extends Fragment {

    private Button btnList, btnChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_post_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnList = view.findViewById(R.id.btnList);
        btnChart = view.findViewById(R.id.btnChart);

        FragmentTransaction frag = getFragmentManager().beginTransaction();
        frag.replace(R.id.fragmentLayout, new VideoStaFragList());
        frag.commit();

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment list = new VideoStaFragList();

                if (list != null) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragmentLayout, list);
                    ft.commit();
                }

            }
        });

        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment chart = new VideoStaFragChart();

                if (chart != null) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragmentLayout, chart);
                    ft.commit();
                }
            }
        });
    }
}
