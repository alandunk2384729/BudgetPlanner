package com.budgetplanner.batch;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:/transaction.properties")
public class TransactionPropertyHelperService {

	@Autowired
	private Environment env;
	
	/**
	 * @return the value of skip.amazon.payments. If the property is not set, the default 
	 * value of skip.amazon.payments=true
	 */
	public boolean skipAmazonPayments() {
		final String skipPaymentsPropKey = "skip.amazon.payments";
		if (env.containsProperty(skipPaymentsPropKey)) {
			return Boolean.parseBoolean(env.getProperty(skipPaymentsPropKey));
		} else {
			return true;
		}
	}
	
	public boolean skipAmexPayments() {
		final String skipAmexPropKey = "skip.amex.payments";
		if (env.containsProperty(skipAmexPropKey)) {
			return Boolean.parseBoolean(env.getProperty(skipAmexPropKey));
		} else {
			return true;
		}
	}
	
	public TransactionProps getTransactionProps() {
		TransactionProps transactionProps = new TransactionProps();
		transactionProps.setSkipAmazonPayments(skipAmazonPayments());
		transactionProps.setSkipAmexPayments(skipAmexPayments());
		return transactionProps;
	}
	
}
