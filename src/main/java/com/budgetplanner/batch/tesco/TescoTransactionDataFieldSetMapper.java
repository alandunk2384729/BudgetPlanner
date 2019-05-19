package com.budgetplanner.batch.tesco;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.budgetplanner.batch.TransactionData;

public class TescoTransactionDataFieldSetMapper implements FieldSetMapper<TransactionData> {

	private static final Logger log = 
			LoggerFactory.getLogger(TescoTransactionDataFieldSetMapper.class);
	
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
	
	@Override
	public TransactionData mapFieldSet(FieldSet fieldSet) throws BindException {
		TransactionData txnData = new TescoTransactionData();
		
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
			txnData.setValue(Double.parseDouble(credit));
		} else {
			double negativeValue = Double.parseDouble(fieldSet.readString("debit"));
			txnData.setValue(negativeValue);
		}
		
		return txnData;
	}
	
}