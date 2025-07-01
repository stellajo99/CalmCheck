package com.example.calmcheck.model;

public class ChatMessage {
    private String sender;
    private String message;
    private boolean isTyping;

    public ChatMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() { return sender; }
    public String getMessage() { return message; }

    public boolean isTyping() {
        return isTyping;
    }

    public void setTyping(boolean typing) {
        isTyping = typing;
    }
}
