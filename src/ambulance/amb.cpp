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

struct coord
{
  int x;
  int y;
  int pflag;
};

int calTime(int startX, int startY, int selX, int selY)
{
  int distance = abs(startX - selX)  + abs(startY - selY);
  
}

int ToHospital(int x, int y, const std::vector<hospital> &hospitals, coord &st)
{
  // closest distance to hospital
  int minDisToEnd = 100000;
  for (std::vector<hospital>::const_iterator it = hospitals.begin(); it != hospitals.end(); ++it ){
      int temp = abs(it->xloc - x ) + abs(it->yloc - y);
      if(temp < minDisToEnd){
	minDisToEnd = temp; 
	st.x = it->xloc;
	st.y = it->yloc;
	st.pflag = 0; 
      }
  }
  return minDisToEnd; 
}


std::vector<person> findSavable(int startX, int startY, 
				const std::vector<person> &patientLeft, 
				const std::vector<hospital> &hospitals, 
				int currentTime, 
				const std::vector<person> &patientsPicked, 
				const int flag[1000][1000])
{
  std::vector<person> patientSavable;
  for(std::vector<person>::const_iterator it = patientLeft.begin(); it != patientLeft.end(); ++it){
    if(flag[it->xloc][it->yloc] != 0){  // picked 
      continue;
    }
    int disToStart = abs(it->xloc - startX) + abs(it->yloc - startY) ;
    int minDisToEnd = 100000;
    for (std::vector<hospital>::const_iterator it1 = hospitals.begin(); it1 != hospitals.end(); ++it1 ){
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
	if(flag[it->xloc][it->yloc] == 0){
	  patientSavable.push_back(*it);
	}
      }
    }
  
  }
  return patientSavable;
}

person heurPick(int currentX, int currentY,std::vector<person> patientSavable, const int traceMap[1000][1000], int flag[1000][1000])
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
    int distance = abs(patientSavable[i].xloc - currentX) + abs(patientSavable[i].yloc - currentY);
    int time =  patientSavable[i].rescueTime;
    prob[i] = (1+traceMap[patientSavable[i].xloc][patientSavable[i].yloc])/(float)(distance + time); 
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
  float random =   rand() /(float)RAND_MAX; // random from [0,1)
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
  
  // flag[patientSavable[indexSel].xloc][patientSavable[indexSel].yloc] = 1;
  // return the selected patient 
  return patientSavable[indexSel];
}


void pickPatients(int startX, int startY, 
		  const std::vector<person> &patientLeft,   //gloabal
		  std::vector<person> &patientSaved ,
		  const std::vector<hospital> &hospitals, 
		  const int traceMap[1000][1000],
		  std::vector<coord> &trace,
		  int flag[1000][1000])  // flag indicates which is picked
{
  coord st;   
  st.x = startX;   // put start point to solution 
  st.y = startY;
  st.pflag  = 0;
  trace.push_back(st);

  int currentTime = 0;
  // starts from hospital 
  int currentX = startX;
  int currentY = startY;

  while(true){  // one amb
    std::cout <<"ambulance go!!!----------------" << std::endl;
    std::vector<person> patientsPicked;   // at most 4
    
    for(int i = 0; i < 4; ++i){ // loop 4 times 
      std::vector<person> patientsSavable = findSavable(currentX, currentY, patientLeft, hospitals, currentTime,patientsPicked,flag);
      if(patientsSavable.empty()){ 
	break;
      }
      // pick an patient 
      person pick = heurPick(currentX,currentY,patientsSavable,traceMap, flag);
      std::cout <<"pick "<< pick.xloc <<"," << pick.yloc<<std::endl;
      flag[pick.xloc][pick.yloc] = 1;  // indicates it has been picked 
      patientsPicked.push_back(pick);
      
      // delete in patient  left 
      
      currentTime += abs(pick.xloc - currentX) + abs(pick.yloc - currentY)+1;
      currentX = pick.xloc;
      currentY = pick.yloc;
      
      st.x = currentX; // put it into solution 
      st.y = currentY;
      st.pflag = 1; // patient
      trace.push_back(st);
    }
    // back to hospital
    if(!patientsPicked.empty()){   // unload patients 
      currentTime += ToHospital(patientsPicked.back().xloc, patientsPicked.back().yloc,hospitals, st) + 1;
      currentX = st.x;
      currentY = st.y;
      std::cout <<"return to hospital "<< currentX <<","<< currentY<<std::endl;
      trace.push_back(st);
      
      for(std::vector<person>::iterator it = patientsPicked.begin() ; it != patientsPicked.end(); ++it){  
	patientSaved.push_back(*it);
      }
      
    }else{ // break the while loop. finish.
      break;
    }
    
  }

}

std::vector< std::vector<coord> > rescue(const std::vector<hospital> &hospitals,
	    const std::vector<person> &persons)
{
  
  // return vector<set> ambulance
  // use ant-colony alg 
  // the first ant starting from the first 
  // ambulance in the first hospital. save as many patients as possible. 
  // iterate 100 times 100 ants?
  int c = 0; // c is a count. 
  int traceMap[1000][1000];
  for(int i = 0; i< 1000; ++i){
    for(int j = 0; j< 1000; ++j){
      traceMap[i][j] = 0;
    }
  }
  
  int bestScore = 0;
  std::vector< std::vector<coord> > bestSolution;
  std::vector<person> bestPatientsSaved;
  int ite = 0;
  while(ite < 1000){ // interate several times
    std::cout << "ant go!!!!-----------" << std::endl;
    int flag[1000][1000];
    for(int i = 0; i< 1000; ++i){
      for(int j = 0; j< 1000; ++j){
	flag[i][j] = 0;
      }
    }
    ite ++;
    // this is one ant
    // std::vector<person> patientsLeft;
    std::vector<person> patientsSaved;
    std::vector< std::vector<coord> > solutions;
    for(std::vector<hospital>::const_iterator it = hospitals.begin();
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
	std::vector<coord> solution;
	pickPatients(it->xloc, it->yloc,persons,patientsSaved,hospitals,traceMap,solution,flag);   // solution is for a ambulance
	solutions.push_back(solution);
	
      }
    }
    
    // local update the pheromone. reduce 
    for(int i = 0 ; i < persons.size(); ++i){ 
      traceMap[persons[i].xloc][persons[i].yloc] *= 0.9;
    }

    // leave trace on tracemap
    for(std::vector<person>::iterator it = patientsSaved.begin() ; it != patientsSaved.end(); ++it){ 
      traceMap[it->xloc][it->yloc] += 1;
    }

    // calculate the score 
    // get the best score
    int tempScore = patientsSaved.size();
    
    if(tempScore > bestScore){
      bestScore = tempScore; 
      bestSolution = solutions;
      bestPatientsSaved = patientsSaved;
      //std::cout <<">>>current score is: "<< bestScore <<std::endl;
    }
    
    // global update the pheromone
    if(c == 10){
      c ++;
      for(std::vector<person>::iterator it = bestPatientsSaved.begin() ; it != bestPatientsSaved.end(); ++it){ 
	traceMap[it->xloc][it->yloc] += 1; 
      }
      c = 0;
    }
  }
  return bestSolution;
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
  
  std::vector< std::vector<coord> > result = rescue(hospitals, persons); 
  std::cout << "the result " << std::endl;
  for(std::vector< std::vector<coord> >::const_iterator it = result.begin(); it != result.end(); ++it){
    
    for(std::vector<coord>::const_iterator it1 = (*it).begin() ; it1 != (*it).end(); ++it1){
      if((*it1).pflag == 0){
	std::cout << "hostpiatl -------"<<(*it1).x << " " << (*it1).y << std::endl; 
      }else{
	std:: cout << (*it1).x << " " << (*it1).y << std::endl; 
      }
    }
  }
  
  
  return 0; 
}

