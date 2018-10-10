<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.io.*,java.util.*,java.sql.*"%>

<%@ page import="javax.servlet.http.*,javax.servlet.*"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jstl/sql_rt"%>

<sql:setDataSource var="datasource" driver="com.mysql.cj.jdbc.Driver"
	url="jdbc:mysql://localhost/MyDB" user="root" password="thehorse" />
<sql:query dataSource="${datasource}" var="result"> SELECT id, title, filename, date,keytype,keypart FROM mykeys   </sql:query>
<html>
<head>

<link rel="stylesheet" type="text/css" href="indexStyles.css">
<style>
</style>
<title>Request Keys</title>
</head>
<body>
	<%@ include file="LoginStatus.jsp"%>

	<form action="ProcessRequestedKeys" method="post">
		<c:out value="<ul>" escapeXml="false" />
		<c:forEach items="${result.rows}" var="row">
			<c:set var="keytype" value="${row.keytype}" />
			<c:set var="id" value="${row.id}" />
			<c:set var="keypart" value="${row.keypart}" />
			<!--  Das Datum vom aktuellen Datensatz und vom nächsten holen, um 
				vergleichen zu können, ob die Schlüssel zusammengehören -->
			<c:set var="currentdate1" value="${row.date}" />
			<c:set var="currentdate3" value="${row.date}" />
			<c:set var="title" value="${row.title}" />

			<c:if test="${currentdate1!=currentdate2&&currentdate2!=null}">

				<c:out value="</ul></li>" escapeXml="false" />
				<c:out value="<br>" escapeXml="false" />
			</c:if>

			<c:if test="${currentdate1!=currentdate2}">
				<c:out
					value="<li><table><tr><td>Date: </td><td>${currentdate1 }</td></tr><tr><td>Description:</td> <td>${title}</td></tr></table><ul>"
					escapeXml="false" />
			</c:if>
			<!-- ID des Keys wird übergeben, mit der ein Key eindeutig identifiziert wird -->
			<c:out
				value="<li><input type='checkbox' name='mycheckbox' value='${id}'>${keypart}</li>"
				escapeXml="false" />

			<c:set var="currentdate2" value="${currentdate1}" />

		</c:forEach>
		</ul>
		</ul>

		<input type="submit" value="anfordern">
	</form>

</body>
</html>

<
