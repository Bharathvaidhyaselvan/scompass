package com.packages.scompass;

public class Comment {
    private String userId;
    private String postId;
    private String text;

    public Comment() {
        // Default constructor required for Firestore
    }

    public Comment(String userId, String postId, String text) {
        this.userId = userId;
        this.postId = postId;
        this.text = text;
    }
    public String getUserId() {
        return userId;
    }

    public String getPostId() {
        return postId;
    }

    public String getText() {
        return text;
    }
}
