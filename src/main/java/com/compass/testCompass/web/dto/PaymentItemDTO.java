package com.compass.testCompass.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentItemDTO {
    @JsonProperty("codigo_cobranca")
    private String billingCode;
    @JsonProperty("valor_pagamento")
    private BigDecimal paymentValue;
    @JsonProperty("situacao_pagamento")
    private String paymentStatus;
}
