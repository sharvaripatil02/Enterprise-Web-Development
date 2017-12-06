import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.*;
import java.sql.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ViewUserSubscriptionServlet extends HttpServlet 
{
	String username = null;
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try
		{
					
			MySqlDataStoreUtilities sqlData  = new MySqlDataStoreUtilities();		
			HashMap<Integer, Subscription> hm = sqlData.fetchUserSubscriptionDetails(WfCache.username);
				
			PrintWriter out = response.getWriter();
			Utilities util = new Utilities(request,out);
			
			util.getDefaultTemplate("header.html");
			util.getDefaultTemplate("leftNavigation.html");		
			util.getUserSubscriptionTemplate(hm);		
			util.getDefaultTemplate("footer.html");		
		
		}
		catch(Exception ex)
		{
			System.out.println("ViewUserSubscriptionServlet- " + ex.toString());
		}
			
	}
}