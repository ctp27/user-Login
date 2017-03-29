package com.ctp.filters;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class SessionCheck
 */
@WebFilter(description = "Checks if Session exists", urlPatterns = { "/*" })
public class SessionCheck implements Filter {
	private ArrayList<String> URLlist;
	
    /**
     * Default constructor. 
     */
    public SessionCheck() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String url = request.getRequestURI();
//		System.out.println(url);
		boolean pass=false;
		if(URLlist.contains(url))
		{
			pass=true;
			chain.doFilter(req,res);
		}
			
		if(pass==false)
		{
			HttpSession session=request.getSession(false);
			
			if(session!=null)
			{
				try{
				int ID=(Integer)session.getAttribute("ID");
				chain.doFilter(req, res);
				
				}
				catch(Exception e){
					
//					e.printStackTrace();
					request.setAttribute("loginfirst", "Please login again");
				    request.getRequestDispatcher("index.xhtml").include(request, response);
				}
			}
			else{
				request.setAttribute("loginfirst", "Please login again");
			    request.getRequestDispatcher("index.xhtml").include(request, response);
			}
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	
	
	public void init(FilterConfig fConfig) throws ServletException {
		URLlist=new ArrayList<String>();
		URLlist.add("/UserLogin/faces/index.xhtml");
		URLlist.add("/UserLogin/faces/Creation.xhtml");
		URLlist.add("/UserLogin/faces/fpassword.xhtml");
		URLlist.add("/UserLogin/faces/Questions.xhtml");
		URLlist.add("/UserLogin/faces/Test.xhtml");
		
		URLlist.add("/UserLogin/faces/ChangePass.xhtml");
		URLlist.add("/UserLogin/faces/javax.faces.resource/css/styles.css");
		URLlist.add("/UserLogin/faces/javax.faces.resource/jsf.js");
		URLlist.add("/UserLogin/faces/javax.faces.resource/default.png");
	}
	
	
}
