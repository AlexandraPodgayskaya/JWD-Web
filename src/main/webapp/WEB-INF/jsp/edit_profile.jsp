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
    <h1>${edit_profile}</h1>
  </header>

  <p>${credentails}</p>
  <form action="Controller" method="post" name="registration" class="edit clearfix">
    <input type="hidden" name="command" value="change_login"/>
	<label>${login}:
		<input type="text" name="login" required placeholder="${sessionScope.userLogin}" value="${sessionScope.userLogin}" />
	</label>
	<input type="hidden" name="passwordCheck" id="passwordCheckInput" value=""/>
    <input type="submit" id="payment-submit-input" value="${change}"/>
  </form>
    <button id="payment-button" onclick="openPopup()">${save}</button>
  
  <form action="Controller" method="post" name="registration" class="edit clearfix">
    <input type="hidden" name="command" value="change_password"/>
	<label>${new_password}:
		<input type="text" name="password" required placeholder="${new_password}" />
	</label>
	<label>${repeat_password}:
		<input type="text" name="password" required placeholder="${repeat_password}" />
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

    <div class="popup" id="check-password-popup">
        <div class="popup-content">
            <p>${enter_password}.</p>       
            <input type="password" id="passwordInput" name="password" required placeholder="${password}"/>
            <button onclick="closePopup()">${continueButton}</button>
            <button onclick="closePopupWithNoPassword()">${cancelButton}</button>
        </div>
    </div>
    
    
</div>

<mytag:copyright/>

<script>
function openPopup() {
      var popup = document.getElementById(popupId);
      popup.style.visibility = "visible";
  }

    function closePopup() {
		var passwordInputValue = document.getElementById("passwordInput").value;
		var passwordCheckInput = document.getElementById("passwordCheckInput");
		passwordCheckInput.setAttribute("value", passwordInputValue);
		var paymentSubmitInput = document.getElementById("payment-submit-input");
		paymentSubmitInput.style.visibility = "visible";
		var paymentSubmitInput = document.getElementById("payment-button");
		paymentSubmitInput.style.visibility = "hidden";
		var popup = document.getElementById("check-password-popup");
		popup.style.visibility = "hidden";
    }
	
	function closePopupWithNoPassword() {
		var popup = document.getElementById("check-password-popup");
		popup.style.visibility = "hidden";
    }
</script>

</body>
</html>
