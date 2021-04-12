package by.epam.payment_system.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Describes the entity CardInfo which contains data for opening new cards
 * 
 * @author Aleksandra Podgayskaya
 */
public class CardInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Card> cardList;
	private List<Currency> currencyList;
	private List<CardType> cardTypeList;

	public CardInfo() {

	}

	public List<Card> getCardList() {
		return Collections.unmodifiableList(cardList);
	}

	public void setCardList(List<Card> cardList) {
		this.cardList = cardList;
	}

	public List<Currency> getCurrencyList() {
		return Collections.unmodifiableList(currencyList);
	}

	public void setCurrencyList(List<Currency> currencyList) {
		this.currencyList = currencyList;
	}

	public List<CardType> getCardTypeList() {
		return Collections.unmodifiableList(cardTypeList);
	}

	public void setCardTypeList(List<CardType> cardTypeList) {
		this.cardTypeList = cardTypeList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cardList == null) ? 0 : cardList.hashCode());
		result = prime * result + ((cardTypeList == null) ? 0 : cardTypeList.hashCode());
		result = prime * result + ((currencyList == null) ? 0 : currencyList.hashCode());
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
		CardInfo other = (CardInfo) obj;
		if (cardList == null) {
			if (other.cardList != null)
				return false;
		} else if (!cardList.equals(other.cardList))
			return false;
		if (cardTypeList == null) {
			if (other.cardTypeList != null)
				return false;
		} else if (!cardTypeList.equals(other.cardTypeList))
			return false;
		if (currencyList == null) {
			if (other.currencyList != null)
				return false;
		} else if (!currencyList.equals(other.currencyList))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CardInfo [cardList=" + cardList + ", currencyList=" + currencyList + ", cardTypeList=" + cardTypeList
				+ "]";
	}

}
