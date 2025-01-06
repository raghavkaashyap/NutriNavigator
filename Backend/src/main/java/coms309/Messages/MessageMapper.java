package coms309.Messages;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageMapper implements RowMapper<Message> {
    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Message(rs.getInt("id"), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5),
                rs.getLong(6), rs.getLong(7), rs.getString(8));
    }
}
