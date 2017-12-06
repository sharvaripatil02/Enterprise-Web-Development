import java.util.*;
import java.io.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MovieCatalog 
{
	private String id;	
	private String name;
	private float price;
	private String image;
	private String director;
	private String cast;
	private int rating;
	private String description;
	private float discount;
	private String category;
	private String onsale;
	private String video;

	
	public MovieCatalog()
	{
		
	}
	
	public MovieCatalog(String id, String name, float price, String image, String director, String cast, int rating, String description, float discount, String category, String onsale, String video)
	{
		this.id = id;
		this.name = name;
		this.price = price;
		this.image = image;
		this.director = director;
		this.cast = cast;	
		this.description = description;
		this.rating = rating;
		this.discount = discount;
		this.category = category;
		this.video = video;
		this.onsale = onsale;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getOnSale() {
		return onsale;
	}

	public void setOnSale(String onsale) {
		this.onsale = onsale;
	}	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getCast() {
		return cast;
	}

	public void setCast(String cast) {
		this.cast = cast;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}
}