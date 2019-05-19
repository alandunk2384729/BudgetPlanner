package com.budgetplanner.batch.util;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.item.database.JpaItemWriter;

import com.budgetplanner.entities.Transaction;

public class TransactionParserUtils {
	
	public static JpaItemWriter<Transaction> transactionWriter(EntityManagerFactory entityManagerFactory) {
		JpaItemWriter<Transaction> jpaItemWriter = new JpaItemWriter<>();
		jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
		return jpaItemWriter;
	}
	
}