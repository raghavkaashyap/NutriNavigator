package coms309.Users;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepo;

    public boolean createUser(String username, String passwordHash, UserType type, String email) {
        if (userRepo.exists(username)) {
            return false;
        }
        long currentTime = System.currentTimeMillis() / 1000L;
        return userRepo.insert(new User(username, passwordHash, type, email, 0, currentTime, currentTime));
    }

    public boolean promoteUser(String username) {
        if (!userRepo.exists(username)) {
            return false;
        }
        return userRepo.updateUserType(username, UserType.MODERATOR);
    }

    public boolean demoteModerator(String username) {
        if (!userRepo.exists(username)) {
            return false;
        }
        return userRepo.updateUserType(username, UserType.USER);
    }

    public boolean delete(String username) {
        if (!userRepo.exists(username)) {
            return false;
        }
        userRepo.deleteEncryptionKey(username);
//        userRepo.deleteActiveToken(username);
        return userRepo.delete(username);
    }

    public boolean userExists(String username) {
        return userRepo.exists(username);
    }

    public User findUser(String username) {
        return userRepo.find(username);
    }

    //Bantime is a future UNIX time that the ban lasts until
    public boolean setUserBanTime(String username, int bantime) {
        return userRepo.updateBanTime(username, bantime);
    }

    public boolean setUserEmail(String username, String email) {
        return userRepo.updateEmail(username, email);
    }

    public boolean setUserPassword(String username, String password) {
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        return userRepo.updatePasswordHash(username, passwordHash);
    }

    public List<User> getRecentUsers() {
        return userRepo.listRecent();
    }

    public boolean setLastLoginToNow(String username) {
        long currentTime = System.currentTimeMillis() / 1000L;
        return userRepo.updateLastLogin(username, currentTime);
    }

    public List<User> getAllFollowed(String username) {
        int userId = findUser(username).getId();
        return getAllFollowed(userId);
    }

    public List<User> getAllFollowed(int userId) {
        return userRepo.listFollowed(userId);
    }

    public List<User> getAllFollowers(String username) {
        int userId = findUser(username).getId();
        return userRepo.listFollowers(userId);
    }

    public List<User> getAllFollowers(int userId) {
        return userRepo.listFollowers(userId);
    }
}
