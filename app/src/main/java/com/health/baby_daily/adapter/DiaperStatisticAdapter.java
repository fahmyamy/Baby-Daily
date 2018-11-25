package com.health.baby_daily.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.health.baby_daily.R;
import com.health.baby_daily.model.Diaper;
import com.health.baby_daily.other.ImageFullscreen;

import java.util.List;

public class DiaperStatisticAdapter extends RecyclerView.Adapter<DiaperStatisticAdapter.DiaperViewHolder> {
    private List<Diaper> diaperList;
    private Context context;

    class DiaperViewHolder extends RecyclerView.ViewHolder{
        TextView textNumber, textDate, textType;
        Button btnImage;
        DiaperViewHolder(@NonNull View view){
            super(view);

            textNumber = view.findViewById(R.id.textNumber);
            textDate = view.findViewById(R.id.textDate);
            textType = view.findViewById(R.id.textType);
            btnImage = view.findViewById(R.id.btnImage);
        }
    }

    public DiaperStatisticAdapter(List<Diaper> diaperList){this.diaperList = diaperList;}

    @NonNull
    @Override
    public DiaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_diaper_row, parent, false);
        DiaperStatisticAdapter.DiaperViewHolder dvh = new DiaperStatisticAdapter.DiaperViewHolder(view);
        return dvh;
    }

    @Override
    public void onBindViewHolder(@NonNull DiaperViewHolder holder, int i) {
        final Diaper diaper = diaperList.get(i);
        int increment = i+1;
        String dt = diaper.getDateTime();
        String t = diaper.getType();

        holder.textNumber.setText(String.valueOf(increment));
        holder.textDate.setText(dt);
        holder.textType.setText(t);

        holder.btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageFullscreen.class);
                intent.putExtra("image_url", diaper.getImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return diaperList.size();
    }
}
