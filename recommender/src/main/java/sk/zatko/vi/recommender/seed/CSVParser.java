package sk.zatko.vi.recommender.seed;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.persistence.EntityManager;

import com.opencsv.CSVReader;

public abstract class CSVParser {
	
	private static final char CSV_DELIMITER = ',';
	private static final char CSV_QUOTE_CHAR = '"';
	
	protected EntityManager entityManager;
	
	protected CSVReader train1Reader;
	protected CSVReader train2Reader;
	protected CSVReader testReader;
	
	public CSVParser(EntityManager entityManager, String train1CsvFile, String train2CsvFile) throws FileNotFoundException, UnsupportedEncodingException {
		
		this.entityManager = entityManager;
		
		this.train1Reader = initReader(train1CsvFile);
		this.train2Reader = initReader(train2CsvFile);
	}
	
	public CSVParser(EntityManager entityManager, String train1CsvFile, String train2CsvFile, String testCsvFile) throws FileNotFoundException, UnsupportedEncodingException {
		
		this(entityManager, train1CsvFile, train2CsvFile);
		
		this.testReader = initReader(testCsvFile);
	}
	
	protected CSVReader initReader(String file) throws FileNotFoundException, UnsupportedEncodingException {
		
		return new CSVReader(new InputStreamReader(new FileInputStream(file), "UTF-8"), CSV_DELIMITER, CSV_QUOTE_CHAR, 1);
	}
	
	protected String[] removeNulls(String csvLine[]) {
		
		for (int i=0; i<csvLine.length; i++) {
			if ("null".equals(csvLine[i].toLowerCase())) {
				csvLine[i] = "";
			}
		}
		
		return csvLine;
	}
	
	public abstract void readAndStoreData() throws NumberFormatException, IOException; 
}
