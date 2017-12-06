import java.util.*;
import java.io.Serializable;

public class Subscription implements Serializable{

	private int subscriptionId;
	private String movieId;
	private String movieName;
	private float moviePrice;
	private String username;
	private String creditcard;
	private String userAddress;
	private Date startDate;
	private Date endDate;
	private String subscriptionStatus;
	
	public Subscription() {}
	
	public Subscription(int subscriptionId, String movieId, String movieName, float moviePrice, String username, String creditcard, String userAddress, Date startDate, Date endDate, String subscriptionStatus)
	{
		this.subscriptionId = subscriptionId;
		this.movieId = movieId;
		this.movieName = movieName;
		this.moviePrice = moviePrice;
		this.username = username;
		this.creditcard = creditcard;
		this.userAddress = userAddress;
		this.startDate = startDate;
		this.endDate = endDate;
		this.subscriptionStatus = subscriptionStatus;
	}

	public int getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(int subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	
	public String getMovieId() {
		return movieId;
	}
	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public float getMoviePrice() {
		return moviePrice;
	}

	public void setMoviePrice(float moviePrice) {
		this.moviePrice = moviePrice;
	}

	public String getCreditcard() {
		return creditcard;
	}

	public void setCreditcard(String creditcard) {
		this.creditcard = creditcard;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getSubscriptionStatus() {
		return subscriptionStatus;
	}

	public void setSubscriptionStatus(String subscriptionStatus) {
		this.subscriptionStatus = subscriptionStatus;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	
}