package com.compass.testCompass.application.service;

import com.compass.testCompass.application.port.MessageSender;
import com.compass.testCompass.domain.model.Billing;
import com.compass.testCompass.exception.BillingNotFoundException;
import com.compass.testCompass.exception.SellerNotFoundException;
import com.compass.testCompass.infrastructure.repository.BillingRepository;
import com.compass.testCompass.infrastructure.repository.SellerRepository;
import com.compass.testCompass.web.dto.PaymentDTO;
import com.compass.testCompass.web.dto.PaymentItemDTO;
import org.springframework.stereotype.Component;

@Component
public class ProcessPaymentsUseCaseImpl implements ProcessPaymentsUseCase {

    private final SellerRepository sellerRepository;
    private final BillingRepository billingRepository;
    private final MessageSender messageSender;

    public ProcessPaymentsUseCaseImpl(SellerRepository sellerRepository, BillingRepository billingRepository, MessageSender messageSender) {
        this.sellerRepository = sellerRepository;
        this.billingRepository = billingRepository;
        this.messageSender = messageSender;
    }

    @Override
    public PaymentDTO processPayments(PaymentDTO payment) {
        validateSeller(payment.getSellerId());

        for (PaymentItemDTO paymentItem : payment.getPaymentItems()) {
            Billing billing = validateBilling(paymentItem.getBillingCode());
            processPaymentItem(paymentItem, billing);
        }

        return payment;
    }

    private void validateSeller(String sellerId) {
        sellerRepository.findById(Long.valueOf(sellerId))
                .orElseThrow(() -> new SellerNotFoundException(String.format("Vendedor de código %s não encontrado.", sellerId)));
    }

    private Billing validateBilling(String billingCode) {
        return billingRepository.findById(Long.valueOf(billingCode))
                .orElseThrow(() -> new BillingNotFoundException(String.format("Cobrança %s não encontrada.", billingCode)));
    }

    private void processPaymentItem(PaymentItemDTO paymentItem, Billing billing) {
        if (paymentItem.getPaymentValue().compareTo(billing.getAmount()) < 0) {
            sendToSQS(paymentItem, "PARCIAL");
        } else if (paymentItem.getPaymentValue().compareTo(billing.getAmount()) == 0) {
            sendToSQS(paymentItem, "TOTAL");
        } else {
            sendToSQS(paymentItem, "EXCEDENTE");
        }
    }

    private void sendToSQS(PaymentItemDTO payment, String status) {
        payment.setPaymentStatus(status);
        String queueName = getQueueNameByStatus(status);
        messageSender.sendMessage(payment, queueName);
    }

    private String getQueueNameByStatus(String status) {
        return switch (status) {
            case "PARCIAL" -> "partial-payments-queue";
            case "TOTAL" -> "total-payments-queue";
            case "EXCEDENTE" -> "excess-payments-queue";
            default -> throw new IllegalArgumentException("Unknown payment status: " + status);
        };
    }
}