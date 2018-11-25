package com.health.baby_daily.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.health.baby_daily.R;
import com.health.baby_daily.model.Diary;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {
    private List<Diary> mDiaryList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class DiaryViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView titleText, dateText,contentText;


        public DiaryViewHolder(View itemView, final OnItemClickListener listener){
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            titleText = itemView.findViewById(R.id.titleText);
            dateText = itemView.findViewById(R.id.dateText);
            contentText = itemView.findViewById(R.id.contentText);

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

    public DiaryAdapter(List<Diary> diaryList){
        mDiaryList = diaryList;
    }

    @Override
    public DiaryViewHolder onCreateViewHolder( ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_diary, parent, false);
        DiaryViewHolder dvh = new DiaryViewHolder(view, mListener);
        return dvh;
    }

    @Override
    public void onBindViewHolder( DiaryViewHolder holder, int position) {
        Diary diary = mDiaryList.get(position);

        Picasso.get().load(diary.getImage()).into(holder.imageView);
        holder.titleText.setText(diary.getTitle());
        holder.contentText.setText(diary.getContent());
        holder.dateText.setText(diary.getDateTime());

    }

    @Override
    public int getItemCount() {
        return mDiaryList.size();
    }


}
