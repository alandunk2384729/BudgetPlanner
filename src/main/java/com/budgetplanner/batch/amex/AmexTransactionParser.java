package com.budgetplanner.batch.amex;

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
public class AmexTransactionParser {

	private static final String AMEX_TXN_FILE_PATH = BudgetAppConstants.STATEMENTS_DIR + "/"
			+ BudgetAppConstants.AMEX_STATEMENT_FILE_NAME;

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Bean
	public FlatFileItemReader<TransactionData> amexTxnReader() {
		FlatFileItemReader<TransactionData> itemReader = new FlatFileItemReaderBuilder<TransactionData>()
			.name("amexTxnReader")
			.resource(new FileSystemResource(AMEX_TXN_FILE_PATH))
			.delimited()
			.names(new String[] { "txnDate", "reference", "value", "description", "processDate", "end" })
			.fieldSetMapper(new AmexTransactionDataFieldSetMapper())
			.build();
		
		// Allow no AMEX CSV file to be present in the statements directory
		itemReader.setStrict(false);
		
		return itemReader;
	}
	
	@Bean
	public TransactionDataItemProcessor amexTxnProcessor() {
		return new TransactionDataItemProcessor();
	}
	
	@Bean
	public JpaItemWriter<Transaction> amexTxnWriter() {
		return TransactionParserUtils.transactionWriter(entityManagerFactory);
	}
	
}