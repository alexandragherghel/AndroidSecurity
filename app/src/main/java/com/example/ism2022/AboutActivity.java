package com.example.ism2022;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class AboutActivity extends AppCompatActivity {

	private  static final  int CONTACTS = 1;
	private  static final  int DISPLAY = 2;
	private  static final  int ADD = 3;
	private  static final  int SYNC = 4;
	private  static final  int EMAIL = 5;
	private  static final  int SENSORS = 6;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ScrollView sv = new ScrollView(this);
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		
		TextView tv1 = new TextView(this);
		tv1.setText("Curs BNR v1.0");
		tv1.setGravity(Gravity.CENTER);
		
		ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.xau);
		
		TextView tv2 = new TextView(this);
		tv2.setText("www.cristianciurea.ase.ro"+"\n"+"Copyright Cristian CIUREA");
		tv2.setGravity(Gravity.CENTER);
		
		ll.addView(tv1);
		ll.addView(iv);
		ll.addView(tv2);
		sv.addView(ll);
		setContentView(sv);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, CONTACTS, 1, "Extrage contacte");
		menu.add(Menu.NONE, DISPLAY, 2, "Afiseaza contacte");
		menu.add(Menu.NONE, ADD, 3, "Adauga contact");
		menu.add(Menu.NONE, SYNC, 4, "Sincronizare contacte");
		menu.add(Menu.NONE, EMAIL, 5, "E-mail");
		menu.add(Menu.NONE, SENSORS, 6, "Sensors and GPS");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		PIM pim = new PIM(this);
		
		switch(item.getItemId())
		{
		case CONTACTS:
			pim.extractContacts();
			break;
		case DISPLAY:
	    	Intent intent = new Intent(this, DisplayContacts.class);
	    	startActivity(intent);
	    	break;
		case ADD:
			if (ActivityCompat.checkSelfPermission(getApplicationContext(),
					Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
					ActivityCompat.checkSelfPermission(getApplicationContext(),
							Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

				ActivityCompat.requestPermissions(AboutActivity.this, new String[] {
						Manifest.permission.READ_CONTACTS,
						Manifest.permission.WRITE_CONTACTS}, 0);
			}
			pim.addContact();
			break;
		case SYNC:
			Intent intent1 = new Intent(this, SyncContacts.class);
	    	startActivity(intent1);
			break;
		case EMAIL:
			pim.sendEmail();
			Toast.makeText(this, "Mail creat cu succes!", Toast.LENGTH_LONG).show();
			break;
		}
		return false;
	}
}
