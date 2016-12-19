package sk.zatko.vi.recommender.recommend;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import sk.zatko.vi.recommender.elasticsearch.morelikethis.LikeModel;
import sk.zatko.vi.recommender.exceptions.NoUserHistoryException;

public class UserHistoryContentBasedRecommender extends ContentBasedRecommender {
	
	public static void main(String args[]) {
		
		new UserHistoryContentBasedRecommender().recommend(885, 35749, new Date(1406852020L * 1000L), 10);
		
		System.exit(0);
	}

	protected ArrayList<LikeModel> createLikes(int currentUserId, int currentDealId) throws NoUserHistoryException {
		
		ArrayList<LikeModel> likes = new ArrayList<LikeModel>();
		
		Query selectQuery = ENTITY_MANAGER.createNativeQuery(USER_DEALS_IN_TRAIN).setParameter("user_id", currentUserId);
		List<?> results = selectQuery.getResultList();
		
		if (results.isEmpty()) {
			
			throw new NoUserHistoryException("user_id: " + currentUserId);
		}
		
		Iterator<?> iterator = results.iterator();  
		   
		while (iterator.hasNext()) {  
		   
			Object[] result = (Object[])iterator.next(); 
		   
			int id = (Integer) result[0];
		     
			LikeModel like = new LikeModel(INDEX_NAME, DOCUMENT_TYPE, (int)id + "");
			likes.add(like);
		}  

		return likes;
	}
	
	public String toString() {
		
		return "UserHistoryContentBasedRecommender";
	}

}