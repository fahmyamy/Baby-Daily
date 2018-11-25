package com.health.baby_daily.adapter;

import android.content.Context;
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
import com.health.baby_daily.model.Follow;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {
    private Context context;
    private OnItemClickListener mListener;
    private List<Follow> followList;
    private DatabaseReference table_user;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(FriendAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder{
        public ImageView searchImage;
        public TextView searchUsername, searchEmail;

        public FriendViewHolder(View view, final OnItemClickListener listener){
            super(view);

            searchImage = itemView.findViewById(R.id.searchImage);
            searchUsername = itemView.findViewById(R.id.searchUsername);
            searchEmail = itemView.findViewById(R.id.searchEmail);

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

    public FriendAdapter(List<Follow> followList){
        this.followList = followList;
    }

    @NonNull
    @Override
    public FriendAdapter.FriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.entry_search, viewGroup, false);
        FriendAdapter.FriendViewHolder fvh = new FriendAdapter.FriendViewHolder(view, mListener);
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final FriendAdapter.FriendViewHolder holder, int position) {
        Follow follow = followList.get(position);

        table_user = FirebaseDatabase.getInstance().getReference("User").child(follow.getFollow_id());
        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
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
        return followList.size();
    }
}
