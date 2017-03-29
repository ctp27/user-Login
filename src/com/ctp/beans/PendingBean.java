package com.ctp.beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.ctp.process.CounterManager;

@ManagedBean
public class PendingBean {
	private String firstName;
	
	@ManagedProperty("#{login}")
	private Login login;
	
	private boolean locked=false;

	public void setLogin(Login login) {
		this.login = login;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	@PostConstruct
	public void Load(){
		CustomerDBUtil db=null;
		CounterManager cm=new CounterManager();
		int id=login.getID();
		if(cm.getRestCounter(id)>=3)
		{
			locked=true;
		}
		try{
			db=CustomerDBUtil.getInstance();
		}
		catch(Exception e){
			System.out.println("Could not get instance");
			e.printStackTrace();
		}
		firstName=db.getFirstName(id);
	}
}
