package org.customer.controller;

import org.customer.dto.CustomerDto;
import org.customer.helper.CustomerMapper;
import org.customer.model.Address;
import org.customer.model.Customer;
import org.customer.service.implementations.JwtService;
import org.junit.jupiter.api.*;

import  org.customer.repository.CustomerRepository;
import  org.customer.service.interfaces.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerService CustomerService;
	@Autowired
	private JwtService jwtService;
    static {
        mongoDBContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dymDynamicPropertyRegistry) {
        dymDynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void createCustomer() throws Exception {
        CustomerDto customerDto = getcustomerDto();
        String customerRequestString = objectMapper.writeValueAsString(customerDto);
	    MvcResult signupResult = mockMvc.perform(post("/api/customers/signup")  // Make sure to match the correct endpoint
					    .contentType(MediaType.APPLICATION_JSON)
					    .content(customerRequestString))
			    .andExpect(status().isCreated())
			    .andReturn();
	    String signupResponseContent = signupResult.getResponse().getContentAsString();
		String idSignup = jwtService.decodeToken(signupResponseContent);

	    MvcResult signInResult = mockMvc.perform(post("/api/customers/signin")  // Make sure to match the correct endpoint
					    .contentType(MediaType.APPLICATION_JSON)
					    .content(customerRequestString))
			    .andReturn();
	    String signInResponseContent = signInResult.getResponse().getContentAsString();
	    String idSignIn = jwtService.decodeToken(signInResponseContent);

		Assertions.assertEquals(idSignup,idSignIn);
     }

    private CustomerDto getcustomerDto() {
        return CustomerDto.builder()
                .address(Address.builder().houseNumber("123").street("Main Street").zipCode("12345").build())
                .email("walid@gmail.com")
                .firstname("walid")
                .lastname("habbach")
		        .password("2002")
                .build();
    }
    @Test
    public void updateCustomer() throws Exception {
	    CustomerDto customerDto = getcustomerDto();
	    customerDto.setEmail("walid13@gmail.com\"");
	    String customerRequestString = objectMapper.writeValueAsString(customerDto);
	    MvcResult signupResult = mockMvc.perform(post("/api/customers/signup")  // Make sure to match the correct endpoint
					    .contentType(MediaType.APPLICATION_JSON)
					    .content(customerRequestString))
			    .andExpect(status().isCreated())
			    .andReturn();
	    String signupResponseContent = signupResult.getResponse().getContentAsString();
	    String idSignup = jwtService.decodeToken(signupResponseContent);
	    customerDto.setId(idSignup);
	    customerDto.setFirstname("oualid");
        customerDto.setLastname("hbh");
        String updatedcustomerDtoString = objectMapper.writeValueAsString(customerDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedcustomerDtoString))
                .andExpect(MockMvcResultMatchers.status().isOk())
		        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(customerDto.getId()))
		        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(customerDto.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname").value(customerDto.getFirstname()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname").value(customerDto.getLastname()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.address.houseNumber").value(customerDto.getAddress().getHouseNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address.street").value(customerDto.getAddress().getStreet()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address.zipCode").value(customerDto.getAddress().getZipCode()));

    }


}