import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.*;
import java.sql.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class DataVisualizationServlet extends HttpServlet 
{	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		try 
		{
			PrintWriter out = response.getWriter();

			Utilities util = new Utilities(request,out);

			MySqlDataStoreUtilities sqlData  = new MySqlDataStoreUtilities();
			ArrayList<MovieCatalog> arr1 = null;
			ArrayList<ChartDataset> arr2 = null;
			String jsonString = null;
			

			String report = request.getParameter("chartData");
			System.out.println("check chart data ####"+ report);

			if(report.equalsIgnoreCase("inventory")) {

			}
			else if (report.equalsIgnoreCase("sales")) {

				arr2 = new ArrayList<ChartDataset>();

				arr1 = sqlData.moviesSubscribed();

				for (MovieCatalog pc : arr1) {

					ChartDataset cds = new ChartDataset();

					cds.setName(pc.getName());
					cds.setPrice(pc.getDiscount());

					arr2.add(cds);
				}
				Gson gson = new Gson();
  				jsonString = gson.toJson(arr2);
			}

			

  			for (ChartDataset pc : arr2) {

					System.out.println("JSON DATA ##$$##" + pc.getName());
				}

			System.out.println("JSON DATA ##$$##" + jsonString);

			
			util.getDefaultTemplate("header.html");		
			util.getChartTemplate(jsonString, report);	

		}catch(Exception e){
				 e.printStackTrace();
			}
	}
}