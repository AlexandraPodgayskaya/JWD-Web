package by.epam.payment_system.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Describes the entity Card
 * 
 * @author Aleksandra Podgayskaya
 */
public class Card implements Serializable {

	private static final long serialVersionUID = 1L;

	private String numberCard;
	private String numberAccount;
	private CardType cardType;
	private CardStatus status;
	private Long ownerId;
	private boolean blocked;
	private boolean closed;
	private BigDecimal balance;
	private Currency currency;

	public Card() {

	}

	public Card(String numberCard, String numberAccount, CardType cardType, CardStatus status, Long ownerId,
			boolean blocked, boolean closed) {
		this(numberCard, numberAccount, cardType, status, ownerId);
		this.blocked = blocked;
		this.closed = closed;
	}

	public Card(String numberCard, String numberAccount, CardType cardType, CardStatus status, Long ownerId) {
		this.numberCard = numberCard;
		this.numberAccount = numberAccount;
		this.cardType = cardType;
		this.status = status;
		this.ownerId = ownerId;
	}

	public Card(String numberCard, boolean blocking) {
		this.numberCard = numberCard;
		this.blocked = blocking;
	}

	public Card(CardType cardType, CardStatus status, long ownerId, Currency currency) {
		this.cardType = cardType;
		this.status = status;
		this.ownerId = ownerId;
		this.currency = currency;
	}

	public Card(String numberAccount, CardType cardType, CardStatus status) {
		this.numberAccount = numberAccount;
		this.cardType = cardType;
		this.status = status;
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

	public CardType getCardType() {
		return cardType;
	}

	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}

	public CardStatus getStatus() {
		return status;
	}

	public void setStatus(CardStatus status) {
		this.status = status;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.blocked = isBlocked;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean isClosed) {
		this.closed = isClosed;
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
		result = prime * result + (blocked ? 1231 : 1237);
		result = prime * result + ((cardType == null) ? 0 : cardType.hashCode());
		result = prime * result + (closed ? 1231 : 1237);
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((numberAccount == null) ? 0 : numberAccount.hashCode());
		result = prime * result + ((numberCard == null) ? 0 : numberCard.hashCode());
		result = prime * result + ((ownerId == null) ? 0 : ownerId.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		if (blocked != other.blocked)
			return false;
		if (cardType == null) {
			if (other.cardType != null)
				return false;
		} else if (!cardType.equals(other.cardType))
			return false;
		if (closed != other.closed)
			return false;
		if (currency != other.currency)
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
		if (ownerId == null) {
			if (other.ownerId != null)
				return false;
		} else if (!ownerId.equals(other.ownerId))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Card [numberCard=" + numberCard + ", numberAccount=" + numberAccount + ", cardType=" + cardType
				+ ", status=" + status + ", ownerId=" + ownerId + ", blocked=" + blocked + ", closed=" + closed
				+ ", balance=" + balance + ", currency=" + currency + "]";
	}
}
