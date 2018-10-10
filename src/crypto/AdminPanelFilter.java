package crypto;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class AdminPanelFilter
 */
@WebFilter({ "/AdminPanelFilter", "/AdminPanelFormDownloadedKeys.jsp", "/AdminPanelFormGeneratedKeys.jsp" })
public class AdminPanelFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public AdminPanelFilter() {
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
		// Check if logged in
		HttpSession mySession = ((HttpServletRequest) request).getSession();
		if (mySession.getAttribute("USER_ID") == null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("LoginFormServlet");
			dispatcher.forward(request, response);
		} else {
			String user_id = (String) mySession.getAttribute("USER_ID").toString();
			// If User-ID is not 1 (=Admin) then redirect to index-page,
			// otherwise
			// continue
			if (Integer.valueOf(user_id) != 1) {

				RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
				dispatcher.forward(request, response);
			} else {

				chain.doFilter(request, response);
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
