package com.health.baby_daily.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.model.Medicine;

import java.util.List;

public class MedStatisticAdapter extends RecyclerView.Adapter<MedStatisticAdapter.MedViewHolder>{
    private List<Medicine> medicineList;
    private DatabaseReference table_med;
    private Query query;

    class MedViewHolder extends RecyclerView.ViewHolder{
        TextView numGen, dateTime, medName, amount, unit;

        MedViewHolder(@NonNull View itemView){
            super(itemView);

            numGen = itemView.findViewById(R.id.textNumber);
            dateTime = itemView.findViewById(R.id.textDate);
            medName = itemView.findViewById(R.id.textMedName);
            amount = itemView.findViewById(R.id.textAmount);
            unit = itemView.findViewById(R.id.textUnits);
        }
    }

    public MedStatisticAdapter(List<Medicine> medicineList){this.medicineList = medicineList;}

    @NonNull
    @Override
    public MedStatisticAdapter.MedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_med_row, parent, false);
        MedStatisticAdapter.MedViewHolder mvh = new MedStatisticAdapter.MedViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MedStatisticAdapter.MedViewHolder holder, int i) {
        Medicine medicine = medicineList.get(i);
        int increment = i+1;
        String dt = medicine.getDateTime();
        String id = medicine.getMedId();

        holder.numGen.setText(String.valueOf(increment));
        holder.dateTime.setText(dt);

        table_med = FirebaseDatabase.getInstance().getReference("MedicineD").child(id);
        table_med.keepSynced(true);

        table_med.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                        String mN = dataSnapshot.child("medName").getValue().toString();
                        String a = dataSnapshot.child("doseAmount").getValue().toString();
                        String u = dataSnapshot.child("units").getValue().toString();
                        holder.medName.setText(mN);
                        holder.amount.setText(a);
                        holder.unit.setText(u);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }
}
