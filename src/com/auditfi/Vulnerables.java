package com.auditfi;

import com.auditfi.vulnerables.JAZZTELXXXX;
import com.auditfi.vulnerables.WLANXXXX;

public class Vulnerables{
	
	public static boolean esCompatible(String SSID, String BSSID){
		return WLANXXXX.esCompatible(SSID, BSSID) || JAZZTELXXXX.esCompatible(SSID, BSSID);
	}
	
	public static String obtenerClave(String SSID, String BSSID){

		//Identificamos
		String key="";
		
		if (WLANXXXX.esCompatible(SSID,BSSID)) {
			key=WLANXXXX.obtenerClave(SSID,BSSID);
		}else if (JAZZTELXXXX.esCompatible(SSID,BSSID)){
			key=JAZZTELXXXX.obtenerClave(SSID,BSSID);
		}
		
		return key;
	}
	
}