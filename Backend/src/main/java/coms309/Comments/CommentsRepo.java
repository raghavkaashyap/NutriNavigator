package coms309.Comments;

import coms309.Notifications.NotificationController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CommentsRepo {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, username, password);
    }

    public void addComment(String commenterUsername, String recipeOwnerUsername, int recipeId, String comment) throws SQLException{
        String query = "INSERT INTO Comments (commenterUsername, recipeOwnerUsername, recipeId, comment, timestamp) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, commenterUsername);
            preparedStatement.setString(2, recipeOwnerUsername);
            preparedStatement.setInt(3, recipeId);
            preparedStatement.setString(4, comment);
            preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            preparedStatement.executeUpdate();
            NotificationController.sendNotification("[" + commenterUsername + "] Added a comment to your recipe"); //notify when someone comments
        }
    }

    public List<Comment> getCommentsByRecipeId(Integer recipeId) throws SQLException{
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Comments WHERE recipeId = ?")){
             stmt.setInt(1, recipeId);
             ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Comment comment = mapResultSetToComment(resultSet);
                comments.add(comment);
            }
        }
        return comments;
    }

    public List<Comment> getCommentsByCommenter(String commenterUsername) throws SQLException{
        String query = "SELECT * FROM Comments WHERE commenterUsername = ?";
        List<Comment> comments = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, commenterUsername);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Comment comment = mapResultSetToComment(resultSet);
                comments.add(comment);
            }
        }
        return comments;
    }

    public List<Comment> getCommentsByRecipeOwner(String recipeOwnerUsername) throws SQLException{
        String query = "SELECT * FROM Comments WHERE recipeOwnerUsername = ?";
        List<Comment> comments = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, recipeOwnerUsername);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Comment comment = mapResultSetToComment(resultSet);
                comments.add(comment);
            }
        }
        return comments;

    }

    //delete by comment id
    public void deleteCommentByCommentId(int id) throws SQLException{
        String query = "Delete FROM Comments WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    //delete by recipe id
    public void deleteCommentByRecipeId(int recipeId) throws SQLException{
        String query = "Delete FROM Comments WHERE recipeId = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, recipeId);
            preparedStatement.executeUpdate();
        }
    }

    //delete users comments
    public void deleteCommentsByUser(String username) throws SQLException{
        String query = "DELETE FROM Comments WHERE commenterUsername = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        }
    }

    public void updateComment(int commentId, String newComment) throws SQLException{
        String query = "UPDATE Comments SET comment = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, newComment);
            preparedStatement.setInt(2, commentId);
            preparedStatement.executeUpdate();
        }
    }


    private Comment mapResultSetToComment(ResultSet resultSet) throws SQLException {
        Comment comment = new Comment();
        comment.setId(resultSet.getInt("id"));
        comment.setCommenter(resultSet.getString("commenterUsername"));
        comment.setRecipeOwner(resultSet.getString("recipeOwnerUsername"));
        comment.setRecipeId(resultSet.getInt("recipeId"));
        comment.setComment(resultSet.getString("comment"));
        comment.setCommentDate(resultSet.getTimestamp("timestamp"));
        return comment;
    }
}
