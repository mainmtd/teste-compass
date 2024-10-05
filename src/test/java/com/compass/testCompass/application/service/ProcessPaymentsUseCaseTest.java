package com.compass.testCompass.application.service;

import com.compass.testCompass.application.port.MessageSender;
import com.compass.testCompass.domain.model.Billing;
import com.compass.testCompass.domain.model.Seller;
import com.compass.testCompass.exception.BillingNotFoundException;
import com.compass.testCompass.exception.SellerNotFoundException;
import com.compass.testCompass.infrastructure.repository.BillingRepository;
import com.compass.testCompass.infrastructure.repository.SellerRepository;
import com.compass.testCompass.web.dto.PaymentDTO;
import com.compass.testCompass.web.dto.PaymentItemDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ProcessPaymentsUseCaseImplTest {

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private BillingRepository billingRepository;

    @Mock
    private MessageSender messageSender;

    @InjectMocks
    private ProcessPaymentsUseCaseImpl processPaymentsUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessPayments_SellerNotFound() {
        when(sellerRepository.findById(anyLong())).thenReturn(Optional.empty());

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setSellerId("1");

        assertThrows(SellerNotFoundException.class, () -> processPaymentsUseCase.processPayments(paymentDTO));
    }

    @Test
    void testProcessPayments_BillingNotFound() {
        when(sellerRepository.findById(anyLong())).thenReturn(Optional.of(new Seller()));
        when(billingRepository.findById(anyLong())).thenReturn(Optional.empty());

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setSellerId("1");
        PaymentItemDTO paymentItemDTO = new PaymentItemDTO();
        paymentItemDTO.setBillingCode("1");
        paymentDTO.setPaymentItems(List.of(paymentItemDTO));

        assertThrows(BillingNotFoundException.class, () -> processPaymentsUseCase.processPayments(paymentDTO));
    }

    @Test
    void testProcessPayments_PartialPayment() {
        when(sellerRepository.findById(anyLong())).thenReturn(Optional.of(new Seller()));
        Billing billing = new Billing();
        billing.setAmount(BigDecimal.valueOf(100));
        when(billingRepository.findById(anyLong())).thenReturn(Optional.of(billing));

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setSellerId("1");
        PaymentItemDTO paymentItemDTO = new PaymentItemDTO();
        paymentItemDTO.setBillingCode("1");
        paymentItemDTO.setPaymentValue(BigDecimal.valueOf(50));
        paymentDTO.setPaymentItems(List.of(paymentItemDTO));

        processPaymentsUseCase.processPayments(paymentDTO);

        verify(messageSender).sendMessage(paymentItemDTO, "partial-payments-queue");
    }

    @Test
    void testProcessPayments_TotalPayment() {
        when(sellerRepository.findById(anyLong())).thenReturn(Optional.of(new Seller()));
        Billing billing = new Billing();
        billing.setAmount(BigDecimal.valueOf(100));
        when(billingRepository.findById(anyLong())).thenReturn(Optional.of(billing));

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setSellerId("1");
        PaymentItemDTO paymentItemDTO = new PaymentItemDTO();
        paymentItemDTO.setBillingCode("1");
        paymentItemDTO.setPaymentValue(BigDecimal.valueOf(100));
        paymentDTO.setPaymentItems(List.of(paymentItemDTO));

        processPaymentsUseCase.processPayments(paymentDTO);

        verify(messageSender).sendMessage(paymentItemDTO, "total-payments-queue");
    }

    @Test
    void testProcessPayments_ExceedingPayment() {
        when(sellerRepository.findById(anyLong())).thenReturn(Optional.of(new Seller()));
        Billing billing = new Billing();
        billing.setAmount(BigDecimal.valueOf(100));
        when(billingRepository.findById(anyLong())).thenReturn(Optional.of(billing));

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setSellerId("1");
        PaymentItemDTO paymentItemDTO = new PaymentItemDTO();
        paymentItemDTO.setBillingCode("1");
        paymentItemDTO.setPaymentValue(BigDecimal.valueOf(150));
        paymentDTO.setPaymentItems(List.of(paymentItemDTO));

        processPaymentsUseCase.processPayments(paymentDTO);

        verify(messageSender).sendMessage(paymentItemDTO, "excess-payments-queue");
    }
}