package com.budgetplanner.batch.amazon;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.budgetplanner.batch.TransactionData;

public class AmazonTransactionDataFieldSetMapper implements FieldSetMapper<TransactionData> {

	private static final Logger log = LoggerFactory.getLogger(AmazonTransactionDataFieldSetMapper.class);
	
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
	
	@Override
	public TransactionData mapFieldSet(FieldSet fieldSet) throws BindException {
		TransactionData txnData = new AmazonTransactionData();
		
		String txnDateString = fieldSet.readString(0);
		try {
			txnData.setTxnDate(dateFormatter.parse(txnDateString));
		} catch (ParseException e) {
			log.debug("ParseException caught when processing transaction date field, txnDateString = " + txnDateString);
			if (txnDateString.equalsIgnoreCase("Pending")) {
				// As txnDate is Pending, the result should be null here so we can continue
				txnData.setTxnDate(null);
			} else {
				log.error("Unexpected ParseException: ", e);
				throw new RuntimeException(e);
			}
		}
		
		txnData.setDescription(fieldSet.readString(1));
		
		Double negativeValue = -Double.parseDouble(fieldSet.readString(2));
		txnData.setValue(negativeValue);
		
		return txnData;
	}
	
}
