#include <iostream>
#include <fstream>
#include <vector>
#include <stdlib.h>
#include <stdlib.h>

struct person
{
  // rescue time > traveling time + uploading & unloading time 
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



int calTime(int startX, int startY, int selX, int selY)
{
  int distance = abs(startX - selX)  + abs(startY - selY);
  
}

void pickPatients(int startX, int startY, std::vector<person> patientLeft, std::vector<hospital> hospitals )
{
  int currentTime ;
  for(std::vector<person>::iterator it = patientLeft.begin(); it != patient.end(); ++it){
    int disToStart = abs(it->xloc - startX) + abs(it->yloc - startY) ;
    int minDisToEnd = 100000;
    for (std::vector<hospital>::iterator it1 = hospitals.begin(); it1 != hospitals.end(); ++it1 ){
      int temp = abs(it->xloc - it1->xloc ) + abs(it->yloc - it1->yloc);
      if(temp < minDisToEnd){
	minDisToEnd = temp; 
      }
    }
    int totalTime = disToStart + minDisToEnd + 2; 
    int remainTime = it->rescueTime - totalTime;
    if(remainTime >= 0){  // savable for 1st slot. 

    }
  
  }
  // pick an patient 
  // delete the selected patient. 
  

  for(){

  }



}

void rescue(const std::vector<hospital> &hospitals,
	    const std::vector<person> &persons)
{
  
  // return vector<set> ambulance
  // use ant-colony alg 
  // the first ant starting from the first 
  // ambulance in the first hospital. save as many patients as possible. 
  // iterate 100 times?? 100 ants?
  for(std::vector<hospital>::iterator it = hospitals.begin();
      it != hospitals.end(); ++it){
    int numAm = it->numAmbulance;
    for(int i = 1; i<= numAm; ++i){ // ant in ith ambulance starts 
      // randomly save as many patient as possible 
      
      // 1. find all patients savable.  * 4 times   
      // find patients savable is a heuristic procedure. 
      // save the current elapsed time, 
      // if rescue time - 2 > elapsed time not feasible.  
      // if any one in the ambulance dies when choose another patient. not
      // feasible. 
      it->xloc
      
      
      
      
    }
  }

 
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
