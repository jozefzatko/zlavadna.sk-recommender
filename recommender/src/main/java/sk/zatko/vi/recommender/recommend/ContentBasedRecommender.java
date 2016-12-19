package sk.zatko.vi.recommender.recommend;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import sk.zatko.vi.recommender.elasticsearch.ElasticsearchSearch;
import sk.zatko.vi.recommender.elasticsearch.morelikethis.LikeModel;
import sk.zatko.vi.recommender.elasticsearch.morelikethis.MoreLikeThisModel;
import sk.zatko.vi.recommender.elasticsearch.morelikethis.MoreLikeThisModelDetails;
import sk.zatko.vi.recommender.elasticsearch.morelikethis.MoreLikeThisQuery;
import sk.zatko.vi.recommender.exceptions.NoUserHistoryException;

public abstract class ContentBasedRecommender extends Recommender {

	protected static final String INDEX_NAME = "deals";
	protected static final String DOCUMENT_TYPE = "deal";
	
	private static final String ELASTIC_DATE_FORMAT = "yyyy-MM-dd";
	
	protected static final String USER_DEALS_IN_TRAIN =
			"SELECT DISTINCT deal_id, created_at FROM activities\r\n" + 
			"WHERE created_at <= '2014-08-01 00:00:00'\r\n" + 
			"AND user_id = :user_id";
	
	protected static int lastTookTime;
	
	protected abstract ArrayList<LikeModel> createLikes(int currentUserId, int currentDealId) throws NoUserHistoryException;
	
	public ArrayList<Integer> recommend(int currentUserId, int currentDealId, Date currentDate, int countOfResults) {
		
		ArrayList<Integer> results;
		
		String preparedQuery = "";
		try {
			preparedQuery = prepareElasticsearchQuery(currentUserId, currentDealId, currentDate);
			
		} catch (NoUserHistoryException e) {
			
			return new ArrayList<Integer>();
		}
		
		
		String response = "";
		try {
			response = new ElasticsearchSearch().search(INDEX_NAME, preparedQuery, countOfResults);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		results = parseResults(response, countOfResults);
		
		return results;
	}
	
	protected String prepareElasticsearchQuery(int currentUserId, int currentDealId, Date currentDate) throws NoUserHistoryException {
		
		ArrayList<MoreLikeThisModel> queries = new ArrayList<MoreLikeThisModel>();
		
		ArrayList<LikeModel> likes = createLikes(currentUserId, currentDealId);
		
		queries.add(createMoreLikeThisModel("title", likes, 1 , 3));
		queries.add(createMoreLikeThisModel("description", likes, 2 , 1));	
		
		MoreLikeThisQuery query = new MoreLikeThisQuery(queries);
		
		return query.toJson(format(currentDate));
	}
	
	protected ArrayList<Integer> parseResults(String response, int countOfResults) {
		
		ArrayList<Integer> results = new ArrayList<Integer>(countOfResults);
		
		JsonElement rootElement = new JsonParser().parse(response);
		JsonObject rootObject = rootElement.getAsJsonObject();
		
		lastTookTime = rootObject.get("took").getAsInt();
		
		JsonObject hitsObjects = rootObject.get("hits").getAsJsonObject();
		JsonArray hitsArray = hitsObjects.getAsJsonArray("hits");
		
		for (int i=0; i<hitsArray.size(); i++) {
			
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
	
	protected String format(Date date) {
		
		if (date == null) {
			
			return null;
		}
		
		return new SimpleDateFormat(ELASTIC_DATE_FORMAT).format(date);
	}
}
