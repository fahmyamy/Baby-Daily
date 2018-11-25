package com.health.baby_daily.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.health.baby_daily.R;
import com.health.baby_daily.model.Bottle;

import java.util.List;

public class BottleStatisticAdapter extends RecyclerView.Adapter<BottleStatisticAdapter.BottleViewHolder>{
    private List<Bottle> bottleList;

    class BottleViewHolder extends RecyclerView.ViewHolder{
        TextView numGen, dateTime, type, amount, units;

        BottleViewHolder(@NonNull View itemView){
            super(itemView);

            numGen = itemView.findViewById(R.id.textNumber);
            dateTime = itemView.findViewById(R.id.textDate);
            type = itemView.findViewById(R.id.textType);
            amount = itemView.findViewById(R.id.textAmount);
            units = itemView.findViewById(R.id.textUnits);
        }
    }

    public BottleStatisticAdapter(List<Bottle> bottleList){this.bottleList = bottleList;}

    @NonNull
    @Override
    public BottleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_bottle_row, parent, false);
        BottleViewHolder bvh = new BottleViewHolder(view);
        return bvh;
    }

    @Override
    public void onBindViewHolder(@NonNull BottleViewHolder holder, int position) {
        Bottle bottle = bottleList.get(position);
        int increment = position+1;
        String dT = bottle.getDateTime();
        String t = bottle.getType();
        String a = bottle.getAmount();
        String u = bottle.getUnits();

        holder.numGen.setText(String.valueOf(increment));
        holder.dateTime.setText(dT);
        holder.type.setText(t);
        holder.amount.setText(a);
        holder.units.setText(u);
    }

    @Override
    public int getItemCount() {
        return bottleList.size();
    }


}
