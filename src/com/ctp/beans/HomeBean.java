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
import javax.faces.bean.ViewScoped;
import javax.servlet.http.Part;

import com.ctp.controllers.Uploader;
import com.ctp.process.Connections;
import com.ctp.process.CounterManager;

@ManagedBean
@ViewScoped
public class HomeBean {
	
	private String prefix,link,firstName,lastName,gender,areaCode,phoneNumber,email,address,city,state,suggest;
	private String id[];
	private int ID;
	private Part image;
	String date;
	private boolean upload=false;
	private CounterManager cm;
	private Uploader up;
	@ManagedProperty("#{login}")
		private Login login;
	
	public HomeBean(){
		cm=new CounterManager();
		 up=new Uploader();
	}
	
	@PostConstruct
	public void Load(){
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
			link=rs.getString("Image");
			System.out.println("The image is"+link);
			cm.setWrongCounter(email, 0);
		}
		catch(Exception e){e.printStackTrace();}
		finally{
			close(Conn,ps,rs);
		}
		}
	
	public void displayUpload(){
		upload=true;
	}
	
	public String uploadIt(){
		
		String action=up.exec(image, ID);
		return action;
	}
	
	public String removeDp(){
		String s=up.removeDP(ID);
		return s;
	}
	
	public void hideUpload(){
		upload=false;
	}
	
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public boolean isUpload() {
		return upload;
	}

	public void setUpload(boolean upload) {
		this.upload = upload;
	}

	public Part getImage() {
		return image;
	}

	public void setImage(Part image) {
		this.image = image;
	}

	public void setLogin(Login login) {
		this.login = login;
	}
	
	
	public String getPrefix() {
		return prefix;
	}



	public String getFirstName() {
		return firstName;
	}



	public String getLastName() {
		return lastName;
	}



	public String getGender() {
		return gender;
	}



	public String getAreaCode() {
		return areaCode;
	}



	public String getPhoneNumber() {
		return phoneNumber;
	}



	public String getEmail() {
		return email;
	}



	public String getAddress() {
		return address;
	}



	public String getCity() {
		return city;
	}



	public String getState() {
		return state;
	}



	public String getSuggest() {
		return suggest;
	}



	public String[] getId() {
		return id;
	}



	public int getID() {
		return ID;
	}



	public String getDate() {
		return date;
	}



	public Login getLogin() {
		return login;
	}



	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	public void setId(String[] id) {
		this.id = id;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setCm(CounterManager cm) {
		this.cm = cm;
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
