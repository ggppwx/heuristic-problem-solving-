
import java.io.*;
import java.util.*;

class Contestant extends NoTippingPlayer {
	private static BufferedReader br;
	private int Lsupport; 
	private int Rsupport;
	
	private int[] Board;
	private int[] Red;
	private int[] Blue;
	private int[] Green;
	
	private int[] optimalBlock;
	private int[] optimalMove;  // best move 
	
	private int[] optimalRemove;
	
	private int flag;
	
    Contestant(int port) {
		super(port);
		


	}   

	protected String process(String command) {
		Lsupport = -9;
		Rsupport = -3;
		
		Board = new int[31];   // from -15 to 15, which maps to 0 -30 in array
		for(int i=0;i<=30;++i){
			Board[i] = 0;
		}
		Red = new int[11];  // from 1 to 10
		for(int i=1;i<=10;++i){
			Red[i] = -100;
		}
		Blue = new int[11];
		for(int i=1;i<=10;++i){
			Blue[i] = -100;
		}
		optimalBlock = new int[20];
		optimalMove = new int[20];	
		
		optimalRemove = new int[20];

		System.out.println(command);  // print the current state
		parseState(command);
		System.out.println("Enter move (position weight): ");
		try {
			
			return analyze();
			//return br.readLine();  // change!!!!
			
			// TBD: return some sort of string, i.e. position weight
			// return analyze();
			
		} catch (Exception ev) {	
			System.out.println(ev.getMessage());
		}
		return "";		
		
		
	}
	
	/**
	 * parse the state, and put them into some data structure
	 * @param command
	 * **/
	private void parseState(String command){
        String str;
        BufferedReader buffer_reader = new BufferedReader(new StringReader(command));
        String delims = "[ ]+";
        try{
        while((str = buffer_reader.readLine()) != null){
        	String[] tokens = str.split(delims);
        	if(tokens[0].equals("ADDING")){
        		flag = 1;  // adding flag 
        	}else if(tokens[0].equals("REMOVING")){
        		flag = 2; // removing flag
        	}else{
        		int number = Integer.parseInt(tokens[0]);
        		int pos = Integer.parseInt(tokens[1]);
        		String color = tokens[2];
        		int block = Integer.parseInt(tokens[3]);
           		if (color.equals("Red") && number == 1){
        			Red[block] = pos;
        			Board[pos+15] = block;
        			Lsupport = Lsupport - block*(pos+3);
        			Rsupport = Rsupport - block*(pos+1);
        			
        		}else if(color.equals("Blue") && number == 1){
        			Blue[block] = pos;
        			Board[pos+15] = block;
        			Lsupport = Lsupport - block*(pos+3);
        			Rsupport = Rsupport - block*(pos+1);
        		}else if(color.equals("Green") && number == 1 ){
        			// Green[block] = pos;
        			Board[pos+15] = block; 
        			Lsupport = Lsupport - block*(pos+3);
        			Rsupport = Rsupport - block*(pos+1);
        		}

        	}
        }
        }catch(IOException ev){
        	System.out.println("reading error");
        }
        // 
        
	}   
		
	
	/**
	 * find all stable combination
	 */
	private ArrayList<Integer> findStable(int block){
		//TBD
		// we know current support value Lsupport Rsupport
		// check the board.
		ArrayList<Integer> que = new ArrayList<Integer>();
		for (int i = -15;i <= 15; ++i){
			if(Board[i+15] == 0){
				if ((Lsupport - block*(i+3))*(Rsupport - block*(i+1)) <= 0){
					// no tipping
					// put i into
					que.add(i);
				}
			}
		}
		return que;
		
	}
	
	/**
	 * get heristic score according to current state 
	 * @return
	 */
	private int heuristicScore(){  // for red
		int score = 0;
		int[][] set1 = {{-12,-4},{-7,-3},{-6,-2},{-5,-2},{-4,-2},{-4,-2},{-4,-2},{-4,-2},{-3,-2},{-3,-2}};
		for(int i=0;i<=30;++i){
			if(Board[i] != 0){
				int weight = Board[i];
				if(i-15 >= set1[weight-1][0] && i-15 <= set1[weight-1][1]){
					// in the set1 
					score --;
				}
				score ++; 
			}
		}
		//System.out.println(score);
		return score;
	}
	
	private int minMax(int weight, int pos, int step){  // starts from 0
		//TBD
		// I AM RED.    
		//minmax alg, N(weight,pos)
		//calulate posible moves in current state
		
		//if we put weight in pos, for next step, we have several ways to go. 
		// Board[pos+15] = weight;
		//System.out.println(step);
		
		int c=0;
		for(int i=0;i<=30;++i){
			if(Board[i]!=0){
				c++;
			}
		}
		if(c==20){
			return 0;  //tie
		}
		
		if (step == 5){
			// return a heuristic score 
			// System.out.println("return heuristic score");
			return heuristicScore();
		}
		
		
		
		if (step%2 == 0){
			// find successors
			// current player himself moves
			// max in minMax(w,p,1), w, p indicates possible move
			// max{minMax(w,p,step+1) .... }
			 
			// judge if it is leaf
			
			int maxScore = -1;
			for (int i=1;i<= 10;++i){ 
				if(Red[i] == -100){ // this block has not been used
					ArrayList<Integer> que = findStable(i);
					for (int move : que){ //posible move for block i 
						// int move = que[j];
						Board[move+15] = i;
						Red[i] = move;
						
						int temp = minMax(i,move,step+1);
						Board[move+15] = 0;
						Red[i] = -100;
						if(temp == 10){ // winning score
							maxScore = temp;
							optimalBlock[step] = i;
							optimalMove[step] = move;
							return maxScore;
						}
						
						if (temp > maxScore){
							maxScore = temp;
							optimalBlock[step] = i;
							optimalMove[step] = move;
						}
					}
				}
			}
			
			if (maxScore == -1){ // cannot find an possible solution, reaching the leaf
				System.out.println("reaching the leaf");
				return -10;  //lose 
			}
			
			//System.out.println(maxScore);
			return maxScore;

		}
		if (step%2 == 1){
			// adversary moves 
			// min in minMax()

			int minScore = 10000;
			for (int i=1;i<= 10;++i){
				if(Blue[i] == -100){
					ArrayList<Integer> que = findStable(i);
					for (int move : que){
						// int move = que[j];
						Board[move+15] = i;
						Blue[i] = move;
						int temp = minMax(i, move, step+1);
						//trace back 
						Board[move+15] = 0;
						Blue[i] = -100;
						
						if(temp == -10){ // losing score
							minScore = temp;
							optimalBlock[step] = i;
							optimalMove[step] = move;
							return minScore;
						}
						
						if(temp < minScore){
							minScore = temp;
							optimalBlock[step] = i;
							optimalMove[step] = move;
						}
					}
					
				}
			}
			
			if(minScore == 10000){ // adversary cannot find a solution, win 
				System.out.println("reaching leaf 2");
				return 10;
			}
			

			return minScore;
		}
		
		return 100000;
	}
	
	
	private int removing(int pos, int step){
		// check current state.
		if(step%2 == 0){
			int maxScore = -10;
			for(int i=0;i<=30;++i){
				if(Board[i] != 0){ 
					if(isStable(i-15)){
						int block = Board[i]; 
						Board[i] = 0;
						int temp = removing(i-15,step);
						Board[i] = block;
						if(temp > maxScore){
							maxScore = temp;
							optimalRemove[step] = i - 15;
						
						              
						}
					
					}
				}
			
			}
			
			return maxScore;
			
		}else{
			int minScore = 1000;
			for(int i=0;i<=30;++i){
				if(Board[i] != 0){
					if(isStable(i-15)){
						int block = Board[i]; 
						Board[i] = 0;
						int temp = removing(i-15,step);
						Board[i] = block;
						if (temp < minScore){
							minScore = temp;
							optimalRemove[step] = i -15;
							
						}
						
					}
				}
			}
			
			return minScore;
		}
		
	}
	
	/**
	 * check if the board is stable after removing block on position pos
	 * @param pos
	 * @return
	 */
	private boolean isStable(int pos){
		int block = Board[pos+15];
		if ((Lsupport + block*(pos+3))*(Rsupport + block*(pos+1)) <= 0){
			return true;
		}
		return false;
	}
	
	
	/**
	 * analyze the next move
	 * 
	 * @return string 
	 * **/
	protected String analyze(){
		//TBD
		// check in which phase it is
		if (flag == 1){  // adding phase
			int score = minMax(0,0,0);
			System.out.format("optimal block %d, move %d\n", optimalBlock[0],optimalMove[0]);
			// check optimalBlock[0], optimalMove[0]
			return Integer.toString(optimalMove[0]) + " " + Integer.toString(optimalBlock[0]);
		}else{ // removing phase
			
		}

		return "error";
	}
	
	
	
	
	

	
	public static void main(String[] args) throws Exception {
		br = new BufferedReader(new InputStreamReader(System.in));
		new Contestant(Integer.parseInt(args[0]));
		
		
	}
}
