<%@ page language="java" contentType="text/html; 
    charset=utf-8"
	pageEncoding="utf-8"%>
	
	<%@ page isErrorPage = "true" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
    <%@ taglib prefix="mytag" uri="/WEB-INF/tld/taglib.tld" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
  <fmt:setLocale value="${sessionScope.locale}"/>
  <fmt:setBundle basename="local" var="loc"/>
  <fmt:message bundle="${loc}" key="local.title.error" var="title"/>
  <fmt:message bundle="${loc}" key="local.locbutton.name.en" var="en_button"/>
  <fmt:message bundle="${loc}" key="local.locbutton.name.ru" var="ru_button"/>
  <fmt:message bundle="${loc}" key="local.error.common_error" var="error"/>
  <fmt:message bundle="${loc}" key="local.logout" var="logout"/>
  <fmt:message bundle="${loc}" key="local.welcome" var="welcome"/>
  <fmt:message bundle="${loc}" key="local.menu.profile" var="menu_profile"/>
  <fmt:message bundle="${loc}" key="local.menu.cards" var="menu_cards"/>
  <fmt:message bundle="${loc}" key="local.menu.card_application" var="menu_card_application"/>
  <fmt:message bundle="${loc}" key="local.menu.add_card_type" var="add_card_type"/>
  <title>${title}</title>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="css/main/style.css" type="text/css" />
  <link rel="stylesheet" href="css/common/header.css" type="text/css" />
  <link rel="stylesheet" href="css/common/footer.css" type="text/css" />
  <link rel="stylesheet" href="css/common/error_info.css" type="text/css" />
</head>
<body>
<header>
    <div>
		<c:if test="${sessionScope.userLogin != null}">
			<p>${welcome}, ${sessionScope.userLogin}!</p>
		</c:if>
		<c:if test="${sessionScope.userLogin == null}">
			<p>${welcome}!</p>
		</c:if>
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
                         <input type="hidden" name="page" value="${pageContext.request.requestURI}"/>
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
                         <input type="hidden" name="page" value="${pageContext.request.requestURI}"/>
                        <input type="submit" value="${ru_button}"/>
                    </form>
                </li>
                <c:if test="${sessionScope.userType == 'CLIENT'}">
                <li>
                    <form action="Controller" method="post">
                        <input type="hidden" name="command" value="go_to_edit_profile_page"/>
                        <input type="submit" value="${menu_profile}"/>
                    </form>
                </li>
                </c:if>
                <li>
                    <form action="Controller" method="post">
                        <input type="hidden" name="command" value="go_to_main_page"/>
                        <input type="submit" value="${menu_cards}"/>
                    </form>
                </li>
                <c:if test="${sessionScope.userType == 'CLIENT'}">
                <li>
                    <form action="Controller" method="post">
                        <input type="hidden" name="command" value="go_to_open_card_page"/>
                        <input type="submit" value="${menu_card_application}"/>
                    </form>
                </li>
                </c:if>
                <c:if test="${sessionScope.userType == 'ADMIN'}">
                <li>
                    <form action="AddCardType" method="get">
                        <input type="submit" value="${add_card_type}"/>
                    </form>
                </li>
                </c:if>
            </ul>
        </nav>
    </div>
</header>

<div class="content">
	<section>
	 <div class="error">
		<h4>${error}</h4>
	</div>
	</section>

</div>

<footer> </footer>


</body>
</html>
