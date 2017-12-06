import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class SAXProductHandler extends DefaultHandler {
	
	boolean brootnode = false;
	boolean bid = false;
	boolean bname = false;
	boolean bprice = false;
	boolean bstars = false;
	boolean bdirector = false;
	boolean bimage = false;
	boolean bvideo = false;
	boolean brating = false;
	boolean bdiscount = false;
	boolean bonsale = false;
	boolean bcategory = false;
	boolean bdescription = false;

	MovieCatalog catalog;
	
	HashMap<String, MovieCatalog> mvCatalog = new HashMap<String, MovieCatalog>();

	public HashMap<String, MovieCatalog> readDataFromXML(String fileName)	throws ParserConfigurationException, SAXException, IOException 
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		javax.xml.parsers.SAXParser parser = factory.newSAXParser();

		parser.parse(new File(fileName), this);
		
		return mvCatalog;
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException 
	{			
		
		if(qName.matches("(?i)Movies"))
		{
			brootnode = true;
			catalog = new MovieCatalog();
		}
		
		if(brootnode)
		{		
			if (qName.equalsIgnoreCase("ID")) {
				bid = true;
			}

			else if (qName.equalsIgnoreCase("NAME")) {
				bname = true;
			}

			else if (qName.equalsIgnoreCase("IMAGE")) {
				bimage = true;
			}

			else if (qName.equalsIgnoreCase("PRICE")) {
				bprice = true;
			}

			else if (qName.equalsIgnoreCase("DIRECTOR")) {
				bdirector = true;
			}

			else if (qName.equalsIgnoreCase("STARS")) {
				bstars = true;
			}
			
			else if (qName.equalsIgnoreCase("DESCRIPTION")) {
				bdescription = true;
			}
			
			else if (qName.equalsIgnoreCase("DISCOUNT")) {
				bdiscount = true;
			}

			else if (qName.equalsIgnoreCase("RATING")) {
				brating = true;
			}
			
			else if (qName.equalsIgnoreCase("CATEGORY")) {
				bcategory = true;
			}

			else if (qName.equalsIgnoreCase("ONSALE")) {
				bonsale = true;
			}

			else if (qName.equalsIgnoreCase("VIDEO")) {
				bvideo = true;
			}
		}
	}

	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
			if(qName.matches("(?i)Movies")) {			
			mvCatalog.put(catalog.getId(), catalog);
			brootnode = false;
		}
	}

	
	public void characters(char[] ch, int start, int length) throws SAXException {	
		

			if (bid) {
				catalog.setId(new String(ch, start, length));
				bid = false;
			}
			
			else if (bname) {
				catalog.setName(new String(ch, start, length));
				bname = false;
			}
			
			else if (bimage) {
				catalog.setImage(new String(ch, start, length));
				bimage = false;
			}
			
			else if (bdirector) {
				catalog.setDirector(new String(ch, start, length));
				bdirector = false;
			}
			else if (bprice) {
				catalog.setPrice(Float.parseFloat(new String(ch, start, length)));
				bprice = false;
			}
			
			else if (bstars) {
				catalog.setCast(new String(ch, start, length));
				bstars = false;
			}

			else if (bdescription) {
				catalog.setDescription(new String(ch, start, length));
				bdescription = false;
			}

			else if (brating) {
				catalog.setRating(Integer.parseInt(new String(ch, start, length)));
				brating = false;
			}

			else if (bdiscount) {
				catalog.setDiscount(Float.parseFloat(new String(ch, start, length)));
				bdiscount = false;
			}

			else if (bonsale) {
				catalog.setOnSale(new String(ch, start, length));
				bonsale = false;
			}	

			else if (bcategory) {
				catalog.setCategory(new String(ch, start, length));
				bcategory = false;
			}

			else if (bvideo) {
				catalog.setVideo(new String(ch, start, length));
				bvideo = false;
			}			
	}
}