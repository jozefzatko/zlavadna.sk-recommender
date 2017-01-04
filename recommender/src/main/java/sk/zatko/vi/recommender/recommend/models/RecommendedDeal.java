package sk.zatko.vi.recommender.recommend.models;

public class RecommendedDeal implements Comparable<RecommendedDeal> {

	private int id;
	private double weight;
	
	public RecommendedDeal(int id, double weight) {
		
		this.id = id;
		this.weight = weight;
	}

	@Override
	public int compareTo(RecommendedDeal o) {
		
		if (this.weight > o.weight) {
			return -1;
		}
		
		if (this.weight < o.weight) {
			return 1;
		}
		
		return 0;
	}

	public int getId() {
		return id;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

}
