package by.epam.payment_system.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Describes the entity Account
 * 
 * @author Aleksandra Podgayskaya
 */
public class Account implements Serializable {

	private static final long serialVersionUID = 1L;

	private String numberAccount;
	private BigDecimal balance;
	private Currency currency;
	private Integer ownerId;

	public Account() {
	}

	public Account(String numberAccount, Currency currency, Integer ownerId) {
		this.numberAccount = numberAccount;
		this.currency = currency;
		this.ownerId = ownerId;
	}

	public Account(String numberAccount, BigDecimal balance, Currency currency, Integer ownerId) {
		this.numberAccount = numberAccount;
		this.balance = balance;
		this.currency = currency;
		this.ownerId = ownerId;
	}

	public String getNumberAccount() {
		return numberAccount;
	}

	public void setNumberAccount(String numberAccount) {
		this.numberAccount = numberAccount;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((balance == null) ? 0 : balance.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((numberAccount == null) ? 0 : numberAccount.hashCode());
		result = prime * result + ((ownerId == null) ? 0 : ownerId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (balance == null) {
			if (other.balance != null)
				return false;
		} else if (!balance.equals(other.balance))
			return false;
		if (currency != other.currency)
			return false;
		if (numberAccount == null) {
			if (other.numberAccount != null)
				return false;
		} else if (!numberAccount.equals(other.numberAccount))
			return false;
		if (ownerId == null) {
			if (other.ownerId != null)
				return false;
		} else if (!ownerId.equals(other.ownerId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Account [numberAccount=" + numberAccount + ", balance=" + balance + ", currency=" + currency
				+ ", ownerId=" + ownerId + "]";
	}

}
