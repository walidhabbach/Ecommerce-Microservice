package org.order.helper;

import org.order.dto.OrderLineDto;
import org.order.model.OrderLine;
import org.order.model.Orders;

import java.util.List;
import java.util.stream.Collectors;

public interface OrderLineMappingHelper {
	// Method to map OrderLineDto to OrderLine
	static OrderLine map(final OrderLineDto orderLineDto) {
		return OrderLine.builder()
				.id(orderLineDto.getId())
				.order(orderLineDto.getOrder())
				.productId(orderLineDto.getProductId())
				.quantity(orderLineDto.getQuantity())
				.build();
	}


	static OrderLineDto map(final OrderLine orderLine) {
		return OrderLineDto.builder()
				.id(orderLine.getId())
				.order(orderLine.getOrder())
				.productId(orderLine.getProductId())
				.quantity(orderLine.getQuantity())
				.build();
	}

	// Method to map List<OrderLine> to List<OrderLineDto>
	static List<OrderLineDto> mapToDtoList(List<OrderLine> orderLineList) {
		return orderLineList.stream()
				.map(OrderLineMappingHelper::map)  // Maps each OrderLine to OrderLineDto
				.collect(Collectors.toList());
	}

	// Method to map List<OrderLineDto> to List<OrderLine>
	static List<OrderLine> mapToModelList(List<OrderLineDto> orderLineDtoList) {
		return orderLineDtoList.stream()
				.map(OrderLineMappingHelper::map)  // Maps each OrderLineDto to OrderLine
				.collect(Collectors.toList());
	}
}
