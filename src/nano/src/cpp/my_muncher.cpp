#include "my_muncher.h"




namespace my_muncher
{

  /// a helper function to load data_file
  class LoadDataFuncHelper{
  public:
    inline void operator()(const Position& node, NodeGraph::NodeData *data) const{
      *data = node;
    }
  };



  void My_munchers::loadData(std::string path){
    std::cout << "loading data......"<< std::endl;
    // TODO 
    LoadDataFile(path, LoadDataFuncHelper(), &my_graph);
    return;
  }

  void My_munchers::analyze(){
    std::cout << "analyzing the data ...." << std::endl;
    // TODO 

    
    return;
  }

  void My_munchers::writeData(){
    std::cout << "writing the data ...." << std::endl;
    // TODO 
     

    return;
  }

  int My_munchers::getScore(const MyMuncher& m){
    // TODO   get the maximum score of a muncher. 
    int startNodeIndex  = m.startNodeIndex;
    Position startPos = my_graph.nodes[startNodeIndex].data;
    int startTime = m.startTime;
    while(true){
      // first step
      for(int i = 0; i< 4; ++i ){
	switch( m.program[i]){
	case Muncher::Up:
	  // if upper node is available. move up 
	  
	  break;
	case Muncher::Down:
	  break;
	case Muncher::Left:
	  break;
	case Muncher::Right:
	  break;
	}
      }


    }

  }


}


