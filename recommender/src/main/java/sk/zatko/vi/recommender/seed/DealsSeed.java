package sk.zatko.vi.recommender.seed;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import sk.zatko.vi.recommender.models.Deal;

public class DealsSeed extends CSVParser {
	
	private static final String INSERT_QUERY_TO_TRAIN_WITH_GPS =
			"INSERT INTO deals (id, city, description, in_train, in_test, location, partner_id, title)\n" +
			"SELECT :id, :city, :description, true, false, ST_SetSRID(ST_MakePoint(:gps_lat, :gps_long),4326), :partner_id, :title\n" +
			"WHERE NOT EXISTS (SELECT * FROM deals WHERE id = :id)";
	
	private static final String INSERT_QUERY_TO_TRAIN_WITHOUT_GPS =
			"INSERT INTO deals (id, city, description, in_train, in_test, partner_id, title)\n" +
			"SELECT :id, :city, :description, true, false, :partner_id, :title\n" +
			"WHERE NOT EXISTS (SELECT * FROM deals WHERE id = :id)";
	
	private static final String INSERT_QUERY_TO_TEST_WITH_GPS =
			"INSERT INTO deals (id, city, description, in_train, in_test, location, partner_id, title)\n" +
			"SELECT :id, :city, :description, false, true, ST_SetSRID(ST_MakePoint(:gps_lat, :gps_long),4326), :partner_id, :title\n" +
			"WHERE NOT EXISTS (SELECT * FROM deals WHERE id = :id)";
	
	private static final String INSERT_QUERY_TO_TEST_WITHOUT_GPS =
			"INSERT INTO deals (id, city, description, in_train, in_test, partner_id, title)\n" +
			"SELECT :id, :city, :description, false, true, :partner_id, :title\n" +
			"WHERE NOT EXISTS (SELECT * FROM deals WHERE id = :id)";

	public DealsSeed(EntityManager entityManager, String train1CsvFile, String train2CsvFile, String testCsvFile) throws FileNotFoundException, UnsupportedEncodingException {
		
		super(entityManager, train1CsvFile, train2CsvFile, testCsvFile);
	}
	
	@Override
	public void readAndStoreData() throws NumberFormatException, IOException {

		EntityTransaction transaction = this.entityManager.getTransaction();
		String[] csvLine;

		while ((csvLine = train1Reader.readNext()) != null) {
			if (csvLine != null) {

				csvLine = removeNulls(csvLine);
				
				Integer id = Integer.parseInt(csvLine[0]);
				String title_deal = csvLine[1];
				String title_desc = csvLine[2];
				String title_city = csvLine[3];
				Integer partner_id = "".equals(csvLine[5]) ? null : Integer.parseInt(csvLine[5]);
				Double gpslat = "".equals(csvLine[6]) ? null : Double.parseDouble(csvLine[6]);
				Double gpslong = "".equals(csvLine[7]) ? null : Double.parseDouble(csvLine[7]);
				
				if (title_desc.startsWith("test") || title_desc.length() < 3) {
					continue;
				}
				
				
				Query insertQuery;
				
				if (gpslat == null || gpslong == null) {
					insertQuery = this.entityManager.createNativeQuery(INSERT_QUERY_TO_TRAIN_WITHOUT_GPS);
				} else {
					insertQuery = this.entityManager.createNativeQuery(INSERT_QUERY_TO_TRAIN_WITH_GPS)
							.setParameter("gps_lat", gpslat)
							.setParameter("gps_long", gpslong);
				}
				
				insertQuery.setParameter("id", id)
							.setParameter("city", title_city)
							.setParameter("description", title_desc)
							.setParameter("partner_id", partner_id)
							.setParameter("title", title_deal);
				
				transaction.begin();
				insertQuery.executeUpdate();
				transaction.commit();
			}
		}
		train1Reader.close();
		
		
		while ((csvLine = train2Reader.readNext()) != null) {
			if (csvLine != null) {

				csvLine = removeNulls(csvLine);
				
				Integer id = Integer.parseInt(csvLine[0]);
				String title_deal = csvLine[1];
				String title_desc = csvLine[2];
				String title_city = csvLine[3];
				Integer partner_id = "".equals(csvLine[5]) ? null : Integer.parseInt(csvLine[5]);
				Double gpslat = "".equals(csvLine[6]) ? null : Double.parseDouble(csvLine[6]);
				Double gpslong = "".equals(csvLine[7]) ? null : Double.parseDouble(csvLine[7]);
				
				if (title_desc.startsWith("test") || title_desc.length() < 3) {
					continue;
				}
				
				
				Query insertQuery;
				
				if (gpslat == null || gpslong == null) {
					insertQuery = this.entityManager.createNativeQuery(INSERT_QUERY_TO_TRAIN_WITHOUT_GPS);
				} else {
					insertQuery = this.entityManager.createNativeQuery(INSERT_QUERY_TO_TRAIN_WITH_GPS)
							.setParameter("gps_lat", gpslat)
							.setParameter("gps_long", gpslong);
				}
				
				insertQuery.setParameter("id", id)
							.setParameter("city", title_city)
							.setParameter("description", title_desc)
							.setParameter("partner_id", partner_id)
							.setParameter("title", title_deal);
				
				transaction.begin();
				insertQuery.executeUpdate();
				transaction.commit();
			}
		}
		train2Reader.close();
		
		
		while ((csvLine = testReader.readNext()) != null) {
			if (csvLine != null) {

				csvLine = removeNulls(csvLine);
				
				Integer id = Integer.parseInt(csvLine[0]);
				
				Deal deal = this.entityManager.find(Deal.class, id);
				if (deal != null) {
					
					transaction.begin();
					deal.setInTest(true);
					transaction.commit();
					continue;
				}
				
				String title_deal = csvLine[1];
				String title_desc = csvLine[2];
				String title_city = csvLine[3];
				Integer partner_id = "".equals(csvLine[5]) ? null : Integer.parseInt(csvLine[5]);
				Double gpslat = "".equals(csvLine[6]) ? null : Double.parseDouble(csvLine[6]);
				Double gpslong = "".equals(csvLine[7]) ? null : Double.parseDouble(csvLine[7]);
				
				if (title_desc.startsWith("test") || title_desc.length() < 3) {
					continue;
				}

				Query insertQuery;
					
				if (gpslat == null || gpslong == null) {
					insertQuery = this.entityManager.createNativeQuery(INSERT_QUERY_TO_TEST_WITHOUT_GPS);
				} else {
					insertQuery = this.entityManager.createNativeQuery(INSERT_QUERY_TO_TEST_WITH_GPS)
							.setParameter("gps_lat", gpslat)
							.setParameter("gps_long", gpslong);
				}
					
				insertQuery.setParameter("id", id)
							.setParameter("city", title_city)
							.setParameter("description", title_desc)
							.setParameter("partner_id", partner_id)
							.setParameter("title", title_deal);
					
				transaction.begin();
				insertQuery.executeUpdate();
				transaction.commit();
			}
		}
		testReader.close();
	}

}