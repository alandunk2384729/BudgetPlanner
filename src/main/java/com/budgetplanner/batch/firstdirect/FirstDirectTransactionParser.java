package com.budgetplanner.batch.firstdirect;


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
public class FirstDirectTransactionParser {

	private static final String FIRST_DIRECT_TXN_FILE_PATH = BudgetAppConstants.STATEMENTS_DIR 
			+ "/" + BudgetAppConstants.FIRST_DIRECT_STATEMENT_FILE_NAME;

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Bean
	public FlatFileItemReader<TransactionData> firstDirectTxnReader() {
		FlatFileItemReader<TransactionData> itemReader = new FlatFileItemReaderBuilder<TransactionData>()
			.name("firstDirectTxnReader")
			.resource(new FileSystemResource(FIRST_DIRECT_TXN_FILE_PATH))
			.delimited()
			.names(new String[] { "txnDate", "description", "amount", "balance" })
			.fieldSetMapper(new FirstDirectTransactionDataFieldSetMapper())
			.build();
		
		itemReader.setLinesToSkip(1);		
		itemReader.setStrict(false);
		
		return itemReader;
	}
	
	@Bean
	public TransactionDataItemProcessor firstDirectTxnProcessor() {
		return new TransactionDataItemProcessor();
	}
	
	@Bean
	public JpaItemWriter<Transaction> firstDirectTxnWriter() {
		return TransactionParserUtils.transactionWriter(entityManagerFactory);
	}
	
}