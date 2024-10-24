package org.notification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.notification.constant.NotificationType;
import org.notification.dto.OrderConfirmation;
import org.notification.dto.PaymentConfirmation;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Notification {

    @Id
    private String id;
    private String customerId;
    private NotificationType type;
    private LocalDateTime notificationDate;
    private OrderConfirmation order;
    private PaymentConfirmation payment;
}