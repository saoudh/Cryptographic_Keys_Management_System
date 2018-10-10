package crypto.generate_keys;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
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
import crypto.MyKey;
import crypto.MyDB.KEYPART;
import crypto.MyDB.KEYTYPE;

/**
 * Servlet implementation class GenerateKeysServlet
 */
@WebServlet("/GenerateKeysServlet")
public class GenerateKeysServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	int user_id;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GenerateKeysServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Übergebene Parameter abfragen und in Variablen schreiben
		HttpSession mySession = request.getSession();
		user_id = (int) mySession.getAttribute("USER_ID");

		String keytype = request.getParameter("keytype");
		boolean isInserted = false;
		// Je nachdem welcher Schlüssel generiert werden soll wird
		// unterschiedliche Methode aufgerufen
		switch (keytype) {
		case "cert":
			isInserted = processCert(request);
			break;
		case "keypair":
			isInserted = processKeypair(request);
			break;
		case "pgp":
			isInserted = processPGP(request);
			break;
		default:
			System.out.println("Error, no keytype-info transmitted");
		}

		/*
		 * Je nachdem ob Schlüssel erfolgreich oder nicht erfolgreich generiert
		 * werden konnte, dann wird entsprechender Hinweis angezeigt und die
		 * selbe Seite wieder aufgerufen
		 */
		if (isInserted) {
			RequestDispatcher rs = request.getRequestDispatcher("GenerateKeysForm.jsp");
			rs.include(request, response);

			response.getWriter().println("Key inserted!");

		} else {
			RequestDispatcher rs = request.getRequestDispatcher("GenerateKeysForm.jsp");
			rs.include(request, response);
			response.getWriter().println("Key not inserted!");
		}
	}

	// PGP Generierung bearbeiten
	private boolean processPGP(HttpServletRequest request) {
		boolean[] isInserted = new boolean[2];

		String email = request.getParameter("email");
		String pass = request.getParameter("pass");
		System.out.println("email=" + email);
		System.out.println("pass=" + pass);
		String description = request.getParameter("description");

		long currentDate = new Date().getTime();

		String hashOfUserID = GenerateKeys.makeSHA3Hash(String.valueOf(user_id));

		// mit SHA3(usser_id) wird Verzeichnisname von User und currentDate der
		// Dateiname gekennzeichnet
		String filepath = request.getServletContext().getAttribute("FILES_DIR") + File.separator + hashOfUserID
				+ File.separator + String.valueOf(currentDate);
		System.out.println("Filepath=" + filepath);

		PGPKeyRingGenerator krgen = GeneratePGPKeys.init(email, pass.toCharArray());
		GeneratePGPKeys.writeKeyRingToFile(krgen.generatePublicKeyRing(), filepath + ".pub");
		GeneratePGPKeys.writeKeyRingToFile(krgen.generateSecretKeyRing(), filepath + ".priv");

		// Infos zu den PGP Schlüsseln in die Datenbank schreiben
		MyKey mykey = new MyKey(user_id, currentDate, description, filepath + ".pub", MyDB.KEYTYPE.PGP,
				MyDB.KEYPART.PUBLIC);
		isInserted[0] = MyDB.insertKeyToDB(mykey);

		mykey = new MyKey(user_id, currentDate, description, filepath + ".priv", MyDB.KEYTYPE.PGP,
				MyDB.KEYPART.PRIVATE);
		isInserted[1] = MyDB.insertKeyToDB(mykey);

		for (int i = 0; i < isInserted.length; i++) {
			if (!isInserted[i])
				return false;
		}
		return true;
	}

	// einfachen Schlüsselpaar generieren
	private boolean processKeypair(HttpServletRequest request) {
		boolean[] isInserted = new boolean[2];

		String pass = request.getParameter("pass");

		String description = request.getParameter("description");
		KeyPair kp = GenerateKeys.generateKeyPair();

		long currentDate = new Date().getTime();
		System.out.println("User_id=" + user_id);

		String hashOfUserID = GenerateKeys.makeSHA3Hash(String.valueOf(user_id));

		// mit SHA3(usser_id) wird Verzeichnisname von User und currentDate der
		// Dateiname gekennzeichnet
		String filepath = request.getServletContext().getAttribute("FILES_DIR") + File.separator + hashOfUserID
				+ File.separator + String.valueOf(currentDate);
		System.out.println("Filepath=" + filepath);
		boolean isWritten = GenerateKeys.writeBytesToFile(kp.getPrivate().getEncoded(), filepath + ".pri");

		/*
		 * If the private Key was written, then encrypt it and delete the
		 * original .priv file It is possible to work with the byte array
		 * without saving it first to a file and use the filestream, but
		 * encryption does not work then
		 */
		if (isWritten) {
			GenerateKeys.writeEncryptedZipFile(filepath + ".pri", pass);
			GenerateKeys.deleteFile(filepath + ".pri");
		}

		GenerateKeys.writeBytesToFile(kp.getPublic().getEncoded(), filepath + ".pub");

		MyKey mykey = new MyKey(user_id, currentDate, description, filepath + ".pub", MyDB.KEYTYPE.KEYPAIR,
				MyDB.KEYPART.PUBLIC);
		isInserted[0] = MyDB.insertKeyToDB(mykey);

		mykey = new MyKey(user_id, currentDate, description, filepath + ".priv", MyDB.KEYTYPE.KEYPAIR,
				MyDB.KEYPART.PRIVATE);
		isInserted[1] = MyDB.insertKeyToDB(mykey);

		for (int i = 0; i < isInserted.length; i++) {
			if (!isInserted[i])
				return false;
		}
		return true;
	}

	// Zertifikat erstellen
	private boolean processCert(HttpServletRequest request) {
		boolean[] isInserted = new boolean[3];
		String email = request.getParameter("email");
		String cn = request.getParameter("cn");
		String o = request.getParameter("o");
		String ou = request.getParameter("ou");
		String l = request.getParameter("l");
		String c = request.getParameter("c");
		String st = request.getParameter("st");
		String description = request.getParameter("description");
		String pass = request.getParameter("pass");

		X500NameBuilder namebld = new X500NameBuilder(BCStyle.INSTANCE);
		namebld.addRDN(BCStyle.CN, cn);
		namebld.addRDN(BCStyle.OU, ou);
		namebld.addRDN(BCStyle.O, o);
		namebld.addRDN(BCStyle.L, l);
		namebld.addRDN(BCStyle.ST, st);
		namebld.addRDN(BCStyle.C, c);
		namebld.addRDN(BCStyle.EmailAddress, email);
		X500Name subDN = namebld.build();

		System.out.println("email=" + email);
		System.out.println("cn=" + cn);

		long currentDate = new Date().getTime();
		KeyPair clientKP = GenerateKeys.generateKeyPair();
		System.out.println("User_id=" + user_id);

		String hashOfUserID = GenerateKeys.makeSHA3Hash(String.valueOf(user_id));

		/*
		 * mit SHA3(usser_id) wird Verzeichnisname von User und currentDate der
		 * Dateiname gekennzeichnet
		 */

		String filepath = request.getServletContext().getAttribute("FILES_DIR") + File.separator + hashOfUserID
				+ File.separator + String.valueOf(currentDate);
		System.out.println("Filepath=" + filepath);
		boolean isWritten = GenerateKeys.writeBytesToFile(clientKP.getPrivate().getEncoded(), filepath + ".pri");

		/*
		 * If the private Key was written, then encrypt it and delete the
		 * original .priv file It is possible to work with the byte array
		 * without saving it first to a file and use the filestream, but
		 * encryption does not work then
		 */
		if (isWritten) {
			GenerateKeys.writeEncryptedZipFile(filepath + ".pri", pass);
			GenerateKeys.deleteFile(filepath + ".pri");
		}

		GenerateKeys.writeBytesToFile(clientKP.getPublic().getEncoded(), filepath + ".pub");

		MyKey mykey = new MyKey(user_id, currentDate, description, filepath + ".pub", MyDB.KEYTYPE.X509,
				MyDB.KEYPART.PUBLIC);
		isInserted[0] = MyDB.insertKeyToDB(mykey);

		mykey = new MyKey(user_id, currentDate, description, filepath + ".priv", MyDB.KEYTYPE.X509,
				MyDB.KEYPART.PRIVATE);
		isInserted[1] = MyDB.insertKeyToDB(mykey);

		try {
			Certificate mycert = GenerateKeys.makeCertificate(clientKP, subDN);
			GenerateKeys.writeBytesToFile(mycert.getEncoded(), filepath + ".cert");
			mykey = new MyKey(user_id, currentDate, description, filepath + ".cert", MyDB.KEYTYPE.X509,
					MyDB.KEYPART.CERT);
			isInserted[2] = MyDB.insertKeyToDB(mykey);
		} catch (OperatorCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < isInserted.length; i++) {
			if (!isInserted[i])
				return false;
		}
		return true;

	}

}
