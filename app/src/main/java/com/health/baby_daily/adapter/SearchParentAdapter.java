package com.health.baby_daily.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.health.baby_daily.R;
import com.health.baby_daily.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchParentAdapter extends RecyclerView.Adapter<SearchParentAdapter.SParentViewHolder> {
    private List<User> userList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class SParentViewHolder extends RecyclerView.ViewHolder {
        public ImageView searchImage;
        public TextView searchUsername, searchEmail;


        public SParentViewHolder(View itemView, final OnItemClickListener listener){
            super(itemView);

            searchImage = itemView.findViewById(R.id.searchImage);
            searchUsername = itemView.findViewById(R.id.searchUsername);
            searchEmail = itemView.findViewById(R.id.searchEmail);

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

    public SearchParentAdapter(List<User> users){
        userList = users;
    }

    @Override
    public SParentViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_search, parent, false);
        SParentViewHolder sPvh = new SParentViewHolder(view, mListener);
        return sPvh;
    }

    @Override
    public void onBindViewHolder( SParentViewHolder holder, int position) {
        User user = userList.get(position);

        if (!user.getImage().equals("none")){
            Picasso.get().load(user.getImage()).into(holder.searchImage);
        }else {
            Picasso.get().load(R.drawable.user_image).into(holder.searchImage);
        }

        holder.searchUsername.setText(user.getUsername());
        holder.searchEmail.setText(user.getEmail());

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


}
