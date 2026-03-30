package com.blackout.api.merch.infrastructure.persistence;

import com.blackout.api.finance.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaTransactionForMerchRepository extends JpaRepository<Transaction, String> {
}
