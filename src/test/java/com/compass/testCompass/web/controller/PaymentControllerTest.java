package com.compass.testCompass.web.controller;

import com.compass.testCompass.application.service.ProcessPaymentsUseCase;
import com.compass.testCompass.web.dto.PaymentDTO;
import com.compass.testCompass.web.dto.PaymentItemDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaymentControllerTest {

    @Mock
    private ProcessPaymentsUseCase processPaymentsUseCase;

    @InjectMocks
    private PaymentController paymentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    void testProcessPayments() throws Exception {
        PaymentItemDTO paymentItemDTO = new PaymentItemDTO();
        paymentItemDTO.setBillingCode("1");
        paymentItemDTO.setPaymentValue(BigDecimal.valueOf(100));
        paymentItemDTO.setPaymentStatus("COMPLETED");

        PaymentDTO updatedPaymentDTO = new PaymentDTO("1", List.of(paymentItemDTO));

        when(processPaymentsUseCase.processPayments(any(PaymentDTO.class))).thenReturn(updatedPaymentDTO);

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo_vendedor\":\"1\",\"pagamentos\":[{\"codigo_cobranca\":\"1\",\"valor_pagamento\":100}]}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"codigo_vendedor\":\"1\",\"pagamentos\":[{\"codigo_cobranca\":\"1\",\"valor_pagamento\":100,\"situacao_pagamento\":\"COMPLETED\"}]}"));
    }
}
