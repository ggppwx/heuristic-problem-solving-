
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;

public class Client {
	private static BufferedReader br;
	
	private static final int grid_len = 100;  // divide to grid_len * grid_len grid
	private static final int square_len = 1000/grid_len;
	
	private int g_num_turns;
	private int g_num_players;
	private int g_my_player;
	private int player_score[];
	private ArrayList<Position>[] player_coords;
	private square board[][];   // 100 * 100 
	
	private class square{
		public double score;
		public boolean picked;
	}
	
	private class playInfo{
		private int player;
		private double distance;
		playInfo(int player, double distance){
			this.setPlayer(player);
			this.setDistance(distance);
		}
		public int getPlayer() {
			return player;
		}
		public void setPlayer(int player) {
			this.player = player;
		}
		public double getDistance() {
			return distance;
		}
		public void setDistance(double distance) {
			this.distance = distance;
		}
	}
	
	public Client(String host, int port){
		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		player_score = new int[20];
		
		player_coords = (ArrayList<Position>[])new ArrayList[20];
		for(int i=0; i<20; ++i){
			player_coords[i] = new ArrayList<Position>();
		}
		
		//initialize the score of each square
		board = new square[grid_len][grid_len];
		for(int i=0 ; i< grid_len; ++i){
			for(int j=0; j< grid_len; ++j){
				board[i][j] = new square();
				board[i][j].picked = false;
				if(i<grid_len/2 && j<grid_len/2){
					board[i][j].score = evlScore(i,j,grid_len*4/10,grid_len*4/10);
				}
				if(i >= grid_len/2 && j<grid_len/2){
					board[i][j].score = evlScore(i,j,grid_len*6/10-1,grid_len*4/10);
				}
				if(i < grid_len/2 && j>=grid_len/2){
					board[i][j].score = evlScore(i,j,grid_len*4/10,grid_len*6/10-1);
				}
				if(i>=grid_len/2 && j >=grid_len/2){
					board[i][j].score = evlScore(i,j,grid_len*6/10-1,grid_len*6/10-1);
				}
				//System.out.print(board[i][j].score);
				//System.out.print(" ");
				
			}
			// System.out.print("\n");
		}
		
		try{
			socket = new Socket(host,port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(),true);
		}catch(IOException ev){
			System.err.println(ev.getMessage());
		}
			
		readSocket(in);
		writeSocket(out);
		
		for(int i = 1; i < g_num_turns; ++i){
			readSocket(in);
			writeSocket(out);

		}
		out.close();
		try{
			in.close();
			socket.close();

		}catch(IOException e){
			System.err.println(e.getMessage());
		}
		
	}
	
	private void readSocket(BufferedReader in){
	//	String content= null;
		String line;
		int stage = 1;
		try{
			while((line = in.readLine()) != null ){
				
				// System.out.println(line);
	//			content += line;
				
				String[] tokens = line.split(":| ");
			
				if(line.startsWith("Total Turns")){
					g_num_turns = Integer.parseInt(tokens[3]);
				}else if(line.startsWith("Total Players")){
					g_num_players = Integer.parseInt(tokens[3]);
				}else if(line.startsWith("You")){
					g_my_player = Integer.parseInt(tokens[4]);
				}else if(line.startsWith("PLAYER")){
					stage = 2;
					continue;
				}else if(line.startsWith("BOARD") ){
					stage = 3;
					continue;
				}
				//System.out.format("Info: %d %d %d", g_num_turns, g_num_players, g_my_player);
				if (2 == stage && 3 == tokens.length ){
					int index = Integer.parseInt(tokens[0]);
					int score = Integer.parseInt(tokens[2]);
					System.out.format("socre %d: %d\n", index, score);
					//TODO save score index, score to members 
					player_score[index] = score;
					
				}	
				if (3 == stage && 4 == tokens.length){
					int index = Integer.parseInt(tokens[0]);
					int x = Integer.parseInt(tokens[2]);
					int y = Integer.parseInt(tokens[3]);
					System.out.format("state %d: %d %d\n", index, x ,y);
					// TODO save state index, x, y to members. 
					//player_coords[index] = new ArrayList<Position>();
					player_coords[index].add(new Position(x, y));
					board[x/square_len][y/square_len].picked = true;
					
				}
				
				if (3 == stage && line.compareTo("") == 0){
					// the last line "Enter.....cannot be read !!!!!"
					break;
				}
						
			}
			// System.out.println("finish");
			
		}catch(IOException e){
			System.err.println(e.getMessage());
		}
		
	}
	
	private void writeSocket(PrintWriter out){
			// out.println(br.readLine());
		out.println(analyze());
	}
	
	
	private playInfo belongsTo(int posX, int posY,  ArrayList<Position>[] player_coords){ // find the square belongs to which player
		double min = 10000000;
		int player = -1;
		for(int i = 0; i<g_num_players; ++i){
			Iterator<Position> it =player_coords[i].iterator();
			while(it.hasNext()){
				Position p = it.next();
				double temp = Math.sqrt((Math.pow(posX - p.getX(),2)+Math.pow(posY - p.getY(),2))); 
				if (temp < min){
					min = temp;
					player = i;
				}
			}
		}
		assert player != -1;
		
		return new playInfo(player,min);
		
	}
	
	private double evlScore(int xPos, int yPos, int centerX, int centerY){ // x, y here is for the grid
		// evaluate the intrinsic score of the square
		double score_h = 20;
		double distance = Math.abs(xPos-centerX)+Math.abs(yPos-centerY);
		return (score_h - distance/10);
	}
	
	
	
	private String analyze(){
		// TODO   main logic!!
		// divide the board into 100 * 100  squares
		// only play in the center of the square. 
		// 1 - 10 , 11-20, .... 
		// brute force try. 
		double max_net_score = -1000000;
		int pickPosX = -1;
		int pickPosY = -1;
		
		for(int i =0; i< grid_len; ++i){
			for(int j=0; j< grid_len; ++j){ // try each square 
				//pick the square
				double score = board[i][j].score;
				if(board[i][j].picked){ // the square is picked, ignore it  
					continue;
				}
				//pick the score.
				board[i][j].picked = true;
				player_coords[g_my_player].add(new Position(square_len/2+i*square_len,square_len/2+j*square_len));
				//calculate the score 
						
				// get the score of all players
				double[] raw_player_score = new double[g_num_players];
				for(int x=0; x<grid_len; ++x){
					for(int y=0; y<grid_len; ++y){
						int centerX = square_len/2 + square_len*x;
						int centerY = square_len/2 + square_len*y;
						// int minDis = 0;
						playInfo player = belongsTo(centerX,centerY,player_coords);
						raw_player_score[player.getPlayer()] += board[x][y].score/(double)(player.getDistance()+1);
								
					}
				}
				
				double max_raw_score = 0;
				for(int k = 0; k< g_num_players; ++k){// find the opponent score player 	
					if (k != g_my_player && max_raw_score < raw_player_score[k]){
						max_raw_score = raw_player_score[k];
					}
				}
				assert max_raw_score != 0;
				double net_score = raw_player_score[g_my_player] - max_raw_score;
				if(max_net_score < net_score){
					max_net_score = net_score;
					pickPosX = square_len/2+i*square_len;
					pickPosY = square_len/2+j*square_len;
				}
				
				// trace back
				board[i][j].picked = false;
				player_coords[g_my_player].remove(player_coords[g_my_player].size()-1); 
				
				
			}
		}
		// pick the square with the highest net score
		assert max_net_score != -1000000;
		assert pickPosX != -1;
		assert pickPosY != -1;
		
		System.out.format("choose: %d %d\n", pickPosX, pickPosY);
		return Integer.toString(pickPosX)+" "+Integer.toString(pickPosY);
	}
	
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		br = new BufferedReader(new InputStreamReader(System.in));
		Client c= new Client(args[0],Integer.parseInt(args[1]));
	}
	
}


