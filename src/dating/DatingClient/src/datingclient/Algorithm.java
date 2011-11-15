package datingclient;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

import org.junit.Test;

import Jama.Matrix;

public class Algorithm{
	private int noOfAttributes;
	private ArrayList<Double> scores;  // score for each candidate
	private ArrayList<Double[]> values; // value for each candidate
	private ArrayList<Double[]> preWeights; // previous weight.
	
	public Algorithm(){
		
	}
	
    public Algorithm(int noOfAttributes){
    	this.scores = new ArrayList<Double>(); 
    	this.values = new ArrayList<Double[]>();
    	this.noOfAttributes = noOfAttributes;
    	this.preWeights = new ArrayList<Double[]>();
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
    
    
    public Candidate generateCandidate(){
    	Candidate cand = new Candidate();
    	// TODO: get a good candidate. which is list<Double>
    	
    	// double[] res = matrixProcess();
    	double[] res = gradientDes();
    	preWeights.add(convertToDouble(res));
    	
    	
    	Double[] temp = guessCandidate(res);
    	List<Double> canVal = new ArrayList<Double>();
    	for(int i = 0; i< temp.length; ++i){
    		canVal.add(temp[i]);
    	}
    	cand.setValues(canVal);
    	return cand;
    }
    
    /*
     * matrix process. proceed  records. 
     * use matrix to calculate a possible weight.
     * */
    private double[] matrixProcess(){
    	// TODO: put all records into a matrix. 
    	// calculate a weight vector. 
    	// w* = (XT X)-1XT Y, where Y is score vector, X is value matrix 
    	assert(values.size() == scores.size());
    	int numVal = values.size();
    	Double[][] vs = values.toArray(new Double[numVal][noOfAttributes]);
    	double[][] firstValueM = new double[numVal][noOfAttributes]; 
    	for(int i=0; i< numVal;++i){
    		// Double[] row = vIt.next();
    		for(int j=0;j< noOfAttributes;++j){
    			firstValueM[i][j] = (double)vs[i][j];	
    		}
    	}
    	Matrix x = new Matrix(firstValueM); //value is m *n 
    	
    	Double[] ss = scores.toArray(new Double[numVal]);
    	double[][] firstScoreM = new double[numVal][1];
    	for(int i=0;i<numVal;++i){
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
    	
    	double[] weight = genRandWeight();
    	
    	/*
    	for(int i = 0 ; i<noOfAttributes; ++i){
    		weight[i] = 1.0/(double)noOfAttributes;
    	}
    	*/
    	scores.add(0d);  // score for each candidate
    	Double[] tempV = new Double[noOfAttributes];
    	for(int k =0; k<tempV.length; ++k){
    		tempV[k] = 1d;
    	}
    	values.add(tempV); // value for each candidate
    	
    	int iter = 0;
    	double difference;
    	while(true){ // some stopping condition.
    		double[] g = new double[noOfAttributes];
    		Iterator<Double[]> vIt = values.iterator();
    		Iterator<Double> sIt = scores.iterator();
    		difference = 0.0;
    		while(vIt.hasNext() && sIt.hasNext()){ // for each candidates
    			Double[] x = vIt.next();
    			Double y = sIt.next();
    			double dotPro = dotProduct(convertFromDouble(x),weight);
    			double diff = dotPro - y;
    			for(int i = 0; i< noOfAttributes; ++i){ // for each gradient index
    				g[i] = g[i] + diff * x[i];
    			}
    			
    			
    		}
    		
    		for(int i = 0; i<noOfAttributes; ++i){ // for each weight index. 
    			// System.out.print(g[i]);
    			weight[i] = weight[i] - eta*g[i];
    			difference += Math.pow(eta*g[i], 2);
    		}
    		//System.out.println(g[0]);
    		
    		iter ++;
    		if(iter == 1000000){
    			break;
    		}
    		
    		difference = Math.sqrt(difference); 
    		//System.out.println("--------");
    		//System.out.println(difference);
    		if(difference < 0.0000001){
    			System.out.println(">>>>>>>>");
    			System.out.println(iter);
    			break;
    		}
    		
    		
    	}
    	scores.remove(scores.size() - 1);
    	values.remove(values.size() - 1);
    	
    	
    	return weight;
    	
    }
    
    /*
     * guess candidates according to weights. 
     * */
    private Double[] guessCandidate(double[] weights){
    	assert(weights.length == noOfAttributes);
    	Double[] res = new Double[noOfAttributes];
    	// TODO: guess a candidate.
    	// the output should be Double[], with 4 decimal places 
    	// TODO: try a naive method
    	DecimalFormat twoDecimalFormat = new DecimalFormat("#.####");
    	for(int i = 0; i<weights.length; ++i){
    		if(weights[i] > 0){
    			double item = 1.0;
    			res[i] = Double.valueOf(twoDecimalFormat.format(item));
    		}else{
    			double item = 0.0;
    			res[i] = Double.valueOf(twoDecimalFormat.format(item));
    		}
    		
    	}
    	
    	return res;
    }
    
    /*
     * generate a random weight
     * */
    private double[] genRandWeight(){
    	Player player = new Player(noOfAttributes);
		player.genWeights();
    	return player.getWeight();
    }
    
    
    
    /*
     * Helper functions 
     * 
     * */
    private double[] convertFromDouble(Double[] D){
    	double[] dou = new double[D.length];
    	for(int i = 0 ; i<D.length; ++i){
    		dou[i] = (double)D[i];
    	}
    	return dou;
    }
    
    private Double[] convertToDouble(double[] d){
    	Double[] Dou = new Double[d.length];
    	for(int i=0; i<d.length; ++i){
    		Dou[i] = d[i];
    	}
    	return Dou;
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
    
    public double[] matrixProcessTest(){
    	return matrixProcess();
    }
    
    public double[] gradientDesTest(){
    	return gradientDes();
    }

}
