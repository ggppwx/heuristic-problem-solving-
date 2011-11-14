package datingclient;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.regex.Pattern;

import org.junit.Test;

import Jama.Matrix;

public class Algorithm{
    // TODO: add some data structure to store history
	private int noOfAttributes;
	private ArrayList<Double> scores;  // score for each candidate
	private ArrayList<Double[]> values; // value for each candidate
	
	public Algorithm(){
		
	}
	
    public Algorithm(int noOfAttributes){
    	this.scores = new ArrayList<Double>(); 
    	this.values = new ArrayList<Double[]>();
    	this.noOfAttributes = noOfAttributes;
    }
    

    /*
     *  
     */
    public void readCandidates(String input){
    	// store them into an data structure, the format is like:
    	// i.e. [0.9626, 0.5476, 0.6633, 0.2155, 0.5082, 0.47, 0.9267, 0.155, 0.279, 0.9403]:[-0.0554]  
    	if(!input.startsWith("[")){
    		return;
    	}
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
    
    /*
     * pre-process. proceed first 20 records. 
     * use matrix to calculate a possible weight.
     * */
    private double[] preProcess(){
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
    	double[] retWeight = new double[noOfAttributes];
    	for(int i=0; i<noOfAttributes; ++i){
    		retWeight[i] = weight[i][0];
    	}
    	return retWeight;
    }
    
    /*
     * use gradient descent algorithm to calculate a possible weight.
     * it seems now the weight cannot pass validator(sum of positive equals 1...)
     * 
     * */
    private double[] gradientDes(){
    	// TODO: implement gradient descent algorithm
    	//	Until some stopping condition 
		//    Gt = vector of length n, initialized to all 0s
		//    foreach candidate c
		//	     dotprod = ∑i xc,i*wt,i
		//	     diff = dotprod - yc
		//	     foreach gradient index i
		//		   Gt,i =  Gt,i + diff * xc,i
		//    foreach weight index i
		//	     wt+1,i = wt,i - η*Gt,i
    	assert(values.size() == scores.size());
    	double eta = 0.0001; // eta is learning rate.
    	double[] g = new double[noOfAttributes];
    	double[] weight = new double[noOfAttributes];
    	for(int i = 0 ; i<noOfAttributes; ++i){
    		weight[i] = 1.0/(double)noOfAttributes;
    	}
    	int iter = 0;
    	while(true){ // some stopping condition.
    		Iterator<Double[]> vIt = values.iterator();
    		Iterator<Double> sIt = scores.iterator();
    		while(vIt.hasNext() && sIt.hasNext()){ // for each candidates
    			Double[] x = vIt.next();
    			Double y = sIt.next();
    			double dotPro = dotProduct(convertFromDouble(x),weight);
    			double diff = dotPro - y;
    			for(int i = 0; i< noOfAttributes; ++i){ // for each gradient index
    				g[i] = g[i] + diff * (double)x[i];
    			}
    			
    		}
    		for(int i = 0; i<noOfAttributes; ++i){ // for each weight index. 
    			weight[i] = weight[i] - eta*g[i];
    		}
    		iter ++;
    		if(iter == 1000000){
    			break;
    		}
    		
    		
    	}
    	return weight;
    	
    }
    
    private double[] convertFromDouble(Double[] D){
    	double[] dou = new double[D.length];
    	for(int i = 0 ; i<D.length; ++i){
    		dou[i] = (double)D[i];
    	}
    	return dou;
    }
    
    private double dotProduct(double[] x, double[] y){
    	assert(x.length == y.length);
    	double sum = 0;
    	for(int i=0; i<x.length; ++i){
    		sum +=x[i]*y[i]; 
    	}
		return sum;
    }
    
    
    /*
     * FOR TEST ONLY:  
     * test private method
     * */
    public double[] convertFromDoubleTest(Double[] D){
    	return convertFromDouble(D);
    }
    
    public double dotProductTest(double[] x, double[] y){
    	return dotProduct(x,y);
    }
    
    public double[] preProcessTest(){
    	return preProcess();
    }
    
    public double[] gradientDesTest(){
    	return gradientDes();
    }

}
