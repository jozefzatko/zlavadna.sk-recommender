package sk.zatko.vi.recommender.seed;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import sk.zatko.vi.recommender.models.DealItem;

public class DealItemsSeed extends CSVParser {
	
	private static final String INSERT_QUERY_TO_TRAIN =
			"INSERT INTO dealitems (id, deal_id, title, coupon_text1, coupon_text2, in_train, in_test, coupon_begin_date, coupon_end_date)\n" + 
			"SELECT :id, :deal_id, :title, :coupon_text1, :coupon_text2, true, false, :coupon_begin_date, :coupon_end_date\n" + 
			"WHERE NOT EXISTS (SELECT * FROM dealitems WHERE id = :id)\n" + 
			"AND EXISTS (SELECT * FROM deals WHERE id = :deal_id)";
	
	private static final String INSERT_QUERY_TO_TEST =
			"INSERT INTO dealitems (id, deal_id, title, coupon_text1, coupon_text2, in_train, in_test, coupon_begin_date, coupon_end_date)\n" + 
			"SELECT :id, :deal_id, :title, :coupon_text1, :coupon_text2, false, true, :coupon_begin_date, :coupon_end_date\n" + 
			"WHERE NOT EXISTS (SELECT * FROM dealitems WHERE id = :id)\n" + 
			"AND EXISTS (SELECT * FROM deals WHERE id = :deal_id)";
	
	public DealItemsSeed(EntityManager entityManager, String train1CsvFile, String train2CsvFile, String testCsvFile) throws FileNotFoundException, UnsupportedEncodingException {
		
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
				Integer deal_id = Integer.parseInt(csvLine[1]);
				String title_dealitem = csvLine[2];
				String coupon_text1 = csvLine[3];
				String coupon_text2 = csvLine[4];
				Date coupon_begin_date = new Date(Long.parseLong(csvLine[5]) * 1000);
				Date coupon_end_date = new Date(Long.parseLong(csvLine[6]) * 1000);
				
				if (title_dealitem.startsWith("test") || title_dealitem.length() < 3) {
					continue;
				}
				
				Query insertQuery = this.entityManager.createNativeQuery(INSERT_QUERY_TO_TRAIN)
										.setParameter("id", id)
										.setParameter("deal_id", deal_id)
										.setParameter("title", title_dealitem)
										.setParameter("coupon_text1", coupon_text1)
										.setParameter("coupon_text2", coupon_text2)
										.setParameter("coupon_begin_date", coupon_begin_date)
										.setParameter("coupon_end_date", coupon_end_date);
				
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
				Integer deal_id = Integer.parseInt(csvLine[1]);
				String title_dealitem = csvLine[2];
				String coupon_text1 = csvLine[3];
				String coupon_text2 = csvLine[4];
				Date coupon_begin_date = new Date(Long.parseLong(csvLine[5]) * 1000);
				Date coupon_end_date = new Date(Long.parseLong(csvLine[6]) * 1000);
				
				if (title_dealitem.startsWith("test") || title_dealitem.length() < 3) {
					continue;
				}
				
				Query insertQuery = this.entityManager.createNativeQuery(INSERT_QUERY_TO_TRAIN)
										.setParameter("id", id)
										.setParameter("deal_id", deal_id)
										.setParameter("title", title_dealitem)
										.setParameter("coupon_text1", coupon_text1)
										.setParameter("coupon_text2", coupon_text2)
										.setParameter("coupon_begin_date", coupon_begin_date)
										.setParameter("coupon_end_date", coupon_end_date);
				
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
				
				DealItem item = this.entityManager.find(DealItem.class, id);
				if (item != null) {
					
					transaction.begin();
					item.setInTest(true);
					transaction.commit();
					continue;
				}
				
				Integer deal_id = Integer.parseInt(csvLine[1]);
				String title_dealitem = csvLine[2];
				String coupon_text1 = csvLine[3];
				String coupon_text2 = csvLine[4];
				Date coupon_begin_date = new Date(Long.parseLong(csvLine[5]) * 1000);
				Date coupon_end_date = new Date(Long.parseLong(csvLine[6]) * 1000);
				
				if (title_dealitem.startsWith("test") || title_dealitem.length() < 3) {
					continue;
				}
				
				Query insertQuery = this.entityManager.createNativeQuery(INSERT_QUERY_TO_TEST)
										.setParameter("id", id)
										.setParameter("deal_id", deal_id)
										.setParameter("title", title_dealitem)
										.setParameter("coupon_text1", coupon_text1)
										.setParameter("coupon_text2", coupon_text2)
										.setParameter("coupon_begin_date", coupon_begin_date)
										.setParameter("coupon_end_date", coupon_end_date);
				
				transaction.begin();
				insertQuery.executeUpdate();
				transaction.commit();
			}
		}
		testReader.close();
	}
}
