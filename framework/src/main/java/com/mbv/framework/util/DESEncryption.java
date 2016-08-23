package com.mbv.framework.util;

import com.mbv.framework.props.FrameworkProps;

import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.spec.KeySpec;

public class DESEncryption {

	private static final String UNICODE_FORMAT = "UTF8";

	private KeySpec myKeySpec;

	private SecretKeyFactory mySecretKeyFactory;

	private Cipher cipher;

	byte[] keyAsBytes;

	SecretKey key;

    @Autowired
    FrameworkProps frameworkProps;
	
	public DESEncryption(){

	}

    public FrameworkProps getFrameworkProps() {
        return frameworkProps;
    }

    public void setFrameworkProps(FrameworkProps frameworkProps) {
        this.frameworkProps = frameworkProps;
    }

    private void init() {
        try {
            keyAsBytes = frameworkProps.getEncryptionKey().getBytes(UNICODE_FORMAT);
            myKeySpec = new DESKeySpec(keyAsBytes);
            mySecretKeyFactory = SecretKeyFactory.getInstance("DES");
            cipher = Cipher.getInstance("DES");
            key = mySecretKeyFactory.generateSecret(myKeySpec);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initWithCustomKey(String customKey) {
        try {
            //Custom key with user pass for transactions
            keyAsBytes =  (frameworkProps.getEncryptionKey() + customKey).getBytes(UNICODE_FORMAT);
            myKeySpec = new DESKeySpec(keyAsBytes);
            mySecretKeyFactory = SecretKeyFactory.getInstance("DES");
            cipher = Cipher.getInstance("DES");
            key = mySecretKeyFactory.generateSecret(myKeySpec);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	public String encrypt(String unencryptedString, String customKey) {
        if(customKey != null) {
            initWithCustomKey(customKey);
        } else {
            init();
        }
		String encryptedString = null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
			byte[] encryptedText = cipher.doFinal(plainText);
			encryptedString = AuthUtil.encodeBase64UrlSafe(encryptedText);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedString;
	}

	public String decrypt(String encryptedString, String customKey) {
        if(customKey != null) {
            initWithCustomKey(customKey);
        } else {
            init();
        }
		String decryptedText = null;
		try {
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] encryptedText = AuthUtil.decodeBase64ToBytes(encryptedString);
			byte[] plainText = cipher.doFinal(encryptedText);
			decryptedText = bytes2String(plainText);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return decryptedText;
	}

	/**
	 * Returns String From An Array Of Bytes
	 */
	private static String bytes2String(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			stringBuffer.append((char) bytes[i]);
		}
		return stringBuffer.toString();
	}
}