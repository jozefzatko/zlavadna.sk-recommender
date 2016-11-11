package sk.zatko.vi.recommender.models;

import java.util.Set;

public class User {

	private int id;
	private Set<Activity> activities;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public Set<Activity> getActivities() {
		return activities;
	}
	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}
	
}
