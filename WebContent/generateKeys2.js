/**
 * 
 */

var pgp_form="<div id=\"form\" style=\"width:100%;\">"
  + "<fieldset>"
     +" <legend style=\"color:blue;font-weight:bold;\">User Information</legend>"
     +" <table>"
    
         +"<tr>"
           +" <td>Passphrase:</td>"
            +"<td><input name=\"pass\" type=\"password\"/></td>"
        +" </tr>"
      +"</table>"
   +"</fieldset>"
+"</div>";

var keypair_form="<div id=\"form\" style=\"width:100%;\">"
	  + "<fieldset>"
	     +" <legend style=\"color:blue;font-weight:bold;\">User Information</legend>"
	     +" <table>"
	   
	     +"<tr>"
	     +" <td>password for AES encryption (private key): </td>"
	     +" <td><input type=\"password\" name=\"pass\" /></td>"
	   +"</tr>"
	      +"</table>"
	   +"</fieldset>"
	+"</div>";
	
var ssl_form="<div id=\"form\" style=\"width:100%;\">"
 +  "<fieldset>"
     +" <legend style=\"color:blue;font-weight:bold;\">User Information</legend>"
     +" <table>"
     +"<tr>"
     +" <td>AES-Password for encryption of private key: </td>"
     +" <td><input type=\"password\" name=\"pass\" type=\"password\"/></td>"
   +"</tr>"
         +"<tr>"
           +" <td>Host Name / User Name [CN]:</td>"
           +" <td><input type=\"text\" name=\"cn\"/></td>"
         +"</tr>"
         +" <tr>"
          +"  <td>Email Address:</td>"
          + " <td><input type=\"text\" name=\"email\"/></td>"
        +" </tr>"
        +" <tr>"
           +" <td>Organization [O]:</td>"
           +" <td><input type=\"text\" name=\"o\"/></td>"
         +"</tr>"
          +" <tr>"
            +"<td>Organization Unit [OU]:</td>"
           +" <td><input type=\"text\" name=\"ou\"/></td>"
         +"</tr>"
        +" <tr>"
           +" <td>City [L]:</td>"
           +" <td><input type=\"text\" name=\"l\"/></td>"
        +" </tr>"
         +" <tr>"
           +" <td>Country [C]:</td>"
            +"<td><input type=\"text\" name=\"c\"/></td>"
         +"</tr>"
        +" <tr>"
           +" <td>State [ST]:</td>"
            +"<td><input type=\"text\" name=\"st\"/></td>"
         +"</tr>"
     +" </table>"
  +" </fieldset>"
+"</div>";

$(document).ready(function() {
	
	var keytype="pgp";

//Check first which radio Button is selected and load the respective form
 if ($('input:radio[name="myradiobtns"]').val() == 'pgp') {
               $("#form").remove();
$("#appending_form").append(pgp_form);
		
	  } 
      else if($('input:radio[name="myradiobtns"]').val() == 'cert') {
      $("#form").remove();
      $("#appending_form").append(ssl_form);
      }
      else
      {
            $("#form").remove();
            $("#appending_form").append(keypair_form);

		}
        



//When Radio Button changed, then show the respective form
$('input:radio[name="myradiobtns"]').change(function() {

	  if ($(this).val() == 'pgp') {
		  keytype="pgp";
              $("#form").remove();
			$("#appending_form").append(pgp_form);

	  } 
      else if($(this).val() == 'cert') {
    	  keytype="cert";
      $("#form").remove();
      $("#appending_form").append(ssl_form);
      }
      else
      {
    	  
    	  keytype="keypair";
            $("#form").remove();
            $("#appending_form").append(keypair_form);

		}

      
	});

//Pass Key Type within a hidden input to Servlet before submitting the form
/*$("#submit").closest('form').on('submit', function(e) {
	e.preventDefault();
	//$('input:hidden[name="keytype"]').val($('input:radio[name="myradiobtns"]').val());
	alert("type="+$('input:radio[name="myradiobtns"]').val());

	//this.submit(); //now submit the form
});
*/

$("#myform").submit(function(e){
   // e.preventDefault();

	//set value of hidden value to tell the servlet which keytype was selected
  	$('input:hidden[name="keytype"]').val(keytype);
  	
	
  	return true;	//now we can submit, after setting value of hidden input
	});




});