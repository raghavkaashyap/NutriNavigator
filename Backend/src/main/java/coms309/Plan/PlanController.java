package coms309.Plan;

import coms309.APIResponse;
import coms309.Recipe.RecipeService;
import coms309.Security.JWTService;
import coms309.Users.User;
import coms309.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@RestController
public class PlanController {
    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private PlanService planService;

    @PostMapping("/plan/create")
    public ResponseEntity<String> createPlan(@RequestParam String username, @RequestParam int recipeid, @RequestParam LocalDate date, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>("Token is missing", HttpStatus.BAD_REQUEST);
        }
        if (!jwtService.checkUserIdentity(username, token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        User submittingUser = userService.findUser(username);
        int goodRecipeId = 0;
        try {
            if (recipeService.getRecipeById(recipeid) != null) {
                goodRecipeId = recipeid;
            }
        } catch (SQLException ex) {
            System.out.println("Error while querying SQL in createPlan(): " + ex.getMessage());
            return new ResponseEntity<>("Error while querying SQL", HttpStatus.BAD_REQUEST);
        }

        LocalDate goodDate = date;

        try {
            planService.addPlanItem(submittingUser.getId(), goodRecipeId, goodDate);
            return new ResponseEntity<>("Successfully added plan item", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Could not add plan item", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/plan/delete")
    public ResponseEntity<String> deletePlan(@RequestParam String username, @RequestParam int recipeid, @RequestParam LocalDate date, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>("Token is missing", HttpStatus.BAD_REQUEST);
        }
        if (!jwtService.checkUserIdentity(username, token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        User submittingUser = userService.findUser(username);

        try {
            planService.removePlanItem(submittingUser.getId(), recipeid, date);
        } catch (Exception ex) {
            System.out.println("Error while deleting from plan: " + ex.getMessage());
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Item successfully deleted", HttpStatus.OK);
    }

    @GetMapping("/plan/list")
    public APIResponse<List<Plan>> listPlans(@RequestParam String username, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new APIResponse<>(null, "Token is missing");
        }
        if (!jwtService.checkUserIdentity(username, token)) {
            return new APIResponse<>(null, "Action not permitted");
        }

        User submittingUser = userService.findUser(username);

        List<Plan> planList = planService.listPlanItems(submittingUser.getId());

        if (planList == null) {
            return new APIResponse<>(null, "Plan could not be generated");
        }
        return new APIResponse<>(planList, "Success");
    }
}
