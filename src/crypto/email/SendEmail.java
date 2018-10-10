package crypto.email;
import java.util.Properties;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;


//Als Singleton-Pattern implementiert!

public class SendEmail {
	 boolean debug;
	   Properties properties;
	   Authenticator authenticator;
	private String subject,  body,  to;
    private static SendEmail instance = null;
    //IP-Adresse des email-Servers. Muss angepasst werden!
    private static String outgoingMailServer = "192.168.199.151";    

public void sendEmail(String subject, String body, String to)
{
	 Session session = Session.getDefaultInstance(properties, authenticator);
	   session.setDebug(debug);

	   //message session
	   Message msg = new MimeMessage(session);

	   //addresses, sender and receiver 
	   InternetAddress fromAddress = null;
	   try {
		   /* 
		    * fromAddress: User-Name vom Absender. Über diesen Absender werden die emails mit den URL's mit 
		   * den Krypto-Schlüsseln versendet
		   */
		fromAddress = new InternetAddress("emailuser");
	} catch (AddressException e) {
		e.printStackTrace();
	}
	   try {
		msg.setFrom(fromAddress);
	} catch (MessagingException e) {
		e.printStackTrace();
	}
	   
	
	   //Recipients: Empfänger-Email-Adressen
	   List<String> recipients=new ArrayList<String>();
	   recipients.add(to);
	   
	   //Umwandeln der Empfänger-Adressen von String in InternetAddress
	   InternetAddress[] toAddress = new InternetAddress[recipients.size()];
	   for (int i = 0; i < recipients.size(); i++) {
	       try {
			toAddress[i] = new InternetAddress(recipients.get(i));
		} catch (AddressException e) {
			e.printStackTrace();
		}
	   }
	   try {
		msg.setRecipients(Message.RecipientType.TO, toAddress);
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	   //Betreff setzen
	   try {
		msg.setSubject(subject);
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   try {
		msg.setContent(body, "text/html; charset=utf-8");
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

//Mit Server verbinden
	   Transport transport = null;
	try {
		transport = session.getTransport("smtp");

	} catch (NoSuchProviderException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   try {

		transport.connect(outgoingMailServer, 25, "emailuser", "emailuser");
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   //Email versenden
	   try {
		transport.sendMessage(msg, msg.getAllRecipients());
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   try {
		transport.close();
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	   

}
	
	



public static SendEmail getInstance() {
    if (instance == null) {
        synchronized(SendEmail.class) {
            instance = new SendEmail();
        }
    }
    return instance;
}

public SendEmail()
{
	System.out.println("Start");
	    //String outgoingMailServer = "10.255.0.14";    

		   boolean debug = false;
	   
	 //set the host outgoing mail smtp server.
	    properties = new Properties();
	   properties.put("mail.smtp.host", outgoingMailServer);
	   properties.put("mail.smtp.auth", "true");
	   //properties.put("mail.smtps.auth", "true");
	  //  properties.put("mail.smtp.auth", "true");
	  //properties.put("mail.smtp.host", "smtp.gmail.com");
	   properties.put("mail.smtp.port", "25");
	   properties.put("mail.transport.protocol", "smtp");
	   //Uproperties.put("mail.smtp.starttls.enable", "true");
      // properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

	   
	    authenticator = new Authenticator(){
		   public PasswordAuthentication getPasswordAuthentication()
		   {
			   return new PasswordAuthentication("emailuser","emailuser");
		   }
	   };
	  

	  
}}