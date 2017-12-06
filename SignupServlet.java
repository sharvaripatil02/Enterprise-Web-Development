import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;  
import javax.servlet.http.*;

public class SignupServlet extends HttpServlet {

	HashMap<String, User> hmUsers = null;
	boolean flag = true;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		PrintWriter out = response.getWriter();

		Utilities util = new Utilities(request,out);
		
		util.getDefaultTemplate("header.html");
		util.getDefaultTemplate("leftNavigation.html");		
		util.getSignUpTemplate();		
		util.getDefaultTemplate("footer.html");		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException	
	{
		try
		{
			User user = new User();
			
			String username = request.getParameter("username").toLowerCase().trim();
			String role = request.getParameter("role").toLowerCase().trim();

			String phonee = request.getParameter("phone").toLowerCase().trim();
			
			user.setFirstName(request.getParameter("firstname"));
			user.setLastName(request.getParameter("lastname"));
			user.setUsername(username);
			user.setPassword(request.getParameter("password"));
			user.setPhone(request.getParameter("phone"));
			user.setRole(role);
			user.setAddress(request.getParameter("address"));
			user.setCity(request.getParameter("city"));
			user.setCountry(request.getParameter("country"));
			user.setState(request.getParameter("state"));
			user.setZipcode(Integer.parseInt(request.getParameter("zipcode")));


			HashMap<String, User> hmUsers = WfCache.UserDetails();

			if(hmUsers != null) 
			{

				if (hmUsers.containsKey(user.getUsername())) {

						response.setContentType("text/html");
						PrintWriter out = response.getWriter();

						out.println("<html>");
						out.println("<head>");
						out.println("<title>Signup Servlet Result</title>");
						out.println("</head>");
						out.println("<body>");
						out.println("<h2>" + "Signup Failed. User already exists!!" + "</h2>");
						out.println("<br/>");
						out.println("<a href='hubHome'> Home Page </a>");
						out.println("</body>");
						out.println("</html>");
						out.close();

				} else {


					MySqlDataStoreUtilities sqlData = new MySqlDataStoreUtilities();

					if (sqlData.insertUserDetails(user)) {

						WfCache.wfUserData.put(user.getUsername(), user);

						response.setContentType("text/html");
						PrintWriter out = response.getWriter();
						out.println("<html>");
						out.println("<head> </head>");
						out.println("<body>");
						out.println("<h1> Welcome, " + user.getFirstName().trim() + "</h1>");
						out.println("<br/>");
						out.println("<a href='hubHome'> Home Page </a>");
						out.println("</body>");
						out.println("</html>");

					} else {

						response.setContentType("text/html");
						java.io.PrintWriter out = response.getWriter();
						out.println("<html>");
						out.println("<head>");
						out.println("<title>Signup Servlet Result</title>");
						out.println("</head>");
						out.println("<body>");
						out.println("<h2>" + "Signup Failed. SQL Update query not working!!" + "</h2>");
						out.println("<br/>");
						out.println("<a href='hubHome'> Home Page </a>");
						out.println("</body>");
						out.println("</html>");
						out.close();

					}
				}

			}

			
		}catch(Exception ex){
			System.out.println("SignupServlet- " + ex.toString());
		}
	}

}