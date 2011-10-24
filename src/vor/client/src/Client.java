
import java.net.*;
import java.io.*;

public class Client {
	private static BufferedReader br;
	
	private int g_num_turns;
	private int g_num_players;
	private int g_my_player;
	private int player_score[];
	private Position player_coord[];
	private int board[][];   // 100 * 100 
	
	Client(String host, int port){
		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;
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
					new Position(x, y);
					board[x/10][y/10];
					int centerX = 5+ x/10 *10; 
					int centerY = 5+ y/10 *10;  
					int distanceToC = ;
					if (distanceToC < board[x/10][y/10].minDis){
						board[x/10][y/10].minDis = distanceToC;
						board[x/10][y/10].player = index; // the square is occupied. 
					}
					
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
		try{
			out.println(br.readLine());
			// TODO out.println(analyze());
			
		}catch(IOException e){
			System.err.println(e.getMessage());
		}
	}
	
	private String analyze(){
		// TODO   main logic!!
		// divide the board into 100 * 100  squares
		// only play in the center of the square. 
		// 1 - 10 , 11-20, .... 
		// brute force try. 
		
		//initialize the score of each square
		
		
		
		
		for(int i =0; i< 100; ++i){
			for(int j=0; j< 100; ++j){ // try each square 
				int score = board[i][j].score;
				
				// get the max score of all players 
				
				
				
				
			}
		}
		
		
		
		
		return "";
	}
	
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		br = new BufferedReader(new InputStreamReader(System.in));
		Client c= new Client(args[0],Integer.parseInt(args[1]));
	}
	
}


