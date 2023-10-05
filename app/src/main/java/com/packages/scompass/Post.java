package com.packages.scompass;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private String imageUrl,profileimageUrl;
    private String postName;
    private String caption;
    private String username;
    private int likes;
    private int commentsCount;
    private String postId;
    private List<Comment> comments;
    private List<String> likedByUserIds;

    public Post() {
        // Initialize any default values here
        this.comments = new ArrayList<>();
        this.likedByUserIds = new ArrayList<>();
    }

    public Post(String imageUrl,String profileimageUrl, String postName, String caption, String username, int likes, int commentsCount, String postId) {
        this.profileimageUrl = profileimageUrl;
        this.imageUrl = imageUrl;
        this.postName = postName;
        this.caption = caption;
        this.username = username;
        this.likes = likes;
        this.commentsCount = commentsCount;
        this.postId = postId;
        this.comments = new ArrayList<>();
        this.likedByUserIds = new ArrayList<>();
    }

    public String getPostImage() {
        return imageUrl;
    }

    public String getPostName() {
        return postName;
    }

    public String getCaption() {
        return caption;
    }

    public String getUsername() {
        return username;
    }

    public int getLikesCount() {
        return likes;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getProfileImageUrl() {
        return profileimageUrl;
    }

    public String getPostId() {
        return postId;
    }

    public void addLike(String userId) {
        // Check if the user has already liked the post
        if (!likedByUserIds.contains(userId)) {
            likedByUserIds.add(userId);
            likes++;
        }
    }

    public boolean isLikedByCurrentUser(String currentUserId) {
        return likedByUserIds.contains(currentUserId);
    }

    public void addComment(Comment comment) {
        commentsCount++;
        // Add the comment to the list of comments
        comments.add(comment);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public static class Comment {
        private String username;
        private String text;

        public Comment(String username, String text) {
            this.username = username;
            this.text = text;
        }

        public String getUsername() {
            return username;
        }

        public String getText() {
            return text;
        }
    }
}
