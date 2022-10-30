package com.example.ism2022;

import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

import java.util.ArrayList;

public class PIM {

	Context context;
	ContactsList clist = new ContactsList();

	public PIM(Context context) {
		this.context = context;
	}

	//extract all the contacts that already have a defined phone number 
	@SuppressLint("Range")
	public void extractContacts()
	{
		// accessing contacts in address book
		Cursor contacts = context.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI,
				new String[] {
						ContactsContract.Contacts.
						_ID,
						ContactsContract.Contacts.
						DISPLAY_NAME,
						ContactsContract.Contacts.
						HAS_PHONE_NUMBER }, 
						null, null, null);
		if (contacts != null) 
		{
			while (contacts.moveToNext()) 
			{
				@SuppressLint("Range") String id = contacts.getString(contacts.
						getColumnIndex(ContactsContract.
								Contacts._ID));
				@SuppressLint("Range") String name = contacts.getString(contacts.
						getColumnIndex(ContactsContract.
								Contacts.DISPLAY_NAME));
				String phone="";
				//select only contacts with phone number
				if (Integer.parseInt(contacts.
						getString(contacts.getColumnIndex
								(ContactsContract.Contacts.
										HAS_PHONE_NUMBER))) > 0) 
				{
					Cursor phoneNumber = context.
							getContentResolver().query(
							Phone.CONTENT_URI, null,
							Phone.CONTACT_ID + " = ?",
							new String[] { id }, null);
					if (phoneNumber != null) 
					{
						while (phoneNumber.moveToNext()) 
							phone = phoneNumber.
							getString(phoneNumber.getColumnIndex(
									Phone.NUMBER));
						phoneNumber.close();
					}
				}
				//instantiate an object of class Contacts
				Contacte contact = new Contacte(id, name, phone);
				//add new contact in collection
				clist.adaugaContact(contact);
			}
			contacts.close();
		}
	}
	
	//add new contact in address book
	public void addContact() {
		ArrayList<ContentProviderOperation> cpo =
				new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = cpo.size();

        cpo.add(ContentProviderOperation.newInsert(
        		RawContacts.CONTENT_URI)
                .withValue(RawContacts.ACCOUNT_TYPE, null)
                .withValue(RawContacts.ACCOUNT_NAME, null).build());
        
        cpo.add(ContentProviderOperation
                .newInsert(Data.CONTENT_URI)
                .withValueBackReference(Data.RAW_CONTACT_ID,
                		rawContactInsertIndex)
                .withValue(Data.MIMETYPE,
                		StructuredName.CONTENT_ITEM_TYPE)
                .withValue(StructuredName.DISPLAY_NAME,
                		"Cristian CIUREA") // contact name
                .build());
        
        cpo.add(ContentProviderOperation
                .newInsert(Data.CONTENT_URI)
                .withValueBackReference(Data.
                		RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                .withValue(Phone.NUMBER, "0722622492") // phone number
                .withValue(Phone.TYPE, Phone.TYPE_MOBILE).build()); // type of mobile number
        try
        {
            ContentProviderResult[] cpr = context.
            		getContentResolver().
            		applyBatch(ContactsContract.AUTHORITY, cpo);
        }
        catch (RemoteException e)
        { 
            Log.d("addContact", e.getMessage());
        }
        catch (OperationApplicationException e)
        {
        	Log.d("addContact", e.getMessage());
        }       
	}	

	//automatically create a new mail message (if an e-mail client is configured) 
	public void sendEmail() {
		//use an Intent with ACTION_SEND action
		Intent ie = new Intent(Intent.ACTION_SEND);
		//specify the message type
		ie.setType("message/rfc822");
		//specify the recipient e-mail address
		ie.putExtra(Intent.EXTRA_EMAIL,
				new String[] { "cristian.ciurea@ie.ase.ro" });
		//specify the subject
		ie.putExtra(Intent.EXTRA_SUBJECT,
				"FX Rates BNR");

		//specify the message text
		ie.putExtra(Intent.EXTRA_TEXT,
				"---------------");
		//if there are many e-mail clients configured, please choose one
		context.startActivity(Intent.createChooser(ie,
				"Choose the application"));
	}
}
