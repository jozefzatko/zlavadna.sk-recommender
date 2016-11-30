package sk.zatko.vi.recommender.models;

import java.util.Set;

import com.google.gson.annotations.Expose;
import com.vividsolutions.jts.geom.Point;

public class Deal {

	@Expose private int id;
	private Set<DealItem> dealItems;
	private Set<Activity> activities;
	@Expose private String title;
	@Expose private String description;
	private String city;
	private int partnerId;
	
	private Point location;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Set<DealItem> getDealItems() {
		return dealItems;
	}
	public void setDealItems(Set<DealItem> dealItems) {
		this.dealItems = dealItems;
	}
	
	public Set<Activity> getActivities() {
		return activities;
	}
	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public int getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(int partnerId) {
		this.partnerId = partnerId;
	}
	
	public Point getLocation() {
		return location;
	}
	public void setLocation(Point location) {
		this.location = location;
	}
	
}
