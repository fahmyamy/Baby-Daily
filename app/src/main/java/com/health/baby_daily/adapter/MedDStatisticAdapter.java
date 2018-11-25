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
import com.health.baby_daily.model.MedicineD;
import com.health.baby_daily.other.ImageFullscreen;

import java.util.List;

public class MedDStatisticAdapter extends RecyclerView.Adapter<MedDStatisticAdapter.MedDViewHolder> {
    private List<MedicineD> medicineDList;
    private Context context;

    class MedDViewHolder extends RecyclerView.ViewHolder{
        TextView numGen, dateTime, medName, amount, units;
        Button btnImage;

        MedDViewHolder(@NonNull View itemView){
            super(itemView);

            numGen = itemView.findViewById(R.id.textNumber);
            dateTime = itemView.findViewById(R.id.textDate);
            medName = itemView.findViewById(R.id.medName);
            amount = itemView.findViewById(R.id.textAmount);
            units = itemView.findViewById(R.id.textUnits);
            btnImage = itemView.findViewById(R.id.btnImage);
        }
    }

    public MedDStatisticAdapter(List<MedicineD> medicineDList){this.medicineDList = medicineDList;}

    @NonNull
    @Override
    public MedDStatisticAdapter.MedDViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_med_detail_row, parent, false);
        MedDStatisticAdapter.MedDViewHolder mvh = new MedDStatisticAdapter.MedDViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MedDStatisticAdapter.MedDViewHolder holder, int i) {
        final MedicineD medicineD = medicineDList.get(i);
        int increment = i+1;
        String dt = medicineD.getDateTime();
        String mN = medicineD.getMedName();
        String dA = medicineD.getDoseAmount();
        String u = medicineD.getUnits();

        holder.numGen.setText(String.valueOf(increment));
        holder.dateTime.setText(dt);
        holder.medName.setText(mN);
        holder.amount.setText(dA);
        holder.units.setText(u);

        holder.btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageFullscreen.class);
                intent.putExtra("image_url", medicineD.getImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicineDList.size();
    }
}
