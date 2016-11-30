package sk.zatko.vi.recommender.elasticsearch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

public class ElasticsearchConnector {

	final static Logger logger = Logger.getLogger(ElasticsearchConnector.class);
	
	private static final String ELASTIC_NODE_URI = "http://localhost:9200/";
	
	
	public CloseableHttpResponse getRequest(String uri) {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(ELASTIC_NODE_URI + uri);

	    setHeader(httpGet);
	    
	    CloseableHttpResponse response = null;
	    
		try {
			response = httpClient.execute(httpGet);
			
		} catch (IOException e) {
			logger.error("Cannot execute request " + httpGet.toString(), e);
		} finally {
			closeQuietly(httpClient);
		}

		return response;
	}
	
	
	public CloseableHttpResponse postRequest(String uri, String requestBody) {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(ELASTIC_NODE_URI + uri);

	    setHeader(httpPost);
	    
	    try {
			httpPost.setEntity(new StringEntity(requestBody));
			
		} catch (UnsupportedEncodingException e) {
			logger.error("Cannot set body of request " + httpPost.toString(), e);
		}
	    
	    CloseableHttpResponse response = null;
	    
		try {
			response = httpClient.execute(httpPost);
			
		} catch (IOException e) {
			logger.error("Cannot execute request " + httpPost.toString(), e);
		} finally {
			closeQuietly(httpClient);
		}

		return response;
	}
	
	
	public CloseableHttpResponse putRequest(String uri, String requestBody) {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPut httpPut = new HttpPut(ELASTIC_NODE_URI + uri);

	    setHeader(httpPut);
	    
	    try {
			httpPut.setEntity(new StringEntity(requestBody));
			
		} catch (UnsupportedEncodingException e) {
			logger.error("Cannot set body of request " + httpPut.toString(), e);
		}
	    
	    CloseableHttpResponse response = null;
	    
		try {
			response = httpClient.execute(httpPut);
			
		} catch (IOException e) {
			logger.error("Cannot execute request " + httpPut.toString(), e);
		} finally {
			closeQuietly(httpClient);
		}

		return response;
	}
	
	
	public CloseableHttpResponse deleteRequest(String uri) {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpDelete httpDelete = new HttpDelete(ELASTIC_NODE_URI + uri);

	    setHeader(httpDelete);
	    
	    CloseableHttpResponse response = null;
	    
		try {
			response = httpClient.execute(httpDelete);
			
		} catch (IOException e) {
			logger.error("Cannot execute request " + httpDelete.toString(), e);
		} finally {
			closeQuietly(httpClient);
		}

		return response;
	}
	

	private void setHeader(HttpRequestBase request) {
		
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/json");
	}
	
	
	private void closeQuietly(CloseableHttpClient httpClient) {
		
		try {
			httpClient.close();
		} catch (IOException e) {
			logger.error("Cannot clode http client", e);
		}
	}
	
	protected String readFile(String filePath) throws IOException {
		
		byte[] encoded = Files.readAllBytes(Paths.get(filePath));
		return new String(encoded, Charset.defaultCharset());
	}
	
	public String convertToUTF8(String s) throws UnsupportedEncodingException {

		return new String(s.getBytes("UTF-8"), "ISO-8859-1");
    }
}
