<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="mytag" uri="/WEB-INF/tld/taglib.tld" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.locale}"/>
    <fmt:setBundle basename="local" var="loc"/>
    <fmt:message bundle="${loc}" key="local.title.profile" var="profile"/>
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
    <fmt:message bundle="${loc}" key="local.back" var="back"/>
    <fmt:message bundle="${loc}" key="local.edit_profile" var="edit_profile"/>
    <fmt:message bundle="${loc}" key="local.credentails" var="credentails"/>
    <fmt:message bundle="${loc}" key="local.new_password" var="new_password"/>
    <fmt:message bundle="${loc}" key="local.repeat_password" var="repeat_password"/>
    <fmt:message bundle="${loc}" key="local.change" var="change"/>
    <fmt:message bundle="${loc}" key="local.enter_password" var="enter_password"/>
    <fmt:message bundle="${loc}" key="local.continue" var="continueButton"/>
    <fmt:message bundle="${loc}" key="local.cancel" var="cancelButton"/>
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
        <input type="submit" name="back" value="${back}"/>
    </form>
	<ul class="clearfix">
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
    </ul>
    <h1>${edit_profile}</h1>
  </header>

  <p>${credentails}</p>
  <form action="Controller" method="post" name="registration" class="edit clearfix">
    <input type="hidden" name="command" value="change_login"/>
	<label>${login}:
		<input type="text" name="login" required placeholder="${login}" value="${sessionScope.userLogin}" />
	</label>
	<input type="hidden" name="passwordCheck" id="passwordCheckForLoginInput" value=""/>
    <input type="submit" id="edit-login-submit-input" value="${change}"/>
  </form>
    <button id="edit-login-button" onclick="openPopup('check-password-for-login-popup')">${save}</button>

  <hr/>

  <form action="Controller" method="post" name="registration" class="edit clearfix">
    <input type="hidden" name="command" value="change_password"/>
	<div>
		<label>${new_password}:
			<input type="password" name="password" required placeholder="${new_password}" />
		</label>
	</div>
	<div>
		<label>${repeat_password}:
			<input type="password" name="passwordRepeat" required placeholder="${repeat_password}" />
		</label>
	</div>
    <input type="hidden" name="passwordCheck" id="passwordCheckForPwdInput" value=""/>
    <input type="submit" id="edit-password-submit-input" value="${change}"/>
  </form>
    <button id="edit-password-button" onclick="openPopup('check-password-for-pwd-popup')">${save}</button>
    
  <hr/>

  <p>${profile}</p>
  <form action="Controller" method="post" name="registration" class="edit clearfix">
    <input type="hidden" name="command" value="change_client_data"/>
    <input type="hidden" name="userId" value="${sessionScope.userId}"/>
    <div>
      <label>${full_name}: <br />
          <input type="text" name="surname" required placeholder="${surname}" value="${userInfo.surname}" />
          <input type="text" name="name" required placeholder="${name}" value="${userInfo.name}" />
          <input type="text" name="patronymic" required placeholder="${patronymic}" value="${userInfo.patronymic}" />
      </label>
    </div>
    <div>
      <label>${date_of_birth}:
		<input type="text" name="dateBirth" required placeholder="XX.XX.XXXX" value="${userInfo.dateBirth}" />
      </label>
    </div>
	<div>
		<label>${personal_number_passport}:
			<input type="text" name="personalNumberPassport" id="passport" required placeholder="1111111A111PB1" value="${userInfo.personalNumberPassport}" />
		</label>
	</div>
    <div>
      <label>${phone}:
		<input type="text" name="phone" required placeholder="+375XXXXXXXXX" value="${userInfo.phone}" />
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

    <div class="popup" id="check-password-for-login-popup">
        <div class="popup-content">
            <p>${enter_password}:</p>
            <input type="password" id="passwordInput" name="password" required placeholder="${password}"/>
            <button onclick="closePopup('passwordInput', 'passwordCheckForLoginInput', 'edit-login-submit-input', 'edit-login-button', 'check-password-for-login-popup')">${continueButton}</button>
            <button onclick="closePopupWithNoPassword('check-password-for-login-popup')">${cancelButton}</button>
        </div>
    </div>

    <div class="popup" id="check-password-for-pwd-popup">
            <div class="popup-content">
                <p>${enter_password}:</p>
                <input type="password" id="passwordInput2" name="password" required placeholder="${password}"/>
                <button onclick="closePopup('passwordInput2', 'passwordCheckForPwdInput', 'edit-password-submit-input', 'edit-password-button', 'check-password-for-pwd-popup')">${continueButton}</button>
                <button onclick="closePopupWithNoPassword('check-password-for-pwd-popup')">${cancelButton}</button>
            </div>
        </div>


</div>

<mytag:copyright/>

<script>
  function openPopup(popupId) {
      var popup = document.getElementById(popupId);
      popup.style.visibility = "visible";
  }

    function closePopup(passwordInputId, passwordCheckInputId, editSubmitInputId, editSubmitButtonId, popupId) {
		var passwordInputValue = document.getElementById(passwordInputId).value;
		var passwordCheckInput = document.getElementById(passwordCheckInputId);
		passwordCheckInput.setAttribute("value", passwordInputValue);
		var editSubmitInput = document.getElementById(editSubmitInputId);
		editSubmitInput.style.visibility = "visible";
		var editSubmitButton = document.getElementById(editSubmitButtonId);
		editSubmitButton.style.visibility = "hidden";
		var popup = document.getElementById(popupId);
		popup.style.visibility = "hidden";
    }

	function closePopupWithNoPassword(popupId) {
		var popup = document.getElementById(popupId);
		popup.style.visibility = "hidden";
    }
</script>

</body>
</html>
