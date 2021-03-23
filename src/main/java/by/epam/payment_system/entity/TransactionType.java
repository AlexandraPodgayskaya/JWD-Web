package by.epam.payment_system.entity;

public enum TransactionType {

	RECEIPT(1), EXPENDITURE(2);

	private final int id;

	private TransactionType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
