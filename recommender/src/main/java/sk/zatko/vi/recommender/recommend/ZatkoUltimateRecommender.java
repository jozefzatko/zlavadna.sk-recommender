package sk.zatko.vi.recommender.recommend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import sk.zatko.vi.recommender.recommend.models.RecommendedDeal;

public class ZatkoUltimateRecommender extends Recommender {

	private Hashtable<Integer, RecommendedDeal> recommended;
	
	private static final double MOST_POPULAR_BOOST_BEST = 100.0;
	private static final double MOST_POPULAR_BOOST_WORST = 20.0;
	
	private static final double HISTORY_BOOST_BEST = 10.0;
	private static final double HISTORY_BOOST_WORST = 5.0;
	
	private static final double KNOWN_PARTNER_BOOST = 8.0;
	
	private static final String MOST_POPULAR_IN_TRAIN_QUERY =
			"WITH\r\n" + 
			"dist_train_activities AS (\r\n" + 
			"	SELECT DISTINCT deal_id, user_id FROM activities\r\n" + 
			"	WHERE in_train = true\r\n" + 
			")\r\n" + 
			"SELECT a.deal_id, count(d.id) FROM deals d\r\n" + 
			"JOIN dist_train_activities a ON d.id = a.deal_id\r\n" + 
			"WHERE d.end_date > :date\r\n" + 
			"AND d.in_test = true\r\n" + 
			"AND d.in_train = true\r\n" + 
			"GROUP BY a.deal_id\r\n" + 
			"ORDER BY count DESC";
	
	private static final String FROM_KNOWN_PARTNER_QUERY =
			"WITH\r\n" + 
			"partner_ids AS (\r\n" + 
			"	SELECT partner_id AS id FROM activities a\r\n" + 
			"	JOIN deals d ON a.deal_id = d.id\r\n" + 
			"	WHERE user_id = :user_id\r\n" +
			"	AND a.in_train = true\r\n" +
			")\r\n" + 
			"SELECT DISTINCT(d.id), d.partner_id FROM deals d\r\n" + 
			"JOIN partner_ids p ON d.partner_id = p.id\r\n" + 
			"WHERE d.in_test = true\r\n" + 
			"AND d.end_date >= :date";
	
	public static void main(String args[]) {
		
		List<Integer> recommended = new ZatkoUltimateRecommender().recommend(128, 0, new Date(1412164800L * 1000L), 10);
		
		for(Integer id : recommended) {
			
			System.out.println(id.intValue());
		}
		
		System.exit(0);
	}
	
	@Override
	public ArrayList<Integer> recommend(int currentUserId, int currentDealId, Date currentDate, int countOfResults) {
		
		this.recommended = new Hashtable<Integer, RecommendedDeal>();
		
		addMostPopular(currentDate);
		addHistory(currentUserId, currentDate);
		addKnownPartner(currentUserId, currentDate);
		
		return fromHashToResults(countOfResults);
	}
	
	private void addMostPopular(Date currentDate) {
		
		Query selectQuery = ENTITY_MANAGER.createNativeQuery(MOST_POPULAR_IN_TRAIN_QUERY)
				.setParameter("date", currentDate, TemporalType.DATE);
		
		List<?> resultList = executeSqlQuery(selectQuery);
		
		List<Integer> mostPopular = parseResults(resultList);
		
		initHashTable(mostPopular);
		addToHashTable(mostPopular, MOST_POPULAR_BOOST_BEST, MOST_POPULAR_BOOST_WORST);
	}
	
	private void addHistory(int currentUserId, Date currentDate) {
		
		List<Integer> historyRecommended = new UserHistoryContentBasedRecommender().recommend(currentUserId, 0, currentDate, -1);
		
		addToHashTable(historyRecommended, HISTORY_BOOST_BEST, HISTORY_BOOST_WORST);
	}
	
	private void addKnownPartner(int currentUserId, Date currentDate) {
		
		Query selectQuery = ENTITY_MANAGER.createNativeQuery(FROM_KNOWN_PARTNER_QUERY)
				.setParameter("date", currentDate, TemporalType.DATE)
				.setParameter("user_id", currentUserId);
		
		List<?> resultList = executeSqlQuery(selectQuery);
		
		List<Integer> knownPartners = parseResults(resultList);
		
		addToHashTable(knownPartners, KNOWN_PARTNER_BOOST, KNOWN_PARTNER_BOOST);
	}
	
	private void initHashTable(List<Integer> resultList) {
		
		for (Integer id : resultList) {
			
			this.recommended.put(id, new RecommendedDeal(id, 1.0));
		}
	}
	
	private void addToHashTable(List<Integer> resultList, double bestBoost, double worstBoost) {
		
		if (resultList.isEmpty()) {
			return;
		}
		
		double increment = 0;
		
		if (resultList.size() > 1) {
			
			increment = (bestBoost - worstBoost) / (resultList.size() - 1);
		}
		
		for (int i=0; i<resultList.size(); i++) {
			
			int dealId = resultList.get(i).intValue();
			
			RecommendedDeal rd;
			
			double weight = bestBoost - (i * increment);
			
			if (this.recommended.containsKey(dealId)) {
				rd = this.recommended.get(dealId);
			}
			else {
				rd = new RecommendedDeal(dealId, MOST_POPULAR_BOOST_WORST);
			}
			
			rd.setWeight(rd.getWeight() * weight);
			this.recommended.put(dealId, rd);
		}
	}
	
	private ArrayList<Integer> fromHashToResults(int countOfResults) {
		
		ArrayList<Integer> results = new ArrayList<Integer>();
		
		ArrayList<RecommendedDeal> recomenndedDeals = new ArrayList<RecommendedDeal>(recommended.values());
		
		Collections.sort(recomenndedDeals);
		
		for (int i=0; i<countOfResults; i++) {
			
			results.add(recomenndedDeals.get(i).getId());
		}
		
		return results;
	}
	
	public String toString() {
		
		return "ZatkoUltimateRecommender";
	}
}
