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
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.bcpg.sig.Features;
import org.bouncycastle.bcpg.sig.KeyFlags;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPKeyRing;
import org.bouncycastle.openpgp.PGPKeyRingGenerator;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.bouncycastle.openpgp.operator.PBESecretKeyEncryptor;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyEncryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
import org.bouncycastle.openpgp.operator.bc.BcPGPKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

public class GeneratePGPKeys {
	private final static String CA_CERT_FILENAME = "/Users/admin/Documents/tomcat.cer";
	private static final AlgorithmParameterSpec KEY_SIZE = null;
	private static PrivateKey CA_PRIVATE_KEY = null;
	private static Certificate CA_CERT = null;
	private static X500Name issDN_x500 = null;

	protected GeneratePGPKeys() {

	}

	// KeyRing in Datei schreiben
	public static boolean writeKeyRingToFile(PGPKeyRing keyring, String filepath) {

		BufferedOutputStream pubout = null;
		try {
			pubout = new BufferedOutputStream(new FileOutputStream(filepath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		try {
			keyring.encode(pubout);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		try {
			pubout.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// Erzeugung eines PGPKeyRingGenerator einleiten
	public static PGPKeyRingGenerator init(String identity, char[] pass) {

		PGPKeyRingGenerator krgen = null;
		try {
			krgen = generateKeyRingGenerator(identity, pass);
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}

		return krgen;

	}

	/*
	 * PGP-Schlüssel erzeugen ohne den s2count parameter. Dafür wird die gleiche
	 * Methode, aber mit Standard-Wert von 0xc0 für s2count gesetzt
	 */
	public final static PGPKeyRingGenerator generateKeyRingGenerator(String id, char[] pass) throws Exception {
		return generateKeyRingGenerator(id, pass, 0xc0);
	}

	// PGP-KeyRingGenerator erzeugen
	public final static PGPKeyRingGenerator generateKeyRingGenerator(String id, char[] pass, int s2kcount)
			throws Exception {

		RSAKeyPairGenerator kpg = new RSAKeyPairGenerator();

		// Schlüssellänge ist 2048 Bits
		kpg.init(new RSAKeyGenerationParameters(BigInteger.valueOf(0x10001), new SecureRandom(), 2048, 12));

		// Signing/Master-Key mit Generator erzeugen
		PGPKeyPair rsakp_sign = new BcPGPKeyPair(PGPPublicKey.RSA_SIGN, kpg.generateKeyPair(), new Date());
		// Dann einen encryption-subkey
		PGPKeyPair rsakp_enc = new BcPGPKeyPair(PGPPublicKey.RSA_ENCRYPT, kpg.generateKeyPair(), new Date());

		// Eigen-Signatur erzeugen
		PGPSignatureSubpacketGenerator signhashgen = new PGPSignatureSubpacketGenerator();

		/*
		 * Metadaten für Signatur angeben
		 */
		// Zweck der Signatur
		signhashgen.setKeyFlags(false, KeyFlags.SIGN_DATA | KeyFlags.CERTIFY_OTHER);

		// Prioritäten für Krypto-Algorithm. für Senden von Nachrichten
		signhashgen.setPreferredSymmetricAlgorithms(false, new int[] { SymmetricKeyAlgorithmTags.AES_256,
				SymmetricKeyAlgorithmTags.AES_192, SymmetricKeyAlgorithmTags.AES_128 });
		signhashgen.setPreferredHashAlgorithms(false, new int[] { HashAlgorithmTags.SHA256, HashAlgorithmTags.SHA1,
				HashAlgorithmTags.SHA384, HashAlgorithmTags.SHA512, HashAlgorithmTags.SHA224, });

		// Sender auffordern Checksumme zu erzeugen
		signhashgen.setFeature(false, Features.FEATURE_MODIFICATION_DETECTION);

		// Signatur auf dem Encryption-Subkey erzeugen
		PGPSignatureSubpacketGenerator enchashgen = new PGPSignatureSubpacketGenerator();

		// Meta-Daten für den Zweck angeben
		enchashgen.setKeyFlags(false, KeyFlags.ENCRYPT_COMMS | KeyFlags.ENCRYPT_STORAGE);

		// für die Versclüsselung des Secret-Key
		PGPDigestCalculator sha1Calc = new BcPGPDigestCalculatorProvider().get(HashAlgorithmTags.SHA1);
		PGPDigestCalculator sha256Calc = new BcPGPDigestCalculatorProvider().get(HashAlgorithmTags.SHA256);

		// SecretKey-Encrypter
		PBESecretKeyEncryptor pske = (new BcPBESecretKeyEncryptorBuilder(PGPEncryptedData.AES_256, sha256Calc,
				s2kcount)).build(pass);

		// KeyRing erzeugen
		PGPKeyRingGenerator keyRingGen = new PGPKeyRingGenerator(PGPSignature.POSITIVE_CERTIFICATION, rsakp_sign, id,
				sha1Calc, signhashgen.generate(), null,
				new BcPGPContentSignerBuilder(rsakp_sign.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA1), pske);

		// Encryption-Subkey mit seiner Signatur hinzufügen
		keyRingGen.addSubKey(rsakp_enc, enchashgen.generate(), null);
		return keyRingGen;
	}
}