package crypto.generate_keys;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.openpgp.PGPKeyRingGenerator;
import org.bouncycastle.operator.OperatorCreationException;

import crypto.MyDB;
import crypto.email.SendEmail;

/**
 * Servlet implementation class GenerateKeysServlet
 */
@WebServlet("/ProcessRequestedKeys")
public class ProcessRequestedKeys extends HttpServlet {
	private static final long serialVersionUID = 1L;
	int user_id;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProcessRequestedKeys() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession mySession = request.getSession();
		user_id = (int) mySession.getAttribute("USER_ID");
		mySession.setAttribute("USER_ID", user_id);

		// Auswahl der schlüssel in Array schreiben
		String[] myvalues = null;
		try {
			myvalues = request.getParameterValues("mycheckbox");

		} catch (NullPointerException e) {

		}
		// Zufällige Url generieren und in Datenbank schreiben
		String myurl = generateURL(16);
		int oneHourInMillisec = 1000 * 60 * 60;
		long expireDate = new Date().getTime() + oneHourInMillisec;
		boolean isInserted = MyDB.insertRequestedKeys(user_id, myvalues, myurl, expireDate);

		// Email-Versand vorbereiten
		String subject = "Requested Key";
		String body = "Here is the link for your requested Keys: "
				+ "https://localhost:8443/CryptoServer2/DownloadKeysForm.jsp?myurl=" + myurl;
		String to = MyDB.getEmailAddressByUserID(user_id);

		/*
		 * Je nachdem ob email versendet wurde wird die Startseite oder die
		 * gleiche Seite neu aufgerufen und ein entsprechender Hinweis angezeigt
		 */
		if (isInserted) {
			// Send Email with link to the requested keys
			SendEmail.getInstance().sendEmail(subject, body, to);
			RequestDispatcher rs = request.getRequestDispatcher("index.jsp");
			rs.include(request, response);
			response.getWriter().println("Key inserted!");
		} else {
			RequestDispatcher rs = request.getRequestDispatcher("ProcessRequestServlet");
			rs.include(request, response);
			response.getWriter().println("Keys not inserted!");
		}
	}

	// Url zufällig erzeugen
	public static String generateURL(int size) {
		SecureRandom sr = new SecureRandom();
		byte[] bytes = new byte[size];
		sr.nextBytes(bytes);
		return new BigInteger(1, bytes).toString(16);
	}

}
