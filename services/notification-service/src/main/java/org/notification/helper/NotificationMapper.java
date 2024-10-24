package org.notification.helper;
import org.notification.dto.NotificationDto;
import org.notification.model.Notification;

public interface NotificationMapper{
   public static NotificationDto map(final Notification notification) {
    return NotificationDto.builder()
            .id(notification.getId())
            .notificationDate(notification.getNotificationDate())
            .type(notification.getType())
            .order(notification.getOrder())
            .payment(notification.getPayment())
            .customerId(notification.getCustomerId())
            .build();
  }
  public static Notification map(final NotificationDto notification) {
    return Notification.builder()
            .id(notification.getId())
            .notificationDate(notification.getNotificationDate())
            .type(notification.getType())
            .order(notification.getOrder())
            .payment(notification.getPayment())
            .customerId(notification.getCustomerId())
            .build();
  }
}
