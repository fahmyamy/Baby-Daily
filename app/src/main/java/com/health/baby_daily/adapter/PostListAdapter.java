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
import com.health.baby_daily.docmenu.MedicalPost;
import com.health.baby_daily.docmenu.createPost.UpdatePost;
import com.health.baby_daily.docmenu.createPost.ViewPost;
import com.health.baby_daily.model.Post;

import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder> {
    private List<Post> postList;
    private Context context;

    class PostViewHolder extends RecyclerView.ViewHolder{
        TextView textNumber, textCreated, textModified, textTitle, textView;
        ImageButton settingBtn;

        public PostViewHolder(@NonNull View view) {
            super(view);

            textNumber = view.findViewById(R.id.textNumber);
            textCreated = view.findViewById(R.id.textCreated);
            textModified = view.findViewById(R.id.textModified);
            textTitle = view.findViewById(R.id.textTitle);
            textView = view.findViewById(R.id.textView);
            settingBtn = view.findViewById(R.id.settingBtn);
        }
    }

    public PostListAdapter(Context context, List<Post> postList){
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostListAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.table_post_row, parent, false);
        PostListAdapter.PostViewHolder pla = new PostListAdapter.PostViewHolder(view);
        return pla;
    }

    @Override
    public void onBindViewHolder(@NonNull final PostListAdapter.PostViewHolder holder, int i) {
        final Post post = postList.get(i);
        int increment = i+1;
        String created = post.getCreated();
        String modified = post.getModified();
        String title = post.getTitle();

        holder.textNumber.setText(String.valueOf(increment));
        holder.textCreated.setText(created);
        holder.textModified.setText(modified);
        holder.textTitle.setText(WordUtils.capitalize(title));

        DatabaseReference count = FirebaseDatabase.getInstance().getReference().child("PostCounter");
        Query qCount = count.orderByChild("postId").equalTo(post.getId());
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
                popupMenu.getMenuInflater().inflate(R.menu.menu_post, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int item = menuItem.getItemId();

                        if (item == R.id.action_view){
                            Intent intent = new Intent(context, ViewPost.class);
                            intent.putExtra("post_id", post.getId());
                            context.startActivity(intent);
                            return true;
                        }else if (item == R.id.action_update) {
                            Intent intent = new Intent(context, UpdatePost.class);
                            intent.putExtra("post_id", post.getId());
                            context.startActivity(intent);
                            return true;
                        } else if (item == R.id.action_delete) {
                            String id = post.getId();
                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Post").child(id);
                            databaseReference.removeValue();
                            Toast.makeText(context, "Post Removed!!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, MedicalPost.class);
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
        return postList.size();
    }
}
