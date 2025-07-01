package com.example.calmcheck.model;

import java.util.List;

public class FeedRequest {
    private List<String> keywords;

    public FeedRequest(List<String> keywords) {
        this.keywords = keywords;
    }
}
