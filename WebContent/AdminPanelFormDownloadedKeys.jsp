<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.io.*,java.util.*,java.sql.*"%>

<%@ page import="javax.servlet.http.*,javax.servlet.*"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jstl/sql_rt"%>
<c:set var="USER_ID" value="${sessionScope.USER_ID}" scope="session" />

<sql:setDataSource var="datasource" driver="com.mysql.cj.jdbc.Driver"
	url="jdbc:mysql://localhost/MyDB" user="root" password="thehorse" />
<sql:query dataSource="${datasource}" var="result">	select u.expire_date as expire_date, us.name as name,  u.url as url, k.title as title, k.keytype as keytype, k.keypart as keypart from requestedkeys rk 
	left join (url u, mykeys k, user us) 
	on (k.user_id=us.id and rk.id=u.id and rk.mykeys_id=k.id) ORDER BY expire_date DESC  
	


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
	<h5>Downloaded Keys</h5>
	<table style="width: 80%">

		<tr>
			<th>Date</th>
			<th>Name</th>
			<th>Title</th>
			<th>Keytype</th>
			<th>Keypart</th>
		</tr>
		<c:forEach items="${result.rows}" var="row">

			<c:set var="expire_date" value="${row.expire_date}" />
			<c:set var="url" value="${row.url}" />
			<c:set var="title" value="${row.title}" />
			<c:set var="filename" value="${row.filename}" />
			<c:set var="keytype" value="${row.keytype}" />
			<c:set var="keypart" value="${row.keypart}" />
			<c:set var="name" value="${row.name}" />


			<c:out value="<tr>" escapeXml="false" />

			<c:out value="<td>${expire_date}</td>" escapeXml="false" />
			<c:out value="<td>${name}</td>" escapeXml="false" />
			<c:out value="<td>${title}</td>" escapeXml="false" />
			<c:out value="<td>${keytype}</td>" escapeXml="false" />
			<c:out value="<td>${keypart}</td>" escapeXml="false" />

			<c:out value="</tr>" escapeXml="false" />

		</c:forEach>
	</table>



</body>
</html>
