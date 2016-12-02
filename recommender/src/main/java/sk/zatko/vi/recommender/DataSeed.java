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
	
	private final static Logger logger = Logger.getLogger(DataSeed.class);
	
	private final static String SET_BEGIN_DATE =
			"UPDATE deals dls\n" + 
			"SET begin_date = (\n" + 
			"	SELECT i.coupon_begin_date FROM dealitems i\n" + 
			"	JOIN deals d\n" + 
			"	ON i.deal_id = d.id\n" + 
			"	WHERE d.id = dls.id\n" +
			"   AND i.coupon_begin_date <> '1970-01-01 01:00:00'\n" + 
			"	ORDER BY i.coupon_begin_date\n" + 
			"	LIMIT 1\n" + 
			")";
	
	private final static String SET_END_DATE =
			"UPDATE deals dls\n" + 
			"SET end_date = (\n" + 
			"	SELECT i.coupon_end_date FROM dealitems i\n" + 
			"	JOIN deals d\n" + 
			"	ON i.deal_id = d.id\n" + 
			"	WHERE d.id = dls.id\n" +
			"   AND i.coupon_end_date <> '1970-01-01 01:00:00'\n" + 
			"	ORDER BY i.coupon_end_date DESC\n" + 
			"	LIMIT 1\n" + 
			")";
	
	public static void main(String[] args) {

		CSVParser parser;
		
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("db");
		EntityManager entityManager = emfactory.createEntityManager();		

		logger.info("Starting data seed from CSV files into database...");
		
		try {
			
			parser = new DealsSeed(entityManager, "data//train_deal_details.csv", "data//test_deal_details.csv");
			parser.readAndStoreData();
			logger.info("Data seed: deals");
			
			parser = new DealItemsSeed(entityManager, "data//train_dealitems.csv", "data//test_dealitems.csv");
			parser.readAndStoreData();
			logger.info("Data seed: deal items");
			
			parser = new UsersSeed(entityManager, "data//train_activity.csv", "data//test_activity.csv");
			parser.readAndStoreData();
			logger.info("Data seed: users");
			
			parser = new ActivitiesSeed(entityManager, "data//train_activity.csv", "data//test_activity.csv");
			parser.readAndStoreData();
			logger.info("Data seed: activities");
			
			entityManager.getTransaction().begin();
			entityManager.createNativeQuery(SET_BEGIN_DATE).executeUpdate();
			entityManager.createNativeQuery(SET_END_DATE).executeUpdate();
			entityManager.getTransaction().commit();
			
		} catch (NumberFormatException | IOException e) {
			logger.error("Error while seeding train database", e);
			System.exit(1);
		} finally {
			entityManager.close();
		}
		
		logger.info("Data seed successfully completed");
		
		System.exit(0);
	}

}
