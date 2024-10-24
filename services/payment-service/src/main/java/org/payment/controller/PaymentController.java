package org.payment.controller;

import lombok.RequiredArgsConstructor;
import org.payment.dto.PaymentDto;
import org.payment.service.interfaces.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @GetMapping
    public ResponseEntity<List<PaymentDto>> findAll() {
        return ResponseEntity.ok(this.service.findAll());
    }


    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(
            @RequestBody PaymentDto request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(request));
    }
    @PutMapping
    public ResponseEntity<PaymentDto> updatePayment( @RequestBody PaymentDto request) {
        return ResponseEntity.ok(this.service.update(request));
    }
}