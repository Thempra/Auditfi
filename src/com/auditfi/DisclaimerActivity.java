package com.auditfi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class DisclaimerActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

    	/* Codigo para no poner el titulo */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disclaimer);
        
        ImageButton pregunta = (ImageButton) findViewById(R.id.EscanearImagen);
        pregunta.setOnClickListener(new View.OnClickListener() {
        	 public void onClick(View view) {
                 Intent myIntent = new Intent(view.getContext(), Auditfi.class);
                 startActivityForResult(myIntent, 0);
             }

         });
        
        ImageButton salir = (ImageButton) findViewById(R.id.SalirImagen);
        salir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }

         });

	}

	
	
}