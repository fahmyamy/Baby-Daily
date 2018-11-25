package com.health.baby_daily.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.health.baby_daily.R;
import com.health.baby_daily.model.Question;

import java.util.List;

public class AppointmentQuesAdapter extends RecyclerView.Adapter<AppointmentQuesAdapter.QuestionViewHolder> {
    private List<Question> questionList;
    private OnItemClickListener mListener;
    private DatabaseReference table_question;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTime, questionText, answerText;

        public QuestionViewHolder(View itemView, final OnItemClickListener listener){
            super(itemView);

            dateTime = itemView.findViewById(R.id.dateTime);
            questionText = itemView.findViewById(R.id.questionText);
            answerText = itemView.findViewById(R.id.answerText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public AppointmentQuesAdapter(List<Question> questionList){
        this.questionList = questionList;
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_question, parent, false);
        QuestionViewHolder avh = new QuestionViewHolder(view, mListener);
        return avh;
    }

    @Override
    public void onBindViewHolder(final QuestionViewHolder holder, final int position) {
        final Question question = questionList.get(position);
        String answer = "Add Your Question Here...";
        holder.dateTime.setText(question.getDateTime());
        holder.questionText.setText(question.getQuestion());
        if (question.getAnswer().equals("none")){
            holder.answerText.setText(answer);
        }else {
            holder.answerText.setText(question.getAnswer());
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }
}
