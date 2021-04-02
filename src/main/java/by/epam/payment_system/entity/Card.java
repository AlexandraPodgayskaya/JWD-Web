package by.epam.payment_system.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class Card implements Serializable {

	private static final long serialVersionUID = 1L;

	private String numberCard;
	private String numberAccount;
	private String typeCard;
	private String imagePath;
	private CardStatus status;
	private int ownerId;
	private boolean isBlocked;
	private boolean isClosed;
	private BigDecimal balance;
	private Currency currency;

	public Card() {

	}

	public Card(String numberCard, String numberAccount, String typeCard, String imagePath, CardStatus status, int ownerId, boolean isBlocked, boolean isClosed) {
		this.numberCard = numberCard;
		this.numberAccount = numberAccount;
		this.typeCard = typeCard;
		this.imagePath = imagePath;
		this.status = status;
		this.ownerId = ownerId;
		this.isBlocked = isBlocked;
		this.isClosed = isClosed;
	}
	
	public Card (String numberCard, boolean blocking ) {
		this.numberCard = numberCard;
		this.isBlocked = blocking;
	}

	public String getNumberCard() {
		return numberCard;
	}

	public void setNumberCard(String numberCard) {
		this.numberCard = numberCard;
	}

	public String getNumberAccount() {
		return numberAccount;
	}

	public void setNumberAccount(String numberAccount) {
		this.numberAccount = numberAccount;
	}

	public String getTypeCard() {
		return typeCard;
	}

	public void setTypeCard(String typeCard) {
		this.typeCard = typeCard;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public CardStatus getStatus() {
		return status;
	}

	public void setStatus(CardStatus status) {
		this.status = status;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public boolean getIsBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	public boolean getIsClosed() {
		return isClosed;
	}

	public void setClosed(boolean isClosed) {
		this.isClosed = isClosed;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((balance == null) ? 0 : balance.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((imagePath == null) ? 0 : imagePath.hashCode());
		result = prime * result + (isBlocked ? 1231 : 1237);
		result = prime * result + (isClosed ? 1231 : 1237);
		result = prime * result + ((numberAccount == null) ? 0 : numberAccount.hashCode());
		result = prime * result + ((numberCard == null) ? 0 : numberCard.hashCode());
		result = prime * result + ownerId;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((typeCard == null) ? 0 : typeCard.hashCode());
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
		Card other = (Card) obj;
		if (balance == null) {
			if (other.balance != null)
				return false;
		} else if (!balance.equals(other.balance))
			return false;
		if (currency != other.currency)
			return false;
		if (imagePath == null) {
			if (other.imagePath != null)
				return false;
		} else if (!imagePath.equals(other.imagePath))
			return false;
		if (isBlocked != other.isBlocked)
			return false;
		if (isClosed != other.isClosed)
			return false;
		if (numberAccount == null) {
			if (other.numberAccount != null)
				return false;
		} else if (!numberAccount.equals(other.numberAccount))
			return false;
		if (numberCard == null) {
			if (other.numberCard != null)
				return false;
		} else if (!numberCard.equals(other.numberCard))
			return false;
		if (ownerId != other.ownerId)
			return false;
		if (status != other.status)
			return false;
		if (typeCard == null) {
			if (other.typeCard != null)
				return false;
		} else if (!typeCard.equals(other.typeCard))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Card [numberCard=" + numberCard + ", numberAccount=" + numberAccount + ", typeCard=" + typeCard
				+ ", imagePath=" + imagePath + ", status=" + status + ", ownerId=" + ownerId + ", isBlocked="
				+ isBlocked + ", isClosed=" + isClosed + ", balance=" + balance + ", currency=" + currency + "]";
	}

	
}

