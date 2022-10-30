package com.example.ism2022;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class DisplayContacts extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//instantiate a ContactsList object (contains a static ArrayList)
		ContactsList list = new ContactsList();
		
		ScrollView sv = new ScrollView(this);
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		
		//display elements of ArrayList collection
		TextView tv1 = new TextView(this);
		tv1.setText("Lista contactelor este:"+"\n" + 
					list.toString());
		
		ll.addView(tv1);
		sv.addView(ll);
		
		setContentView(sv);
	}

}
