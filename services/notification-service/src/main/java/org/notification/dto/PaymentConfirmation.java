package org.notification.dto;

import lombok.Getter;
import lombok.Setter;
import org.notification.constant.PaymentMethod;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentConfirmation{
    private Integer orderId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private String customerId;
    private String customerFirstname;
    private String customerLastname;
    private String customerEmail;

}
