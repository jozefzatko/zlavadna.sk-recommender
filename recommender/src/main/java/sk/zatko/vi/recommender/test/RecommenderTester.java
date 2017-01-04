package sk.zatko.vi.recommender.test;

import java.util.ArrayList;
import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import sk.zatko.vi.recommender.models.Activity;
import sk.zatko.vi.recommender.recommend.Recommender;

public class RecommenderTester {

	private static final Logger logger = Logger.getLogger(RecommenderTester.class);
	
	protected static final EntityManager ENTITY_MANAGER = Persistence.createEntityManagerFactory("db").createEntityManager();
	
	private static final String USER_DEALS_QUERY = 
			"SELECT a.deal_id, a.id FROM activities a\r\n" + 
			"WHERE a.created_at > '2014-08-01 00:00:00'\r\n" + 
			"AND a.user_id = :user_id";
	
	private Recommender recommender;
	private ArrayList<Integer> users;
	
	int firstActivityId;
	
	public RecommenderTester(Recommender recommender, ArrayList<Integer> users) {
		
		this.recommender = recommender;
		this.users = users;
	}
	
	public void test(int countOfRecommendations) {
		
		PerformanceCapturer.reset();
		
		double totalPrecision = 0.0;
		double totalRecall = 0.0;
		double totalF1 = 0.0;
		
		int zeroResultsCounter = 0;
		int someResultsCounter = 0;
		int fullResultsCounter = 0;
		
		for (Integer id : users) {
			
			ArrayList<Integer> userDeals = getUserTestDeals(id);
			
			Activity firstActivity = ENTITY_MANAGER.find(Activity.class, this.firstActivityId);
			
			ArrayList<Integer> recommended = recommender.recommend(
					id, firstActivity.getDeal().getId(), firstActivity.getCreatedAt(), countOfRecommendations);

			
			if (recommended.size() == 0) {	
				zeroResultsCounter++;
			}
			
			if (recommended.size() > 0 && recommended.size() < countOfRecommendations) {
				someResultsCounter++;
			}
			
			if (recommended.size() == countOfRecommendations) {
				fullResultsCounter++;
			}
			
			TestModel tm = evaluate(userDeals, recommended);
			
			totalPrecision += tm.getPrecision();
			totalRecall += tm.getRecall();
			totalF1 += tm.getF1();
		}
		
		logger.info("- - - - - - - - - - - - - - - - - - - - - - - - - - -");
		
		logger.info(recommender.toString() + " -- " + countOfRecommendations + " recommendations");
		
		logger.info("Precision: " + totalPrecision / users.size());
		logger.info("Recall: " + totalRecall / users.size());
		logger.info("F1: " + totalF1 / users.size());
		
		logger.info("Results count distribution: " + zeroResultsCounter + ", " + someResultsCounter + ", " + fullResultsCounter);
		
		logger.info("Prepare query: " + PerformanceCapturer.getPrepareQueryTime() / (double)users.size() + " ms");
		logger.info("Query: " + PerformanceCapturer.getQueryTime() / (double)users.size() + " ms");
		logger.info("Parse results: " + PerformanceCapturer.getParseResultsTime() / (double)users.size() + " ms");
		
	}
	
	private TestModel evaluate(ArrayList<Integer> userDeals, ArrayList<Integer> recommended) {
		
		TestModel tm = new TestModel();
		
		userDeals.remove(0);
		
		int hits = 0;
		
		for(Integer i : recommended) {
			
			for(Integer j : userDeals) {
				
				if (i.equals(j)) {
					
					hits += 1;
				}
			}
		}

		double tp = new Double(hits).doubleValue();
		double fp = new Double(recommended.size() - hits).doubleValue();
		double fn = new Double(userDeals.size() - hits).doubleValue();
				
		tm.setTp(tp);
		tm.setFp(fp);
		tm.setFn(fn);
		
		return tm;
	}

	private ArrayList<Integer> getUserTestDeals(int userId) {
		
		ArrayList<Integer> deals = new ArrayList<Integer>();
		
		Query selectQuery = ENTITY_MANAGER.createNativeQuery(USER_DEALS_QUERY)
				.setParameter("user_id", userId);
		
		Iterator<?> iterator = selectQuery.getResultList().iterator();  
		
		boolean first = true;
		
		while (iterator.hasNext()) {  
		   
			Object[] result = (Object[])iterator.next(); 
			int id = (int) result[0];
			
			if (first) {
			
				this.firstActivityId = (int) result[1];
				first = false;
			}
			
			deals.add(id);
		}
		
		return deals;
	}
	
}
