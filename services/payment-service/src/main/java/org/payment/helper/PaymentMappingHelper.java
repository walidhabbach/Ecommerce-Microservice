package org.payment.helper;

import org.payment.constant.PaymentMethod;
import org.payment.dto.CustomerDto;
import org.payment.dto.PaymentConfirmation;
import org.payment.dto.PaymentDto;
import org.payment.model.Payment;

import java.math.BigDecimal;

public interface PaymentMappingHelper{
	
	public static PaymentDto map(final Payment payment) {
		return PaymentDto.builder()
				.orderId(payment.getOrderId())
				.id(payment.getId())
				.amount(payment.getAmount())
				.paymentMethod(payment.getPaymentMethod())
				.createdDate(payment.getCreatedDate())
				.lastModifiedDate(payment.getLastModifiedDate())
				.customer(CustomerDto.builder().id(payment.getCustomerId()).build())
				.build();
	}
	
	public static Payment map(final PaymentDto paymentDto) {
		return Payment.builder()
				.orderId(paymentDto.getOrderId())
				.id(paymentDto.getId())
				.amount(paymentDto.getAmount())
				.paymentMethod(paymentDto.getPaymentMethod())
				.createdDate(paymentDto.getCreatedDate())
				.lastModifiedDate(paymentDto.getLastModifiedDate())
				.customerId(paymentDto.getCustomer().getId())
				.build();
	}

	public static Payment map(final PaymentConfirmation paymentConfirmation) {
		return Payment.builder()
				.orderId(paymentConfirmation.getOrderId())
				.amount(paymentConfirmation.getAmount())
				.paymentMethod(paymentConfirmation.getPaymentMethod())
				.build();
	}  

}










