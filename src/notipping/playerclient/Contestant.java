
import java.io.*;


class Contestant extends NoTippingPlayer {
	private static BufferedReader br;
	private int Lsupport; 
	private int Rsupport;
	
	
    Contestant(int port) {
		super(port);
		Lsupport = -6;
		Rsupport = 6;
	}   

	protected String process(String command) {
		System.out.println(command);  // print the current state
		parseState(command);
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
        String str;
        BufferedReader buffer_reader = BufferedReader(new StringReader(command));
        String delims = "[ ]+";
        while((str = buffer_reader.readLine()) != null){
        	String[] tokens = str.split(delims);
        	if(tokens[0].equals("ADDING")){
        		flag = 1;  // adding flag 
        	}else if(tokens[0].equals("REMOVING")){
        		flag = 2 // removing flag
        	}else{
        		int number = Integer.parseInt(tokens[0]);
        		int pos = Integer.parseInt(tokens[1]);
        		String color = tokens[2];
        		int block = Integer.parseInt(tokens[3]);
           		if (color.equals("Red") && number == 1){
        			Red[block] = pos;
        			Board[pos] = block;
        			Lsupport = Lsupport - block*(pos+3);
        			Rsupport = Rsupport - block*(pos+1);
        			
        		}else if(color.equals("Blue") && number == 1){
        			Blue[block] = pos;
        			Board[pos] = block;
        			Lsupport = Lsupport - block*(pos+3);
        			Rsupport = Rsupport - block*(pos+1);
        		}else if(color.equals("Green") && number == 1 ){
        			Green[block] = pos;
        			Board[pos] = block; 
        			Lsupport = Lsupport - block*(pos+3);
        			Rsupport = Rsupport - block*(pos+1);
        		}

        	}
        }
        // 
        
	}   
		
	
	/**
	 * find all stable combinatio
	 */
	private void findStable(int block){
		//TBD
		// we know current support value Lsupport Rsupport
		// check the board.   
		
		for (int i = -15;i <= 15; ++i){
			if(Board[i] == 0){
				if ((Lsupport - block*(i+3))*(Rsupport - block*(i+1)) <= 0){
					// no tipping
					// put i into
					que.push(i);
				}
				
			}
		}
		return que;
		
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
