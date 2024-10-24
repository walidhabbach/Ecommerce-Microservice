package org.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.order.constant.PaymentMethod;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties (ignoreUnknown = true)
public class PaymentDto {
    Integer id;
    BigDecimal amount;
    PaymentMethod paymentMethod;
    Integer orderId;
    CustomerDto customer;
}
