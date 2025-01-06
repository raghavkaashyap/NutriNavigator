package coms309.Recipe;

import jakarta.websocket.server.PathParam;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;

import java.util.HashSet;

@Controller
@ServerEndpoint(value = "/livestats/{username}")
public class LiveTrackingController {
    private static LiveTrackingRepository trackingRepository;

    @Autowired
    public void setTrackingRepository(LiveTrackingRepository repo) {
        trackingRepository = repo;
    }

    private static HashSet<Session> sessionList = new HashSet<>();
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        sessionList.add(session);
        System.out.println("Live tracking web socket connection established");
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println("Live tracking web socket message received");
        LiveTrackingService.messageTypeParser(message, session, trackingRepository);
        for (Session s : sessionList) {
            LiveTrackingService.returnSortedList(s);
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessionList.remove(session);
        System.out.println("Live tracking web socket connection closed");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("Error in live tracking service: " + throwable.getMessage());
        System.out.println("Stacktrace: " + throwable.getStackTrace());
    }
}
