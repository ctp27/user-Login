package com.ctp.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.http.Part;

import com.ctp.beans.CustomerDBUtil;

public class Uploader {
	
//	Directory in which the images are to be stored
	public static final String dir="C:\\boom\\to\\image";
	
	
	public String exec(Part img,int ID){
		if(getLink(ID)==null){
			String link=writeFile(img);
			setLink(link,ID);
		}
		else{
			removeFromDisk(getLink(ID));
			String link=writeFile(img);
			setLink(link,ID);
		}
		return "Home?faces-redirect=true";
	}
	
	public String removeDP(int ID){
		removeFromDisk(getLink(ID));
		setLink(null,ID);
		return "Home?faces-redirect=true";
	}
	
	private void removeFromDisk(String link){
		String l=dir+"\\"+link;
		Path p =new File(l).toPath();
		try {
			Files.deleteIfExists(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setLink(String link,int ID){
		CustomerDBUtil db=null;
		try{
			db=CustomerDBUtil.getInstance();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		db.setLink(ID, link);
	}
	private String getLink(int ID){
		CustomerDBUtil db=null;
		try{
			db=CustomerDBUtil.getInstance();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		String s=db.getLink(ID);
		return s;
	}
	
	private String writeFile(Part img){
		try{
			InputStream Input=img.getInputStream();
			File f=File.createTempFile("ctp", "app");
			String filename=f.getName()+".png";
			Files.copy(Input, new File(dir,filename).toPath());
			return filename;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
