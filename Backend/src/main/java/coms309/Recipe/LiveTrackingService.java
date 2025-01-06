package coms309.Recipe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.websocket.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.*;

//Json example
/*
{
"messageType": "increment",
"recipeId": "",
"incrementValue": ""
}

{
"messageType": "requestData",
"dataType": "popular, mostRecent"
}
 */

public class LiveTrackingService {
    private static HashMap<Integer, LiveTrackingObject> liveTrackerMap = new HashMap<>();

    public static void messageTypeParser(String s, Session session, LiveTrackingRepository trackingRepository) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(s, JsonObject.class);
        String messageType = null;
        if (jsonObject.has("messageType")) {
            messageType = jsonObject.get("messageType").getAsString();
        }
        if (messageType == null) {
            return;
        }

        switch (messageType) {
            case "increment" -> processIncrementMessage(jsonObject);
            case "requestData" -> processRequestMessage(jsonObject, session);
            case "getHistory" -> processHistoryRequest(session, trackingRepository);
            case "test" -> addTestValues();
            case "testSave" -> logTestValues(trackingRepository);
        }
    }

    @Scheduled(cron = "0 0 * * *")
    private static void resetValues(LiveTrackingRepository trackingRepository) {
        List<LiveTrackingObject> sortedList = sortRecipeObjects();
        LiveTrackingObject mostPopular = sortedList.get(0);
        trackingRepository.insert(mostPopular.getRecipeId(), mostPopular.getPulls(), mostPopular.getDateLogged());
        liveTrackerMap = new HashMap<>();
    }

    private static void processIncrementMessage(JsonObject jsonObject) {
        int recipeId = -1;
        int incrementValue = -1;
        if (jsonObject.has("recipeId")) {
            recipeId = jsonObject.get("recipeId").getAsInt();
        }
        if (jsonObject.has("incrementValue")) {
            incrementValue = jsonObject.get("incrementValue").getAsInt();
        }
        if (recipeId == -1 || incrementValue == -1) {
            return;
        }
        incrementPull(recipeId, incrementValue);
    }

    private static void incrementPull(int id, int incrementValue) {
        if (liveTrackerMap.containsKey(id)) {
            LiveTrackingObject trackingObject = liveTrackerMap.get(id);
            trackingObject.incrementPulls(incrementValue);
        } else {
            LiveTrackingObject trackingObject = new LiveTrackingObject();
            trackingObject.incrementPulls(incrementValue);
            liveTrackerMap.putIfAbsent(id, trackingObject);
        }
    }

    private static void processRequestMessage(JsonObject jsonObject, Session session) {
        if (!jsonObject.has("dataType")) {
            return;
        }
        String dataType = jsonObject.get("dataType").getAsString();
        if (dataType.equals("popular")) {
            returnSortedList(session);
        }
    }

    public static void returnSortedList(Session session) {
        try {
            String json = new ObjectMapper().writeValueAsString(sortRecipeObjects());
            session.getBasicRemote().sendText(json);
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }
    }

    private static List<LiveTrackingObject> sortRecipeObjects() {
        List<Map.Entry<Integer, LiveTrackingObject>> tempList = new ArrayList<>(liveTrackerMap.entrySet());
        tempList.sort(new Comparator<Map.Entry<Integer, LiveTrackingObject>>() {
            @Override
            public int compare(Map.Entry<Integer, LiveTrackingObject> o1, Map.Entry<Integer, LiveTrackingObject> o2) {
                return Integer.compare(o2.getValue().getPulls(), o1.getValue().getPulls());
            }
        });

        List<LiveTrackingObject> returnList = new ArrayList<>();
        for (Map.Entry<Integer, LiveTrackingObject> e : tempList) {
            Integer recipeId = e.getKey();
            LiveTrackingObject tempObject = e.getValue();
            returnList.add(new LiveTrackingObject(recipeId, tempObject.getPulls(), tempObject.getDateLogged()));
        }
        return returnList;
    }

    private static void processHistoryRequest(Session session, LiveTrackingRepository trackingRepository) {
        try {
            String json = new ObjectMapper().writeValueAsString(trackingRepository.listLogs(10));
            session.getBasicRemote().sendText(json);
        } catch (JsonProcessingException ex) {
            System.out.println("Error while attempting to send user history of popular recipes");
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }
    }

    private static void addTestValues() {
        liveTrackerMap.put(1, new LiveTrackingObject(56, new Date(System.currentTimeMillis())));
        liveTrackerMap.put(2, new LiveTrackingObject(4, new Date(System.currentTimeMillis())));
        liveTrackerMap.put(3, new LiveTrackingObject(37, new Date(System.currentTimeMillis())));
        liveTrackerMap.put(4, new LiveTrackingObject(1, new Date(System.currentTimeMillis())));
        System.out.println("Test data added");
    }

    private static void logTestValues(LiveTrackingRepository trackingRepository) {
        resetValues(trackingRepository);
    }
}
