package coms309.Social;

import coms309.APIResponse;
import coms309.Security.JWTService;
import coms309.Users.User;
import coms309.Users.UserLimited;
import coms309.Users.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

@OpenAPIDefinition(info = @Info(title = "Followers Social Controller", version = "1.0"))
@RestController
public class SocialController {
    @Autowired
    private SocialService socialService;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    @Operation(summary = "Follow", description = "Allows a user to follow another user")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "The user already exists, or the request could otherwise not be completed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The token was bad or otherwise did not authenticate")
    @ApiResponse(responseCode = "NOT_FOUND", description = "The user that the requester wants to follow does not exist")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    //Create
    @PostMapping("/social/follow")
    public ResponseEntity<String> followUser(@RequestParam String username, @RequestParam String usertofollow, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>("Token is missing", HttpStatus.BAD_REQUEST);
        }

        if (!jwtService.checkUserIdentity(username, token)) {
            return new ResponseEntity<>("Action not permitted", HttpStatus.FORBIDDEN);
        }

        if (!userService.userExists(usertofollow)) {
            return new ResponseEntity<>("User that was requested to follow does not exist", HttpStatus.NOT_FOUND);
        }
        try {
            socialService.addFollowing(userService.findUser(username), userService.findUser(usertofollow));
            return new ResponseEntity<>("User successfully followed", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("User could not be followed: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Unfollow", description = "Allows a user to unfollow another user")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "The user already exists, or the request could otherwise not be completed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The token was bad or otherwise did not authenticate")
    @ApiResponse(responseCode = "NOT_FOUND", description = "The user that the requester wants to unfollow does not exist")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    //Delete
    @DeleteMapping("/social/unfollow")
    public ResponseEntity<String> unfollowUser(@RequestParam String username, @RequestParam String usertounfollow, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>("Token is missing", HttpStatus.BAD_REQUEST);
        }

        if (!jwtService.checkUserIdentity(username, token)) {
            return new ResponseEntity<>("Action not permitted", HttpStatus.FORBIDDEN);
        }

        if (!userService.userExists(usertounfollow)) {
            return new ResponseEntity<>("User that was requested to unfollow does not exist", HttpStatus.NOT_FOUND);
        }
        try {
            socialService.removeFollowing(userService.findUser(username), userService.findUser(usertounfollow));
            return new ResponseEntity<>("User successfully unfollowed", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("User could not be unfollowed: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "List All Following", description = "Allows a user to list all of the users that they follow")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "The user already exists, or the request could otherwise not be completed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The token was bad or otherwise did not authenticate")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    //Get list of all users that the querying user is following
    @GetMapping("/social/listfollowed")
    public APIResponse<List<UserLimited>> listFollowed(@RequestParam String username, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new APIResponse<>(null, "Token is missing");
        }

        if (!jwtService.checkUserIdentity(username, token)) {
            return new APIResponse<>(null, "Action not permitted");
        }

        List<User> userList = userService.getAllFollowed(username);
        List<UserLimited> redactedUserList = new ArrayList<>();

        for (User u : userList) {
            redactedUserList.add(new UserLimited(u.getId(), u.getUsername(), u.getUserType()));
        }

        if (userList != null) {
            return new APIResponse<>(redactedUserList, "List successfully generated");
        } else {
            return new APIResponse<>(null, "List could not be generated");
        }
    }

    @Operation(summary = "List All Followers", description = "Allows a user to list all of the users that follow them")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "The user already exists, or the request could otherwise not be completed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The token was bad or otherwise did not authenticate")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    //Get list of all users that follow the querying user
    @GetMapping("/social/listfollowers")
    public APIResponse<List<UserLimited>> listFollowers(@RequestParam String username, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new APIResponse<>(null, "Token is missing");
        }

        if (!jwtService.checkUserIdentity(username, token)) {
            return new APIResponse<>(null, "Action not permitted");
        }

        List<User> userList = userService.getAllFollowers(username);
        List<UserLimited> redactedUserList = new ArrayList<>();

        for (User u : userList) {
            redactedUserList.add(new UserLimited(u.getId(), u.getUsername(), u.getUserType()));
        }

        if (userList != null) {
            return new APIResponse<>(redactedUserList, "List successfully generated");
        } else {
            return new APIResponse<>(null, "List could not be generated");
        }
    }
}
