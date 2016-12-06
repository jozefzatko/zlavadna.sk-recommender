package sk.zatko.vi.recommender.elasticsearch.morelikethis;

import java.util.ArrayList;

public class MoreLikeThisModelDetails {

	private ArrayList<String> fields;
	private ArrayList<LikeModel> like;
	private int min_term_freq;
	private int boost;
	
	public MoreLikeThisModelDetails(ArrayList<String> fields, ArrayList<LikeModel> like, int min_term_freq, int boost) {

		this.fields = fields;
		this.like = like;
		this.min_term_freq = min_term_freq;
		this.boost = boost;
	}

	public ArrayList<String> getFields() {
		return fields;
	}
	public void setFields(ArrayList<String> fields) {
		this.fields = fields;
	}

	public ArrayList<LikeModel> getLike() {
		return like;
	}
	public void setLike(ArrayList<LikeModel> like) {
		this.like = like;
	}

	public int getMin_term_freq() {
		return min_term_freq;
	}
	public void setMin_term_freq(int min_term_freq) {
		this.min_term_freq = min_term_freq;
	}

	public int getBoost() {
		return boost;
	}
	public void setBoost(int boost) {
		this.boost = boost;
	}
}
