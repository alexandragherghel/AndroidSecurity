package com.example.ism2022;

import java.util.ArrayList;

public class ContactsList {
	
	static ArrayList<Contacte> lista = new
            ArrayList<Contacte>(100);
	
	public ContactsList()
	{
		
	}

	public void adaugaContact(Contacte c) 
	{
		lista.add(c);
	}
	
	public String toString()
	{
		StringBuilder contact = new StringBuilder();
		
		for(Contacte c:lista)
		{
			contact.append(c.getID());
			contact.append(" ");
			contact.append(c.getNume());
			contact.append(" ");
			contact.append(c.getTelefon());
			contact.append(" ");
			contact.append("\n");
		}
		return contact.toString();	
	}
}
