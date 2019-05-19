package com.budgetplanner.batch;

import java.io.IOException;
import java.io.Writer;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.budgetplanner.entities.Transaction;
import com.budgetplanner.util.BudgetAppConstants;

@Configuration
public class TransactionEntityToCsvPrinter {

	private static final Logger log = LoggerFactory.getLogger(TransactionEntityToCsvPrinter.class);
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Bean
	public JpaPagingItemReader<Transaction> reader() {
		log.debug("inside reader()");
		return new JpaPagingItemReaderBuilder<Transaction>()
			.name("transactionEntityReader")
			.entityManagerFactory(entityManagerFactory)
			.queryString("select t from Transaction t order by t.date desc, t.id")
			.pageSize(100)
			.build();
	}
	
	@Bean
	public TransactionItemPrinter processor() {
		log.debug("inside processor()");
		return new TransactionItemPrinter();
	}
	
	@Bean
	public FlatFileItemWriter<Transaction> writer() {
		log.debug("inside writer()");
		
		String[] fieldNames = { "date", "description", "value", "category" };
		
		return new FlatFileItemWriterBuilder<Transaction>()
			.name("txnEntityToCsvWriter")
			.resource(new FileSystemResource(BudgetAppConstants.REPORTS_DIR + "/" + 
					BudgetAppConstants.ALL_TRANSACTIONS_REPORT_NAME))
			.headerCallback(new TransactionPrinterHeaderCallback())
			.delimited()
			.delimiter(",")
			.names(fieldNames)
			.build();
	}
	
	private class TransactionItemPrinter implements ItemProcessor<Transaction, Transaction> {
		@Override
		public Transaction process(Transaction item) throws Exception {
			return item;
		}
	}
	
	private class TransactionPrinterHeaderCallback implements FlatFileHeaderCallback {
		@Override
		public void writeHeader(Writer writer) throws IOException {
			// Print header
			writer.write("Date,Description,Amount,Category");
		}
	}
	
}
