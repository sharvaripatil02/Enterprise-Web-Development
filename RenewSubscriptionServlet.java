import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.*;
import java.sql.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import java.util.Date;

public class RenewSubscriptionServlet extends HttpServlet 
{
	public HashMap<String, MovieCatalog> mveHm = null;
	
	MovieCatalog mvc = null;
	String username = null;

	public HashMap<String, MovieCatalog> hm = null;
	MovieCatalog pc = null;
	MovieCatalog pc1 = null;
	public HashMap<String, MovieCatalog> hm1 = null;

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException 
	{
		try 
		{			
			String movieID = request.getParameter("movieid");

			System.out.println("hey in renew subscription");

			if(Servlet_UpdateCart(movieID))
			{			
				WfCache.cartCount = WfCache.cartCount + 1;									
			}			
			response.sendRedirect("hubWatchSubscription");
		}		
		catch(Exception ex)
		{
			
		}
	}

	public boolean Servlet_UpdateCart(String movieID)throws IOException , SQLException
	{
		hm = WfCache.getAllMovies();
		pc = hm.get(movieID);
		
		hm1 = WfCache.showCart();
		
		if(hm1 != null)
		{
			if(hm1.containsKey(movieID))
			{
				pc1 = hm1.get(movieID);
				if(pc1 != null)
				{
					// int qty = pc1.getQuantity() + 1;
					// pc1.setQuantity(qty);
					WfCache.addToCart(pc1);
					return false;
				}
			}
			else
			{
				// int qty = 1;
				// pc.setQuantity(qty);
				WfCache.addToCart(pc);								
			}
		}
		else
		{
			// int qty = 1;
			// pc.setQuantity(qty);
			WfCache.addToCart(pc);			
		}
		return true;
	}
}