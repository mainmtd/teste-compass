package com.compass.testCompass.application.port;

import com.compass.testCompass.web.dto.PaymentItemDTO;

public interface MessageSender {
    void sendMessage(PaymentItemDTO paymentItem, String queueName);
}
