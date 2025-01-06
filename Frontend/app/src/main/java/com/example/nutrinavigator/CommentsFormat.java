package com.example.nutrinavigator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class CommentsFormat extends RecyclerView.Adapter<CommentsFormat.CommentsViewHolder> {
    private List<Comment> comments;

    public CommentsFormat(List<Comment> comments){
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position){
        Comment comment = comments.get(position);
        holder.viewCommenterUser.setText(comment.getCommenterUser());
        holder.viewComment.setText(comment.getComments());
    }

    @Override
    public int getItemCount(){
        return comments.size();
    }

    public static class CommentsViewHolder extends RecyclerView.ViewHolder {
        public TextView viewCommenterUser;
        public TextView viewComment;

        public CommentsViewHolder(View item){
            super(item);
            viewCommenterUser = item.findViewById(R.id.vCommenterUser);
            viewComment = item.findViewById(R.id.vComment);
        }
    }

}

