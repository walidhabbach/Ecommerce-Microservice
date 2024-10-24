package org.order.service.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.order.client.CustomerClient;
import org.order.client.PaymentClient;
import org.order.client.ProductClient;
import org.order.dto.*;
import org.order.exception.wrapper.BusinessException;
import org.order.exception.wrapper.OrderNotFoundException;
import org.order.helper.OrderMappingHelper;
import org.order.kafka.OrderProducer;
import org.order.model.Orders;
import org.order.repository.OrderRepository;
import org.order.service.interfaces.OrderLineService;
import org.order.service.interfaces.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceIml implements OrderService{

    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;
    private final PaymentClient paymentClient;
    private final ProductClient productClient;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    @Override
    @Transactional
    public OrderDto save(OrderDto orderDto) {
        CustomerDto customer = this.customerClient.findCustomerById(orderDto.getCustomerId())
                .orElseThrow(() -> new BusinessException ("Cannot create order:: No customer exists with the provided ID"));

        BigDecimal totalAmount = this.productClient.purchaseProducts(orderDto.getOrderLines());

        Orders order = this.orderRepository.save(OrderMappingHelper.map(orderDto));

        for (OrderLineDto orderLineDto : orderDto.getOrderLines()) {
            orderLineDto.setOrder(order);
            orderLineService.save(orderLineDto);
        }
        PaymentDto paymentDto =  PaymentDto.builder()
                .amount(totalAmount)
                .paymentMethod(order.getPaymentMethod())
                .customer(customer)
                .orderId(order.getId())
                .build();

        this.paymentClient.requestOrderPayment(paymentDto);

        orderProducer.sendOrderConfirmation(
                OrderConfirmation.builder()
                        .customerId(order.getCustomerId())
                        .orderId(order.getId())
                        .paymentMethod(paymentDto.getPaymentMethod())
                        .totalAmount(paymentDto.getAmount())
                        .orderLines(orderDto.getOrderLines())
                        .customerEmail(customer.getEmail())
                        .customerFirstname(customer.getFirstname())
                        .customerLastname(customer.getLastname())
                        .build()
        );

        return OrderMappingHelper.map(order);
    }

    @Override
    public List<OrderDto> findAll() {
        log.info("*** orderRequest List, service; fetch all orders *");
        return this.orderRepository.findAll()
                .stream()
                .map(OrderMappingHelper::map)
                .distinct()
                .toList();
    }


    @Override
    public OrderDto findById(final Integer orderId) {
        log.info("*** orderRequest, service; fetch order by id *");
        return this.orderRepository.findById(orderId)
                .map(OrderMappingHelper::map)
                .orElseThrow(() -> new OrderNotFoundException(String
                        .format("Order with id: %d not found", orderId)));
    }



    @Override
    public List<OrderDto> findAllOrders() {
        return this.orderRepository.findAll()
                .stream()
                .map(OrderMappingHelper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> findAllOrdersByCustomerId (String id) {
        return  this.orderRepository.findAllByCustomerId (id)
                .stream()
                .map(OrderMappingHelper::map)
                .collect(Collectors.toList());
    }



    @Override
    public OrderDto update(final OrderDto orderRequest) {
        log.info("*** orderRequest, service; update order *");
        return OrderMappingHelper.map(this.orderRepository
                .save(OrderMappingHelper.map(orderRequest)));
    }




    @Override
    public OrderDto update(final Integer orderId, final OrderDto orderRequest) {
        log.info("*** orderRequest, service; update order with orderId *");
        return OrderMappingHelper.map(this.orderRepository
                .save(OrderMappingHelper.map(this.findById(orderId))));
    }

    @Override
    public void deleteById(Integer orderId) {

    }


}
