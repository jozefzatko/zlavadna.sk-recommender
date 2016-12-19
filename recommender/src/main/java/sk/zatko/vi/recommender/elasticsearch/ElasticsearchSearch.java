package sk.zatko.vi.recommender.elasticsearch;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;

public class ElasticsearchSearch extends ElasticsearchConnector {

	private static final Logger logger = Logger.getLogger(ElasticsearchSearch.class);
	
	public String search(String indexName, String query, int countOfResults) throws IOException {
		
		CloseableHttpResponse response = postRequest(indexName + "/_search?size=" + countOfResults, query);
		
		int statusCode = response.getStatusLine().getStatusCode();
	    
		if (statusCode != 200) {

			logger.error(query);
	    	throw new IOException("Response " + response.toString() + " returned with code " + statusCode);
	    }
		
		return lastResponse;
	}
}
