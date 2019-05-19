package com.budgetplanner.categorization;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TxnCategorizationUtilTest {

	@Test
	public void testGetCategory() {
		TxnCategorizationUtil categorizationUtil = TxnCategorizationUtil.getInstance();
		
		assertEquals("Unexpected Transaction Category", TransactionCategory.MISCELLANEOUS, 
				categorizationUtil.getCategory("DUFF TRANSACTION"));
		
		assertEquals("Unexpected Transaction Category", TransactionCategory.FOOD_AND_DRINK, 
				categorizationUtil.getCategory("ASDA"));
		assertEquals("Unexpected Transaction Category", TransactionCategory.FOOD_AND_DRINK, 
				categorizationUtil.getCategory("GREGGS"));
		assertEquals("Unexpected Transaction Category", TransactionCategory.FOOD_AND_DRINK, 
				categorizationUtil.getCategory("LIDL"));
		
		assertEquals("Unexpected Transaction Category", TransactionCategory.HOME, 
				categorizationUtil.getCategory("GIFFGAFF"));
		assertEquals("Unexpected Transaction Category", TransactionCategory.HOME, 
				categorizationUtil.getCategory("SOUTH LANARKSHIRE"));
		
		assertEquals("Unexpected Transaction Category", TransactionCategory.FUN, 
				categorizationUtil.getCategory("AMAZON.CO.UK"));
		assertEquals("Unexpected Transaction Category", TransactionCategory.FUN, 
				categorizationUtil.getCategory("GLASGOW SCIENCE CENTRE"));
		
		assertEquals("Unexpected Transaction Category", TransactionCategory.INSURANCE, 
				categorizationUtil.getCategory("COVERFORYOU.COM"));
		
		assertEquals("Unexpected Transaction Category", TransactionCategory.COMMUTING, 
				categorizationUtil.getCategory("KINGSWAY M O T"));
		
		assertEquals("Unexpected Transaction Category", TransactionCategory.CHARITY, 
				categorizationUtil.getCategory("JUSTGIVING"));
		assertEquals("Unexpected Transaction Category", TransactionCategory.CHARITY, 
				categorizationUtil.getCategory("WORLD VISION"));
		
		assertEquals("Unexpected Transaction Category", TransactionCategory.CHARITY,
				categorizationUtil.getCategory("\"'WORLD VISION DONOR\""));
	}
	
}
