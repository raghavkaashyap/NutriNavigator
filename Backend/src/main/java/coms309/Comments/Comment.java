package coms309.Comments;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;

public class Comment {

    private int id;
    private String commenter;
    private String recipeOwner;
    private int recipeId;
    private String comment;
    private Timestamp commentDate;

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getCommenter() {
        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public String getRecipeOwner() {
        return recipeOwner;
    }

    public void setRecipeOwner(String recipeOwner) {
        this.recipeOwner = recipeOwner;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Timestamp commentDate) {
        this.commentDate = commentDate;
    }
}
