package sk.zatko.vi.recommender;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import sk.zatko.vi.recommender.seed.ActivitiesSeed;
import sk.zatko.vi.recommender.seed.CSVParser;
import sk.zatko.vi.recommender.seed.DealItemsSeed;
import sk.zatko.vi.recommender.seed.DealsSeed;
import sk.zatko.vi.recommender.seed.UsersSeed;

public class DataSeed {
	
	final static Logger logger = Logger.getLogger(DataSeed.class);
	
	public static void main(String[] args) {

		CSVParser parser;
		
		EntityManagerFactory emfactory;
		EntityManager entityManager;		
		
		emfactory = Persistence.createEntityManagerFactory("train_db");
		entityManager = emfactory.createEntityManager();
		
		logger.info("Starting data seed from CSV files into database...");
		
		try {
			
			parser = new DealsSeed(entityManager, "data//train_deal_details.csv");
			parser.readAndStoreData();
			logger.info("Data seed from train_deal_details.csv to train_db.deals completed");
			
			parser = new DealItemsSeed(entityManager, "data//train_dealitems.csv");
			parser.readAndStoreData();
			logger.info("Data seed from train_dealitems.csv to train_db.dealitems completed");
			
			parser = new UsersSeed(entityManager, "data//train_activity.csv");
			parser.readAndStoreData();
			logger.info("Data seed from train_activity.csv to train_db.activities completed");
			
			parser = new ActivitiesSeed(entityManager, "data//train_activity.csv");
			parser.readAndStoreData();
			logger.info("Data seed from train_activity.csv to train_db.users completed");
			
		} catch (NumberFormatException | IOException e) {
			logger.error("Error while seeding train database");
			logger.error(e.getLocalizedMessage());
			logger.error(e);
			e.printStackTrace();
		} finally {
			entityManager.close();
		}
		
		
		emfactory = Persistence.createEntityManagerFactory("test_db");
		entityManager = emfactory.createEntityManager();
		
		try {
			
			parser = new DealsSeed(entityManager, "data//test_deal_details.csv");
			parser.readAndStoreData();
			logger.info("Data seed from test_deal_details.csv to test_db.deals completed");
			
			parser = new DealItemsSeed(entityManager, "data//test_dealitems.csv");
			parser.readAndStoreData();
			logger.info("Data seed from test_dealitems.csv to test_db.dealitems completed");
			
			parser = new UsersSeed(entityManager, "data//test_activity.csv");
			parser.readAndStoreData();
			logger.info("Data seed from test_activity.csv to test_db.activities completed");
			
			parser = new ActivitiesSeed(entityManager, "data//test_activity.csv");
			parser.readAndStoreData();
			logger.info("Data seed from test_activity.csv to test_db.users completed");
			
		} catch (NumberFormatException | IOException e) {
			logger.error("Error while seeding test database");
			logger.error(e.getLocalizedMessage());
			logger.error(e);
			e.printStackTrace();
		} finally {
			entityManager.close();
		}
		
		logger.info("Data seed successfully completed");
		
		System.exit(0);
	}

}
