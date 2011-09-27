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



std::vector<coor> city;

float calCost(const coor &a, const coor &b)
{
  // calculate the distance between two points
  return sqrt( pow(a.x - b.x, 2)+pow(a.y-b.y, 2)+pow(a.z-b.z,2) );
}

float calQue(const std::vector<int> &que)
{
  // get the total cost in the queue
  float sum = 0;
  for(int i=0; i!=que.size()-1; ++i){
    sum += calCost(city[que[i]-1],city[que[i+1]-1]);
  }
  sum += calCost(city[que[0]-1],city[que[que.size()-1] - 1]);
  return sum;
}


int c=0;

void f(const std::deque<int> &que,const std::set<int> &s)
{
  if (s.empty()){
    for(int i=0;i!=que.size();i++){
      std::cout << que[i]<<" ";
      
    }
    c++; 
    std::cout << std::endl;
  }

  // for i in set s
  for(std::set<int>::iterator it = s.begin(); it!=s.end(); ++it){
    std::set<int> s_next = s;
    std::deque<int> q_next = que;
    s_next.erase(*it);
    q_next.push_back(*it);
    f(q_next,s_next);
  }

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
    std::cout << minCost << std::endl;
  }
  return sum;  
}


std::vector<int> test1(std::set<int> s);
std::vector<int> generateNewPath(std::vector<int> que);

void proceed(std::vector<int> que)
{

  // init a que
  
  std::vector<int> temp_q;
  float w = calQue(que);
  float temperature = w;
  float temp_w = w;

  while(temperature > 0.00001){
    std::vector<int> new_q;
    new_q = generateNewPath(que);
    float new_w = calQue(new_q);
    std::cout << new_w<<std::endl;

    float delta = new_w - w;
    if (delta < 0 ){
      // replace 
      if(temp_w > new_w){
	std::cout << "------------------------"<<temp_w<<std::endl;
	temp_w = new_w;
	temp_q = new_q;
      }
      que = new_q;
      w = new_w;
      //temp_w = w;
      //temp_q = que;
    }else{
      float rangen= rand()/float(RAND_MAX);
      std::cout <<delta<<" "<<exp((0-delta)/temperature)<<" "<<rangen << std::endl;
      
      if(exp((0-delta)/temperature) > rangen){
	// replace
	//que = new_q;
	//w = new_w;
	//temp_w = w;
	//temp_q = que;
      }
      // else do nothin
    }
    temperature *= 0.9999;

  }

  std::cout <<" the min cost: "<<temp_w <<" "<<temp_q.size()<< std::endl; 
  for(int i = 0; i< temp_q.size(); ++i){
    std::cout << temp_q[i]<<"~";
  }
  std::cout <<std::endl; 
      

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


int main(int argc, char* argv[])
{
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

  //calculate.
  std::vector<int> re;
   re = test1(s);
   std::cout << "init cost: "<<  calQue(re)<< std::endl;
   for(int i=0;i < re.size(); ++i){
     std::cout << re[i] << ">";
   }
   std::cout << std::endl;

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

std::vector<int> test1(std::set<int> s)
{
  int start = 1;  // start point is 1 
  std::vector<float> st;
  
  while (true){ // 
    std::vector<int> que; 
    float weight = findPath(start, s, que); 
    for(int i = 0 ; i != que.size(); ++i){
      std::cout << que[i] << "-" ;
    }
    std::cout << std::endl;
    int end = que.back(); // end point  
    weight += calCost(city[end-1], city[start-1]);
    // std::cout << "cost is: "<< weight << std::endl;
    st.push_back(weight);

    que.clear();
    weight = findPath(end,s,que);
    for(int i = 0 ; i != que.size(); ++i){
      std::cout << que[i] << "-" ;
    }
    std::cout << std::endl;
    int p = que.back();
    weight += calCost(city[end-1],city[p-1]);
    // std::cout << "cost is: "<< weight << std::endl;
    st.push_back(weight);

    if (start == p ) { // end the loop 
      return que;
      break;
    }else{
      start = p; 
    }

    
  }
  for(int i =0; i< st.size(); ++i){
    std::cout << st[i] <<"  ";
  }

}
