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
public class AmexAndNatwestTxnProcessorTest {

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
	public void testProcessAmexAndNatwestTxns() throws Exception {
		final String testDirPath = "unittest/AmexAndNatwestTxnProcessorTest";
		
		// Add the unit test files
		FileSystem fs = FileSystems.getDefault();
		Path natwestTxnsSource = fs.getPath(testDirPath, BudgetAppConstants.NATWEST_STATEMENT_FILE_NAME);
		Path natwestTxnsTarget = fs.getPath(BudgetAppConstants.STATEMENTS_DIR, BudgetAppConstants.NATWEST_STATEMENT_FILE_NAME);
		Files.copy(natwestTxnsSource, natwestTxnsTarget, StandardCopyOption.REPLACE_EXISTING);
		
		Path amexTxnsSource = fs.getPath(testDirPath, BudgetAppConstants.AMEX_STATEMENT_FILE_NAME);
		Path amexTxnsTarget = fs.getPath(BudgetAppConstants.STATEMENTS_DIR, BudgetAppConstants.AMEX_STATEMENT_FILE_NAME);
		Files.copy(amexTxnsSource, amexTxnsTarget, StandardCopyOption.REPLACE_EXISTING);
		
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		assertEquals(BatchStatus.COMPLETED.toString(), 
				jobExecution.getExitStatus().getExitCode());
		
		// verify output file contains the expected content
		AssertFile.assertFileEquals(new FileSystemResource(testDirPath + "/" + BudgetAppConstants.ALL_TRANSACTIONS_REPORT_NAME), 
				new FileSystemResource(BudgetAppConstants.REPORTS_DIR + "/" + 
						BudgetAppConstants.ALL_TRANSACTIONS_REPORT_NAME));
	}
	
}