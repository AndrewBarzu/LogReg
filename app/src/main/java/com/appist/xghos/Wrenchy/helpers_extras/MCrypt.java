package com.appist.xghos.Wrenchy.helpers_extras;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;

class MCrypt {

	static char[] HEX_CHARS = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
	public  String SECRET_KEY = "L1%ore@45Fer0#maPel!9sa$mI-36^123gGlo_Oskq*7-8pfeT";
	private IvParameterSpec ivspec;
	private SecretKeySpec keyspec;
	private Cipher cipher;

	private static MCrypt INSTANCE;

	static MCrypt getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new MCrypt();
		}
		return INSTANCE;
	}

	private String getSkInMD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] md5sum = md.digest(input.getBytes());
			String output = String.format("%032X", new BigInteger(1, md5sum));
			return output.toLowerCase();
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	private MCrypt() {


		final String KEY =  getSkInMD5(SECRET_KEY);
		Log.d("+++", "KEY: "+KEY);
		final String iv = KEY.substring(4, 20);
		Log.d("+++", "iv: "+iv);

		ivspec = new IvParameterSpec(iv.getBytes());
		keyspec = new SecretKeySpec(KEY.getBytes(), "AES");

		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public byte[] encrypt(String text) {
		if(text == null || text.length() == 0)
			return new byte[0];

		byte[] encrypted = null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
			//String paddedText = padString(text);
			encrypted = cipher.doFinal(text.getBytes("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return encrypted;
	}

	public byte[] decrypt(String code) {
		if(code == null || code.length() == 0)
			return new byte[0];

		byte[] decrypted = null;
		try {
			cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
			decrypted = cipher.doFinal(hexToBytes(code));
			//Remove trailing zeroes
			if( decrypted.length > 0) {
				int trim = 0;
				for( int i = decrypted.length - 1; i >= 0; i-- ) if( decrypted[i] == 0 ) trim++;

				if( trim > 0 ) {
					byte[] newArray = new byte[decrypted.length - trim];
					System.arraycopy(decrypted, 0, newArray, 0, decrypted.length - trim);
					decrypted = newArray;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return decrypted;
	}

	private String bytesToHex(byte[] buf) {
		char[] chars = new char[2 * buf.length];
		for (int i = 0; i < buf.length; ++i) {
			chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
			chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
		}
		return new String(chars);
	}

	private byte[] hexToBytes(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}

	String encryptHex(String code) {
		byte[] encrypted = encrypt(code);
		if(encrypted == null) {
			return "";
		}
		return bytesToHex(encrypted);
	}

	String decryptHex(String code) {
		return new String(decrypt(code));
	}

	String getEncryptedSecretKey() {
		return SECRET_KEY;
	}

}
