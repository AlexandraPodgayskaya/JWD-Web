package by.epam.payment_system.entity;

import java.io.Serializable;

public class Transfer implements Serializable {

	private static final long serialVersionUID = 1L;

	private String senderAccountNumber;
	private String recipientAccountNumber;
	private String amount;

	public Transfer() {

	}

	public Transfer(String senderAccountNumber, String recipientAccountNumber, String amount) {
		this.senderAccountNumber = senderAccountNumber;
		this.recipientAccountNumber = recipientAccountNumber;
		this.amount = amount;
	}

	public String getSenderAccountNumber() {
		return senderAccountNumber;
	}

	public void setSenderAccountNumber(String senderAccountNumber) {
		this.senderAccountNumber = senderAccountNumber;
	}

	public String getRecipientAccountNumber() {
		return recipientAccountNumber;
	}

	public void setRecipientAccountNumber(String recipientAccountNumber) {
		this.recipientAccountNumber = recipientAccountNumber;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((recipientAccountNumber == null) ? 0 : recipientAccountNumber.hashCode());
		result = prime * result + ((senderAccountNumber == null) ? 0 : senderAccountNumber.hashCode());
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
		Transfer other = (Transfer) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (recipientAccountNumber == null) {
			if (other.recipientAccountNumber != null)
				return false;
		} else if (!recipientAccountNumber.equals(other.recipientAccountNumber))
			return false;
		if (senderAccountNumber == null) {
			if (other.senderAccountNumber != null)
				return false;
		} else if (!senderAccountNumber.equals(other.senderAccountNumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Transfer [senderAccountNumber=" + senderAccountNumber + ", recipientAccountNumber="
				+ recipientAccountNumber + ", amount=" + amount + "]";
	}

}
