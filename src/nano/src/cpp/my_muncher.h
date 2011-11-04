#ifndef MY_MUNCHER_H
#define MY_MUNCHER_H
#include "nanomunchers_data.h"
#include "nanomunchers_serialize.h"
#include "data_file.h"
#include "gtest/gtest.h"
#include <algorithm>
#include <math.h>
#include <iostream>



namespace my_muncher{
  using namespace hps;
  typedef Graph<Position> NodeGraph;

  struct MyMuncher : Muncher{
    int startNodeIndex; 
    
  }; 


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
    

  private:
    NodeGraph my_graph;
    MuncherList my_muncher_list;
    
    /// get score for a muncher. 
    int getScore(const MyMuncher &m);
  };



}


#endif
