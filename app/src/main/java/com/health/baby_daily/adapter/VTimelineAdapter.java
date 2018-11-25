package com.health.baby_daily.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.contact.ContactList;
import com.health.baby_daily.docmenu.uploadVideo.FullscreenVideo;
import com.health.baby_daily.guide.PostProfile;
import com.health.baby_daily.model.Notify;
import com.health.baby_daily.model.PostCounter;
import com.health.baby_daily.model.Post_Like;
import com.health.baby_daily.model.Report;
import com.health.baby_daily.model.Video;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class VTimelineAdapter extends RecyclerView.Adapter<VTimelineAdapter.VTimeViewHolder> {
    private DatabaseReference table_user;
    private FirebaseAuth firebaseAuth;

    private String currId;
    private List<Video> videoList;
    private Context context;
    private boolean isPlaying;

    class VTimeViewHolder extends RecyclerView.ViewHolder {
        ImageView timelineImage, expand_menu, playBtn, fullscreenBtn, buttonLike, buttonUnLike, shareBtn;
        TextView username, dateTime, textTitle, countLike, countViewer;
        VideoView videoView;
        FrameLayout fullscreenFrame;

        public VTimeViewHolder(@NonNull View view) {
            super(view);

            timelineImage = view.findViewById(R.id.timelineImage);
            expand_menu = view.findViewById(R.id.expand_menu);
            username = view.findViewById(R.id.username);
            dateTime = view.findViewById(R.id.dateTime);
            textTitle = view.findViewById(R.id.textTitle);
            videoView = view.findViewById(R.id.videoView);
            playBtn = view.findViewById(R.id.playBtn);
            fullscreenBtn = view.findViewById(R.id.fullscreenBtn);
            fullscreenFrame = view.findViewById(R.id.fullscreenFrame);
            buttonLike = view.findViewById(R.id.buttonLike);
            buttonUnLike = view.findViewById(R.id.buttonUnLike);
            countLike = view.findViewById(R.id.countLike);
            countViewer = view.findViewById(R.id.countViewer);
            shareBtn = view.findViewById(R.id.shareBtn);
        }
    }

    public VTimelineAdapter(List<Video> videoList) {
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VTimelineAdapter.VTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.entry_vtimeline, parent, false);
        VTimelineAdapter.VTimeViewHolder vtla = new VTimelineAdapter.VTimeViewHolder(view);
        return vtla;
    }

    @Override
    public void onBindViewHolder(@NonNull final VTimelineAdapter.VTimeViewHolder holder, int i) {
        final Video video = videoList.get(i);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        currId = user.getUid();

        final String uid = video.getUid();
        table_user = FirebaseDatabase.getInstance().getReference("User").child(uid);
        table_user.keepSynced(true);
        table_user.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString();
                    String image = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();

                    holder.username.setText(username);
                    if (!image.equals("none")) {
                        Picasso.get().load(image).into(holder.timelineImage);
                    } else {
                        Picasso.get().load(R.drawable.user_image).into(holder.timelineImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        String title = video.getTitle();
        String dateTime = video.getCreated();

        holder.textTitle.setText(WordUtils.capitalize(title));
        holder.dateTime.setText(dateTime);

        if (!currId.equals(video.getUid())) {
            holder.expand_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String[] menu = {"User Profile", "Report Video"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setItems(menu, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (menu[which].equals("User Profile")) {
                                Intent intent = new Intent(context, PostProfile.class);
                                intent.putExtra("user_id", video.getUid());
                                context.startActivity(intent);
                            }else if (menu[which].equals("Report Video")) {
                                DatabaseReference table_report = FirebaseDatabase.getInstance().getReference("Post_Report");
                                Report report = new Report(video.getId(), uid);
                                table_report.push().setValue(report);
                                Toast.makeText(context, "Report Submitted.. Admin will check it", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.show();
                }
            });
        }else {
            holder.expand_menu.setVisibility(View.GONE);
        }

        final String video_url = video.getVideo();
        holder.videoView.setVideoURI(Uri.parse(video_url));
        holder.videoView.requestFocus();
        holder.videoView.start();
        isPlaying = true;
        holder.playBtn.setImageResource(R.drawable.ic_pause_circle);
        holder.fullscreenFrame.setVisibility(View.VISIBLE);

        holder.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying){
                    holder.videoView.pause();
                    isPlaying = false;
                    holder.playBtn.setImageResource(R.drawable.ic_play_circle);
                    holder.fullscreenFrame.setVisibility(View.GONE);
                }else {
                    holder.videoView.start();
                    isPlaying = true;
                    holder.playBtn.setImageResource(R.drawable.ic_pause_circle);
                    holder.fullscreenFrame.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.fullscreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference table_post = FirebaseDatabase.getInstance().getReference().child("PostCounter");
                Query query = table_post.orderByChild("postId_uid").equalTo(video.getId()+"_"+currId);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()){
                            String count = UUID.randomUUID().toString();
                            DatabaseReference table_addCount = FirebaseDatabase.getInstance().getReference().child("PostCounter");
                            PostCounter counter = new PostCounter(count,  video.getId(),video.getId()+"_"+currId);
                            table_addCount.child(count).setValue(counter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                holder.videoView.pause();
                Intent intent1 = new Intent(context, FullscreenVideo.class);
                intent1.putExtra("video_id", video_url);
                context.startActivity(intent1);
            }
        });

        holder.timelineImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostProfile.class);
                intent.putExtra("user_id", video.getUid());
                context.startActivity(intent);
            }
        });

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostProfile.class);
                intent.putExtra("user_id", video.getUid());
                context.startActivity(intent);
            }
        });


        final List<Post_Like> post_likes = new ArrayList<>();
        final DatabaseReference table_like = FirebaseDatabase.getInstance().getReference("Post_Like");
        Query query = table_like.orderByChild("post_id").equalTo(video.getId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    int count = 0;
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()){
                        final Post_Like post_like = snapshot.getValue(Post_Like.class);
                        post_likes.add(post_like);
                        count++;
                        holder.countLike.setText(String.valueOf(count));
                        if (post_like.getUser_id().equals(uid) && post_like.getPost_id().equals(video.getId()) && post_like.getType().equals("Video")){
                            holder.buttonLike.setVisibility(View.VISIBLE);
                            holder.buttonUnLike.setVisibility(View.GONE);
                            holder.buttonLike.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int total = Integer.parseInt(holder.countLike.getText().toString()) - 1;
                                    holder.countLike.setText(String.valueOf(total));
                                    DatabaseReference delete_like = FirebaseDatabase.getInstance().getReference("Post_Like").child(snapshot.getKey());
                                    delete_like.removeValue();
                                    holder.buttonLike.setVisibility(View.GONE);
                                    holder.buttonUnLike.setVisibility(View.VISIBLE);
                                }
                            });
                        }else if (!post_like.getUser_id().equals(uid) && !post_like.getPost_id().equals(video.getId()) && !post_like.getType().equals("Video")){
                            holder.buttonLike.setVisibility(View.GONE);
                            holder.buttonUnLike.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.buttonUnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int total = Integer.parseInt(holder.countLike.getText().toString()) + 1;
                holder.countLike.setText(String.valueOf(total));
                String id = UUID.randomUUID().toString();
                Post_Like pL = new Post_Like(id, video.getId(), uid, "Video");
                table_like.child(id).setValue(pL);
                holder.buttonLike.setVisibility(View.VISIBLE);
                holder.buttonUnLike.setVisibility(View.GONE);


                DatabaseReference table_notify = FirebaseDatabase.getInstance().getReference("Notifications");
                Notify notify = new Notify(currId, "likeV", null);
                table_notify.child(uid).push().setValue(notify);
            }
        });

        DatabaseReference count = FirebaseDatabase.getInstance().getReference().child("PostCounter");
        Query qCount = count.orderByChild("postId").equalTo(video.getId());
        qCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String counter = String.valueOf(dataSnapshot.getChildrenCount());
                    holder.countViewer.setText(counter);
                }else if (!dataSnapshot.exists()){
                    holder.countViewer.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptView = layoutInflater.inflate(R.layout.alertdialog_custom, null);

                final AlertDialog alertD = new AlertDialog.Builder(context).create();

                ImageView inAppShare = (ImageView) promptView.findViewById(R.id.inAppShare);

                ImageView otherAppShare = (ImageView) promptView.findViewById(R.id.otherAppShare);

                inAppShare.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //inAppShare
                        String shareBody = "https://babydaily-a6e5a.firebaseapp.com/video/index.html?video_id="+ video.getId() + System.lineSeparator() +"Hey try this out!!";
                        Intent shareIntent = new Intent(context, ContactList.class);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra("share_content", shareBody);
                        context.startActivity(shareIntent);
                        alertD.dismiss();
                    }
                });

                otherAppShare.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String shareBody = "https://babydaily-a6e5a.firebaseapp.com/video/index.html?video_id="+ video.getId() + System.lineSeparator() +"Hey try this out!!";
                        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Baby-Daily");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                        context.startActivity(Intent.createChooser(shareIntent,"Share via"));
                        alertD.dismiss();
                    }
                });
                alertD.setView(promptView);
                alertD.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

}