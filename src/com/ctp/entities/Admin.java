package com.ctp.entities;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class Admin {
	
	private String firstName,lastName,email,password;
	
	public Admin(){
		
	}
	
	public Admin(String firstName,String lastName,String email,String password){
		this.firstName=firstName;
		this.lastName=lastName;
		this.email=email;
		this.password=password;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String toString(){
		return firstName+" "+lastName+" "+email;
	}
}
