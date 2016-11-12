package sk.zatko.vi.recommender.seed;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

public class ActivitiesSeed extends CSVParser {
	
	private static final String INSERT_QUERY =
			"INSERT INTO activities (id, user_id, deal_item_id, deal_id, quantity, price, created_at)\n" + 
			"SELECT :id, :user_id, :deal_item_id, :deal_id, :quantity, :price, :created_at\n" + 
			"WHERE NOT EXISTS (SELECT * FROM activities WHERE id = :id)\n" + 
			"AND EXISTS (SELECT * FROM dealitems WHERE id = :deal_item_id)\n" + 
			"AND EXISTS (SELECT * FROM deals WHERE id = :deal_id)";
	
	public ActivitiesSeed(EntityManager entityManager, String csvFile) throws FileNotFoundException, UnsupportedEncodingException {
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
				Integer user_id = Integer.parseInt(csvLine[1]);
				Integer dealitem_id = Integer.parseInt(csvLine[2]);
				Integer deal_id = Integer.parseInt(csvLine[3]);
				Integer quantity = Integer.parseInt(csvLine[4]);
				Double market_price = "".equals(csvLine[5]) ? null : Double.parseDouble(csvLine[5]);
				Date create_time = new Date(Long.parseLong(csvLine[6]) * 1000);
				
				Query insertQuery = this.entityManager.createNativeQuery(INSERT_QUERY)
										.setParameter("id", id)
										.setParameter("user_id", user_id)
										.setParameter("deal_item_id", dealitem_id)
										.setParameter("deal_id", deal_id)
										.setParameter("quantity", quantity)
										.setParameter("price", market_price)
										.setParameter("created_at", create_time);
									
				insertTransaction.begin();
				insertQuery.executeUpdate();
				insertTransaction.commit();
			}
		}
	}
}
