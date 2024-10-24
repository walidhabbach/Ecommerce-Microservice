package org.notification.service.interfaces;
import org.notification.dto.OrderLineDto;

import java.math.BigDecimal;
import java.util.List;

public interface EmailService{

      void sendPaymentEmail(String destinationEmail, String customerName, BigDecimal amount, Integer orderId);

     void sendOrderEmail( String destinationEmail, String customerName,   BigDecimal amount,   Integer orderId,  List<OrderLineDto> products);
}
