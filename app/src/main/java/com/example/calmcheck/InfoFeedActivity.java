package com.example.calmcheck;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.calmcheck.adapter.FeedAdapter;
import com.example.calmcheck.model.FeedItem;
import com.example.calmcheck.network.LlamaApiClient;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoFeedActivity extends AppCompatActivity {

    private FeedAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tvNoArticles;
    private LottieAnimationView loadingAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_feed);

        SharedPreferences prefs = getSharedPreferences("calmcheck_user", MODE_PRIVATE);
        String userId = prefs.getString("user_id", "default_user");

        TextView tvUserInfo = findViewById(R.id.tvUserInfo);
        tvUserInfo.setText("Welcome, " + userId);

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        Button btnChat = findViewById(R.id.btnGoToChat);
        btnChat.setOnClickListener(v -> {
            startActivity(new Intent(this, ChatActivity.class));
        });

        tvNoArticles = findViewById(R.id.tvNoArticles);
        recyclerView = findViewById(R.id.rvFeed);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FeedAdapter(this);
        recyclerView.setAdapter(adapter);

        loadingAnimation = findViewById(R.id.loadingAnimation);

        // 🔄 Show loading animation
        showLoading(true);

        List<String> keywords = Arrays.asList("health");

        LlamaApiClient.getInstance().getApi().getFeed().enqueue(new Callback<List<FeedItem>>() {
            @Override
            public void onResponse(Call<List<FeedItem>> call, Response<List<FeedItem>> response) {
                if (isFinishing() || isDestroyed()) return; // ✅ Safety check
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    List<FeedItem> items = response.body();
                    Log.d("Feed", "Loaded articles: " + items.size());

                    if (items.isEmpty()) {
                        showError("No articles found.");
                    } else {
                        tvNoArticles.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter.setArticles(items);
                    }
                } else {
                    Log.e("Feed", "Response error: " + response.code());
                    showError("Error loading feed.");
                }
            }

            @Override
            public void onFailure(Call<List<FeedItem>> call, Throwable t) {
                if (isFinishing() || isDestroyed()) return; // ✅ Safety check
                showLoading(false);
                Log.e("Feed", "Feed fetch failed", t);
                showError("Unable to load feed.");
            }
        });
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            loadingAnimation.setVisibility(View.VISIBLE);
            loadingAnimation.playAnimation();
            recyclerView.setVisibility(View.GONE);
            tvNoArticles.setVisibility(View.GONE);
        } else {
            loadingAnimation.cancelAnimation();
            loadingAnimation.setVisibility(View.GONE);
        }
    }

    private void showError(String message) {
        tvNoArticles.setText(message);
        tvNoArticles.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }
}
