<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.io.*,java.util.*,java.sql.*"%>

<%@ page import="javax.servlet.http.*,javax.servlet.*"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jstl/sql_rt"%>
<c:set var="USER_ID" value="${sessionScope.USER_ID}" scope="session" />

<sql:setDataSource var="datasource" driver="com.mysql.cj.jdbc.Driver"
	url="jdbc:mysql://localhost/MyDB" user="root" password="thehorse" />
<sql:query dataSource="${datasource}" var="result">select k.title as title,k.filename as filename,  us.name as name, k.date as date, k.keypart as keypart,k.keytype as keytype from mykeys k left join (user us) 
on (k.user_id=us.id)  
ORDER BY date DESC  


 </sql:query>
<html>
<head>
<link rel="stylesheet" type="text/css" href="indexStyles.css">

<style>
ul {
	list-style-type: none;
}

td {
	text-align: center;
}
</style>
<title>Admin Panel</title>
</head>
<body>
	<c:out value="url=${param.myurl} - userid=${ sessionScope.USER_ID}" />

	<%@ include file="LoginStatus.jsp"%>
	<h1>Admin Panel</h1>
	<p>
		<a href="AdminPanelFormGeneratedKeys.jsp">Generated Keys</a> / <a
			href="AdminPanelFormDownloadedKeys.jsp">Downloaded Keys</a>
	</p>
	<h5>Generated Keys</h5>

	<table style="width: 80%">

		<tr>
			<th>Date</th>
			<th>Name</th>
			<th>Title</th>
			<th>Keytype</th>
		</tr>
		<c:forEach items="${result.rows}" var="row">


			<c:set var="keytype" value="${row.keytype}" />
			<c:set var="name" value="${row.name}" />

			<c:set var="keypart" value="${row.keypart}" />
			<!--  Das Datum vom aktuellen Datensatz und vom nächsten holen, um 
				vergleichen zu können, ob die Schlüssel zusammengehören -->
			<c:set var="currentdate1" value="${row.date}" />
			<c:set var="title" value="${row.title}" />
			<c:set var="filename" value="${row.filename}" />


			<c:if test="${ currentdate1!=currentdate2}">
				<c:out value="<tr>" escapeXml="false" />


				<c:out value="<td>${currentdate1}</td>" escapeXml="false" />
				<c:out value="<td>${name}</td>" escapeXml="false" />
				<c:out value="<td>${title}</td>" escapeXml="false" />

				<c:out value="<td>${keytype}</td>" escapeXml="false" />
				</tr>
			</c:if>
			<c:set var="currentdate2" value="${currentdate1}" />

		</c:forEach>
	</table>



</body>
</html>
