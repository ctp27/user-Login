package com.ctp.beans;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class Customer {
	private String firstName, lastName, email,link;
	private int ID;
	private boolean flag;
	public Customer(int ID,boolean flag,String fn,String ln,String email,String link){
		this.ID=ID;
		this.flag=flag;
		firstName=fn;
		lastName=ln;
		this.email=email;
		this.link=link;
		
	}

	


	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
}
