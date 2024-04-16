package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {
    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;
    private final LayoutInflater inflater;
    private List<Message> messages;

    public MessageListAdapter(Context context, List<Message> enterSliderItems) {
        this.inflater = LayoutInflater.from(context);
        this.messages = enterSliderItems;
    }

    @NonNull
    @Override
    public MessageListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = inflater.inflate(R.layout.chat_item,parent,false);
            return new ViewHolder(view);
        }
        else{
            View view = inflater.inflate(R.layout.chat_item_ai,parent,false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageListAdapter.ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.user.setText(message.getSender());
        holder.showMessage.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).isBot()){
            return MSG_TYPE_LEFT;
        }else{
            return MSG_TYPE_RIGHT;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView showMessage;
        public TextView user;

        ViewHolder(View view){
            super(view);
            user = view.findViewById(R.id.sender);
            showMessage = view.findViewById(R.id.message);
        }
    }
}