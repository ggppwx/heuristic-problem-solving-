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

  bool My_munchers::checkPos(Position currentPos, Position nextPos, Muncher::Instruction ins){
     switch(ins){
      case Muncher::Up:
	if(nextPos.x - currentPos.x == 0 && nextPos.y - currentPos.y > 0){
	  return true;
	}
	return false;
      case Muncher::Down:
	if(nextPos.x - currentPos.x == 0 && nextPos.y - currentPos.y < 0){
	  return true;
	}
	return false;
      case Muncher::Right:
	if(nextPos.x - currentPos.x > 0 && nextPos.y - currentPos.y == 0){
	  return true;
	}
	return false;
      case Muncher::Left:
	if(nextPos.x - currentPos.x < 0 && nextPos.y - currentPos.y == 0){
	  return true;
	}
	return false;
      default:
	std::cerr <<"error, adj node is the same node"<< std::endl;
	return false;
      }

  }


  int My_munchers::getScore(const MyMuncher& m){
    // TODO   get the maximum score of a muncher. 
    int startNodeIndex  = m.startNodeIndex;
    Position startPos = my_graph.nodes[startNodeIndex].data;
    int startTime = m.startTime;
    int currentNodeIndex = startPos;
    while(true){
      // first step
      // get the current node 
      Graph::Node currentNode =  my_graph.nodes[currentNodeIndex];
      Position currentPos = currentNode.data;
      
      for(int i = 0; i< 4; ++i ){

	for(int i = 0; i< currentNode.adjacencyList.size(); ++i ){
	    Graph::Node* adjNodePtr = currentNode.adjacencyList[i];
	    if (checkPos(currentPos, adjNodePtr->data, m.program[i]) ){
	      int nextNodeIndex = GetNodeId(my_graph, adjNodePtr);
	      if(checkNodeId(nextNodeIndex)){
		// move
		currentNodeIndex = nextNodeIndex;
		node_map[currentNodeIndex] = 1; 
	      }
	    }
	}
	/*
	switch( m.program[i]){
	case Muncher::Up:
	  // if upper node is available. move up
	  for(int i = 0; i< currentNode.adjacencyList.size(); ++i ){
	    Graph::Node* adjNodePtr = currentNode.adjacencyList[i];
	    //	    Position adjPos  = currentNode.adjacencyList[i]->data;
	    if (checkPos(currentPos, adjNodePtr->data, Muncher::Up) ){
	      int upNodeIndex = GetNodeId(my_graph, adjNodePtr);
	      if(checkNodeId(upNodeIndex)){
		// move up 
		currentNodeIndex = upNodeIndex;
		node_map[currentNodeIndex] = 1; 
	      }
	    }
	  }
	  break;
	case Muncher::Down:
	  break;
	case Muncher::Left:
	  break;
	case Muncher::Right:
	  break;
	}
      }
     */

    }

  }







  } // end of the class 


