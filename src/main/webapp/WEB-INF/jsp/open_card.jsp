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
    <fmt:message bundle="${loc}" key="local.title.open_card" var="title"/>
    <fmt:message bundle="${loc}" key="local.locbutton.name.en" var="en_button"/>
    <fmt:message bundle="${loc}" key="local.locbutton.name.ru" var="ru_button"/>
    <fmt:message bundle="${loc}" key="local.logout" var="logout"/>
    <fmt:message bundle="${loc}" key="local.welcome" var="welcome"/>
    <fmt:message bundle="${loc}" key="local.menu.profile" var="menu_profile"/>
    <fmt:message bundle="${loc}" key="local.menu.cards" var="menu_cards"/>
    <fmt:message bundle="${loc}" key="local.menu.card_application" var="menu_card_application"/>
    <fmt:message bundle="${loc}" key="local.card_application" var="card_application"/>
    <fmt:message bundle="${loc}" key="local.card_opening_data" var="card_opening_data"/>
    <fmt:message bundle="${loc}" key="local.main_card" var="main_card"/>
    <fmt:message bundle="${loc}" key="local.additional_card" var="additional_card"/>
    <fmt:message bundle="${loc}" key="local.card_type" var="card_type"/>
    <fmt:message bundle="${loc}" key="local.currency" var="currency"/>
    <meta charset="UTF-8">
    <title>${tittle}</title>
    <link rel="stylesheet" href="css/open_card/style.css" type="text/css" />
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
                <li>
                    <form action="Controller" method="post">
                        <input type="hidden" name="command" value="go_to_main_page"/>
                        <input type="submit" value="${menu_cards}"/>
                    </form>
                </li>
                <li class="active">
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
            <p><a href="Controller?command=go_to_main_page">${menu_cards}</a> >${card_application} </p>
        </div>

		<div class ="open_card">
			<p>${card_opening_data}:</p>
			<form action="Controller" method="post">
				<div>
				<c:forEach var="cardStatus" items="${requestScope.cardStatusList}">
					<input type="radio" name="cardStatus" id="${cardStatus}" onclick="openDiv('info_for_${cardStatus}')" checked/>
					<label for="${cardStatus}">${cardStatus}</label>
			    </c:forEach>
				</div>

				<div>
					<label>Card type:</label>
				</div>
				<div>
				<c:forEach var="cardType" items="${requestScope.cardTypeList}">
					<input type="radio" name="cardTypeId" id="${cardType.id}" checked/>
					<label for="${cardType.type}"><img src="${cardType.imagePath}"/></label>
				</c:forEach>
				</div>

                <div id="specific_info">
                    <div id="info_for_main">
                        <label for="currency">${currency}:</label>
                        <select size="${requestScope.currencyList.size()}" id="currency" name="currency">
                            <c:forEach var="currency" items="${requestScope.currencyList}">
                                <option value="${currency}">${currency}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div id="info_for_additional">
                        <div>
                            <label for="main_card_number">Card number:</label>
                            <select size="${requestScope.cardList.size()}" id="main_card_number" name="main_card_number">
                                <c:forEach var="card" items="${requestScope.cardList}">
                                    <option value="${card.numberCard}">${card.numberCard}, ${card.currency}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div>
                            <label for="owner">Owner passport number:</label>
                            <input type="text" id="owner" name="owner" placeholder="1111111A111PB1"/>
                        </div>
                    </div>
				</div>

				<input type="hidden" name="command" value="open_card"/>
				<input type="submit" value="Open card"/>
			</form>
		</div>
	</section>

</div>

<mytag:copyright/>

<script>
    function openDiv(openId) {
	  var closeDivs = document.getElementById("specific_info").children;
	  for (var i=0, child; child=closeDivs[i]; i++) {
	    child.style.display = "none";
	  }

      var openDiv = document.getElementById(openId);
      openDiv.style.display = "block";
    }
</script>

</body>
</html>
