<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="mytag" uri="/WEB-INF/tld/taglib.tld" %>

<html>
<head>
    <meta charset="utf-8">
    <fmt:setLocale value="${sessionScope.locale}"/>
    <fmt:setBundle basename="local" var="loc"/>
    <fmt:message bundle="${loc}" key="local.title.registration" var="title"/>
    <fmt:message bundle="${loc}" key="local.locbutton.name.en" var="en_button"/>
    <fmt:message bundle="${loc}" key="local.locbutton.name.ru" var="ru_button"/>
    <fmt:message bundle="${loc}" key="local.login" var="login"/>
    <fmt:message bundle="${loc}" key="local.password" var="password"/>
    <fmt:message bundle="${loc}" key="local.register" var="register"/>
    <title>${title}</title>
    <link rel="stylesheet" href="css/registration/style.css" type="text/css" />
    <link rel="stylesheet" href="css/common/footer.css" type="text/css" />
    <link rel="stylesheet" href="css/common/error_info.css" type="text/css" />
</head>
<body>
<div id="wrap">
<div id="registration-form">
  <header>
    <ul class="clearfix">
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
    </ul>
    <h1>${title}</h1>
    <a href="index.jsp">Back</a>
  </header>

  <fieldset>
    <form action="Controller" method="post">
      <input type="hidden" name="command" value="registration"/>
      <input type="text" name="login" required placeholder="${login}"/>
      <input type="password" name="password" required placeholder="${password}"/>
      <input type="submit" value="${register}"/>
    </form>
  </fieldset>
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
