package com.poly.carnetdebord.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;

/*
 * A Class used to encrypt String messages
 * 
 */
public class Encryption {

	private static final String FICHIER_PROPERTIES = "utils.properties";
	private Context context;
	private Properties properties = new Properties();

	public Encryption(Context c) {
		this.context = c;

		try {
			InputStream fichierProperties = context.getAssets().open(
					FICHIER_PROPERTIES);
			properties.load(fichierProperties);
			if (fichierProperties == null) {
				throw new IOException("Le fichier properties "
						+ FICHIER_PROPERTIES + " est introuvable.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Method to encrypt a message
	 * 
	 * @param content the message to be encrypted
	 * 
	 * @return the encrypted message
	 */
	public String encode(String content) throws IOException {

		content = Base64.encodeToString(content.getBytes(), Base64.DEFAULT);

		Logger logger = Logger.getLogger("Encryption");

		String strKey = properties.getProperty("keyAES");
		SecretKey key = new SecretKeySpec(
				Base64.decode(strKey, Base64.DEFAULT), "AES");

		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return new String(cipher.doFinal(content.getBytes("UTF8")));
		} catch (NoSuchPaddingException e) {
			logger.log(Level.SEVERE, "padding problem", e);
		} catch (NoSuchAlgorithmException e) {
			logger.log(Level.SEVERE, "algorithm problem", e);
		} catch (InvalidKeyException e) {
			logger.log(Level.SEVERE, "key invalid", e);
		} catch (BadPaddingException e) {
			logger.log(Level.SEVERE, "bad padding", e);
		} catch (IllegalBlockSizeException e) {
			logger.log(Level.SEVERE, "illegal block size", e);
		} catch (UnsupportedEncodingException e) {
			logger.log(Level.SEVERE, "Bad encoding", e);
		}

		return null;
	}

}
