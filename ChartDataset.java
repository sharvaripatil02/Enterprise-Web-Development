import java.util.*;
import java.io.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChartDataset 
{
		
	private String name;
	private int quantity = 0;
	private float price;
	
	public ChartDataset()
	{
		
	}
	
	public ChartDataset(String Name, int Quantity, float Price)
	{
		
		this.name = Name;
		
		this.quantity = Quantity;

		this.price = Price;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
}