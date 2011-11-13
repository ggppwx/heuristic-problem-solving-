package datingclient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.regex.Pattern;

import Jama.Matrix;

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
    	Matrix a;
	
    	
    	return "";
    }
    
    /*
     * pre-process. proceed first 20 records. 
     * */
    private void preProcess(){
    	// TODO: put first 20 records into a matrix. 
    	// calculate a weight vector. 
    	// w* = (XT X)-1XT Y, where Y is score vector, X is value matrix 

    	Double[][] vs = values.toArray(new Double[20][noOfAttributes]);
    	// Iterator<Double[]> vIt = values.iterator();
    	double[][] firstValueM = new double[20][noOfAttributes]; 
    	for(int i=0; i< 20;++i){
    		// Double[] row = vIt.next();
    		for(int j=0;j< noOfAttributes;++j){
    			firstValueM[i][j] = (double)vs[i][j];	
    		}
    	}
    	Matrix x = new Matrix(firstValueM); //value is m *n 
    	
    	Double[] ss = scores.toArray(new Double[20]);
    	double[][] firstScoreM = new double[20][1];
    	for(int i=0;i<20;++i){
    		firstScoreM[i][0] = (Double)ss[i];
    	}
    	Matrix y = new Matrix(firstScoreM); //value is m*1
    	
    	Matrix w = ((x.transpose().times(x)).inverse().times(x.transpose())).times(y);
    	double[][] weight = w.getArray();  // weight is n*1
    	return;
    }
    
    

}
