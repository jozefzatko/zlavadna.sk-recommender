package sk.zatko.vi.recommender.test;

import java.util.ArrayList;
import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class RandomUserGenerator {

	protected static final EntityManager ENTITY_MANAGER = Persistence.createEntityManagerFactory("db").createEntityManager();
	
	private static final String SELECT_QUERY =
			"WITH\r\n" + 
			"user_buy AS (\r\n" + 
			"	SELECT DISTINCT(deal_id), user_id FROM activities\r\n" + 
			"	WHERE created_at >= '2014-08-01 00:00:01'\r\n" + 
			"),\r\n" + 
			"groupped AS (\r\n" + 
			"	SELECT user_id, count(deal_id) FROM user_buy\r\n" + 
			"	GROUP BY user_id\r\n" + 
			")\r\n" + 
			"SELECT * FROM groupped\r\n" + 
			"WHERE count = :count\r\n" +
			"ORDER BY RANDOM()\r\n" +  
			"LIMIT :limit";
	
	public static ArrayList<Integer> get(int count, int countOfDeals) {
		
		ArrayList<Integer> results = new ArrayList<Integer>(count);
		
		Query selectQuery = ENTITY_MANAGER.createNativeQuery(SELECT_QUERY)
				.setParameter("count", countOfDeals)
				.setParameter("limit", count);
		
		Iterator<?> iterator = selectQuery.getResultList().iterator();  
		   
		while (iterator.hasNext()) {  
		   
			Object[] result = (Object[])iterator.next(); 
			int id = (Integer) result[0];
			
			results.add(id);
		}
		
		return results;
	}
}
