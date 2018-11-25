package com.health.baby_daily.statistic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.adapter.AppointmentQuesAdapter;
import com.health.baby_daily.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppointmentStaQuestions extends Fragment {
    private View view;
    private RecyclerView entry_questionList;
    private AppointmentQuesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView no_question_text;
    private DatabaseReference table_question;
    private Query query;
    private FloatingActionButton add_btn;

    private String bId;

    private List<Question> questionList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.appointment_sta_ques, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefBaby = getContext().getSharedPreferences("selectedBaby", 0);
        bId = prefBaby.getString("babyId", null);

        questionList = new ArrayList<>();

        table_question = FirebaseDatabase.getInstance().getReference().child("Question");
        query = table_question.orderByChild("dateTime");
        query.keepSynced(true);

        entry_questionList = view.findViewById(R.id.entry_questionList);
        entry_questionList.setHasFixedSize(true);
        no_question_text = view.findViewById(R.id.no_question_text);
        add_btn = view.findViewById(R.id.add_btn);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AppointmentStaQuesAdd.class));
            }
        });

        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new AppointmentQuesAdapter(questionList);

        entry_questionList.setLayoutManager(mLayoutManager);
        entry_questionList.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AppointmentQuesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String id = questionList.get(position).getId();
                Intent intent = new Intent(getActivity(), AppointmentStaQuesEdit.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questionList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Question question = snapshot.getValue(Question.class);
                        if (question.getBaby_Id().equals(bId)) {
                            questionList.add(question);
                        }
                    }
                    no_question_text.setVisibility(View.GONE);
                    Collections.reverse(questionList);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
