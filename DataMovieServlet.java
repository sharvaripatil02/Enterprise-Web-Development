import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.text.*;
import com.mongodb.*;
import java.util.Map;
import java.util.Map.Entry;
import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.model.*;

public class DataMovieServlet extends HttpServlet {
	String htmlString = "";
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		int counter=0, counter2=0;
			PrintWriter out = response.getWriter();
			Utilities util = new Utilities(request,out);
			String htmlString = "";
			util.getDefaultTemplate("header.html");
			util.getDefaultTemplate("leftNavigation.html");	
		try{
			MongoDBDataStoreUtilities mongo= new MongoDBDataStoreUtilities();
			AggregationOutput agg2=mongo.movieTrend();
			htmlString+="<div><table cellpadding=\"10\" align=\"center\"><p style=\"text-align:center;\"><b>Highest number of feedbacks based on movies</p></b>";
			htmlString+="<th style=\"border: 3px solid black;text-align:center;\">Movie Name</th><th style=\"border: 3px solid black;text-align:center;\">Number of feedbacks</th>";
			for (DBObject result : agg2.results()) {
				BasicDBObject bobj= (BasicDBObject) result;
				htmlString += "<tr><td style=\"border: 3px solid black;text-align:center;\"> "+bobj.getString("value")+"</td>"
				+"<td style=\"border: 3px solid black;text-align:center;\">"+bobj.getString("ReviewValue")+"</td></tr>";
				
			}
			
			htmlString+="</table></div><br/>";
			out.println(htmlString);
			//util.getDefaultTemplate("footer.html");	
			out.close();
		}
		
		catch(Exception e){
			System.err.println(e);
		}
		
	}
	
}	