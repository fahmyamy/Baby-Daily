package com.health.baby_daily.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.health.baby_daily.R;
import com.health.baby_daily.model.Sleep;

import java.util.List;

public class SleepStatisticAdapter extends RecyclerView.Adapter<SleepStatisticAdapter.SleepViewHolder> {
    private List<Sleep> sleepList;


    class SleepViewHolder extends RecyclerView.ViewHolder{
        TextView numGen, startDate, endDate;

        SleepViewHolder(@NonNull View view){
            super(view);

            numGen = view.findViewById(R.id.textNumber);
            startDate = view.findViewById(R.id.textStart);
            endDate = view.findViewById(R.id.textEnd);
        }
    }

    public SleepStatisticAdapter(List<Sleep> sleepList){this.sleepList = sleepList;}

    @NonNull
    @Override
    public SleepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_sleep_row, parent, false);
        SleepStatisticAdapter.SleepViewHolder svh = new SleepStatisticAdapter.SleepViewHolder(view);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull SleepViewHolder holder, int i) {
        Sleep sleep = sleepList.get(i);
        int increment = i+1;
        String sD = sleep.getStartTime();
        String eD = sleep.getEndTime();

        holder.numGen.setText(String.valueOf(increment));
        holder.startDate.setText(sD);
        holder.endDate.setText(eD);
    }

    @Override
    public int getItemCount() {
        return sleepList.size();
    }
}
