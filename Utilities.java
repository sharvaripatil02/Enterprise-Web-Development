import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Collection;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;


public class Utilities {

	private HttpServletRequest request = null;
	private PrintWriter out = null;
	private String pHtml = null;

	public String username = null;
	public String cartcount = "0";
	public String role = null;

	public  HashMap<String, MovieCatalog> movieHm = null;
	MovieCatalog mvc = null;
	private static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); 
	
	
	public Utilities(HttpServletRequest request, PrintWriter out)
	{		
		this.request = request;
		this.out = out;
	}

	public String fetchHtmlContent(String filePath)
	{
		String htmlContent = null;
		String htmlLine;		
		StringBuffer strBuffer = new StringBuffer();
		
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			while((htmlLine = br.readLine()) != null) {
				strBuffer.append(htmlLine);
			}
			htmlContent = strBuffer.toString();
		} catch (IOException e) {
			htmlContent = "Error in reading file " + filePath;
		}		
		return htmlContent;		
	}
	
	public String modifyHtml(String oldHtml, String newHtml, String toChange) {		
		oldHtml = oldHtml.replaceAll(toChange, newHtml);
		return oldHtml;
	}

	public void getDefaultTemplate(String fileName) throws IOException
	{		

		if(fileName.equalsIgnoreCase("header.html"))
		{
			StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.DEFAULT_HEADER_TEMPLATE));
			StringBuilder dynamicHtml = new StringBuilder();

			try 
			{	
				if(WfCache.isNewApp) {
					WfCache.isNewApp = false;
					WfCache.resetDB();
					WfCache.refreshSubscriptionStatus();
				}

				if(WfCache.allMovieCatalog == null) {

					movieHm = WfCache.isAllMOvieFetched();
				}

			}catch(SQLException e){
				System.out.println("Utilities SQL EXCEPTION");
			}

			
			if(WfCache.username == null) {

				dynamicHtml.append("<li><a href='hubSignUp'>Sign Up </a></li>");
				dynamicHtml.append("<li><a href='hubLogin'>Login </a></li>");	

			} 
			else if(WfCache.role.equalsIgnoreCase("customer")) {

				dynamicHtml.append("<li><a href='hubHome'><b>Welcome, " + WfCache.username.substring(0,1).toUpperCase() + WfCache.username.substring(1).toLowerCase() + "</b></a></li>");

				if(cartcount != null) {								
					dynamicHtml.append("<li><a href='hubCart'> <span><i class='glyphicon glyphicon-shopping-cart'></i></span> Cart(" +WfCache.cartCount+ ")</a></li>");			
				} 
				else {						
					dynamicHtml.append("<li><a href='hubCart'><span><i class='glyphicon glyphicon-shopping-cart'></i></span> Cart</a></li>");
				}

				dynamicHtml.append("<li><a href='hubLogout'>Logout </a></li>");

			}
			else {
				
				dynamicHtml.append("<li><a href='hubHome'><b>Welcome, " + WfCache.username.substring(0,1).toUpperCase() + WfCache.username.substring(1).toLowerCase() + "</b></a></li>");

				dynamicHtml.append("<li><a href='hubLogout'>Logout </a></li>");	
			}		

			
			pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#headerList");
			out.append(pHtml);
		}
		else if(fileName.equalsIgnoreCase("leftNavigation.html"))
		{
			StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.DEFAULT_LEFTNAVIGATION_TEMPLATE));
			StringBuilder dynamicHtml = new StringBuilder();
			
			if(WfCache.username != null)
			{
				if(WfCache.role.equalsIgnoreCase("admin"))
				{
					dynamicHtml.append("<li class='list-group-item'>");
              		dynamicHtml.append("<h4 class='mb-1 text-warning'>Data Analysis</h4>");
              		dynamicHtml.append("<a href='hubDataCategory'><small class='text-muted'><strong>Category Analysis</strong></small></a><br>");
              		dynamicHtml.append("<a href='hubDataCity'><small class='text-muted'><strong>City Analysis</strong></small></a><br>");
              		dynamicHtml.append("<a href='hubDataMovie'><small class='text-muted'><strong>Movie Analysis</strong></small></a><br>");
              		dynamicHtml.append("<a href='hubDataLeast'><small class='text-muted'><strong>Least Rated Movie Analysis</strong></small></a><br>");
            		dynamicHtml.append("</li>");
            		dynamicHtml.append("<li class='list-group-item'>");
					dynamicHtml.append("<a href='hubAddMovies'><small class='text-muted'><strong>Add Movies</strong></small></a><br>");
					dynamicHtml.append("<a href='hubUpdateMovies'><small class='text-muted'><strong>Update Movies</strong></small></a><br>");
					dynamicHtml.append("<a href='hubDeleteMovies'><small class='text-muted'><strong>Delete Movies</strong></small></a><br>");
					dynamicHtml.append("<a href='hubRemoveSubscription'><small class='text-muted'><strong>Remove User Subscription</strong></small></a><br>");
					dynamicHtml.append("<a href='hubAddcustomer'><small class='text-muted'><strong>Manage User Accounts</strong></small></a><br>");
					dynamicHtml.append("<a href='hubReadreview'><small class='text-muted'><strong>Monitor Feedbacks</strong></small></a><br>");
					dynamicHtml.append("<a href='hubSalesReport'><small class='text-muted'><strong>Generate Sales Reports</strong></small></a><br>");
					dynamicHtml.append("</li>");
				}
				else if(WfCache.role.equalsIgnoreCase("guest"))
				{
					dynamicHtml.append("<li class='list-group-item'>");
					dynamicHtml.append("<a href='hubFree'><small class='text-muted'><strong>View Free Movies</strong></small></a><br>");
					dynamicHtml.append("<a href=''><small class='text-muted'><strong>Feedback</strong></small></a><br>");
					dynamicHtml.append("</li>");
				}
				else if(WfCache.role.equalsIgnoreCase("customer"))
				{						
					dynamicHtml.append("<li class='list-group-item'>");
					dynamicHtml.append("<a href='hubWatchSubscription'><small class='text-muted'><strong>Watch Movies</strong></small></a><br>");
					
					dynamicHtml.append("<a href='hubReadreview'><small class='text-muted'><strong>Feedback</strong></small></a><br>");
					dynamicHtml.append("</li>");

				} 
				else {
					
					dynamicHtml.append("<li class='list-group-item'>");
					dynamicHtml.append("<a href='hubFree'><small class='text-muted'><strong>View Free Movies</strong></small></a><br>");
					dynamicHtml.append("<a href='hubReadreview'><small class='text-muted'><strong>Feedback</strong></small></a><br>");
					dynamicHtml.append("</li>");
				}			
			}
			else
			{
				
				dynamicHtml.append("<li class='list-group-item'>");
				dynamicHtml.append("<a href='hubFree'><small class='text-muted'><strong>View Free Movies</strong></small></a><br>");
				dynamicHtml.append("<a href='hubReadreview'><small class='text-muted'><strong>Feedback</strong></small></a><br>");
				dynamicHtml.append("</li>");
			}
			
			pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#navigationList");
			out.append(pHtml);			
		}
		else
		{					
			StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.DEFAULT_FOOTER_TEMPLATE));
			out.append(defaultTemplate.toString());
		}			
	}
	
	public void getHomeTemplate()
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.DEFAULT_CONTENT_TEMPLATE));
		StringBuilder dynamicHtml = new StringBuilder();

		dynamicHtml.append("<div align='center'>");		
		dynamicHtml.append("<h1 align='center'>Welcome to Movie Hub</h1><br>");
		dynamicHtml.append("<a href ='https://www.facebook.com/The-Hollywood-Review-107531659351795/'>");
		dynamicHtml.append("<img src = 'images/Social/facebook.JPG' width = '50' height = '50' alt = 'facebook'></a>");
		dynamicHtml.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

		dynamicHtml.append("<a href ='https://later.com/blog/instagram-year-in-review-2016/'>");
		dynamicHtml.append("<img src = 'images/Social/instagram.JPG' width = '50' height = '50' alt = 'instagram'></a>");
		dynamicHtml.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

		dynamicHtml.append("<a href ='https://twitter.com/hollywood?lang=en'>");
		dynamicHtml.append("<img src = 'images/Social/twitter.JPG' width = '50' height = '50' alt = 'twitter'></a>");
		dynamicHtml.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		dynamicHtml.append("</div>"); 
		
		
		pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#Content");
		out.append(pHtml);	
	}

	public void getFAQTemplate()
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.DEFAULT_FAQ_TEMPLATE));
		StringBuilder dynamicHtml = new StringBuilder();
		
		dynamicHtml.append("<h1 align='center'>Welcome to Movie Hub</h1><br>"); 
		
		
		pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#Content");
		out.append(pHtml);	
	}

	public void getDonationTemplate()
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.DEFAULT_DONATION_TEMPLATE));
		StringBuilder dynamicHtml = new StringBuilder();
		
		dynamicHtml.append("<h1 align='center'>Click to Donate !!</h1><br>"); 
		
		
		pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#Content");
		out.append(pHtml);	
	}

	public void getRSSTemplate()
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.DEFAULT_RSS_TEMPLATE));
		StringBuilder dynamicHtml = new StringBuilder();	
		
		pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#Content");
		out.append(pHtml);	
	}

	public void getLoginTemplate()
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_LOGIN_TEMPLATE));				
		pHtml = defaultTemplate.toString();
		out.append(pHtml);		
	}

	public void getWatchMoviesTemplate(String movieUrl)
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_WATCH_MOVIES_TEMPLATE));

		StringBuilder dynamicHtml = new StringBuilder();

		dynamicHtml.append("<iframe width='420' height='345' src='" + movieUrl + "'></iframe>");


		pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#WatchMovie");
		out.append(pHtml);		
	}

	public void getRemoveSubscriptionTemplate(HashMap<Integer, Subscription> subDetails, String selectedUser )	throws IOException, SQLException
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_REMOVESUBSCRIPTION_TEMPLATE));
		StringBuilder dynamicHtml = new StringBuilder();
		StringBuilder dynamicHtml1 = new StringBuilder();

		HashMap<String, User> userHm = WfCache.UserDetails();

		dynamicHtml1.append("<div class='row' align='center'>");
		dynamicHtml1.append("<form method = 'get' action = 'hubRemoveSubscription'>");

		dynamicHtml1.append("<div class='col-sm-2'>");
		dynamicHtml1.append("<label>Select Users : </label>");
		dynamicHtml1.append("</div>");
		dynamicHtml1.append("<div class='col-sm-4'>");
		dynamicHtml1.append("<select name = 'selectuser' class='form-control' required/>");
		dynamicHtml1.append("<option>Select</option>");

		for(String key : userHm.keySet()) {

			User u = new User();
			u = userHm.get(key);

			dynamicHtml1.append("<option value = '"+ u.getUsername()+"'>"+ u.getFirstName() +"</option>");
		}

		dynamicHtml1.append("</select>");
		dynamicHtml1.append("</div>");
		dynamicHtml1.append("<div class='col-sm-2'>");
		dynamicHtml1.append("<input type = 'submit' name = 'Select' value = 'Select' class='btn btn-primary btn-sm'>");
		dynamicHtml1.append("</div>");
		dynamicHtml1.append("<div class='col-sm-4'>");
		dynamicHtml1.append("</div>");
		dynamicHtml1.append("</form>");			
		dynamicHtml1.append("</div>");
		dynamicHtml1.append("<br><br>");

		if(selectedUser == null) {

		} else {
			User u1 = new User();
			u1 = userHm.get(selectedUser);
			dynamicHtml1.append("<p> User : " +  u1.getFirstName().substring(0,1).toUpperCase() + u1.getFirstName().substring(1).toLowerCase());


		}

		
		Subscription order = new Subscription();		
		username = WfCache.username;
		role = WfCache.role;

		if(subDetails.size() > 0)
		{
			dynamicHtml.append("<table class='table well' align='center'>");
			dynamicHtml.append("<tr>");

						
			dynamicHtml.append("<th>Movie Name</th>");
			dynamicHtml.append("<th>Start Date</th>");
			dynamicHtml.append("<th>End Date</th>");
			dynamicHtml.append("<th>Subscription Status</th>");
			dynamicHtml.append("<th>Action</th>");
			dynamicHtml.append("</tr>");

			for(Integer key: subDetails.keySet())
			{					
					order = subDetails.get(key);	

					dynamicHtml.append("<tr>");
										
					dynamicHtml.append("<td>"+ order.getMovieName() +"</td>");
					dynamicHtml.append("<td>"+ order.getStartDate() +"</td>");
					dynamicHtml.append("<td>"+ order.getEndDate() +"</td>");
					dynamicHtml.append("<td>"+ order.getSubscriptionStatus() +"</td>");

					if(order.getSubscriptionStatus().equalsIgnoreCase("Active")) {

						dynamicHtml.append("<td>");
						dynamicHtml.append("<form method = 'post' action = 'hubRemoveSubscription'>");
						dynamicHtml.append("<input type='hidden' name = 'productid' value='"+key+"'/>");
						dynamicHtml.append("<input type = 'submit' name = 'Remove' value = 'Remove' class='btn btn-primary btn-sm'>");
						dynamicHtml.append("</form>");
						dynamicHtml.append("</td>");

					} else {

						dynamicHtml.append("<td>");
						
						dynamicHtml.append("</td>");
					}
									
					
					dynamicHtml.append("</tr>");

			}

			dynamicHtml.append("</table>");
		} else {
			dynamicHtml.append("<b>No Subscriptions Found. Select User.</b>");
		}
		pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml1.toString(),"#UserDetail");
		pHtml = modifyHtml(pHtml,dynamicHtml.toString(),"#SubsList");			
		out.append(pHtml);		
	}

	public void getChartTemplate(String productArray, String type)
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_CHART_TEMPLATE));	

		StringBuilder dynamicHtml = new StringBuilder();

		dynamicHtml.append("<input type='hidden' id='idchk' name = 'idchk' value='"+productArray+"'/>");
		dynamicHtml.append("<input type='hidden' id='reporttype' name = 'reporttype' value='"+type+"'/>");


		pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#Chart");
		out.append(pHtml);	
	}

	public void getAddCustomerTemplate()
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_ADDCUSTOMER_TEMPLATE));				
		pHtml = defaultTemplate.toString();
		out.append(pHtml);		
	}
	
	public void getSignUpTemplate()
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_SIGNUP_TEMPLATE));				
		pHtml = defaultTemplate.toString();
		out.append(pHtml);		
	}

	public void getAddMoviesTemplate()
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_ADDMOVIES_TEMPLATE));				
		pHtml = defaultTemplate.toString();
		out.append(pHtml);		
	}

	public void getDeleteMoviesTemplate(HashMap<String, MovieCatalog> categoryMoviesHm, String selectedCategory)	throws IOException, SQLException
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_DELETEMOVIES_TEMPLATE));
		StringBuilder dynamicHtml = new StringBuilder();

		mvc = new MovieCatalog();

		if(categoryMoviesHm.size() > 0){

			dynamicHtml.append("<p><b>Movies of Category : "+selectedCategory+"</b></p>");

			dynamicHtml.append("<table class='table well' align='center' style='overflow: auto;'>");

			for(String key: categoryMoviesHm.keySet()){

				mvc = categoryMoviesHm.get(key);

				dynamicHtml.append("<tr>");			
				dynamicHtml.append("<td>");
				dynamicHtml.append("<p>Movie Name: "+ mvc.getName() +"</p>");
				dynamicHtml.append("</td>");
				dynamicHtml.append("<td align='center'>");
				dynamicHtml.append("<form method = 'post' action = 'hubDeleteMovies'>");
				dynamicHtml.append("<input type='hidden' name = 'productid' value='"+key+"'/>");
				dynamicHtml.append("<input type = 'submit' name = 'Delete' value = 'Delete' class='btn btn-primary btn-sm'>");
				dynamicHtml.append("</form>");			
				dynamicHtml.append("</td>");
				dynamicHtml.append("</tr>");
			}
			dynamicHtml.append("</table>");

		} else {

			dynamicHtml.append("<b>No Movies Found. Select Category.</b>");

		}
		
		pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#ProductsList");			
		out.append(pHtml);		
	}

	
	public void getUpdateMovieTemplate(HashMap<String, MovieCatalog> categoryMoviesHm, String selectedCategory)	throws IOException, SQLException
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_UPDATEMOVIES_TEMPLATE));
		StringBuilder dynamicHtml = new StringBuilder();

		
		mvc = new MovieCatalog();		

		if(categoryMoviesHm.size() > 0){

			dynamicHtml.append("<p><b>Movies of Category : "+selectedCategory+"</b></p>");

		dynamicHtml.append("<table class='table well' align='center' style='overflow: auto;'>");

		for(String key: categoryMoviesHm.keySet())
			{
				mvc = categoryMoviesHm.get(key);

				dynamicHtml.append("<tr>");			
				dynamicHtml.append("<td>");
				dynamicHtml.append("<p>Model: "+ mvc.getName() +"</p>");
				dynamicHtml.append("</td>");
				dynamicHtml.append("<td align='center'>");
				dynamicHtml.append("<form method = 'post' action = 'hubUpdateMovies'>");
				dynamicHtml.append("<input type='hidden' name = 'productid' value='"+key+"'/>");
				dynamicHtml.append("<input type = 'submit' name = 'Update' value = 'Update' class='btn btn-primary btn-sm'>");
				dynamicHtml.append("</form>");			
				dynamicHtml.append("</td>");
				dynamicHtml.append("</tr>");
			}

			dynamicHtml.append("</table>");

		} else {

			dynamicHtml.append("<b>No Movies Found. Select Category.</b>");

		}

		

		pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#movieList");			
		out.append(pHtml);		
	}
	
	public void getUpdateMoviesDetailsTemplate(String movieId)throws IOException, SQLException 
	{		
		StringBuilder dynamicHtml = new StringBuilder();

		movieHm = WfCache.getAllMovies();
		mvc = new MovieCatalog();	

		mvc = movieHm.get(movieId);

		dynamicHtml.append("<div class='col-md-9' align='justify'>");
		dynamicHtml.append("<section id='content'>");
		dynamicHtml.append("<table class='table well' align='center' style='overflow: auto;'>");

		dynamicHtml.append("<form method='post' action='hubUpdateMoviesDetails'>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td>Movie Id : </td><td><input type='text' class='form-control' name='mid'	value='"+mvc.getId()+"' readonly></td>");
		dynamicHtml.append("</tr>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td>Movie Name : </td><td><input type='text' class='form-control' name='mname' value='"+mvc.getName()+"' readonly></td>");
		dynamicHtml.append("</tr>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td>Description : </td><td><input type='text' class='form-control' name='mdescription' value='"+mvc.getDescription()+"' ></td>");
		dynamicHtml.append("</tr>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td>Movie Price : </td><td><input type='number' class='form-control' name='mprice' value='"+mvc.getPrice()+"' ></td>");
		dynamicHtml.append("</tr>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td>Rating : </td><td><input type='text' class='form-control' class='form-control' name='mrating' value='"+mvc.getRating()+"' ></td>");
		dynamicHtml.append("</tr>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td>Director : </td><td><input type='text' class='form-control' name='mdirector' value='"+mvc.getDirector()+"' ></td>");
		dynamicHtml.append("</tr>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td>Movie Casts:</td><td><input type='text' class='form-control' name='mcast' value='"+mvc.getCast()+"' ></td>");
		
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td>On Sale:</td><td><input type='text' class='form-control' name='monsale' value='"+mvc.getOnSale()+"' ></td>");
		dynamicHtml.append("</tr>");

		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td>Discount : </td><td><input type='text' class='form-control' name='mdiscount' value='"+mvc.getDiscount()+"' ></td>");
		dynamicHtml.append("</tr>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td>Category : </td><td><input type='text' class='form-control' name='mcategory' value='"+mvc.getCategory()+"' readonly></td>");
		dynamicHtml.append("</tr>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td>Video :</td><td><input type='text' class='form-control' name='mvideo' value='"+mvc.getVideo()+"' readonly></td>");
		dynamicHtml.append("</tr>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td>Image : </td><td><input type='text' class='form-control' name='mimage' value='"+mvc.getImage()+"' readonly></td>");
		dynamicHtml.append("</tr>");


		
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td align='center' colspan='2'><input type='submit' value='Update Movie' class='btn btn-warning btn-sm'></td>");
		dynamicHtml.append("</tr>");
		dynamicHtml.append("</form>");
		dynamicHtml.append("</table>");
		dynamicHtml.append("</section>");
		dynamicHtml.append("</div");
		dynamicHtml.append("</div");

		pHtml = dynamicHtml.toString();			
		out.append(pHtml);		
	}
	
	
	
	public void getProductContentTemplate(String movieCategory, String movieId)	throws IOException
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_PRODUCT_TEMPLATE));
		StringBuilder dynamicHtml = new StringBuilder();
		String post = "hub" + movieCategory;

		System.out.println("%%%%%%%%%%%%%%" + WfCache.username + WfCache.role);	

		try 
		{
			movieHm = WfCache.getCategoryMovies(movieCategory);

		}catch(SQLException e){
			System.out.println("Utilities SQL EXCEPTION");
		}

		dynamicHtml.append("<div class = 'container-fluid'>");
		

		mvc = new MovieCatalog();

		if(movieId != null) {

			//movieId = movieId.toUpperCase();


			for(String key: movieHm.keySet())
			{
				if(key.equalsIgnoreCase(movieId)){

					mvc = movieHm.get(key);
					break;
				}

			}

				dynamicHtml.append("<div class='col-sm-4 hsize' >");
				dynamicHtml.append("<div class='card'>");
				dynamicHtml.append("<div  align='center'>");
				dynamicHtml.append("<img class='card-img-top' src='" + mvc.getImage() +"' alt='Card Image'>");
				dynamicHtml.append("</div>");
				dynamicHtml.append("<div class='card-body'>");
				dynamicHtml.append("<h4 class='card-title'>");
				dynamicHtml.append("<b>"+ mvc.getName() +"</b>");
				dynamicHtml.append("<form id='viewmve' method = 'get' action = 'hubViewdescription' style='display: initial;'>");
				dynamicHtml.append("<input type='hidden' name = 'movieid' value='"+movieId+"'/>");
				dynamicHtml.append("<button type = 'submit' name = '"+movieCategory+"' class='btn btn-info btn-circle pull-right'><i class='glyphicon glyphicon-info-sign '></i></button>");
				dynamicHtml.append("</form>");
				dynamicHtml.append("</h4>");
				dynamicHtml.append("<h5>" + mvc.getPrice() + "</h5>");
				dynamicHtml.append("<p class='card-text'>" + mvc.getDescription() + "</p>");
				dynamicHtml.append("</div>");
				dynamicHtml.append("<div class='card-footer'>");
				dynamicHtml.append("<div class='row-fluid'>");
				dynamicHtml.append("<div id='stars-existing' class='starrr' data-rating='"+ mvc.getRating() +"'></div>");



				
				if(WfCache.username != null) {

					if(mvc.getOnSale().equalsIgnoreCase("yes") && !WfCache.role.equalsIgnoreCase("admin")) {

						
						dynamicHtml.append("<form method = 'post' action = '"+post+"' style='display: initial;'>");
						dynamicHtml.append("<input type='hidden' name = 'movieid' value='"+movieId+"'/>");
						dynamicHtml.append("<input type = 'submit' name = '"+movieCategory+"' class='btn btn-warning btn-sm pull-right' value = 'Add to Cart'>");
						dynamicHtml.append("</form>");

					}
				}else if(WfCache.username == null) {

					if(WfCache.role.equalsIgnoreCase("guest") && mvc.getCategory().equalsIgnoreCase("Free")) {

						
						dynamicHtml.append("<form method = 'get' action = 'hubWatch' style='display: initial;'>");
						dynamicHtml.append("<input type='hidden' name = 'movieid' value='"+movieId+"'/>");
						dynamicHtml.append("<input type = 'submit' name = 'watch' class='btn btn-warning btn-sm pull-right' value = 'Watch It!!'>");
						dynamicHtml.append("</form>");

					}
				}

				dynamicHtml.append("<form method = 'get' action = 'hubWriteFeedback' style='display: initial;'>");
				dynamicHtml.append("<input type='hidden' name = 'movieid' value='"+movieId+"'/>");
				dynamicHtml.append("<input type = 'submit' name = 'feedback' class='btn btn-warning btn-sm pull-right' value = 'Write Feedback'>");
				dynamicHtml.append("</form>");

				dynamicHtml.append("</div>");

				dynamicHtml.append("</div>");
				dynamicHtml.append("</div>");
				dynamicHtml.append("</div>");

		}
		else {

			for(String key: movieHm.keySet())
			{
				mvc = movieHm.get(key);

				dynamicHtml.append("<div class='col-sm-4 hsize' >");
				dynamicHtml.append("<div class='card'>");
				dynamicHtml.append("<div  align='center'>");
				dynamicHtml.append("<img class='card-img-top' src='" + mvc.getImage() +"' alt='Card Image'>");
				dynamicHtml.append("</div>");
				dynamicHtml.append("<div class='card-body'>");
				dynamicHtml.append("<h4 class='card-title'>");
				dynamicHtml.append("<b>"+ mvc.getName() +"</b>");
				dynamicHtml.append("<form id='viewmve' method = 'get' action = 'hubViewdescription' style='display: initial;'>");
				dynamicHtml.append("<input type='hidden' name = 'movieid' value='"+key+"'/>");
				dynamicHtml.append("<button type = 'submit' name = '"+movieCategory+"' class='btn btn-info btn-circle pull-right'><i class='glyphicon glyphicon-info-sign '></i></button>");
				dynamicHtml.append("</form>");
				dynamicHtml.append("</h4>");
				dynamicHtml.append("<h5>" + mvc.getPrice() + "</h5>");
				dynamicHtml.append("<p class='card-text'>" + mvc.getDescription() + "</p>");
				dynamicHtml.append("</div>");
				dynamicHtml.append("<div class='card-footer'>");
				dynamicHtml.append("<div class='row-fluid'>");
				dynamicHtml.append("<div id='stars-existing' class='starrr' data-rating='"+ mvc.getRating() +"'></div>");



				
				if(WfCache.username != null) {

					if(mvc.getOnSale().equalsIgnoreCase("yes") && !WfCache.role.equalsIgnoreCase("admin")) {

						
						dynamicHtml.append("<form method = 'post' action = '"+post+"' style='display: initial;'>");
						dynamicHtml.append("<input type='hidden' name = 'movieid' value='"+key+"'/>");
						dynamicHtml.append("<input type = 'submit' name = '"+movieCategory+"' class='btn btn-warning btn-sm pull-right' value = 'Add to Cart'>");
						dynamicHtml.append("</form>");

					}
				}else if(WfCache.username == null) {

					if( mvc.getCategory().equalsIgnoreCase("Free")) {

						
						dynamicHtml.append("<form method = 'get' action = 'hubWatch' style='display: initial;'>");
						dynamicHtml.append("<input type='hidden' name = 'movieid' value='"+key+"'/>");
						dynamicHtml.append("<input type = 'submit' name = 'watch' class='btn btn-warning btn-sm pull-right' value = 'Watch It!!'>");
						dynamicHtml.append("</form>");

					}
				}

				dynamicHtml.append("<form method = 'get' action = 'hubWriteFeedback' style='display: initial;'>");
				dynamicHtml.append("<input type='hidden' name = 'movieid' value='"+key+"'/>");
				dynamicHtml.append("<input type = 'submit' name = 'feedback' class='btn btn-warning btn-sm pull-right' value = 'Write Feedback'>");
				dynamicHtml.append("</form>");

				dynamicHtml.append("</div>");

				dynamicHtml.append("</div>");
				dynamicHtml.append("</div>");
				dynamicHtml.append("</div>");				
			}

		}

		

		dynamicHtml.append("</div>");
		pHtml = modifyHtml(defaultTemplate.toString(),movieCategory,"#productType");
		pHtml = modifyHtml(pHtml,dynamicHtml.toString(),"#ProductsList");
		System.out.println(pHtml);		
		out.append(pHtml);		
	}

	public void getCartTemplate() throws IOException, SQLException
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_CART_TEMPLATE));
		StringBuilder dynamicHtml = new StringBuilder();

		movieHm = WfCache.showCart();	

		dynamicHtml.append("<table class='table well' align='center'>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<th>Name</th>");
		dynamicHtml.append("<th>Description</th>");
		dynamicHtml.append("<th>Price</th>");
		dynamicHtml.append("<th></th>");
		dynamicHtml.append("</tr>");

		mvc = new MovieCatalog();

		if(movieHm != null)
		{			
			for(String key: movieHm.keySet())
			{
				mvc = movieHm.get(key);

				dynamicHtml.append("<tr>");				
				dynamicHtml.append("<td>");
				dynamicHtml.append("<p>"+ mvc.getName() +"</p>");
				dynamicHtml.append("</td>");
				dynamicHtml.append("<td>");
				dynamicHtml.append("<p>"+ mvc.getDescription()+ "</p>");
				dynamicHtml.append("</td>");
				dynamicHtml.append("<td>");
				dynamicHtml.append("<p>"+ mvc.getPrice()+ "</p>");
				dynamicHtml.append("</td>");
				dynamicHtml.append("<td>");
				dynamicHtml.append("<form method = 'post' action = 'hubCart'>");
				dynamicHtml.append("<input type='hidden' name = 'movieid' value='"+key+"'/>");
				dynamicHtml.append("<input type='hidden' name = 'opr' value='delete'/>");
				dynamicHtml.append("<button type = 'submit' name = 'Delete' value = 'Delete Item' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-trash'></button>");
				dynamicHtml.append("</form>");
				dynamicHtml.append("</td>");
				dynamicHtml.append("</tr>");
			}
			dynamicHtml.append("<tr>");
			dynamicHtml.append("<td align='center' colspan='5'>");
			dynamicHtml.append("<form method = 'get' action = 'hubSubscribe'>");
			dynamicHtml.append("<input type = 'submit' name = 'Subscribe' value = 'Subscribe' class='btn btn-warning btn-sm'>");
			dynamicHtml.append("</form>");
			dynamicHtml.append("</td>");
			dynamicHtml.append("</tr>");
		}

			dynamicHtml.append("</table>");

		pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#CartList");				
		out.append(pHtml);		
	}
	
	public void getSubscribeTemplate()
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_SUBSCRIBE_TEMPLATE));
		StringBuilder dynamicHtml = new StringBuilder();
		float totalCartPrice = 0;
		float totalProductPrice = 0;

		if(WfCache.username != null && (WfCache.role.equalsIgnoreCase("customer") || WfCache.role.equalsIgnoreCase("guest")))
		{ 
			movieHm = WfCache.showCart();

			dynamicHtml.append("<table class='table well' align='center'>");
			dynamicHtml.append("<tr>");
			dynamicHtml.append("<th>Name</th>");
			dynamicHtml.append("<th>Retailer</th>");
			dynamicHtml.append("<th>Total Price</th>");
			//dynamicHtml.append("<th>Quantity</th>");
			dynamicHtml.append("</tr>");
			mvc = new MovieCatalog();

			if(movieHm != null)
			{			
				for(String key: movieHm.keySet())
				{
					mvc = movieHm.get(key);

					totalProductPrice = mvc.getPrice();
					totalCartPrice = totalCartPrice + totalProductPrice;				

					dynamicHtml.append("<tr>");				
					dynamicHtml.append("<td>");
					dynamicHtml.append("<p>"+ mvc.getName() +"</p>");
					dynamicHtml.append("</td>");
					dynamicHtml.append("<td>");
					dynamicHtml.append("<p>"+ mvc.getDescription()+ "</p>");
					dynamicHtml.append("</td>");
					dynamicHtml.append("<td>");				
					dynamicHtml.append("<p>"+ totalProductPrice + "</p>");
					dynamicHtml.append("</td>");
					dynamicHtml.append("</tr>");
				}

				dynamicHtml.append("</table>");

				dynamicHtml.append("<div class='row' align='center'>");
				dynamicHtml.append("<p>Subtotal: "+ totalCartPrice +"</p>");
				dynamicHtml.append("</div>");

				dynamicHtml.append("<div class='row' align='center'>");
				dynamicHtml.append("<form method = 'post' action = 'hubSubscribe'>");

				dynamicHtml.append("<div class='col-sm-4'>");					
				dynamicHtml.append("<p>Customer Name: </p>");
				dynamicHtml.append("<input type='text' class='form-control' name='custName' required>");
				dynamicHtml.append("</div>");

				dynamicHtml.append("<div class='col-sm-4'>");
				dynamicHtml.append("<p>Address: </p>");
				dynamicHtml.append("<input type='text' class='form-control' name='custAddr' required>");
				dynamicHtml.append("</div>");

				dynamicHtml.append("<div class='col-sm-4'>");
				dynamicHtml.append("<p>CardNumber: </p>");
				dynamicHtml.append("<input type='text' class='form-control' name='cardnumber' required>");
				dynamicHtml.append("</div>");


				dynamicHtml.append("</div> <br> <br>");

				dynamicHtml.append("<div class='row' align='center'>");			
				dynamicHtml.append("<input type = 'submit' name = 'Buy' value = 'Buy' class='btn btn-warning btn-sm'>");			
				dynamicHtml.append("</div>");
				dynamicHtml.append("</form>");

			}

		}
		else
		{
			dynamicHtml.append("<h1 style='float: center; color:red'>Please Login.</h1>");
			dynamicHtml.append("<a href='hubLogin'> Login Page </a>");
			dynamicHtml.append("<br/>");
			dynamicHtml.append("<a href='hubSigUp'> Signup Page </a>");			
		}
		pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#Checkout");				
		out.append(pHtml);
	}
	
	
	public void getUserSubscriptionTemplate(HashMap<Integer, Subscription> subDetails)
	{

		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_MYSUBSCRIPTION_TEMPLATE));
		StringBuilder dynamicHtml = new StringBuilder();

		Subscription order = new Subscription();		
		username = WfCache.username;
		role = WfCache.role;

		if(subDetails != null)
		{
			dynamicHtml.append("<table class='table well' align='center'>");
			dynamicHtml.append("<tr>");

			if(role.equals("admin")) {
				dynamicHtml.append("<th>Customer</th>");
			}
			dynamicHtml.append("<th>Subscription ID</th>");
			dynamicHtml.append("<th>Movie Name</th>");
			dynamicHtml.append("<th>Start Date</th>");
			dynamicHtml.append("<th>End Date</th>");
			dynamicHtml.append("<th>Subscription Status</th>");
			dynamicHtml.append("<th>Action</th>");
			dynamicHtml.append("</tr>");

			for(Integer key: subDetails.keySet())
			{					
					order = subDetails.get(key);	

					dynamicHtml.append("<tr>");
					if(role.equals("admin")) {
						dynamicHtml.append("<td>"+ order.getUsername().substring(0,1).toUpperCase() + order.getUsername().substring(1).toLowerCase() +"</td>");
					}
					dynamicHtml.append("<td>"+ order.getSubscriptionId() +"</td>");
					dynamicHtml.append("<td>"+ order.getMovieName() +"</td>");
					dynamicHtml.append("<td>"+ order.getStartDate() +"</td>");
					dynamicHtml.append("<td>"+ order.getEndDate() +"</td>");
					dynamicHtml.append("<td>"+ order.getSubscriptionStatus() +"</td>");

					if(order.getSubscriptionStatus().equalsIgnoreCase("Active")) {

						dynamicHtml.append("<td>");
						dynamicHtml.append("<form method = 'get' action = 'hubWatch' style='display: initial;'>");
						dynamicHtml.append("<input type='hidden' name = 'movieid' value='"+order.getMovieId()+"'/>");
						dynamicHtml.append("<input type = 'submit' name = 'watch' class='btn btn-warning btn-sm pull-right' value = 'Watch It!!'>");
						dynamicHtml.append("</form>");
						dynamicHtml.append("</td>");

					} else {

						dynamicHtml.append("<td>");
						dynamicHtml.append("<form method = 'post' action = 'hubRenewSubscription' style='display: initial;'>");
						dynamicHtml.append("<input type='hidden' name = 'movieid' value='"+order.getMovieId()+"'/>");
						dynamicHtml.append("<input type = 'submit' name = 'renew' class='btn btn-warning btn-sm pull-right' value = 'Renew'>");
						dynamicHtml.append("</form>");
						dynamicHtml.append("</td>");
					}
									
					
					dynamicHtml.append("</tr>");

			}

			dynamicHtml.append("</table>");
			pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#UserOrder");				
			out.append(pHtml);
		}


	}
	
	public void getProductDescription(String movieCategory, String movieID) throws IOException, SQLException
	{
			StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_DESCRIPTION_TEMPLATE));
			StringBuilder dynamicHtml = new StringBuilder();

			mvc = new MovieCatalog();
			movieHm = WfCache.getAllMovies();

			
			dynamicHtml.append("<table class='table well' align='center'>");
			dynamicHtml.append("<tr>");
			dynamicHtml.append("</tr>");


			for(String key: movieHm.keySet())
			{
				mvc = movieHm.get(key);

				if(mvc.getId().equalsIgnoreCase(movieID)) {

					dynamicHtml.append("<tr>");
					dynamicHtml.append("<td>");				
					dynamicHtml.append("<img src = '"+ mvc.getImage() +"' width = '250' height = '250' alt = 'phone'>");				
					dynamicHtml.append("</td>");
					dynamicHtml.append("<td>");
					dynamicHtml.append("<p>Rating : <div id='stars-existing' class='starrr' data-rating='"+ mvc.getRating() +"'></div></p>");
					dynamicHtml.append("<p>Movie Name: "+ mvc.getName() +"</p>");
					dynamicHtml.append("<p>Director: "+ mvc.getDirector()+ "</p>");
					dynamicHtml.append("<p>Cast: "+ mvc.getCast()+ "</p>");
					dynamicHtml.append("<p>Subscription Price: "+ mvc.getPrice()+ "</p>");
					dynamicHtml.append("<p>Category: "+ mvc.getCategory()+ "</p>");
					dynamicHtml.append("<p>Description: "+ mvc.getDescription()+ "</p>");
					dynamicHtml.append("<p>Movie On Sale: "+ mvc.getOnSale()+ "</p>");
					dynamicHtml.append("<iframe src='https://www.facebook.com/plugins/like.php?href=https%3A%2F%2Fwww.facebook.com%2F"+mvc.getName()+"&width=450&layout=standard&action=like&size=small&show_faces=true&share=true&height=80&appId' width='450' height='80' style='border:none;overflow:hidden' scrolling='no' frameborder='0' allowTransparency='true'></iframe>");
					dynamicHtml.append("<a float='left' href='https://twitter.com/"+mvc.getName()+"?ref_src=twsrc%5Etfw' class='twitter-follow-button' data-show-count='false'></a><script async src='https://platform.twitter.com/widgets.js' charset='utf-8'></script>");
		
					dynamicHtml.append("</td>");

					dynamicHtml.append("</tr>");
				}


			}




			pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#Description");				
			out.append(pHtml);
	}

	public void getWriteFeebackTemplate(MovieCatalog pc, String category)
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_WRITEFEEDBACK_TEMPLATE));
		StringBuilder dynamicHtml = new StringBuilder();

		String ndate = dateFormat.format(new Date());

		dynamicHtml.append("<form method = 'post' action = 'hubWriteFeedback'>");
		dynamicHtml.append("<table class='table well' align='center'>");				

		dynamicHtml.append("<tr>");				
		dynamicHtml.append("<td>");
		dynamicHtml.append("Movie Name : ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='mname' class='form-control' value='"+pc.getName()+"' readonly>");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");

		dynamicHtml.append("<tr>");	
		dynamicHtml.append("<td>");
		dynamicHtml.append("Category : ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='mcategory' class='form-control' value='"+category+"' readonly>");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");	

		dynamicHtml.append("<tr>");	
		dynamicHtml.append("<td>");
		dynamicHtml.append("Movie Price : ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='mprice' class='form-control' value='"+pc.getPrice()+"' readonly>");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");	

		dynamicHtml.append("<tr>");	
		dynamicHtml.append("<td>");
		dynamicHtml.append("Description : ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='mdescription' class='form-control' value='"+pc.getDescription()+"' readonly>");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");		

		dynamicHtml.append("<tr>");	
		dynamicHtml.append("<td>");
		dynamicHtml.append("Director :");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='mdirector' class='form-control' value='"+pc.getDirector()+"' readonly>");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");

		dynamicHtml.append("<tr>");				
		dynamicHtml.append("<td>");
		dynamicHtml.append("Review Rating: ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<select name='mrating' class='form-control'>");
		dynamicHtml.append("<option value='1'>1</option>");
		dynamicHtml.append("<option value='2'>2</option>");
		dynamicHtml.append("<option value='3'>3</option>");
		dynamicHtml.append("<option value='4'>4</option>");
		dynamicHtml.append("<option value='5'>5</option>");
		dynamicHtml.append("</select>");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");	

		dynamicHtml.append("<tr>");	
		dynamicHtml.append("<td>");
		dynamicHtml.append("User ID : ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='username' class='form-control' value='"+WfCache.username+"' >");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");	

		dynamicHtml.append("<tr>");	
		dynamicHtml.append("<td>");
		dynamicHtml.append("User Age : ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='userage' class='form-control' >");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");	

		dynamicHtml.append("<tr>");	
		dynamicHtml.append("<td>");
		dynamicHtml.append("User Gender : ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='usergender' class='form-control' >");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");

		dynamicHtml.append("<tr>");	
		dynamicHtml.append("<td>");
		dynamicHtml.append("City : ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='mcity' class='form-control' >");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");

		dynamicHtml.append("<tr>");				
		dynamicHtml.append("<td>");
		dynamicHtml.append("Feedback Date: ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='feedbackdate' class='form-control' value='"+ ndate +"' readonly>");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");	

		dynamicHtml.append("<tr>");	
		dynamicHtml.append("<td>");
		dynamicHtml.append("Comments : ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<textarea type='text' name='mcomments' class='form-control' ></textarea>");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");
		dynamicHtml.append("</table>");										
	
		dynamicHtml.append("<div class='row' align='center'>");			
		dynamicHtml.append("<input type = 'submit' name = 'Submit Feedback' value = 'Submit Feedback' class='btn btn-warning btn-sm'>");			
		dynamicHtml.append("</div>");
		dynamicHtml.append("</form>");		

		pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#WriteReview");				
		out.append(pHtml);
	}

	
}