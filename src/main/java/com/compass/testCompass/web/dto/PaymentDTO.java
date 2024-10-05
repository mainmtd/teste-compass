package com.compass.testCompass.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    @JsonProperty("codigo_vendedor")
    private String sellerId;
    @JsonProperty("pagamentos")
    private List<PaymentItemDTO> paymentItems;
}
