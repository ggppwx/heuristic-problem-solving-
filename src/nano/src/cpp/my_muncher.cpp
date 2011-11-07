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
    /// for TEST
    // std::cout << "current pos "<<currentPos.x<<"-"<<currentPos.y<<std::endl;
    // std::cout << "next pos "<<nextPos.x<<"_"<<nextPos.y<<std::endl;
    // std::cout << ins <<std::endl;
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
    int score = 0;
    int startTime = m.startTime;
    int currentNodeIndex = m.startNodeIndex;
    node_map[currentNodeIndex] = 1;
    std::deque<Muncher::Instruction> program(m.program,m.program+sizeof(m.program)/sizeof(Muncher::Instruction)); 

    while(true){
      // one step
      // get the current node 
      // std::cout << "current node "<<currentNodeIndex<<std::endl;  // TEST
      
      Graph<Position>::Node currentNode =  my_graph.nodes[currentNodeIndex];
      Position currentPos = currentNode.data;
      // std::cout << currentPos.x<< "-"<<currentPos.y<<std::endl;
      bool move_flag = false;
      
      /// for test
      std::cout << "try------- " << program.front() << std::endl;
      
      for(int i = 0; i< 4; ++i ){
	std::cout << "trying "<<program.front()<<std::endl;
	for(int j = 0; j< currentNode.adjacencyList.size(); ++j ){
	  Graph<Position>::Node* adjNodePtr = currentNode.adjacencyList[j];
	  if (checkPos(currentPos, adjNodePtr->data, program.front()) ){
	    int nextNodeIndex = GetNodeId(my_graph, adjNodePtr);
	    
	    // std::cout<<"next node id "<<nextNodeIndex<<std::endl;
	    
	    if(checkNodeId(nextNodeIndex)){
	      // move
	      //std::cout << "moving to "<< nextNodeIndex << std::endl;
	      
	      score ++; 
	      currentNodeIndex = nextNodeIndex;
	      node_map[currentNodeIndex] = 1; 
	      move_flag = true;
	    }
	    break;
	  }
	}
	program.push_back(program.front());
	program.pop_front();
	if(move_flag){
	  break;
	}

      }
      if(!move_flag){ // cannot move anymore
	break;
      }
       
    }
    return score; 
  } // end of the function


  void My_munchers::getMultScore(const MyMuncherList& ml){
    int globleTime = 0;
    std::map<int, int> currentNodeIndex;
    while(true){ // one step
      for(int muncherId = 0; muncherId < ml.size(); ++muncherId){
	if(ml[muncherId].startTime == globleTime){ // drop this 
	  std::cout << "drop muncher "<<muncherId<<std::endl;
	  currentNodeIndex[muncherId] = ml[muncherId].startNodeIndex;
	  
	}
      }

      
      for(std::map<int,int>::iterator it = currentNodeIndex.begin();
	  it!= currentNodeIndex.end();it++){
	int cNode = it->second; // current node for muncher it->first. 
	





      }

      // move one step. 





      globleTime++;
    }

  }
  


}  // end of namespace.  

