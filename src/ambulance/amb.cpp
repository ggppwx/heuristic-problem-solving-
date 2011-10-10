#include <iostream>
#include <fstream>
#include <vector>
#include <stdlib.h>

struct person
{
  int xloc;
  int yloc;
  int rescueTime;
};

struct hospital
{
  int xloc;
  int yloc;
  int numAmbulance;
};



void rescue(const std::vector<hospital> &hospitals,
	    const std::vector<person> &persons)
{
  
  // return vector<set> ambulance 
}

bool processText(const std::string &text, int &p1, int &p2, int &p3)
{
  // get p1 p2 p3 of the text
  size_t firstD = text.find_first_of(",");
  size_t secondD = text.find_first_of(",",firstD+1);
  if (!firstD ){
    return false;
  }
  std::string p1Text, p2Text, p3Text;
  p1Text = text.substr(0,firstD);
  p2Text = text.substr(firstD+1,secondD-firstD-1);
  p3Text = text.substr(secondD+1);
  p1 = atoi(p1Text.c_str());
  p2 = atoi(p2Text.c_str());
  p3 = atoi(p3Text.c_str());
  //test 
  std::cout << "info-"<<p1<<":"<<p2<<":"<<p3<<std::endl;
  return true;
}


void readInputFile(const char* fileName, 
		   std::vector<person> &persons,
		   std::vector<hospital> &hospitals)
{
  std::ifstream inputFile;
  inputFile.open(fileName);
  std::string text;
  int stage = 0; 
  if (inputFile.is_open()){
    while(std::getline(inputFile, text)){
      if(text.empty()){ // blank line 
	continue;
      }
      
      if(text.substr(0,6).compare("people") == 0){
	std::cout << "Reading person info.." << std::endl;
	stage = 1;
	continue;
      }else if(text.substr(0,8).compare("hospital") == 0){
	std::cout << "Reading hospital info.."<< std::endl;
	stage = 2;
	continue;
      } 
      
      if (stage == 1){ // process text,  store them to persons
	person p;
	if(processText(text, p.xloc, p.yloc, p.rescueTime)){
	  persons.push_back(p);
	}
      }else if (stage == 2){// process text, store them to hospitals 
	hospital h;
	if(processText(text, h.xloc, h.yloc, h.numAmbulance)){
	  hospitals.push_back(h);
	}
      }else{
	//error 
	std::cout << "format error.."<<std::endl;
      }
    }
    inputFile.close();
  }else{
    std::cout << "cannot open file.."<<std::endl;
  }
}


int main(int argv, char* argc[])
{
  std::vector<person> persons;
  std::vector<hospital> hospitals;
  // get all informations 
  readInputFile("input",persons,hospitals);
  // start gaming
  
  
  
  return 0; 
}
