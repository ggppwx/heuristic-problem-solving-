package datingclient;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Algorithm{
    // TODO: add some data structure to store history
	private int noOfAttributes;
	private ArrayList<Double> scores;  // score for each candidate
	private ArrayList<Double[]> values; // value for each candidate
	
    public Algorithm(int noOfAttributes){
    	this.scores = new ArrayList<Double>(); 
    	this.values = new ArrayList<Double[]>();
    	this.noOfAttributes = noOfAttributes;
    }
    

    /*
     *  
     */
    public void readCandidates(String input){
    	// TODO: store them into an data structure, the format is like:
    	// i.e. [0.9626, 0.5476, 0.6633, 0.2155, 0.5082, 0.47, 0.9267, 0.155, 0.279, 0.9403]:[-0.0554]  
    	Pattern p = Pattern.compile("");
    	String[] tokens = input.split(", |\\]|\\[|:");
    	Double[] value = new Double[noOfAttributes];
    	int index = 0;
    	for(int i = 0; i< tokens.length-1;++i){
    		if(!tokens[i].equals("")){
    			value[index] = Double.parseDouble(tokens[i]);
    			index++;
    		}
    	}
    	double score = Double.parseDouble(tokens[tokens.length-1]);
    	values.add(value);
    	scores.add(score);
  
    }
    

    public String generateCandidate(){
    	// TODO: main logic is here. 
    	
    		
    	
    	return "";
    }


}
