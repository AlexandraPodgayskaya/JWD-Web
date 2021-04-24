<%@ page language="java" contentType="text/html;
    charset=utf-8"
	pageEncoding="utf-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="customtag" prefix="mytag"%>

<html>
<head>
    <fmt:setLocale value="${sessionScope.locale}"/>
    <fmt:setBundle basename="local" var="loc"/>
    <fmt:message bundle="${loc}" key="local.title.transfer" var="title"/>
    <fmt:message bundle="${loc}" key="local.locbutton.name.en" var="en_button"/>
    <fmt:message bundle="${loc}" key="local.locbutton.name.ru" var="ru_button"/>
    <fmt:message bundle="${loc}" key="local.logout" var="logout"/>
    <fmt:message bundle="${loc}" key="local.welcome" var="welcome"/>
    <fmt:message bundle="${loc}" key="local.menu.profile" var="menu_profile"/>
    <fmt:message bundle="${loc}" key="local.menu.cards" var="menu_cards"/>
    <fmt:message bundle="${loc}" key="local.menu.card_application" var="menu_card_application"/>
    <fmt:message bundle="${loc}" key="local.transfer_card" var="transfer_card"/>
    <fmt:message bundle="${loc}" key="local.your_card" var="your_card"/>
    <fmt:message bundle="${loc}" key="local.payment_system_card" var="payment_system_card"/>
    <fmt:message bundle="${loc}" key="local.amount" var="amount"/>
    <fmt:message bundle="${loc}" key="local.recipient_card_number" var="recipient_card_number"/>
    <fmt:message bundle="${loc}" key="local.card_number" var="card_number"/>
    <fmt:message bundle="${loc}" key="local.balance" var="balance"/>
    <fmt:message bundle="${loc}" key="local.transfer_data" var="transfer_data"/>
    <fmt:message bundle="${loc}" key="local.transfer_amount" var="transfer_amount"/>
    <fmt:message bundle="${loc}" key="local.command.transfer" var="transfer"/>
    <fmt:message bundle="${loc}" key="local.enter_password" var="enter_password"/>
    <fmt:message bundle="${loc}" key="local.password" var="password"/>
    <fmt:message bundle="${loc}" key="local.continue" var="continueButton"/>
    <fmt:message bundle="${loc}" key="local.cancel" var="cancelButton"/>
    <fmt:message bundle="${loc}" key="local.save" var="save"/>
    <fmt:message bundle="${loc}" key="local.confirm_operation" var="confirm_operation"/>
    <fmt:message bundle="${loc}" key="local.operation_transfer" var="operation_transfer"/>
    <fmt:message bundle="${loc}" key="local.with_card" var="with_card"/>
    <meta charset="UTF-8">
    <title>${tittle}</title>
    <link rel="stylesheet" href="css/transfer/style.css" type="text/css" />
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
                    <form action="Controller" method="get">
                        <input type="hidden" name="command" value="go_to_edit_profile_page"/>
                        <input type="submit" value="${menu_profile}"/>
                    </form>
                </li>
                <li class="active">
                    <form action="Controller" method="get">
                        <input type="hidden" name="command" value="go_to_main_page"/>
                        <input type="submit" value="${menu_cards}"/>
                    </form>
                </li>
                <li>
                    <form action="Controller" method="get">
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
			<p><a href="Controller?command=go_to_main_page">${menu_cards}</a> > ${transfer_card} № ${requestScope.numberCard}. ${balance}: ${requestScope.balance} ${requestScope.currency} </p>
		</div>

		<div class ="transfer">
			<p>${transfer_data}:</p>
			<div>
			<p>${transfer_card} № ${requestScope.numberCard}</p>
				<c:if test="${!requestScope.cardList.isEmpty()}">
					<input type="radio" name="transferType" id="transfer_to_me_input" onclick="openCloseDiv('transfer_to_me', 'transfer_to_other')"/>
					<label for="transfer_to_me_input">${your_card}</label>
				</c:if>
				<input type="radio" name="transferType" id="transfer_to_other_input" onclick="openCloseDiv('transfer_to_other', 'transfer_to_me')" />
				<label for="transfer_to_other_input">${payment_system_card}</label>
			</div>
			<div id="transfer_to_me">
                <form action="Controller" method="post" >
                    <label for="recipient_card_number">${recipient_card_number}:</label>
                    <select size="${requestScope.cardList.size()}" id="recipient_card_number" name="recipientCardNumber" required>
                        <c:forEach var="card" items="${requestScope.cardList}">
                            <option value="${card.numberCard}">${card.numberCard}, ${card.currency}</option>
                        </c:forEach>
                    </select>
                    <div>
                        <label for="amount">${transfer_amount}:</label>
                        <input type="text" name="amount" placeholder="${amount} 0.00" required />
                        <input type="text" name="currency" placeholder="${requestScope.currency}" value="${requestScope.currency}"  disabled/>
                        <input type="hidden" name="currency" value="${requestScope.currency}"/>
                    </div>
                    <input type="hidden" name="senderCardNumber" value="${requestScope.numberCard}"/>
                    <input type="hidden" name="command" value="transfer"/>
                    <input type="hidden" name="passwordCheck" id="passwordCheckInput" value=""/>
                    <input type="submit" id="transfer-submit-input" value="${transfer}"/>
                </form>
                <button id="transfer-button" onclick="openPopup('${requestScope.numberCard}', 'transfer-button', 'transfer-submit-input', 'passwordCheckInput')">${save}</button>
			</div>
			<div id="transfer_to_other">
                <form action="Controller" method="post">
                    <div>
                        <label for="recipient_card_number2">${recipient_card_number}:</label>
                        <input type="text" name="recipientCardNumber" id="recipient_card_number2" placeholder="${card_number}" required />
                    </div>
                    <div>
                        <label for="amount">${transfer_amount}:</label>
                        <input type="text" name="amount" placeholder="${amount} 0.00" required />
                        <input type="text" name="currency" placeholder="${requestScope.currency}" disabled />
                        <input type="hidden" name="currency" value="${requestScope.currency}"/>
                    </div>
                    <input type="hidden" name="senderCardNumber" value="${requestScope.numberCard}"/>
                    <input type="hidden" name="command" value="transfer"/>
                    <input type="hidden" name="passwordCheck" id="passwordCheckInput2" value=""/>
                    <input type="submit" id="transfer-submit-input-2" value="${transfer}"/>
                </form>
                <button id="transfer-button-2" onclick="openPopup('${requestScope.numberCard}', 'transfer-button-2', 'transfer-submit-input-2', 'passwordCheckInput2')">${save}</button>
            </div>
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
            <p>${confirm_operation}: <b>${operation_transfer}</b> ${with_card} № <span id="сardNumberHolder"></span>.</p>
            <input type="password" id="passwordInput" name="password" required placeholder="${password}"/>
            <button onclick="closePopup()">${continueButton}</button>
            <button onclick="closePopupWithNoPassword()">${cancelButton}</button>
        </div>
    </div>
</div>

<mytag:copyright/>

<script>
    function openCloseDiv(openId, closeId) {
      var openDiv = document.getElementById(openId);
      openDiv.style.display = "block";
      var closeDiv = document.getElementById(closeId);
      closeDiv.style.display = "none";
    }

    function openPopup(cardNumber, buttonId, submitInputId, passwordCheckInputId) {
        popup = document.getElementById("check-password-popup");
        popup.style.visibility = "visible";
        var cardNumberHolder = document.getElementById("сardNumberHolder");
        cardNumberHolder.innerHTML = cardNumber;

        submitInput = document.getElementById(submitInputId);
        button = document.getElementById(buttonId);
		passwordCheckInput = document.getElementById(passwordCheckInputId);
    }

    function closePopup() {
        var passwordInputValue = document.getElementById("passwordInput").value;
        passwordCheckInput.setAttribute("value", passwordInputValue);

        submitInput.style.visibility = "visible";
        button.style.visibility = "hidden";
        popup.style.visibility = "hidden";
    }

    function closePopupWithNoPassword() {
        popup.style.visibility = "hidden";
    }
</script>

</body>
</html>
