package sk.zatko.vi.recommender.models;

import java.util.Date;

public class Activity {

	private int id;
	private User user;
	private DealItem dealItem;
	private Deal deal;
	private int quantity;
	private double marketPrice;
	private double teamPrice;
	private double discount;
	private Date createdAt;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public DealItem getDealItem() {
		return dealItem;
	}
	public void setDealItem(DealItem dealItem) {
		this.dealItem = dealItem;
	}
	
	public Deal getDeal() {
		return deal;
	}
	public void setDeal(Deal deal) {
		this.deal = deal;
	}
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public double getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}
	
	public double getTeamPrice() {
		return teamPrice;
	}
	public void setTeamPrice(double teamPrice) {
		this.teamPrice = teamPrice;
	}
	
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
