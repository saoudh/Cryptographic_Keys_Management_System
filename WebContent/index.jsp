<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<%@ page import="java.io.*,java.util.*,java.sql.*"%>

<%@ page import="javax.servlet.http.*,javax.servlet.*"%>

<c:set var="USER_ID" value="${sessionScope.USER_ID }" scope="session" />


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="indexStyles.css">



<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Home Menu</title>

</head>

<body>

	<%-- <c:set var="USER_NAME" value="Myname" scope="session"/> --%>


	<%@ include file="LoginStatus.jsp"%>

	<fieldset>
		<legend>Home</legend>
		<p>
			<a href="GenerateKeysForm.jsp">Generate Keys</a>
		</p>
		<p>
			<a href="RequestKeysForm.jsp">Download your Keys</a>
		</p>
		<c:if test="${USER_ID==1}">

			<c:out
				value="<p><a href='AdminPanelFormGeneratedKeys.jsp'>Admin Panel</a></p>"
				escapeXml="false" />
		</c:if>
	</fieldset>
	</div>
	</div>
</body>
</html>