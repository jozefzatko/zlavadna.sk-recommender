package sk.zatko.vi.recommender;

import java.util.ArrayList;

import sk.zatko.vi.recommender.recommend.CollaborativeRecommender;
import sk.zatko.vi.recommender.recommend.CombineContentBasedRecommender;
import sk.zatko.vi.recommender.recommend.PopularityRecommender;
import sk.zatko.vi.recommender.recommend.Recommender;
import sk.zatko.vi.recommender.recommend.SimpleContentBasedRecommender;
import sk.zatko.vi.recommender.recommend.UserHistoryContentBasedRecommender;
import sk.zatko.vi.recommender.test.RandomUserGenerator;
import sk.zatko.vi.recommender.test.RecommenderTester;

public class TestRunner {

	private static Recommender simpleContentBasedRecommender = new SimpleContentBasedRecommender();
	private static Recommender userHistoryContentBasedRecommender = new UserHistoryContentBasedRecommender();
	private static Recommender combineContentBasedRecommender = new CombineContentBasedRecommender();
	private static Recommender collaborativeRecommender = new CollaborativeRecommender();
	private static Recommender popularityRecommender = new PopularityRecommender();
	
	public static void main(String args[]) {
		
		new TestRunner().test();
		
		System.exit(0);
	}
	
	public void test() {
		
		for (int i=0; i<5; i++) {
			
			testAll(5);
			testAll(10);
			testAll(25);
		}
	}
	
	public void testAll(int countOfResults) {
		
		ArrayList<Integer> users = prepareUsers();
		
		new RecommenderTester(simpleContentBasedRecommender, users).test(countOfResults);
		new RecommenderTester(userHistoryContentBasedRecommender, users).test(countOfResults);
		new RecommenderTester(combineContentBasedRecommender, users).test(countOfResults);
		new RecommenderTester(collaborativeRecommender, users).test(countOfResults);
		new RecommenderTester(popularityRecommender, users).test(countOfResults);
	}
	
	private ArrayList<Integer> prepareUsers() {
		
		ArrayList<Integer> users = new ArrayList<Integer>();
		
		users.addAll(RandomUserGenerator.get(300, 2));
		users.addAll(RandomUserGenerator.get(300, 3));
		users.addAll(RandomUserGenerator.get(250, 4));
		users.addAll(RandomUserGenerator.get(100, 5));
		users.addAll(RandomUserGenerator.get(30, 6));
		users.addAll(RandomUserGenerator.get(20, 7));
		
		return users;
	}
}
