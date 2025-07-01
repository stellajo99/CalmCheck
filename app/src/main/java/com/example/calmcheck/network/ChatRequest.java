package com.example.calmcheck.network;

public class ChatRequest {
    private String input;
    private String user_id;

    public ChatRequest(String input, String user_id) {
        this.input = input;
        this.user_id = user_id;
    }

    public String getInput() { return input; }
    public String getUser_id() { return user_id; }
}
