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
import java.sql.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;



public class RemoveSubscriptionServlet extends HttpServlet
{
	public String movieCategory;
	public String filePath;

	public static HashMap<String, MovieCatalog> hm_prod = new HashMap<String, MovieCatalog>();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{		
		try
		{
			PrintWriter out = response.getWriter();

			Utilities util = new Utilities(request,out);

			String selectedUser = request.getParameter("selectuser");

			MySqlDataStoreUtilities sqlData = new MySqlDataStoreUtilities();
			HashMap<Integer, Subscription> hm = sqlData.fetchUserSubscriptionDetails(selectedUser);

			System.out.println("userrrrrrrrrrrrrrrrrrrr" + hm.size());

			util.getDefaultTemplate("header.html");
			util.getDefaultTemplate("leftNavigation.html");		
			util.getRemoveSubscriptionTemplate(hm, selectedUser);		
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
			int subscriptionTableId = Integer.parseInt(request.getParameter("productid"));
			

			PrintWriter out = response.getWriter();	
			MovieCatalog pc = new MovieCatalog();			

			WfCache.removeSubscriptionByAdmin(subscriptionTableId);
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Remove Subscription</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h2>" + "Subscription Removed Successfully by the Admin." + "</h2>");
			out.println("<br/>");
			out.println("<a href='hubHome'> Home Page </a>");			
			out.println("</body>");
			out.println("</html>");	

		}catch(Exception ex)
		{

		}finally{}
	}

	
	


}