package coms309.Administration;

import coms309.APIResponse;
import coms309.Security.JWTService;
import coms309.Users.User;
import coms309.Users.UserService;
import coms309.Users.UserType;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@OpenAPIDefinition(info = @Info(title = "Controller for Admin level interactions", version = "1.0"))
@RestController
public class AdminController {
    @Autowired
    UserService userService;

    @Autowired
    JWTService jwtService;

    @Operation(summary = "User Promotion", description = "Allows an admin to promote a specified user to a moderator")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The admin action is not permitted because of a bad token, or the requester is not an admin")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    @PostMapping("/admin/promote")
    public ResponseEntity<String> promoteUserToMod(@RequestParam String username, @RequestParam String targetuser, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>("Token not found", HttpStatus.BAD_REQUEST);
        }
        if (!jwtService.checkUserIsType(username, UserType.ADMIN, token)) {
            return new ResponseEntity<>("Admin action not authorized", HttpStatus.FORBIDDEN);
        }
        if (userService.promoteUser(targetuser)) {
            return new ResponseEntity<>("User promoted to moderator", HttpStatus.OK);
        }
        return new ResponseEntity<>("Could not complete action", HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "User Demotion", description = "Allows an admin to demote a specified moderator back to a user")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The admin action is not permitted because of a bad token, or the requester is not an admin")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    @PostMapping("/admin/demote")
    public ResponseEntity<String> demoteModToUser(@RequestParam String username, @RequestParam String targetuser, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>("Token not found", HttpStatus.BAD_REQUEST);
        }
        if (!jwtService.checkUserIsType(username, UserType.ADMIN, token)) {
            return new ResponseEntity<>("Admin action not authorized", HttpStatus.FORBIDDEN);
        }
        if (userService.demoteModerator(targetuser)) {
            return new ResponseEntity<>("Moderator demoted to user", HttpStatus.OK);
        }
        return new ResponseEntity<>("Could not complete action", HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "User Deletion", description = "Allows an admin to delete a specified user")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The admin action is not permitted because of a bad token, or the requester is not an admin")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    @PostMapping("/admin/deleteuser")
    public ResponseEntity<String> deleteUser(@RequestParam String username, @RequestParam String targetuser, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>("Token not found", HttpStatus.BAD_REQUEST);
        }
        if (!jwtService.checkUserIsType(username, UserType.ADMIN, token)) {
            return new ResponseEntity<>("Admin action not authorized", HttpStatus.FORBIDDEN);
        }
        if (userService.delete(targetuser)) {
            return new ResponseEntity<>("Deleted user", HttpStatus.OK);
        }
        return new ResponseEntity<>("Could not complete action", HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "User Ban", description = "Allows an admin to ban a specified user")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The admin action is not permitted because of a bad token, or the requester is not an admin")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    @PostMapping("/admin/banuser")
    public ResponseEntity<String> banUser(@RequestParam String username, @RequestParam String targetuser, @RequestParam int bantime, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>("Token not found", HttpStatus.BAD_REQUEST);
        }
        if (!jwtService.checkUserIsType(username, UserType.ADMIN, token)) {
            return new ResponseEntity<>("Admin action not authorized", HttpStatus.FORBIDDEN);
        }
        if (userService.setUserBanTime(targetuser, bantime)) {
            return new ResponseEntity<>("Set user ban time to " + bantime, HttpStatus.OK);
        }
        return new ResponseEntity<>("Could not complete action", HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "User Unban", description = "Allows an admin to unban a specified user")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The admin action is not permitted because of a bad token, or the requester is not an admin")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    @PostMapping("/admin/unbanuser")
    public ResponseEntity<String> unbanUser(@RequestParam String username, @RequestParam String targetuser, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>("Token not found", HttpStatus.BAD_REQUEST);
        }
        if (!jwtService.checkUserIsType(username, UserType.ADMIN, token)) {
            return new ResponseEntity<>("Admin action not authorized", HttpStatus.FORBIDDEN);
        }
        if (userService.setUserBanTime(targetuser, 0)) {
            return new ResponseEntity<>("Set user ban time to 0", HttpStatus.OK);
        }
        return new ResponseEntity<>("Could not complete action", HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Get User", description = "Allows an admin to retrieve information about a specified user")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The admin action is not permitted because of a bad token, or the requester is not an admin")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    @GetMapping("/admin/getuser")
    public ResponseEntity<APIResponse<User>> getUserInfo(@RequestParam String username, @RequestParam String targetuser, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>(new APIResponse<>(null, "Token not found"), HttpStatus.BAD_REQUEST);
        }
        if (!jwtService.checkUserIsType(username, UserType.ADMIN, token)) {
            return new ResponseEntity<>(new APIResponse<>(null, "Admin action not authorized"), HttpStatus.FORBIDDEN);
        }
        User foundUser = userService.findUser(targetuser);
        if (foundUser != null) {
            return new ResponseEntity<>(new APIResponse<>(foundUser, "User found"), HttpStatus.FOUND);
        }
        return new ResponseEntity<>(new APIResponse<>(null, "User not found"), HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "List Recent Users", description = "Allows an admin to list the most recently logged in users")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The admin action is not permitted because of a bad token, or the requester is not an admin")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    @GetMapping("/admin/listrecentusers")
    public ResponseEntity<APIResponse<List<User>>> listRecentUsers(@RequestParam String username, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>(new APIResponse<>(null, "Token not found"), HttpStatus.BAD_REQUEST);
        }
        if (!jwtService.checkUserIsType(username, UserType.ADMIN, token)) {
            return new ResponseEntity<>(new APIResponse<>(null, "Admin action not authorized"), HttpStatus.FORBIDDEN);
        }
        List<User> userList = userService.getRecentUsers();
        if (userList != null) {
            return new ResponseEntity<>(new APIResponse<>(userList, "Users found"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new APIResponse<>(null, "Users not found"), HttpStatus.NOT_FOUND);
    }
}