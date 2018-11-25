package com.health.baby_daily.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.health.baby_daily.R;
import com.health.baby_daily.model.Measurement;

import java.util.List;

public class MeasureStatisticAdapter extends RecyclerView.Adapter<MeasureStatisticAdapter.MeasureViewHolder> {
    private List<Measurement> measurementList;

    class MeasureViewHolder extends RecyclerView.ViewHolder{
        TextView textNumber, textDate, textHeight, textWeight, textHead;

        MeasureViewHolder(@NonNull View view){
            super(view);

            textNumber = view.findViewById(R.id.textNumber);
            textDate = view.findViewById(R.id.textDate);
            textHeight = view.findViewById(R.id.textHeight);
            textWeight = view.findViewById(R.id.textWeight);
            textHead = view.findViewById(R.id.textHead);
        }
    }

    public MeasureStatisticAdapter(List<Measurement> measurementList){this.measurementList = measurementList;}

    @NonNull
    @Override
    public MeasureStatisticAdapter.MeasureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_measure_row, parent, false);
        MeasureStatisticAdapter.MeasureViewHolder mvh = new MeasureStatisticAdapter.MeasureViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MeasureStatisticAdapter.MeasureViewHolder holder, int i) {
        Measurement measurement = measurementList.get(i);
        int increment = i+1;
        String dt = measurement.getDateTime();
        String h = measurement.getHeight();
        String w = measurement.getWeight();
        String hC = measurement.getHeadC();

        holder.textNumber.setText(String.valueOf(increment));
        holder.textDate.setText(dt);
        holder.textHeight.setText(h);
        holder.textWeight.setText(w);
        holder.textHead.setText(hC);
    }

    @Override
    public int getItemCount() {
        return measurementList.size();
    }
}
