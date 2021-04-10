<%@ page language="java" contentType="text/html;
    charset=utf-8"
	pageEncoding="utf-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="mytag" uri="/WEB-INF/tld/taglib.tld" %>

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
            <p><a href="Controller?command=go_to_main_page">${menu_cards}</a> > ${transfer_card} № ${requestScope.numberCard}. ${balance}: ${requestScope.balance} ${requestScope.currency} </p>
        </div>

		<div class ="open_card">
			<p>${card_opening_data}:</p>
			<form action="Controller" method="post">
				<div>
                    <input type="radio" name="transferType" id="transfer_to_me_input" onclick="openCloseDiv('transfer_to_me', 'transfer_to_other')" checked/>
                    <label for="transfer_to_me_input">${your_card}</label>
                    <input type="radio" name="transferType" id="transfer_to_other_input" onclick="openCloseDiv('transfer_to_other', 'transfer_to_me')" />
                    <label for="transfer_to_other_input">${payment_system_card}</label>
                </div>

				<div id="transfer_to_me">
                    <label for="recipient_card_number">����� ����������</label>
                    <select size="2" id="recipient_card_number" name="recipientCardNumber">
                        <option value="1000 2000 3000 4000">1000 2000 3000 4000</option>
                        <option value="1111 2222 3333 4444">1111 2222 3333 4444</option>
                    </select>
                </div>
                <div id="transfer_to_other">
                    <label for="recipient_card_number2">����� ����� ����������</label>
                    <input type="text" name="recipient_card_number" id="recipientCardNumber" placeholder="����� ����� ����������"/>
                </div>
                <div>
                    <label for="amount">����� ��������</label>
                    <input type="text" name="amount" id="amount" placeholder="����� ��������"/>
                    <input type="text" name="currency" placeholder="BYN" disabled/>
                </div>
                <input type="hidden" name="action" value=""/>
                <input type="hidden" name="page" value=""/>
                <input type="submit" value="���������"/>
			</form>
		</div>
	</section>

</div>

<mytag:copyright/>

<script>
    function openCloseDiv(openId, closeId) {
      var openDiv = document.getElementById(openId);
      openDiv.style.display = "block";
      var closeDiv = document.getElementById(closeId);
      closeDiv.style.display = "none";
    }
</script>

</body>
</html>
