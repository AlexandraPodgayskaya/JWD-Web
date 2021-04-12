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
    <fmt:message bundle="${loc}" key="local.enter_recipient_data" var="enter_data"/>
    <fmt:message bundle="${loc}" key="local.amount" var="amount"/>
    <fmt:message bundle="${loc}" key="local.currency" var="currency"/>
    <fmt:message bundle="${loc}" key="local.card_number" var="card_number"/>
    <fmt:message bundle="${loc}" key="local.ynp" var="ynp"/>
    <fmt:message bundle="${loc}" key="local.recipient_bic" var="recipient_bic"/>
    <fmt:message bundle="${loc}" key="local.recipient_iban" var="recipient_iban"/>
    <fmt:message bundle="${loc}" key="local.recipient_name" var="recipient_name"/>
    <fmt:message bundle="${loc}" key="local.purpose_of_payment" var="purpose_of_payment"/>
    <fmt:message bundle="${loc}" key="local.command.make_payment" var="command_make_payment"/>
    <fmt:message bundle="${loc}" key="local.payment_card" var="payment_card"/>
    <fmt:message bundle="${loc}" key="local.balance" var="balance"/>
    <fmt:message bundle="${loc}" key="local.command.pay" var="command_pay"/>
    <fmt:message bundle="${loc}" key="local.password" var="password"/>
    <fmt:message bundle="${loc}" key="local.confirm_operation" var="confirm_operation"/>
    <fmt:message bundle="${loc}" key="local.operation_payment" var="operation_payment"/>
    <fmt:message bundle="${loc}" key="local.with_card" var="with_card"/>
    <fmt:message bundle="${loc}" key="local.continue" var="continueButton"/>
    <fmt:message bundle="${loc}" key="local.cancel" var="cancelButton"/>
    <fmt:message bundle="${loc}" key="local.save" var="save"/>
    <title>${title}</title>
    <link rel="stylesheet" href="css/payment/style.css" type="text/css" />
    <link rel="stylesheet" href="css/common/header.css" type="text/css" />
    <link rel="stylesheet" href="css/common/footer.css" type="text/css" />
    <link rel="stylesheet" href="css/common/error_info.css" type="text/css" />
</head>

<body>
<header>
    <div>
        <p>${welcome}, ${sessionScope.userLogin}!</p>
        <a href="Controller?command=logout">${logout}</a>
        <nav>
            <ul>
                <c:if test="${sessionScope.locale == 'en'}">
                <li class="active">
                </c:if>
                <c:if test="${sessionScope.locale != 'en'}">
                <li>
                </c:if>
                    <form action="Controller" method="post" class="locale">
                        <input type="hidden" name="command" value="en"/>
                        <input type="submit" value="${en_button}"/>
                    </form>
                </li>
                <c:if test="${sessionScope.locale == 'ru'}">
                <li class="active">
                </c:if>
                <c:if test="${sessionScope.locale != 'ru'}">
                <li>
                </c:if>
                    <form action="Controller" method="post" class="locale">
                        <input type="hidden" name="command" value="ru"/>
                        <input type="submit" value="${ru_button}"/>
                    </form>
                </li>
                <li>
                    <form action="Controller" method="post">
                        <input type="hidden" name="command" value="go_to_edit_profile_page"/>
                        <input type="submit" value="${menu_profile}"/>
                    </form>
                </li>
                <li class="active">
                    <form action="Controller" method="post">
                        <input type="hidden" name="command" value="go_to_main_page"/>
                        <input type="submit" value="${menu_cards}"/>
                    </form>
                </li>
                <li>
                    <form action="Controller" method="post">
                        <input type="hidden" name="command" value="go_to_open_card_page"/>
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
            <p><a href="Controller?command=go_to_main_page">${menu_cards}</a> > ${payment_card} № ${requestScope.numberCard}. ${balance}: ${requestScope.balance} ${requestScope.currency} </p>
        </div>

		<div class ="transfer">
			<p>${enter_data}</p>
			<form action="Controller" method="post">
				<div>
					<label>${ynp}:
						<input type="text" name="recipientYNP" required placeholder="123456789"/>
					</label>
				</div>
				<div>
					<label>${recipient_name}:
						<input type="text" name="recipientName" required placeholder="${recipient_name}"/>
					</label>
				</div>
				<div>
					<label>${recipient_bic}:
						<input type="text" name="BIC" required placeholder="BPSBBY2X"/>
					</label>
				</div>
				<div>
					<label>${recipient_iban}:
						<input type="text" id="IBAN" name="IBAN" required placeholder="BY10BPSB30120001000200030004"/>
					</label>
				</div>
				<div>
					<label>${purpose_of_payment}:
						<input type="text" name="purposeOfPayment" placeholder="${purpose_of_payment}"/>
					</label>
				</div>
				<div>
					<label>${amount}:
						<input type="text" name="amount" required placeholder="0.00"/>
					</label>
				</div>
				<div>
					<label>${currency}:
						<input type="text" name="currency" value="${requestScope.currency}" disabled/>
					</label>
				</div>
				
				<input type="hidden" name="senderCardNumber" value="${requestScope.numberCard}"/>
				<input type="hidden" name="currency" value="${requestScope.currency}"/>
				<input type="hidden" name="passwordCheck" id="passwordCheckInput" value=""/>
				<input type="hidden" name="command" value="pay"/>
				<input type="submit" id="payment-submit-input" value="${command_pay}"/>
			</form>
			<button id="payment-button" onclick="openPopup('${requestScope.numberCard}')">${save}</button>
		</div>

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

	</section>
    
    <div class="popup" id="check-password-popup">
        <div class="popup-content">
            <p>${confirm_operation}: <b>${operation_payment}</b> ${with_card} № <span id="сardNumberHolder"></span>.</p>       
            <input type="password" id="passwordInput" name="password" required placeholder="${password}"/>
            <button onclick="closePopup()">${continueButton}</button>
            <button onclick="closePopupWithNoPassword()">${cancelButton}</button>
        </div>
    </div>
    

</div>
<mytag:copyright/>

<script>
    function openPopup(cardNumber) {
		var popup = document.getElementById("check-password-popup");
		popup.style.visibility = "visible";
		var cardNumberHolder = document.getElementById("сardNumberHolder");
		cardNumberHolder.innerHTML = cardNumber;
    }

    function closePopup() {
		var passwordInputValue = document.getElementById("passwordInput").value;
		var passwordCheckInput = document.getElementById("passwordCheckInput");
		passwordCheckInput.setAttribute("value", passwordInputValue);
		var paymentSubmitInput = document.getElementById("payment-submit-input");
		paymentSubmitInput.style.visibility = "visible";
		var paymentSubmitInput = document.getElementById("payment-button");
		paymentSubmitInput.style.visibility = "hidden";
		var popup = document.getElementById("check-password-popup");
		popup.style.visibility = "hidden";
    }
	
	function closePopupWithNoPassword() {
		var popup = document.getElementById("check-password-popup");
		popup.style.visibility = "hidden";
    }
</script>

</body>
</html>
