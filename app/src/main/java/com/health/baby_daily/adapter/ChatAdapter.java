package com.health.baby_daily.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.health.baby_daily.R;
import com.health.baby_daily.model.Chat;
import com.health.baby_daily.model.Chatroom;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private Context context;
    private List<Chat> chatroomList;
    private OnItemClickListener mListener;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference table_user;
    private DatabaseReference table_content;
    private Query query;
    private String id;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        public TextView chatUsername, dateText, contentText;
        public ImageView chatImage;

        public ChatViewHolder(View itemView, final OnItemClickListener listener){
            super(itemView);

            chatUsername = itemView.findViewById(R.id.chatUsername);
            dateText = itemView.findViewById(R.id.dateText);
            contentText = itemView.findViewById(R.id.contentText);
            chatImage = itemView.findViewById(R.id.chatImage);

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

    public ChatAdapter(List<Chat> chatroomList){
        this.chatroomList = chatroomList;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.entry_chat, parent, false);
        ChatViewHolder cvh = new ChatViewHolder(view, mListener);
        return cvh;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(final ChatViewHolder holder, final int position) {
        final Chat chat = chatroomList.get(position);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            id = user.getUid();
        }

        table_content = FirebaseDatabase.getInstance().getReference().child("Chat");
        query = table_content.orderByChild("timestamp").limitToLast(1);

        if (chat.getUserA().equals(id)){
            String second_id = chat.getUserB();

            table_user = FirebaseDatabase.getInstance().getReference("User").child(second_id);
            table_user.keepSynced(true);
            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        String name = dataSnapshot.child("username").getValue().toString();
                        String image = dataSnapshot.child("image").getValue().toString();

                        holder.chatUsername.setText(name);
                        if (!image.equals("none")){
                            Picasso.get().load(image).into(holder.chatImage);
                        }else {
                            Picasso.get().load(R.drawable.user_image).into(holder.chatImage);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                            Chatroom chatA = snapshot.getValue(Chatroom.class);
                            if (chatA.getChat_id().equals(chat.getId())){
                                holder.contentText.setText(chatA.getTextContent());
                                holder.dateText.setText(chatA.getCreated());
                            }
                        }
                        notifyItemChanged(position);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        if (chat.getUserB().equals(id)){
            String second_id = chat.getUserA();

            table_user = FirebaseDatabase.getInstance().getReference("User").child(second_id);
            table_user.keepSynced(true);
            table_user.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        String name = Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString();
                        String image = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();

                        holder.chatUsername.setText(name);
                        if (!image.equals("none")){
                            Picasso.get().load(image).into(holder.chatImage);
                        }else {
                            Picasso.get().load(R.drawable.user_image).into(holder.chatImage);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                            Chatroom chatB = snapshot.getValue(Chatroom.class);
                            if (chatB.getChat_id().equals(chat.getId())){
                                holder.contentText.setText(chatB.getTextContent());
                                holder.dateText.setText(chatB.getCreated());
                            }
                        }
                        notifyItemChanged(position);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return chatroomList.size();
    }

}