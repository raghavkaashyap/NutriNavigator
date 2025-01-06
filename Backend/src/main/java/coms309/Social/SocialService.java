package coms309.Social;

import coms309.Users.UserRepository;
import coms309.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocialService {
    @Autowired
    private SocialRepository socialRepo;

    @Autowired
    private UserRepository userRepo;

    public void addFollowing(User user, User following) {
        socialRepo.insert(user.getId(), following.getId());
    }

    public void removeFollowing(User user, User following) {
        socialRepo.remove(user.getId(), following.getId());
    }
}
