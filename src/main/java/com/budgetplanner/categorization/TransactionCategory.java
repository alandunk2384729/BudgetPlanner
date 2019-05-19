package com.budgetplanner.categorization;

public enum TransactionCategory {
	HOME("Home"),
	INSURANCE("Insurance"),
	FOOD_AND_DRINK("Food and Drink"),
	COMMUTING("Commuting"),
	LOAN_REPAYMENT("Loan Repayment"),
	SAVINGS_AND_INVESTMENTS("Savings and Investments"),
	FAMILY("Family"),
	FUN("Fun"),
	HEALTH_AND_BEAUTY("Health and Beauty"),
	CLOTHES("Clothes and Fashon"),
	EDUCATION("Education"),
	BIG_ONE_OFFS("Big one offs"),
	MISCELLANEOUS("Miscellaneous"),
	CHARITY("Charity");
	
	private String displayName;
	private TransactionCategory(String displayName) {
		this.displayName = displayName;
	}
	
	public String toString() {
		return displayName;
	}
}
