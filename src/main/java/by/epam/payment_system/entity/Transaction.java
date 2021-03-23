package by.epam.payment_system.entity;

import java.io.Serializable;

public class Transaction implements Serializable {

	private static final long serialVersionUID = 1L;

	private String transactionAccount;
	private String numberCard;
	private TransactionType typeTransaction;
	private int typeTransactionId;
	private String amount;
	private String currency;
	private int currencyId;
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

	public int getTypeTransactionId() {
		return typeTransaction.getId();
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((bankCode == null) ? 0 : bankCode.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + currencyId;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((numberCard == null) ? 0 : numberCard.hashCode());
		result = prime * result + ((purposePayment == null) ? 0 : purposePayment.hashCode());
		result = prime * result + ((senderOrRecipientAccount == null) ? 0 : senderOrRecipientAccount.hashCode());
		result = prime * result + ((transactionAccount == null) ? 0 : transactionAccount.hashCode());
		result = prime * result + ((typeTransaction == null) ? 0 : typeTransaction.hashCode());
		result = prime * result + typeTransactionId;
		result = prime * result + ((ynp == null) ? 0 : ynp.hashCode());
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
		Transaction other = (Transaction) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (bankCode == null) {
			if (other.bankCode != null)
				return false;
		} else if (!bankCode.equals(other.bankCode))
			return false;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (currencyId != other.currencyId)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (numberCard == null) {
			if (other.numberCard != null)
				return false;
		} else if (!numberCard.equals(other.numberCard))
			return false;
		if (purposePayment == null) {
			if (other.purposePayment != null)
				return false;
		} else if (!purposePayment.equals(other.purposePayment))
			return false;
		if (senderOrRecipientAccount == null) {
			if (other.senderOrRecipientAccount != null)
				return false;
		} else if (!senderOrRecipientAccount.equals(other.senderOrRecipientAccount))
			return false;
		if (transactionAccount == null) {
			if (other.transactionAccount != null)
				return false;
		} else if (!transactionAccount.equals(other.transactionAccount))
			return false;
		if (typeTransaction != other.typeTransaction)
			return false;
		if (typeTransactionId != other.typeTransactionId)
			return false;
		if (ynp == null) {
			if (other.ynp != null)
				return false;
		} else if (!ynp.equals(other.ynp))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Transaction [transactionAccount=" + transactionAccount + ", numberCard=" + numberCard
				+ ", typeTransaction=" + typeTransaction + ", typeTransactionId=" + typeTransactionId + ", amount="
				+ amount + ", currency=" + currency + ", currencyId=" + currencyId + ", bankCode=" + bankCode
				+ ", senderOrRecipientAccount=" + senderOrRecipientAccount + ", ynp=" + ynp + ", name=" + name
				+ ", purposePayment=" + purposePayment + "]";
	}

	
}
