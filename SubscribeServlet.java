import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.*;
import java.sql.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import java.util.Date;

public class SubscribeServlet extends HttpServlet 
{
	public HashMap<String, MovieCatalog> mveHm = null;
	MySqlDataStoreUtilities sqlData = new MySqlDataStoreUtilities();
	MovieCatalog mvc = null;
	String username = null;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		PrintWriter out = response.getWriter();

		Utilities util = new Utilities(request,out);
		
		util.getDefaultTemplate("header.html");
		util.getDefaultTemplate("leftNavigation.html");		
		util.getSubscribeTemplate();		
		util.getDefaultTemplate("footer.html");		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException 
	{
		try 
		{			
			String custName = request.getParameter("custName");
			String custAddr = request.getParameter("custAddr");
			String creditCard = request.getParameter("cardnumber");		

			System.out.println("hey in checkout");
						
			
			mveHm = WfCache.showCart();
			

			if(sqlData.insertSubscriptionDetails(mveHm, creditCard, WfCache.username, custAddr))
			{
				WfCache.clearCart();
				WfCache.cartCount = 0;
								
				response.setContentType("text/html");
				java.io.PrintWriter out = response.getWriter();
				Utilities util = new Utilities(request,out);
		
				util.getDefaultTemplate("header.html");
				out.println("<h2>Order Successfully placed.</h2>");
				out.println("<h4>Subscribed to Movies at "+ WfCache.subscriptionTime +".</h4>");
				out.println("<br/>");
				out.println("<a href='hubHome'> Go to Home Page </a>");
				out.println("</body>");
				out.println("</html>");
				out.close();
			}
			else
			{
				response.setContentType("text/html");
				java.io.PrintWriter out = response.getWriter();
				out.println("<html>");
				Utilities util = new Utilities(request,out);
		
				util.getDefaultTemplate("header.html");
				out.println("<h2>Order UnSuccessful.</h2>");
				out.println("<h2>" + "Order not placed." + "</h2>");
				out.println("<br/>");
				out.println("<a href='hubCart'> Go to cart </a>");
				out.println("</body>");
				out.println("</html>");
				out.close();
			}

		}		
		catch(Exception ex)
		{
			
		}
	}
}