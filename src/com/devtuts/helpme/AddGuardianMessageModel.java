package com.devtuts.helpme;
 
public class AddGuardianMessageModel {

	private String msg = ""; 
	private int id = 0;

	public AddGuardianMessageModel() {

	} 

	public void setMessage(String msg) {
		this.msg = msg;
	}

	public String getMessage() {
		return this.msg;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}
}
