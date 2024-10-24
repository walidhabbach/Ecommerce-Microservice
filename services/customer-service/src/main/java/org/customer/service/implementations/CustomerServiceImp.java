package org.customer.service.implementations;

import lombok.extern.slf4j.Slf4j;
import org.customer.dto.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.customer.exception.wrapper.CustomerNotFoundException;
import org.customer.helper.CustomerMapper;
import org.customer.model.Customer;
import org.customer.repository.CustomerRepository;
import org.customer.service.interfaces.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImp implements CustomerService {

	private final CustomerRepository customerRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private JwtService jwtService;
  @Autowired
  private AuthenticationManager authenticationManager;

	    @Override
	    public List<CustomerDto> findAll(){
		    return  this.customerRepository.findAll()
				    .stream()
				    .map(CustomerMapper::map)
				    .collect(Collectors.toList());

	    }

	    @Override
	    public CustomerDto findById(String id){
		     return this.customerRepository.findById(id)
				    .map(CustomerMapper::map)
				    .orElseThrow(() -> new CustomerNotFoundException(String.format("No customer found with the provided ID: %s", id)));
	    }

	    @Override
		  public String save(CustomerDto customerDto) {
		      customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
		      Customer customer = this.customerRepository.save(CustomerMapper.map(customerDto));
		    return jwtService.generateToken(customer.getId());
		  }
	  @Override
	  public String login(CustomerDto customerDto) {
	            System.out.println("signin : "+customerDto.getPassword()+customerDto.getEmail());
	            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(customerDto.getEmail(), customerDto.getPassword()));
	            if (authenticate.isAuthenticated()) {
		            Customer customer = customerRepository.findByEmail(customerDto.getEmail());
	                return jwtService.generateToken(customer.getId());
	            } else {
	                throw new RuntimeException("invalid access");
	            }
		  }

	    @Override
	    public CustomerDto update(CustomerDto customerDto){
		    Customer customer = this.customerRepository.findById(customerDto.getId())
				    .orElseThrow(() -> new CustomerNotFoundException(
						    String.format("Cannot update customer:: No customer found with the provided ID: %s", customerDto.getId())
				    ));
		    if (!passwordEncoder.matches(customerDto.getPassword(), customer.getPassword())) {
			    throw new IllegalStateException("Wrong password");
		    }

		    return CustomerMapper.map(this.customerRepository
				    .save(CustomerMapper.map(customerDto)));
	    }

	    @Override
	    public CustomerDto updateByID(String id, CustomerDto customerDto){
		    Customer customer = this.customerRepository.findById(id)
				    .orElseThrow(() -> new CustomerNotFoundException(
						    String.format("Cannot update customer:: No customer found with the provided ID: %s", customerDto.getId())
				    ));
		    if (!passwordEncoder.matches(customerDto.getPassword(), customer.getPassword())) {
			    throw new IllegalStateException("Wrong password");
		    }

		    return CustomerMapper.map(this.customerRepository
				    .save(CustomerMapper.map(customerDto)));
	    }

		@Override
		public void deleteById(String CustomerId) {
			this.customerRepository.deleteById(CustomerId);
		}

	    @Override
	    public boolean existsById(String CustomerId){
		    return this.customerRepository.findById(CustomerId)
				    .isPresent();
	    }

    }






