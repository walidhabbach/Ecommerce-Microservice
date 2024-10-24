package org.payment.service.implementations;
import org.payment.dto.PaymentConfirmation;
import org.payment.dto.PaymentDto;
import org.payment.helper.PaymentMappingHelper;
import org.payment.kafka.NotificationProducer;
import org.payment.repository.PaymentRepository;
import org.payment.service.interfaces.PaymentService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.desktop.SystemSleepEvent;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImp implements PaymentService{

	private final PaymentRepository repository;
	private final NotificationProducer notificationProducer;



	@Override
	public PaymentDto save(PaymentDto paymentDto) {
		PaymentDto payment = PaymentMappingHelper.map(this.repository.save(PaymentMappingHelper.map(paymentDto)));
		System.out.println(paymentDto.getCustomer());
		this.notificationProducer.sendNotification(
				 PaymentConfirmation.builder()
						 .customerId(paymentDto.getCustomer().getId())
						 .customerEmail(paymentDto.getCustomer().getEmail())
						 .customerLastname(paymentDto.getCustomer().getLastname())
						 .customerFirstname(paymentDto.getCustomer().getFirstname())
						 .amount(paymentDto.getAmount())
						 .paymentMethod(paymentDto.getPaymentMethod())
						 .orderId(paymentDto.getOrderId())
						 .build()
		);
		return payment;
	}

	@Override
	public PaymentDto update(PaymentDto paymentDto){
		 return PaymentMappingHelper.map(this.repository.save(PaymentMappingHelper.map(paymentDto)));
	}

	@Override
	public List<PaymentDto> findAll(){
		return this.repository.findAll()
				.stream()
				.map(PaymentMappingHelper::map)
				.collect(Collectors.toList());
	}


}









