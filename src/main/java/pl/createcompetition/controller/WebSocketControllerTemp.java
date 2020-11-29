package pl.createcompetition.controller;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import pl.createcompetition.model.websockets.SendNotificationPayload;
import pl.createcompetition.security.CurrentUser;
import pl.createcompetition.security.UserPrincipal;

import java.security.Principal;
import java.util.Map;

@AllArgsConstructor
@Controller
public class WebSocketControllerTemp {


    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private Gson gson = new Gson();



    @MessageMapping("/news")
    @SendTo("/topic/news")
    public String broadcastNews(@Payload String message) {
        return message;
    }

    @MessageMapping("/message")
    @SendToUser("/queue/reply")
    public String processMessageFromClient(
            @Payload String message,
            Principal principal) throws Exception {
        return gson
                .fromJson(message, Map.class)
                .get("name").toString();
    }


    @MessageMapping("/notification")
    @SendToUser("/queue/notification")
    public SendNotificationPayload send(@Payload SendNotificationPayload sendNotificationPayload, @CurrentUser UserPrincipal userPrincipal) {
        return sendNotificationPayload;
    }




}
