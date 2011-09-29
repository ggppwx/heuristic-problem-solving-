
import java.io.*;


class Contestant extends NoTippingPlayer {
	private static BufferedReader br;

	Contestant(int port) {
		super(port);
	}

	protected String process(String command) {
		System.out.println(command);  // print the current state
		System.out.println("Enter move (position weight): ");
		try {
			/** deprecated
			return br.readLine();  // change!!!!
			*/
			
			// TBD: return some sort of string, i.e. position weight
			return analyze();
			
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
		//TBD
	}
	
	
	/**
	 * analyze the next move
	 * 
	 * @return string 
	 * **/
	protected string analyze(){
		//TBD
		
	}
	
	
	
	
	

	public static void main(String[] args) throws Exception {
		br = new BufferedReader(new InputStreamReader(System.in));
		new Contestant(Integer.parseInt(args[0]));
		
		
	}
}
