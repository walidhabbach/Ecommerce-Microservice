package org.order.client;
import org.order.dto.OrderLineDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.List;

@FeignClient (
        name = "product-service",
        url = "${application.config.product-url}"
)
public interface ProductClient {

    @PostMapping("/purchase")
    BigDecimal  purchaseProducts(List<OrderLineDto> orderLineDtoList) ;
}