package org.notification.kafka;
import org.notification.constant.NotificationType;
import org.notification.dto.OrderConfirmation;
import org.notification.dto.PaymentConfirmation;
import org.notification.model.Notification;
import org.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.notification.service.interfaces.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    @KafkaListener(topics = "payment-topic")
    public void consumePaymentNotification(PaymentConfirmation paymentConfirmation)   {
        log.info(format("Consuming the message from payment-topic Topic:: %s", paymentConfirmation));
        notificationRepository.save(
                Notification.builder()
                        .customerId(paymentConfirmation.getCustomerId())
                        .type(NotificationType.PAYMENT_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .payment(paymentConfirmation)
                        .build()
        );
        var customerName = paymentConfirmation.getCustomerFirstname() + " " + paymentConfirmation.getCustomerLastname();
        emailService.sendPaymentEmail(
                paymentConfirmation.getCustomerEmail(),
                customerName,
                paymentConfirmation.getAmount(),
                paymentConfirmation.getOrderId()
        );
    }

    @KafkaListener(topics = "order-topic")
    public void consumeOrderNotification(OrderConfirmation orderConfirmation)  {
        System.out.println("orderConfirmation :"+orderConfirmation.getOrderId());
        log.info(format("Consuming the message from order-topic Topic:: %s", orderConfirmation));
        notificationRepository.save(
                Notification.builder()
                        .customerId(orderConfirmation.getCustomerId())
                        .type(NotificationType.ORDER_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .order(orderConfirmation)
                        .build()
        );
        String customerName = orderConfirmation.getCustomerFirstname() + " " + orderConfirmation.getCustomerFirstname();

        emailService.sendOrderEmail(
                orderConfirmation.getCustomerEmail(),
                customerName,
                orderConfirmation.getTotalAmount(),
                orderConfirmation.getOrderId(),
                orderConfirmation.getOrderLines()
        );
    }
}