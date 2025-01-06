package coms309.Security;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EncryptionKeyMapper implements RowMapper<EncryptionKey> {
    @Override
    public EncryptionKey mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new EncryptionKey(rs.getString(2), rs.getString(3));
    }
}
