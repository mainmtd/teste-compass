// src/main/java/com/compass/testCompass/domain/model/Billing.java
package com.compass.testCompass.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Billing {
    @Id
    private Long id;
    private BigDecimal amount;
}