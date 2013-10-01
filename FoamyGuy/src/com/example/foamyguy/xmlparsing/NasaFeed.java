package com.example.foamyguy.xmlparsing;
/*
 * Data object that holds all of our information about a NASA daily image item.
 */
public class NasaFeed {
	private String title;
	private String date;
	private String enclosure;
	private String link;
	

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	
	public String getEnclosure() {
		return enclosure;
	}
	public void setEnclosure(String enclosure) {
		this.enclosure = enclosure;
	}
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	
	@Override
	public String toString() {
		return "Nasa Daily Image Item [Title=" + title + ", Date=" + date + ", Image="
				+ enclosure +  "]";
	}
}
