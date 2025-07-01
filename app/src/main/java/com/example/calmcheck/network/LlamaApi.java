package com.example.calmcheck.network;

import com.example.calmcheck.model.ChatMessage;
import com.example.calmcheck.model.FeedItem;
import com.example.calmcheck.model.FeedRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LlamaApi {
    @POST("/chat")
    Call<ChatResponse> sendMessage(@Body ChatRequest request);

    @GET("/feed")
    Call<List<FeedItem>> getFeed();

    @GET("/history")
    Call<List<ChatMessage>> getChatHistory(@Query("user_id") String userId);

}
