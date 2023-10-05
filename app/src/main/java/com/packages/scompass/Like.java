package com.packages.scompass;

public class Like {
    private String userId;
    private String postId;

    public Like() {
    }

    public Like(String userId, String postId) {
        this.userId = userId;
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public String getPostId() {
        return postId;
    }
}
