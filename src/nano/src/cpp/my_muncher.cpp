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
      bool move_flag = moveOneStep(currentNodeIndex, program, score);
      if(!move_flag){ // cannot move anymore
	break;
      }
      node_map[currentNodeIndex] = 1; 
       
    }
    return score; 
  } // end of the function


  void My_munchers::getMultScore(const MyMuncherList& ml){
    int globleTime = 0;
    // init 
    std::map<int, int> currentNodeIndex;
    std::map<int, std::deque<Muncher::Instruction> > program;
    std::map<int, int> score;    
    for(int i =0; i< ml.size(); ++i){
      currentNodeIndex[i] = ml[i].startNodeIndex;
      std::deque<Muncher::Instruction> p(ml[i].program, ml[i].program+sizeof(ml[i].program)/sizeof(Muncher::Instruction));
      program[i] = p;
      score[i] = 0;
      node_map[ml[i].startNodeIndex] = i;
    } 
    
    while(true){ // one step
      std::cout << "this is step "<<globleTime<<"-------------"<<std::endl;
      for(int muncherId = 0; muncherId < ml.size(); ++muncherId){
	if(ml[muncherId].startTime == globleTime){ // drop this 
	  std::cout << "drop muncher "<<muncherId<<std::endl;
	  currentNodeIndex[muncherId] = ml[muncherId].startNodeIndex;
	  
	}
      }
      
      bool all_move_flags = false;
      for(std::map<int,int>::iterator it = currentNodeIndex.begin();
	  it!= currentNodeIndex.end();it++){
	int muncherId = it->first;
	std::cout << "for muncher "<<muncherId<<std::endl;
	bool move_flag = moveOneStep(currentNodeIndex[muncherId],
				     program[muncherId],
				     score[muncherId]);
	/// currentNodeIndex, program, score has changed.	
	if(!move_flag){
	  std::cout<<"muncher "<<it->first<<" ends "<<std::endl;
	  continue;
	}else{
	  all_move_flags = true;
	}

	
	// up(0) has highest living priority, then left(2), then down(1), then right(3).
	if(node_map.count(currentNodeIndex[muncherId]) == 0){
	  node_map[currentNodeIndex[muncherId]] = muncherId;  // node taken by muncherId
	}else{ // the node has been taken. in this round
	  int oldMuncherId = node_map[currentNodeIndex[muncherId]];
	  int newMuncherId = muncherId;
	  Muncher::Instruction oldDir = program[oldMuncherId].back();
	  Muncher::Instruction newDir = program[newMuncherId].back();
	  if(insGL(newDir, oldDir)){ // higher priority
	    node_map[currentNodeIndex[newMuncherId]] = newMuncherId;  
	  }
	  
	}

      }
      if(!all_move_flags){
	std::cout << "all munchers died" << std::endl;
	break;
      }
      
     
      // if currentNodeIndex has duplicate value
      // choose the one with highest priority, kill other munchers
      for(std::map<int,int>::iterator it = currentNodeIndex.begin();
	  it!= currentNodeIndex.end();){// each muncher has node index
	int cMuncherIndex = it->first;
	int cNodeIndex = it->second;
	std::cout << "Muncher "<<cMuncherIndex<<" node "<< cNodeIndex <<std::endl;
	if(cMuncherIndex != node_map[cNodeIndex]){ // this muncher has been killed.
	  currentNodeIndex.erase(it);
	  
	}else{
	  it++;
	}
      }
      
      globleTime++;
    }

    for(int i =0; i< ml.size(); ++i){
      std::cout <<"score is "<<score[i] <<std::endl;
    }
  }


  bool My_munchers::moveOneStep(int& currentNodeIndex, 
				std::deque<Muncher::Instruction>& program,
				int& score){ // try 4 directions. 
    Graph<Position>::Node currentNode =  my_graph.nodes[currentNodeIndex];
    Position currentPos = currentNode.data;
    bool move_flag = false;

    /// for test
    
    for(int i = 0; i< 4; ++i ){ // try four direction. 
      std::cout << "trying "<<program.front()<<std::endl;
      for(int j = 0; j< currentNode.adjacencyList.size(); ++j ){
	Graph<Position>::Node* adjNodePtr = currentNode.adjacencyList[j];
	if (checkPos(currentPos, adjNodePtr->data, program.front()) ){
	  int nextNodeIndex = GetNodeId(my_graph, adjNodePtr);
	    
	  // std::cout<<"next node id "<<nextNodeIndex<<std::endl;
	  
	  if(checkNodeId(nextNodeIndex)){
	    // move
	    // std::cout << "moving "<<program.front()<<std::endl;
	    std::cout << "moving to "<< nextNodeIndex << std::endl;
	    
	    score ++; 
	    currentNodeIndex = nextNodeIndex;
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
      
    }  // end of try four directions 
    return move_flag;
  }
  


}  // end of namespace.  

