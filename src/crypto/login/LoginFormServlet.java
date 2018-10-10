package crypto.login;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Login Form
 */
@WebServlet("/LoginFormServlet")
public class LoginFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginFormServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		PrintWriter writer=response.getWriter();
		response.setContentType("text/html");

		String callerURL=((HttpServletRequest)request).getRequestURI();
		request.setAttribute("URL", callerURL);
		
		writer.println("<html>\n<head><title>login form</title></head>\n"
				+ "<body>\n<form method=\"post\" action=\"LoginServlet\">"
	        +"Email ID:<input type=\"text\" name=\"email\" /><br/>"
	        +"Password:<input type=\"password\" name=\"pass\"/><br/>"
	        +"<input type=\"submit\" value=\"login\" />"
	        +"</form></body></html>");
		
		writer.close();
	 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
