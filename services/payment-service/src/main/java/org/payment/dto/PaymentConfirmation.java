package org.payment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.payment.constant.PaymentMethod;

import java.math.BigDecimal;
@Builder
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
