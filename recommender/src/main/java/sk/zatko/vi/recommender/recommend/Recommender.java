package sk.zatko.vi.recommender.recommend;

import java.util.ArrayList;

public abstract class Recommender {

	protected static final String INDEX_NAME = "deals";
	protected static final String DOCUMENT_TYPE = "deal";
	
	public abstract ArrayList<Integer> recommend(int currentuserId, int currentDealId, int countOfResults);
}
