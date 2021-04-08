package by.epam.payment_system.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class CardInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Card> cardList;
	private List<CardStatus> cardStatusList;
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

	public List<CardStatus> getCardStatusList() {
		return Collections.unmodifiableList(cardStatusList);
	}

	public void setCardStatusList(List<CardStatus> cardStatusList) {
		this.cardStatusList = cardStatusList;
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
		result = prime * result + ((cardStatusList == null) ? 0 : cardStatusList.hashCode());
		result = prime * result + ((cardTypeList == null) ? 0 : cardTypeList.hashCode());
		result = prime * result + ((currencyList == null) ? 0 : currencyList.hashCode());
		result = prime * result + ((cardList == null) ? 0 : cardList.hashCode());
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
		if (cardStatusList == null) {
			if (other.cardStatusList != null)
				return false;
		} else if (!cardStatusList.equals(other.cardStatusList))
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
		if (cardList == null) {
			if (other.cardList != null)
				return false;
		} else if (!cardList.equals(other.cardList))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CardInfo [mainCardList=" + cardList + ", cardStatusList=" + cardStatusList + ", currencyList="
				+ currencyList + ", cardTypeList=" + cardTypeList + "]";
	}

}
