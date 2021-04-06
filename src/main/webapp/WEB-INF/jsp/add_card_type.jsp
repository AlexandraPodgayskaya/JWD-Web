<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="mytag" uri="/WEB-INF/tld/taglib.tld" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.locale}"/>
    <fmt:setBundle basename="local" var="loc"/>
    <fmt:message bundle="${loc}" key="local.title.adding_card_type" var="title"/>
    <fmt:message bundle="${loc}" key="local.locbutton.name.en" var="en_button"/>
    <fmt:message bundle="${loc}" key="local.locbutton.name.ru" var="ru_button"/>
    <fmt:message bundle="${loc}" key="local.logout" var="logout"/>
    <fmt:message bundle="${loc}" key="local.welcome" var="welcome"/>
    <fmt:message bundle="${loc}" key="local.menu.cards" var="menu_cards"/>
    <fmt:message bundle="${loc}" key="local.menu.add_card_type" var="add_card_type"/>
    <fmt:message bundle="${loc}" key="local.enter_data" var="enter_data"/>
    <fmt:message bundle="${loc}" key="local.card_type" var="card_type"/>
    <fmt:message bundle="${loc}" key="local.image" var="image"/>
    <fmt:message bundle="${loc}" key="local.add_type" var="add_type"/>
    <meta charset="UTF-8">
    <title>${tittle}</title>
    <link rel="stylesheet" href="css/add_card_type/style.css" type="text/css" />
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
                        <input type="hidden" name="page" value="${pageContext.request.requestURI}"/>
                        <input type="submit" value="${en_button}"/>
                    </form>
                </li>
                <li>
                    <form action="Controller" method="post" class="locale">
                        <input type="hidden" name="command" value="ru"/>
                        <input type="hidden" name="page" value="${pageContext.request.requestURI}"/>
                        <input type="submit" value="${ru_button}"/>
                    </form>
                </li>
                <li>
                    <form action="Controller" method="post">
                        <input type="hidden" name="command" value="go_to_main_page"/>
                        <input type="submit" value="${menu_cards}"/>
                    </form>
                </li>
                <li>
                   <form action="AddCardType" method="get">
                        <input type="submit" value="${add_card_type}"/>
                    </form>
                </li>
            </ul>
        </nav>
    </div>
</header>

<div class="content">

    <section>

		<div class="card_type">
			<p>${enter_data}</p>
			<form action="Controller" method="post" enctype="multipart/form-data">
				<div>
                    <label>${card_type}: <input type="text" name="cardType" required placeholder="${card_type}" value="" /></label>
                </div>
                 <div>
                    <label>${image}: <input type="file" name="image" accept="image/*,image/jpeg"></label>
                 </div>
				<input type="hidden" name="command" value="add_card_type"/>
				<input type="submit" value="${add_type}"/>
			</form>
		</div>

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
