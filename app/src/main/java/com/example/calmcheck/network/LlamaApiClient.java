package com.example.calmcheck.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LlamaApiClient {
    private static LlamaApiClient instance;
    private final LlamaApi api;

    private LlamaApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")  // Flask URL (This is android emulator url, change this url to yor working url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(LlamaApi.class);
    }

    public static synchronized LlamaApiClient getInstance() {
        if (instance == null) {
            instance = new LlamaApiClient();
        }
        return instance;
    }

    public LlamaApi getApi() {
        return api;
    }
}
