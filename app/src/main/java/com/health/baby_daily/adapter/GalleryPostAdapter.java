package com.health.baby_daily.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.health.baby_daily.R;
import com.health.baby_daily.model.PostGallery;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GalleryPostAdapter extends RecyclerView.Adapter<GalleryPostAdapter.GalleryViewHolder> {
    private List<PostGallery> postGalleryList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder{
        ImageView galImage;

        GalleryViewHolder(@NonNull View view, final OnItemClickListener listener){
            super(view);

            galImage = view.findViewById(R.id.thumbnail);

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

    public GalleryPostAdapter(List<PostGallery> postGalleryList){
        this.postGalleryList = postGalleryList;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_view, parent, false);
        GalleryPostAdapter.GalleryViewHolder svh = new GalleryPostAdapter.GalleryViewHolder(view, mListener);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder galleryViewHolder, int i) {
        PostGallery post = postGalleryList.get(i);

        Picasso.get().load(post.getImage()).fit().into(galleryViewHolder.galImage);
    }

    @Override
    public int getItemCount() {
        return postGalleryList.size();
    }
}
