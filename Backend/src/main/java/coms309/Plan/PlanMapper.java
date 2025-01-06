package coms309.Plan;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlanMapper implements RowMapper<Plan> {
    @Override
    public Plan mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Plan(rs.getInt("userId"), rs.getInt("recipeId"), rs.getDate("plannedDate").toLocalDate());
    }
}
