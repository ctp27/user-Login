package com.ctp.entities;

public class UserState {

	private int ID;
	private boolean flagState;
	private String returning;
	public UserState(String returning,int ID,boolean flagState){
		this.returning=returning;
		this.ID=ID;
		this.flagState=flagState;
	}

	public int getID() {
		return ID;
	}

	public boolean isFlagState() {
		return flagState;
	}

	public String getReturning() {
		return returning;
	}

	
}
