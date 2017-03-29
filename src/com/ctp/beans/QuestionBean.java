package com.ctp.beans;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.ctp.entities.QA;
import com.ctp.process.CounterManager;
import com.ctp.process.FlagManager;


@ManagedBean
public class QuestionBean {
	private String q1,q2,a1,a2,email;
	private QA qa=null;
	boolean lock=false;
	private FlagManager fm=new FlagManager();
	
	String usertype;
	private CustomerDBUtil db=null;
	
	
	@PostConstruct
	public void loadQuestions(){
		HttpSession session=(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		email=(String)session.getAttribute("sq");
		usertype=(String)session.getAttribute("type");
		if(usertype.equals("lock"))
		{
			lock=true;
		}
		
		try{
			db=CustomerDBUtil.getInstance();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		qa=db.getQA(email);
		q1=qa.getQ1();
		q2=qa.getQ2();
	}

	public String getQ1() {
		return q1;
	}

	public void setQ1(String q1) {
		this.q1 = q1;
	}

	public String getQ2() {
		return q2;
	}

	public void setQ2(String q2) {
		this.q2 = q2;
	}

	public String getA1() {
		return a1;
	}

	public void setA1(String a1) {
		this.a1 = a1;
	}

	public String getA2() {
		return a2;
	}

	public void setA2(String a2) {
		this.a2 = a2;
	}
	
	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public boolean isLock() {
		return lock;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}

	public String control(){
		CounterManager cm=new CounterManager();
		if(a1.equalsIgnoreCase(qa.getA1()) && a2.equalsIgnoreCase(qa.getA2())){
			if(usertype.equals("forgot")){
				cm.setWrongCounter(email, 0);
				return "ChangePass";
			}
			else if(usertype.equals("lock")){
				addErrorMessage("Your account has been unlocked");
				cm.setWrongCounter(email, 0);
				int count=cm.getRestCounter(email);
				cm.setRestCounter(email, ++count);
				removeSession();
				if(cm.getRestCounter(email)>=3){
					fm.setFlag(email);
					return "Pending";
				}
				
				return "Home";
			}
			else{
				addErrorMessage("Your account has been unlocked");
				cm.setWrongCounter(email, 0);
				int count=cm.getRestCounter(email);
				cm.setRestCounter(email, ++count);
				removeSession();
				if(cm.getRestCounter(email)>=3){
					fm.setFlag(email);
					return "Pending";
				}
				return "Reg";
			}
		}
		else{
			addErrorMessage("Your answers do not match");
			return "Questions";
		}
	}
	
	private void removeSession(){
		HttpSession session=(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		session.removeAttribute("type");
		session.removeAttribute("sq");
		int id=db.getID(email);
		session.setAttribute("ID", id);
	}
	
	private void addErrorMessage(String exc){
		FacesMessage message = new FacesMessage(exc);
		FacesContext.getCurrentInstance().addMessage(null, message);
		
	}
	
}
