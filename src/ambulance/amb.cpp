#include <iostream>
#include <fstream>
#include <vector>
#include <stdlib.h>
#include <time.h>

struct person
{
  // rescue time > traveling time + uploading & unloading time 
  int xloc;
  int yloc;
  int rescueTime;  
  int pickTime;
  float pherom;
  int saveCount;
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

int ToHospital(int x, int y, const std::vector<hospital> & hospitals)
{
  // closest distance to hospital
  int minDisToEnd = 100000;
  for (std::vector<hospital>::iterator it1 = hospitals.begin(); it1 != hospitals.end(); ++it1 ){
      int temp = abs(it->xloc - it1->xloc ) + abs(it->yloc - it1->yloc);
      if(temp < minDisToEnd){
	minDisToEnd = temp; 
      }
  }
  return minDisToEnd; 
}


std::vector<person> findSavable(int startX, int startY, 
				std::vector<person> patientLeft, std::vector<hospital> hospitals, 
				int currentTime, const std::vector<person> &patientsPicked)
{
  std::vector<person> patientSavable;
  for(std::vector<person>::iterator it = patientLeft.begin(); it != patient.end(); ++it){
    int disToStart = abs(it->xloc - startX) + abs(it->yloc - startY) ;
    int minDisToEnd = 100000;
    for (std::vector<hospital>::iterator it1 = hospitals.begin(); it1 != hospitals.end(); ++it1 ){
      int temp = abs(it->xloc - it1->xloc ) + abs(it->yloc - it1->yloc);
      if(temp < minDisToEnd){
	minDisToEnd = temp; 
      }
    }
    int totalTime = disToStart + minDisToEnd + 2 + currentTime; 
    int remainTime = it->rescueTime - totalTime;
    if(remainTime >= 0){  
      // check if it will cause death of previous ones
      bool sFlag = true;
      for(int i = 0; i<patientsPicked.size(); ++i){
	if(patientsPicked[i].rescueTime - totalTime < 0){
	  sFlag = false;
	}

      }
      if(sFlag){
	patientSavable.push_back(*it);
      }
    }
  
  }
  return patientSavable;
}

person heurPick(int currentX, int currentY,std::vector<person> patientsSavable, const int traceMap[][])
{
  // heuristically pick a patient
  int distanceSum = 0;
  int timeSum = 0;
  int countSum = 0;
  
  int size = patientSavable.size();
  for(int i=0; i< patientSavable.size(); ++i){
    distanceSum += abs(patientSavable[i].xloc - currentX) + abs(patientSavable[i].yloc - currentY);
    timeSum += patientSavable[i].rescueTime;
    
  }
  
  std::vector<float> prob(patientSavable.size());
  float weightSum = 0;
  for(int i=0; i< patientSavable.size(); ++i){
    float distanceP = (abs(patientSavable[i].xloc - currentX) + abs(patientSavable[i].yloc - currentY))/(float)nceSum;
    float timeP = (timeSum*2/size - patientSavable[i].rescueTime)/ (float)timeSum;
    prob[i] = (distanceP + timeP)/2*(1+traceMap[patientSavable[i].xloc][patientSavable[i].yloc]); 
    weightSum += prob[i];
  }

  for(int i=0; i< patientSavable.size(); ++i){
    prob[i] = prob[i] / weightSum;
    std::cout << "prob choosing"<<i<<"-"<< prob[i] << std::endl;// TEST
  }

  float probSum = 0.0;
  for(int i = 0; i < prob.size(); ++i){
    probSum += prob[i];
    prob[i] = probSum;
  }

  // select a patient.
  float random =   rand() % RAND_MAX; // random from [0,1)
  int indexSel;
  if(random >= 0 && random < prob[0]){
    indexSel = 0;
  }else{
    for(int i = 1; i< prob.size(); ++i){
      if(random >= prob[i-1] && random <= prob[i]){
	indexSel = i;
	break;
      }
    }
  }
  // return the selected patient 
  return patientSavable[indexSel];
}


void pickPatients(int startX, int startY, 
		  std::vector<person> &patientLeft, std::? &patientSaved 
		  const std::vector<hospital> &hospitals, 
		  const int traceMap[][] )
{
  int currentTime = 0;
  // starts from hospital 
  std::? patientsSaved; 

  while(true){

    std::vector<person> patientsPicked;
    int currentX = startX;
    int currentY = startY;
    for(int i = 0; i < 4; ++i){ // loop 4 times 
      std::vector<person> patientsSavable = findSavable(currentX, currentY, patientLeft, hospitals, currentTime,patientsPicked);
      if(patientSavable.empty()){ //TBD: no savable patient, back to hospital
	break;
      }
      // pick an patient 
      person pick = heurPick(patientSavable,traceMap);
      patientsPicked.push_back(pick);
      //TBD:  delete the selected patient. 
      currentTime += pick.pickTime;
      currentX = pick.xloc;
      currentY = pick.yloc;
    }
    // back to hospital
    if(!patientsPicked.empty()){ 
      currentTime += ToHospital(patientsPicked.back().xloc, patientsPicked.back().yloc) + 1;
      //TBD: put patients in amb into hospital
      patientsSaved.add(patientsPicked);
      
    }else{ // break the while loop. finish.
      break;
    }
    
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
  int c = 0; // c is a count. 
  int traceMap[1000][1000];
  int bestScore = 0;
  std::? bestSolution;
  while(){ // interate several times
    // this is one ant
    std::? patientsLeft;
    std::? patientsSaved; 
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
	pickPatients(it->xloc, it->yloc,patientsLeft,patientSaved,hospitals,traceMap);
	
	
      }
    }
    
    // local update the pheromone. reduce 
    for(int i = 0 ; i < persons.size(); ++i){ 
      traceMap[persons[i].xloc][persons[i].yloc] *= 0.9;
    }

    // leave trace on tracemap
    for(){ // patient saved. 
      traceMap[][] += ;
    }

    // calculate the score 
    // get the best score
    int tempScore = 0;
    for(){ //patient saved
      tempScore = ;
    }
    if(tempScore > bestScore){
      bestScore = temp; 
      bestSolution = ;
    }
    
    // global update the pheromone
    if(c == 20){
      c ++;
      for(){ // bestSolution
	traceMap[][] +=; 
      }
      c = 0;
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
  srand(time(NULL));
  std::vector<person> persons;
  std::vector<hospital> hospitals;
  // get all informations 
  readInputFile("input",persons,hospitals);
  // start gaming
  
  
  
  return 0; 
}
