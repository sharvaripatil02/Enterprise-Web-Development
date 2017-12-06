import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.*;
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
import java.io.File;
import java.util.HashMap;	
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
import java.sql.*;


public class AddMoviesServlet extends HttpServlet
{
	public String movieCategory;
	public String filePath;

	public static HashMap<String, MovieCatalog> hm_prod = new HashMap<String, MovieCatalog>();
	MovieCatalog objProductCatalog = new MovieCatalog();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();

		Utilities util = new Utilities(request,out);

		util.getDefaultTemplate("header.html");
		util.getDefaultTemplate("leftNavigation.html");		
		util.getAddMoviesTemplate();		
		util.getDefaultTemplate("footer.html");	
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException
	{
		try
		{
			response.setContentType("text/html");

			PrintWriter out = response.getWriter();				
			MySqlDataStoreUtilities objSql = new MySqlDataStoreUtilities();	

			String uniqueID = UUID.randomUUID().toString();
			String imagePath = "images/loading.gif";
			String videoSample = "https://www.youtube.com/embed/ryUxrFUk6MY";	

			System.out.println("random number **********" + uniqueID);

			objProductCatalog.setId(uniqueID);
			objProductCatalog.setName(request.getParameter("movieName"));
			objProductCatalog.setImage(imagePath);
			objProductCatalog.setPrice(Float.valueOf(request.getParameter("moviePrice")));
			objProductCatalog.setDirector(request.getParameter("movieDirector"));
			objProductCatalog.setDescription(request.getParameter("movieDescription"));
			objProductCatalog.setDiscount(Float.valueOf(request.getParameter("movieDiscount")));
			objProductCatalog.setOnSale(request.getParameter("movieOnsale"));
			objProductCatalog.setVideo(videoSample);
			objProductCatalog.setCast(request.getParameter("movieStar"));
			objProductCatalog.setRating(Integer.parseInt(request.getParameter("movieRating")));


			movieCategory = request.getParameter("movie_Category");
			objProductCatalog.setCategory(movieCategory);

			WfCache.allMovieCatalog.put(objProductCatalog.getId(),objProductCatalog);
			objSql.insertMovieDetails(objProductCatalog);
			filePath = Constants.ALLMOVIES_XML;

			
			Utilities util = new Utilities(request,out);

			util.getDefaultTemplate("header.html");

			SAXProductHandler saxHandler = new SAXProductHandler();
			try
			{
				hm_prod = saxHandler.readDataFromXML(filePath);
				WriteXML();	
			}
			catch(SAXException e)
			{
				
			}
			catch(ParserConfigurationException e)
			{
				
			}
			catch(TransformerException e)
			{
				
			}

			out.println(
				"<html>"+
				"<body>"+
				"<h3>Movie is successfully added to the System.</a></h3>"+
				"<a href='hubHome'> Home Page </a>"
				);
			util.getDefaultTemplate("footer.html");
		}

		catch(SQLException ex)
		{

		}

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

		nodeID.setTextContent(objProductCatalog.getId());
		nodeName.setTextContent(objProductCatalog.getName());
		nodePrice.setTextContent(Float.toString(objProductCatalog.getPrice()));
		nodeImage.setTextContent(objProductCatalog.getImage());
		nodeDirector.setTextContent(objProductCatalog.getDirector());
		nodeStars.setTextContent(objProductCatalog.getCast());
		nodeDescription.setTextContent((objProductCatalog.getDescription()));
		nodeRating.setTextContent(Integer.toString(objProductCatalog.getRating()));		
		nodeOnsale.setTextContent(objProductCatalog.getOnSale());
		nodeDiscount.setTextContent(Float.toString(objProductCatalog.getDiscount()));
		nodeCategory.setTextContent(objProductCatalog.getCategory());
		nodeVideo.setTextContent((objProductCatalog.getVideo()));
		
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