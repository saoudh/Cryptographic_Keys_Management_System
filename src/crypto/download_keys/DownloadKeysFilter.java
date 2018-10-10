package crypto.download_keys;

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

import crypto.MyDB;

/**
 * Servlet Filter implementation class DownloadKeysFilter
 */
@WebFilter("/DownloadKeysForm.jsp")
public class DownloadKeysFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public DownloadKeysFilter() {
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
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		HttpSession mySession = ((HttpServletRequest) request).getSession();
		PrintWriter writer = response.getWriter();
		String myurl = null;
		try {
			myurl = (String) request.getParameter("myurl");
		} catch (NullPointerException e) {
			writer.println("false link!");
		}
		int user_id = -1;
		// Check if logged in. If not, then change to Login-Site
		if (mySession.getAttribute("USER_ID") == null) {

			RequestDispatcher dispatcher = request.getRequestDispatcher("LoginFormServlet");

			dispatcher.forward(request, response);
		}
		// check if user is authorised to access the link or if the link is
		// correct
		else {
			String str_user_id = mySession.getAttribute("USER_ID").toString();

			user_id = Integer.parseInt(str_user_id);
			boolean isAllowed = false;
			try {
				isAllowed = MyDB.checkIfURLBelongsToUser(user_id, myurl);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (isAllowed) {
				writer.println("Link is available!");

				chain.doFilter(request, response);
			} else {
				writer.println("Link is not available or you are not permitted to access it!");
			}

		}

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
