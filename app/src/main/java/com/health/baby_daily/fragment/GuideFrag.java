package com.health.baby_daily.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.health.baby_daily.R;
import com.health.baby_daily.guide.FirstAid;
import com.health.baby_daily.guide.MedicalPost;
import com.health.baby_daily.guide.VaccinePost;
import com.health.baby_daily.guide.VidStream;

public class GuideFrag extends Fragment {
    private CardView firstAidCard, guidesCard, vidStreamCard, vaccineCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.guide_fragment, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firstAidCard = view.findViewById(R.id.firstAidCard);
        guidesCard = view.findViewById(R.id.guidesCard);
        vidStreamCard = view.findViewById(R.id.vidStreamCard);
        vaccineCard = view.findViewById(R.id.vaccineCard);

        firstAidCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), FirstAid.class));
            }
        });

        guidesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MedicalPost.class));
            }
        });

        vidStreamCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), VidStream.class));
            }
        });

        vaccineCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), VaccinePost.class));
            }
        });
    }
}
