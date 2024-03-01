package in.anmol.backend;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/loginForm")
public class Login extends HttpServlet
{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		res.setContentType("text/html");
		// Display data to browser	
		PrintWriter out = res.getWriter();
		
		String myemail = req.getParameter("email1");
		String mypass = req.getParameter("pass1");
		
		// Database connectivity
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/reg_page", "root", "password");
			
			PreparedStatement ps = conn.prepareStatement("select * from register where email=? and password=?");
			ps.setString(1, myemail);
			ps.setString(2, mypass);
			
			// if query is execute
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				// print the session name in profile.jsp page
				HttpSession session = req.getSession();
				session.setAttribute("session_name", rs.getString("name")); // name -> database table name
				
				RequestDispatcher rd = req.getRequestDispatcher("/profile.jsp");
				rd.include(req, res);
			}
			else
			{
				// print error
				out.print("<h3 style='color:red'> Email and password didnt matched </h3>");
				
				RequestDispatcher rd = req.getRequestDispatcher("/login.jsp");
				rd.include(req, res);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			out.print("<h3 style='color:red'> Exception occured: "+e.getMessage()+"</h3>");
			
			RequestDispatcher rd = req.getRequestDispatcher("/login.jsp");
			rd.include(req, res);
		}
	}
}
