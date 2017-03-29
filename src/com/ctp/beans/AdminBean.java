package com.ctp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import com.ctp.process.CounterManager;


@ManagedBean
public class AdminBean {
	private List<Customer> CustList;
	private List<Customer> author;
	private List<Customer> unauthor;
	
	public AdminBean(){
		CustList=new ArrayList<>();
		author=new ArrayList<>();
		unauthor=new ArrayList<>();
	}
	

	public List<Customer> getAuthor() {
		return author;
	}



	public void setAuthor(List<Customer> author) {
		this.author = author;
	}





	public List<Customer> getUnauthor() {
		return unauthor;
	}


	public void setUnauthor(List<Customer> unauthor) {
		this.unauthor = unauthor;
	}
	
	@PostConstruct
	public void load(){
		CustList.clear();
		CustomerDBUtil db=null;
		try{
			
			db=CustomerDBUtil.getInstance();
			
		}
		catch(Exception e){
			e.printStackTrace();
			
		}
		CustList=db.loadList();
		setLists();
	}
	
	public String activate(int ID,String email){
		CounterManager cm=new CounterManager();
		CustomerDBUtil db=null;
		try{
			db=CustomerDBUtil.getInstance();
		}
		catch(Exception e){
			e.printStackTrace();
			return "adminHome";
		}
		boolean state=db.activateCust(ID);
		if(state){
			cm.setWrongCounter(email, 0);
			cm.setRestCounter(email, 0);
			return "adminHome?faces-redirect=true";
		}
		else{
			addErrorMessage("User couldnt be activated");
			return "adminHome";
		}
	}
	
	public String deactivate(int ID){
		CustomerDBUtil db=null;
		try{
			db=CustomerDBUtil.getInstance();
		}
		catch(Exception e){
			e.printStackTrace();
			return "adminHome";
		}
		boolean state=db.deactivateCust(ID);
		if(state)
			return "adminHome?faces-redirect=true";
		else{
			addErrorMessage("User couldnt be deactivated");
			return "adminHome";
		}
	}
	
	
	public String deleteCust(int ID){
		CustomerDBUtil db=null;
		try{
			db=CustomerDBUtil.getInstance();
		}
		catch(Exception e){
			e.printStackTrace();
			return "adminHome?faces-redirect=true";
		}
		boolean state=db.deleteCust(ID);
		if(state)
			return "adminHome?faces-redirect=true";
		else{
			addErrorMessage("User couldnt be deleted");
			return "adminHome?faces-redirect=true";
		}
	}
	
	
	private void setLists(){
		for(int i=0;i<CustList.size();i++)
		{
			if(CustList.get(i).isFlag())
				unauthor.add(CustList.get(i));
			else
				author.add(CustList.get(i));
		}
	}
	
	private void addErrorMessage(String exc){
		FacesMessage message = new FacesMessage(exc);
		FacesContext.getCurrentInstance().addMessage(null, message);
		
	}
	
}
