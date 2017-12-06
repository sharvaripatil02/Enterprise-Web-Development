import java.io.*;
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

public class WfCache
{
	public static HashMap<String, MovieCatalog> allMovieCatalog = null;
	public static HashMap<String, MovieCatalog> allCategoryMovie = null;


	public static HashMap<String, MovieCatalog> cartData = null;
	public static HashMap<String, User> wfUserData = null;

	public static int cartCount = 0;
	public static int currentOrderNo = 0;
	public static String subscriptionTime = null;	

	public static String username = null;
	public static String role = "guest";

	public static boolean isNewApp = true;
	
	
	public WfCache()
	{
		
	}

	public static HashMap<String, MovieCatalog> isAllMOvieFetched()throws IOException, SQLException
	{		
		System.out.println("***Fetching All Movies ***");

		allMovieCatalog = SAXInterface(Constants.ALLMOVIES_XML);

		MySqlDataStoreUtilities objSql = new MySqlDataStoreUtilities();	
			for (String key : allMovieCatalog.keySet()) 
			{
				MovieCatalog pc = allMovieCatalog.get(key);				
				objSql.insertMovieDetails(pc);				
			}

		HashMap<String, MovieCatalog> mtc  = getAllMovies();
		return mtc;
	}
	
	public static HashMap<String, MovieCatalog> getAllMovies()throws IOException, SQLException
	{
		MySqlDataStoreUtilities sqlData = new MySqlDataStoreUtilities();			
		HashMap<String, MovieCatalog> allMovieCatalog  = sqlData.selectAllMovies();

		return allMovieCatalog;
	}

	public static HashMap<String, MovieCatalog> getCategoryMovies(String category)throws IOException, SQLException
	{
		MySqlDataStoreUtilities sqlData = new MySqlDataStoreUtilities();			
		HashMap<String, MovieCatalog> allCategoryMovie  = sqlData.selectCategoryMovie(category);

		return allCategoryMovie;
	}
	
	public static void deleteMovies(String movieId)throws IOException, SQLException
	{		
		MySqlDataStoreUtilities sqlData = new MySqlDataStoreUtilities();			
		sqlData.deleteAllMovies(movieId);
	}

	public static void resetDB()throws IOException, SQLException
	{		
		MySqlDataStoreUtilities sqlData = new MySqlDataStoreUtilities();			
		sqlData.deleteAllMovies(null);
	}

	public static void refreshSubscriptionStatus()throws IOException, SQLException
	{		
		MySqlDataStoreUtilities sqlData = new MySqlDataStoreUtilities();			
		sqlData.updateAllSubscriptionDetails();
	}

	public static void removeSubscriptionByAdmin(int subscriptionTableId)throws IOException, SQLException
	{		
		MySqlDataStoreUtilities sqlData = new MySqlDataStoreUtilities();			
		sqlData.removeSubscriptionByAdmin(subscriptionTableId);
	}

	public static void addToCart(MovieCatalog mvObj)
	{
		if(cartData == null)
		{
			cartData = new HashMap<String, MovieCatalog>();			
		}
		cartData.put(mvObj.getId(), mvObj);
	}
	
	public static HashMap<String, MovieCatalog> showCart()
	{		
		return cartData;
	}
	
	public static HashMap<String, MovieCatalog> deleteCart(String ID)
	{
		if(cartData != null)
		{			
			cartData.remove(ID);			
		}
		return cartData;
	}	
	
	public static void clearCart()
	{
		if(cartData != null)
		{			
			cartData.clear();			
		}
	}

	public static HashMap<String, User> UserDetails() throws SQLException {

		if (wfUserData == null) {
			
			MySqlDataStoreUtilities objSql = new MySqlDataStoreUtilities();
			wfUserData = objSql.getAllUserDetails();
		}
		return wfUserData;
	}
	
	public static HashMap<String, MovieCatalog> SAXInterface(String filePath)throws IOException
	{
		SAXProductHandler saxHandler = new SAXProductHandler();
		HashMap<String, MovieCatalog> mveMap = new HashMap<String, MovieCatalog>();
		try
		{
			mveMap = saxHandler.readDataFromXML(filePath);
		}
		catch(SAXException e)
		{
			System.out.println("Error - SAXException");
		}
		catch(ParserConfigurationException e)
		{
			System.out.println("Error - ParserConfigurationException");
		}
		return mveMap;
	}
}

