package com.budgetplanner.batch.nationwide;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.budgetplanner.batch.TransactionData;
import com.budgetplanner.batch.TransactionDataItemProcessor;
import com.budgetplanner.batch.util.TransactionParserUtils;
import com.budgetplanner.entities.Transaction;
import com.budgetplanner.util.BudgetAppConstants;

@Configuration
public class NationwideTransactionParser {

	private static final String NATIONWIDE_TXN_FILE_PATH = BudgetAppConstants.STATEMENTS_DIR 
			+ "/" + BudgetAppConstants.NATIONWIDE_STATEMENT_FILE_NAME;
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Bean
	public FlatFileItemReader<TransactionData> nationwideTxnReader() {
		FlatFileItemReader<TransactionData> itemReader = new FlatFileItemReaderBuilder<TransactionData>()
			.name("nationwideTxnReader")
			.resource(new FileSystemResource(NATIONWIDE_TXN_FILE_PATH))
			.delimited()
			.names(new String[] { "txnDate", "transactionType", "description", "debit", "credit", "balance" })
			.fieldSetMapper(new NationwideTransactionDataFieldSetMapper())
			.build();
		
		itemReader.setLinesToSkip(5);
		itemReader.setStrict(false);
		
		return itemReader;
	}
	
	@Bean
	public TransactionDataItemProcessor nationwideTxnProcessor() {
		return new TransactionDataItemProcessor();
	}
	
	@Bean
	public JpaItemWriter<Transaction> nationwideTxnWriter() {
		return TransactionParserUtils.transactionWriter(entityManagerFactory);
	}
	
}