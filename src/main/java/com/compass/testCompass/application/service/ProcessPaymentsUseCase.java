package com.compass.testCompass.application.service;

import com.compass.testCompass.web.dto.PaymentDTO;

public interface ProcessPaymentsUseCase {
    PaymentDTO processPayments(PaymentDTO payment);
}