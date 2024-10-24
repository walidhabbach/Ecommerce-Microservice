package org.order.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.order.constant.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderConfirmation {
    private Integer orderId;
    private PaymentMethod paymentMethod;
    private BigDecimal totalAmount;
    private List<OrderLineDto> orderLines;
    private String customerId;
    private String customerFirstname;
    private String customerLastname;
    private String customerEmail;
}
