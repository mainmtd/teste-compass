package com.compass.testCompass.infrastructure.repository;

import com.compass.testCompass.domain.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}