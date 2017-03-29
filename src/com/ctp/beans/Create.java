package com.ctp.beans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.ctp.process.Hashing;


@ManagedBean
@ViewScoped
public class Create {
	private String firstName,lastName,email,password,password2,sq1,sq2,sqa1,sqa2,customq;
	
	public Create(){
		
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
	
	public String getSq1() {
		return sq1;
	}

	public void setSq1(String sq1) {
		this.sq1 = sq1;
	}

	public String getSq2() {
		return sq2;
	}

	public void setSq2(String sq2) {
		this.sq2 = sq2;
	}
	

	public String getCustomq() {
		return customq;
	}

	public void setCustomq(String customq) {
		this.customq = customq;
	}

	public String getSqa1() {
		return sqa1;
	}

	public void setSqa1(String sqa1) {
		this.sqa1 = sqa1;
	}

	public String getSqa2() {
		return sqa2;
	}

	public void setSqa2(String sqa2) {
		this.sqa2 = sqa2;
	}
	
	
	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String execute(){
		if(!checkPass()){
			addErrorMessage("Passwords do not match");
			return "Creation";
		}
		CustomerDBUtil db = null;
		password=Hashing.hashPassword(password);
		try{
		db=CustomerDBUtil.getInstance();}
		catch(Exception connect){}
		if(customq!=null){
			sq1=customq;
		}
		String temp=db.signUp(firstName,lastName,email,password);
		if(temp.equals("success")){
			db.signUp2(getID(),sq1,sq2,sqa1,sqa2);
			return "Success.xhtml";
		}
		else if(temp.equals("repeat")){
			addErrorMessage("Email ID already exists");
			return "Creation";
		}
		else{
			addErrorMessage("Not again");
			return "Creation";
		}
			
	}
	
	private int getID(){
		CustomerDBUtil db=null;
		try{
			db=CustomerDBUtil.getInstance();}
		catch(Exception connect){
			connect.printStackTrace();
			
		}
		int id=db.getID(email);
		return id;
	}
	
	private boolean checkPass(){
		if(password.equals(password2)){
			return true;
		}
		else
			return false;
	}
	private void addErrorMessage(String exc){
		FacesMessage message = new FacesMessage(exc);
		FacesContext.getCurrentInstance().addMessage(null, message);
	
}
}
