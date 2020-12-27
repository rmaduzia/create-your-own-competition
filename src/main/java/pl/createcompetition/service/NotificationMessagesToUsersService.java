package pl.createcompetition.service;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.createcompetition.model.websockets.UserNotification;
import pl.createcompetition.repository.NotificationRepository;

@AllArgsConstructor
@Service
public class NotificationMessagesToUsersService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void notificationMessageToUser(String recipientUserName, String subject, String action, String event) {

        String content = notificationBuilderContent(subject, action, event);

        UserNotification userNotification = UserNotification.builder().recipient(recipientUserName).content(content).build();
        notificationRepository.save(userNotification);

        simpMessagingTemplate.convertAndSendToUser(recipientUserName, "/queue/notifications", userNotification);
    }

    public String notificationBuilderContent(String Subject,String action,  String event) {
        return Subject +" "+ action + " "+ event;
    }

}
