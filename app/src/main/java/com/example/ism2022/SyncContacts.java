package com.example.ism2022;

import android.app.Activity;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SyncContacts extends Activity {

	static ArrayList<String> list = new ArrayList<String>(100);
	StringBuilder sb = new StringBuilder();

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    this.getApplicationContext().getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, contentObserver);

    Cursor cursor = managedQuery(ContactsContract.Contacts.CONTENT_URI,
    		new String[] { ContactsContract.Contacts._ID,
			ContactsContract.Contacts.DISPLAY_NAME,
			ContactsContract.Contacts.HAS_PHONE_NUMBER }, null, null, null);
    cursor.setNotificationUri(this.getApplicationContext().getContentResolver(), ContactsContract.Contacts.CONTENT_URI);
    this.getApplicationContext().getContentResolver().notifyChange(ContactsContract.Contacts.CONTENT_URI, contentObserver);
    cursor.registerContentObserver(contentObserver);
    cursor.requery();
    
    LinearLayout sv = new LinearLayout(this);
	sv.setOrientation(LinearLayout.VERTICAL);

	TextView tv1 = new TextView(this);
	
	for(String s:list)
	{
		sb.append(s);
	}
	
	tv1.setText(sb);
		
	sv.addView(tv1);

	setContentView(sv);
}

	private class CustomContentObserver extends ContentObserver {
	
	    public CustomContentObserver() {
	        super(null);
	    }
	
	    @Override
	    public boolean deliverSelfNotifications() {
	    	list.add("Notification!"+"\n");
	        return true;
	        }
	
	    @Override
	    public void onChange(boolean selfChange) {
	        super.onChange(selfChange);
	        list.add("Contacts changes are: "+selfChange + "\n");
	    }
	
	}

	CustomContentObserver contentObserver = new CustomContentObserver();
}


