package org.payment.controller;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.junit.jupiter.api.*;
import org.payment.constant.PaymentMethod;
import org.payment.dto.CustomerDto;
import org.payment.dto.PaymentDto;
import  org.payment.service.interfaces.PaymentService;
import org.payment.repository.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15.3");

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PaymentRepository PaymentRepository;
    @Autowired
    private PaymentService PaymentService;

    @BeforeAll
    static void setUp() {
        postgresContainer.start();
    }
    @AfterAll
    static void tearDown() {
        postgresContainer.stop();
    }
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

//    @Test
//    void createPayment() throws Exception {
//        PaymentDto paymentDto = getPaymentDto();
//        System.out.println(paymentDto.getCustomer());
//        String PaymentRequestString = objectMapper.writeValueAsString(paymentDto);
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/payments")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(PaymentRequestString))
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(paymentDto.getAmount()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentMethod").value(paymentDto.getPaymentMethod().toString()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.orderId").value(paymentDto.getOrderId()));
//
//        Assertions.assertEquals(1, PaymentRepository.findAll().size());
//    }

    private PaymentDto getPaymentDto() {
        CustomerDto customerDto = CustomerDto.builder()
                .id("66b6301d22939a59e7267b58")
                .email("elmessaoudi759@gmail.com")
                .firstname("ahmed")
                .lastname("elmessaoudi")
                .build();

        return PaymentDto.builder()
                .paymentMethod(PaymentMethod.PAYPAL)
                .orderId(1)
                .customer(customerDto)
                .amount(BigDecimal.valueOf(1200.0))
                .build();
    }

    @Test
    public void updatePayment() throws Exception {
        PaymentDto paymentDto = getPaymentDto();
        paymentDto = PaymentService.update(paymentDto);
        // Arrange
        paymentDto.setAmount(BigDecimal.valueOf(1000.0));
        paymentDto.setPaymentMethod(PaymentMethod.BITCOIN);
        String updatedPaymentDtoString = objectMapper.writeValueAsString(paymentDto);


        mockMvc.perform(MockMvcRequestBuilders.put("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPaymentDtoString))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(paymentDto.getAmount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentMethod").value(paymentDto.getPaymentMethod().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderId").value(paymentDto.getOrderId()));


    }

}