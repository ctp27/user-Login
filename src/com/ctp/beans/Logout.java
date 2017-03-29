package com.ctp.beans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean
public class Logout {
	
	public String invalidate(){
			HttpSession session=(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			session.invalidate();
			addErrorMessage("You have successfully Logged out");
			return "index";
		}
	
	private void addErrorMessage(String exc){
		FacesMessage message = new FacesMessage(exc);
		FacesContext.getCurrentInstance().addMessage(null, message);
		
	}
	
}

