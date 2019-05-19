package com.budgetplanner.batch.amazon;

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
public class AmazonTransactionParser {

	private static final String AMAZON_TXN_FILE_PATH = BudgetAppConstants.STATEMENTS_DIR + 
			"/" + BudgetAppConstants.AMAZON_STATEMENT_FILE_NAME;

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Bean
	public FlatFileItemReader<TransactionData> amazonTxnReader() {	
		FlatFileItemReader<TransactionData> itemReader = new FlatFileItemReaderBuilder<TransactionData>()
			.name("amazonTxnItemReader")
			.resource(new FileSystemResource(AMAZON_TXN_FILE_PATH))
			.delimited()
			.names(new String[] {"txnDate", "description", "value"})
			.fieldSetMapper(new AmazonTransactionDataFieldSetMapper())
			.build();

		// Allow no files
		itemReader.setStrict(false);
		
		// Skip header
		itemReader.setLinesToSkip(1);
		
		return itemReader;
	}
	
	@Bean
	public TransactionDataItemProcessor amazonTxnProcessor() {
		return new TransactionDataItemProcessor();
	}
	
	@Bean
	public JpaItemWriter<Transaction> amazonTxnWriter() {
		return TransactionParserUtils.transactionWriter(entityManagerFactory);
	}
	
}

