package com.compass.testCompass.infrastructure.repository;

import com.compass.testCompass.domain.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingRepository extends JpaRepository<Billing, Long> {
}