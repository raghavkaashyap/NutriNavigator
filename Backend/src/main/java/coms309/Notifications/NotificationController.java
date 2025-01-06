package coms309.Notifications;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Controller
@ServerEndpoint("/notifications/{username}")
public class NotificationController {

    private static HashSet<Session> sessions = new HashSet<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username){
        sessions.add(session);
        System.out.println("Notification WebSocket established for " + username);
    }

    @OnClose
    public void onClose(Session session){
        sessions.remove(session);
        System.out.println("Notification WebSocket closed");
    }

    @OnError
    public void onError(Session session, Throwable throwable){
        System.out.println("Error in notification connection: " + throwable.getMessage());
    }

    public static void sendNotification(String message){
        for (Session session: sessions){
            try{
                session.getBasicRemote().sendText(message);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
