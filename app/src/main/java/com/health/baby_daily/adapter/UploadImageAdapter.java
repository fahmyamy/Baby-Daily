package com.health.baby_daily.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.health.baby_daily.R;

import java.util.List;

public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageAdapter.ViewHolder> {

    public List<String> fileNameList;
    public List<String> fileDoneList;

    public UploadImageAdapter(List<String> fileNameList, List<String> fileDoneList){
        this.fileNameList = fileNameList;
        this.fileDoneList = fileDoneList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_image_upload, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String fileName = fileNameList.get(i);
        viewHolder.fileNameView.setText(fileName);

        String fileDone = fileDoneList.get(i);

        if(fileDone.equals("uploading")){

            viewHolder.fileDoneView.setImageResource(R.drawable.progress);

        } else {

            viewHolder.fileDoneView.setImageResource(R.drawable.checked);

        }
    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public TextView fileNameView;
        public ImageView fileDoneView;

        public ViewHolder(View view){
            super(view);

            mView = itemView;

            fileNameView = mView.findViewById(R.id.upload_filename);
            fileDoneView = mView.findViewById(R.id.upload_loading);

        }
    }
}
