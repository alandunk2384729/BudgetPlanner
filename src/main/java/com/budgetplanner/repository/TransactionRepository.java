package com.budgetplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.budgetplanner.entities.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
	
	public List<Transaction> findByValueLessThan(Double value);
	
}
