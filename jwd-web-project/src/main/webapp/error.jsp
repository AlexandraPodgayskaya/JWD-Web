<%@ page language="java" contentType="text/html; 
    charset=utf-8"
	pageEncoding="utf-8"%>
	
	<%@ page isErrorPage = "true" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

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
