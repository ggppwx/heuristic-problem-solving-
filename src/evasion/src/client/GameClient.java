package client;

import java.util.Random;
import java.util.regex.*;


public class GameClient extends Client {
	private static int M;
	private static int N;
	
	private int countM;
	private int countN;
	
	private class wallInfo{
		wallInfo(int x0,int y0,int x1,int y1){
			this.x0 = x0;
			this.y0 = y0;
			this.x1 = x1;
			this.y1 = y1;
		}
		int x0;
		int y0;
		int x1;
		int y1;
	}
	
	private class preyInfo{
		int x;
		int y;
	}
	
	private class huntInfo{
		int x;
		int y;
		int delta_x;
		int delta_y;
	}
	
	private class pair{
		pair(int first, int second){
			this.first = first;
			this.second = second;
		}
		int first;
		int second;
	}
	
	private wallInfo[] walls;
	private preyInfo prey;
	private huntInfo hunt;
	private int num_of_walls;
	
	private Random generator;
	
	public GameClient(String host, int port) {
		super(host, port);
		// TODO Auto-generated constructor stub
		generator = new Random();
		prey = new preyInfo();
		hunt = new huntInfo();
		walls = new wallInfo[100];
		num_of_walls = 0;
		countM = 0;
		countN = 0;
	}

	
	private String processPreyTest(){
		// random version prey.
		// for test only
		String[] dir = {"1 0", "-1 0","0 1","0 -1"};
		int index = generator.nextInt(4);  // 0 1 2 3
		System.out.println("prey move: "+dir[index]);
		return dir[index]; 
		
	}
	
	private void processPrey(){
		// TODO process the prey
		// all logic of prey should be written here
		
	}
	
	private pair farWallToPrey(){
		int maxesc = 0;
		int maxIndex = -1;
		for(int i = 0; i<num_of_walls; ++i){
			int wallDir = checkWall(walls[i]);
			int esc = -1;
			if(wallDir == 0){ // h
				esc = Math.abs(prey.y - walls[i].y0);
			}else if(wallDir == 1){// v
				esc = Math.abs(prey.x - walls[i].x0);
			}
			
			if(esc > maxesc){
				maxesc = esc;
				maxIndex = i;
			}
		}
		assert maxIndex != -1;
		return new pair(maxIndex,maxesc);
	}
	
	private int checkWall(wallInfo wall){
		// return 0 if horizontal
		if(wall.x0 - wall.x1 == 0){
			return 1;
		}
		if(wall.y0 - wall.y1 == 0){
			return 0;
		}
		return -1;
	}
	
	
	private void processHunt(){
		// TODO process the hunt
		// all logic of hunt should be written here
		
		// before move 1
		// the relative direction of prey to hunter 
		// judge if build a wall 
		int disX = prey.x - hunt.x;
		int disY = prey.y - hunt.y;
		// TODO test: always build the wall towards the axis whose dis is smaller.
		/*
		// if there'a wall right ahead. check if the prey is in the same side
		if(){ // wall ahead.
			if(){// check if in the same side.
				
			
			}else{// remove the wall. 
				
			}
		}
		*/
		// if no wall ahead.
		if(countN != 0){
			// skip build wall
		}
		
		if(disX>0 && disY >0){ // in district 1 
			// if close enough
			// build a wall if close enough 
			if(disX>disY && disY < 4){
				// build a wall towards x axis.
				// before building a wall. check 1) time count countN is 0. 2) walls left countM > 0  
				// if countN != 0. u cannot build a wall
				if(countM == M){ // reach the max number of walls
					// have to remove.
					pair wallIndex = farWallToPrey();  //		
					if(disY < wallIndex.second){
						// remove the wallIndex
						countM--;
					}
			
				}
				if(countM != M){ // build a wall
					
				}
			
			}
			if(disX<disY && disX < 4){
				// build a wall towards y axis
				if(countM == M){ // reach the max number of walls
					// have to remove.
					pair wallIndex = farWallToPrey();  //
					if(disX < wallIndex.second){
						// remove the wallIndex
						countM--;
					}
			
				}
				if(countM != M){ // build a wall
					
				}
				
			}
			// if disX = disY = 1, hunter catches the prey
			
		}else if(disX>0 && disY<0){  //in district 2
			// build wall
			if(disX < 4){
				if(countM == M){ // reach the max number of walls
					// have to remove.
					pair wallIndex = farWallToPrey();  //
					if(disX < wallIndex.second){
						// remove the wallIndex.first 
						countM--;
					}
			
				}
				if(countM != M){ // build a wall
					
				}
			}
			
		}else if(disX<0 && disY>0){ //district 3
			// build wall
			if(disY < 4){
				if(countM == M){ // reach the max number of walls
					// have to remove.
					pair wallIndex = farWallToPrey();  //
					if(disY < wallIndex.second){
						// remove the wallIndex
						countM--;
					}
			
				}
				if(countM != M){ // build a wall
					
				}
			}
		}else if(disX<0 && disY<0){ // district 4 
			// remove wall ,optional
			// build wall
			
		}else{ //in the same axis
			// cannot build wall 
			
		}
		
		
		
		// before move 2 
		
	}
	
	protected String process(String[] command) {
		// TODO Auto-generated method stub
		parseHunt(command[1]); // hunt
		parsePrey(command[2]); // prey
		parseWall(command[3]); // wall
		if(command[0].equals("You are Hunter")){
			
		}else if(command[0].equals("You are Prey")){
			return processPreyTest();
		}else{
			// error
			System.err.println("role not assigned");
		}

		
		return null;
	}

	private void parseHunt(String huntString) {
		// TODO Auto-generated method stub
		// the format is    Hunter: 23 93 -1 1
		String[] tokens = huntString.substring(7).split(" ");
		hunt.x = Integer.parseInt(tokens[0]);
		hunt.y = Integer.parseInt(tokens[1]);
		hunt.delta_x = Integer.parseInt(tokens[2]);
		hunt.delta_y = Integer.parseInt(tokens[3]);
		
		return;
	}


	private void parsePrey(String preyString) {
		// TODO Auto-generated method stub
		// the format is   Prey: 234 184
		String[] tokens = preyString.substring(7).split(" ");
		prey.x = Integer.parseInt(tokens[0]);
		prey.y = Integer.parseInt(tokens[1]);
		return;
	}


	private void parseWall(String wallString) {
		// TODO Auto-generated method stub
		// the format is    Walls: [0 (1, -1, 1, 500), 1 ()]
		Pattern p = Pattern.compile("[0-9]+ \\((1|-1), (1|-1), [0-9]+, [0-9]+\\)");
		Matcher m = p.matcher(wallString);
		while(m.find()){
			String record = m.group();
			System.out.println(record);
			walls[0] = new wallInfo(0,0,0,0);
			num_of_walls++;
	
		}
		return;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		M = 3;
		N = 3;
		new GameClient(args[0],Integer.parseInt(args[1]));
	}

}

