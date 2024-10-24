package org.customer.helper;


import org.customer.dto.CustomerDto;
import org.customer.model.Customer;

public interface CustomerMapper {

  public static Customer map(final CustomerDto customerDto) {
    return Customer.builder()
            .id(customerDto.getId())
            .firstname(customerDto.getFirstname())
            .lastname(customerDto.getLastname())
            .email(customerDto.getEmail())
            .address(customerDto.getAddress())
            .password(customerDto.getPassword())
            .build();
  }

  public static CustomerDto map(final Customer customer) {
    return CustomerDto.builder()
            .id(customer.getId())
            .firstname(customer.getFirstname())
            .lastname(customer.getLastname())
            .email(customer.getEmail())
            .address(customer.getAddress())
            .password(customer.getPassword())
            .build();
  }
}
