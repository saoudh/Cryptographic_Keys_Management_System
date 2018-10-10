package crypto.login;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crypto.MyDB;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

/**
 * Servlet implementation class Login
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String email = request.getParameter("email");
		String pass = request.getParameter("pass");

		if (MyDB.checkUser(email, pass)) {

			out.println("Username or Password correct");

			HttpSession mySession = request.getSession();
			int user_id = MyDB.getUserID(email);
			String user_name = MyDB.getUserName(email);

			mySession.setAttribute("USER_ID", user_id);
			mySession.setAttribute("USER_NAME", user_name);

			// return to caller-page after successfull login
			String callerURL = (String) request.getAttribute("URL");
			RequestDispatcher dispatcher = null;
			if (callerURL != null) {
				dispatcher = request.getRequestDispatcher(callerURL);
			} else {

				dispatcher = request.getRequestDispatcher("index.jsp");

			}
			dispatcher.forward(request, response);

		} else {
			out.println("Username or Password incorrect");
			RequestDispatcher rs = request.getRequestDispatcher("LoginFormServlet");
			rs.include(request, response);
		}
	}

}
