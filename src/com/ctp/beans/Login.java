package com.ctp.beans;

import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.ctp.entities.UserState;
import com.ctp.process.CounterManager;
import com.ctp.process.FlagManager;
import com.ctp.process.Hashing;

@ManagedBean
@SessionScoped
public class Login {
	private Logger logger = Logger.getLogger(getClass().getName());
	private String email,password,type;
	private int ID;
	private boolean flag=false;
	CounterManager cm=new CounterManager();
	public Login(){}
	
	public int getID() {
		return ID;
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
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	public boolean isFlag() {
		return flag;
	}

	public String exec(){
		FlagManager fm=new FlagManager();
		String s="";
		if(type.equals("Admin"))
			s=authenticateAd();
		else{
//			s=authenticate();
			try{
				if(cm.getRestCounter(email)>=3){
					fm.setFlag(email);
				}
				}
				catch(Exception e){}
			s=authenticatetrial();
		}	
		return s;
	}

	private String authenticateAd(){
		
		CustomerDBUtil db=null;
		boolean flag;
		try{
			db=CustomerDBUtil.getInstance();
			
			}
			catch(Exception e){
				logger.info("Not connected");
				addErrorMessage(e);
				return "index?faces-redirect=true";
			}
		flag=db.authenticateAd(email, password);
		if(flag==true)
			return "adminHome?faces-redirect=true";
		else
		{
			addErrorMessage("Invalid Username and Password");
			return "index?faces-redirect=true";
		}	
	}
	
	private String authenticatetrial(){
		CounterManager cm=new CounterManager();
		CustomerDBUtil db=null;
		
		 password=Hashing.hashPassword(password);
		 UserState userstate;
		try{
		db=CustomerDBUtil.getInstance();
		
		}
		catch(Exception e){
			logger.info("Not connected");
			addErrorMessage(e);
			return "index?faces-redirect=true";
		}
		
		
		userstate=db.authenticatetrial(email, password);
		
		if(userstate.getReturning().equals("New") && userstate.isFlagState())
		{
			ID=userstate.getID();
			flag=true;
			HttpSession session=(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			session.setAttribute("ID",ID);
			if(cm.getWrongCounter(email)>=3){
				session.setAttribute("sq", email);
				session.setAttribute("type","newlock");
				return "Questions?faces-redirect=true";
			}
			logger.info("Authenticated");
			return "Reg?faces-redirect=true";
		}
		else if(userstate.getReturning().equals("Old") && userstate.isFlagState()){
				ID=userstate.getID();
				HttpSession session=(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
				if(cm.getWrongCounter(email)>=3){
					session.setAttribute("sq", email);
					session.setAttribute("type","lock");
					return "Questions?faces-redirect=true";
				}
				session.setAttribute("ID",ID);
				session.setAttribute("type", "lock");
				return "Pending?faces-redirect=true";
		}
			
		else if(userstate.getReturning().equals("Old") && !userstate.isFlagState()){
			ID=userstate.getID();
			HttpSession session=(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			if(cm.getWrongCounter(email)>=3){
				session.setAttribute("sq", email);
				session.setAttribute("type", "lock");
				return "Questions?faces-redirect=true";
			}
			
			session.setAttribute("ID",ID);
			return "Home?faces-redirect=true";
			}
		else if(userstate.getReturning().equals("Invalid")){
			addErrorMessage("Invalid Username or Password");
			try{
			int count=cm.getWrongCounter(email);
			if(count>=2){
				cm.setWrongCounter(email, ++count);
				addErrorMessage("Your account is locked");
				
			}
			else if(count<3){
					cm.setWrongCounter(email,++count);
					addErrorMessage("You have "+(3-count)+" attempt(s) left");
			}
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("Not a user from database");
			}
			
			return "index";
		}
		
		else if(userstate.getReturning().equals("Error")){
			addErrorMessage("DB Connection error");
			return "index";
		}
		else{
			addErrorMessage("Dunno the error");
			return "index";
		}
			
			
		}
	
	private void addErrorMessage(Exception exc){
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
		
	}
	
	private void addErrorMessage(String exc){
		FacesMessage message = new FacesMessage(exc);
		FacesContext.getCurrentInstance().addMessage(null, message);
		
	}
}
