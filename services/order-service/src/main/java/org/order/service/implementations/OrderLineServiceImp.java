package org.order.service.implementations;

import lombok.RequiredArgsConstructor;
import org.order.dto.OrderLineDto;
import org.order.helper.OrderLineMappingHelper;
import org.order.repository.OrderLineRepository;
import org.order.service.interfaces.OrderLineService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderLineServiceImp implements OrderLineService{

    private final OrderLineRepository orderLineRepository;
    @Override
    public OrderLineDto save(OrderLineDto orderLineDto){

        return OrderLineMappingHelper.map(orderLineRepository.save(OrderLineMappingHelper.map(orderLineDto)));
    }
    @Override
    public List<OrderLineDto> findAllByOrderId(Integer orderId){
        return this.orderLineRepository.findAllByOrderId(orderId)
                .stream()
                .map(OrderLineMappingHelper::map)
                .collect(Collectors.toList());
     
    }
  
}
