import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.*;

public class MySqlDataStoreUtilities
{
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 

	Connection conn = null;
	PreparedStatement pst = null;
	ResultSet rs = null;
	ResultSet rs1 = null;
	Statement stmt = null;
	Statement stmt1 = null;

	HashMap<String, User> hmUsers = null;
	HashMap<String, MovieCatalog> hmMveCatalog = new HashMap<String, MovieCatalog>();	

	MovieCatalog pc = null;
	
	
	public MySqlDataStoreUtilities() {}
	
	public HashMap<String, User> getAllUserDetails() throws SQLException 
	{
		try {

			hmUsers = new HashMap<String, User>();
			User user = null;

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movieHub", "root", "root");
			stmt = conn.createStatement();

			String query = "SELECT * FROM UserDetail";
			rs = stmt.executeQuery(query);

			while (rs.next()) {

				user = new User();

				user.setUserId(rs.getInt("userId"));
				user.setFirstName(rs.getString("firstname"));
				user.setLastName(rs.getString("lastname"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("userPassword"));
				user.setPhone(rs.getString("phone"));
				user.setRole(rs.getString("userRole"));
				user.setAddress(rs.getString("address"));
				user.setCity(rs.getString("city"));
				user.setCountry(rs.getString("country"));
				user.setState(rs.getString("state"));
				user.setZipcode(rs.getInt("zipcode"));

				hmUsers.put(user.getUsername(), user);
			}

			rs.close();
			stmt.close();
			conn.close();

		} catch (SQLException se) { // Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) { // Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {

			}

			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return hmUsers;
	}
	
	public boolean insertUserDetails(User user) throws SQLException 
	{
		try {

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movieHub", "root", "root");

			String query = "INSERT INTO UserDetail(firstname, lastname, username, userPassword, userRole, phone, address, city, state, country, zipcode) "
								+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			pst = conn.prepareStatement(query);

			pst.setString(4, user.getPassword());
			pst.setString(3, user.getUsername());
			pst.setString(1, user.getFirstName());
			pst.setString(2, user.getLastName());
			pst.setString(5, user.getRole());
			pst.setString(6, user.getPhone());
			pst.setString(7, user.getAddress());
			pst.setString(8, user.getCity());
			pst.setString(9, user.getState());
			pst.setString(10, user.getCountry());
			pst.setString(11, Integer.toString(user.getZipcode()));

			int result = pst.executeUpdate();

			pst.close();
			conn.close();

			if (result > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException se) { // Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) { // Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			try {
				if (pst != null)
					pst.close();
			} catch (SQLException se2) {

			}

			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return false;
	}


	public boolean insertSubscriptionDetails(HashMap<String, MovieCatalog> orderDetails, String creditcard, String userName, String userAddress)throws SQLException
	{
		try
		{
			int newOrderNo = getLastOrderNo(userName) + 1;

			Date startDate = new Date();
			System.out.println(dateFormat.format(startDate));

			Calendar c = Calendar.getInstance();
        	c.setTime(startDate);

        	if(WfCache.role.equalsIgnoreCase("customer")) {

        		c.add(Calendar.DATE, 14);

        	} else if (WfCache.role.equalsIgnoreCase("guest")) {

        		c.add(Calendar.DATE, 1);

        	}
			
			Date endDate = c.getTime();
        	System.out.println(dateFormat.format(endDate));
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movieHub","root","root");	
			stmt1 = conn.createStatement();		
			
			pc = new MovieCatalog();

			for(String key: orderDetails.keySet()) {

				MovieCatalog mv = new MovieCatalog();
				mv = orderDetails.get(key);

				String query1 = "SELECT * FROM Subscriptions WHERE username = '"+userName+"'and movieId = '"+mv.getId()+"';";
				rs1 = stmt1.executeQuery(query1);

				while(rs1.next()) {
					updateUserSubscription(rs1.getString("movieId"));
				}
			}

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movieHub","root","root");	

			
			String query = "INSERT INTO Subscriptions(subscriptionId, movieId, movieName, username, creditcard, userAddress, moviePrice, startDate, endDate, subscriptionStatus) "
									+ "VALUES (?,?,?,?,?,?,?,?,?,?);";
			
			if(orderDetails != null)
			{
				for(String key: orderDetails.keySet())
				{
					pc = orderDetails.get(key);
										
					pst = conn.prepareStatement(query);

					pst.setInt(1,newOrderNo);
					pst.setString(2,pc.getId());
					pst.setString(3,pc.getName());					
					pst.setString(4,userName);
					pst.setString(5,creditcard);
					pst.setString(6,userAddress);
					pst.setFloat(7,pc.getPrice());
					pst.setString(8,dateFormat.format(startDate));				
					pst.setString(9,dateFormat.format(endDate));
					pst.setString(10, "Active");
					
					pst.execute();

					WfCache.currentOrderNo = newOrderNo;
				}
				WfCache.subscriptionTime = dateFormat.format(startDate);
			}
		}
		catch(Exception ex)
		{
			System.out.println("insertOrderDetails()- " );
			ex.printStackTrace();
			return false;
		}
		finally
		{			
			pst.close();
			conn.close();
		}
		return true;
	}
	
	public HashMap<Integer, Subscription> fetchUserSubscriptionDetails(String username)throws SQLException
	{
		HashMap<Integer, Subscription> userSubscriptionDetails = new HashMap<Integer, Subscription>();
		try
		{			
			System.out.println(username);
			Subscription subscribe = null;
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movieHub","root","root");
			stmt = conn.createStatement();

			if(WfCache.role.equalsIgnoreCase("guest")) {
				String query = "SELECT * FROM Subscriptions;";
				rs = stmt.executeQuery(query);

			} else {
				String query = "SELECT * FROM Subscriptions WHERE username = '"+username+"';";
				rs = stmt.executeQuery(query);
			}

			
						
						
			while(rs.next())
			{
				subscribe = new Subscription();
				
				subscribe.setSubscriptionId(rs.getInt("subscriptionId"));
				subscribe.setMovieId(rs.getString("movieId"));
				subscribe.setMovieName(rs.getString("movieName"));
				subscribe.setUsername(rs.getString("username"));
				subscribe.setMoviePrice(rs.getFloat("moviePrice"));
				subscribe.setCreditcard(rs.getString("creditcard"));
				subscribe.setUserAddress(rs.getString("userAddress"));
				subscribe.setStartDate(dateFormat.parse(rs.getString("startDate")));
				subscribe.setEndDate(dateFormat.parse(rs.getString("endDate")));
				subscribe.setSubscriptionStatus(rs.getString("subscriptionStatus"));
				
				
				userSubscriptionDetails.put(rs.getInt("Id"), subscribe);
			}

			System.out.println("details length" + userSubscriptionDetails.size());
			
			rs.close();
			stmt.close();
			conn.close();
		}
		catch(Exception ex)
		{
			System.out.println("fetchOrderDetails()- " + ex.toString());			
		}
		finally
		{			
			rs.close();
			stmt.close();
			conn.close();
		}
		return userSubscriptionDetails;
	}
	
	
	public int getLastOrderNo(String username)throws SQLException
	{
		int lastOrderNo = 0;

		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movieHub","root","root");
			
			String selectQuery = "SELECT MAX(subscriptionId) as subscriptionId FROM Subscriptions where username='"+ username +"'";
			
			pst = conn.prepareStatement(selectQuery);
			rs = pst.executeQuery();
				
			if(!rs.next())
			{
				return lastOrderNo;
			}
			else
			{
				do
				{
					lastOrderNo = rs.getInt("subscriptionId");
				}while(rs.next());
			}		
		}
		catch(Exception ex)
		{
			System.out.println("getLastOrderNo()- " + ex.toString());
			return lastOrderNo;
		}
		finally
		{
			rs.close();
			pst.close();
			conn.close();
		}
		return lastOrderNo;
	}

	public void updateAllSubscriptionDetails() throws SQLException 
	{
		try {

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movieHub", "root", "root");
			stmt = conn.createStatement();

			String query = "UPDATE Subscriptions SET subscriptionStatus = 'Expired' WHERE endDate < CURRENT_TIMESTAMP;";
			
			pst = conn.prepareStatement(query);

			pst.executeUpdate();
		}
		catch(Exception ex)
		{
			System.out.println("updateOrderDetails()- " + ex.toString());
		}
		finally
		{			
			pst.close();
			conn.close();
		}
		
	}

	public void removeSubscriptionByAdmin(int subscriptionTableId) throws SQLException 
	{
		try {

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movieHub", "root", "root");
			stmt = conn.createStatement();

			String query = "UPDATE Subscriptions SET subscriptionStatus = 'Removed by Admin' WHERE id = "+subscriptionTableId+";";
			
			pst = conn.prepareStatement(query);

			pst.executeUpdate();
		}
		catch(Exception ex)
		{
			System.out.println("updateOrderDetails()- " + ex.toString());
		}
		finally
		{			
			pst.close();
			conn.close();
		}
		
	}

	public void updateUserSubscription(String movieId) throws SQLException 
	{
		try {

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movieHub", "root", "root");
			stmt = conn.createStatement();

			String deleteQuery = "DELETE from Subscriptions where movieId = '"+ movieId +"';";
			System.out.println(deleteQuery);
					
			stmt.executeUpdate(deleteQuery);
						
			stmt.close();
			conn.close();
		}
		catch(Exception ex)
		{
			System.out.println("updateOrderDetails()- " + ex.toString());
		}
		finally
		{			
			pst.close();
			conn.close();
		}
		
	}
	
		public void deleteAllMovies(String movieId) throws SQLException {
		try 
		{			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movieHub", "root", "root");
			stmt = conn.createStatement();
			
			String deleteQuery;


			
			if(movieId == null)
			{
				deleteQuery = "DELETE from Movies";	
			}
			else
			{
				deleteQuery = "DELETE from Movies where movieId = '"+ movieId +"';";
				System.out.println(deleteQuery);
			}
					
			stmt.executeUpdate(deleteQuery);
						
			stmt.close();
			conn.close();
		} catch (SQLException se) { // Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) { // Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}

			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	
	public boolean insertMovieDetails(MovieCatalog mvc) throws SQLException 
	{
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movieHub", "root", "root");

			String insertQuery = "INSERT INTO Movies(movieId, movieImage, movieName, moviePrice, movieDirector, movieStar, movieDescription, movieDiscount, movieOnsale, movieCategory, movieVideo, movieRating) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";

			pst = conn.prepareStatement(insertQuery);

			pst.setString(1, mvc.getId());
			pst.setString(2, mvc.getImage());
			pst.setString(3, mvc.getName());
			pst.setFloat(4, mvc.getPrice());
			pst.setString(5, mvc.getDirector());
			pst.setString(6, mvc.getCast());
			pst.setString(7, mvc.getDescription());
			pst.setFloat(8, mvc.getDiscount());
			pst.setString(9, mvc.getOnSale());
			pst.setString(10, mvc.getCategory());
			pst.setString(11, mvc.getVideo());
			pst.setInt(12, mvc.getRating());

			int result = pst.executeUpdate();
			pst.close();
			conn.close();

			if (result > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException se) { // Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) { // Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			try {
				if (pst != null)
					pst.close();
			} catch (SQLException se2) {

			}

			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return false;
	}

	

	public HashMap<String, MovieCatalog> selectAllMovies() throws SQLException 
	{
		try {

			hmMveCatalog = new HashMap<String, MovieCatalog>();
			MovieCatalog pc = null;

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movieHub", "root", "root");
			stmt = conn.createStatement();

			String selectQuery;
			selectQuery = "SELECT * FROM Movies";

			
			rs = stmt.executeQuery(selectQuery);

			while (rs.next()) {

				pc = new MovieCatalog();

				pc.setId(rs.getString("movieId"));
				pc.setName(rs.getString("movieName"));
				pc.setImage(rs.getString("movieImage"));
				pc.setPrice(rs.getFloat("moviePrice"));
				pc.setDirector(rs.getString("movieDirector"));
				pc.setDescription(rs.getString("movieDescription"));
				pc.setDiscount(rs.getFloat("movieDiscount"));
				pc.setCategory(rs.getString("movieCategory"));
				pc.setOnSale(rs.getString("movieOnsale"));
				pc.setVideo(rs.getString("movieVideo"));
				pc.setCast(rs.getString("movieStar"));
				pc.setRating(rs.getInt("movieRating"));


				hmMveCatalog.put(pc.getId(), pc);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) { // Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) { // Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {

			}

			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return hmMveCatalog;
	}

	public HashMap<String, MovieCatalog> selectCategoryMovie(String mveCategory) throws SQLException 
	{
		try {

			hmMveCatalog = new HashMap<String, MovieCatalog>();
			MovieCatalog pc = null;

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movieHub", "root", "root");
			stmt = conn.createStatement();

			String selectQuery;
			selectQuery = "SELECT * FROM Movies WHERE movieCategory = '" + mveCategory + "';";

			
			rs = stmt.executeQuery(selectQuery);

			while (rs.next()) {

				pc = new MovieCatalog();

				pc.setId(rs.getString("movieId"));
				pc.setName(rs.getString("movieName"));
				pc.setImage(rs.getString("movieImage"));
				pc.setPrice(rs.getFloat("moviePrice"));
				pc.setDirector(rs.getString("movieDirector"));
				pc.setDescription(rs.getString("movieDescription"));
				pc.setDiscount(rs.getFloat("movieDiscount"));
				pc.setCategory(rs.getString("movieCategory"));
				pc.setOnSale(rs.getString("movieOnsale"));
				pc.setVideo(rs.getString("movieVideo"));
				pc.setCast(rs.getString("movieStar"));
				pc.setRating(rs.getInt("movieRating"));


				hmMveCatalog.put(pc.getId(), pc);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) { // Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) { // Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {

			}

			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return hmMveCatalog;
	}

	

	public HashMap<String, String> dailySales() throws SQLException 
	{
		HashMap<String, String> hmTrendData = new HashMap<String, String>();

		try 
		{			

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movieHub", "root", "root");
			stmt = conn.createStatement();

			String selectQuery;
			selectQuery = "SELECT STR_TO_DATE(startDate, '%Y/%m/%d') as Dates, SUM(moviePrice) as TotalSales FROM Subscriptions GROUP BY STR_TO_DATE(startDate, '%Y/%m/%d');";

			
			rs = stmt.executeQuery(selectQuery);

			while (rs.next()) {

				hmTrendData.put(rs.getString("Dates"), rs.getString("TotalSales"));
			}

			rs.close();
			stmt.close();
			conn.close();

		} catch (SQLException se) { // Handle errors for JDBC
			se.printStackTrace();

		} catch (Exception e) { // Handle errors for Class.forName
			e.printStackTrace();

		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {

			}

			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return hmTrendData;
	}

	public ArrayList<MovieCatalog> moviesSubscribed() throws SQLException 
	{

		ArrayList<MovieCatalog> arrayList = new ArrayList<MovieCatalog>();

		try 
		{		
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movieHub", "root", "root");
			stmt = conn.createStatement();

			String selectQuery;
			selectQuery = "SELECT subs.movieId, subs.movieName as MovieName, mov.moviePrice as SubscriptionPrice, count(subs.movieId) as CountOfSubscription, (mov.moviePrice * count(subs.movieId)) as TotalSales FROM Subscriptions as subs JOIN Movies mov ON subs.movieId = mov.movieId GROUP BY subs.movieId;";

			
			rs = stmt.executeQuery(selectQuery);

			while (rs.next()) {

				MovieCatalog prd = new MovieCatalog();

				prd.setName(rs.getString("MovieName"));
				prd.setPrice(rs.getFloat("SubscriptionPrice"));
				prd.setRating(rs.getInt("CountOfSubscription"));
				prd.setDiscount(rs.getFloat("TotalSales"));

				arrayList.add(prd);
			}

			rs.close();
			stmt.close();
			conn.close();

		} catch (SQLException se) { // Handle errors for JDBC
			se.printStackTrace();

		} catch (Exception e) { // Handle errors for Class.forName
			e.printStackTrace();

		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {

			}

			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return arrayList;
	}
	
	
	

}