package sk.zatko.vi.recommender.elasticsearch.morelikethis;

import java.util.ArrayList;

import com.google.gson.Gson;

public class MoreLikeThisQuery {

	private static final String BEFORE = "{\"query\":{\"filtered\": {\"query\":{\"dis_max\":{\"queries\":";
	private static final String BETWEEN = " }},\"filter\": {\"range\": {\"end_date\": {\"lt\": \"";
	private static final String AFTER = "\",\"format\":\"yyyy-MM-dd\"}}}}}}";
	
	private ArrayList<MoreLikeThisModel> queries;

	public MoreLikeThisQuery(ArrayList<MoreLikeThisModel> queries) {

		this.queries = queries;
	}
	
	public String toJson(String date) {
		
		String json = new Gson().toJson(queries);
		
		return BEFORE + json + BETWEEN + date + AFTER;
	}

	public ArrayList<MoreLikeThisModel> getQueries() {
		return queries;
	}
	public void setQueries(ArrayList<MoreLikeThisModel> queries) {
		this.queries = queries;
	}

}
