package com.ctp.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.ctp.process.Connections;
import com.ctp.process.CounterManager;
import com.ctp.process.Populate;

@ManagedBean
@RequestScoped
public class Register {
	
	private String prefix,firstName,lastName,gender,areaCode,phoneNumber,email,address,city,state,suggest;
	private String id[];
	private int ID;
	String date;
	private boolean pan=false,aadh=false,pass=false,lic=false;
	private ArrayList<String> prefixList;
	private ArrayList<String> stateList;
	
	private CounterManager cm;
	@ManagedProperty("#{login}")
	private Login login;
	
	
	public Register(){
		id=new String[4];
		prefixList=Populate.fillPrefix();
		stateList=Populate.fillStates();
		cm=new CounterManager();
	}
	
	@PostConstruct
	private void Load(){
		ResultSet rs = null;
		Connection Conn=null;
		PreparedStatement ps=null;
		Connections c=null;
		int n=login.getID();
		try{
			CustomerDBUtil db=CustomerDBUtil.getInstance();
			c=db.getValues(n);
			Conn=c.getConn();
			ps=c.getStatement();
			rs=c.getRs();
			rs.next();
			ID=rs.getInt("ID");
			prefix=rs.getString("Prefix");
			firstName=rs.getString("FirstName");
			lastName=rs.getString("LastName");
			gender=rs.getString("Gender");
			date=rs.getString("DOB");
			email=rs.getString("email");
			if(rs.getString("Phone")!=null)
			AreaCode(rs.getString("Phone"));
			setArray(rs);
			address=rs.getString("Address");
			city=rs.getString("City");
			state=rs.getString("State");
			suggest=rs.getString("Suggest");
			if(cm.getWrongCounter(email)>0){
				cm.setWrongCounter(email, 0);
			}
		}
		catch(Exception e){e.printStackTrace();}
		finally{
			close(Conn,ps,rs);		
		}
		}
		


	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getID() {
		return ID;
	}


	public void setLogin(Login login) {
		this.login = login;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSuggest() {
		return suggest;
	}
	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	public ArrayList<String> getPrefixList() {
		return prefixList;
	}

	public ArrayList<String> getStateList() {
		return stateList;
	}
	public String[] getId() {
		return id;
	}
	public void setId(String[] id) {
		this.id = id;
	}
	
	private String injectNonFlag(){
		CustomerDBUtil db=null;
		boolean control;
		try{
			 db= CustomerDBUtil.getInstance();
			
		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception a){
			a.printStackTrace();
		}
		setBooleans();
		String phone=areaCode+"-"+phoneNumber;
		control=db.setRegValues(ID,prefix,firstName,lastName,gender,date,email,phone,address,city,
				state,suggest,pan,aadh,pass,lic);
		if(control==true)
			return "Home?faces-redirect=true";
		else
			return "Reg?faces-redirect=true";
		
	}
	
	public String changePass(){
		HttpSession session=(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		session.setAttribute("sq", email);
		session.setAttribute("type","logged");
		return "ChangePass?faces-redirect=true";
	}
	
	public String exec(){
		String s;
		if(login.isFlag()){
			s=injectFlag();
			return s;
		}
		else{
			s=injectNonFlag();
			return s;
		}
		
	}
	
	private String injectFlag(){
		CustomerDBUtil db=null;
		boolean control;
		try{
			 db= CustomerDBUtil.getInstance();
			
		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception a){
			a.printStackTrace();
		}
		setBooleans();
		String phone=areaCode+"-"+phoneNumber;
		control=db.setRegValues(ID,prefix,firstName,lastName,gender,date,email,phone,address,city,
				state,suggest,pan,aadh,pass,lic);
		if(control==true){
			HttpSession session=(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			session.setAttribute("type", "lock");
			return "Pending?faces-redirect=true";
		}
		else
			return "Reg?faces-redirect=true";
		
	}
	
	private void AreaCode(String no){
		String arr[]=no.split("-");
		areaCode=arr[0];
		phoneNumber=arr[1];
	}
	
	
	private void setArray(ResultSet rs){   //Logic check
		Object s[]=new Object[4];
		ArrayList<String> al=new ArrayList<String>();
		ArrayList<Boolean> bl=new ArrayList<Boolean>();
		al.add("PAN");
		al.add("Aadhar");
		al.add("Passport");
		al.add("License");
		try{
		 bl.add(rs.getBoolean("Pan"));
		 bl.add(rs.getBoolean("Aadh"));
		 bl.add(rs.getBoolean("Pass"));
		 bl.add(rs.getBoolean("Lic"));
		}
		catch(SQLException e){e.printStackTrace();}
		for(int i=0;i< bl.size();i++)
		{
			if(bl.get(i)==false)
			{
				al.remove(i);
				bl.remove(i);
				i--;
			}
		}
		s=al.toArray();
		String[] stringArray = Arrays.copyOf(s, s.length, String[].class);
		id=stringArray;
	}
	private void setBooleans(){
		for(int i=0;i<id.length;i++)
		{
			if(id[i].equals("PAN"))
			{
				pan=true;
			}
			else if(id[i].equals("Aadhar"))
			{
				aadh=true;
			}
			else if(id[i].equals("Passport"))
			{
				pass=true;
			}
			else if(id[i].equals("License")){
				lic=true;
			}
			
			
		}
	}
	private void close(Connection theConn, Statement theStmt, ResultSet theRs) {

		try {
			if (theRs != null) {
				theRs.close();
			}

			if (theStmt != null) {
				theStmt.close();
			}

			if (theConn != null) {
				theConn.close();
			}
			
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
}
