package sk.zatko.vi.recommender.recommend;

import java.util.ArrayList;
import java.util.Date;

import sk.zatko.vi.recommender.elasticsearch.morelikethis.LikeModel;

public class SimpleContentBasedRecommender extends ContentBasedRecommender {
	
	public static void main(String args[]) {
		
		new SimpleContentBasedRecommender().recommend(885, 35749, new Date(1406852020L * 1000L), 10);
		
		System.exit(0);
	}
	
	protected ArrayList<LikeModel> createLikes(int currentUserId, int currentDealId) {
		
		ArrayList<LikeModel> likes = new ArrayList<LikeModel>();
		
		LikeModel like = new LikeModel(INDEX_NAME, DOCUMENT_TYPE, currentDealId + "");
		likes.add(like); 

		return likes;
	}
	
	public String toString() {
		
		return "SimpleContentBasedRecommender";
	}
}
