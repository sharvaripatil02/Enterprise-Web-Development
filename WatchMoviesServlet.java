import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.*;
import java.sql.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import java.util.Date;

public class WatchMoviesServlet extends HttpServlet 
{
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		try {

			PrintWriter out = response.getWriter();

			Utilities util = new Utilities(request,out);

			String movieID = request.getParameter("movieid");

			HashMap<String, MovieCatalog> hmMveCatalog = new HashMap<String, MovieCatalog>();
			hmMveCatalog = WfCache.getAllMovies();

			MovieCatalog mc = new MovieCatalog();
			mc = hmMveCatalog.get(movieID);

			String movieUrl = mc.getVideo();
			
			util.getDefaultTemplate("header.html");
			util.getWatchMoviesTemplate(movieUrl);		
			util.getDefaultTemplate("footer.html");	
		
		}catch(SQLException e){
				System.out.println("Utilities SQL EXCEPTION");
			}	
	}
}
	
	