

import java.io.*;
import java.util.*;
public class Contestant extends NoTippingPlayer {
	private static BufferedReader br;
	private String mycolor;

	public Contestant(int port) {
		super(port);

	}

	protected String process(String command) {
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
		int destination = avSpots.get(generator.nextInt(avSpots.size()));
		int weight = avWeights.get(generator.nextInt(avWeights.size())).weight;
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


