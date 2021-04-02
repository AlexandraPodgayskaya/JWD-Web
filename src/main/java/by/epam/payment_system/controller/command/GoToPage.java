package by.epam.payment_system.controller.command;

public final class GoToPage {

	public static final String INDEX_PAGE = "index.jsp";
	public static final String ERROR_PAGE = "error.jsp";
	public static final String MAIN_PAGE = "Controller?command=go_to_main_page";
	public static final String PAYMENT_PAGE = "Controller?command=go_to_payment_page";
	public static final String TOP_UP_CARD_PAGE = "Controller?command=go_to_top_up_card_page";
	public static final String TRANSACTION_LOG_PAGE = "Controller?command=";
	public static final String CLIENT_DATA_PAGE = "UserData?userId=";
	public static final String REGISTRATION_PAGE = "Registration";
	public static final String FORWARD_MAIN_PAGE = "/WEB-INF/jsp/main.jsp";
	public static final String FORWARD_PAYMENT_PAGE = "/WEB-INF/jsp/payment.jsp";
	public static final String FORWARD_TOP_UP_CARD_PAGE = "/WEB-INF/jsp/top_up_card.jsp";
	public static final String FORWARD_TRANSACTION_LOG_PAGE = "/WEB-INF/jsp/transaction_log.jsp";

	private GoToPage() {

	}
}
