package org.notification.repository;

import org.notification.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String>{
    List<Notification> findAllByCustomerId(final String customerId);
}