package com.budgetplanner.batch.chart;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.budgetplanner.entities.Transaction;
import com.budgetplanner.repository.TransactionRepository;
import com.budgetplanner.util.BudgetAppConstants;

@Configuration
public class BarChartCreatorTasklet implements Tasklet {

	@Autowired
	private TransactionRepository transactionRepo;
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// Create an Income and Expenditure chart as a PNG file
		JFreeChart barChart = ChartFactory.createBarChart("Income and Expenditure", 
				"Income and Expenditure", "Amount", createDataset(), PlotOrientation.VERTICAL, false, false, false);
		BufferedImage bufferedChartImage = barChart.createBufferedImage(800, 600);
		try (FileOutputStream fos = new FileOutputStream(BudgetAppConstants.REPORTS_DIR + 
				"/incomeAndExpenditure.png")) {
			EncoderUtil.writeBufferedImage(bufferedChartImage, "png", fos);
		}
		
		return RepeatStatus.FINISHED;
	}
	
	private CategoryDataset createDataset() {
		final String incomeCategory = "Income";
		final String expenditureCategory = "Expenditure";
		
		final String valueBar = "Amount";
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(0.0, valueBar, incomeCategory);
		dataset.addValue(0.0, valueBar, expenditureCategory);
		
		for (Transaction transaction : transactionRepo.findAll()) {
			if (transaction.getValue() > 0) {
				dataset.incrementValue(transaction.getValue(), valueBar, incomeCategory);
			} else {
				dataset.incrementValue(-transaction.getValue(), valueBar, expenditureCategory);
			}
		}
			
		return dataset;
	}
	
}
