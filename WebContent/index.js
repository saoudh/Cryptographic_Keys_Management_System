/**
 * 
 */

var logged_in="<div id=\"top\"/>logged in as:</div>";

var login="<div id=\"top\"/>a href='LoginFormServlet'>login</a> / <a href='RegisterFormServlet'>register</a></div>";
         
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
		}
        



//When Radio Button changed, then show the respective form
$('input:radio[name="myradiobtns"]').change(function() {


	  if ($(this).val() == 'pgp') {
		  keytype="pgp";
              $("#form").remove();
			$("#appending_form").append(pgp_form);
		      alert($(this).val());

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
  	
	 // alert("frust");
  	//$(this).submit();
  	return true;	//now we can submit, after setting value of hidden input
  	//$("#myform").unbind().submit();
	});




});