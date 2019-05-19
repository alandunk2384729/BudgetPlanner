package com.budgetplanner.batch.tesco;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.budgetplanner.batch.TransactionData;
import com.budgetplanner.batch.TransactionProps;
import com.budgetplanner.entities.Transaction;
import com.budgetplanner.util.BudgetAppConstants;

public class TescoTransactionData extends TransactionData {

	private static final Logger log = LoggerFactory.getLogger(TescoTransactionData.class);
	
	@Override
	public Transaction createTransaction(Transaction transaction, TransactionProps props) {
		if (this.getDescription().contains(BudgetAppConstants.AMAZON_CREDIT_CARD_TXN_ID)
				&& props.isSkipAmazonPayments()) {
			log.debug("Skipping transaction: Payment to Amazon credit card detected");
			return null;
		} else if (this.getDescription().contains(BudgetAppConstants.AMEX_CREDIT_CARD_TXN_ID) 
				&& props.isSkipAmexPayments()) {
			log.debug("Skipping transaction: Payment to Amex credit card detected");
			return null;
		}
		
		return transaction;
	}
	
}