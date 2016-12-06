package sk.zatko.vi.recommender.recommend;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import sk.zatko.vi.recommender.elasticsearch.morelikethis.LikeModel;

public class CombineContentBasedRecommender extends ContentBasedRecommender {

	public static void main(String args[]) {
		
		new CombineContentBasedRecommender().recommend(885, 35749, new Date(1406852020L * 1000L), 10);
		
		System.exit(0);
	}

	protected ArrayList<LikeModel> createLikes(int currentUserId, int currentDealId) {
		
		ArrayList<LikeModel> likes = new ArrayList<LikeModel>();
		
		likes.add(new LikeModel(INDEX_NAME, DOCUMENT_TYPE, currentDealId + ""));
		
		
		Query selectQuery = ENTITY_MANAGER.createNativeQuery(USER_DEALS).setParameter("user_id", currentUserId);
		List<?> results = selectQuery.getResultList();
		Iterator<?> iterator = results.iterator();  
		   
		while (iterator.hasNext()) {  
		   
			Object[] result = (Object[])iterator.next(); 
		   
			int id = (Integer) result[0];
		     
			LikeModel like = new LikeModel(INDEX_NAME, DOCUMENT_TYPE, (int)id + "");
			likes.add(like);
		}
		
		return likes;
	}
}
