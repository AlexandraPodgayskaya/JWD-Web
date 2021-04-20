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
    <fmt:message bundle="${loc}" key="local.main_card" var="main_card"/>
    <fmt:message bundle="${loc}" key="local.additional_card" var="additional_card"/>
    <fmt:message bundle="${loc}" key="local.card_type" var="card_type"/>
    <fmt:message bundle="${loc}" key="local.main_card_number" var="main_card_number"/>
    <fmt:message bundle="${loc}" key="local.owner_passport" var="owner_passport"/>
    <fmt:message bundle="${loc}" key="local.command.open_card" var="open_card"/>
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
                    <form action="Controller" method="get">
                        <input type="hidden" name="command" value="go_to_edit_profile_page"/>
                        <input type="submit" value="${menu_profile}"/>
                    </form>
                </li>
                <li>
                    <form action="Controller" method="get">
                        <input type="hidden" name="command" value="go_to_main_page"/>
                        <input type="submit" value="${menu_cards}"/>
                    </form>
                </li>
                <li class="active">
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
            <p><a href="Controller?command=go_to_main_page">${menu_cards}</a> >${card_application} </p>
        </div>

		<div class ="open_card">
			<p>${card_opening_data}:</p>
			<div>
                <input type="radio" name="cardStatus" id="main" value="main" onclick="openCloseDiv('info_for_main', 'info_for_additional')" checked/>
                <label for="main">${main_card}</label>
                <c:if test="${!requestScope.cardList.isEmpty()}">
                    <input type="radio" name="cardStatus" id="additional" name="additional" onclick="openCloseDiv('info_for_additional', 'info_for_main')"/>
                    <label for="additional">${additional_card}</label>
                </c:if>
            </div>
			<form action="Controller" method="post" id="info_for_main">
				<div>
					<label>${card_type}:</label>
				</div>
				<div>
				<c:forEach var="cardType" items="${requestScope.cardTypeList}">
					<input type="radio" name="cardTypeId" id="${cardType.type}" value="${cardType.id}" required>
					<label for="${cardType.type}">${cardType.type} <img src="${cardType.imagePath}"/></label>
				</c:forEach>
				</div>
				<div>
                    <label for="currency">${currency}:</label>
                    <select size="${requestScope.currencyList.size()}" id="currency" name="currency" required>
                        <c:forEach var="currency" items="${requestScope.currencyList}">
                            <option value="${currency}">${currency}</option>
                        </c:forEach>
                    </select>
                </div>
                <input type="hidden" name="command" value="open_main_card"/>
                <input type="submit" value="${open_card}"/>
            </form>
            <form action="Controller" method="post" id="info_for_additional">
                <div>
					<label>${card_type}:</label>
				</div>
                <div>
				<c:forEach var="cardType" items="${requestScope.cardTypeList}">
					<input type="radio" name="cardTypeId" id="${cardType.type}" value="${cardType.id}" required>
					<label for="${cardType.type}">${cardType.type} <img src="${cardType.imagePath}"/></label>
				</c:forEach>
				</div>
				<div>
                    <label for="main_card_number">${main_card_number}:</label>
                    <select size="${requestScope.cardList.size()}" id="main_card_number" name="numberAccount" required>
                        <c:forEach var="card" items="${requestScope.cardList}">
                            <option value="${card.numberAccount}">${card.numberCard}, ${card.currency}</option>
                        </c:forEach>
                    </select>
                </div>
                <div>
                    <label for="owner">${owner_passport}:</label>
                    <input type="text" id="owner" name="personalNumberPassport" placeholder="1111111A111PB1" required />
                </div>
				<input type="hidden" name="command" value="open_additional_card"/>
				<input type="submit" value="${open_card}"/>
			</form>
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
