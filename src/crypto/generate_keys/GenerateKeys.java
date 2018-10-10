package crypto.generate_keys;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.encoders.Hex;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import sun.security.provider.MD5;

public class GenerateKeys {
	private final static String CA_CERT_FILENAME = "/Users/admin/Documents/tomcat.cer";
	private static final AlgorithmParameterSpec KEY_SIZE = null;
	private static PrivateKey CA_PRIVATE_KEY = null;
	private static Certificate CA_CERT = null;
	private static X500Name issDN_x500 = null;

	private static GenerateKeys instance = null;

	protected GenerateKeys() {
		init();
	}

	public static GenerateKeys getInstance() {
		if (instance == null) {
			instance = new GenerateKeys();
		}
		return instance;
	}

	public void init() {
		// Server-Zertifikats-Infos
		X500NameBuilder namebld = new X500NameBuilder(BCStyle.INSTANCE);
		namebld.addRDN(BCStyle.CN, "localhost");
		namebld.addRDN(BCStyle.OU, "Fachbereich Informatik");
		namebld.addRDN(BCStyle.O, "TU Darmstadt");
		namebld.addRDN(BCStyle.L, "Darmstadt");
		namebld.addRDN(BCStyle.ST, "Hessen");
		namebld.addRDN(BCStyle.C, "DE");
		namebld.addRDN(BCStyle.EmailAddress, "hs3000@live.de");
		issDN_x500 = namebld.build();

	}

	private static X500Name getCA_X500_Name() {
		// Server-Zertifikats-Infos

		X500NameBuilder namebld = new X500NameBuilder(BCStyle.INSTANCE);
		namebld.addRDN(BCStyle.CN, "localhost");
		namebld.addRDN(BCStyle.OU, "Fachbereich Informatik");
		namebld.addRDN(BCStyle.O, "TU Darmstadt");
		namebld.addRDN(BCStyle.L, "Darmstadt");
		namebld.addRDN(BCStyle.ST, "Hessen");
		namebld.addRDN(BCStyle.C, "DE");
		namebld.addRDN(BCStyle.EmailAddress, "hs3000@live.de");
		return namebld.build();
	}

	// Zertifikat für User erstellen
	public static X509Certificate makeCertificate(KeyPair subKP, X500Name subDN)
			throws GeneralSecurityException, IOException, OperatorCreationException {

		PublicKey subPub = subKP.getPublic();

		Date startDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
		Date expiryDate = new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000);

		BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());

		X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(getCA_X500_Name(), serialNumber, startDate,
				expiryDate, subDN, SubjectPublicKeyInfo.getInstance(subKP.getPublic().getEncoded()));
		JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256withRSA");
		ContentSigner signer = builder.build(getPrivateKey());

		byte[] certBytes = certBuilder.build(signer).getEncoded();
		CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
		X509Certificate certificate = (X509Certificate) certificateFactory
				.generateCertificate(new ByteArrayInputStream(certBytes));

		return certificate;
	}

	// Private Key des Servers zurückgeben
	private static PrivateKey getPrivateKey() throws IOException {
		if (CA_PRIVATE_KEY != null) {
			return CA_PRIVATE_KEY;
		}
		String keyPath = "/Users/admin/Documents/server_private.key";
		BufferedReader br = new BufferedReader(new FileReader(keyPath));
		PEMParser pp = new PEMParser(br);
		PEMKeyPair pemKeyPair = (PEMKeyPair) pp.readObject();
		KeyPair kp = new JcaPEMKeyConverter().getKeyPair(pemKeyPair);
		pp.close();
		System.out.println("Myprivate key: " + kp.getPrivate());
		CA_PRIVATE_KEY = kp.getPrivate();
		return kp.getPrivate();

	}

	// Zertifikat vom Server zurückgeben
	private Certificate getCAcertificateFromFile() {
		if (CA_CERT != null) {
			return CA_CERT;
		}

		File myFile = null;
		try {
			myFile = new File(CA_CERT_FILENAME);
		} catch (Exception e) {
			System.out.println("exception: " + e.getMessage());
		}
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(myFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedInputStream bis = new BufferedInputStream(fis);

		CertificateFactory cf = null;
		try {
			cf = CertificateFactory.getInstance("X.509");
		} catch (CertificateException e) {
			e.printStackTrace();
		}

		try {
			while (bis.available() > 0) {
				CA_CERT = cf.generateCertificate(bis);
				System.out.println(CA_CERT.toString());
			}
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return CA_CERT;

	}

	// Bytes vom Schlüssel in eine Datei schreiben
	public static boolean writeBytesToFile(byte[] mybytes, String filepath) {
		File file = new File(filepath);

		try {
			// Write to file
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			BufferedOutputStream buff_out = new BufferedOutputStream(out);
			buff_out.write(mybytes);

			buff_out.flush();
			out.close();
			buff_out.close();
		} catch (IOException e) {
			return false;
		}
		return true;

	}

	// Sha3-Hash aus String erzeugen
	public static String makeSHA3Hash(String input) {
		SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest224();
		byte[] digest = digestSHA3.digest(input.getBytes());

		return Hex.toHexString(digest);

	}

	// Schlüssel aus Datei lesen
	public Key readKeyFromFile(String path) {
		PublicKey reconstructed_pub_key = null;
		try {
			// Read from file
			FileInputStream in = new FileInputStream(path);
			byte[] pub_key_arr = new byte[in.available()];
			in.read(pub_key_arr, 0, in.available());
			in.close();

			// Reconstruct public key
			reconstructed_pub_key = reconstruct_public_key("RSA", pub_key_arr);
		} catch (IOException e) {
		}
		return reconstructed_pub_key;
	}

	// Schlüsse-Datei löschen
	public static void deleteFile(String path) {

		try {
			Files.delete(Paths.get(path));
		} catch (NoSuchFileException x) {
			System.err.format("%s: no such" + " file or directory%n", path);
		} catch (DirectoryNotEmptyException x) {
			System.err.format("%s not empty%n", path);
		} catch (IOException x) {
			// File permission problems are caught here.
			System.err.println(x);
		}
	}

	// Einfachen Keypair erzeugen
	public static KeyPair generateKeyPair() {

		// create keypair
		Security.addProvider(new BouncyCastleProvider());

		KeyPairGenerator keypairGen = null;

		try {
			keypairGen = KeyPairGenerator.getInstance("RSA", "BC");
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			e.printStackTrace();
		}

		keypairGen.initialize(4096, new SecureRandom());
		KeyPair keypair = keypairGen.generateKeyPair();
		return keypair;
	}

	// Public Key-Objekt erzeugen aus gelesener public-Key-Datei im
	// Byte-Array-Format
	public static PublicKey reconstruct_public_key(String algorithm, byte[] pub_key) {
		PublicKey public_key = null;

		try {
			KeyFactory kf = KeyFactory.getInstance(algorithm);
			EncodedKeySpec pub_key_spec = new X509EncodedKeySpec(pub_key);
			public_key = kf.generatePublic(pub_key_spec);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Could not reconstruct the public key, the given algorithm oculd not be found.");
		} catch (InvalidKeySpecException e) {
			System.out.println("Could not reconstruct the public key");
		}

		return public_key;
	}

	// Verschlüsselte Zip-Datei erstellen
	public static void writeEncryptedZipFile(String filepath, String pass) {
		ZipFile zipFile = null;

		try {
			zipFile = new ZipFile(filepath + "." + "zip");
		} catch (ZipException e) {
			e.printStackTrace();
		}
		ZipParameters parameters = new ZipParameters();
		parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

		parameters.setEncryptFiles(true);

		parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);

		parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);

		parameters.setPassword(pass);

		try {
			zipFile.addFile(new File(filepath), parameters);
		} catch (ZipException e) {
			e.printStackTrace();
		}

	}

}
