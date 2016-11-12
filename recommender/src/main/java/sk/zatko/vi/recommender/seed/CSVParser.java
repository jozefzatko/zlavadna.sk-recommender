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
	private String csvFile;
	
	protected CSVReader reader;
	
	public CSVParser(EntityManager entityManager, String csvFile) throws FileNotFoundException, UnsupportedEncodingException {
		
		this.entityManager = entityManager;
		this.csvFile = csvFile;
		
		reader = initReader();
	}
	
	protected CSVReader initReader() throws FileNotFoundException, UnsupportedEncodingException {
		
		return new CSVReader(new InputStreamReader(new FileInputStream(this.csvFile), "UTF-8"), CSV_DELIMITER, CSV_QUOTE_CHAR, 1);
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