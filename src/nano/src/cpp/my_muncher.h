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


  class My_munchers{
  public:
    My_munchers(){
      
    }
    ~My_munchers(){}
    void loadData(std::string path);
    void analyze();
    void writeData();

  private:
    NodeGraph my_graph;
    MuncherList my_muncher_list;
  };



}


#endif
