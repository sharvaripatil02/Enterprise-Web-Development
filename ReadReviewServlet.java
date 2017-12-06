import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ReadReviewServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		Utilities util = new Utilities(request,out);
		util.getDefaultTemplate("header.html");
		List<Object>collectPreviousReview=new ArrayList<>();
		
		MongoDBDataStoreUtilities mongoDBDataStoreUtilities= new MongoDBDataStoreUtilities();
        int reviewCount=0;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		collectPreviousReview=mongoDBDataStoreUtilities.readReview();   
		for(int i=0;i<5;i++){  
			String html = "";
			String category = "";
			if (i==0){
				category="Action";
			}
			else if (i==1){
				category="Animation";
			}
			else if (i==2){
				category="Classic";
			}
			else if (i==3){
				category="Comedy";
			}
			else if (i==4){
				category="Horror";
			}
			
			html="<div class=\"container\"><div><table class=\"table well\" align=\"center\"><p style=\"text-align:center;\"><b>Feedbacks based on "+category+" category</p></b><thead><tr><th style=\"padding: 5px 10px;\">Movie Name</th><th style=\"padding: 5px 10px;\">Movie Category</th><th style=\"padding: 5px 10px;\">Ratings</th><th style=\"padding: 5px 10px;\">Comments</th><th style=\"padding: 5px 10px;\">City</th><th style=\"padding: 5px 10px;\">Date</th></tr></thead><tbody>";
			
			if(collectPreviousReview.size()>0){
				
				for(Object iteratorObject:collectPreviousReview){
					Map<String,Object> reviewStoreMap=new HashMap<String,Object>();	
					reviewStoreMap=(Map<String,Object>)iteratorObject;
					if(reviewStoreMap.get("MovieCategory").equals(category)){
						html+="<tr><td>"+String.valueOf(reviewStoreMap.get("MovieName"))+"</td>";
						html+="<td>"+String.valueOf(reviewStoreMap.get("MovieCategory"))+"</td>";
						html+="<td>"+String.valueOf(reviewStoreMap.get("Rating"))+"</td>";
						html+="<td>"+String.valueOf(reviewStoreMap.get("Comments"))+"</td>";
						html+="<td>"+String.valueOf(reviewStoreMap.get("City"))+"</td>";
						html+="<td>"+String.valueOf(reviewStoreMap.get("Date"))+"</td>";
						html+="</tr>";
						reviewCount++;
					}
				}
			}
			html+="</tbody><tfoot style=\"background: SeaGreen;color: white;text-align: right;\"><tr><th style=\"padding: 5px 10px;\" colspan=\"6\">Total Feedback</th><th>"+String.valueOf(reviewCount)+"</th></tr></tfoot></table></div></div><br/>";
			reviewCount=0;
			out.println(html);
		}
	}
}
