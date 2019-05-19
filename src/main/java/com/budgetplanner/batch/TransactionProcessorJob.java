package com.budgetplanner.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.budgetplanner.batch.amazon.AmazonTransactionParser;
import com.budgetplanner.batch.amex.AmexTransactionParser;
import com.budgetplanner.batch.chart.BarChartCreatorTasklet;
import com.budgetplanner.batch.chart.PieChartCreatorTasklet;
import com.budgetplanner.batch.nationwide.NationwideTransactionParser;
import com.budgetplanner.batch.natwest.NatwestTransactionParser;
import com.budgetplanner.batch.tesco.TescoTransactionParser;
import com.budgetplanner.entities.Transaction;

@Configuration
@EnableBatchProcessing
public class TransactionProcessorJob {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private AmazonTransactionParser amazonTransactionParser;
	
	@Autowired
	private NatwestTransactionParser natwestTransactionParser;

	@Autowired
	private AmexTransactionParser amexTransactionParser;
	
	@Autowired
	private NationwideTransactionParser nationwideTransactionParser;
	
	@Autowired
	private TescoTransactionParser tescoTransactionParser;
	
	@Autowired
	private TransactionEntityToCsvPrinter txnEntityToCsvPrinter;
	
	@Autowired
	private BarChartCreatorTasklet barChartCreatorTasklet;
	
	@Autowired
	private PieChartCreatorTasklet expensesPieChartTasklet;
	
	@Bean
	public Job importTxnJob(@Qualifier("amazonTxnImportStep") Step amazonTxnImportStep,
			@Qualifier("natwestTxnImportStep") Step natwestTxnsImportStep,
			@Qualifier("amexTxnImportStep") Step amexTxnImportStep,
			@Qualifier("nationwideTxnImportStep") Step nationwideTxnImportStep,
			@Qualifier("tescoTxnImportStep") Step tescoTxnImportStep,
			@Qualifier("printTxnsStep") Step printTxnsStep,
			@Qualifier("createIncomeExpensesChart") Step createIncomeExpensesChart,
			@Qualifier("createExpensesPieChart") Step createExpensesPieChart) {
		return jobBuilderFactory.get("processTxnsJob")
			.incrementer(new RunIdIncrementer())
			.start(amazonTxnImportStep)
			.next(natwestTxnsImportStep)
			.next(amexTxnImportStep)
			.next(nationwideTxnImportStep)
			.next(tescoTxnImportStep)
			.next(printTxnsStep)
			.next(createIncomeExpensesChart)
			.next(createExpensesPieChart)
			.build();
	}
		
	@Bean
	public Step amazonTxnImportStep() {
		return stepBuilderFactory.get("amazonTxnImportStep")
			.<TransactionData,Transaction> chunk(100)
			.reader(amazonTransactionParser.amazonTxnReader())
			.processor(amazonTransactionParser.amazonTxnProcessor())
			.writer(amazonTransactionParser.amazonTxnWriter())
			.build();
	}
	
	@Bean
	public Step natwestTxnImportStep() {
		return stepBuilderFactory.get("natwestTxnImportStep")
			.<TransactionData,Transaction> chunk(100)
			.reader(natwestTransactionParser.nwTxnReader())
			.processor(natwestTransactionParser.nwTxnProcessor())
			.writer(natwestTransactionParser.nwTxnWriter())
			.build();
	}
	
	@Bean
	public Step amexTxnImportStep() {
		return stepBuilderFactory.get("amexTxnImportStep")
			.<TransactionData,Transaction> chunk(100)
			.reader(amexTransactionParser.amexTxnReader())
			.processor(amexTransactionParser.amexTxnProcessor())
			.writer(amexTransactionParser.amexTxnWriter())
			.build();
	}
	
	@Bean
	public Step nationwideTxnImportStep() {
		return stepBuilderFactory.get("nationwideTxnImportStep")
			.<TransactionData,Transaction> chunk(100)
			.reader(nationwideTransactionParser.nationwideTxnReader())
			.processor(nationwideTransactionParser.nationwideTxnProcessor())
			.writer(nationwideTransactionParser.nationwideTxnWriter())
			.build();
	}
	
	@Bean
	public Step tescoTxnImportStep() {
		return stepBuilderFactory.get("tescoTxnImportStep")
			.<TransactionData,Transaction> chunk(100)
			.reader(tescoTransactionParser.tescoTxnReader())
			.processor(tescoTransactionParser.tescoTxnProcessor())
			.writer(tescoTransactionParser.tescoTxnWriter())
			.build();
	}
	
	@Bean
	public Step printTxnsStep() {
		return stepBuilderFactory.get("printTxnsStep")
			.<Transaction, Transaction> chunk(100)
			.reader(txnEntityToCsvPrinter.reader())
			.processor(txnEntityToCsvPrinter.processor())
			.writer(txnEntityToCsvPrinter.writer())
			.build();
	}
	
	@Bean 
	public Step createIncomeExpensesChart() {
		return stepBuilderFactory.get("createIncomeExpensesChart")
			.tasklet(barChartCreatorTasklet)
			.build();
	}
	
	@Bean
	public Step createExpensesPieChart() {
		return stepBuilderFactory.get("createExpensesPieChart")
			.tasklet(expensesPieChartTasklet)
			.build();
	}
	
}