package pl.createcompetition.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.createcompetition.model.websockets.UserNotification;

public interface NotificationRepository extends MongoRepository<UserNotification, String> {
}