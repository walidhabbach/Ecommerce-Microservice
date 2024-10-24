package org.order.controller;
import org.order.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.order.service.interfaces.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder( @RequestBody OrderDto orderDto) {

        return ResponseEntity.ok(this.service.save(orderDto));
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> findAll() {

        return ResponseEntity.ok(this.service.findAllOrders());
    }

    @GetMapping("/{order-id}")
    public ResponseEntity<OrderDto> findById(@PathVariable("order-id") Integer orderId) {
        return ResponseEntity.ok(this.service.findById(orderId));
    }
}
