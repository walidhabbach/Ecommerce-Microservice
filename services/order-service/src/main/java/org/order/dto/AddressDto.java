package org.order.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AddressDto {

    private String street;
    private String houseNumber;
    private String zipCode;
}
