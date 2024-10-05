package com.compass.testCompass.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Seller {
    @Id
    private Long id;
    private String name;
}
