package org.order.service.interfaces;

import org.order.dto.OrderLineDto;

import java.util.List;

public interface OrderLineService{

      OrderLineDto save(OrderLineDto orderLineDto);
      List<OrderLineDto> findAllByOrderId(Integer orderId);
}
