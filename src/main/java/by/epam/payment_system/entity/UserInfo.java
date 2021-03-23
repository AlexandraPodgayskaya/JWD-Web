package by.epam.payment_system.entity;

import java.io.Serializable;

public class UserInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String login;
	private String password;
	private String surname;
	private String name;
	private String patronymic;
	private String dateBirth;
	private String personalNumberPassport;
	private String phone;
	private int typeUserId;

	public UserInfo() {

	}

	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPatronymic() {
		return patronymic;
	}

	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}

	public String getDateBirth() {
		return dateBirth;
	}

	public void setDateBirth(String dateBirth) {
		this.dateBirth = dateBirth;
	}

	public String getPersonalNumberPassport() {
		return personalNumberPassport;
	}

	public void setPersonalNumberPassport(String personalNumberPassport) {
		this.personalNumberPassport = personalNumberPassport;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getTypeUserId() {
		return typeUserId;
	}

	public void setTypeUserId(int typeUserId) {
		this.typeUserId = typeUserId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateBirth == null) ? 0 : dateBirth.hashCode());
		result = prime * result + id;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((patronymic == null) ? 0 : patronymic.hashCode());
		result = prime * result + ((personalNumberPassport == null) ? 0 : personalNumberPassport.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
		result = prime * result + typeUserId;
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
		UserInfo other = (UserInfo) obj;
		if (dateBirth == null) {
			if (other.dateBirth != null)
				return false;
		} else if (!dateBirth.equals(other.dateBirth))
			return false;
		if (id != other.id)
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (patronymic == null) {
			if (other.patronymic != null)
				return false;
		} else if (!patronymic.equals(other.patronymic))
			return false;
		if (personalNumberPassport == null) {
			if (other.personalNumberPassport != null)
				return false;
		} else if (!personalNumberPassport.equals(other.personalNumberPassport))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		if (typeUserId != other.typeUserId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", login=" + login + ", password=" + password + ", surname=" + surname + ", name="
				+ name + ", patronymic=" + patronymic + ", dateBirth=" + dateBirth + ", personalNumberPassport="
				+ personalNumberPassport + ", phone=" + phone + ", typeUserId=" + typeUserId + "]";
	}

	
	
}
