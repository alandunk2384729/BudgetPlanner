package com.budgetplanner.batch;

public class TransactionProps {

	private boolean skipAmazonPayments;
	private boolean skipAmexPayments;
	
	public boolean isSkipAmazonPayments() {
		return skipAmazonPayments;
	}
	
	public void setSkipAmazonPayments(boolean skipAmazonPayments) {
		this.skipAmazonPayments = skipAmazonPayments;
	}
	
	public boolean isSkipAmexPayments() {
		return skipAmexPayments;
	}
	
	public void setSkipAmexPayments(boolean skipAmexPayments) {
		this.skipAmexPayments = skipAmexPayments;
	}
	
}
