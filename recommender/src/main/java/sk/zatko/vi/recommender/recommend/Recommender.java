package sk.zatko.vi.recommender.recommend;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

public abstract class Recommender {
	
	protected static final EntityManager ENTITY_MANAGER = Persistence.createEntityManagerFactory("db").createEntityManager();
	
	public abstract ArrayList<Integer> recommend(int currentuserId, int currentDealId, Date currentDate, int countOfResults);
	
	protected List<?> executeSqlQuery(Query preparedQuery) {
		
		return preparedQuery.getResultList();
	}
	
	protected ArrayList<Integer> parseResults(List<?> resultList) {
		
		ArrayList<Integer> results = new ArrayList<Integer>();
		
		Iterator<?> iterator = resultList.iterator();  
		   
		while (iterator.hasNext()) {  
		   
			Object[] result = (Object[])iterator.next(); 
			int id = (Integer) result[0];
			
			results.add(id);
		}  

		return results;
	}
}
