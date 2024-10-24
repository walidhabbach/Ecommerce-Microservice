package org.customer.controller;

import lombok.RequiredArgsConstructor;
import org.customer.dto.CustomerDto;
import org.customer.service.implementations.JwtService;
import org.customer.service.interfaces.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerService customerService;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private JwtService jwtService;

  @PostMapping("/signup")
  public ResponseEntity<String> createCustomer(
      @RequestBody CustomerDto customerDto
  ) {
    return  ResponseEntity.status(HttpStatus.CREATED).body(this.customerService.save(customerDto));
  }
   @PostMapping("/signin")
   public String login(@RequestBody CustomerDto customerDto) {
       System.out.println("signin : "+customerDto.getPassword()+customerDto.getEmail());
	   return this.customerService.login(customerDto);
    }
    @GetMapping("/validate")
    public String validateToken(@RequestParam ("token") String token) {
        jwtService.validateToken(token);
        return "Token is valid";
    }
  @PutMapping
  public ResponseEntity<CustomerDto> updateCustomer(
      @RequestBody  CustomerDto customerDto
  ) {
    return ResponseEntity.ok(this.customerService.update(customerDto));
  }

  @GetMapping
  public ResponseEntity<List<CustomerDto>> findAll() {
    return ResponseEntity.ok(this.customerService.findAll());
  }

  @GetMapping("/exists/{customer-id}")
  public ResponseEntity<Boolean> existsById(@PathVariable("customer-id") String customerId) {
    return ResponseEntity.ok(this.customerService.existsById(customerId));
  }

  @GetMapping("/{customer-id}")
  public ResponseEntity<CustomerDto> findById(
      @PathVariable("customer-id") String customerId
  ) {
    return ResponseEntity.ok(this.customerService.findById(customerId));
  }

  @DeleteMapping("/{customer-id}")
  public ResponseEntity<Void> delete(
      @PathVariable("customer-id") String customerId
  ) {
    this.customerService.deleteById(customerId);
    return ResponseEntity.accepted().build();
  }

}
