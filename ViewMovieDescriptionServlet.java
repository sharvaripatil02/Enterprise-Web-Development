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

public class ViewMovieDescriptionServlet extends HttpServlet 
{
	public HashMap<String, MovieCatalog> hm = null;
	MovieCatalog pc = null;
	MovieCatalog pc1 = null;
	public HashMap<String, MovieCatalog> hm1 = null;
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try{
	
		PrintWriter out = response.getWriter();

		Utilities util = new Utilities(request,out);
		
		String movieID = request.getParameter("movieid");
		System.out.println("hfw*************" + movieID);
		String movieCategory = null;

		HashMap<String, MovieCatalog> hmMveCatalog = new HashMap<String, MovieCatalog>();
		hmMveCatalog = WfCache.getAllMovies();
		MovieCatalog mc = new MovieCatalog();
		mc = hmMveCatalog.get(movieID);

		movieCategory = mc.getCategory();


		util.getDefaultTemplate("header.html");
		util.getDefaultTemplate("leftNavigation.html");		
		util.getProductDescription(movieCategory, movieID);		
		util.getDefaultTemplate("footer.html");	

		}catch(SQLException e){
				System.out.println("Utilities SQL EXCEPTION");
			}		
	}
}