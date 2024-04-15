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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = inflater.inflate(R.layout.chat_item,parent,false);
            return new ViewHolder(view);
        }
        else{
            View view = inflater.inflate(R.layout.chat_item,parent,false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        ViewHolder(View view){
            super(view);
            show_message = view.findViewById(R.id.message);
        }
    }
}