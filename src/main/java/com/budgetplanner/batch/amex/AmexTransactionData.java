package com.budgetplanner.batch.amex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.budgetplanner.batch.TransactionData;
import com.budgetplanner.batch.TransactionProps;
import com.budgetplanner.entities.Transaction;

public class AmexTransactionData extends TransactionData {

	private static final Logger log = LoggerFactory.getLogger(AmexTransactionData.class);
	
	@Override
	public Transaction createTransaction(Transaction transaction, TransactionProps props) {
		if (this.getValue() > 0 && props.isSkipAmexPayments()) {
			log.debug("Skipping transaction: Amex credit card payment detected");
			return null;
		}
		
		return transaction;
	}

}

