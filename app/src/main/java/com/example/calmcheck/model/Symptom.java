package com.example.calmcheck.model;

public class Symptom {
    private int id;
    private String description;
    private String response;
    private String severity;
    private String summary;
    private String date;
    private String userId;

    public Symptom(int id, String description, String response, String severity, String summary, String date, String userId) {
        this.id = id;
        this.description = description;
        this.response = response;
        this.severity = severity;
        this.summary = summary;
        this.date = date;
        this.userId = userId;
    }

    public int getId() { return id; }
    public String getDescription() { return description; }
    public String getResponse() { return response; }
    public String getSeverity() { return severity; }
    public String getSummary() { return summary; }
    public String getDate() { return date; }
    public String getUserId() { return userId; }
}
