package coms309.Users;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("id"), rs.getString("username"), rs.getString("passwordHash"), rs.getInt("usertype"), rs.getString("email"),
                rs.getLong("ban_time"), rs.getLong("date_created"), rs.getLong("last_login"));
    }
}
