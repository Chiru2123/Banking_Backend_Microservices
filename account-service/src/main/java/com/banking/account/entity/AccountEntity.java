package com.banking.account.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "account_accounts")
@Data
public class AccountEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Long customerId;
    private String accountNumber;
    private String type;
    private BigDecimal balance = BigDecimal.ZERO;
    private String holdStatus;
    private String loan;
    private String acType;
    private String passbook;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getHoldStatus() {
		return holdStatus;
	}
	public void setHoldStatus(String holdStatus) {
		this.holdStatus = holdStatus;
	}
	public String getLoan() {
		return loan;
	}
	public void setLoan(String loan) {
		this.loan = loan;
	}
	public String getAcType() {
		return acType;
	}
	public void setAcType(String acType) {
		this.acType = acType;
	}
	public String getPassbook() {
		return passbook;
	}
	public void setPassbook(String passbook) {
		this.passbook = passbook;
	}
    
    
}