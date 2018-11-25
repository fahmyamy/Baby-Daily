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
import com.health.baby_daily.docmenu.statistic.PostStaFragChart;
import com.health.baby_daily.docmenu.statistic.PostStaFragList;

public class MedicalPostList extends Fragment {

    private Button btnList, btnChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medical_post_list,container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnList = view.findViewById(R.id.btnList);
        btnChart = view.findViewById(R.id.btnChart);

        FragmentTransaction frag = getFragmentManager().beginTransaction();
        frag.replace(R.id.fragmentLayout, new PostStaFragList());
        frag.commit();

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment list = new PostStaFragList();

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
                Fragment chart = new PostStaFragChart();

                if (chart != null) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragmentLayout, chart);
                    ft.commit();
                }
            }
        });
    }
}
