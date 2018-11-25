package com.health.baby_daily.adapter;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.model.Notify;
import com.health.baby_daily.model.ParentnDoctorList;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestDoctorAdapter extends RecyclerView.Adapter<RequestDoctorAdapter.RequestDViewHolder> {
    private List<ParentnDoctorList> requestList;
    private OnItemClickListener mListener;
    private DatabaseReference table_request;
    private String user_id;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class RequestDViewHolder extends RecyclerView.ViewHolder {
        public ImageView requestImage;
        public TextView requestUsername;
        public Button btn_confirm, btn_delete;
        public String role;

        public RequestDViewHolder(View itemView, final OnItemClickListener listener){
            super(itemView);

            requestImage = itemView.findViewById(R.id.requestImage);
            requestUsername = itemView.findViewById(R.id.requestUsername);
            btn_confirm = itemView.findViewById(R.id.btn_confirm);
            btn_delete = itemView.findViewById(R.id.btn_delete);

            SharedPreferences prefRole = itemView.getContext().getSharedPreferences("currRole", 0);
            role = prefRole.getString("role", null);

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

    public RequestDoctorAdapter(List<ParentnDoctorList> rList){
        requestList = rList;
    }

    @Override
    public RequestDViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_request, parent, false);
        RequestDViewHolder dvh = new RequestDViewHolder(view, mListener);
        return dvh;
    }

    @Override
    public void onBindViewHolder(final RequestDViewHolder holder, final int position) {
        final ParentnDoctorList request = requestList.get(position);

        String role = holder.role;

        if (role.equals("Mother") || role.equals("Father")){
            user_id = request.getDoctor_id().toString();
        }else if (role.equals("Doctor")){
            user_id = request.getParent_id().toString();
        }

        table_request = FirebaseDatabase.getInstance().getReference("User").child(user_id);
        table_request.keepSynced(true);
        table_request.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String username = dataSnapshot.child("username").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();

                    holder.requestUsername.setText(username);
                    if (!image.equals("none")){
                        Picasso.get().load(image).into(holder.requestImage);
                    }else {
                        Picasso.get().load(R.drawable.user_image).into(holder.requestImage);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = request.getId().toString();
                String parent_id = request.getParent_id().toString();
                String doctor_id = request.getDoctor_id().toString();
                String desc = request.getDesc().toString();
                String status = "1";

                DatabaseReference table_caregiver = FirebaseDatabase.getInstance().getReference("ParentnDoctorList").child(id);
                ParentnDoctorList caregiver = new ParentnDoctorList(id, parent_id, doctor_id, status, desc);
                table_caregiver.setValue(caregiver);

                DatabaseReference table_notify = FirebaseDatabase.getInstance().getReference("Notifications");
                String role = holder.role;
                if (role.equals("Mother") || role.equals("Father")){
                    user_id = request.getParent_id().toString();
                    Notify notify = new Notify(user_id, "request_acceptP", null);
                    table_notify.child(doctor_id).push().setValue(notify);
                }else if (role.equals("Doctor")){
                    user_id = request.getDoctor_id().toString();
                    Notify notify = new Notify(user_id, "request_acceptD", null);
                    table_notify.child(parent_id).push().setValue(notify);
                }

                notifyDataSetChanged();
            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String id = request.getId().toString();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Map<String, Object> map = new HashMap<>();
                map.put("/Follow/" + id + "/", null);
                map.put("/ParentnDoctorList/" + id + "/", null);
                //other locations
                databaseReference.updateChildren(map);

                requestList.remove(position);
                notifyItemRemoved(position);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }


}