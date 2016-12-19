package sk.zatko.vi.recommender.test;

public class PerformanceCapturer {

	private static long prepareQueryTime;
	private static long queryTime;
	private static long parseResultsTime;
	
	public static void reset() {
		
		prepareQueryTime = 0;
		queryTime = 0;
		parseResultsTime = 0;
	}
	
	public static void addToPrepareQueryTime(long value) {
		
		prepareQueryTime += value;
	}
	
	public static void addToQueryTime(long value) {
		
		queryTime += value;
	}
	
	public static void addToParseResultsTime(long value) {
		
		parseResultsTime += value;
	}

	
	public static long getPrepareQueryTime() {
		return prepareQueryTime;
	}

	public static long getQueryTime() {
		return queryTime;
	}

	public static long getParseResultsTime() {
		return parseResultsTime;
	}
	
}
