<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="mytag" uri="/WEB-INF/tld/taglib.tld" %>

<html>
<head>
    <meta charset="UTF-8">
    <fmt:setLocale value="${sessionScope.locale}"/>
    <fmt:setBundle basename="local" var="loc"/>
    <fmt:message bundle="${loc}" key="local.title.topping_up_card" var="title"/>
    <fmt:message bundle="${loc}" key="local.locbutton.name.en" var="en_button"/>
    <fmt:message bundle="${loc}" key="local.locbutton.name.ru" var="ru_button"/>
    <fmt:message bundle="${loc}" key="local.logout" var="logout"/>
    <fmt:message bundle="${loc}" key="local.welcome" var="welcome"/>
    <fmt:message bundle="${loc}" key="local.block" var="block"/>
    <fmt:message bundle="${loc}" key="local.menu.profile" var="menu_profile"/>
    <fmt:message bundle="${loc}" key="local.menu.cards" var="menu_cards"/>
    <fmt:message bundle="${loc}" key="local.menu.card_application" var="menu_card_application"/>
    <fmt:message bundle="${loc}" key="local.command.top_up_card" var="command_top_up_card"/>
    <fmt:message bundle="${loc}" key="local.enter_data" var="enter_data"/>
    <fmt:message bundle="${loc}" key="local.amount" var="amount"/>
    <fmt:message bundle="${loc}" key="local.expiration_date" var="expiration_date"/>
    <fmt:message bundle="${loc}" key="local.sender_card_data" var="sender_card_data"/>
    <fmt:message bundle="${loc}" key="local.recipient_card_data" var="recipient_card_data"/>
    <fmt:message bundle="${loc}" key="local.card_number" var="card_number"/>
    <title>${title}</title>
    <link rel="stylesheet" href="css/transfer/style.css" type="text/css" />
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
                    <form action="profile.html" method="post">
                        <input type="hidden" name="command" value=""/>
                        <input type="hidden" name="page" value=""/>
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
                        <input type="hidden" name="page" value=""/>
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
			<p><a href="Controller?command=go_to_main_page">${menu_cards}</a> > ${command_top_up_card} № ${requestScope.numberCard} </p>
		</div>
	
		<div class ="transfer">
			<p>${enter_data}</p>
			<form action="Controller" method="post">
				<div>
                    <label>${sender_card_data}:<input type="text" name="senderCardNumber" required placeholder="${card_number}" value="" /></label>
                </div>
                 <div>
                    <input type="text" name="senderExpirationDate" required placeholder="${expiration_date}" value="" />
                    <input type="text" name="senderCvvCode" required placeholder="CVV" value="" />
                 </div>
                 <div>
                    <input type="text" name="amount" required placeholder="${amount} 0.00" value="" />
                    <input type="text" name="currency" value="${requestScope.currency}" disabled />
                    <input type="hidden" name="currency" value="${requestScope.currency}"/>
                 </div>
                 <div>
                    <label>${recipient_card_data}: <input type="text" name="recipientCardNumber" value="${requestScope.numberCard}" disabled/></label>
                    <input type="hidden" name="recipientCardNumber" value="${requestScope.numberCard}"/>
                 </div>
            
				<input type="hidden" name="command" value="top_up_card"/>
				<input type="submit" value="${command_top_up_card}"/>
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
