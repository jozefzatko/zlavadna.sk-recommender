package sk.zatko.vi.recommender.seed;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;


public class DealsSeed extends CSVParser {
	
	private static final String INSERT_QUERY_WITH_GPS =
			"INSERT INTO deals (id, city, description, location, partner_id, title)\n" +
			"SELECT :id, :city, :description, ST_SetSRID(ST_MakePoint(:gps_lat, :gps_long),4326), :partner_id, :title\n" +
			"WHERE NOT EXISTS (SELECT * FROM deals WHERE id = :id)";
	
	private static final String INSERT_QUERY_WITHOUT_GPS =
			"INSERT INTO deals (id, city, description, partner_id, title)\n" +
			"SELECT :id, :city, :description, :partner_id, :title\n" +
			"WHERE NOT EXISTS (SELECT * FROM deals WHERE id = :id)";
	
	public DealsSeed(EntityManager entityManager, String csvFile) throws FileNotFoundException, UnsupportedEncodingException {
		super(entityManager, csvFile);
	}
	
	@Override
	public void readAndStoreData() throws NumberFormatException, IOException {

		EntityTransaction insertTransaction = this.entityManager.getTransaction();
		String[] csvLine;

		while ((csvLine = reader.readNext()) != null) {
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
					insertQuery = this.entityManager.createNativeQuery(INSERT_QUERY_WITHOUT_GPS);
				} else {
					insertQuery = this.entityManager.createNativeQuery(INSERT_QUERY_WITH_GPS)
							.setParameter("gps_lat", gpslat)
							.setParameter("gps_long", gpslong);
				}
				
				insertQuery.setParameter("id", id)
							.setParameter("city", title_city)
							.setParameter("description", title_desc)
							.setParameter("partner_id", partner_id)
							.setParameter("title", title_deal);
				
				insertTransaction.begin();
				insertQuery.executeUpdate();
				insertTransaction.commit();
			}
		} 
	}

}