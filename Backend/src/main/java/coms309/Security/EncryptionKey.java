package coms309.Security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import coms309.Users.User;
import jakarta.persistence.*;
import javax.crypto.*;

public class EncryptionKey {
    private String userId;
    private String encryptionKey;

    public EncryptionKey(String userId, String encryptionKey) {
        this.userId = userId;
        this.encryptionKey = encryptionKey;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String getUserId() {
        return userId;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }
}
