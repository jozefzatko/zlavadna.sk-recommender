package sk.zatko.vi.recommender.seed;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.opencsv.CSVReader;

public class UsersSeed extends CSVParser {
	
	private static final String INSERT_QUERY =
			"INSERT INTO users (id)\n" +
			"SELECT :id\n" +
			"WHERE NOT EXISTS (SELECT * FROM users WHERE id = :id)";
	
	public UsersSeed(EntityManager entityManager, String train1CsvFile, String train2CsvFile) throws FileNotFoundException, UnsupportedEncodingException {
		
		super(entityManager, train1CsvFile, train2CsvFile);
	}

	@Override
	public void readAndStoreData() throws NumberFormatException, IOException {
		
		readAndStoreUser(this.train1Reader, this.entityManager);
		readAndStoreUser(this.train2Reader, this.entityManager);
	}
	
	private void readAndStoreUser(CSVReader reader, EntityManager entityManagers) throws NumberFormatException, IOException {
		
		EntityTransaction insertTransaction = entityManager.getTransaction();
		String[] csvLine;

		while ((csvLine = reader.readNext()) != null) {
			if (csvLine != null) {

				Integer user_id = Integer.parseInt(csvLine[1]);
				
				Query insertQuery = this.entityManager.createNativeQuery(INSERT_QUERY)
										.setParameter("id", user_id);
				
				insertTransaction.begin();
				insertQuery.executeUpdate();
				insertTransaction.commit();
			}
		}
		
		reader.close();
	}
}
