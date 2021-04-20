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
      <fmt:message bundle="${loc}" key="local.title.transaction_log" var="title"/>
      <fmt:message bundle="${loc}" key="local.locbutton.name.en" var="en_button"/>
      <fmt:message bundle="${loc}" key="local.locbutton.name.ru" var="ru_button"/>
      <fmt:message bundle="${loc}" key="local.logout" var="logout"/>
      <fmt:message bundle="${loc}" key="local.welcome" var="welcome"/>
      <fmt:message bundle="${loc}" key="local.menu.profile" var="menu_profile"/>
      <fmt:message bundle="${loc}" key="local.menu.cards" var="menu_cards"/>
      <fmt:message bundle="${loc}" key="local.menu.card_application" var="menu_card_application"/>
      <fmt:message bundle="${loc}" key="local.menu.card_application" var="menu_card_application"/>
      <fmt:message bundle="${loc}" key="local.account_transaction_log" var="account_transaction_log"/>
      <fmt:message bundle="${loc}" key="local.card_transaction_log" var="card_transaction_log"/>
      <title>${title}</title>
    </head>
    <link rel="stylesheet" href="css/transaction_log/style.css" type="text/css" />
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
                <c:if test="${sessionScope.userType == 'CLIENT'}">
                <li>
                    <form action="Controller" method="get">
                        <input type="hidden" name="command" value="go_to_edit_profile_page"/>
                        <input type="submit" value="${menu_profile}"/>
                    </form>
                </li>
                </c:if>
                <li class="active">
                    <form action="Controller" method="get">
                        <input type="hidden" name="command" value="go_to_main_page"/>
                        <input type="submit" value="${menu_cards}"/>
                    </form>
                </li>
                <c:if test="${sessionScope.userType == 'CLIENT'}">
                <li>
                    <form action="Controller" method="get">
                        <input type="hidden" name="command" value="go_to_open_card_page"/>
                        <input type="submit" value="${menu_card_application}"/>
                    </form>
                </li>
                </c:if>
            </ul>
        </nav>
    </div>
</header>

<div class="content">
    <section>
		<div>
			<p><a href="Controller?command=go_to_main_page">${menu_cards}</a> > 
			<c:if test="${requestScope.command == 'show_account_log'}" > ${account_transaction_log} № ${transactionList[0].transactionAccount}
			</c:if>
			<c:if test="${requestScope.command == 'show_card_log'}" > ${card_transaction_log} № ${transactionList[0].numberCard}
			</c:if>
			</p>
		</div>

		<c:forEach var="transaction" items="${requestScope.transactionList}">
		    <c:if test="${transaction.typeTransaction == 'RECEIPT'}">
                <div class="journal income">
                  <p>+ ${transaction.amount} ${transaction.currency}</p>
                  <p>${transaction.numberCard} | <fmt:formatDate value="${transaction.dateTime}" pattern = "dd.MM.yyyy HH:mm"/></p>
                  <p>${transaction.purposePayment}</p>
                </div>
            </c:if>
            <c:if test="${transaction.typeTransaction == 'EXPENDITURE'}">
                <div class="journal outcome">
                    <p>- ${transaction.amount} ${transaction.currency}</p>
                    <p>${transaction.numberCard} | <fmt:formatDate value="${transaction.dateTime}" pattern = "dd.MM.yyyy HH:mm"/></p>
                    <p>${transaction.purposePayment}</p>
                </div>
            </c:if>
		</c:forEach>

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

</div>
<mytag:copyright/>


</body>
</html>
