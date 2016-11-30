package sk.zatko.vi.recommender;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.http.client.ClientProtocolException;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import sk.zatko.vi.recommender.elasticsearch.ElasticsearchIndexer;
import sk.zatko.vi.recommender.models.Deal;

public class DataIndex {

	final static Logger logger = Logger.getLogger(DataIndex.class);
	
	private static final String MAPPING_FILE = "src//main//resources//elasticsearch//mappings.json";
	
	private static final String INDEX_NAME = "deals/";
	
	private static final String TRAIN_PERSISTANCE_UNIT = "train_db";
	private static final String TEST_PERSISTANCE_UNIT = "test_db";
	
	private static final String DOCUMENT_NAME = "deal";
	

	public static void main(String[] args) {
		
		DataIndex indexer = new DataIndex();
		
		logger.info("Indexing data...");
		
		indexer.index(INDEX_NAME);
		
		logger.info("Indexing was successfull");
		
		System.exit(0);
	}
	
	private void index(String indexName) {
	
		ElasticsearchIndexer indexer = new ElasticsearchIndexer();
		
		try {
			
			if (indexer.indexExists(indexName)) {
				indexer.deleteIndex(indexName);
			}
			indexer.createIndex(indexName, MAPPING_FILE);
			
			indexData(indexer, TRAIN_PERSISTANCE_UNIT, "train", indexName, DOCUMENT_NAME);
			indexData(indexer, TEST_PERSISTANCE_UNIT, "test", indexName, DOCUMENT_NAME);
			
		} catch (IOException e) {
			logger.error("Error while indexing data", e);
		}		
	}
	
	private void indexData(ElasticsearchIndexer indexer, String persistenceUnit, String type, String indexName, String documentName) throws ClientProtocolException, IOException {
		
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory(persistenceUnit);
		EntityManager entityManager = emfactory.createEntityManager();

	    Query query = entityManager.createQuery("SELECT d FROM Deal d");
	    
		for (Object o : query.getResultList()) {
			
			indexDealObject(indexer, (Deal) o, type, indexName, documentName);
		}
		
		entityManager.close();
	}
	
	private void indexDealObject(ElasticsearchIndexer indexer, Deal deal, String type, String indexName, String documentName) {
		
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		JsonElement jsonElement = gson.toJsonTree(deal);
		jsonElement.getAsJsonObject().addProperty("type", type);
		gson.toJson(jsonElement);
		
		String jsonObject = gson.toJson(jsonElement);
		
		try {
			indexer.indexRecord(indexName, documentName, jsonObject, deal.getId());
			
			logger.info("Indexing deal with id: " + deal.getId() + " was successfull");
			
		} catch (IOException e) {
			
			logger.error("Failed to create index with id: " + deal.getId(), e);
			logger.info(jsonObject);
		}
	}
	
}
