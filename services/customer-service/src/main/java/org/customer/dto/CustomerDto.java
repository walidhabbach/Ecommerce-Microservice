package org.customer.dto;

import lombok.*;
import org.customer.model.Address;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CustomerDto{
        private String id;
        private String firstname;
        private String lastname;
        private String email;
        private Address address;
        private String password;
}
