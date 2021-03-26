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
  <title>${title}</title>
</head>
<body>

        <form action="Controller" method="post" class="locale">
          <input type="hidden" name="command" value="en"/>
           <input type="hidden" name="page" value="${pageContext.request.requestURI}"/>
          <input type="submit" value="${en_button}"/> <br />
        </form>
     
        <form action="Controller" method="post" class="locale">
          <input type="hidden" name="command" value="ru"/>
           <input type="hidden" name="page" value="${pageContext.request.requestURI}"/>
          <input type="submit" value="${ru_button}"/> <br />
        </form>

	<c:out value = "${error}" />

	<br />

	<a href="Controller?command=logout">${logout}</a>

	<mytag:copyright/>

</body>
</html>