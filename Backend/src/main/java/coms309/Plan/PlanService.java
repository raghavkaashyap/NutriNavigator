package coms309.Plan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PlanService {
    @Autowired
    private PlanRepo repo;

    public boolean addPlanItem(int userId, int recipeId, LocalDate date) {
        return repo.insert(userId, recipeId, date);
    }

    public boolean removePlanItem(int userId, int recipeId, LocalDate date) {
        return repo.delete(userId, recipeId, date);
    }

    public List<Plan> listPlanItems(int userId) {
        return repo.list(userId);
    }
}
