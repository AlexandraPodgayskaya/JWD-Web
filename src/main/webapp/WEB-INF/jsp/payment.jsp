<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="mytag" uri="/WEB-INF/tld/taglib.tld" %>

<html>
<head>
    <meta charset="UTF-8">
    <fmt:setLocale value="${sessionScope.locale}"/>
    <fmt:setBundle basename="local" var="loc"/>
    <fmt:message bundle="${loc}" key="local.title.payment" var="title"/>
    <fmt:message bundle="${loc}" key="local.locbutton.name.en" var="en_button"/>
    <fmt:message bundle="${loc}" key="local.locbutton.name.ru" var="ru_button"/>
    <fmt:message bundle="${loc}" key="local.logout" var="logout"/>
    <fmt:message bundle="${loc}" key="local.welcome" var="welcome"/>
    <fmt:message bundle="${loc}" key="local.menu.profile" var="menu_profile"/>
    <fmt:message bundle="${loc}" key="local.menu.cards" var="menu_cards"/>
    <fmt:message bundle="${loc}" key="local.menu.card_application" var="menu_card_application"/>
    <fmt:message bundle="${loc}" key="local.enter_data" var="enter_data"/>
    <fmt:message bundle="${loc}" key="local.amount" var="amount"/>
    <fmt:message bundle="${loc}" key="local.card_number" var="card_number"/>
    <fmt:message bundle="${loc}" key="local.ynp" var="ynp"/>
    <fmt:message bundle="${loc}" key="local.recipient_bik" var="recipient_bik"/>
    <fmt:message bundle="${loc}" key="local.recipient_iban" var="recipient_iban"/>
    <fmt:message bundle="${loc}" key="local.recipient_name" var="recipient_name"/>
    <fmt:message bundle="${loc}" key="local.purpose_of_payment" var="purpose_of_payment"/>
    <fmt:message bundle="${loc}" key="local.command.make_payment" var="command_make_payment"/>
    <title>${title}</title>
    <link rel="stylesheet" href="css/payment/style.css" type="text/css" />
</head>

<body>

<header>
    <div>
        <p>${welcome}, ${sessionScope.userLogin}!</p>
        <a href="Controller?command=logout">${logout}</a>
        <nav>
            <ul>
                <li>
                    <form action="Controller" method="post" class="locale">
                        <input type="hidden" name="command" value="en"/>
                        <input type="submit" value="${en_button}"/>
                    </form>
                </li>
                <li>
                    <form action="Controller" method="post" class="locale">
                        <input type="hidden" name="command" value="ru"/>
                        <input type="submit" value="${ru_button}"/>
                    </form>
                </li>
                <li>
                    <form action="profile.html" method="post">
                        <input type="hidden" name="command" value=""/>
                        <input type="hidden" name="page" value=""/>
                        <input type="submit" value="${menu_profile}"/>
                    </form>
                </li>
                <li>
                    <form action="Controller" method="post">
                        <input type="hidden" name="command" value="go_to_main_page"/>
                        <input type="submit" value="${menu_cards}"/>
                    </form>
                </li>
                <li>
                    <form action="cart_application.html" method="post">
                        <input type="hidden" name="command" value=""/>
                        <input type="hidden" name="page" value=""/>
                        <input type="submit" value="${menu_card_application}"/>
                    </form>
                </li>
            </ul>
        </nav>
    </div>
</header>

<div class="content">

    <section>
		<div>
            <p><a href="Controller?command=go_to_main_page">${menu_cards}</a> > ����� � ����� � ${requestScope.numberCard} </p>
        </div>

		<div class ="transfer">
			<p>${enter_data}</p>
			<form action="Controller" method="post">
				<div>
					<label>${ynp}:
						<input type="text" name="YNP" placeholder="${ynp}"/>
					</label>
				</div>
				<div>
					<label>${recipient_name}:
						<input type="text" name="recipientName" placeholder="${recipient_name}"/>
					</label>
				</div>
				<div>
					<label>${recipient_bik}:
						<input type="text" name="BIK" placeholder="${recipient_bik}"/>
					</label>
				</div>
				<div>
					<label>${recipient_iban}:
						<input type="text" name="IBAN" placeholder="${recipient_iban}"/>
					</label>
				</div>
				<div>
					<label>${purpose_of_payment}:
						<input type="text" name="purposeOfPayment" placeholder="${purpose_of_payment}"/>
					</label>
				</div>
				<div>
					<label>${amount}:
						<input type="text" name="amount" placeholder="${amount}"/>
					</label>
				</div>
				<div>
					<label>${currency}:
						<input type="text" name="currency" value="${requestScope.currency}" disabled/>
					</label>
				</div>
				<div>
					<label>CVV:
						<input type="text" name="senderCvvCode" placeholder="CVV"/>
					</label>
				</div>
				<input type="hidden" name="senderCardNumber" value="${requestScope.numberCard}"/>
				<input type="hidden" name="action" value=""/>
				<input type="hidden" name="page" value=""/>
				<input type="submit" value="${command_make_payment}"/>
			</form>
		</div>


	</section>

	<c:if test="${errorMessageList != null}">
        <c:forEach var="errorMessageKey" items="${errorMessageList}">
        <fmt:message bundle="${loc}" key="${errorMessageKey}" var="error"/>
            <div class="error">
                <h4>${error}</h4>
            </div>
        </c:forEach>
        <c:remove var="errorMessageList"/>
    </c:if>

    <c:if test="${infoMessage != null}">
        <fmt:message bundle="${loc}" key="${infoMessage}" var="message"/>
        <div class="message">
            <h4>${message}</h4>
        </div>
        <c:remove var="infoMessage"/>
    </c:if>

</div>

<footer> </footer>


</body>
</html>
