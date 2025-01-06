package coms309.Test;

import coms309.Security.JWTService;
import coms309.Users.UserType;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@OpenAPIDefinition(info = @Info(title = "Test Controller", version = "1.0"))
@RestController
public class TestController {
    @Autowired
    JWTService jwtService;

    @Operation(summary = "Test GET", description = "A GET endpoint that always returns 200")
    @ApiResponse(responseCode = "OK", description = "Ping")
    //Test endpoint for get
    @GetMapping("/test/get")
    public String testEndpoint() {
        return "200";
    }

    //Test endpoint for post
    @Operation(summary = "Test POST", description = "A POST endpoint that always returns 200")
    @ApiResponse(responseCode = "OK", description = "Ping")
    @PostMapping("/test/post")
    public ResponseEntity<String> testPostEndpoint() {
        return ResponseEntity.ok("200: Request successful");
    }

    @Operation(summary = "Test For User Token", description = "An endpoint that tests whether a user token authenticates")
    @ApiResponse(responseCode = "OK", description = "User action is authorized or not authorized")
    //Test endoint for user token
    @PostMapping("/test/user")
    public String testUserEndpoint(@RequestParam String username, @RequestHeader("Authorization") String token) {
        if (jwtService.checkUserIsType(username, UserType.USER, token)) {
            return "User action authorized";
        } else {
            return "User action not authorized";
        }
    }

    @Operation(summary = "Test For Moderator Token", description = "An endpoint that tests whether a user token authenticates")
    @ApiResponse(responseCode = "OK", description = "User action is authorized or not authorized")
    //Test endpoint for moderator token
    @PostMapping("/test/moderator")
    public String testModeratorEndpoint(@RequestParam String username, @RequestHeader("Authorization") String token) {
        if (jwtService.checkUserIsType(username, UserType.MODERATOR, token)) {
            return "Moderator action authorized";
        } else {
            return "Moderator action not authorized";
        }
    }

    @Operation(summary = "Test For Admin Token", description = "An endpoint that tests whether an admin token authenticates")
    @ApiResponse(responseCode = "OK", description = "User action is authorized or not authorized")
    //Test endpoint for admin token
    @PostMapping("/test/admin")
    public String testAdminEndpoint(@RequestParam String username, @RequestHeader("Authorization") String token) {
        if (jwtService.checkUserIsType(username, UserType.ADMIN, token)) {
            return "Admin action authorized";
        } else {
            return "Admin action not authorized";
        }
    }
}
