package com.budgetplanner.batch.chart;


import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.budgetplanner.categorization.TransactionCategory;
import com.budgetplanner.entities.Transaction;
import com.budgetplanner.repository.TransactionRepository;
import com.budgetplanner.util.BudgetAppConstants;

@Configuration
public class PieChartCreatorTasklet implements Tasklet {

	private static final Logger log = LoggerFactory.getLogger(PieChartCreatorTasklet.class);
	
	@Autowired
	private TransactionRepository transactionRepo;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		log.debug("inside execute(contribution, chunkContext)");
		
		// Create a Costs Pie Chart as a PNG file
		JFreeChart pieChart = ChartFactory.createPieChart("Expenses", 
				createDataset(), false, true, false);
		BufferedImage bufferedChartImage = pieChart.createBufferedImage(800, 600);
		try (FileOutputStream fos = new FileOutputStream(BudgetAppConstants.REPORTS_DIR + 
				"/expensesPieChart.png")) {
			EncoderUtil.writeBufferedImage(bufferedChartImage, "png", fos);
		}
		
		return RepeatStatus.FINISHED;
	}

	private PieDataset createDataset() {
		log.debug("inside createDataset()");
		
		DefaultPieDataset dataset = new DefaultPieDataset();
		
		// Add dataset values
		for (Transaction transaction : transactionRepo.findByValueLessThan(Double.parseDouble("0.0"))) {
			TransactionCategory txnCategory = transaction.getCategory();
			
			log.debug("Adding transaction to dataset: " + transaction.toString() + " for Category " + txnCategory);
			try {
				double value = -transaction.getValue() + dataset.getValue(txnCategory.toString()).doubleValue();
				log.debug("Category value being set to " + value);
				dataset.setValue(txnCategory.toString(), value);
			} catch (UnknownKeyException e) {
				// If the key is not contained in the data set, set the value for the 
				// first time here
				double value = -transaction.getValue();
				log.debug("Category being created, with value set to " + value);
				dataset.setValue(txnCategory.toString(), value);
			}
		}
		
		return dataset;
	}
	
}
