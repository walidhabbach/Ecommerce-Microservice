package org.order.service.interfaces;

import java.util.List;

import org.order.dto.OrderDto;

public interface OrderService {
	
	List<OrderDto> findAll();
	OrderDto findById(final Integer orderId);
	OrderDto save(OrderDto orderDto);
	OrderDto update(final OrderDto orderDto);
	OrderDto update(final Integer orderId, final OrderDto orderDto);
	void deleteById(final Integer orderId);

	List<OrderDto> findAllOrders();
	List<OrderDto> findAllOrdersByCustomerId(String id);
}

