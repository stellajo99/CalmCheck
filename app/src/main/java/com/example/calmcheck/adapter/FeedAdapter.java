package com.example.calmcheck.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.calmcheck.R;
import com.example.calmcheck.model.FeedItem;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private List<FeedItem> articles = new ArrayList<>();
    private final Context context;

    public FeedAdapter(Context context) {
        this.context = context;
    }

    public void setArticles(List<FeedItem> newArticles) {
        this.articles = newArticles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeedItem item = articles.get(position);
        holder.title.setText(item.getTitle());
        holder.summary.setText(item.getSummary());

        String imageUrl = item.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context).load(imageUrl).into(holder.thumbnail);
        } else {
            holder.thumbnail.setImageResource(R.drawable.placeholder); // drawable에 placeholder 이미지 있어야 함
        }

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getArticleUrl()));
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title, summary;

        public ViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.imgThumbnail);
            title = view.findViewById(R.id.tvTitle);
            summary = view.findViewById(R.id.tvSummary);
        }
    }
}
