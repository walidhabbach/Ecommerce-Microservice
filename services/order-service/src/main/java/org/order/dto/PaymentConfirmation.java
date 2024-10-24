package org.order.dto;

import lombok.Getter;
import lombok.Setter;
import org.order.constant.PaymentMethod;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentConfirmation{
    private Integer orderId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private String customerFirstname;
    private String customerLastname;
    private String customerEmail;
}
