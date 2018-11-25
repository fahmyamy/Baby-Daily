package com.health.baby_daily.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.health.baby_daily.R;
import com.health.baby_daily.model.Feed;

import java.util.List;

public class FeedStatisticAdapter extends RecyclerView.Adapter<FeedStatisticAdapter.FeedViewHolder> {
    private List<Feed> feedList;

    class FeedViewHolder extends RecyclerView.ViewHolder{
        TextView textNumber, textDate, textType, textAmount, textUnit;

        FeedViewHolder(@NonNull View view){
            super(view);

            textNumber = view.findViewById(R.id.textNumber);
            textDate = view.findViewById(R.id.textDate);
            textType = view.findViewById(R.id.textType);
            textAmount = view.findViewById(R.id.textAmount);
            textUnit = view.findViewById(R.id.textUnits);

        }
    }

    public FeedStatisticAdapter(List<Feed> feedList){this.feedList = feedList;}

    @NonNull
    @Override
    public FeedStatisticAdapter.FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_feed_row, parent, false);
        FeedStatisticAdapter.FeedViewHolder fvh = new FeedStatisticAdapter.FeedViewHolder(view);
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int i) {
        Feed feed = feedList.get(i);
        int increment = i+1;
        String dt = feed.getDateTime();
        String t = feed.getType();
        String a = feed.getAmount();
        String u = feed.getUnits();

        holder.textNumber.setText(String.valueOf(increment));
        holder.textDate.setText(dt);
        holder.textType.setText(t);
        holder.textAmount.setText(a);
        holder.textUnit.setText(u);

    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }
}
