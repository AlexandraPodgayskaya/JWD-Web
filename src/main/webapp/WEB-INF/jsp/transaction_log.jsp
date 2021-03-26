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
    <link rel="stylesheet" href="css/account_journal/style.css" type="text/css" />
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
			<p><a href="main.html">Карты</a> > Операции по счёту ${transactionList[0].transactionAccount}</p>
		</div>

		<c:forEach var="transaction" items="${requestScope.transactionList}">
		    <c:if test="${transaction.typeTransaction == 'RECEIPT'}">
                <div class="journal income">
                  <p>+ ${transaction.amount} ${transaction.currency}</p>
                  <p>${transaction.numberCard} | ${transaction.dateTime}</p>
                  <p>${transaction.name}</p>
                </div>
            </c:if>
            <c:if test="${transaction.typeTransaction == 'EXPENDITURE'}">
                <div class="journal outcome">
                    <p>- ${transaction.amount} ${transaction.currency}</p>
                    <p>${transaction.numberCard} | ${transaction.dateTime}</p>
                    <p>${transaction.name}</p>
                </div>
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
