package com.packages.scompass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.LikeViewHolder> {

    private Context context;
    private List<Like> likeList;

    public LikeAdapter(Context context, List<Like> likeList) {
        this.context = context;
        this.likeList = likeList;
    }

    @NonNull
    @Override
    public LikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.like_item, parent, false);
        return new LikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LikeViewHolder holder, int position) {
        Like like = likeList.get(position);
        // Display like information in the view
        holder.userIdTextView.setText(like.getUserId());
        holder.postIdTextView.setText(like.getPostId());
    }

    @Override
    public int getItemCount() {
        return likeList.size();
    }

    public static class LikeViewHolder extends RecyclerView.ViewHolder {
        TextView userIdTextView;
        TextView postIdTextView;

        public LikeViewHolder(View itemView) {
            super(itemView);
            userIdTextView = itemView.findViewById(R.id.userIdTextView);
            postIdTextView = itemView.findViewById(R.id.postIdTextView);
        }
    }

}
