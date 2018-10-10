<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.io.*,java.util.*,java.sql.*"%>

<%@ page import="javax.servlet.http.*,javax.servlet.*"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jstl/sql_rt"%>
<c:set var="USER_ID" value="${sessionScope.USER_ID }" scope="session" />

<sql:setDataSource var="datasource" driver="com.mysql.cj.jdbc.Driver"
	url="jdbc:mysql://localhost/MyDB" user="root" password="thehorse" />
<sql:query dataSource="${datasource}" var="result">select k.title as title,k.filename as filename,k.date as date, k.keypart as keypart from requestedkeys rk left join (url u, mykeys k, user us) on (k.user_id=us.id and rk.id=u.id and rk.mykeys_id=k.id) where u.url=? and us.id=?   
<sql:param value="${param.myurl}" />
	<sql:param value="${sessionScope.USER_ID}" />

</sql:query>
<html>
<head>
<link rel="stylesheet" type="text/css" href="indexStyles.css">

<style>
ul {
	list-style-type: none;
}
</style>
<title>Download Keys</title>
</head>
<body>
	<c:out value="url=${param.myurl} - userid=${ sessionScope.USER_ID}" />

	<%@ include file="LoginStatus.jsp"%>

	<ul style="list-style-type: none">
		<c:forEach items="${result.rows}" var="row">


			<c:set var="keytype" value="${row.keytype}" />
			<c:set var="id" value="${row.id}" />
			<c:set var="keypart" value="${row.keypart}" />
			<!--  Das Datum vom aktuellen Datensatz und vom nächsten holen, um 
				vergleichen zu können, ob die Schlüssel zusammengehören -->
			<c:set var="currentdate1" value="${row.date}" />
			<c:set var="title" value="${row.title}" />
			<c:set var="filename" value="${row.filename}" />

			<c:if test="${currentdate1!=currentdate2}">

				<c:out value="</ul></li>" escapeXml="false" />
				<c:out value="<br>" escapeXml="false" />
			</c:if>

			<c:if test="${currentdate1!=currentdate2}">
				<c:out value="<br>" escapeXml="false" />
				<c:out
					value="<li><table><tr><td>Date: </td><td>${currentdate1 }</td></tr><tr><td>Description:</td> <td>${title}</td></tr></table><ul>"
					escapeXml="false" />
			</c:if>
			<!-- ID des Keys wird übergeben, mit der ein Key eindeutig identifiziert wird -->

			<c:out
				value="<li><a href='DownloadKeysServlet?fileName=${filename}'> ${keypart}</a></li>"
				escapeXml="false" />




			<c:set var="currentdate2" value="${currentdate1}" />

		</c:forEach>
	</ul>
	</ul>



</body>
</html>
