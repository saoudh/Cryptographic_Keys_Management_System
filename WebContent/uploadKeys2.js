/**
 * 
 */

var pgp_form = "<div id=\"form\" style=\"width:100%;\">"
		+ "<fieldset>"
		+ " <legend style=\"color:blue;font-weight:bold;\">User Information</legend>"
		+ " <table>" + "  <tr>" + " <td>Description:</td>"
		+ " <td><input type=\"text\" name=\"description\" required/></td>"
		+ "</tr>" + "<tr>" + " <td>Public Key:</td>"
		+ "<td><input type=\"file\" name=\"publickey\" required/></td>"
		+ " </tr>" + "<tr>" + " <td>Secret Key:</td>"
		+ "<td><input type=\"file\" name=\"privatekey\" required/></td>"
		+ " </tr>" + "</table>" + "</fieldset>" + "</div>"

var keypair_form = "<div id=\"form\" style=\"width:100%;\">"
		+ "<fieldset>"
		+ " <legend style=\"color:blue;font-weight:bold;\">User Information</legend>"
		+ " <table>" + "  <tr>" + " <td>Description:</td>"
		+ " <td><input type=\"text\" name=\"description\" required/></td>"
		+ "</tr>" + "<tr>" + " <td>Public Key:</td>"
		+ "<td><input type=\"file\" name=\"publickey\" required/></td>"
		+ " </tr>" + "<tr>" + " <td>Private Key:</td>"
		+ "<td><input type=\"file\" name=\"privatekey\" required/></td>"
		+ " </tr>" + "</table>" + "</fieldset>" + "</div>"

var ssl_form = "<div id=\"form\" style=\"width:100%;\">"
		+ "<fieldset>"
		+ " <legend style=\"color:blue;font-weight:bold;\">User Information</legend>"
		+ " <table>" + "  <tr>" + " <td>Description:</td>"
		+ " <td><input type=\"text\" name=\"description\" required/></td>"
		+ "</tr>" + "<tr>" + " <td>Public Key:</td>"
		+ "<td><input type=\"file\" name=\"publickey\" required/></td>"
		+ " </tr>" + "<tr>" + " <td>Private Key:</td>"
		+ "<td><input type=\"file\" name=\"privatekey\" required/></td>"
		+ " </tr>" + "<tr>" + " <td>Certificate:</td>"
		+ "<td><input type=\"file\" name=\"certificate\" required/></td>"
		+ " </tr>" + "</table>" + "</fieldset>" + "</div>"

$(document).ready(function() {
	var keytype = "pgp";
	alert("ddd");

	//Check first which radio Button is selected and load the respective form
	if ($('input:radio[name="myradiobtns"]').val() == 'pgp') {
		$("#form").remove();
		$("#appending_form").append(pgp_form);

	} else if ($('input:radio[name="myradiobtns"]').val() == 'cert') {
		$("#form").remove();
		$("#appending_form").append(ssl_form);
	} else if ($('input:radio[name="myradiobtns"]').val() == 'keypair') {
		$("#form").remove();
		$("#appending_form").append(keypair_form);

	}

	//When Radio Button changed, then show the respective form
	$('input:radio[name="myradiobtns"]').change(function() {

		if ($(this).val() == 'pgp') {
			keytype = "pgp";
			$("#form").remove();
			$("#appending_form").append(pgp_form);

		} else if ($(this).val() == 'cert') {
			keytype = "cert";
			$("#form").remove();
			$("#appending_form").append(ssl_form);
		} else if ($(this).val() == 'keypair') {

			keytype = "keypair";
			$("#appending_form").append(keypair_form);

			$("#form").remove();
		}

	});

	$("#myform").submit(function(e) {
		var valid = true;
		//set value of hidden value to tell the servlet which keytype was selected
		$('input:hidden[name="keytype"]').val(keytype);

		return true; //now we can submit, after setting value of hidden input
	});

});