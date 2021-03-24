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
                    <form action="journal_account.html" method="post">
                        <input type="hidden" name="command" value=""/>
                        <input type="hidden" name="page" value=""/>
                        <input type="submit" value="Журнал операций по счёту"/>
                    </form>
                </c:if>
                <form action="journal.html" method="post">
                    <input type="hidden" name="command" value=""/>
                    <input type="submit" value="Журнал операций по карте"/>
                </form>
                <form action="Controller" method="post">
                    <input type="hidden" name="command" value="go_to_payment_page"/>
                    <input type="hidden" name="numberCard" value="${card.numberCard}"/>
                    <input type="hidden" name="currency" value="${card.currency}"/>
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
                <form action="Controller" method="post">
                    <input type="hidden" name="command" value="blocking"/>
                    <input type="hidden" name="numberCard" value="${card.numberCard}"/>
                    <input type="submit" value="${block}"/>

                </form>
                <form action="Controller" method="post">
                    <input type="hidden" name="command" value="close_card"/>
                    <input type="hidden" name="numberCard" value="${card.numberCard}"/>
                    <input type="submit" value="${close}"/>
                </form>
            </div>
        </c:if>
		<c:if test="${card.isBlocked == true}">
		    <div class ="cart blocked">
                <img src="img/zamok.png"/>
                <p>${card.numberCard}</p>
                <p>${card.balance} ${card.currency}</p>
                <c:if test="${card.status == 'MAIN'}">
                    <form action="journal_account.html" method="post">
                        <input type="hidden" name="command" value=""/>
                        <input type="hidden" name="page" value=""/>
                        <input type="submit" value="Журнал операций по счёту"/>
                    </form>
                </c:if>
                <form action="Controller" method="post">
                    <input type="hidden" name="command" value=""/>
                    <input type="submit" value="Журнал операций по карте"/>
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
    
</div>

<mytag:copyright/>


</body>
</html>
