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

float calQue(const std::deque<int> &que)
{
  // get the total cost in the queue
  float sum = 0;
  for(int i=0;i!=que.size()-1;i++){
    sum += calCost(city[que[i]-1],city[que[i+1]-1]);
  }
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
  for(std::set<int>::iterator it = s.begin();it!=s.end();it++){
    std::set<int> s_next = s;
    std::deque<int> q_next = que;
    s_next.erase(*it);
    q_next.push_back(*it);
    f(q_next,s_next);
  }

}

int main(int argc, char* argv[])
{
  // input is the path of a file.
  
  /*
  std::string path = argv[1];
  std::fstream myfile;
  int index; 
  std::vector<coor> city; // city starts from 0 -??
  myfile.open(path.c_str());
  while(!myfile.eof()){
    coor item;
    myfile >> index >> item.x >> item.y >> item.z;
    city.push_back(item);
  }
  myfile.close();
  */
  //calculate.
  std::deque<int> q(1,1);
  int myset[] = {2,3,4,5,6,7,8};
  std::set<int> s(myset,myset+7);
  f(q, s);
  std::cout << c <<std::endl;



  return 0;
}
