package sk.zatko.vi.recommender.seed;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

public class DealItemsSeed extends CSVParser {
	
	private static final String INSERT_QUERY =
			"INSERT INTO dealitems (id, deal_id, title, coupon_text1, coupon_text2, coupon_begin_date, coupon_end_date)\n" + 
			"SELECT :id, :deal_id, :title, :coupon_text1, :coupon_text2, :coupon_begin_date, :coupon_end_date\n" + 
			"WHERE NOT EXISTS (SELECT * FROM dealitems WHERE id = :id)\n" + 
			"AND EXISTS (SELECT * FROM deals WHERE id = :deal_id)";
	
	public DealItemsSeed(EntityManager entityManager, String csvFile) throws FileNotFoundException, UnsupportedEncodingException {
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
				Integer deal_id = Integer.parseInt(csvLine[1]);
				String title_dealitem = csvLine[2];
				String coupon_text1 = csvLine[3];
				String coupon_text2 = csvLine[4];
				Date coupon_begin_date = new Date(Long.parseLong(csvLine[5]) * 1000);
				Date coupon_end_date = new Date(Long.parseLong(csvLine[6]) * 1000);
				
				if (title_dealitem.startsWith("test") || title_dealitem.length() < 3) {
					continue;
				}
				
				Query insertQuery = this.entityManager.createNativeQuery(INSERT_QUERY)
										.setParameter("id", id)
										.setParameter("deal_id", deal_id)
										.setParameter("title", title_dealitem)
										.setParameter("coupon_text1", coupon_text1)
										.setParameter("coupon_text2", coupon_text2)
										.setParameter("coupon_begin_date", coupon_begin_date)
										.setParameter("coupon_end_date", coupon_end_date);
				
				insertTransaction.begin();
				insertQuery.executeUpdate();
				insertTransaction.commit();
			}
		} 
	}
}