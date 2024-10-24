package org.notification.service.interfaces;

import org.notification.dto.NotificationDto;
import org.notification.model.Notification;

import java.util.List;

public interface NotificationService{
    List<NotificationDto> findAllByCustomerId(final String customerId);
    NotificationDto findById(final String id);
}
