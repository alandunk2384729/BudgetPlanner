package com.budgetplanner.batch.amazon;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.budgetplanner.batch.TransactionData;
import com.budgetplanner.batch.TransactionProps;
import com.budgetplanner.entities.Transaction;

public class AmazonTransactionData extends TransactionData {

	private static final Logger log = LoggerFactory.getLogger(AmazonTransactionData.class);
	
	@Override
	public Transaction createTransaction(Transaction transaction, TransactionProps props) {
		if (this.getValue() > 0 && props.isSkipAmazonPayments()) {
			log.debug("Skipping transaction: Amazon credit card payment detected");
			return null;
		}
		
		return transaction;
	}

}
