package coms309.Bookmarks;

import coms309.Security.JWTService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@OpenAPIDefinition(info = @Info(title = "Controller for User Recipe Bookmark handling", version = "1.0"))
@RestController
@RequestMapping("/bookmarks")
@Tag(name = "Bookmarks API", description = "API for managing user bookmarks on recipes")
public class BookmarksController {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private BookmarksService bookmarksService;

    @Operation(summary = "Add a bookmark", description = "Adds a new bookmark for a specific user and recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bookmark added successfully"),
            @ApiResponse(responseCode = "500", description = "Error adding bookmark", content = @Content)
    })
    @PostMapping
    public ResponseEntity<String> addBookmark(@RequestParam String username, @RequestParam int recipeId /*, @RequestHeader("Authorization") String encryptedToken*/){
        try{
            bookmarksService.addBookmark(username, recipeId);
            return ResponseEntity.ok("Added Bookmark");
        } catch (SQLException e){
            return ResponseEntity.status(500).body("Error adding bookmark: " + e.getMessage());
        }
    }

    @Operation(summary = "Get user bookmarks", description = "Retrieves all bookmarks for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bookmarks retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Error retrieving bookmarks", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Map<String,Object>>> getUserBookmarks(@RequestParam String username /*, @RequestHeader("Authorization") String encryptedToken*/){
        try{
            List<Map<String, Object>> bookmarks = bookmarksService.getUserBookmarks(username);
            return ResponseEntity.ok(bookmarks);
        } catch (SQLException e){
            return ResponseEntity.status(500).body(null);
        }
    }

    @Operation(summary = "Remove a bookmark", description = "Removes a bookmark for a specific user and recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bookmark deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Error deleting bookmark", content = @Content)
    })
    @DeleteMapping
    public ResponseEntity<String> removeBookmark(@RequestParam String username, @RequestParam int recipeId /*, @RequestHeader("Authorization") String encryptedToken*/){
        try {
            boolean isDeleted = bookmarksService.removeBookmark(username, recipeId);
            if(isDeleted){
                return ResponseEntity.ok("Bookmark deleted");
            } else {
                return ResponseEntity.status(404).body("Bookmark not found");
            }
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Error deleting bookmark");
        }

    }
}
