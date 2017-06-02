package sk.zatko.vi.recommender.recommend;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import sk.zatko.vi.recommender.test.PerformanceCapturer;

public class CollaborativeRecommender extends Recommender {
	
	protected static final String SELECT_QUERY =
			"WITH\r\n" + 
			"user_activities AS (\r\n" + 
			"	SELECT DISTINCT deal_id, user_id FROM activities a\r\n" + 
			"	WHERE a.in_train = true" + 
			"	AND user_id = :user_id\r\n" + 
			"),\r\n" + 
			"all_activities AS (\r\n" + 
			"	SELECT DISTINCT deal_id, user_id FROM activities\r\n" + 
			"	WHERE a.in_train = true\r\n" +
			"	AND user_id <> :user_id\r\n" + 
			"),\r\n" + 
			"similiar_users AS(\r\n" + 
			"	SELECT aa.user_id, count(aa.deal_id) FROM user_activities ua\r\n" + 
			"	JOIN all_activities aa\r\n" + 
			"	ON ua.deal_id = aa.deal_id\r\n" + 
			"	GROUP BY aa.user_id\r\n" + 
			"	ORDER BY count DESC\r\n" + 
			"	LIMIT 10\r\n" + 
			")\r\n" + 
			"\r\n" + 
			"SELECT a.deal_id, count(a.id) FROM activities a\r\n" + 
			"JOIN deals d ON d.id = a.deal_id\r\n" + 
			"JOIN similiar_users su ON su.user_id = a.user_id\r\n" + 
			"WHERE NOT EXISTS (SELECT * FROM activities WHERE user_id = :user_id AND d.id = deal_id)\r\n" + 
			"AND a.in_test = true\r\n" + 
			"AND d.end_date >= :date\r\n" + 
			"GROUP BY a.deal_id\r\n" + 
			"ORDER BY count DESC\r\n" + 
			"LIMIT :limit";
	
	public static void main(String args[]) {
		
		new CollaborativeRecommender().recommend(885, 35749, new Date(1406852020L * 1000L), 10);
		
		System.exit(0);
	}
	
	public ArrayList<Integer> recommend(int currentUserId, int currentDealId, Date currentDate, int countOfResults) {
		
		ArrayList<Integer> results;
		
		long startTime = System.currentTimeMillis();
		
		Query preparedQuery = prepareSqlQuery(currentUserId, currentDealId, currentDate, countOfResults);
		long preparedTime = System.currentTimeMillis();
		
		List<?> resultList = executeSqlQuery(preparedQuery);
		long executeTime = System.currentTimeMillis();
		
		results = parseResults(resultList);
		long parsedTime = System.currentTimeMillis();
		
		PerformanceCapturer.addToPrepareQueryTime(preparedTime - startTime);
		PerformanceCapturer.addToQueryTime(executeTime - preparedTime);
		PerformanceCapturer.addToParseResultsTime(parsedTime - executeTime);
		
		return results;
	}

	private Query prepareSqlQuery(int currentUserId, int currentDealId, Date currentDate, int countOfResults) {
		
		Query selectQuery = ENTITY_MANAGER.createNativeQuery(SELECT_QUERY)
				.setParameter("user_id", currentUserId)
				.setParameter("date", currentDate, TemporalType.DATE)
				.setParameter("limit", countOfResults);
		
		return selectQuery;
	}
	
	public String toString() {
		
		return "CollaborativeRecommender";
	}

}
