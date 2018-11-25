package com.health.baby_daily.adapter;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.model.ParentnDoctorList;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {
    private List<ParentnDoctorList> requestList;
    private OnItemClickListener mListener;
    private DatabaseReference table_doctor;
    private String user_id;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder {
        public ImageView searchImage;
        public TextView searchUsername, searchEmail;
        public String role;

        public DoctorViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            searchImage = itemView.findViewById(R.id.searchImage);
            searchUsername = itemView.findViewById(R.id.searchUsername);
            searchEmail = itemView.findViewById(R.id.searchEmail);

            SharedPreferences prefRole = itemView.getContext().getSharedPreferences("currRole", 0);
            role = prefRole.getString("role", null);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public DoctorAdapter(List<ParentnDoctorList> rList) {
        requestList = rList;
    }

    @Override
    public DoctorViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_search, parent, false);
        DoctorViewHolder dvh = new DoctorViewHolder(view, mListener);
        return dvh;
    }

    @Override
    public void onBindViewHolder(final DoctorViewHolder holder, int position) {
        ParentnDoctorList request = requestList.get(position);

        String role = holder.role;

        if (role.equals("Mother") || role.equals("Father")){
            user_id = request.getDoctor_id().toString();
        }else if (role.equals("Doctor")){
            user_id = request.getParent_id().toString();
        }

        table_doctor = FirebaseDatabase.getInstance().getReference("User").child(user_id);
        table_doctor.keepSynced(true);
        table_doctor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("username").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();

                    holder.searchUsername.setText(username);
                    holder.searchEmail.setText(email);
                    if (!image.equals("none")) {
                        Picasso.get().load(image).into(holder.searchImage);
                    } else {
                        Picasso.get().load(R.drawable.user_image).into(holder.searchImage);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }
}