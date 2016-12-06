package sk.zatko.vi.recommender.elasticsearch;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;

public class ElasticsearchSearch extends ElasticsearchConnector {

	public String search(String indexName, String query, int countOfResults) throws IOException {
		
		CloseableHttpResponse response = postRequest(indexName + "/_search?size=" + countOfResults, query);
		
		System.out.println(query);
		
		int statusCode = response.getStatusLine().getStatusCode();
	    
		if (statusCode != 200) {
	    	throw new IOException("Response " + response.toString() + " returned with code " + statusCode);
	    }
		
		return lastResponse;
	}
}
