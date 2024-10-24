package org.customer.service.interfaces;

import org.customer.dto.CustomerDto;

import java.util.List;

public interface CustomerService {
	
	List<CustomerDto> findAll();
	CustomerDto findById(final String id);
	String save(final CustomerDto customerDto);
	String login(final CustomerDto customerDto);
	CustomerDto update(final CustomerDto CustomerDto);
	CustomerDto updateByID(String id, CustomerDto customerDto) ;
	void deleteById(final String CustomerId);
	boolean existsById(final String CustomerId);

	
}
