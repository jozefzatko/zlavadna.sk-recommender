package sk.zatko.vi.recommender.exceptions;

public class NoUserHistoryException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5830690217095832171L;
	
	public NoUserHistoryException(String s) {
		
		super(s);
	}
	
	public NoUserHistoryException(String s, Exception e) {
		
		super(s, e);
	}
}
