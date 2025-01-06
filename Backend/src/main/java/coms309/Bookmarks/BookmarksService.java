package coms309.Bookmarks;

import coms309.Notifications.NotificationController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookmarksService {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, username, password);
    }

    public void addBookmark(String username, int recipeId) throws SQLException{
        String query = "INSERT INTO Bookmarks (username, recipeId) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, username);
            statement.setInt(2, recipeId);
            statement.executeUpdate();
            NotificationController.sendNotification("[" + username + "] You added a bookmark"); //notify when users bookmark a recipe
        }
    }

    public List<Map<String, Object>> getUserBookmarks(String username) throws SQLException{
        List<Map<String, Object>> bookmarks = new ArrayList<>();
        String query = "SELECT b.recipeId, r.recipe, r.calories, r.ingredients, r.date_added " + "FROM Bookmarks b " +
                "JOIN Recipes r ON b.recipeId = r.recipeId " + "WHERE b.username = ?";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Map<String, Object> bookmark = new HashMap<>();
                bookmark.put("recipeId", resultSet.getInt("recipeId"));
                bookmark.put("recipe", resultSet.getString("recipe"));
                bookmark.put("calories", resultSet.getInt("calories"));
                bookmark.put("ingredients", resultSet.getString("ingredients"));
                bookmark.put("dateAdded", resultSet.getDate("date_added"));
                bookmarks.add(bookmark);
            }
        }
        return bookmarks;
    }

    public boolean removeBookmark(String username, int recipeId) throws SQLException {
        String query = "DELETE FROM Bookmarks WHERE username = ? AND recipeId = ?";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, username);
            statement.setInt(2, recipeId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected>0;
        }
    }

}
