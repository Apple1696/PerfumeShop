package com.example.perfumeshop.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perfumeshop.R;
import com.example.perfumeshop.data.models.entities.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {
    private List<Comment> comments;
    private Context context;

    public CommentsAdapter(Context context) {
        this.context = context;
        this.comments = new ArrayList<>();
    }

    public void updateComments(List<Comment> newComments) {
        this.comments.clear();
        if (newComments != null) {
            this.comments.addAll(newComments);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUserName;
        private TextView textViewContent;
        private TextView textViewDate;
        private RatingBar ratingBarComment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewContent = itemView.findViewById(R.id.textViewContent);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            ratingBarComment = itemView.findViewById(R.id.ratingBarComment);
        }

        public void bind(Comment comment) {
            // Use author username if available, otherwise fall back to userName field
            String username = "Anonymous";
            if (comment.getAuthor() != null && comment.getAuthor().getUsername() != null) {
                username = comment.getAuthor().getUsername();
            } else if (comment.getUserName() != null) {
                username = comment.getUserName();
            }
            textViewUserName.setText(username);
            
            textViewContent.setText(comment.getContent());
            
            // Set rating
            if (ratingBarComment != null) {
                ratingBarComment.setRating(comment.getRating());
            }
            
            // Format date if available
            if (comment.getCreatedAt() != null && !comment.getCreatedAt().isEmpty()) {
                try {
                    // Parse ISO date format: 2025-07-12T18:17:16.291Z
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                    Date date = formatter.parse(comment.getCreatedAt());
                    SimpleDateFormat displayFormatter = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                    textViewDate.setText(displayFormatter.format(date));
                } catch (Exception e) {
                    textViewDate.setText(comment.getCreatedAt());
                }
            } else {
                textViewDate.setText("Recent");
            }
        }
    }
}
