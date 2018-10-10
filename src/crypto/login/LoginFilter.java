package crypto.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Login Filter um zu prüfen ob User eingeloggt ist oder nicht
 */


@WebFilter({ "/GenerateKeysForm.jsp","/RequestKeysForm.jsp","/UploadKeysForm2.jsp"})
public class LoginFilter implements Filter {

	/**
	 * Default constructor. 
	 */
	public LoginFilter() {
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
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	
		HttpSession mySession=((HttpServletRequest)request).getSession();
		PrintWriter writer=response.getWriter();
		
	
		
		//Check if logged in
		if(mySession.getAttribute("USER_ID")==null)
		{
			
			request.setAttribute("URL",((HttpServletRequest)request).getRequestURI() );
			String callerURL=((HttpServletRequest)request).getRequestURI();
			request.setAttribute("URL", callerURL);
			RequestDispatcher dispatcher = request.getRequestDispatcher("LoginFormServlet");
			dispatcher.forward(request, response);
		}
		else
		{

			chain.doFilter(request, response);
		}
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
