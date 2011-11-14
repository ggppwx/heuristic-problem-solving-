
package datingclient;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Buce
 */
public class Datingclient {

    public static String machineName;
    public static int port;
    public static int noOfArrtributes;
    
    public static void main(String[] args) throws IOException {
        
    	machineName = args[0];
    	port = Integer.parseInt(args[1]);
    	noOfArrtributes = Integer.parseInt(args[2]);
         
        socketConnection oS = new socketConnection(machineName, Integer.valueOf(port));
	
        Algorithm a = new Algorithm(noOfArrtributes); //init the algorithm

        for(int i=0;i<22;i++){
        	String input = oS.socketRead();
        	System.out.println(input);
        	a.readCandidates(input);
	
        }
        
        for(int l=0;l<20;l++){
        String temp;
        // temp = generateRandomCandidates(noOfArrtributes).getValues().toString();
        // TODO: generate a candidate here. 
        temp = a.generateCandidate().getValues().toString();
        oS.socketWrite(temp);
        System.out.println(temp);
        
        String input = oS.socketRead();
        System.out.println(input);
        a.readCandidates(input);
	       
        String te = oS.socketRead();
        System.out.println(te);
        // format: give candidate2:
        if(te.contains("Bye")){
            break;
        }
        
        
        }
    }
    
    private static Candidate generateRandomCandidates(int noOfArrtributes) {
		
		Random doubleGenerator = new Random();
		Double score;
		DecimalFormat twoDecimalFormat = new DecimalFormat("#.####");
		
		Candidate candi = new Candidate();
		score= 0d;
		List<Double> oL = new ArrayList<Double>();
		for(int j=0; j<noOfArrtributes; j++){
			Double doubleValue ;    
			
			while(true){
				doubleValue = doubleGenerator.nextDouble();
				if(doubleValue==0.0){
					continue;    
				}
				break;
			}
			doubleValue = Double.valueOf(twoDecimalFormat.format(doubleValue));
			oL.add(doubleValue);
		}
		candi.setValues(oL);
		return candi;
	}
    
	
}
