package com.ctp.process;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Connections {
	private Connection conn;
	private PreparedStatement statement;
	private ResultSet rs;
	
	public Connections(Connection conn2,PreparedStatement ps,ResultSet rs){
		conn=conn2;
		statement=ps;
		this.rs=rs;
	}

	public Connection getConn() {
		return conn;
	}

	public PreparedStatement getStatement() {
		return statement;
	}

	public ResultSet getRs() {
		return rs;
	}
	
}
