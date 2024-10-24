package org.payment.service.interfaces;
import org.payment.dto.PaymentDto;

import java.util.List;

public interface PaymentService{
     PaymentDto save(PaymentDto paymentDto);
     PaymentDto update(PaymentDto paymentDto);
     List<PaymentDto> findAll();

}