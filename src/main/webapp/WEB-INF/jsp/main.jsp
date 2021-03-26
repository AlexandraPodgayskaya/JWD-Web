<%@ page language="java" contentType="text/html;
    charset=utf-8"
	pageEncoding="utf-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="mytag" uri="/WEB-INF/tld/taglib.tld" %>

<html>
<head>
    <meta charset="utf-8">

      <fmt:setLocale value="${sessionScope.locale}"/>
      <fmt:setBundle basename="local" var="loc"/>
      <fmt:message bundle="${loc}" key="local.title.main" var="title"/>
      <fmt:message bundle="${loc}" key="local.locbutton.name.en" var="en_button"/>
      <fmt:message bundle="${loc}" key="local.locbutton.name.ru" var="ru_button"/>
      <fmt:message bundle="${loc}" key="local.logout" var="logout"/>
      <fmt:message bundle="${loc}" key="local.welcome" var="welcome"/>
      <fmt:message bundle="${loc}" key="local.block" var="block"/>
      <fmt:message bundle="${loc}" key="local.menu.profile" var="menu_profile"/>
      <fmt:message bundle="${loc}" key="local.menu.cards" var="menu_cards"/>
      <fmt:message bundle="${loc}" key="local.menu.card_application" var="menu_card_application"/>
      <fmt:message bundle="${loc}" key="local.command.top_up_card" var="command_top_up_card"/>
      <fmt:message bundle="${loc}" key="local.command.make_payment" var="command_make_payment"/>
      <fmt:message bundle="${loc}" key="local.command.close_card" var="close"/>
      <fmt:message bundle="${loc}" key="local.cancel" var="cancelButton"/>
      <fmt:message bundle="${loc}" key="local.word_card" var="word_card"/>
      <fmt:message bundle="${loc}" key="local.command.account_transaction_log" var="account_transaction_log"/>
      <fmt:message bundle="${loc}" key="local.command.card_transaction_log" var="card_transaction_log"/>
      <title>${title}</title>
    </head>
    <link rel="stylesheet" href="css/main/style.css" type="text/css" />
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

	<c:forEach var="card" items="${requestScope.cardList}">
	  <c:if test="${card.isClosed == false}">
        <c:if test="${card.isBlocked == false}">
            <div class ="cart">
                <p>${card.numberCard}</p>
                <p>${card.balance} ${card.currency}</p>
                <c:if test="${card.status == 'MAIN'}">
                    <form action="Controller" method="post">
                        <input type="hidden" name="command" value="show_account_log"/>
                        <input type="hidden" name="numberCard" value="${card.numberCard}"/>
                        <input type="submit" value="${account_transaction_log}"/>
                    </form>
                </c:if>
                <form action="Controller" method="post">
                    <input type="hidden" name="command" value="show_card_log"/>
                    <input type="hidden" name="numberCard" value="${card.numberCard}"/>
                    <input type="submit" value="${card_transaction_log}"/>
                </form>
                <form action="Controller" method="post">
                    <input type="hidden" name="command" value="go_to_payment_page"/>
                    <input type="hidden" name="numberCard" value="${card.numberCard}"/>
                    <input type="hidden" name="currency" value="${card.currency}"/>
                     <input type="hidden" name="balance" value="${card.balance}"/>
                    <input type="submit" value="${command_make_payment}"/>

                </form>
                <form action="transfer.html" method="post">
                    <input type="hidden" name="command" value=""/>
                    <input type="submit" value="Перевод на карту другого банка"/>

                </form>
                <form action="transfer.html" method="post">
                    <input type="hidden" name="command" value=""/>
                    <input type="submit" value="Перевод внутри платёжной системы"/>

                </form>
                <form action="transfer_to_me.html" method="post">
                    <input type="hidden" name="command" value=""/>
                    <input type="submit" value="Перевод на свою карту"/>

                </form>
                <form action="Controller" method="post">
                    <input type="hidden" name="command" value="go_to_top_up_card_page"/>
                    <input type="hidden" name="numberCard" value="${card.numberCard}"/>
                    <input type="hidden" name="currency" value="${card.currency}"/>
                    <input type="submit" value="${command_top_up_card}"/>
                </form>
                
                <button onclick="openPopup('block-card-popup', 'blockNumberCard', 'blockCardNumberHolder', '${card.numberCard}')">${block}</button>
                <button onclick="openPopup('close-card-popup', 'closeNumberCard', 'closeCardNumberHolder', '${card.numberCard}')">${close}</button>
                
            </div>
        </c:if>
		<c:if test="${card.isBlocked == true}">
		    <div class ="cart blocked">
                <img src="img/zamok.png"/>
                <p>${card.numberCard}</p>
                <p>${card.balance} ${card.currency}</p>
                <c:if test="${card.status == 'MAIN'}">
                    <form action="Controller" method="post">
                        <input type="hidden" name="command" value="show_account_log"/>
                        <input type="hidden" name="numberCard" value="${card.numberCard}"/>
                        <input type="submit" value="${account_transaction_log}"/>
                    </form>
                </c:if>
                <form action="Controller" method="post">
                    <input type="hidden" name="command" value="show_card_log"/>
                    <input type="hidden" name="numberCard" value="${card.numberCard}"/>
                    <input type="submit" value="${card_transaction_log}"/>
                </form>
            </div>
		</c:if>
	  </c:if>
	</c:forEach>	
	
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
    
    <div class="popup" id="close-card-popup">
        <div class="popup-content">
            <p> <b>${close}</b> ${word_card} № <span id="closeCardNumberHolder"></span> ?</p>
            <form action="Controller" method="post">
                <input type="hidden" name="command" value="close_card"/>
                <input type="hidden" id="closeNumberCard" name="numberCard" value="${card.numberCard}"/>
                <input type="submit" value="${close}"/>
            </form>
            <button onclick="closePopup('close-card-popup')">${cancelButton}</button>
        </div>
    </div>

    <div class="popup" id="block-card-popup">
        <div class="popup-content">
            <p><b>${block}</b> ${word_card} № <span id="blockCardNumberHolder"></span> ?</p>
            <form action="Controller" method="post">
                <input type="hidden" name="command" value="blocking"/>
                <input type="hidden" id="blockNumberCard" name="numberCard" value="${card.numberCard}"/>
                <input type="submit" value="${block}"/>
            </form>
            <button onclick="closePopup('block-card-popup')">${cancelButton}</button>
        </div>
    </div>
</div>
<mytag:copyright/>

<script>
    function openPopup(popupId, inputId, spanId, cardNumber) {
      var popup = document.getElementById(popupId);
      popup.style.visibility = "visible";
	  var numberCardInput = document.getElementById(inputId);
	  numberCardInput.setAttribute("value", cardNumber);
	  var cardNumberHolder = document.getElementById(spanId);
	  cardNumberHolder.innerHTML = cardNumber;
    }

    function closePopup(popupId, cardNumber) {
      var popup = document.getElementById(popupId);
      popup.style.visibility = "hidden";
    }
</script>

</body>
</html>
