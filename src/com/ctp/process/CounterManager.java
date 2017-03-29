package com.ctp.process;

import com.ctp.beans.CustomerDBUtil;
import com.ctp.entities.Counters;

public class CounterManager {
	public int getWrongCounter(String email){
		CustomerDBUtil db=null;
		Counters counters=null;
		try{
			db=CustomerDBUtil.getInstance();}
		catch(Exception connect){
			connect.printStackTrace();
			
		}
		int pos=getInvalidID(email);
		counters=db.getCounters(pos);
		return counters.getWrongCounter();
	}
	
	public void setWrongCounter(String email,int value){
		CustomerDBUtil db=null;
		try{
			db=CustomerDBUtil.getInstance();}
		catch(Exception connect){
			connect.printStackTrace();
			
		}
		int id=getInvalidID(email);
		db.setWrongCounters(value,id);
	}
	
	public int getRestCounter(String email){
		CustomerDBUtil db=null;
		Counters counters=null;
		try{
			db=CustomerDBUtil.getInstance();}
		catch(Exception connect){
			connect.printStackTrace();
			
		}
		int id=getInvalidID(email);
		counters=db.getCounters(id);
		return counters.getRestCounter();
	}
	public int getRestCounter(int ID){
		CustomerDBUtil db=null;
		Counters counters=null;
		try{
			db=CustomerDBUtil.getInstance();}
		catch(Exception connect){
			connect.printStackTrace();
			
		}
		counters=db.getCounters(ID);
		return counters.getRestCounter();
	}
	public void setRestCounter(String email,int value){
		CustomerDBUtil db=null;
		try{
			db=CustomerDBUtil.getInstance();}
		catch(Exception connect){
			connect.printStackTrace();
			
		}
		int id=getInvalidID(email);
		db.setRestCounter(value, id);
	}
	
	private int getInvalidID(String email){
		
		CustomerDBUtil db=null;
		try{
			db=CustomerDBUtil.getInstance();}
		catch(Exception connect){
			connect.printStackTrace();
			
		}
		int id=db.getID(email);
		return id;
}
}
