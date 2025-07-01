package com.example.calmcheck;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calmcheck.adapter.ChatAdapter;
import com.example.calmcheck.model.ChatMessage;
import com.example.calmcheck.network.ChatRequest;
import com.example.calmcheck.network.ChatResponse;
import com.example.calmcheck.network.LlamaApi;
import com.example.calmcheck.network.LlamaApiClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatActivity extends AppCompatActivity {

    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList = new ArrayList<>();
    private DBHelper dbHelper;
    private LlamaApi api;

    private RecyclerView recycler;
    private EditText input;
    private TextView emptyState;

    private ChatMessage typingPlaceholder = null;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        input = findViewById(R.id.etMessage);
        ImageButton send = findViewById(R.id.btnSend);
        recycler = findViewById(R.id.recyclerView);
        emptyState = findViewById(R.id.tvEmptyState);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(messageList);
        recycler.setAdapter(chatAdapter);

        dbHelper = new DBHelper(this);
        SharedPreferences prefs = getSharedPreferences("calmcheck_user", MODE_PRIVATE);
        userId = prefs.getString("user_id", "default_user");

        api = LlamaApiClient.getInstance().getApi();

        loadHistory();
        updateEmptyState();

        send.setOnClickListener(v -> {
            String userInput = input.getText().toString().trim();
            if (userInput.isEmpty()) {
                Toast.makeText(this, "Please describe your symptom", Toast.LENGTH_SHORT).show();
                return;
            }

            input.setText("");

            ChatMessage userMsg = new ChatMessage("user", userInput);
            messageList.add(userMsg);
            chatAdapter.notifyItemInserted(messageList.size() - 1);
            recycler.scrollToPosition(messageList.size() - 1);
            updateEmptyState();

            typingPlaceholder = new ChatMessage("bot", "typing...");
            typingPlaceholder.setTyping(true);
            messageList.add(typingPlaceholder);
            chatAdapter.notifyItemInserted(messageList.size() - 1);
            recycler.scrollToPosition(messageList.size() - 1);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            api.sendMessage(new ChatRequest(userInput, userId)).enqueue(new Callback<ChatResponse>() {
                @Override
                public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                    if (isFinishing() || isDestroyed()) return;

                    runOnUiThread(() -> {
                        if (typingPlaceholder != null && messageList.contains(typingPlaceholder)) {
                            int index = messageList.indexOf(typingPlaceholder);
                            messageList.remove(index);
                            chatAdapter.notifyItemRemoved(index);
                        }

                        if (response.isSuccessful() && response.body() != null) {
                            String reply = response.body().getMessage();
                            String severity = response.body().getSeverity();
                            String summary = response.body().getSummary();

                            if (reply.contains("See a Doctor!!")) {
                                reply = reply + "\n\n🩺 Please seek immediate medical attention.";
                            }

                            ChatMessage botMsg = new ChatMessage("bot", reply);
                            messageList.add(botMsg);
                            chatAdapter.notifyItemInserted(messageList.size() - 1);
                            recycler.scrollToPosition(messageList.size() - 1);

                            dbHelper.insertSymptom(userInput, reply, severity, summary, timestamp, userId);
                        } else {
                            addErrorMessage("Response error: " + response.code());
                        }

                        updateEmptyState();
                    });
                }

                @Override
                public void onFailure(Call<ChatResponse> call, Throwable t) {
                    if (isFinishing() || isDestroyed()) return;

                    runOnUiThread(() -> {
                        if (typingPlaceholder != null && messageList.contains(typingPlaceholder)) {
                            int index = messageList.indexOf(typingPlaceholder);
                            messageList.remove(index);
                            chatAdapter.notifyItemRemoved(index);
                        }
                        addErrorMessage("Sorry, something went wrong.");
                        updateEmptyState();
                    });
                }

                private void addErrorMessage(String message) {
                    ChatMessage error = new ChatMessage("bot", message);
                    messageList.add(error);
                    chatAdapter.notifyItemInserted(messageList.size() - 1);
                    recycler.scrollToPosition(messageList.size() - 1);
                }
            });
        });
    }

    private void loadHistory() {
        api.getChatHistory(userId).enqueue(new Callback<List<ChatMessage>>() {
            @Override
            public void onResponse(Call<List<ChatMessage>> call, Response<List<ChatMessage>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    messageList.addAll(response.body());
                    chatAdapter.notifyDataSetChanged();
                    recycler.scrollToPosition(messageList.size() - 1);
                    updateEmptyState();
                }
            }

            @Override
            public void onFailure(Call<List<ChatMessage>> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Failed to load previous messages", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateEmptyState() {
        if (messageList.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);
        }
    }
}
