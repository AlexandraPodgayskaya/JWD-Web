<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="mytag" uri="/WEB-INF/tld/taglib.tld" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.locale}"/>
    <fmt:setBundle basename="local" var="loc"/>
    <fmt:message bundle="${loc}" key="local.title.profile" var="title"/>
    <fmt:message bundle="${loc}" key="local.locbutton.name.en" var="en_button"/>
    <fmt:message bundle="${loc}" key="local.locbutton.name.ru" var="ru_button"/>
    <fmt:message bundle="${loc}" key="local.login" var="login"/>
    <fmt:message bundle="${loc}" key="local.password" var="password"/>
    <fmt:message bundle="${loc}" key="local.save" var="save"/>
    <fmt:message bundle="${loc}" key="local.full_name" var="full_name"/>
    <fmt:message bundle="${loc}" key="local.surname" var="surname"/>
    <fmt:message bundle="${loc}" key="local.name" var="name"/>
    <fmt:message bundle="${loc}" key="local.patronymic" var="patronymic"/>
    <fmt:message bundle="${loc}" key="local.date_of_birth" var="date_of_birth"/>
    <fmt:message bundle="${loc}" key="local.personal_number_passport" var="personal_number_passport"/>
    <fmt:message bundle="${loc}" key="local.phone" var="phone"/>
    <title>${profile}</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="css/edit_profile/style.css" type="text/css" />
    <link rel="stylesheet" href="css/common/footer.css" type="text/css" />
    <link rel="stylesheet" href="css/common/error_info.css" type="text/css" />
</head>
<body>

<div class="main">
  <header>
    <form action="Controller" method="get" class="back">
        <input type="hidden" name="command" value="go_to_main_page"/>
        <input type="submit" name="back" value="Back"/>
    </form>
	<ul class="clearfix">
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
    </ul>
    <h1>Edit profile</h1>
  </header>

  <p>Credentials</p>
  <form action="Controller" method="post" name="registration" class="edit clearfix">
    <input type="hidden" name="command" value=""/>
	<input type="hidden" name="userId" value=""/>
	<label>${login}:
		<input type="text" name="login" required />
	</label>
	<label>${password}:
		<input type="text" name="password" required />
	</label>
    <input type="submit" value="${save}"/>
  </form>

  <hr/>

  <p>${profile}</p>
  <form action="Controller" method="post" name="registration" class="edit clearfix">
    <input type="hidden" name="command" value="save_additional_client_data"/>
	<input type="hidden" name="userId" value=""/>
    <div>
      <label>${full_name}: <br />
          <input type="text" name="surname" required placeholder="${surname}" />
          <input type="text" name="name" required placeholder="${name}" />
          <input type="text" name="patronymic" required placeholder="${patronymic}" />
      </label>
    </div>
    <div>
      <label>${date_of_birth}:
		<input type="text" name="date_of_birth" required placeholder="XX.XX.XXXX" />
      </label>
    </div>
	<div>
		<label>${personal_number_passport}:
			<input type="text" name="personal_number_passport" id="passport" required placeholder="1111111A111PB1" />
		</label>
	</div>
    <div>
      <label>${phone}:
		<input type="text" name="phone" required placeholder="+375XXXXXXXXX" />
      </label>
    </div>

    <input type="submit" value="${save}"/>

  </form>

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
