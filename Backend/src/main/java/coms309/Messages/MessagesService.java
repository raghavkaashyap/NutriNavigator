package coms309.Messages;

import coms309.Security.JWTService;
import coms309.Users.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class MessagesService {
    @Autowired
    MessagesRepository messageRepo;

    public boolean submitNewMessage(User user, String subject, String message) {
        if (subject.length() > 254 || message.length() > 254) {
            return false;
        }
        long currentTime = System.currentTimeMillis() / 1000L;
        Message newMessage = new Message(0, user.getUsername(), subject, message, currentTime, 0, "NULL");
        return messageRepo.insert(newMessage);
        
    }

    public List<Message> listUserMessages(User user) {
        List<Message> messageList = messageRepo.listMessages();
        if (messageList == null) {
            return null;
        }
        Iterator<Message> iter = messageList.iterator();
        while (iter.hasNext()) {
            Message m = iter.next();
            if (!m.getUserSubmitted().equals(user.getUsername())) {
                iter.remove();
            }
        }
        if (messageList.isEmpty()) {
            return null;
        }
        return messageList;
    }

    public boolean updateMessage(int messageId, String subject, String message) {
        if (subject.length() > 254 || message.length() > 254) {
            return false;
        }
        return messageRepo.updateSubjectAndMessage(messageId, subject, message);
    }

    public boolean resolveMessage(int messageId, User resolvingUser) {
        long currentTime = System.currentTimeMillis() / 1000L;
        if (!messageRepo.existsById(messageId)) {
            return false;
        }
        boolean resolved = messageRepo.updateResolved(messageId, 1);
        boolean timeUpdated = messageRepo.updateDateResolved(messageId, currentTime);
        boolean resolvingUserUpdated = messageRepo.updateUserResolved(messageId, resolvingUser.getUsername());
        return resolved && timeUpdated && resolvingUserUpdated;
    }

    public Message searchMessage(int messageId) {
        if (!messageRepo.existsById(messageId)) {
            return null;
        }
        return messageRepo.findById(messageId);
    }

    public boolean deleteMessage(int messageId) {
        if (!messageRepo.existsById(messageId)) {
            return false;
        }
        return messageRepo.deleteById(messageId);
    }

    public List<Message> listUnresolved() {
        List <Message> messageList = messageRepo.listMessages();
        if (messageList == null) {
            return null;
        }
        Iterator<Message> iter = messageList.iterator();
        while (iter.hasNext()) {
            Message m = iter.next();
            if (m.getResolved() == 1) {
                iter.remove();
            }
        }
        return messageList;
    }

    public List<Message> listResolved() {
        List <Message> messageList = messageRepo.listMessages();
        if (messageList == null) {
            return null;
        }
        Iterator<Message> iter = messageList.iterator();
        while (iter.hasNext()) {
            Message m = iter.next();
            if (m.getResolved() == 0) {
                iter.remove();
            }
        }
        return messageList;
    }

    public List<Message> listAll() {
        return messageRepo.listMessages();
    }
}
