package pl.createcompetition.controller;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import pl.createcompetition.model.websockets.UserNotification;
import pl.createcompetition.security.CurrentUser;
import pl.createcompetition.security.UserPrincipal;


@AllArgsConstructor
@Controller
public class WebSocketMessagingController {

    private final SimpMessageSendingOperations messagingTemplate;
    private Gson gson = new Gson();

    @MessageMapping("/news")
    @SendTo("/topic/news")
    public String broadcastNews(@Payload String message) {
        return message;
    }



    /* Test Code
    @MessageMapping("/message")
    @SendToUser("/queue/reply")
    public String processMessageFromClient(
            @Payload String message,
            Principal principal) throws Exception {
        return gson
                .fromJson(message, Map.class)
                .get("name").toString();
    }
*/

    @MessageMapping("/notification")
    @SendToUser("/queue/notification")
    public UserNotification send(@Payload UserNotification userNotification, @CurrentUser UserPrincipal userPrincipal) {
        return userNotification;
    }




}
