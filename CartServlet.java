import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.*;
import java.sql.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;


public class CartServlet extends HttpServlet 
{
	public HashMap<String, MovieCatalog> hm = null;
	MovieCatalog pc = null;
	MovieCatalog pc1 = null;
	public HashMap<String, MovieCatalog> hm1 = null;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		try 
		{

		PrintWriter out = response.getWriter();

		Utilities util = new Utilities(request,out);
		
		util.getDefaultTemplate("header.html");
		util.getDefaultTemplate("leftNavigation.html");		
		util.getCartTemplate();		
		util.getDefaultTemplate("footer.html");	

		}catch(SQLException e){
				System.out.println("Utilities SQL EXCEPTION");
			}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException 
	{
		try 
		{
			String movieID = request.getParameter("movieid");

			hm = WfCache.deleteCart(movieID);
					
			WfCache.cartCount = WfCache.cartCount - 1;					
			
			response.sendRedirect("hubCart");
		}		
		catch(Exception ex)
		{
			
		}
	}	
}