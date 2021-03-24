package by.epam.payment_system.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Transaction implements Serializable {

	private static final long serialVersionUID = 1L;

	private String transactionAccount;
	private String numberCard;
	private TransactionType typeTransaction;
	private String amount;
	private String currency;
	private int currencyId;
	private Timestamp dateTime;
	private String bankCode;
	private String senderOrRecipientAccount;
	private String ynp;
	private String name;
	private String purposePayment;

	public Transaction(String transactionAccount, String numberCard, TransactionType typeTransaction, String amount,
			int currencyId, String senderOrRecipientAccount, String purposePayment) {
		this.transactionAccount = transactionAccount;
		this.numberCard = numberCard;
		this.typeTransaction = typeTransaction;
		this.amount = amount;
		this.currencyId = currencyId;
		this.senderOrRecipientAccount = senderOrRecipientAccount;
		this.purposePayment = purposePayment;
	}

	public String getTransactionAccount() {
		return transactionAccount;
	}

	public void setTransactionAccount(String transactionAccount) {
		this.transactionAccount = transactionAccount;
	}

	public String getNumberCard() {
		return numberCard;
	}

	public void setNumberCard(String numberCard) {
		this.numberCard = numberCard;
	}

	public TransactionType getTypeTransaction() {
		return typeTransaction;
	}

	public void setTypeTransaction(TransactionType typeTransaction) {
		this.typeTransaction = typeTransaction;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public int getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}

	public Timestamp getDateTime() {
		return dateTime;
	}

	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getSenderOrRecipientAccount() {
		return senderOrRecipientAccount;
	}

	public void setSenderOrRecipientAccount(String senderOrRecipientAccount) {
		this.senderOrRecipientAccount = senderOrRecipientAccount;
	}

	public String getYnp() {
		return ynp;
	}

	public void setYnp(String ynp) {
		this.ynp = ynp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPurposePayment() {
		return purposePayment;
	}

	public void setPurposePayment(String purposePayment) {
		this.purposePayment = purposePayment;
	}

}
