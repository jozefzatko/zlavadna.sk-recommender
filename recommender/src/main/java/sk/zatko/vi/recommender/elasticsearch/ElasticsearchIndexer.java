package sk.zatko.vi.recommender.elasticsearch;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;

public class ElasticsearchIndexer extends ElasticsearchConnector {
	
	private static final Logger logger = Logger.getLogger(ElasticsearchIndexer.class);
	
	public void createIndex(String indexName, String mappingFile) throws IOException {
		
		CloseableHttpResponse response = postRequest(indexName, readFile(mappingFile));
		
		int statusCode = response.getStatusLine().getStatusCode();
	    
		if (statusCode != 200) {
	    	throw new IOException("Response " + response.toString() + " returned with code " + statusCode);
	    }
		
		logger.info("Index " + indexName + " was created");
	}
	
	
	public void deleteIndex(String indexName) throws IOException {
		
		CloseableHttpResponse response = deleteRequest(indexName);
	    
		int statusCode = response.getStatusLine().getStatusCode();
		
		if (statusCode != 200) {
	    	throw new IOException("Response " + response.toString() + " returned with code " + statusCode);
	    }
	    
	    logger.info("Index " + indexName + " was deleted");
	}
	
	
	public boolean indexExists(String indexName) throws IOException {
		
		CloseableHttpResponse response = getRequest(indexName);
		
	    int statusCode = response.getStatusLine().getStatusCode();
	    
		if (statusCode == 200) {
	    	return true;
	    }  
	    else if (statusCode == 404) {
	    	return false;
	    }
	    else {	
	    	throw new IOException("Response " + response.toString() + " returned with code " + statusCode);
	    }
	}
	
	
	public void indexRecord(String indexName, String documentName, String record, int id) throws IOException {
		
		CloseableHttpResponse response = postRequest(indexName + documentName + "/" + id, convertToUTF8(record));
	    
		int statusCode = response.getStatusLine().getStatusCode();
	    
		if (statusCode != 200 && statusCode != 201) {
			throw new IOException("Response " + response.toString() + " returned with code " + statusCode);
	    }
	}
}
