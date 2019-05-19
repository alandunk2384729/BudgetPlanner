package com.budgetplanner.batch;

import static org.junit.Assert.assertEquals;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.batch.runtime.BatchStatus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.junit4.SpringRunner;

import com.budgetplanner.TestUtilities;
import com.budgetplanner.repository.TransactionRepository;
import com.budgetplanner.util.BudgetAppConstants;

@SpringBatchTest
@SpringBootTest
@RunWith(SpringRunner.class)
public class AmazonAndNatwestTxnProcessorTest {

	@Autowired
	public JobLauncherTestUtils jobLauncherTestUtils;
	
	@Autowired
	private TransactionRepository transactionRepo;
	
	@Before
	public void setUp() {
		TestUtilities.clearOutputDirectories();
		transactionRepo.deleteAll();
	}
	
	@Test
	public void testProcessAmazonAndNatwestTxns() throws Exception {
		final String testDirPath = "unittest/AmazonAndNatwestTxnProcessorTest";
		
		// Add the unit test files
		FileSystem fs = FileSystems.getDefault();
		Path natwestTxnsSource = fs.getPath(testDirPath, BudgetAppConstants.NATWEST_STATEMENT_FILE_NAME);
		Path natwestTxnsTarget = fs.getPath(BudgetAppConstants.STATEMENTS_DIR, BudgetAppConstants.NATWEST_STATEMENT_FILE_NAME);
		Files.copy(natwestTxnsSource, natwestTxnsTarget, StandardCopyOption.REPLACE_EXISTING);

		Path amazonTxnsSource = fs.getPath(testDirPath, BudgetAppConstants.AMAZON_STATEMENT_FILE_NAME);
		Path amazonTxnsTarget = fs.getPath(BudgetAppConstants.STATEMENTS_DIR, BudgetAppConstants.AMAZON_STATEMENT_FILE_NAME);
		Files.copy(amazonTxnsSource, amazonTxnsTarget, StandardCopyOption.REPLACE_EXISTING);
		
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		assertEquals(BatchStatus.COMPLETED.toString(), 
				jobExecution.getExitStatus().getExitCode());
		
		// verify output file contains the expected content
		AssertFile.assertFileEquals(new FileSystemResource(testDirPath + "/" + BudgetAppConstants.ALL_TRANSACTIONS_REPORT_NAME), 
				new FileSystemResource(BudgetAppConstants.REPORTS_DIR + "/" + 
						BudgetAppConstants.ALL_TRANSACTIONS_REPORT_NAME));
	}
	
}
