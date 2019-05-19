package com.budgetplanner.batch;

import java.util.Date;

import com.budgetplanner.entities.Transaction;

public abstract class TransactionData {
	
	protected Date txnDate;
	
	protected String description;
	
	protected Double value;
	
	public TransactionData() {}
	
	public TransactionData(Date txnDate, String description, Double value) {
		this.txnDate = txnDate;
		this.description = description;
		this.value = value;
	}
	
	public Date getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(Date txnDate) {
		this.txnDate = txnDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
	public String toString() {
		return txnDate + "," + description + "," + value;
	}
	
	public abstract Transaction createTransaction(Transaction transaction, TransactionProps props);

}
