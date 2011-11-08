#include "my_muncher.h"
#include <fstream>



namespace my_muncher
{

  /// a helper function to load data_file
  class LoadDataFuncHelper{
  public:
    inline void operator()(const Position& node, NodeGraph::NodeData *data) const{
      *data = node;
    }
  };

  void My_munchers::writeData(const std::string &out){
    std::cout << "writing the data ...." << std::endl;
    std::vector<Muncher> muncherList;
    for(int i =0; i< my_muncher_list.size(); ++i){
      Muncher m;
      m.startTime = my_muncher_list[i].startTime;
      int nodeId = my_muncher_list[i].startNodeIndex;
      m.startPos.x = my_graph.nodes[nodeId].data.x; // position
      m.startPos.y = my_graph.nodes[nodeId].data.y;
      m.program[0] = my_muncher_list[i].program[0];
      m.program[1] = my_muncher_list[i].program[1];
      m.program[2] = my_muncher_list[i].program[2];
      m.program[3] = my_muncher_list[i].program[3];
      muncherList.push_back(m);
    }
    std::string outName = out + "-output.txt";
    std::ofstream output(outName.c_str());
    WriteMuncherList(muncherList, &output);
    output.close();

    return;
  }
  
  void My_munchers::loadData(std::string path){
    std::cout << "loading data......"<< std::endl;
    LoadDataFile(path, LoadDataFuncHelper(), &my_graph);
    return;
  }

  void My_munchers::analyze(){
    std::cout << "analyzing the data ...." << std::endl;
    // TODO 
    // std::vector< std::vector<Muncher::Instruction> > programList = getAllCombinations();
    std::vector<int> nodeIndexList;
    for(int i = 0; i< my_graph.nodes.size();++i){
      nodeIndexList.push_back(i);
    }
    // std::cout <<nodeIndexList.size()<<std::endl; 
    Divide(nodeIndexList, 0 );
    std::deque<int> left;
    //newDivide(nodeIndexList,0,left);
    return;
  }

  void My_munchers::newAnalyze(){
    std::cout << "analyzing the data ...." << std::endl;
    // TODO 
    // std::vector< std::vector<Muncher::Instruction> > programList = getAllCombinations();
    std::vector<int> nodeIndexList;
    for(int i = 0; i< my_graph.nodes.size();++i){
      nodeIndexList.push_back(i);
    }
    // std::cout <<nodeIndexList.size()<<std::endl; 
    std::deque<int> left;
    newDivide(nodeIndexList,0,left);
    return;
  }
  
  MyMuncher My_munchers::process(const std::vector<int> group){
   	// in each group drop a muncher.
	// drop a muncher eat the most.   
	// probe all the nodes in this group.
	
	// before drop a muncher. save drop time. 
    std::cout << "------------process-----------------"<<std::endl;
	std::cout <<std::endl;
	MyMuncher m;
	int maxScore = -1;
	int bestNode = -1;
	Muncher::Instruction bestProgram[4];
	for(int j = 0; j<group.size(); ++j){
	  int nodeIndex = group[j];
	  m.startNodeIndex = nodeIndex;
	  m.startTime = -1;
	  for(int i = 0; i< 8 ;++i){ // for each node try 8 combinations
	    m.program[0] = comb[i][0]; // Muncher::Up;
	    m.program[1] = comb[i][1]; //Muncher::Right;
	    m.program[2] = comb[i][2]; //Muncher::Down;
	    m.program[3] = comb[i][3]; //Muncher::Left;
	    std::cout <<"muncher prog " <<m.program[0]<<m.program[1]
			      <<m.program[2]<<m.program[3]
			      << std::endl;
	    node_map = current_node_map; // load current node_map
	    int tempScore = getScore(m);
	    std::cout <<"temp score " <<tempScore<<std::endl;
	    if(tempScore > maxScore){
	      maxScore = tempScore;
	      // save program 
	      bestNode = nodeIndex;
	      bestProgram[0] = m.program[0];
	      bestProgram[1] = m.program[1];
	      bestProgram[2] = m.program[2];
	      bestProgram[3] = m.program[3];		
	    }

	  }	  
	}
	// get the best node 
	assert(bestNode != -1);
	MyMuncher bestMuncher; 
	bestMuncher.startNodeIndex = bestNode;
	// bestMuncher.startTime = 0;
	bestMuncher.program[0] = bestProgram[0];
	bestMuncher.program[1] = bestProgram[1];
	bestMuncher.program[2] = bestProgram[2];
	bestMuncher.program[3] = bestProgram[3];
	
	
	return bestMuncher;

  }

  std::vector<int> My_munchers::BFS(int nodeindex, std::map<int,int> &visit_nodes)
  {
    std::vector<int> queue;
    std::vector<int> group;
    NodeGraph::Node node = my_graph.nodes[nodeindex];
    queue.push_back(nodeindex);
    group.push_back(nodeindex);
    visit_nodes[nodeindex] = 1;
    while(queue.size() > 0){
      int orgindex = queue.back();
      queue.pop_back();
      NodeGraph::Node tmpnode = my_graph.nodes[orgindex];
      for(int i=0;i<tmpnode.adjacencyList.size();i++){
	int childindex = GetNodeId(my_graph,tmpnode.adjacencyList[i]);
	if(current_node_map.find(childindex) == current_node_map.end() 
	   && visit_nodes.find(childindex) == visit_nodes.end()){
	  std::cout<<"child "<<childindex<<std::endl;
	  queue.push_back(childindex);
	  group.push_back(childindex);
	  visit_nodes[childindex] = 1;
	}
	
      }
    }
    return group;
  }
  
  
  std::vector< std::vector<int> > My_munchers::getGroup(const std::vector<int> nodeIndexList){
    std::vector< std::vector<int> > groups;
    std::map<int, int> visit_nodes;
    for(int i =0; i< nodeIndexList.size(); ++i){
      if(current_node_map.find(nodeIndexList[i]) == current_node_map.end()
	 && visit_nodes.find(nodeIndexList[i]) == visit_nodes.end()){
	// std::cout<<"test:process node "<<i<<std::endl;
	std::vector<int> newgroup = BFS(nodeIndexList[i], visit_nodes);
	groups.push_back(newgroup);
      }
    }

    return groups;
    
  }

  std::deque<int> My_munchers::getTrace(const MyMuncher& m){
    // TODO   get the trace of a muncher. 
    std::deque<int> nodeTrace;
    int score = 0;
    int startTime = m.startTime;
    int currentNodeIndex = m.startNodeIndex;
    node_map[currentNodeIndex] = 1;
    std::deque<Muncher::Instruction> program(m.program,m.program+sizeof(m.program)/sizeof(Muncher::Instruction)); 
    
    while(true){
      // one step
      // get the current node 
      std::cout << "current node "<<currentNodeIndex<<std::endl;  // TEST
      nodeTrace.push_back(currentNodeIndex);
      bool move_flag = moveOneStep(currentNodeIndex, program, score);
      if(!move_flag){ // cannot move anymore
	break;
      }
      node_map[currentNodeIndex] = 1; 
       
    }
    return nodeTrace; 
  }


  int My_munchers::updateCurrentNode(const std::deque<int> &traceUpdated){
    for(int i=0;i<traceUpdated.size();++i){
      assert(current_node_map.count(traceUpdated[i]) == 0);
      current_node_map[traceUpdated[i]] = 1;
    }
    return traceUpdated.size();
  }


  void My_munchers::newDivide(const std::vector<int> l,int startTime,std::deque<int> left){
    // get group is due to current_node_map
    std::cout << l.size()<<std::endl;
    std::vector< std::vector<int> > groups =  getGroup(l);
    if(groups.empty()){
      return;
    }
    for(int i = 0; i< groups.size(); ++i){
      // do something in group i 
      std::cout<<"for group "<<i<<"---------------"<< left.size()<<std::endl;
      if(left.size() !=0 && groups[i].end() != std::find(groups[i].begin(),groups[i].end(),left.front()) ){ 
	// there's a muncher in group
	// finish the trace.
	std::deque<int> traceUpdated = left;
	std::deque<int> traceLeft;  // nothing in it 
	traceLeft.clear();
	int scoreForGroup = updateCurrentNode(traceUpdated);
	std::cout<<"group left!!  ";
	for(int j=0;j<groups[i].size();++j){
	  std::cout<<groups[i][j]<<" "; 
	}
	std::cout<<std::endl;
	std::cout<<"resumes from "<<left[0]
		 <<" max score "<<scoreForGroup<<std::endl;
       
	newDivide(groups[i],startTime + scoreForGroup,traceLeft );
	
      }else{ // no muncher in group i or muncher has died 
	MyMuncher mForGroup = process(groups[i]); // drop a new muncher.
	mForGroup.startTime = startTime;
	my_muncher_list.push_back(mForGroup);
	
	node_map = current_node_map;  //load current node_map
	std::deque<int> traceForGroup = getTrace(mForGroup);
	std::deque<int> traceUpdated ; // first half
	std::deque<int> traceLeft; // left

	for(int k = 0; k<traceForGroup.size(); k++){
	  if(k<=traceForGroup.size()/2){
	    traceUpdated.push_back(traceForGroup[k]);
	  }else{
	    traceLeft.push_back(traceForGroup[k]);
	  }
	}


	int scoreForGroup = updateCurrentNode(traceUpdated);
	// traceLeft for group
	std::cout<<"group ";
	for(int j=0;j<groups[i].size();++j){
	  std::cout<<groups[i][j]<<" "; 
	}
	std::cout<<std::endl;
	std::cout<<"starts from "<<mForGroup.startNodeIndex
		 <<" max score "<<scoreForGroup<<std::endl;
	
	
	newDivide(groups[i],startTime + scoreForGroup,traceLeft);
      }

    }
  }


  void My_munchers::Divide(const std::vector<int> l,int startTime){
    // get group is due to current_node_map
    std::cout << l.size()<<std::endl;
    std::vector< std::vector<int> > groups =  getGroup(l);
    if(groups.empty()){
      return;
    }
    for(int i = 0; i< groups.size(); ++i){
      // do something 
      std::cout<<"for group "<<i<<"---------------"<<std::endl;
      MyMuncher mForGroup = process(groups[i]);
      // get a muncher for this group.
      // save the muncher, startTime.
      mForGroup.startTime = startTime;
      my_muncher_list.push_back(mForGroup);

      node_map = current_node_map;  //load current node_map
      int scoreForGroup = getScore(mForGroup); // update node_map.
      current_node_map = node_map;  // after running this muncher the current_node_map
      std::cout<<"group ";
      for(int j=0;j<groups[i].size();++j){
	std::cout<<groups[i][j]<<" "; 
      }
      std::cout<<std::endl;
      std::cout<<"starts from "<<mForGroup.startNodeIndex
	       <<" max score "<<scoreForGroup<<std::endl;
      
      // get the startTime for groups split from this group  
      // int nextTime = startTime + scoreForGroup;
      Divide(groups[i],startTime + scoreForGroup);


    }
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
      // std::cout << "trying "<<program.front()<<std::endl;
      for(int j = 0; j< currentNode.adjacencyList.size(); ++j ){
	Graph<Position>::Node* adjNodePtr = currentNode.adjacencyList[j];
	if (checkPos(currentPos, adjNodePtr->data, program.front()) ){
	  int nextNodeIndex = GetNodeId(my_graph, adjNodePtr);
	    
	  // std::cout<<"next node id "<<nextNodeIndex<<std::endl;
	  
	  if(checkNodeId(nextNodeIndex)){
	    // move
	    // std::cout << "moving "<<program.front()<<std::endl;
	    // std::cout << "moving to "<< nextNodeIndex << std::endl;
	    
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

