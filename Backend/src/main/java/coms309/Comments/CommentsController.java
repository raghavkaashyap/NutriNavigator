package coms309.Comments;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@OpenAPIDefinition(info = @Info(title = "Controller for User comment handling", version = "1.0"))
@RestController
@RequestMapping("/comments")
@Tag(name = "Comments API", description = "Endpoints for managing comments made by users under recipes")
public class CommentsController {

    @Autowired
    CommentsService commentsService;

    @Operation(summary = "Add a new comment", description = "Adds a new comment to a recipe")
    @ApiResponse(responseCode = "200", description = "Comment added successfully")
    @ApiResponse(responseCode = "500", description = "Error adding comment", content = @Content)
    @PostMapping
    public ResponseEntity<String> addComment(@RequestParam String commenterUsername, @RequestParam String recipeOwnerUsername, @RequestParam int recipeId, @Parameter(description = "Comment content", required = true) @RequestBody String comment){
        try{
            commentsService.addComment(commenterUsername, recipeOwnerUsername, recipeId, comment);
            return ResponseEntity.ok("Comment added successfully");
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Error adding comment: " + e.getMessage());
        }
    }

    @Operation(summary = "Get comments by recipe ID", description = "Retrieves all comments made under a particular recipe")
    @ApiResponse(responseCode = "200", description = "List of comments", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class)))
    @ApiResponse(responseCode = "500", description = "Error fetching comments", content = @Content)
    @GetMapping("/recipeId")
    public ResponseEntity<List<Comment>> getCommentsByRecipeId(@RequestParam int recipeId){
        try{
            List<Comment> comments = commentsService.getCommentsByRecipeId(recipeId);
            return ResponseEntity.ok(comments);
        } catch (SQLException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @Operation(summary = "Get comments by commenter username", description = "Retrieves all comments made by a user")
    @ApiResponse(responseCode = "200", description = "List of comments", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class)))
    @ApiResponse(responseCode = "500", description = "Error fetching comments", content = @Content)
    @GetMapping("/user")
    public ResponseEntity<List<Comment>> getCommentsByCommenter(@RequestParam String commenterUsername){
        try{
            List<Comment> comments = commentsService.getCommentsByCommenter(commenterUsername);
            return ResponseEntity.ok(comments);
        } catch (SQLException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @Operation(summary = "Get comments by a recipe owner's username", description = "Retrieves all comments received by a user")
    @ApiResponse(responseCode = "200", description = "List of comments", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class)))
    @ApiResponse(responseCode = "500", description = "Error fetching comments", content = @Content)
    @GetMapping("/owner")
    public ResponseEntity<List<Comment>> getCommentsByRecipeOwner(@RequestParam String recipeOwnerUsername){
        try{
            List<Comment> comments = commentsService.getCommentsByRecipeOwner(recipeOwnerUsername);
            return ResponseEntity.ok(comments);
        } catch (SQLException e){
            return ResponseEntity.status(500).body(null);
        }
    }

    @Operation(summary = "Delete a comment by comment ID", description = "Deletes a comment by its id")
    @ApiResponse(responseCode = "200", description = "Comment deleted successfully")
    @ApiResponse(responseCode = "500", description = "Error deleting comment", content = @Content)
    @DeleteMapping("/commentId")
    public ResponseEntity<String> deleteCommentByCommentId(@RequestParam int commentId){
        try{
            commentsService.deleteCommentByCommentId(commentId);
            return ResponseEntity.ok("Comment deleted successfully.");
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Error deleting comment: " + e.getMessage());
        }
    }

    @Operation(summary = "Delete all comments by a particular recipe ID", description = "Deletes all comments made under a particular recipe")
    @ApiResponse(responseCode = "200", description = "Comments deleted successfully")
    @ApiResponse(responseCode = "500", description = "Error deleting comments", content = @Content)
    @DeleteMapping("/recipeId")
    public ResponseEntity<String> deleteCommentByRecipeId(@RequestParam int recipeId){
        try{
            commentsService.deleteCommentsByRecipeId(recipeId);
            return ResponseEntity.ok("Comments deleted successfully.");
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Error deleting comment: " + e.getMessage());
        }
    }

    @Operation(summary = "Delete comments by commenter's username", description = "Deletes all comments made by a particular user")
    @ApiResponse(responseCode = "200", description = "Comments deleted successfully")
    @ApiResponse(responseCode = "500", description = "Error deleting comments", content = @Content)
    @DeleteMapping("/username")
    public ResponseEntity<String> deleteCommentsByUser(@RequestParam String username){
        try{
            commentsService.deleteCommentsByUser(username);
            return ResponseEntity.ok("Comments deleted successfully.");
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Error deleting comment: " + e.getMessage());
        }
    }

    @Operation(summary = "Update a comment", description = "Allows a user to update their comment")
    @ApiResponse(responseCode = "200", description = "Comment updated successfully")
    @ApiResponse(responseCode = "500", description = "Could not update comment", content = @Content)
    @PutMapping
    public ResponseEntity<String> updateComment(@RequestParam int commentId, @RequestParam String newMessage){
        try{
            commentsService.updateComment(commentId, newMessage);
            return ResponseEntity.ok("Comment updated.");
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Could not update comment: " + e.getMessage());
        }
    }
}
