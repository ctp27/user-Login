package com.ctp.process;

import com.ctp.beans.CustomerDBUtil;

public class FlagManager {
	
	public void setFlag(String email){
		CustomerDBUtil db=null;
		try{
			db=CustomerDBUtil.getInstance();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		int id=db.getID(email);
		db.deactivateCust(id);
	}
}
