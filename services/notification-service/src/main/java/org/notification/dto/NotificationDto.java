package org.notification.dto;

import lombok.*;
import org.notification.constant.NotificationType;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto{

    private String id;
    private String customerId;
    private NotificationType type;
    private LocalDateTime notificationDate;
    private OrderConfirmation order;
    private PaymentConfirmation payment;


}
