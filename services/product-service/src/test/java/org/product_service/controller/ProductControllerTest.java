package org.product_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.product_service.dto.ProductDto;
import org.product_service.helper.CategoryMappingHelper;
import org.product_service.model.Category;
import org.product_service.repository.CategoryRepository;
import org.product_service.repository.ProductRepository;
import org.product_service.service.interfaces.ProductService;
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

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15.3");

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryRepository categoryRepository;
    @BeforeAll
    static void setUp() {
        postgresContainer.start();
    }
    @BeforeEach
    void setUpCategory() {
        // Insert test data
        Category category = new Category();
        category.setId(1);
        category.setName("mobile");
        categoryRepository.save(category);
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Test
    void createProduct() throws Exception {
        ProductDto productDto = getProductDto();
        String productRequestString = objectMapper.writeValueAsString(productDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestString))
                .andExpect(status().isCreated());

        System.out.println("Data " + productRepository.findAll().toString());
        Assertions.assertEquals(1, productRepository.findAll().size());
    }

    private ProductDto getProductDto() {
        return ProductDto.builder()
                .name("iPhone 13")
                .description("iPhone 13")
                .price(BigDecimal.valueOf(1200.0))
                .quantity(100)
                .id(1)
                .category(CategoryMappingHelper.map(categoryRepository.findById(1).get()))
                .build();
    }

    @Test
    public void updateProduct() throws Exception {
        ProductDto productDto = getProductDto();
        productDto = productService.save(productDto);
        // Arrange
        productDto.setPrice(BigDecimal.valueOf(1000.0));
        productDto.setName("update iPhone 13");
        String updatedProductDtoString = objectMapper.writeValueAsString(productDto);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProductDtoString))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(productDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(productDto.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(productDto.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.category.id").value(productDto.getCategory().getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category.name").value(productDto.getCategory().getName()));
    }

}