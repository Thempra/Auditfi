package com.auditfi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AuditfiActivity extends Activity implements OnClickListener {
	private static final String TAG = "Auditfi";
	WifiManager wifi;
	BroadcastReceiver receiver;

	
	Button buttonScan;
	CheckBox vulnerables;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

    	/* Codigo para no poner el titulo */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Setup UI
		vulnerables = (CheckBox) findViewById(R.id.vulnerables);
		buttonScan = (Button) findViewById(R.id.buttonScan);
		buttonScan.setOnClickListener(this);

		// Setup WiFi
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		//Si no esta activado wifi lo activamos
		if(!wifi.isWifiEnabled()){
			//No esta activado el wifi!
            Toast.makeText( this, getResources().getString( R.string.ActivandoWifi ), Toast.LENGTH_LONG ).show();
            
            //Activamos el wifi
            wifi.setWifiEnabled(true);
		}
		Log.d(TAG, "onCreate()");
	}


	public void onClick(View view) {

		if (view.getId() == R.id.buttonScan) {
			ListView list = (ListView) findViewById(R.id.filaWifis);
			
			if(!wifi.isWifiEnabled()){
				//No esta activado el wifi!
	            Toast.makeText( this, getResources().getString( R.string.WifiOff ), Toast.LENGTH_LONG ).show();
	            
	            //Salimos
	            return ;
			}
			
			Log.d(TAG, "onClick() wifi.startScan()");
			wifi.startScan();

			// Listado de las redes wifi
			final List<ScanResult> configs = wifi.getScanResults();
			
			//textStatus.setText(""); //Limpiamos el texto
			int numero_redes = 0;
			
			
			
			/******************************************************************/
			/******** EVENTO CLICK EN EL LISTADO DE LAS WIFIS ****************/
			/******************************************************************/

			OnItemClickListener clickListaWifis= new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

					//Si esta marcado solo ver vulnerables tenemos que buscar cual ha sido
					if (vulnerables.isChecked())
					{
						int cont=0;
						for (ScanResult config : configs) 
						{
							
							if (Vulnerables.esCompatible(config.SSID, config.BSSID)) 
							{
								arg2--;
							}
							
							if (arg2<0)
							{
								arg2=cont;
								break;
							}
							cont++;
						}
					}
					
					//Identificamos
					String key= Vulnerables.obtenerClave(configs.get(arg2).SSID, configs.get(arg2).BSSID);
					
					if(key!=""){
						/*Copiamos al portapapeles */
						ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 
						clipboard.setText(key);
						
						showMsg(getResources().getString(R.string.Copiar),getResources().getString(R.string.contrasenaCopiada)+" "+key, getResources().getString(R.string.Aceptar));
					}else{
						showMsg(getResources().getString(R.string.Copiar),getResources().getString(R.string.redNoVulnerable), getResources().getString(R.string.Aceptar));
					}
					
					
				}
			};	
			


			
			/******************************************************************/
			/***************** MOSTRAMOS LAS WIFIS EN PANTALLA ****************/
			/******************************************************************/
			
				// Lo asignamos a la vista
				ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> map;
				
				
				
				for (ScanResult config : configs) {
					map = new HashMap<String, String>();
					
					//Si esta marcado el checkbox vulnerables, solamente cogemos las vulnerables
					if (!vulnerables.isChecked() || Vulnerables.esCompatible(config.SSID, config.BSSID)){

						map.put("ssid", config.SSID);
						map.put("bssid",  "MAC: " + config.BSSID);
						map.put("level", config.level +" dB");

						numero_redes++;
						

						//Identificamos
						String key= Vulnerables.obtenerClave(config.SSID, config.BSSID);						
						
						if(key!=""){
							map.put("key", "Key: "+ key);
						}
						
						mylist.add(map);
					}
					
					
					
				
				}

				//Si no se ha mostrado ninguna red, escribimos mensaje
				if(numero_redes == 0){
					map = new HashMap<String, String>();
					map.put("ssid", getResources().getString( R.string.NoHayRedes ));
					mylist.add(map);
					
				}

				SimpleAdapter mSchedule = new SimpleAdapter(this, mylist,
						R.layout.filawifis,
						new String[] { "ssid", "bssid", "level", "key"}, new int[] {
								R.id.SSID_CELL, R.id.BSSID_CELL, R.id.LEVEL_CELL, R.id.KEY_CELL });
				list.setAdapter(mSchedule);

				
				// creamos el overwrite el click
				list.setOnItemClickListener(clickListaWifis);		
				
			
			
			Log.d(TAG, "onClick() Refresh()");

		}
		
		
	

	}

	/*
	 *  Funciones relativas al menu inferior 
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.auditfi_menu, menu);
	    return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.exit:
	        //Salida del sistema
	    	 finish();
	    	 
	        return true;
	    case R.id.about:
	        //Menu Acerca de ...
	    	showMsg(getResources().getString(R.string.about), getResources().getString(R.string.developers), getResources().getString(R.string.Aceptar)); 
 

	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	 private void showMsg(String title, String msg, String txtButton){
	        new AlertDialog.Builder(this).setTitle(title) 
	        .setMessage(msg) 
	        .setNeutralButton(txtButton, new android.content.DialogInterface.OnClickListener() { 

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				} 
	        }).show();
	    }
	 
	 
}