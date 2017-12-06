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


public class MongoDBDataStoreUtilities  {
	
	private static final long serialVersionUID = 1L;
	private static MongoClient mongoClient = new MongoClient("localhost",27017);	
	private static DB db = mongoClient.getDB("CustomerReviews");  
	BasicDBObject Query = new BasicDBObject();
	BasicDBObject sort = new BasicDBObject();
	private static DBCollection reviewCollection = db.getCollection("movieFeed");
	
	
	public void init() throws ServletException{
	}
	
	
	public static void insertReview(Feedback rv)
	{
		try
		{
			if(rv != null)
			{
				BasicDBObject temp = new BasicDBObject("MovieName", rv.getMovieName()).
				append("MovieCategory", rv.getMovieCategory()).
				append("MoviePrice", rv.getMoviePrice()).
				append("MovieDescription", rv.getMovieDescription()).
				append("Director", rv.getMovieDirector()).
				append("Rating", rv.getMovieRating()).
				append("Username", rv.getUserID()).
				append("Age", rv.getUserAge()).
				append("Gender", rv.getUserGender()).
				append("City",rv.getUserCity()).
				append("Comments", rv.getComment()).
				append("Date", rv.getDate());
				reviewCollection.insert(temp);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public AggregationOutput category(){
		DBObject object1= new BasicDBObject("_id", 0);
		object1.put("count",new BasicDBObject("$sum",1));
		object1.put("_id", "$MovieCategory");
		DBObject temp1 = new BasicDBObject("$group", object1);
		sort = new BasicDBObject();
		DBObject db1= new BasicDBObject("_id", 0);
		db1.put("value", "$_id");
		db1.put("ReviewValue","$count");
		DBObject temp2 = new BasicDBObject("$project", db1);
		sort.put("ReviewValue",-1);
		DBObject temp3=new BasicDBObject("$sort",sort);
		DBObject temp4=new BasicDBObject("$limit",5);
		AggregationOutput agg0 = reviewCollection.aggregate(temp1,temp2,temp3,temp4);
		return agg0;
	}	
	
	public AggregationOutput city(){
		DBObject object1= new BasicDBObject("_id", 0);
		object1.put("count",new BasicDBObject("$sum",1));
		object1.put("_id", "$City");
		DBObject temp1 = new BasicDBObject("$group", object1);
		sort = new BasicDBObject();
		DBObject db1= new BasicDBObject("_id", 0);
		db1.put("value", "$_id");
		db1.put("ReviewValue","$count");
		DBObject temp2 = new BasicDBObject("$project", db1);
		sort.put("ReviewValue",-1);
		DBObject temp3=new BasicDBObject("$sort",sort);
		DBObject temp4=new BasicDBObject("$limit",5);
		AggregationOutput agg1 = reviewCollection.aggregate(temp1,temp2,temp3,temp4);
		return agg1;
	}	
	
	public AggregationOutput movieTrend(){
		DBObject object2= new BasicDBObject("_id", 0);
		object2.put("count",new BasicDBObject("$sum",1));
		object2.put("_id", "$MovieName");
		DBObject temp5 = new BasicDBObject("$group", object2);
		sort = new BasicDBObject();
		DBObject db2= new BasicDBObject("_id", 0);
		db2.put("value", "$_id");
		db2.put("ReviewValue","$count");
		DBObject temp6 = new BasicDBObject("$project", db2);
		sort.put("ReviewValue",-1);
		DBObject temp7=new BasicDBObject("$sort",sort);
		DBObject temp8=new BasicDBObject("$limit",5);
		AggregationOutput agg2 = reviewCollection.aggregate(temp5,temp6,temp7,temp8);
		return agg2;
	}	
	
	public AggregationOutput leastTrend(){
		DBObject object2= new BasicDBObject("_id", 0);
		object2.put("count",new BasicDBObject("$sum",1));
		object2.put("_id", "$MovieName");
		DBObject temp5 = new BasicDBObject("$group", object2);
		sort = new BasicDBObject();
		DBObject db2= new BasicDBObject("_id", 0);
		db2.put("value", "$_id");
		db2.put("ReviewValue","$count");
		DBObject temp6 = new BasicDBObject("$project", db2);
		sort.put("ReviewValue",1);
		DBObject temp7=new BasicDBObject("$sort",sort);
		DBObject temp8=new BasicDBObject("$limit",5);
		AggregationOutput agg3 = reviewCollection.aggregate(temp5,temp6,temp7,temp8);
		return agg3;
	}
	
	public List<Object> readReview() {
		Map<String,Object>returnReviewMap=new HashMap<String,Object>();
		List<Object>returnReviewList=new ArrayList<>();
		  try{   			
		         MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
		         DB db = mongoClient.getDB("CustomerReviews");				
		         DBCollection coll = db.getCollection("movieFeed");
		         DBCursor cursor = coll.find();		
		         while (cursor.hasNext()) { 
		        	returnReviewList.add(cursor.next());
		         }					
		      }catch(Exception e){
		         System.err.println(e);
		      }
		return returnReviewList;
	}
}