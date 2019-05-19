package com.budgetplanner;

import java.io.File;

import org.assertj.core.util.Files;

import com.budgetplanner.util.BudgetAppConstants;

public class TestUtilities {

	public static void clearOutputDirectories() {
		for (String fileName : Files.fileNamesIn(BudgetAppConstants.REPORTS_DIR, false)) {
			Files.delete(new File(fileName));
		}
		
		for (String fileName : Files.fileNamesIn(BudgetAppConstants.STATEMENTS_DIR, false)) {
			Files.delete(new File(fileName));
		}
	}
	
}
