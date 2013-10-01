package com.example.foamyguy.xmlparsing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.util.Log;

public class ImagesXmlPullParser {

	static final String ITEM = "item";
	static final String TITLE = "title";
	static final String DATE = "date";
	static final String ENCLOSURE = "enclosure";
	static final String  LINK = "link";

	public static List<NasaFeed> getItemsFromFile(Context ctx) {

		// List of StackSites that we will return
		List<NasaFeed> items;
		items = new ArrayList<NasaFeed>();

		// temp holder for current image while parsing
		NasaFeed curItem = null;
		// temp holder for current text value while parsing
		String curText = "";

		try {
			// Get our factory and PullParser
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xpp = factory.newPullParser();

			// Open up InputStream and Reader of our file.
			FileInputStream fis = ctx.openFileInput("nasa_image_of_the_day.xml");
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

			// point the parser to our file.
			xpp.setInput(reader);

			// get initial eventType
			int eventType = xpp.getEventType();

			// Loop through pull events until we reach END_DOCUMENT
			while (eventType != XmlPullParser.END_DOCUMENT) {
				// Get the current tag
				String tagname = xpp.getName();

				// React to different event types appropriately
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if (tagname.equalsIgnoreCase(ITEM)) {
						// If we are starting a new <site> block we need
						//a new StackSite object to represent it
						curItem = new NasaFeed();
					}
					if(tagname.equalsIgnoreCase(ENCLOSURE)){
						Log.i("StackSites", xpp.getAttributeValue(null, "url"));
						curItem.setEnclosure(xpp.getAttributeValue(null, "url"));
					}
					break;

				case XmlPullParser.TEXT:
					//grab the current text so we can use it in END_TAG event
					curText = xpp.getText();
					break;

				case XmlPullParser.END_TAG:
					if (tagname.equalsIgnoreCase(ITEM)) {
						// if </site> then we are done with current Site
						// add it to the list.
						items.add(curItem);
					}
					if(null != curItem){
						if (tagname.equalsIgnoreCase(TITLE)) {
							// if </title> use setTitle() on curItem
							
							curItem.setTitle(curText);
						} else if (tagname.equalsIgnoreCase(DATE)) {
							// if </date> use setDate() on curItem
							curItem.setDate(curText);
						} else if (tagname.equalsIgnoreCase(ENCLOSURE)) {
							// if </link> use setEnclosure() on curItem
							//curItem.setEnclosure(curText);
						}  else if (tagname.equalsIgnoreCase(LINK)) {
							// if </link> use setLink() on curItem
							curItem.setLink(curText);
						}
					}
					break;

				default:
					break;
				}
				//move on to next iteration
				eventType = xpp.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// return the populated list.
		return items;
	}
}