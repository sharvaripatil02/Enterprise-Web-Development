import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.*;
import java.sql.*;
import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

public class WriteFeedbackServlet extends HttpServlet 
{
	public HashMap<String, MovieCatalog> hm = null;
	Feedback fb = null;	
	MovieCatalog pc = null;	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try 
		{

			PrintWriter out = response.getWriter();
			Utilities util = new Utilities(request,out);

			String movieId = request.getParameter("movieid");

			hm = WfCache.getAllMovies();
			MovieCatalog mv = hm.get(movieId);	

			util.getDefaultTemplate("header.html");
			util.getDefaultTemplate("leftNavigation.html");		
			util.getWriteFeebackTemplate(mv, mv.getCategory());		
			util.getDefaultTemplate("footer.html");	
		}

			catch(SQLException ex)
		{
			System.out.println(ex.toString());
		}	
		
			
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException 
	{
		try 
		{
			fb = new Feedback();

						
			
			fb.setMovieName(request.getParameter("mname"));
			fb.setMovieCategory(request.getParameter("mcategory"));
			fb.setMoviePrice(request.getParameter("mprice"));
			fb.setMovieDescription(request.getParameter("mdescription"));
			fb.setMovieDirector(request.getParameter("mdirector"));
			fb.setMovieRating(request.getParameter("mrating"));
			fb.setUserID(request.getParameter("username"));
			fb.setUserAge(request.getParameter("userage"));
			fb.setUserGender(request.getParameter("usergender"));
			fb.setUserCity(request.getParameter("mcity"));
			fb.setComment(request.getParameter("mcomments"));
			fb.setDate(request.getParameter("feedbackdate"));

			
			MongoDBDataStoreUtilities.insertReview(fb);
						
			response.setContentType("text/html");
			java.io.PrintWriter out = response.getWriter();
			Utilities util = new Utilities(request,out);
		
			util.getDefaultTemplate("header.html");
			out.println("<h2>" + "Feedback for " + request.getParameter("mname") + " Submitted Successfully." + "</h2>");
			out.println("<br/>");
			out.println("<a href='hubHome'> Home Page </a>");			
			out.println("</body>");
			out.println("</html>");
			out.close();
		}		
		catch(Exception ex)
		{
			System.out.println(ex.toString());
		}
	}
}