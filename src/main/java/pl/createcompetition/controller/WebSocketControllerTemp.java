package pl.createcompetition.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import pl.createcompetition.model.websockets.SendMessage;

@Controller
public class WebSocketControllerTemp {

    @MessageMapping("/broadcast")
    @SendTo("/topic/messages")
    public SendMessage send(SendMessage sendMessage) throws Exception {
        return new SendMessage(sendMessage.getFrom(), sendMessage.getText(), "ALL");
    }


}
