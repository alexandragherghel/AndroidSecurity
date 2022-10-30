package com.example.ism2022;

public class Contacte {
	
	String idc;
	String numec;
	String telefonc;
	
	public Contacte(String id, String num, String tel) {
		super();
		this.idc = id;
		this.numec = num;
		this.telefonc = tel;
	}
	
	public String getID() {
		return idc;
	}
	public void setID(String id) {
		this.idc = id;
	}
	
	public String getNume() {
		return numec;
	}
	public void setNume(String num) {
		this.numec = num;
	}
	
	public String getTelefon() {
		return telefonc;
	}
	public void setTelefon(String tel) {
		this.telefonc = tel;
	}
}
