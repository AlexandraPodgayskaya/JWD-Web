package by.epam.payment_system.entity;

import java.io.Serializable;

/**
 * Describes the entity CardType
 * 
 * @author Aleksandra Podgayskaya
 */
public class CardType implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String type;
	private String imagePath;

	public CardType() {

	}

	public CardType(Integer id) {
		this.id = id;
	}

	public CardType(String type, String imagePath) {
		this.type = type;
		this.imagePath = imagePath;
	}

	public CardType(Integer id, String type, String imagePath) {
		this.id = id;
		this.type = type;
		this.imagePath = imagePath;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((imagePath == null) ? 0 : imagePath.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		CardType other = (CardType) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imagePath == null) {
			if (other.imagePath != null)
				return false;
		} else if (!imagePath.equals(other.imagePath))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CardType [id=" + id + ", type=" + type + ", imagePath=" + imagePath + "]";
	}

}
