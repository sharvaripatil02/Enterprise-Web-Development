import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.*;
import javax.xml.*;
import java.io.File;
import java.util.HashMap;	
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.IOException;
import javax.xml.*;
import java.sql.*;


public class UpdateMovieServlet extends HttpServlet
{
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{		
		try
		{
			PrintWriter out = response.getWriter();

			Utilities util = new Utilities(request,out);

			String selectedCategory = request.getParameter("movieCategory");

			HashMap<String, MovieCatalog> categoryMoviesHm = WfCache.getCategoryMovies(selectedCategory);

			util.getDefaultTemplate("header.html");
			util.getDefaultTemplate("leftNavigation.html");		
			util.getUpdateMovieTemplate(categoryMoviesHm, selectedCategory);		
			util.getDefaultTemplate("footer.html");
		}
		catch(Exception ex)
		{

		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException
	{
		try
		{
			String movieId = request.getParameter("productid");

			PrintWriter out = response.getWriter();

			Utilities util = new Utilities(request,out);

			util.getDefaultTemplate("header.html");
			util.getDefaultTemplate("leftNavigation.html");		
			util.getUpdateMoviesDetailsTemplate(movieId);		
			util.getDefaultTemplate("footer.html");
		}

		catch(Exception ex)
		{

		}
	}
}