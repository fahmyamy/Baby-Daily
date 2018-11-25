package com.health.baby_daily.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.docmenu.VideoPost;
import com.health.baby_daily.docmenu.uploadVideo.UpdateVideo;
import com.health.baby_daily.docmenu.uploadVideo.ViewVideo;
import com.health.baby_daily.model.Video;

import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {
    private List<Video> videoList;
    private Context context;

    class VideoViewHolder extends RecyclerView.ViewHolder{
        TextView textNumber, textCreated, textTitle, textType, textView;
        ImageButton settingBtn;

        public VideoViewHolder(@NonNull View view) {
            super(view);

            textNumber = view.findViewById(R.id.textNumber);
            textCreated = view.findViewById(R.id.textCreated);
            textType = view.findViewById(R.id.textType);
            textTitle = view.findViewById(R.id.textTitle);
            textView = view.findViewById(R.id.textView);
            settingBtn = view.findViewById(R.id.settingBtn);
        }
    }

    public VideoListAdapter(Context context, List<Video> videoList){
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoListAdapter.VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.table_video_row, parent, false);
        VideoListAdapter.VideoViewHolder pla = new VideoListAdapter.VideoViewHolder(view);
        return pla;
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoListAdapter.VideoViewHolder holder, int i) {
        final Video video = videoList.get(i);
        int increment = i+1;
        String created = video.getCreated();
        String title = video.getTitle();
        String type = video.getType();

        holder.textNumber.setText(String.valueOf(increment));
        holder.textCreated.setText(created);
        holder.textType.setText(type);
        holder.textTitle.setText(WordUtils.capitalize(title));

        DatabaseReference count = FirebaseDatabase.getInstance().getReference().child("PostCounter");
        Query qCount = count.orderByChild("postId").equalTo(video.getId());
        qCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String counter = String.valueOf(dataSnapshot.getChildrenCount());
                    holder.textView.setText(counter);
                }else if (!dataSnapshot.exists()){
                    holder.textView.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_video, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int item = menuItem.getItemId();

                        if (item == R.id.action_view){
                            Intent intent = new Intent(context, ViewVideo.class);
                            intent.putExtra("video_id", video.getId());
                            context.startActivity(intent);
                            return true;
                        }else if (item == R.id.action_update) {
                            Intent intent = new Intent(context, UpdateVideo.class);
                            intent.putExtra("video_id", video.getId());
                            context.startActivity(intent);
                            return true;
                        } else if (item == R.id.action_delete) {
                            String id = video.getId();
                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Video").child(id);
                            databaseReference.removeValue();
                            Toast.makeText(context, "Video Post Removed!!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, VideoPost.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                            return true;
                        }

                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }
}