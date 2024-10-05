// src/main/java/com/compass/testCompass/controller/PaymentController.java
package com.compass.testCompass.web.controller;

import com.compass.testCompass.application.service.ProcessPaymentsUseCase;
import com.compass.testCompass.web.dto.PaymentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private ProcessPaymentsUseCase processPayments;

    @PostMapping
    public ResponseEntity<?> processPayments(@RequestBody PaymentDTO payment) {
        PaymentDTO updatedPayment = processPayments.processPayments(payment);
        return ResponseEntity.ok(updatedPayment);
    }
}