package sk.zatko.vi.recommender.recommend;

import java.io.IOException;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import sk.zatko.vi.recommender.elasticsearch.ElasticsearchSearch;
import sk.zatko.vi.recommender.elasticsearch.morelikethis.LikeModel;
import sk.zatko.vi.recommender.elasticsearch.morelikethis.MoreLikeThisModel;
import sk.zatko.vi.recommender.elasticsearch.morelikethis.MoreLikeThisModelDetails;
import sk.zatko.vi.recommender.elasticsearch.morelikethis.MoreLikeThisQuery;

public abstract class ContentBasedRecommender extends Recommender {

	protected static final EntityManager ENTITY_MANAGER = Persistence.createEntityManagerFactory("db").createEntityManager();
	
	protected static final String USER_DEALS =
			"SELECT d.id, d.title, d.description FROM activities a\r\n" + 
			"JOIN users u ON a.user_id = u.id\r\n" + 
			"JOIN deals d on a.deal_id = d.id\r\n" + 
			"WHERE d.in_test = false\r\n" + 
			"AND d.in_train = true\r\n" + 
			"AND u.id = :user_id";
	
	protected static int lastTookTime;
	
	protected abstract ArrayList<LikeModel> createLikes(int currentUserId, int currentDealId);
	
	public ArrayList<Integer> recommend(int currentUserId, int currentDealId, int countOfResults) {
		
		ArrayList<Integer> results;
		
		long startTime = System.currentTimeMillis();
		
		String preparedQuery = prepareElasticsearchQuery(currentUserId, currentDealId);
		long preparedTime = System.currentTimeMillis();
		
		String response = "";
		try {
			response = new ElasticsearchSearch().search(INDEX_NAME, preparedQuery, countOfResults);
		} catch (IOException e) {
			e.printStackTrace();
		}
		long responseTime = System.currentTimeMillis();
		
		results = parseResults(response, countOfResults);
		long parsedTime = System.currentTimeMillis();
		
		System.out.println("Create elasticsearch query: " + (preparedTime - startTime) + " ms");
		System.out.println("REST request: " + (responseTime - preparedTime) + " ms (took: " + lastTookTime + " ms)");
		System.out.println("Parse results time: " + (parsedTime - responseTime) + " ms");
		
		return results;
	}
	
	protected String prepareElasticsearchQuery(int currentUserId, int currentDealId) {
		
		ArrayList<MoreLikeThisModel> queries = new ArrayList<MoreLikeThisModel>();
		
		ArrayList<LikeModel> likes = createLikes(currentUserId, currentDealId);
		
		queries.add(createMoreLikeThisModel("title", likes, 1 , 3));
		queries.add(createMoreLikeThisModel("description", likes, 1 , 1));	
		
		MoreLikeThisQuery query = new MoreLikeThisQuery(queries);
		
		return query.toJson("2014-08-30");
	}
	
	protected ArrayList<Integer> parseResults(String response, int countOfResults) {
		
		ArrayList<Integer> results = new ArrayList<Integer>(countOfResults);
		
		JsonElement rootElement = new JsonParser().parse(response);
		JsonObject rootObject = rootElement.getAsJsonObject();
		
		lastTookTime = rootObject.get("took").getAsInt();
		
		JsonObject hitsObjects = rootObject.get("hits").getAsJsonObject();
		JsonArray hitsArray = hitsObjects.getAsJsonArray("hits");
		
		for (int i=0; i<countOfResults; i++) {
			
			JsonObject dealJsonObject = hitsArray.get(i).getAsJsonObject();	
			results.add(dealJsonObject.get("_id").getAsInt());	
		}
		
		return results;
	}
	
	protected MoreLikeThisModel createMoreLikeThisModel(String field, ArrayList<LikeModel> likes, int minTermFreq, int boost) {
		
		ArrayList<String> fields = new ArrayList<String>();
		fields.add(field);
		MoreLikeThisModelDetails model =  new MoreLikeThisModelDetails(fields, likes, minTermFreq, boost);
		
		return new MoreLikeThisModel(model);
	}
}
