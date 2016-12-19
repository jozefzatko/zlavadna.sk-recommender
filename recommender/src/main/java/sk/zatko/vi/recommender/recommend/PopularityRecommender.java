package sk.zatko.vi.recommender.recommend;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TemporalType;

public class PopularityRecommender extends Recommender {

	private static final String SELECT_QUERY =
			"WITH\r\n" + 
			"actual_deal AS (\r\n" + 
			"	SELECT id, location FROM deals\r\n" + 
			"	WHERE id = :deal_id\r\n" + 
			"),\r\n" + 
			"close_deals AS (\r\n" + 
			"	SELECT d.id, d.end_date FROM deals d\r\n" + 
			"	JOIN actual_deal ad ON ST_Distance(d.location, ad.location) < 0.4\r\n" + 
			"	WHERE d.id <> ad.id\r\n" + 
			"	AND d.in_test = true\r\n" + 
			"),\r\n" + 
			"dist_activities AS (\r\n" + 
			"	SELECT DISTINCT deal_id, user_id FROM activities\r\n" + 
			")\r\n" + 
			"SELECT a.deal_id, count(d.id) from close_deals d\r\n" + 
			"JOIN dist_activities a ON d.id = a.deal_id\r\n" + 
			"WHERE d.end_date > :date\r\n" + 
			"GROUP BY a.deal_id\r\n" + 
			"ORDER BY count DESC\r\n" + 
			"LIMIT :limit";
	
	public static void main(String args[]) {
		
		new PopularityRecommender().recommend(885, 35749, new Date(1406852020L * 1000L), 10);
		
		System.exit(0);
	}
	
	public ArrayList<Integer> recommend(int currentUserId, int currentDealId, Date currentDate, int countOfResults) {
		
		ArrayList<Integer> results;
		
		Query preparedQuery = prepareSqlQuery(currentUserId, currentDealId, currentDate, countOfResults);
		
		List<?> resultList = executeSqlQuery(preparedQuery);
		
		results = parseResults(resultList);
		
		return results;
	}

	private Query prepareSqlQuery(int currentUserId, int currentDealId, Date currentDate, int countOfResults) {
		
		Query selectQuery = ENTITY_MANAGER.createNativeQuery(SELECT_QUERY)
				.setParameter("deal_id", currentDealId)
				.setParameter("date", currentDate, TemporalType.DATE)
				.setParameter("limit", countOfResults);
		
		return selectQuery;
	}
	
	public String toString() {
		
		return "PopularityRecommender";
	}
}
