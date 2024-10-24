package org.notification.dto;

import lombok.*;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineDto {
    private Integer id;
    private Integer productId;
    private Integer orderId;
    private Integer quantity;

}
