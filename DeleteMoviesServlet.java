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



public class DeleteMoviesServlet extends HttpServlet
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

			String selectedCategory = request.getParameter("movieCategory");

			HashMap<String, MovieCatalog> categoryMoviesHm = WfCache.getCategoryMovies(selectedCategory);

			util.getDefaultTemplate("header.html");
			util.getDefaultTemplate("leftNavigation.html");		
			util.getDeleteMoviesTemplate(categoryMoviesHm, selectedCategory);		
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
			MySqlDataStoreUtilities sqlData = new MySqlDataStoreUtilities();

			PrintWriter out = response.getWriter();	
			MovieCatalog pc = new MovieCatalog();

			WfCache.allMovieCatalog.remove(movieId);
			filePath = Constants.ALLMOVIES_XML;
			hm_prod = WfCache.allMovieCatalog;

			WriteXML();			

			WfCache.deleteMovies(movieId);

			out.println("<html>");
			out.println("<head>");
			out.println("<title>Movie Delete</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h2>" + "Movie Deleted Successfully." + "</h2>");
			out.println("<br/>");
			out.println("<a href='hubHome'> Home Page </a>");			
			out.println("</body>");
			out.println("</html>");	

		}catch(Exception ex)
		{

		}finally{}
	}

	public void WriteXML() throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException
	{
		DocumentBuilder builder = null;
		builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = builder.newDocument();

		Element root = document.createElement("ProductCatalog");

		for(String key : hm_prod.keySet())
		{				 
			MovieCatalog value = hm_prod.get(key);				

			Element newNode = document.createElement("Movies");


			Element nodeID = document.createElement("id");					
			Element nodeImage = document.createElement("image");
			Element nodeName = document.createElement("name");
			Element nodePrice = document.createElement("price");
			Element nodeDirector = document.createElement("director");
			Element nodeStars = document.createElement("stars");
			Element nodeDescription = document.createElement("description");
			Element nodeRating = document.createElement("rating");
			Element nodeOnsale = document.createElement("onsale");
			Element nodeDiscount = document.createElement("discount");
			Element nodeCategory = document.createElement("category");
			Element nodeVideo = document.createElement("video");
			
			
			nodeID.setTextContent(value.getId());
			nodeName.setTextContent(value.getName());
			nodePrice.setTextContent(Float.toString(value.getPrice()));
			nodeImage.setTextContent(value.getImage());
			nodeDirector.setTextContent(value.getDirector());
			nodeStars.setTextContent(value.getCast());
			nodeDescription.setTextContent((value.getDescription()));
			nodeRating.setTextContent(Integer.toString(value.getRating()));		
			nodeOnsale.setTextContent(value.getOnSale());
			nodeDiscount.setTextContent(Float.toString(value.getDiscount()));
			nodeCategory.setTextContent(value.getCategory());
			nodeVideo.setTextContent((value.getVideo()));

			newNode.appendChild(nodeID);
			newNode.appendChild(nodeImage);
			newNode.appendChild(nodeName);
			newNode.appendChild(nodePrice);
			newNode.appendChild(nodeDirector);
			newNode.appendChild(nodeStars);
			newNode.appendChild(nodeDescription);
			newNode.appendChild(nodeRating);
			newNode.appendChild(nodeDiscount);
			newNode.appendChild(nodeOnsale);
			newNode.appendChild(nodeCategory);
			newNode.appendChild(nodeVideo);
			

			root.appendChild(newNode);

		}

		document.appendChild(root);

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		Source source = new DOMSource(document);
		File file = new File(filePath);
		Result result = new StreamResult(file);
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		transformer.transform(source, result);				
	}

	


}