package com.budgetplanner.batch.firstdirect;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.budgetplanner.batch.TransactionData;

public class FirstDirectTransactionDataFieldSetMapper implements FieldSetMapper<TransactionData> {

	private static final Logger log = LoggerFactory.getLogger(FirstDirectTransactionDataFieldSetMapper.class);
	
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	
	@Override
	public TransactionData mapFieldSet(FieldSet fieldSet) throws BindException {
		TransactionData txnData = new FirstDirectTransactionData();
		
		try {
			txnData.setTxnDate(formatter.parse(fieldSet.readString("txnDate")));
		} catch (ParseException e) {
			String msg = "ParseException occurred unexpectedly when processing Date";
			log.error(msg);
			throw new RuntimeException(msg, e);
		}
		
		txnData.setDescription(fieldSet.readString("description"));
		txnData.setValue(Double.parseDouble(fieldSet.readString("amount")));
		
		return txnData;
	}
	
}
