package com.ctp.filters;

import java.io.IOException;

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
 * Servlet Filter implementation class HomeSecure
 */
@WebFilter({"/faces/Home.xhtml", "/faces/Reg.xhtml"})
public class HomeSecure implements Filter {

    /**
     * Default constructor. 
     */
    public HomeSecure() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session=request.getSession(false);
		String type=null;
		try{
			type=(String) session.getAttribute("type");
			if(type.equals("lock") || type.equals("newlock") || type.equals("forgot") || type==null){
				request.setAttribute("loginfirst", "Please login again");
			    request.getRequestDispatcher("index.xhtml").include(request, response);
			}
			else{
				chain.doFilter(req, res);
			}
		}
		catch(NullPointerException f){
			chain.doFilter(req, res);
		}
		
		catch(Exception e){
			request.setAttribute("loginfirst", "Please login again");
		    request.getRequestDispatcher("index.xhtml").include(request, response);
		}
			System.out.println(type);
			
			
		}
		
	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
