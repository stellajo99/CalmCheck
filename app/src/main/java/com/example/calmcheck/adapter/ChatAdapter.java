package com.example.calmcheck.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.calmcheck.R;
import com.example.calmcheck.model.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private final List<ChatMessage> messages;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        TextView tvSender;
        LottieAnimationView lottieTyping;

        public ViewHolder(View view) {
            super(view);
            tvMessage = view.findViewById(R.id.textMessage);
            tvSender = view.findViewById(R.id.tvUserInitial);
            lottieTyping = view.findViewById(R.id.typingAnimation);  // only available in bot layout
        }
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage msg = messages.get(position);
        return msg.getSender().equals("user") ? 1 : 0;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = (viewType == 1)
                ? R.layout.item_chat_user
                : R.layout.item_chat_bot;

        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        ChatMessage msg = messages.get(position);

        // If it's a typing placeholder
        if (msg.isTyping() && holder.lottieTyping != null) {
            holder.tvMessage.setVisibility(View.GONE);
            holder.lottieTyping.setVisibility(View.VISIBLE);
            holder.lottieTyping.playAnimation();
        } else {
            if (holder.tvMessage != null) {
                holder.tvMessage.setVisibility(View.VISIBLE);
                holder.tvMessage.setText(msg.getMessage() != null ? msg.getMessage() : "[Empty]");
            }
            if (holder.lottieTyping != null) {
                holder.lottieTyping.cancelAnimation();
                holder.lottieTyping.setVisibility(View.GONE);
            }
        }

        if (holder.tvSender != null) {
            holder.tvSender.setText(msg.getSender().equals("user") ? "You" : "AI");
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
