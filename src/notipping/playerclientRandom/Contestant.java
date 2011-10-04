

import java.io.*;
import java.util.*;
public class Contestant extends NoTippingPlayer {
	private static BufferedReader br;
	private String mycolor;

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
    
    
	public Contestant(int port) {
		super(port);

	}

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
        
        
        parseState(command);
        
		List<String> myCommands = new LinkedList<String>(Arrays.asList((command.split("\n"))));

		if (myCommands.get(0).equals("ADDING"))
		{
			myCommands.remove(0);
			return makeAnAdd(myCommands);
		} 
		try {
			return br.readLine();
			
		} catch (Exception ev) {
			System.out.println(ev.getMessage());
		}
		return "";			
	}
    
    private boolean isStable(int weight, int pos)
    {
     
				if ((Lsupport - weight*(pos+3))*(Rsupport - weight*(pos+1)) <= 0){
					// no tipping
					// put i into
					return true;
				}
        return false;
    }

	private String makeAnAdd(List<String> command)
	{
		
		if (mycolor == null) {
			init(command);
		}
		System.out.println("i am " + mycolor);
		Map<Integer,BoardPosition> myPos = new HashMap<Integer,BoardPosition>(30);
		List<Weight> avWeights = new ArrayList<Weight>();
		List<Integer> avSpots = new ArrayList<Integer>();

		for (String s: command) {
			
			List<String> myCommands = new ArrayList<String>(Arrays.asList((s.split(" "))));
			int position = Integer.valueOf(myCommands.get(1));
			int weight = Integer.valueOf(myCommands.get(3));
			String weightcolor = myCommands.get(2);
			if (weightcolor.equals(mycolor))
			{
				if (position == 0)
					avWeights.add(new Weight (mycolor,weight,false));
				
			}
			if (position != 0 )
				myPos.put(position, new BoardPosition(null,weight));
		}
		
		for (int i = -15;i<=15;i++)
		{
			if (myPos.get(i)==null)
				avSpots.add(i);
		}
		
		Random generator = new Random();
        int destination;
        int weight;
		do{
           destination = avSpots.get(generator.nextInt(avSpots.size()));
		   weight = avWeights.get(generator.nextInt(avWeights.size())).weight;
        }while(isStable(weight, destination) != true);
        
		System.out.println(destination + " " + weight);
		return new String(destination + " " + weight);
	
	}		
	
	
	private void init(List<String> command)
	{

		boolean allEmpty = true;
		for (String s: command) {
			List<String> myCommands = new ArrayList<String>(Arrays.asList((s.split(" "))));
			int position = Integer.valueOf(myCommands.get(1));
			String color = myCommands.get(2);
			if (color.equals("Red") || color.equals("Blue"))
				if (position != 0)
					allEmpty = false;
		}
		if (allEmpty) { mycolor = "Red";} else {mycolor = "Blue";}
	}
	public static void main(String[] args) throws Exception {
		//
		new Contestant(Integer.parseInt(args[0]));
		br = new BufferedReader(new InputStreamReader(System.in));
	}
	class Weight
	{
		String color;
		int weight;
		boolean used;
		Weight(String color, int weight,boolean used)
		{
			this.color = color;
			this.weight = weight;
			this.used = used;
		}
		
	}
	class BoardPosition
	{
		Weight weight;
		int position;
		BoardPosition(Weight w,int p)
		{
			weight = w;
			position = p;
		}
	}
}


