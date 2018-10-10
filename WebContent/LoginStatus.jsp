
<c:set var="logged_in"
	value="<div id='top'>logged in as: ${sessionScope.USER_NAME } / <a href='LogoutServlet'>logout</a></div>" />

<c:set var="login"
	value="<div id='top'><a href='LoginFormServlet'>login</a> / <a href='RegisterFormServlet'>register</a></div>" />

<div id="container">

	<c:out value="<div id='left'><a href='index.jsp'>Home</a></div>"
		escapeXml="false" />

	<c:choose>
		<c:when test="${not empty sessionScope.USER_NAME }">
			<c:out value="${logged_in }" escapeXml="false" />
		</c:when>
		<c:otherwise>
			<c:out value="${login }" escapeXml="false" />
		</c:otherwise>
	</c:choose>
	<br>
	<br>
	<br>
	<div>