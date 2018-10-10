
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">



<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="generateKeyStyles.css">
<link rel="stylesheet" type="text/css" href="indexStyles.css">

<style type="text/css">
#keypair {
	position: absolute;
	right: 2%;
}
</style>
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.5.0/jquery.min.js"></script>
<script type="text/javascript" src="generateKeys2.js">
	
</script>

<title>Generate your keys</title>

</head>
<body>
	<%@ include file="LoginStatus.jsp"%>

	<div class="keytypes">
		<form action="GenerateKeysServlet" method="post" id="myform">
			<div id="container" style="width: 100%">
				<div id="pgp">
					<label for="pgp" id="pgp1">PGP Keypair/Certificate</label> <input
						type="radio" name="myradiobtns" id="pgp1" value="pgp"
						checked="checked">
				</div>
				<div id="cert">
					<label for="cert" id="cert1">X.509-/SSL-Certificate</label> <input
						type="radio" name="myradiobtns" id="cert1" value="cert">
				</div>
				<div id="keypair">
					<label for="keypair" id="keypair1">just keypair</label> <input
						type="radio" name="myradiobtns" id="keypair1" value="keypair">
				</div>
			</div>
			<br> <br> <br> <label for="description"
				id="description">Description: </label> <input type="text"
				name="description" id="description""> <br> <br> <br>
			<div id="appending_form"></div>
			<input type="hidden" name="keytype" id="hiddeninput" value="pgp">


			<input type="submit" id="submit"> <br>
	</div>

	</form>



</body>
</html>