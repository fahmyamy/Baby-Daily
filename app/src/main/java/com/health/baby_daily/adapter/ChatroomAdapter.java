package com.health.baby_daily.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.health.baby_daily.R;
import com.health.baby_daily.model.Chatroom;

import java.util.List;

public class ChatroomAdapter extends RecyclerView.Adapter<ChatroomAdapter.ChatroomViewHolder> {
    private static final int TYPE_ONE = 1;
    private static final int TYPE_TWO = 2;
    private List<Chatroom> chatroomList;
    private OnItemClickListener mListener;
    private FirebaseAuth firebaseAuth;
    private String id;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void addItem(Chatroom chatroom){
        chatroomList.add(chatroom);
        notifyItemInserted(chatroomList.size());
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class ChatroomViewHolder extends RecyclerView.ViewHolder {
        public TextView textContent_sender, dateTime_sender, textContent_receiver, dateTime_receiver;
        public LinearLayout lineChat, sender_bubble, receiver_bubble;

        public ChatroomViewHolder(View itemView, final OnItemClickListener listener){
            super(itemView);

            textContent_sender = itemView.findViewById(R.id.textContent_sender);
            dateTime_sender = itemView.findViewById(R.id.dateTime_sender);
            textContent_receiver = itemView.findViewById(R.id.textContent_receiver);
            dateTime_receiver = itemView.findViewById(R.id.dateTime_receiver);
            lineChat = itemView.findViewById(R.id.lineChat);
            sender_bubble = itemView.findViewById(R.id.sender_bubble);
            receiver_bubble = itemView.findViewById(R.id.receiver_bubble);

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

    public ChatroomAdapter(List<Chatroom> chatroomList){
        this.chatroomList = chatroomList;
    }

    @Override
    public ChatroomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_box, parent, false);
            ChatroomViewHolder cvh = new ChatroomViewHolder(view, mListener);
            return cvh;
    }

    @Override
    public void onBindViewHolder( ChatroomViewHolder holder, int position) {
        Chatroom chat = chatroomList.get(position);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        id = user.getUid().toString();

        if (chat.getSender_id().equals(id)){

            String text = chat.getTextContent().toString().trim();
            String date = chat.getCreated().toString().trim();

            holder.sender_bubble.setVisibility(View.VISIBLE);
            holder.receiver_bubble.setVisibility(View.GONE);
            holder.textContent_sender.setText(text);
            Linkify.addLinks(holder.textContent_sender, Linkify.WEB_URLS);
            holder.dateTime_sender.setText(date);

        }else if (chat.getReceiver_id().equals(id)){

            String text = chat.getTextContent().toString().trim();
            String date = chat.getCreated().toString().trim();

            holder.sender_bubble.setVisibility(View.GONE);
            holder.receiver_bubble.setVisibility(View.VISIBLE);
            holder.textContent_receiver.setText(text);
            Linkify.addLinks(holder.textContent_receiver, Linkify.WEB_URLS);
            holder.dateTime_receiver.setText(date);
        }
    }

    @Override
    public int getItemCount() {
        return chatroomList.size();
    }


}
