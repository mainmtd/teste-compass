package com.compass.testCompass.infrastructure.messaging;

import com.compass.testCompass.application.port.MessageSender;
import com.compass.testCompass.web.dto.PaymentItemDTO;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class SQSMessageSender implements MessageSender {

    Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public void sendMessage(PaymentItemDTO paymentItem, String queueName) {
        // Simulate sending message to SQS
        logger.info("Sending message to queue %s: %s%n");
    }
}
