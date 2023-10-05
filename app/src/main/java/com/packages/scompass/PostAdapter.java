package com.packages.scompass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private final Context context;
    private final List<Post> posts;
    private final String currentUserId;
    private String userName;

    public PostAdapter(Context context, List<Post> posts, String currentUserId, String userName) {
        this.context = context;
        this.posts = posts;
        this.currentUserId = currentUserId;
        this.userName = userName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);

        // Populate the UI elements with post data
        holder.captionTextView.setText(post.getCaption());

        // Set the username to the usernameTextView
        holder.usernameTextView.setText(post.getUsername()); // Use the userName variable

        holder.likesCountTextView.setText(String.valueOf(post.getLikesCount()));
        holder.commentsCountTextView.setText(String.valueOf(post.getCommentsCount()));

        // Load the post image using Glide (you may need to add Glide to your dependencies)
        Glide.with(context)
                .load(post.getImageUrl())
                .into(holder.postImageView);

        // Show the image if is not -1 in imageview.
        // If the post uploader has no image, we get -1 in string.
        if (!post.getProfileImageUrl().equals("-1"))
        {
            Glide.with(context)
                    .load(post.getProfileImageUrl())
                    .into(holder.postProfileImageView);
        }

        // Check if the current user has already liked the post
        if (post.isLikedByCurrentUser(currentUserId)) {
//          change the image of like button here, 'Liked Image'
            holder.likeButton.setEnabled(false);
        } else {
//          change the image of like button here, 'Like Image'
            holder.likeButton.setEnabled(true);
        }



        // Handle like button click
        holder.likeButton.setOnClickListener(v -> {
            // Update the post's likes count and user's likedPosts list
            post.addLike(currentUserId);

            // Check if the post has a valid ID
            if (post.getPostId() != null && !post.getPostId().isEmpty()) {
                // Update the post in Firestore
                FirebaseFirestore.getInstance()
                        .collection("posts")
                        .document(post.getPostId())
                        .set(post, SetOptions.merge())
                        .addOnSuccessListener(aVoid -> {
//          change the image of like button here, 'Liked Image'
                            holder.likeButton.setEnabled(false);
                            holder.likesCountTextView.setText(String.valueOf(post.getLikesCount()));
                            Toast.makeText(context, "You liked the post!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
//          change the image of like button here, 'Like Image'
                            holder.likeButton.setEnabled(true);
                            Toast.makeText(context, "Failed to like the post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                // Handle the case where the post does not have a valid ID
                Toast.makeText(context, "Invalid post ID, cannot update like status.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText addcomment;
        TextView sendcomment;
        ImageView postImageView;
        // declare variable for profile name and image
        ImageView postProfileImageView;
        TextView usernameTextView;
        TextView captionTextView;
        TextView likesCountTextView;
        TextView commentsCountTextView;
        ImageView likeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sendcomment = itemView.findViewById(R.id.sendcomment);
            postImageView = itemView.findViewById(R.id.post_image);
            addcomment = itemView.findViewById(R.id.addcomment);
            postProfileImageView = itemView.findViewById(R.id.profile_post_image);
            captionTextView = itemView.findViewById(R.id.post_caption);
            usernameTextView = itemView.findViewById(R.id.post_username);
            likesCountTextView = itemView.findViewById(R.id.post_likes);
            commentsCountTextView = itemView.findViewById(R.id.post_comments);
            likeButton = itemView.findViewById(R.id.like_button);
        }
    }

    // Add the setPosts method to set the posts in the adapter
    @SuppressLint("NotifyDataSetChanged")
    public void setPosts(List<Post> posts) {
        this.posts.clear();
        this.posts.addAll(posts);
        notifyDataSetChanged();
    }
}
