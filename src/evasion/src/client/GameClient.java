package client;

import java.util.regex.*;


public class GameClient extends Client {

	public GameClient(String host, int port) {
		super(host, port);
		// TODO Auto-generated constructor stub
	}


	protected String process(String[] command) {
		// TODO Auto-generated method stub
		parseHunt(command[1]); // hunt
		parsePrey(command[2]); // prey
		parseWall(command[3]); // wall
		if(command[0].equals("You are Hunter")){
			
		}else if(command[0].equals("You are Prey")){
			
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
		int x = Integer.parseInt(tokens[0]);
		int y = Integer.parseInt(tokens[1]);
		int delta_x = Integer.parseInt(tokens[2]);
		int delta_y = Integer.parseInt(tokens[3]);
		return;
	}


	private void parsePrey(String preyString) {
		// TODO Auto-generated method stub
		// the format is   Prey: 234 184
		String[] tokens = preyString.substring(7).split(" ");
		int x = Integer.parseInt(tokens[0]);
		int y = Integer.parseInt(tokens[1]);
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
		}
		return;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GameClient(args[0],Integer.parseInt(args[1]));
	}

}
