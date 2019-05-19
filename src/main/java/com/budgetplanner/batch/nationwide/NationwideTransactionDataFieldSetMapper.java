package com.budgetplanner.batch.nationwide;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.budgetplanner.batch.TransactionData;

public class NationwideTransactionDataFieldSetMapper implements FieldSetMapper<TransactionData> {

	private static final Logger log = LoggerFactory.getLogger(NationwideTransactionDataFieldSetMapper.class);

	private SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
	
	@Override
	public TransactionData mapFieldSet(FieldSet fieldSet) throws BindException {
		TransactionData txnData = new NationwideTransactionData();
		
		try {
			txnData.setTxnDate(formatter.parse(fieldSet.readString("txnDate")));
		} catch (ParseException e) {
			String msg = "ParseException occurred unexpectedly when processing Date";
			log.error(msg);
			throw new RuntimeException(msg, e);
		}
		
		txnData.setDescription(fieldSet.readString("description"));
		
		String credit = fieldSet.readString("credit");
		if (credit != null && !credit.isEmpty()) {
			double currencyValue = parseCurrency(fieldSet.readString("credit"));
			txnData.setValue(currencyValue);
		} else {
			Double negativeValue = -parseCurrency(fieldSet.readString("debit"));
			txnData.setValue(negativeValue);
		}
		
		return txnData;
	}
	
	private double parseCurrency(String currencyString) {
		try {
			NumberFormat formatter = NumberFormat.getCurrencyInstance();
			return formatter.parse(currencyString).doubleValue();
		} catch (ParseException e) {
			String msg = "ParseException occurred unexpected when parsing currency: " + currencyString;
			log.error(msg);
			throw new RuntimeException(msg, e);
		}
	}
	
}