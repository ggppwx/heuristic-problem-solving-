import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Algorithm {
	
	public Algorithm() {
	
	}
	
	public Algorithm(int mode, int gambleNum, int classNum){
		//  initialize the alg
		this.gambleNum = gambleNum;
		this.mode = mode;
		this.classNum = classNum;
		retStates = new ArrayList<double[]>();
		outcomes = new ArrayList<double[]>();
		gambleStates = new gamble[gambleNum];
		links = new int[gambleNum+1][gambleNum+1];
		classes = (List<Integer>[])new ArrayList[classNum];
		for(int i = 0; i<classes.length; ++i){
			classes[i] = new ArrayList<Integer>();
		}
	}
	
	public void readState(String stateStr){
		// read one state, actually only read the first line 
		if(stateStr.startsWith("gamble")){
			/*
			 * gamble
			   1,1,2,0.3,1.2,0.4,0.8,0.3
			   2,2,1.8,0.4,1.1,0.5,0.9,0.1
			   3,0,2,0.2,1,3,0.3,1.1,0.0.5

				link
				1,2	
				1,3

				Give allocation:
			 * */
			String[] lines = stateStr.split("\\r?\\n");
			int flag = -1;
			for(int i=0; i<lines.length; ++i){
				if(lines[i].startsWith("gamble")){
					flag = 1;
					continue;
				}else if(lines[i].startsWith("link")){
					flag = 2;
					continue;
				}else if(lines[i].compareTo("") == 0){
					flag = 3;
					continue;
				}
				if(1 == flag ){
					String[] tokens = lines[i].split(",");
					assert(tokens.length == 8);
					int id = Integer.parseInt(tokens[0]);
					int classId = Integer.parseInt(tokens[1]);
					double hRet = Double.parseDouble(tokens[2]);
					double hPro = Double.parseDouble(tokens[3]);
					double mRet = Double.parseDouble(tokens[4]);
					double mPro = Double.parseDouble(tokens[5]);
					double lRet = Double.parseDouble(tokens[6]);
					double lPro = Double.parseDouble(tokens[7]);
					gambleStates[id - 1] = new gamble(id,classId,hRet,hPro,mRet,mPro,lRet,lPro);
				}
				if(2 == flag){
					String[] tokens = lines[i].split(",");
					assert(tokens.length == 2);
					links[Integer.parseInt(tokens[0])][Integer.parseInt(tokens[1])] = 1;
					links[Integer.parseInt(tokens[1])][Integer.parseInt(tokens[0])] = 1;
				}
				
				
			}
			
			
		}else{
			String[] lines = stateStr.split("\\r?\\n");
			String[] ret = lines[0].split(",");
			assert(ret.length == gambleNum);
			double[] state = new double[gambleNum];
			for(int i=0; i<gambleNum; ++i){
				state[i] = Double.parseDouble(ret[i]);
			}
			retStates.add(state);
			// actual outcome for each gamble  
			String[] outcome = lines[1].split(",");
			assert(outcome.length == gambleNum);
			double[] state1 = new double[gambleNum];
			for(int i=0; i<gambleNum; ++i){
				state1[i] = Double.parseDouble(outcome[i]);
			}
			outcomes.add(state1);
		}
		
	}
	
	public String makeAllocation(){
		// main logic of the algorithm. make a guess of allocations in each gamble.
		double[] results;
		if(mode == 1){
			// play game 1
			results = playGameOne();
			
		}else{
			// play game 2
			results = playGameTwo();
		}
		assert(results.length == gambleNum);
		// return format: amount_on_gamble1,amount_on_gamble2,......................,amount_on_gambleN
		String resStr = new String();
		for(int i = 0; i<results.length-1; ++i){
			resStr += results[i]+",";
		}
		resStr += results[results.length-1];
		return resStr;
	}
	
	/*
	 * play the first game.
	 * give the coresponding allocations 
	 * */
	double[] playGameOne(){
		//  play first game
		groupByClass();
		int[] favorStat = calFavor();
		return distributeVal(1.0, favorStat);
	}
	
	/*
	 * play the second game 
	 * */
	double[] playGameTwo(){
		//  play second game
		groupByClass();
		int[] favorStat = calFavor(); // fa
		// the accumulated money. 
		double totalMoney = getCurrentTotalMoney();
		return distributeVal(totalMoney,favorStat); //dummy
	}
	
	
	/*
	 * group the gamble by its class
	 * save it to classes. 
	 * */
	void groupByClass(){
		//  put class1 to classes[0]
		for(int i = 0; i<gambleStates.length;++i){
			classes[gambleStates[i].classId].add(gambleStates[i].id);
		}
		
	}
	
	
	
	
	
	/*
	 * main logic calculation. 
	 * favor table starts from 0 
	 * */
	int[] calFavor(){
		int[] favorTable = new int[classNum];
		double[] hisRetAvg = processRetHis();
		for(int k = 0; k<classNum; ++k){ // for each class
			List<Integer> ls = classes[k]; 
			double[] diff = new double[3];
			for(int favor = -1; favor <=1; favor++){ // guess favored or unfavored
				Iterator<Integer> item = ls.iterator();
				while(item.hasNext()){ // for each gamble in this class
					// i is gamble index, starts from 0, calculate the predicted val. 
					int i = item.next() - 1;
					double predRet = getExpRet(gambleStates[i], favor);
					// actual history return
					double hisAvgRet = hisRetAvg[i];
					// get the diff for gamble i in the class 
					diff[favor+1] += Math.pow(predRet - hisAvgRet,2);
				}					
				
			}
			// select the favor for class k  
			favorTable[k] =  getMinIndex(diff) -1 ;
			
		}
		return favorTable; 
		
	}
	
	
	
	
	/*
	 * process return history
	 * get the expectation of all previous returns.
	 * the returns are in return states.  
	 * */
	double[] processRetHis(){
		double[] avgGambs = new double[gambleNum];
		if(retStates.isEmpty()){
			for(int i = 0; i< gambleStates.length; ++i){
				avgGambs[i] = gambleStates[i].highProb*gambleStates[i].high_return + gambleStates[i].medProb*gambleStates[i].medium_return+gambleStates[i].lowProb*gambleStates[i].low_return;
			}	
			return avgGambs;
		}
		
		Iterator<double[]> it = retStates.iterator();
		while(it.hasNext()){
			double[] gambs = it.next();
			for(int i = 0; i< gambs.length; ++i){
				avgGambs[i] += gambs[i];
			}			
		}
		for(int i = 0; i< avgGambs.length; ++i){
			avgGambs[i] = avgGambs[i]/retStates.size();
		}
		return avgGambs;
	}
	
	/*
	 * get expect return value. 
	 * favor:  0: neutral, 1: favored, -1:unfavored
	 * */
	double getExpRet(gamble g, int favor){
		double ret = -1;
		switch(favor){
		case 0:
			//neutral:
			ret = g.highProb*g.high_return+g.medProb*g.medium_return+g.lowProb*g.low_return;
			break;
		case 1:
			ret = (g.highProb+g.lowProb/2)*g.high_return+g.medProb*g.medium_return+(g.lowProb/2)*g.low_return;
			break;
		case -1:
			ret = (g.highProb/2)*g.high_return+g.medProb*g.medium_return+(g.lowProb+g.highProb/2)*g.low_return;
			break;
		default:
			//error;
			break;
		}
		return ret;
	}
	
	
	/*
	 * analyze the links of a gamble.
	 * the more favored gambles a gamble links to, the bigger chance the prob of it will be changed  
	 * return a correction factor 
	 * */
	double analyzeLinks(gamble g, int[] favorTable){
		int faCount = 0;
		int unfCount = 0;
		int neuCount = 0;
		for(int i = 1; i<=gambleNum; ++i){
			if(1 == links[g.id][i]){
				// gamble i links to gamble g. 
				switch(favorTable[gambleStates[i-1].classId]){
				case 0:
					// the linked gamble is neatural.
					neuCount ++;
					break;
				case -1:
					unfCount ++;
					break;
				case 1:
					// the linked gamble is favored. 
					//ret = (gambleStates[i-1].highProb+gambleStates[i-1].lowProb/2)*gambleStates[i-1].high_return
					//	+gambleStates[i-1].medProb*gambleStates[i-1].medium_return+(gambleStates[i-1].lowProb/2)*gambleStates[i-1].low_return;
					faCount ++;
					break;
				default:
					//error 
					break;
				
				}
				
			}
		}
		if(neuCount + unfCount + faCount == 0){
			// clean gamble. 
			return 2;
		}
		return (double)faCount/(double)(neuCount+unfCount+faCount)+2;
	}
	
	
	/*
	 * distribute amount money to each gamble
	 * the first version. 
	 * */
	double[] distributeVal(double amount, int[] favorTable){
		amount = amount ;//*0.999;
		double[] allocVal = new double[gambleNum];
		for(int i = 0; i<classNum; ++i){
			Iterator<Integer> it = classes[i].iterator();
			switch(favorTable[i]){
			case 0:	
				while(it.hasNext()){
					int gId = it.next(); // starts from 1; each gamble id in this class
					// TODO: use link as a parameter. of allocVal ??
					allocVal[gId - 1] = getExpRet(gambleStates[gId - 1], 0) * analyzeLinks(gambleStates[gId-1],favorTable);
				}
				break;
			case -1:
				while(it.hasNext()){
					int gId = it.next(); // starts from 1; each gamble id in this class
					allocVal[gId - 1] = getExpRet(gambleStates[gId - 1], -1)* analyzeLinks(gambleStates[gId-1],favorTable);
				}
				break;
			case 1:
				while(it.hasNext()){
					int gId = it.next(); // starts from 1; each gamble id in this class
					allocVal[gId - 1] = getExpRet(gambleStates[gId - 1], 1) * analyzeLinks(gambleStates[gId-1],favorTable);
				}
				break;
			default:
				break;
			}

		}
		double sum = 0.0;
		for(int i = 0; i<allocVal.length; ++i){
			assert(allocVal[i] != 0);
			
			// System.out.println(i);
			// System.out.println(allocVal[i]);
			//sum += df.format(allocVal[i]);
			sum = sum+ allocVal[i];
		}
		double sum1 = 0;
		for(int i = 0; i<allocVal.length; ++i){
			BigDecimal al = new BigDecimal(allocVal[i]);
			BigDecimal s = new BigDecimal(sum);
			BigDecimal ra = al.divide(s,5,BigDecimal.ROUND_DOWN);
			
			//double rate = allocVal[i]/sum;
			//DecimalFormat df = new DecimalFormat("#.####");
			//BigDecimal r = new BigDecimal(df.format(rate));
			BigDecimal a = new BigDecimal(amount);
			
			allocVal[i] = a.multiply(ra).doubleValue();
			sum1 += ra.doubleValue();
		}
		System.out.println(amount);
		System.out.println(sum1);
		
		return allocVal;
		
	}
	
	/*
	 * TODO: another way to allocate money??
	 * */
	
	
	
	
	
	

	/*
	 * guo's method
	 * */
	double[] distributeVal(double amount, List l){
		return new double[1]; // dummy.
	}
	
	/*
	 * get current total money
	 * */
	double getCurrentTotalMoney(){
		if(outcomes.isEmpty()){ // first round
			return 1.0;
		}
		double[] curOutcome = outcomes.get(outcomes.size() - 1);
		double sum = 0.0;
		assert(curOutcome.length == gambleNum);
		for(int i = 0; i<curOutcome.length; ++i){
			sum += curOutcome[i];
			
		}
		return sum;
	}
	
	/*
	 * private member variable
	 * */
	private int mode;
	private int gambleNum;
	private int classNum;
	private ArrayList<double[]> retStates; // return state of each round. 
	private ArrayList<double[]> outcomes; // outcome of each round
	private gamble[] gambleStates;  //is given before allocation
	private int[][] links;  // a matrix indicates the link condition.
	private List<Integer>[]  classes;  // class id starts from 0
	
	@SuppressWarnings("unused")
	private class gamble{
		int id;
		int classId;
		double high_return;
		double highProb;
		double medium_return;
		double medProb;
		double low_return;
		double lowProb;
		public gamble(int id, int classId,
			      	double high_return, double highProb,
			      	double medium_return, double medProb,
			      	double low_return, double lowProb)
		{
			this.id = id;
			this.classId = classId;
			this.high_return = high_return;
			this.highProb = highProb;
			this.medium_return = medium_return;
			this.medProb = medProb;
			this.low_return = low_return;
			this.lowProb = lowProb;
		}
	}
	
	/*
	 * get the index of minimum value in array 
	 * */
	public static int getMinIndex(double[] array){
		double min = 1000000000;
		int index = -1;
		for(int i = 0; i<array.length; ++i){
			if(array[i] < min){
				min = array[i];
				index = i;
			}
		}
		return index;
	}
	
}
