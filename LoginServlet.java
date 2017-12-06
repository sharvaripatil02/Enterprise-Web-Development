import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.*;

public class LoginServlet extends HttpServlet {
	
	HashMap<String, User> userMap = null;
	String username = null,password= null,role = null;
	boolean flag = false;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		PrintWriter out = response.getWriter();

		Utilities util = new Utilities(request,out);
		
		util.getDefaultTemplate("header.html");
		util.getDefaultTemplate("leftNavigation.html");		
		util.getLoginTemplate();		
		util.getDefaultTemplate("footer.html");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException 
	{
		try {

			username = request.getParameter("username").toLowerCase().trim();
			password = request.getParameter("password").toLowerCase().trim();
			role = request.getParameter("role").toLowerCase().trim();


			HashMap<String, User> hmUsers = WfCache.UserDetails();
			
			if (hmUsers != null) 
			{
				if (hmUsers.containsKey(username)) 
				{
					User user = hmUsers.get(username);
					if (user.getPassword().equals(password) && user.getRole().equalsIgnoreCase(role) && user.getUsername().equalsIgnoreCase(username)) 
					{
						WfCache.username = username;
						WfCache.role = role;
						response.sendRedirect("hubHome");
					} 
					else 
					{
						response.setContentType("text/html");
						java.io.PrintWriter out = response.getWriter();
						Utilities util = new Utilities(request,out);
		
						util.getDefaultTemplate("header.html");
						util.getDefaultTemplate("leftNavigation.html");

						out.println("<div class='col-md-9' align='justify'>");

						out.println("<h2>" + "Login Failed. Incorrect Credentials" + "</h2>");
						out.println("<br/>");
						out.println("<a href='hubHome'> Home Page </a>");
						out.println("<br/>");
						out.println("<a href='hubLogin'> Login Page </a>");
						out.println("</div >");
						out.println("</div >");
						util.getDefaultTemplate("footer.html");
						out.close();
					}
				}
				else
				{
					response.setContentType("text/html");
					java.io.PrintWriter out = response.getWriter();
					Utilities util = new Utilities(request,out);
		
					util.getDefaultTemplate("header.html");
					util.getDefaultTemplate("leftNavigation.html");

					out.println("<div class='col-md-9' align='justify'>");

					out.println("<h2>" + "Login Failed. Incorrect Credentials" + "</h2>");
					out.println("<br/>");
					out.println("<a href='hubHome'> Home Page </a>");
					out.println("<br/>");
					out.println("<a href='hubLogin'> Login Page </a>");
					out.println("</div >");
					out.println("</div >");
					util.getDefaultTemplate("footer.html");
					out.close();
				}
			}
		}
		catch(Exception ex)
		{
			System.out.println("LoginServlet- "+ ex.toString());
		}
	}
}
