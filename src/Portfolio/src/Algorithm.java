import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Algorithm {
	
	public Algorithm() {
		// TODO Auto-generated constructor stub
	}
	
	public Algorithm(int mode, int gambleNum, int classNum){
		// TODO initialize the alg
		this.gambleNum = gambleNum;
		this.mode = mode;
		this.classNum = classNum;
		retStates = new ArrayList<double[]>();
		outcomes = new ArrayList<double[]>();
		gambleStates = new gamble[gambleNum];
		links = new int[classNum+1][classNum+1];
		classes = (List<Integer>[])new ArrayList[classNum];
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
			// TODO actual outcome for each gamble  
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
		// TODO main logic of the algorithm. make a guess of allocations in each gamble.
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
		// TODO play first game
		groupByClass();
		int[] favorStat = calFavor();
		return distributeVal(1.0, favorStat);
	}
	
	/*
	 * play the second game 
	 * */
	double[] playGameTwo(){
		// TODO play second game
		groupByClass();
		int[] favorStat = calFavor();
		// the accumulated money. 
		double totalMoney = getCurrentTotalMoney();
		return distributeVal(totalMoney,favorStat); //dummy
	}
	
	
	/*
	 * group the gamble by its class
	 * save it to classes. 
	 * */
	void groupByClass(){
		// TODO: put class1 to classes[0]
	}
	
	
	
	
	
	/*
	 * main logic calculation. 
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
					diff[favor+1] += predRet - hisAvgRet;
				}					
				
			}
			// select the favor for class k  
			favorTable[k] =  getMaxIndex(diff) -1 ;
			
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
	 * distribute amount money to each gamble
	 * the first version. 
	 * */
	double[] distributeVal(double amount, int[] favorTable){
		double[] allocVal = new double[gambleNum];
		for(int i = 0; i<classNum; ++i){
			Iterator<Integer> it = classes[i].iterator();
			switch(favorTable[i]){
			case 0:	
				while(it.hasNext()){
					int gId = it.next(); // starts from 1; each gamble id in this class
					allocVal[gId - 1] = getExpRet(gambleStates[gId - 1], 0);
				}
				break;
			case -1:
				while(it.hasNext()){
					int gId = it.next(); // starts from 1; each gamble id in this class
					allocVal[gId - 1] = getExpRet(gambleStates[gId - 1], -1);
				}
				break;
			case 1:
				while(it.hasNext()){
					int gId = it.next(); // starts from 1; each gamble id in this class
					allocVal[gId - 1] = getExpRet(gambleStates[gId - 1], 1);
				}
				break;
			default:
				break;
			}

		}
		double sum = 0;
		for(int i = 0; i<allocVal.length; ++i){
			assert(allocVal[i] != 0);
			sum +=allocVal[i];
		}
		for(int i = 0; i<allocVal.length; ++i){
			allocVal[i] = allocVal[i]/sum;
		}
		
		return allocVal;
		
	}
	
	/*
	 * guo's method
	 * */
	double[] distributeVal(double amount, List l){
		return new double[1]; // dummy.
	}
	
	
	double getCurrentTotalMoney(){
		// TODO: 
		return 1.0;
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
	private List<Integer>[]  classes;
	
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
	 * get the index of maximum value in array 
	 * */
	public static int getMaxIndex(double[] array){
		double max = -1;
		int index = -1;
		for(int i = 0; i<array.length; ++i){
			if(array[i] > max){
				max = array[i];
				index = i;
			}
		}
		return index;
	}
	
}
