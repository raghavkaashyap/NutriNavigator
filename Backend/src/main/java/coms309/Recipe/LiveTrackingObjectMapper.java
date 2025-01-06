package coms309.Recipe;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LiveTrackingObjectMapper implements RowMapper<LiveTrackingObject> {
    @Override
    public LiveTrackingObject mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new LiveTrackingObject(rs.getInt("recipeId"), rs.getInt("pulls"), rs.getDate("date_logged"));
    }
}
