package com.ctp.beans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean
public class FpageBean {
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String execute(){
		CustomerDBUtil db=null;
		try{
			db=CustomerDBUtil.getInstance();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		int id=db.getID(email);
			if(id==0){
				addErrorMessage("Email ID not found. Please enter a registered email ID");
				return "fpassword";
			}
			else{
			HttpSession session=(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			session.setAttribute("sq", email);
			session.setAttribute("type","forgot");
			return "Questions";
			}
		
		
	}
	private void addErrorMessage(String exc){
		FacesMessage message = new FacesMessage(exc);
		FacesContext.getCurrentInstance().addMessage(null, message);
		
	}
}
