package org.product_service.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OrderLineDto {
    private Integer id;
    private Integer productId;
    private Integer quantity;
}