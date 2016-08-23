package com.mbv.framework.util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthUtil {

	private static final Logger logger = LoggerFactory.getLogger(AuthUtil.class);

	/* AES Start*/
	public static byte[] encryptAES(String strPassword, String toEncrypt) throws Exception {
		SecretKeySpec key = new SecretKeySpec(strPassword.getBytes(), "AES");
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.ENCRYPT_MODE, key);
		final byte[] encryptedData = c.doFinal(toEncrypt.getBytes());
		return encryptedData;
	}

	public static byte[] decryptAES(String strPassword, byte[] encryptedData) throws Exception {
		SecretKeySpec key = new SecretKeySpec(strPassword.getBytes(), "AES");
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.DECRYPT_MODE, key);
		final byte[] decryptedData = c.doFinal(encryptedData);
		return decryptedData;
	}

	public static SecretKeySpec generateAESKey(String secretKey, Object salt) throws Exception {
		byte[] key = (secretKey + salt).getBytes("UTF-8");
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		key = sha.digest(key);
		key = Arrays.copyOf(key, 16); // use only first 128 bit
		return new SecretKeySpec(key, "AES");
	}
	/* AES END*/


	/* BASE64 START */
	public static byte[] encodeBase64(String str){
		return Base64.encodeBase64(str.getBytes());
	}

	public static String encodeBase64UrlSafe(byte[] bytes){
		return Base64.encodeBase64URLSafeString(bytes);
	}

	public static byte[] decodeBase64ToBytes(String str){
		return Base64.decodeBase64(str);
	}
	/* BASE64 END */

	/* MD5 START */
	public static byte[] computeMD5(byte[] dataBytes) {
		if (dataBytes == null) {
			return null;
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException ex) {
			logger.error("NoSuchAlgorithmException ", ex);
			return null;
		}
		return md.digest(dataBytes);
	}

	public static String computeMD5HexString(byte[] dataBytes) {
		byte[] md5 = computeMD5(dataBytes);
		if (md5 != null) {
			HexBinaryAdapter adapter = new HexBinaryAdapter();
			return adapter.marshal(md5);
		}
		return null;
	}
	/* MD5 END */

	/* HMAC START */
	public static byte[] generateHMAC(final String sharedSecret, final String stringToSign) throws GeneralSecurityException {
		final SecretKeySpec secretKey = new SecretKeySpec(sharedSecret.getBytes(), "HmacSHA1");
		final Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(secretKey);
		mac.update(stringToSign.getBytes());
		return mac.doFinal();
	}

	public static String generateHMACString(final String sharedSecret, final String stringToSign) throws GeneralSecurityException {
		byte[] hmac = generateHMAC(sharedSecret,stringToSign);
		HexBinaryAdapter adapter = new HexBinaryAdapter();
		return adapter.marshal(hmac);
	}
	/* HMAC END */


	public static String digest(String string, String salt) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.reset();
		digest.update(salt.getBytes("UTF-8"));

		byte[] btPass = digest.digest(string.getBytes("UTF-8"));
		int iter = 1000;
		for (int i = 0; i < iter; i++) {
			digest.reset();
			btPass = digest.digest(btPass);
		}
		HexBinaryAdapter adapter = new HexBinaryAdapter();
		return adapter.marshal(btPass);
	}

	/* GENERAL START*/
	public static String generateSalt(Integer... length) {
		return RandomStringUtils.randomAlphanumeric(length.length == 1 && length[0] < 128 ? length[0] : 32);
	}

	public static int generateRandomInt() {
		SecureRandom secureRandom = new SecureRandom();
		return secureRandom.nextInt(Integer.MAX_VALUE);
	}

	public synchronized static String generateUniqueId() {
		UUID uuid = java.util.UUID.randomUUID();
		return uuid.toString().replaceAll("-", "").toUpperCase();
	}
}
