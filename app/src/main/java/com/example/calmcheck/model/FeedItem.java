package com.example.calmcheck.model;

import com.google.gson.annotations.SerializedName;

public class FeedItem {
    @SerializedName("title")
    private String title;

    @SerializedName("summary")
    private String summary;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("articleUrl")
    private String articleUrl;

    public FeedItem(String title, String summary, String imageUrl, String articleUrl) {
        this.title = title;
        this.summary = summary;
        this.imageUrl = imageUrl;
        this.articleUrl = articleUrl;
    }

    public String getTitle() { return title; }
    public String getSummary() { return summary; }
    public String getImageUrl() { return imageUrl; }
    public String getArticleUrl() { return articleUrl; }
}
