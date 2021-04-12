package by.epam.payment_system.service.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Encrypting the password
 * 
 * @author Aleksandra Podgayskaya
 */
public final class PasswordEncryption {

	private static final String MESSAGE_DIGEST_ALGORITHM = "SHA-1";
	private static final String ENCODING = "utf8";

	private PasswordEncryption() {

	}

	/**
	 * Encrypt password
	 * 
	 * @param password {@link String} password to be encrypted.
	 * @return {@link String} hashCode in SHA-1
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String encrypt(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {

		MessageDigest messageDigest = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM);
		messageDigest.update(password.getBytes(ENCODING));
		byte[] bytesEncoded = messageDigest.digest();
		BigInteger bigInt = new BigInteger(1, bytesEncoded);
		String encryptedPassword = bigInt.toString(16);

		return encryptedPassword;
	}
}
