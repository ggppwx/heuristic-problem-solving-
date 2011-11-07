#ifndef MY_MUNCHER_H
#define MY_MUNCHER_H
#include "nanomunchers_data.h"
#include "nanomunchers_serialize.h"
#include "data_file.h"
#include "gtest/gtest.h"
#include <algorithm>
#include <math.h>
#include <map>
#include <deque>
#include <iostream>



namespace my_muncher{
  using namespace hps;
  typedef Graph<Position> NodeGraph;

  struct MyMuncher : Muncher{
    int startNodeIndex; 
    
  }; 
  
  typedef std::vector<MyMuncher> MyMuncherList;

  class My_munchers{
  public:
    My_munchers(){
      
    }
    ~My_munchers(){}
    
    /// load data from text file
    void loadData(std::string path);
    /// main logic: solve the nanomuncher puzzle
    void analyze();
    /// write the muncher to validator. 
    void writeData();



    /// For TEST ONLY ---------------------
    const NodeGraph& getGraph(){ 
      return my_graph;
    }
    
    /// test private member functions
    void MYSINGLETEST(){
      std::cout << "---------------" << std::endl;
      node_map.clear();
      MyMuncher muncher;
      muncher.startTime = 0;
      muncher.program[0] = Muncher::Right;
      muncher.program[1] = Muncher::Down;
      muncher.program[2] = Muncher::Left;
      muncher.program[3] = Muncher::Up;

      muncher.startNodeIndex = 47;
      std::cout<<"score is "<<getScore(muncher)<<std::endl;

    }

    void MYMULTTEST(){
      std::cout <<"----------------"<<std::endl;
      node_map.clear();
      MyMuncher m1;
      MyMuncher m2;
      m1.startTime = 0;
      m2.startTime = 0;
      m1.program[0] = Muncher::Right;
      m1.program[1] = Muncher::Down;
      m1.program[2] = Muncher::Left;
      m1.program[3] = Muncher::Up;

      m2.program[0] = Muncher::Right;
      m2.program[1] = Muncher::Down;
      m2.program[2] = Muncher::Left;
      m2.program[3] = Muncher::Up;
      m1.startNodeIndex = 0;
      m2.startNodeIndex = 47;
      
      std::vector<MyMuncher> ml;
      // ml.push_back(m1);
      ml.push_back(m2);
      getMultScore(ml);
      
    }

    void MYTEST(){  
      MyMuncher muncher;
      muncher.startTime = 0;
      muncher.program[0] = Muncher::Right;  // 3
      muncher.program[1] = Muncher::Up; // 0 
      muncher.program[2] = Muncher::Left; // 2
      muncher.program[3] = Muncher::Down; // 1
      // muncher.startNodeIndex = 126;
      // std::cout<<getScore(muncher)<<std::endl;
      
      std::cout << "graph size "<<my_graph.nodes.size()<<std::endl;
      std::set<Muncher::Instruction> insSet;
      insSet.insert(Muncher::Right);
      insSet.insert(Muncher::Up);
      insSet.insert(Muncher::Left);
      insSet.insert(Muncher::Down);

      for(std::set<Muncher::Instruction>::iterator it=insSet.begin(); 
	  it != insSet.end(); it++ ){
	muncher.program[0]= *it;
	for(std::set<Muncher::Instruction>::iterator it1=insSet.begin(); 
	    it1 != insSet.end(); it1++){
	  if(*it1 != muncher.program[0]){
	    muncher.program[1] = *it1;	  
	    for(std::set<Muncher::Instruction>::iterator it2=insSet.begin(); 
		it2 != insSet.end(); it2++){
	      if(*it2 != muncher.program[0] && *it2 != muncher.program[1] ){
		muncher.program[2] = *it2; 
		for(std::set<Muncher::Instruction>::iterator it3=insSet.begin(); 
		    it3 != insSet.end(); it3++){
		  if(*it3!=muncher.program[0] && 
		     *it3!=muncher.program[1] &&
		     *it3!=muncher.program[2] ){
		    muncher.program[3] = *it3;

		    std::cout << muncher.program[0]<<muncher.program[1]
			      <<muncher.program[2]<<muncher.program[3]
			      << "-------------------"<< std::endl;
		    
		    for(int i = 0; i<my_graph.nodes.size(); ++i){
		      muncher.startNodeIndex = i;
		      node_map.clear();
		      std::cout<<getScore(muncher)<<std::endl;
		    }
		    


		  }		
		}

	      }
	    }

	  }
	}

      }


    }

  private:
    NodeGraph my_graph;
    MuncherList my_muncher_list;
    std::map<int, int> node_map; 
    /// get score for a muncher. 
    int getScore(const MyMuncher &m);
    /// get score of multiple muncher
    void getMultScore(const MyMuncherList& ml);
    
    
    /// check if next position is generated by the instruction. 
    /// if it is, return true 
    bool checkPos(Position currentPos, Position nextPos, Muncher::Instruction ins);
    
    /// check if the node has been taken, if not return true
    bool checkNodeId(int nodeId){
      if(node_map.count(nodeId) == 0){
	return true;
      }else{  // the position has been taken
	return false;
      }
    }
    
    /// current node move one step. 
    bool moveOneStep(int& currentNodeIndex, 
		     std::deque<Muncher::Instruction>& program,
		     int& score);

    /// compare the priority of two muncher
    bool insGL(Muncher::Instruction in1, Muncher::Instruction in2){
      int insInt1 = convertToP(in1);
      int insInt2 = convertToP(in2);
      if(insInt1 > insInt2){
	return true;
      }
      return false; 
    }
    /// convert enum to priority, the larger the higher
    int convertToP(Muncher::Instruction ins){
      switch(ins){
      case Muncher::Up:
	return 4;
      case Muncher::Left:
	return 3;
      case Muncher::Down:
	return 2;
      case Muncher::Right:
	return 1;
      default:
	std::cerr<<"error"<<std::endl;

      }
    }

    /// get all comminations of program
    std::vector< std::vector<Muncher::Instruction> > getAllCombinations(){
      std::set<Muncher::Instruction> insSet;
      insSet.insert(Muncher::Right);
      insSet.insert(Muncher::Up);
      insSet.insert(Muncher::Left);
      insSet.insert(Muncher::Down);
      std::vector<Muncher::Instruction> program(4);
      std::vector< std::vector<Muncher::Instruction> >programList;
      for(std::set<Muncher::Instruction>::iterator it=insSet.begin(); 
	  it != insSet.end(); it++ ){
	program[0]= *it;
	for(std::set<Muncher::Instruction>::iterator it1=insSet.begin(); 
	    it1 != insSet.end(); it1++){
	  if(*it1 != program[0]){
	    program[1] = *it1;	  
	    for(std::set<Muncher::Instruction>::iterator it2=insSet.begin(); 
		it2 != insSet.end(); it2++){
	      if(*it2 != program[0] && *it2 != program[1] ){
		program[2] = *it2; 
		for(std::set<Muncher::Instruction>::iterator it3=insSet.begin(); 
		    it3 != insSet.end(); it3++){
		  if(*it3!=program[0] && 
		     *it3!=program[1] &&
		     *it3!=program[2] ){
		    program[3] = *it3;
		    /*
		    std::cout << program[0]<< program[1]
			      << program[2]<< program[3]
			      << "-------------------"<< std::endl;
		    */
		    programList.push_back(program);
		    
		  }		
		}

	      }
	    }

	  }
	}

      }
      return programList;
      
    
    }

    

  };



}


#endif
