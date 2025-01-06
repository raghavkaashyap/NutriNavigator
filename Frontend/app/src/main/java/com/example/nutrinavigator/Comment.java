package com.example.nutrinavigator;

public class Comment {
    private int commentId;
    private String commenterUsername;
    private String recipeOwnerUsername;
    private int recipeId;
    private String comment;

    public Comment(String commenterUsername, String comments) {
        this.commenterUsername = commenterUsername;
        this.comment = comment;
    }

    public int getCommentId(){
        return commentId;
    }

    public void setCommentId(int commentId){
        this.commentId = commentId;
    }

    public String getCommenterUser(){
        return commenterUsername;
    }

    public void setCommenterUser(String commenterUsername){
        this.commenterUsername = commenterUsername;
    }

    public String getRecipeOwnerUsername(){
        return recipeOwnerUsername;
    }

    public void setRecipeOwnerUsername(String recipeOwnerUsername){
        this.recipeOwnerUsername = recipeOwnerUsername;
    }

    public int getRecipeId(){
        return recipeId;
    }

    public void setRecipeId(int recipeId){
        this.recipeId = recipeId;
    }

    public String getComments(){
        return comment;
    }

    public void setComments(String comment){
        this.comment = comment;
    }

}
