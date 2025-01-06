package coms309.Comments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class CommentsService {

    @Autowired
    CommentsRepo commentsRepo;

    public void addComment(String commenterUsername, String recipeOwnerUsername, int recipeId, String comment) throws SQLException{
        commentsRepo.addComment(commenterUsername, recipeOwnerUsername, recipeId, comment);
    }

    public List<Comment> getCommentsByRecipeId(int recipeId) throws SQLException{
        return commentsRepo.getCommentsByRecipeId(recipeId);
    }

    public List<Comment> getCommentsByCommenter(String commenterUsername) throws SQLException{
        return commentsRepo.getCommentsByCommenter(commenterUsername);
    }

    public List<Comment> getCommentsByRecipeOwner(String recipeOwnerUsername) throws  SQLException{
        return commentsRepo.getCommentsByRecipeOwner(recipeOwnerUsername);
    }

    public void deleteCommentByCommentId(int id) throws SQLException{
        commentsRepo.deleteCommentByCommentId(id);
    }

    public void deleteCommentsByRecipeId(int recipeId) throws SQLException{
        commentsRepo.deleteCommentByRecipeId(recipeId);
    }

    public void deleteCommentsByUser(String username) throws SQLException{
        commentsRepo.deleteCommentsByUser(username);
    }

    public void updateComment(int commentId, String newMessage) throws SQLException{
        commentsRepo.updateComment(commentId, newMessage);
    }
}
