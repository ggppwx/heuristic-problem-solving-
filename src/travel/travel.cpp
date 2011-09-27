/*
travelling salesman  puzzles 
 */ 


#include <iostream>
#include <string>
#include <fstream>
#include <vector>
#include <set>
#include <deque>
#include <math.h>
#include <assert.h>
#include <time.h>
#include <stdlib.h>

struct coor
{
  float x;
  float y;
  float z; 
};


float cityCost[1001][1001];


std::vector<coor> city;

float calCost(const coor &a, const coor &b)
{
  // calculate the distance between two points
  return sqrt( pow(a.x - b.x, 2)+pow(a.y-b.y, 2)+pow(a.z-b.z,2) );
}

float calCost1(int index1, int index2)
{
  // get cost between two cities, which are indicated by index(starts from 1)
  cityCost[index1][index2] = calCost(city[index1-1], city[index2 - 1]);
  //    std::cout << cityCost[index1][index2];
  cityCost[index2][index1] = cityCost[index1][index2];
  return cityCost[index1][index2];
}

float calQue(const std::vector<int> &que)
{
  // get the total cost in the queue
  float sum = 0;
  for(int i=0; i!=que.size()-1; ++i){
    // sum += calCost(city[que[i]-1],city[que[i+1]-1]);
    //sum += calCost1(que[i],que[i+1]);
    sum += cityCost[que[i]][que[i+1]];
  }
  //sum += calCost(city[que[0]-1],city[que[que.size()-1] - 1]);
  //  sum += calCost1(que[0],que[que.size()-1] );
  sum += cityCost[que[0]][que[que.size()-1]];
  return sum;
}


float  findPath(int start, std::set<int> s, std::vector<int> &q )
{
  // init 
  s.erase(start);
  q.push_back(start);

  int current = start ;
  float sum = 0.0;
  while(!s.empty()){ // s is not empty  
    float minCost = 10000000;
    int nextCity = -1; 
    // for item in set s, calculate the cost 
    for (std::set<int>::iterator it = s.begin() ; it != s.end(); ++it){ // get the point with minimum cost 
      // for it in set 
      float temp = calCost(city[current-1], city[*it -1]);
      if (temp < minCost) {
	  minCost = temp;
	  nextCity = *it;
	}
    } 
    
    assert(nextCity != -1);
    // nextCity is the city choosed 
    // push nextCity into a queue
    q.push_back(nextCity);
    current = nextCity; 
    // pop nextCity out of the set 
    s.erase(nextCity);
    sum +=  minCost; 
    //std::cout << minCost << std::endl;
  }
  return sum;  
}



std::vector<int> generateNewPath(std::vector<int> que);


void proceed(std::vector<int> que)
{
  // init a que 
  const double tempRatio = 0.9999;  // !!key variable 
  const double hightemperature = 100;  // !!key variable 
  

  std::vector<int> temp_q;
  double w = calQue(que);
     
  double temp_w = w;
  int ite = 0;
  
  std::vector<int> new_q;
  double new_w;
  bool done =false;
  double temperature = hightemperature;
  while(temperature > 0.00001){  
    
    while(true){
      ite ++;
      if(ite > 10000000){
	done =true;
	break;
      }
    new_q = generateNewPath(que);
    new_w = calQue(new_q);
    //std::cout << new_w<<"-- "<<w<<std::endl;
    if(temp_w > new_w){
      temp_w = new_w;
      temp_q = new_q;
      std::cout << "------------------------"<<temp_w<<std::endl;
    }


    float delta = new_w - w;
    if (delta < 0 ){
      // replace
      /*
      if(temp_w > new_w){
	std::cout << "------------------------"<<temp_w<<std::endl;
	temp_w = new_w;
	temp_q = new_q;
      }
      */
      que = new_q;
      w = new_w;
      break;
      //temp_w = w;
      //temp_q = que;
    }else{
      
      double rangen= rand()/double(RAND_MAX);
      //std::cout <<delta<<" "<<exp((0-delta)/temperature)<<" "<<rangen << std::endl;

      if(exp((0-delta)/temperature) > rangen){
	// replace
	que = new_q;
	w = new_w;
	break;
	//temp_w = w;
	//temp_q = que;
      }
      
      // else do nothin
    }

    }
    if(done)
      break;
    temperature *= tempRatio;  

  }

  std::cout <<" the min cost: "<<temp_w <<" "<<temp_q.size()<< std::endl; 
  for(int i = 0; i< temp_q.size(); ++i){
    std::cout << temp_q[i]<<"~";
  }
  std::cout <<std::endl; 
  std::cout << ite << std::endl;   

}


std::vector<int> generateNewPath(std::vector<int> que)
{
  int size = que.size();
  int randomIndex = rand() % size ;  // que  from 0 to size-1  
  int randomIndex1 = rand() % size; 
  // change swap randomIndex and ramdomIndex + 1
  int temp = que[randomIndex];
  que[randomIndex] = que[randomIndex1];
  que[randomIndex1] = temp;
  return que;
}



std::vector<int> initQue(const std::set<int> &s)
{
  int start = 1;  // start point is 1 
  std::vector<float> st;
  float minWeight = 100000000; 
  std::vector<int> minQue;

  int ite = 0;   // !!! determin the max number of interation.

  while (true){ //
    ite++;
    if(ite > 100){
      return minQue;
    }
 
    std::vector<int> que; 
    float weight = findPath(start, s, que); 
    /*
    for(int i = 0 ; i != que.size(); ++i){
      std::cout << que[i] << "-" ;
    }
    std::cout << std::endl;
    */
    int end = que.back(); // end point  
    weight += calCost(city[end-1], city[start-1]);
    // std::cout << "cost is: "<< weight << std::endl;
    if(weight < minWeight){
      minQue = que;
    }

    que.clear();
    weight = findPath(end,s,que);
    /*
    for(int i = 0 ; i != que.size(); ++i){
      std::cout << que[i] << "-" ;
    }
    std::cout << std::endl;
    */
    int p = que.back();
    weight += calCost(city[end-1],city[p-1]);
    // std::cout << "cost is: "<< weight << std::endl;
    if (weight < minWeight){
      minQue = que;
    }

    if (start == p ) { // end the loop 
      return minQue;
    }else{
      start = p; 
    }  
  }
  
}


int main(int argc, char* argv[])
{

  std::cout << "start" << std::endl;
  // input is the path of a file.
  srand( time(NULL) );
  std::string path = argv[1];
  std::fstream myfile;
  int index; 
  
  std::set<int> s;

  myfile.open(path.c_str());
  coor item;
  while(myfile >> index >> item.x >> item.y >> item.z){
    city.push_back(item);
    s.insert(index);
  }
  myfile.close();
  std::cout << "cities size " << city.size() << s.size()<< std::endl;
  
  for(int i=1; i<1001; i++){
    for(int j=1; j<1001; j++){
  
      	calCost1(i,j);
      
    }
  }

  //calculate.
  std::vector<int> re(1000);
  re = initQue(s);
  std::cout << "init cost: "<<  calQue(re)<< std::endl;
   for(int i=0;i < re.size(); ++i){
     //      re[i] = i+1;
     std::cout << re[i] << ">";
   }
   std::cout << "size "<< re.size()<<std::endl;

   /*
   re = generateNewPath(re);
   for(int i=0;i < re.size(); ++i){
     std::cout << re[i] << ">";
   }
   std::cout << std::endl;
   */
    proceed(re); 

  return 0;
}
