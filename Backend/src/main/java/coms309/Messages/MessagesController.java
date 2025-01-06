package coms309.Messages;

import coms309.APIResponse;
import coms309.Security.JWTService;
import coms309.Users.*;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@OpenAPIDefinition(info = @Info(title = "Controller for CRUDLing Admin Messages", version = "1.0"))
@RestController
public class MessagesController {
    @Autowired
    JWTService jwtService;

    @Autowired
    UserService userService;

    @Autowired
    MessagesService messagesService;

    @Operation(summary = "Submit Message", description = "Allows a moderator or user to submit a new message")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The moderator action is not permitted because of a bad token")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    @PostMapping("/messages/submit")
    public ResponseEntity<String> submitMessage(@RequestParam String username, @RequestBody Map<String, Object> json, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>("Token is missing", HttpStatus.BAD_REQUEST);
        }
        if (!jwtService.checkUserIdentity(username, token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        User submittingUser = userService.findUser(username);
        String subject = "";
        String message = "";

        if (json.containsKey("subject")) {
            subject = (String) json.get("subject");
        }

        if (json.containsKey("message")) {
            message = (String) json.get("message");
        }

        if (messagesService.submitNewMessage(submittingUser, subject, message)) {
            return new ResponseEntity<>("Message successfully submitted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Message could not be submitted", HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "List", description = "Allows a moderator or user to list all of their previously submitted messages")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The moderator action is not permitted because of a bad token")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    @GetMapping("/messages/list")
    public ResponseEntity<APIResponse<List<Message>>> listMessages(@RequestParam String username, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>(new APIResponse<>(null, "Token is missing"), HttpStatus.BAD_REQUEST);
        }
        if (!jwtService.checkUserIdentity(username, token)) {
            return new ResponseEntity<>(new APIResponse<>(null, "Token did not match"), HttpStatus.FORBIDDEN);
        }
        User queryingUser = userService.findUser(username);
        if (queryingUser == null) {
            return new ResponseEntity<>(new APIResponse<>(null, "No messages found for this user"), HttpStatus.NOT_FOUND);
        }
        List<Message> allMessages = messagesService.listUserMessages(queryingUser);
        if (allMessages != null) {
            return new ResponseEntity<>(new APIResponse<>(allMessages, "Found messages for this user"), HttpStatus.FOUND);
        }
        return new ResponseEntity<>(new APIResponse<>(null, "No messages found for this user"), HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Search Messages", description = "Allows a moderator or user to search for a specific message they previously submitted")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The moderator action is not permitted because of a bad token")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    //Search for messages that the user owns
    @GetMapping("/messages/search")
    public ResponseEntity<APIResponse<Message>> searchMessage(@RequestParam String username, @RequestParam int messageId, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>(new APIResponse<>(null, "Token is missing"), HttpStatus.BAD_REQUEST);
        }
        Message searchedMessage = messagesService.searchMessage(messageId);
        if (searchedMessage == null) {
            return new ResponseEntity<>(new APIResponse<>(null, "Message not found"), HttpStatus.NOT_FOUND);
        }
        String submittedUserName = searchedMessage.getUserSubmitted();
        if (!jwtService.checkUserIdentity(submittedUserName, token)) {
            return new ResponseEntity<>(new APIResponse<>(null, "This user did not create this message"), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new APIResponse<>(searchedMessage, "Token is missing"), HttpStatus.FOUND);
    }

    @Operation(summary = "Update Message", description = "Allows a moderator or user to make alterations to a previously submitted message")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The moderator action is not permitted because of a bad token")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    @PostMapping("/messages/update")
    public ResponseEntity<String> updateMessage(@RequestParam String username, @RequestParam int messageId, @RequestBody Map<String, Object> json, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>("Token is missing", HttpStatus.BAD_REQUEST);
        }
        Message searchedMessage = messagesService.searchMessage(messageId);
        if (searchedMessage == null) {
            return new ResponseEntity<>("Message not found", HttpStatus.NOT_FOUND);
        }
        String submittedUserName = searchedMessage.getUserSubmitted();
        if (!jwtService.checkUserIdentity(submittedUserName, token)) {
            return new ResponseEntity<>("This user did not create this message", HttpStatus.FORBIDDEN);
        }
        String subject = "";
        String message = "";

        if (json.containsKey("subject")) {
            subject = (String) json.get("subject");
        }
        if (json.containsKey("message")) {
            message = (String) json.get("message");
        }

        if (messagesService.updateMessage(messageId, subject, message)) {
            return new ResponseEntity<>("Message successfully updated", HttpStatus.OK);
        }
        return new ResponseEntity<>("Message could not be updated", HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Delete Message", description = "Allows a moderator or user to delete a previously submitted message")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The moderator action is not permitted because of a bad token")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    @DeleteMapping("/messages/delete")
    public ResponseEntity<String> deleteMessage(@RequestParam String username, @RequestParam int messageId, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>("Token is missing", HttpStatus.BAD_REQUEST);
        }
        Message searchedMessage = messagesService.searchMessage(messageId);
        if (searchedMessage == null) {
            return new ResponseEntity<>("Message not found", HttpStatus.NOT_FOUND);
        }
        String submittedUserName = searchedMessage.getUserSubmitted();
        if (!jwtService.checkUserIdentity(submittedUserName, token)) {
            return new ResponseEntity<>("This user did not create this message", HttpStatus.FORBIDDEN);
        }
        if (messagesService.deleteMessage(messageId)) {
            return new ResponseEntity<>("Message successfully deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Message could not be deleted", HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Admin Message Search", description = "Allows an admin to search any message")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The moderator action is not permitted because of a bad token")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    //Allows admins to search all messages
    @GetMapping("/messages/admin/search")
    public ResponseEntity<APIResponse<Message>> adminSearchMessage(@RequestParam String username, @RequestParam int messageId, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>(new APIResponse<>(null, "Token is missing"), HttpStatus.BAD_REQUEST);
        }
        Message searchedMessage = messagesService.searchMessage(messageId);
        if (searchedMessage == null) {
            return new ResponseEntity<>(new APIResponse<>(null, "Message not found"), HttpStatus.NOT_FOUND);
        }
        String submittedUserName = searchedMessage.getUserSubmitted();
        if (!jwtService.checkUserIsType(username, UserType.ADMIN, token)) {
            return new ResponseEntity<>(new APIResponse<>(null, "Admin did not authenticate, action forbidden"), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new APIResponse<>(searchedMessage, "Message found"), HttpStatus.FOUND);
    }

    @Operation(summary = "Admin List All Messages", description = "Allows an admin to list all submitted messages")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The moderator action is not permitted because of a bad token")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    @GetMapping("/messages/admin/listall")
    public ResponseEntity<APIResponse<List<Message>>> adminListAllMessages(@RequestParam String username, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>(new APIResponse<>(null, "Token is missing"), HttpStatus.BAD_REQUEST);
        }
        if (!jwtService.checkUserIsType(username, UserType.ADMIN, token)) {
            return new ResponseEntity<>(new APIResponse<>(null, "Admin did not authenticate, action forbidden"), HttpStatus.FORBIDDEN);
        }
        List<Message> allMessages = messagesService.listAll();
        if (allMessages != null) {
            return new ResponseEntity<>(new APIResponse<>(allMessages, "Messages found"), HttpStatus.FOUND);
        }
        return new ResponseEntity<>(new APIResponse<>(null, "Messages not found"), HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Admin List Unresolved", description = "Allows an admin to list all unresolved messages")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The moderator action is not permitted because of a bad token")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    @GetMapping("/messages/admin/listunresolved")
    public ResponseEntity<APIResponse<List<Message>>> adminListUnresolvedMessages(@RequestParam String username, @RequestHeader("Authorization") String token) {
        if (token == null) {
            return new ResponseEntity<>(new APIResponse<>(null, "Token is missing"), HttpStatus.BAD_REQUEST);
        }
        if (!jwtService.checkUserIsType(username, UserType.ADMIN, token)) {
            return new ResponseEntity<>(new APIResponse<>(null, "Admin did not authenticate, action forbidden"), HttpStatus.FORBIDDEN);
        }
        List<Message> allMessages = messagesService.listUnresolved();
        if (allMessages != null) {
            return new ResponseEntity<>(new APIResponse<>(allMessages, "Messages found"), HttpStatus.FOUND);
        }
        return new ResponseEntity<>(new APIResponse<>(null, "Messages not found"), HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Admin List Resolved", description = "Allows an admin to list all resolved messages")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The moderator action is not permitted because of a bad token")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    @GetMapping("/messages/admin/listresolved")
    public ResponseEntity<APIResponse<List<Message>>> adminListResolvedMessages(@RequestParam String username, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>(new APIResponse<>(null, "Token is missing"), HttpStatus.BAD_REQUEST);
        }
        if (!jwtService.checkUserIsType(username, UserType.ADMIN, token)) {
            return new ResponseEntity<>(new APIResponse<>(null, "Admin did not authenticate, action forbidden"), HttpStatus.FORBIDDEN);
        }
        List<Message> allMessages = messagesService.listResolved();
        if (allMessages != null) {
            return new ResponseEntity<>(new APIResponse<>(allMessages, "Messages found"), HttpStatus.FOUND);
        }
        return new ResponseEntity<>(new APIResponse<>(null, "Messages not found"), HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Admin Delete Message", description = "Allows an admin to delete any message")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The moderator action is not permitted because of a bad token")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    @DeleteMapping("/messages/admin/delete")
    public ResponseEntity<String> adminDeleteMessage(@RequestParam String username, @RequestParam int messageId, @RequestHeader("Authorization") String token) {
        if (token == null) {
            return new ResponseEntity<>("Token is missing", HttpStatus.BAD_REQUEST);
        }
        if (!jwtService.checkUserIsType(username, UserType.ADMIN, token)) {
            return new ResponseEntity<>("Admin did not authenticate, action forbidden", HttpStatus.FORBIDDEN);
        }
        Message searchedMessages = messagesService.searchMessage(messageId);
        if (searchedMessages == null) {
            return new ResponseEntity<>("Message not found", HttpStatus.NOT_FOUND);
        }
        if (messagesService.deleteMessage(messageId)) {
            return new ResponseEntity<>("Message successfully deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Message could not be deleted", HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Admin Mark Message Resolved", description = "Allows an admin to mark an unresolved message as resolved")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The moderator action is not permitted because of a bad token")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    @PostMapping("/messages/admin/resolve")
    public ResponseEntity<String> adminResolveMessage(@RequestParam String username, @RequestParam int messageId, @RequestHeader("Authorization") String token) {
        if (token == null) {
            return new ResponseEntity<>("Token is missing", HttpStatus.BAD_REQUEST);
        }
        if (!jwtService.checkUserIsType(username, UserType.ADMIN, token)) {
            return new ResponseEntity<>("Admin did not authenticate, action forbidden", HttpStatus.FORBIDDEN);
        }
        Message searchedMessages = messagesService.searchMessage(messageId);
        User resolvingUser = userService.findUser(username);
        if (searchedMessages == null || resolvingUser == null) {
            return new ResponseEntity<>("Message not found", HttpStatus.NOT_FOUND);
        }
        if (messagesService.resolveMessage(messageId, resolvingUser)) {
            return new ResponseEntity<>("Message resolved", HttpStatus.OK);
        }
        return new ResponseEntity<>("Message could not be resolved", HttpStatus.BAD_REQUEST);
    }
}
