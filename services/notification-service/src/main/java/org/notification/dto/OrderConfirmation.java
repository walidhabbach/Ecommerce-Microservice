package org.notification.dto;

import lombok.*;
import org.notification.constant.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;



@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderConfirmation {
    private Integer orderId;
    private PaymentMethod paymentMethod;
    private BigDecimal totalAmount;
    private String customerId;
    private String customerFirstname;
    private String customerLastname;
    private String customerEmail;
    private List<OrderLineDto> orderLines;

}