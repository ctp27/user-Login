package com.ctp.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.ctp.entities.Admin;
import com.ctp.entities.Counters;
import com.ctp.entities.QA;
import com.ctp.entities.UserState;
import com.ctp.process.Connections;



public class CustomerDBUtil {
	private static CustomerDBUtil instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/ctpDatabase";
	
	public static CustomerDBUtil getInstance() throws Exception {
		if (instance == null) {
			instance = new CustomerDBUtil();
		}
		
		return instance;
	}
	
	private CustomerDBUtil() throws Exception {		
		dataSource = getDataSource();
	}
	
	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
	
	private Connection getConnection() throws Exception {

		Connection theConn = dataSource.getConnection();
		
		return theConn;
	}
	
	/********* Login Authentication Code for user*********/
	
	public UserState authenticatetrial(String email,String password){
		Connection Conn = null;
		PreparedStatement ps = null;
		ResultSet Rs = null;
		
		try {
			Conn = getConnection();
			ps = Conn.prepareStatement("SELECT `ID`,`Flag`,`Email`, `pwd`,`Gender` FROM `customer` WHERE Email=? AND pwd=?");
			ps.setString(1, email);
			ps.setString(2, password);
			

			Rs = ps.executeQuery();
			if(Rs.next())
			{
				String gender=Rs.getString("Gender");
				Boolean flagState=Rs.getBoolean("Flag");
				if(gender==null && flagState==true)
				{
					return new UserState("New",Rs.getInt("ID"),flagState);
				}
				else if(gender!=null && flagState==true)
				{
					return new UserState("Old",Rs.getInt("ID"),flagState);
				}
				else    //if(gender!=null && flagState==false)
				{
					return new UserState("Old",Rs.getInt("ID"),flagState);
				}
			}
			else
			{
				return new UserState("Invalid",-1,false);
			}
				
		}
		catch(Exception e){
			e.printStackTrace();
			return new UserState("Error",-2,false);
		}
		finally {
			close (Conn, ps, Rs);
		}
		
	}
	
	public int[] authenticate(String email,String password){
		int a[]=new int[2];
		Connection Conn = null;
		PreparedStatement ps = null;
		ResultSet Rs = null;
		
		try {
			Conn = getConnection();
			ps = Conn.prepareStatement("SELECT `ID`,`Email`, `pwd`,`Gender` FROM `customer` WHERE Email=? AND pwd=?");
			ps.setString(1, email);
			ps.setString(2, password);
			

			Rs = ps.executeQuery();
			if(Rs.next())
			{
				String gender=Rs.getString("Gender");
				if(gender==null || gender.equals(""))
				{
					a[0]=0;
					a[1]=Rs.getInt("ID");
					return a;		
				}						// New account. Redirect to Registration
				else {
					a[0]=1;
					a[1]=Rs.getInt("ID");
					return a;			// Returning User. Redirect to Home.
				}
			}
			else
			{
				a[0]=-1;
				return a;
			}
				
		}
		catch(Exception e){
			System.out.println("Connection error");
			e.printStackTrace();
			a[0]=-2;
			return a;
		}
		finally {
			close (Conn, ps, Rs);
		}
		
	}
	
	
	/********* Login Authentication Code for Admin*********/
	
	public boolean authenticateAd(String email,String password){
		Connection Conn = null;
		PreparedStatement ps = null;
		ResultSet Rs = null;
		
		try {
			Conn = getConnection();
			ps = Conn.prepareStatement("SELECT `ID`,`Email`, `Password` FROM `admin` WHERE Email=? AND Password=?");
			ps.setString(1, email);
			ps.setString(2, password);
			

			Rs = ps.executeQuery();
			if(Rs.next())
			{
				
				int ID=Rs.getInt("ID");
				HttpSession session=(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
				session.setAttribute("ID",ID);
				
				return true;
			}
			else
			{
				return false;
			}
				
		}
		catch(Exception e){
			System.out.println("Connection error");
			e.printStackTrace();
			return false;
		}
		finally {
			close (Conn, ps, Rs);
		}
		
	}
	
/* *******************Sign Up code for Database**************************** */
	
	public String signUp(String firstName,String lastName,String email,String pwd){
		Connection Conn = null;
		PreparedStatement statement = null;
			try{
			Conn = getConnection();
			statement = Conn.prepareStatement("INSERT into customer(Email,pwd,FirstName,LastName) VALUES(?,?,?,?)");
            statement.setString(1,email);
            statement.setString(2, pwd);
            statement.setString(3, firstName);
            statement.setString(4, lastName);
            statement.executeUpdate();
			return "success";
			}
			catch(SQLIntegrityConstraintViolationException ve){
				return "repeat";
			}
			catch(Exception e){
				return "error";
			}
			finally{
				close(Conn,statement);
			}
		}
	
	public boolean signUp2(int ID,String sq1,String sq2,String sqa1,String sqa2){
		Connection Conn = null;
		PreparedStatement statement = null;
			try{
			Conn = getConnection();
			statement = Conn.prepareStatement("INSERT INTO `trackpass`(`ID`, `PwQ1`, `PwA1`, `PwQ2`, `PwA2`) VALUES (?,?,?,?,?)");
            statement.setInt(1,ID);
            statement.setString(2, sq1);
            statement.setString(3, sqa1);
            statement.setString(4, sq2);
            statement.setString(5, sqa2);
            statement.executeUpdate();
            return true;
			}
			catch(Exception e){
				e.printStackTrace();
				return false;
			}
			finally{
				close(Conn,statement);
			}
		}
	
	public int getID(String email){
		Connection Conn = null;
		PreparedStatement statement = null;
		ResultSet rs=null;
		try {
			Conn = getConnection();
			statement = Conn.prepareStatement("SELECT `ID` FROM `customer` WHERE email=?");
			statement.setString(1, email);
			rs=statement.executeQuery();
			rs.next();
			int id=rs.getInt("ID");
			return id;
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
		finally{
			close(Conn,statement,rs);
		}
	}
	
	/* ****************Prepopulate Registration code************************** */
	
	public Connections getValues(int ID){
		Connection Conn = null;
		PreparedStatement statement = null;
		ResultSet rs=null;
		Connections connections=null;
		try{
		Conn=getConnection();
		statement = Conn.prepareStatement("SELECT `ID`,`Prefix`,`Image`, `FirstName`, `LastName`,`Gender`, `DOB`, `Phone`, `Email`, `Pan`, `Aadh`, `Pass`, `Lic`, `Address`, `City`, `State`, `Suggest` FROM `customer` WHERE ID=?;");
		statement.setInt(1,ID);
		rs = statement.executeQuery();
		
		connections=new Connections(Conn,statement,rs);
		return connections;
		}
		catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		catch(Exception f){
			f.printStackTrace();
			return null;
		}
		finally{
			
		}
	}
	
	/* *************Registration submit ******************** */
	
	public boolean setRegValues(int ID,String prefix,String firstName,String lastName,String gender,
								String date,String email,String phone,String address,String city,
								String state,String suggest,boolean pan,boolean aadh,boolean pass,boolean lic){
		 Connection Conn=null;
		 PreparedStatement statement=null;
		 try{
			 Conn=getConnection();
          statement = Conn.prepareStatement("UPDATE `customer` SET "
         		+ "`Prefix`=?,"
         		+ "`FirstName`=?,"
         		+ "`LastName`=?,"
         		+ "`Gender`=?,"
         		+ "`DOB`=?,"
         		+ "`Phone`=?,"
         		+ "`Email`=?,"
         		+ "`Pan`=?,"
         		+ "`Aadh`=?,"
         		+ "`Pass`=?,"
         		+ "`Lic`=?,"
         		+ "`Address`=?,"
         		+ "`City`=?,"
         		+ "`State`=?,"
         		+ "`Suggest`=?"
         		+ " WHERE ID=?");
         statement.setString(1, prefix);
         statement.setString(2, firstName);
         statement.setString(3, lastName);
         statement.setString(4, gender);
         statement.setString(5, date);
         statement.setString(6, phone);
         statement.setString(7, email);
         statement.setBoolean(8, pan);
         statement.setBoolean(9, aadh);
         statement.setBoolean(10, pass);
         statement.setBoolean(11, lic);
         statement.setString(12, address);
         statement.setString(13, city);
         statement.setString(14, state);
         statement.setString(15, suggest);
         statement.setInt(16, ID);
         int i=statement.executeUpdate();
         if(i!=0){
        	 return true;
         }
         else return false;
		 }catch(Exception e){
			 e.printStackTrace();
			 return false;
		}
		 finally{
			 close(Conn,statement);
		 }
	}
	/* *************DB code for pending page ******************** */
	
	public String getFirstName(int ID){
		Connection Conn = null;
		PreparedStatement statement = null;
		ResultSet rs=null;
		try {
			Conn = getConnection();
			statement = Conn.prepareStatement("SELECT `FirstName` FROM `customer` WHERE ID=?");
			statement.setInt(1, ID);
			rs=statement.executeQuery();
			rs.next();
			return rs.getString("FirstName");
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{
			close(Conn,statement,rs);
		}
	}
	
	/* *************DB code for populating Admin page******************** */
	
	public ArrayList<Customer> loadList(){
		ArrayList<Customer> custList=new ArrayList<Customer>();
		Connection Conn = null;
		Statement statement = null;
		ResultSet rs=null;
		String link=null;
		String sql="SELECT `ID`,`Image`,`Flag`,`FirstName`,`LastName`,`Email` FROM `customer` ORDER BY LastName";
		try{
			Conn=getConnection();
			statement = Conn.createStatement();
			rs=statement.executeQuery(sql);
			while(rs.next())
			{
				int ID=rs.getInt("ID");
				boolean flag=rs.getBoolean("Flag");
				String fn=rs.getString("FirstName");
				String ln=rs.getString("LastName");
				String em=rs.getString("Email");
				if(rs.getString("Image")!=null)
					link=rs.getString("Image");
					
				custList.add(new Customer(ID,flag,fn,ln,em,link));
			}
			return custList;
		}
		catch(Exception e){
			System.out.println("Customer DBUtil exception");
			e.printStackTrace();
			return null;
		}
		finally{
			close(Conn,statement,rs);
		}
	}
	
	public boolean activateCust(int ID){
		Connection Conn = null;
		PreparedStatement statement = null;
		try{
			Conn=getConnection();
			statement=Conn.prepareStatement("UPDATE `customer` SET `Flag` = '0' WHERE `customer`.`ID` =?");
			statement.setInt(1, ID);
			int i=statement.executeUpdate();
			if(i!=0){
				return true;
			}
			else
				return false;
		}
		catch(Exception e){
			System.out.println("Error in DBUTil activation");
			e.printStackTrace();
			return false;
		}
		finally{
			close(Conn,statement);
		}
	}
	
	public boolean deactivateCust(int ID){
		Connection Conn = null;
		PreparedStatement statement = null;
		try{
			Conn=getConnection();
			statement=Conn.prepareStatement("UPDATE `customer` SET `Flag` = '1' WHERE `customer`.`ID` =?");
			statement.setInt(1, ID);
			int i=statement.executeUpdate();
			if(i!=0){
				return true;
			}
			else
				return false;
		}
		catch(Exception e){
			System.out.println("Error in DBUTil deactivation");
			e.printStackTrace();
			return false;
		}
		finally{
			close(Conn,statement);
		}
	}
	
	public boolean deleteCust(int ID){
			Connection Conn = null;
			PreparedStatement statement = null;
			try{
				Conn=getConnection();
				statement=Conn.prepareStatement("DELETE FROM `customer` WHERE `customer`.`ID` = ?");
				statement.setInt(1, ID);
				int i=statement.executeUpdate();
				if(i!=0)
					return true;
				else 
					return false;
			}
			catch(Exception e){
				System.out.println("Error in deleting user");
				e.printStackTrace();	
				return false;			}
			finally{
				close(Conn,statement);
			}
	}
	
	public boolean addAdmin(Admin admin){
		Connection Conn = null;
		PreparedStatement statement = null;
		try{
			Conn=getConnection();
			return false;
		}
		catch(Exception e){
			System.out.println("Exception in addAdmin()");
			return false;
		}
		finally{
			close(Conn,statement);
		}
	}
	
	public String getPassword(int ID){
		Connection Conn = null;
		PreparedStatement statement = null;
		ResultSet rs=null;
		try {
			Conn = getConnection();
			statement = Conn.prepareStatement("SELECT `pwd` FROM `customer` WHERE ID=?");
			statement.setInt(1, ID);
			rs=statement.executeQuery();
			rs.next();
			return rs.getString("pwd");
		}
		catch(Exception e){
			System.out.println("Exception in getPassword()");
			e.printStackTrace();
			return null;
		}
		finally{
			close(Conn,statement,rs);
		}
	}
	
	public void updatePassword(int ID,String pwd){
		Connection Conn = null;
		PreparedStatement statement = null;
		try {
			Conn = getConnection();
			statement = Conn.prepareStatement("UPDATE `customer` SET `pwd` =? WHERE `customer`.`ID` = ?;");
			statement.setString(1, pwd);
			statement.setInt(2, ID);
			statement.executeUpdate();
			
		}
		catch(Exception e){
			System.out.println("Exception in getPassword()");
			e.printStackTrace();
		}
		finally{
			close(Conn,statement);
		}
		
	}
	
	/* ***************  Counter getter setters  ******************** */
	
	public Counters getCounters(int ID){
		Connection Conn=null;
		PreparedStatement statement = null;
		ResultSet rs=null;
		Counters counter=null;
		try{
			Conn=getConnection();
			statement=Conn.prepareStatement("SELECT `WrongCounter`,`RestCounter` FROM `trackpass` WHERE ID=?");
			statement.setInt(1, ID);
			rs=statement.executeQuery();
			rs.next();
			counter=new Counters(rs.getInt("WrongCounter"),rs.getInt("RestCounter"));
			return counter;
		}catch(Exception e){
		return null;
		}
		finally{
			close(Conn,statement,rs);
		}
	}

	public void setWrongCounters(int wrc, int ID) {
		Connection Conn=null;
		PreparedStatement statement=null;
		try{
			Conn=getConnection();
			statement=Conn.prepareStatement("UPDATE `trackpass` SET `WrongCounter` =? WHERE `trackpass`.`ID` =?;");
			statement.setInt(1, wrc);
			statement.setInt(2, ID);
			statement.executeUpdate();		
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			close(Conn,statement);
		}
	}
	
	public void setRestCounter(int rc, int ID) {
		Connection Conn=null;
		PreparedStatement statement=null;
		try{
			Conn=getConnection();
			statement=Conn.prepareStatement("UPDATE `trackpass` SET `RestCounter` =? WHERE `trackpass`.`ID` =?;");
			statement.setInt(1, rc);
			statement.setInt(2, ID);
			statement.executeUpdate();		
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			close(Conn,statement);
		}
	}
	
	public QA getQA(String email){
		QA qa=null;
		int id=getID(email);
		Connection Conn=null;
		PreparedStatement statement=null;
		ResultSet rs=null;
		try{
			Conn=getConnection();
			statement=Conn.prepareStatement("SELECT `PwQ1`,`PwA1`,`PwQ2`,`PwA2` FROM `trackpass` WHERE ID=?");
			statement.setInt(1, id);
			rs=statement.executeQuery();
			rs.next();
			qa=new QA(rs.getString("PwQ1"),rs.getString("PwQ2"),rs.getString("PwA1"),rs.getString("PwA2"));
			return qa;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{
			close(Conn,statement,rs);
		}
	}
	
	public void setLink(int ID,String link){
		Connection Conn=null;
		PreparedStatement statement=null;
		try{
			Conn=getConnection();
			statement=Conn.prepareStatement("UPDATE `customer` SET `Image` = ? WHERE `customer`.`ID` = ?;");
			statement.setString(1, link);
			statement.setInt(2, ID);
			statement.executeUpdate();		
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			close(Conn,statement);
		}
	}
	
	public String getLink(int ID){
		Connection Conn=null;
		PreparedStatement statement=null;
		ResultSet rs=null;
		try{
			Conn=getConnection();
			statement=Conn.prepareStatement("SELECT `Image` FROM `customer` WHERE ID=?");
			statement.setInt(1, ID);
			rs=statement.executeQuery();
			rs.next();
			String s=rs.getString("Image");
			return s;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{
			close(Conn,statement,rs);
		}
	}
	
	private void close(Connection theConn, Statement theStmt) {
		close(theConn, theStmt, null);
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
