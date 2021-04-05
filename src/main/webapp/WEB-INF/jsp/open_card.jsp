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
    <fmt:message bundle="${loc}" key="local.locbutton.name.en" var="en_button"/>
    <fmt:message bundle="${loc}" key="local.locbutton.name.ru" var="ru_button"/>
    <fmt:message bundle="${loc}" key="local.logout" var="logout"/>
    <fmt:message bundle="${loc}" key="local.welcome" var="welcome"/>
    <fmt:message bundle="${loc}" key="local.menu.profile" var="menu_profile"/>
    <fmt:message bundle="${loc}" key="local.menu.cards" var="menu_cards"/>
    <fmt:message bundle="${loc}" key="local.menu.card_application" var="menu_card_application"/>
    <meta charset="UTF-8">
    <title>Открытие карты</title>
    <link rel="stylesheet" href="css/open_card/style.css" type="text/css" />
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
                    <form action="profile.jsp" method="post">
                        <input type="hidden" name="command" value=""/>
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
            <p><a href="Controller?command=go_to_main_page">${menu_cards}</a> > Открытие карты </p>
        </div>

		<div class ="open_card">
			<p>Введите данные для открытия карты:</p>
			<form action="Controller" method="post">
				<div>
					<input type="radio" name="type" id="main" onclick="openCloseDiv('info_for_main', 'info_for_additional')" checked/>
					<label for="main">Основная</label>
					<input type="radio" name="type" id="additional" onclick="openCloseDiv('info_for_additional', 'info_for_main')" />
					<label for="additional">Дополнительная</label>
				</div>

				<div id="info_for_main">
					<label for="card_type">Тип карты:</label>
					<select size="2" id="card_type" name="card_type">
						<option value="1">visa</option>
						<option value="2">gold</option>
					</select>
					<label for="currency">Валюта:</label>
					<select size="3" id="currency" name="currency">
						<option value="1">BYN</option>
						<option value="2">USD</option>
						<option value="3">EUR</option>
					</select>
				</div>

				<div id="info_for_additional">
					<div>
						<label for="main_card_number">Выберете основную карту:</label>
						<select size="2" id="main_card_number" name="main_card_number">
							<option value="1111 2222 3333 4444">1111 2222 3333 4444, BYN</option>
							<option value="5555 6666 7777 8888">5555 6666 7777 8888, EUR</option>
						</select>
						<label for="card_type">Тип карты:</label>
						<select size="2" id="card_type" name="card_type">
							<option value="visa">visa</option>
							<option value="gold">gold</option>
						</select>
					</div>
					<div>
						<label for="owner">Номер паспорта владельца карты:</label>
						<input type="text" id="owner" name="owner" placeholder="1111111A111PB1"/>
					</div>
				</div>

				<input type="hidden" name="command" value=""/>
				<input type="submit" value="Открыть карту"/>
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
