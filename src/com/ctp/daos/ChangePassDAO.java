package com.ctp.daos;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.ctp.beans.ChangePassBean;
import com.ctp.beans.CustomerDBUtil;
import com.ctp.process.CounterManager;
import com.ctp.process.Hashing;

@ManagedBean
public class ChangePassDAO {
	private int ID;
	private CounterManager cm=new CounterManager();
	public String exec(ChangePassBean fp){
		String s=null;
		if(fp.getType().equals("forgot")){
			s=changePass(fp);
		}
		else
		{
			s=updatePass(fp);
		}
		return s;
	}
	
	private String changePass(ChangePassBean fp){
		CustomerDBUtil db=null;
		try{
			db=CustomerDBUtil.getInstance();
		}catch(Exception e){
			
		}
		String npass=fp.getnPass();
		String cpass=fp.getcPass();
		if(!npass.equals(cpass)){
			addErrorMessage("Passwords do not match");
			return "ChangePass?faces-redirect=true";
		}
		else{
			HttpSession session=(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			String email=(String)session.getAttribute("sq");
			int id=db.getID(email);
			changePassword(id,npass);
			removeSession();
			int count=cm.getRestCounter(email);
			cm.setRestCounter(email, ++count);
			addErrorMessage("Password reset successfully");
			return "index?faces-redirect=true";
		}
	}
	private String updatePass(ChangePassBean fp){
		String cpass=getCurrentPassword();
		String fppass=fp.getCurrentPass();
		fppass=Hashing.hashPassword(fppass);
		if(fppass.equals(cpass) && fp.getnPass().equals(fp.getcPass())){
			changePassword(ID, fp.getnPass());
			addErrorMessage("Password Changed Successfully");
			removeSession();
			return "Home?faces-redirect=true";
		}
		else if(!fp.getnPass().equals(fp.getcPass())){
			addErrorMessage("Passwords do not match");
			return "ChangePass?faces-redirect=true";
		}
		else{
			addErrorMessage("Please Enter correct current password");
			return "ChangePass?faces-redirect=true";
		}
		
	}
	
	private String getCurrentPassword(){
		CustomerDBUtil db=null;
		HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		ID=(Integer)session.getAttribute("ID");
		try{
			db=CustomerDBUtil.getInstance();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		String pass=db.getPassword(ID);
		return pass;	
	}
	
	private void changePassword(int ID,String pwd){
		CustomerDBUtil db=null;
		pwd=Hashing.hashPassword(pwd);
		try{
			db=CustomerDBUtil.getInstance();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		db.updatePassword(ID, pwd);
		
	}
	private void removeSession(){
		HttpSession session=(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		session.removeAttribute("type");
		session.removeAttribute("sq");
	}
	
	private void addErrorMessage(String exc){
		FacesMessage message = new FacesMessage(exc);
		FacesContext.getCurrentInstance().addMessage(null, message);
		
	}
}

