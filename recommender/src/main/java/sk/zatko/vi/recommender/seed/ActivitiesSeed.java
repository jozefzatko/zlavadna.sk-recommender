package sk.zatko.vi.recommender.seed;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.opencsv.CSVReader;

public class ActivitiesSeed extends CSVParser {
	
	private static final String INSERT_QUERY =
			"INSERT INTO activities (id, user_id, deal_item_id, deal_id, quantity, market_price, team_price, discount, created_at, in_train, in_test)\n" + 
			"SELECT :id, :user_id, :deal_item_id, :deal_id, :quantity, :market_price, :team_price, :discount, :created_at, :in_train, :in_test\n" + 
			"WHERE NOT EXISTS (SELECT * FROM activities WHERE id = :id)\n" + 
			"AND EXISTS (SELECT * FROM dealitems WHERE id = :deal_item_id)\n" + 
			"AND EXISTS (SELECT * FROM deals WHERE id = :deal_id)";
	
	public ActivitiesSeed(EntityManager entityManager, String train1CsvFile, String train2CsvFile) throws FileNotFoundException, UnsupportedEncodingException {
		
		super(entityManager, train1CsvFile, train2CsvFile);
	}

	@Override
	public void readAndStoreData() throws NumberFormatException, IOException {
		
		readAndStoreActivities(this.train1Reader, this.entityManager, true);
		readAndStoreActivities(this.train2Reader, this.entityManager, true);
	}
	
	private void readAndStoreActivities(CSVReader reader, EntityManager entityManager, boolean isTrain) throws NumberFormatException, IOException {
		
		EntityTransaction insertTransaction = entityManager.getTransaction();
		
		String[] csvLine;

		while ((csvLine = reader.readNext()) != null) {
			if (csvLine != null) {
				
				csvLine = removeNulls(csvLine);
				
				Integer id = Integer.parseInt(csvLine[0]);
				Integer user_id = Integer.parseInt(csvLine[1]);
				Integer dealitem_id = Integer.parseInt(csvLine[2]);
				Integer deal_id = Integer.parseInt(csvLine[3]);
				Integer quantity = Integer.parseInt(csvLine[4]);
				Double market_price = "".equals(csvLine[5]) ? null : Double.parseDouble(csvLine[5]);
				Double team_price = "".equals(csvLine[6]) ? null : Double.parseDouble(csvLine[6]);
				Date create_time = new Date(Long.parseLong(csvLine[7]) * 1000);
				
				Double discount = null;
				if (market_price != null && team_price != null) {
					discount = 1 - (team_price / market_price);
				}
				
				Query insertQuery = this.entityManager.createNativeQuery(INSERT_QUERY)
										.setParameter("id", id)
										.setParameter("user_id", user_id)
										.setParameter("deal_item_id", dealitem_id)
										.setParameter("deal_id", deal_id)
										.setParameter("quantity", quantity)
										.setParameter("market_price", market_price)
										.setParameter("team_price", team_price)
										.setParameter("discount", discount)
										.setParameter("created_at", create_time);
				
				if (isTrain) {
					insertQuery.setParameter("in_train", true);
					insertQuery.setParameter("in_test", false);
				} else {
					insertQuery.setParameter("in_train", false);
					insertQuery.setParameter("in_test", true);
				}
									
				insertTransaction.begin();
				insertQuery.executeUpdate();
				insertTransaction.commit();
			}
		}
		
		reader.close();
	}
}
