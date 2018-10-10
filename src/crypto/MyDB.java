package crypto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

//Alle Datenbank-Operation finden in den Methoden dieser Klasse statt

public class MyDB {

	// Konstanten für Bezeichnung der Schlüsseltypen setzen
	public static class KEYTYPE {
		public final static String PGP = "PGP";
		public final static String X509 = "509";
		public final static String KEYPAIR = "KEYPAIR";
	}

	public static class KEYPART {
		public final static String PRIVATE = "PRIVATE";
		public final static String PUBLIC = "PUBLIC";
		public final static String CERT = "CERT";

	}

	// Datenbank-Einstellung setzen
	public static Connection initDB() {
		// loading drivers for mysql
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// creating connection with the database
		try {
			return DriverManager.getConnection("jdbc:mysql://localhost:3306/MyDB", "root", "thehorse");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// Generierten Schlüssel in Datenbank schreiben
	public static boolean insertKeyToDB(MyKey mykey) {
		System.out.println("MyDB-writeKeyToDB");
		boolean st = false;
		try {

			Connection con = initDB();
			PreparedStatement ps = con.prepareStatement(
					"Insert Into mykeys(title,filename,user_id,date,keytype,keypart) values(?,?,?,?,?,?)");
			ps.setString(1, mykey.getDescription());
			ps.setString(2, mykey.getFilepath());
			ps.setInt(3, mykey.getUser_id());
			ps.setTimestamp(4, new Timestamp(mykey.getCurrentDate()));
			ps.setString(5, mykey.getKeytype());
			ps.setString(6, mykey.getKeypart());

			int count = ps.executeUpdate();
			if (count > 0) {
				st = true;
			} else {
				return false;
			}

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	// Angeforderten Schlüssel in datenbank schreiben
	public static boolean insertRequestedKeys(int user_id, String[] myvalues, String myurl, long expireDate) {
		boolean isInserted = true;
		int url_id = insertIntoURLTable(myurl, expireDate);

		for (int i = 0; i < myvalues.length; i++) {

			isInserted = insertIntoRequestedKeysTable(Integer.parseInt(myvalues[i]), url_id);

		}
		return isInserted;

	}

	// In Tabelle RequestedKeys Infos schreiben
	private static boolean insertIntoRequestedKeysTable(int key_id, int url_id) {
		boolean st = false;

		try {

			Connection con = initDB();

			PreparedStatement ps = con.prepareStatement("Insert Into requestedkeys(id,mykeys_id) values(?,?)");
			ps.setInt(1, url_id);
			ps.setInt(2, key_id);

			int count = ps.executeUpdate();
			if (count > 0) {
				st = true;
			} else {
				st = false;
			}

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return st;
	}

	// Infos in URL-Tabelle schreiben
	private static int insertIntoURLTable(String myurl, long expireDate) {

		int url_id = -1;
		boolean st = false;
		try {

			Connection con = initDB();

			PreparedStatement ps = con.prepareStatement("Insert Into url(url, expire_date) values(?,?)");
			ps.setString(1, myurl);
			ps.setTimestamp(2, new Timestamp(expireDate));

			int count = ps.executeUpdate();
			if (count > 0) {
				ps = con.prepareStatement("Select last_insert_id()");
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					url_id = rs.getInt(1);

				}
			}

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url_id;
	}

	// prüfen ob user berechtigt ist url zu besuchen
	public static boolean checkIfURLBelongsToUser(int user_id, String myurl) throws SQLException {
		Connection con = initDB();
		PreparedStatement ps = con.prepareStatement(
				"select rk.id, rk.mykeys_id, u.url, k.title, us.id from requestedkeys rk left join (url u, mykeys k, user us) on (k.user_id=us.id and rk.id=u.id and rk.mykeys_id=k.id) where u.url=? and us.id=?  and u.expire_date>now()");
		ps.setString(1, myurl);
		ps.setInt(2, user_id);

		ResultSet rs = ps.executeQuery();
		int count = 0;
		while (rs.next()) {
			count++;

		}

		con.close();

		return count > 0 ? true : false;

	}

	// Login-Infos prüfe
	public static boolean checkUser(String email, String pass) {
		boolean st = false;
		try {

			Connection con = initDB();
			PreparedStatement ps = con.prepareStatement("select * from User where email=? and password=?");
			ps.setString(1, email);
			ps.setString(2, pass);
			ResultSet rs = ps.executeQuery();
			st = rs.next();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return st;
	}

	// User-ID zurückgeben
	public static int getUserID(String email) {
		boolean st = false;
		int id = -1;
		try {
			Connection con = initDB();
			PreparedStatement ps = con.prepareStatement("select id from User where email=?");
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if (st = rs.next()) {
				id = rs.getInt(1);
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return id;

	}

	// Neuen User hinzufügen
	public static boolean insertUser(String name, String email, String pass) {

		try {

			Connection con = initDB();

			PreparedStatement ps = con.prepareStatement("insert into User values(?,?,?)");

			ps.setString(1, name);
			ps.setString(2, email);
			ps.setString(3, pass);
			int i = ps.executeUpdate();

			if (i > 0) {

				return true;
			}

		} catch (Exception se) {
		}
		return false;
	}

	// User-Namen anhand der email-Adresse zurückgeben
	public static String getUserName(String email) {
		boolean st = false;
		String myname = null;

		try {

			Connection con = initDB();
			PreparedStatement ps = con.prepareStatement("select name from User where email=?");
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if (st = rs.next()) {
				myname = rs.getString(1);
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return myname;
	}

	// Email-Adresse durch User-ID zurückgeben
	public static String getEmailAddressByUserID(int user_id) {
		boolean st = false;
		String emailAddress = null;

		try {

			Connection con = initDB();
			PreparedStatement ps = con.prepareStatement("select email from User where id=?");
			ps.setInt(1, user_id);
			ResultSet rs = ps.executeQuery();
			if (st = rs.next()) {
				emailAddress = rs.getString(1);
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return emailAddress;
	}

}
