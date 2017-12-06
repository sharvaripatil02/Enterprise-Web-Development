
public class Feedback {

	private String movieName;	
	private String movieCategory;
	private String moviePrice;
	private String movieDescription;
	private String movieDirector;
	private String movieRating;
	private String userId;
	private String userAge;	
	private String userGender;
	private String userCity;
	private String fbdate;
	private String comments;
	
	
	public Feedback()
	{
		
	}
	

	public Feedback(String movieName, String movieCategory, String moviePrice, String movieDescription, String movieDirector, String movieRating, String userId, String userAge, String userGender, String userCity, String fbdate, String comments) {
		
		this.movieName = movieName;	
		this.movieCategory = movieCategory;
		this.moviePrice = moviePrice;
		this.movieDescription = movieDescription;
		this.movieDirector = movieDirector;
		this.movieRating = movieRating;
		this.userId = userId;
		this.userAge = userAge;
		this.userGender = userGender;
		this.userCity = userCity;
		this.fbdate = fbdate;
		this.comments = comments;
	}
	
	
	public String getMovieName() {
		return movieName;
	}
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public String getMovieCategory() {
		return movieCategory;
	}

	public void setMovieCategory(String movieCategory) {
		this.movieCategory = movieCategory;
	}

	public String getMoviePrice() {
		return moviePrice;
	}

	public void setMoviePrice(String moviePrice) {
		this.moviePrice = moviePrice;
	}

	public String getMovieDescription() {
		return movieDescription;
	}

	public void setMovieDescription(String movieDescription) {
		this.movieDescription = movieDescription;
	}

	public String getMovieDirector() {
		return movieDirector;
	}

	public void setMovieDirector(String movieDirector) {
		this.movieDirector = movieDirector;
	}

	public String getMovieRating() {
		return movieRating;
	}

	public void setMovieRating(String movieRating) {
		this.movieRating = movieRating;
	}

	public String getUserID() {
		return userId;
	}

	public void setUserID(String userId) {
		this.userId = userId;
	}

	public String getUserAge() {
		return userAge;
	}

	public void setUserAge(String userAge) {
		this.userAge = userAge;
	}

	public String getUserGender() {
		return userGender;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}

	public String getUserCity() {
		return userCity;
	}

	public void setUserCity(String userCity) {
		this.userCity = userCity;
	}

	public String getComment() {
		return comments;
	}

	public void setComment(String comments) {
		this.comments = comments;
	}

	public String getDate() {
		return fbdate;
	}

	public void setDate(String fbdate) {
		this.fbdate = fbdate;
	}

	
}
