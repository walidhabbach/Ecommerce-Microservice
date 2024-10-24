package org.product_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.product_service.dto.InventoryDto;
import org.product_service.dto.OrderLineDto;
import org.product_service.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.product_service.response.CollectionResponse;
import org.product_service.service.interfaces.ProductService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> findAll() {
        log.info("*** ProductDto List, controller; fetch all categories *");
        return ResponseEntity.ok(this.productService.findAll());
    }
    @PostMapping("/purchase")
    public ResponseEntity<BigDecimal> purchaseProducts(@RequestBody List<OrderLineDto> request) {
        return ResponseEntity.ok(productService.purchaseProducts(request));
    }



    @GetMapping("/inventory")
    public ResponseEntity <List<InventoryDto>> isInStock(@RequestParam List<Integer> ids) {
        log.info("Received inventory check request for id: {}", ids);
        return ResponseEntity.ok(productService.isInStock(ids));
    }
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> findById(@PathVariable("productId") final Integer productId) {
        log.info("*** ProductDto, resource; fetch product by id *");
        return ResponseEntity.ok(this.productService.findById(productId));
    }
    @PostMapping
    public ResponseEntity<ProductDto> save( @RequestBody final ProductDto productDto) {
        log.info("*** ProductDto, resource; save product *"+productDto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(this.productService.save(productDto));
    }

    @PutMapping
    public ResponseEntity<ProductDto> update(  @RequestBody  final ProductDto productDto) {
        log.info("*** ProductDto, resource; update product *");
        return ResponseEntity.ok(this.productService.update(productDto));
    }
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> update(  @PathVariable("productId")  final Integer productId, @RequestBody final ProductDto productDto) {
        log.info("*** ProductDto, resource; update product with productId *");
        return ResponseEntity.ok(this.productService.update(productId, productDto));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("productId") final Integer productId) {
        log.info("*** Boolean, resource; delete product by id *");
        this.productService.deleteById(productId);
        return ResponseEntity.ok(true);
    }
}
