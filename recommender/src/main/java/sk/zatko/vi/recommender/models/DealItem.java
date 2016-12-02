package sk.zatko.vi.recommender.models;

import java.util.Date;
import java.util.Set;

public class DealItem {

	private int id;
	private Deal deal;
	private Set<Activity> activities;
	private String title;
	private String couponText1;
	private String couponText2;
	private Date couponBeginDate;
	private Date couponEndDate;

	private boolean inTrain;
	private boolean inTest;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Deal getDeal() {
		return deal;
	}
	public void setDeal(Deal deal) {
		this.deal = deal;
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
	
	public String getCouponText1() {
		return couponText1;
	}
	public void setCouponText1(String couponText1) {
		this.couponText1 = couponText1;
	}
	
	public String getCouponText2() {
		return couponText2;
	}
	public void setCouponText2(String couponText2) {
		this.couponText2 = couponText2;
	}
	
	public Date getCouponBeginDate() {
		return couponBeginDate;
	}
	public void setCouponBeginDate(Date couponBeginDate) {
		this.couponBeginDate = couponBeginDate;
	}
	
	public Date getCouponEndDate() {
		return couponEndDate;
	}
	public void setCouponEndDate(Date couponEndDate) {
		this.couponEndDate = couponEndDate;
	}
	
	public boolean isInTrain() {
		return inTrain;
	}
	public void setInTrain(boolean inTrain) {
		this.inTrain = inTrain;
	}
	
	public boolean isInTest() {
		return inTest;
	}
	public void setInTest(boolean inTest) {
		this.inTest = inTest;
	}

}
