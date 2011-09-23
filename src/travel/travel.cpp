/*
travelling salesman  puzzles 
 */ 


#include <iostream>
#include <string>
#include <fstream>
#include <vector>

struct coor
{
  int x;
  int y;
  int z; 
};



int main(int argc, char* argv[])
{
  // input is the path of a file.
  // coor city[100];
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
  
  //calculate. 

  



  return 0;
}
