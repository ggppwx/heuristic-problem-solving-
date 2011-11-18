import java.util.ArrayList;


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
		gambleStates = new gamble[gambleNum];
		links = new int[classNum+1][classNum+1];
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
	 * */
	double[] playGameOne(){
		// TODO play first game
		return new double[gambleNum];  //dummy
	}
	
	/*
	 * play the second game 
	 * */
	double[] playGameTwo(){
		// TODO play second game
		return new double[gambleNum]; //dummy
	}
	
	
	
	/*
	 * private member 
	 * */
	private int mode;
	private int gambleNum;
	private int classNum;
	private ArrayList<double[]> retStates; // return state of each round. 
	private gamble[] gambleStates;  //is given before allocation
	private int[][] links;  // a matrix indicates the link condition.
	
	
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
	
}
