package com.budgetplanner.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.budgetplanner.categorization.TransactionCategory;

@Entity
public class Transaction {

	@Id
	@GeneratedValue
	private Integer id;
	
	private String description;
	
	private Double value;
	
	@Temporal(TemporalType.DATE)
	private Date date;
	
	private TransactionCategory category;
	
	@ManyToOne
	private Account account;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	public TransactionCategory getCategory() {
		return category;
	}

	public void setCategory(TransactionCategory category) {
		this.category = category;
	}

	public String toString() {
		return "[" + id + "," + date + "," + description + "," + value + "," + account + "," 
				+ category.toString() + "]";
	}
	
}
