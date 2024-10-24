package org.order.helper;


import org.order.dto.OrderDto;
import org.order.model.Orders;


public interface OrderMappingHelper {

    public static OrderDto map(final Orders order) {
        return OrderDto.builder()
                .customerId(order.getCustomerId())
                .id(order.getId())
                .lastModifiedDate(order.getLastModifiedDate())
                .createdDate(order.getCreatedDate())
                .paymentMethod(order.getPaymentMethod())
                .orderLines(OrderLineMappingHelper.mapToDtoList(order.getOrderLines()))  // Convert list of OrderLine to OrderLineDto
                .build();
    }

    public static Orders map(final OrderDto orderDto) {
        return Orders.builder()
                .customerId(orderDto.getCustomerId())
                .id(orderDto.getId())
                .lastModifiedDate(orderDto.getLastModifiedDate())
                .createdDate(orderDto.getCreatedDate())
                .paymentMethod(orderDto.getPaymentMethod())
                .orderLines(OrderLineMappingHelper.mapToModelList(orderDto.getOrderLines()))  // Convert list of OrderLineDto to OrderLine
                .build();
    }

}




