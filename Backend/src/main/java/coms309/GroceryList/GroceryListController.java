package coms309.GroceryList;

import coms309.APIResponse;
import coms309.Security.JWTService;
import coms309.Users.UserService;
import org.apiguardian.api.API;
import org.springframework.beans.factory.annotation.Autowired;
import coms309.Users.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GroceryListController {
    @Autowired
    private GroceryListService groceryListService;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    @GetMapping("/grocerylist")
    public APIResponse<List<String>> generateGroceryList(@RequestParam String username, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new APIResponse<>(null, "Token is missing");
        }
        if (!jwtService.checkUserIdentity(username, token)) {
            return new APIResponse<>(null, "Action not permitted");
        }

        User submittingUser = userService.findUser(username);

        List<String> groceryList = groceryListService.generateList(submittingUser.getId());

        if (groceryList != null) {
            return new APIResponse<>(groceryList, "Success");
        }
        return new APIResponse<>(null, "Grocery list could not be generated");
    }
}
