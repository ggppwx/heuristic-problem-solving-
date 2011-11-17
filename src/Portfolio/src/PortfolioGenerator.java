import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PortfolioGenerator {

	public static List<Gamble> gambles = new ArrayList<Gamble>();

	public static List<Integer> favourates = new ArrayList<Integer>();

	public static List<Integer> unfavourates = new ArrayList<Integer>();

	public static void assignCategoryForGambles(int classNum) throws Exception {
		int seedF = (int)getARandomNumInRange(0, 10);
		int seedU = (int)getARandomNumInRange(0, 10);
		int seedN = (int)getARandomNumInRange(0, 10);
		int sum = seedF + seedU + seedN;
		int favourateNum = (int)getARandomNumInRange(0, (int)(classNum * seedF / sum));
		int unfavourateNum = (int)getARandomNumInRange(0, (int)(classNum * seedU / sum));
		List<Integer> classList = new LinkedList<Integer>();
		List<Integer> indexList = new LinkedList<Integer>();
		for (int i = 1; i <= classNum; i++) {
			classList.add(i);
			indexList.add(i);
		}
//		System.out.println("========favourates=========");
		for (int i = 0; i < favourateNum; i++) {
			int fid = (int)(Math.random() * indexList.size() - 1);
			favourates.add(classList.get(indexList.get(fid)));
			indexList.remove(fid);
		}
//		System.out.println("========unfavourates=========");
		for (int i = 0; i < unfavourateNum; i++) {
			int fid = (int)(Math.random() * indexList.size() - 1);
			unfavourates.add(classList.get(indexList.get(fid)));
			indexList.remove(fid);
		}
	}
	
	public static void gen(int gambleNum, int classNum, double expectedReturn) throws Exception {
		List<Double> rates = new ArrayList<Double>();
		double sum = 0;
		for (int i = 0; i < gambleNum; i++) {
			double r = getARandomNumInRange(1, 10);
			rates.add(r);
			sum += r;
		}
		for (int i = 0; i < gambleNum; i++) {
			double expect = rates.get(i) / sum * expectedReturn;
			Gamble gamble = generate(classNum, expect);
			gambles.add(gamble);
		}
	}

	public static Gamble generate(int classNum, double expectedReturn) throws Exception {
		Gamble gamble = new Gamble();
		int classId = (int)getARandomNumInRange(0, classNum - 1);
		gamble.classId = classId;
		gamble.id = gambles.size() + 1;

		gamble.medProb = getARandomNumInRange(0.4, 1);
		gamble.lowProb = getARandomNumInRange(0, 1 - gamble.medProb);
		gamble.highProb = 1 - gamble.medProb - gamble.lowProb;

		while (true) {
			double expectedReturnCopy = expectedReturn;
			gamble.high_return = getARandomNumInRange(0, expectedReturnCopy / gamble.highProb);
			expectedReturnCopy -= gamble.high_return * gamble.highProb;
			gamble.medium_return = getARandomNumInRange(0, Math.min(gamble.high_return, expectedReturnCopy / gamble.medProb));
			expectedReturnCopy -= gamble.medium_return * gamble.medProb;
			gamble.low_return = expectedReturnCopy / gamble.lowProb;
			if (gamble.high_return > gamble.medium_return
					&& gamble.medium_return > gamble.low_return
					&& gamble.low_return < 1) {
				break;
			}
		}
	    //System.out.println(gamble);
		return gamble;
	}

	public static double getARandomNumInRange(double l, double h) throws Exception {
		if (h < l) {
			throw new Exception("the high bound should be no less than the low bound!");
		}
		return Math.random() * (h - l) + l;
	}

	public static double getARandomNumLargerThanAPositive(double n) throws Exception {
		if (n < 0) {
			throw new Exception("N should be no smaller than 0!");
		}
		double highBound = getARandomNumInRange(n, Double.MAX_VALUE);
		return getARandomNumInRange(n, highBound);
	}
	
	public static boolean checkGambles(double expect) throws Exception {
		boolean b = false;
		double expectedReturn = 0;
		for (Gamble gamble : gambles) {
			if (gamble.medProb < .4) {
				throw new Exception("medProb should < 0.4");
			}
			if (!(gamble.high_return > gamble.medium_return
					&& gamble.medium_return > gamble.low_return)) {
				System.err.println("high_return=" +  gamble.high_return
						           + "\nmed_return=" + gamble.medium_return
						           + "\nlow_return=" + gamble.low_return);
				throw new Exception("return are not in a proper order");
			}
			expectedReturn += (gamble.high_return * gamble.highProb
					         + gamble.medium_return * gamble.medProb
					         + gamble.low_return * gamble.lowProb);
		}
		if (Math.abs(expect - expectedReturn) > 1) {
			System.err.println(expectedReturn);
			throw new Exception("wrong!");
		}
		return b;
	}

	public static boolean checkCategory() throws Exception {
//		System.out.println("favourates num=" + favourates);
//		for (Gamble gamble : gambles) {
//			if (favourates.contains(gamble.classId)) {
//				System.out.println("favourate " + gamble);
//			}
//		}
//		System.out.println("unfavourates num=" + unfavourates);
//		for (Gamble gamble : gambles) {
//			if (unfavourates.contains(gamble.classId)) {
//				System.out.println("unfavourate" + gamble);
//			}
//		}
		if (favourates.size() + unfavourates.size() > gambles.size()) {
			throw new Exception("category wrong!");
		}
		return true;
	}
	
	//public static 

	public static void main(String[] args) throws Exception {
		gen(200, 16, 200 * 2);
		checkGambles(400);
	    assignCategoryForGambles(16);
	    checkCategory();
	}
}
