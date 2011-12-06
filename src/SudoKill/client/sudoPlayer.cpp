#include "sudoPlayer.h"
#include "assert.h"
namespace{
  //////////////////////////////////////////////////////////////
  /// helper functions in unnamed space 
  std::vector<int> stringToList(std::string temp){
    std::vector<int> res;
    std::istringstream tempis(temp);
    std::string xStr, yStr, numStr;
    tempis >> xStr >> yStr >> numStr;
    res.push_back(atoi(xStr.c_str()));
    res.push_back(atoi(yStr.c_str()));
    res.push_back(atoi(numStr.c_str()));
    return res;
  }


  std::string IntToString(int i){
    std::stringstream ss;
    ss << i;
    return ss.str();
  }


}

namespace hps{
namespace sudokill{

  sudoPlayer::sudoPlayer(const char* host, int port, const char* name)
    :player(host,port)
  {
    player::writeStates("SUDOKILL_PLAYER\n"+std::string(name)+"\n");
    for(int i = 0; i<9; ++i){
      for(int j=0; j<9; ++j){
	board[i][j] = 0;
      }
    }
    srand(time(NULL));
  }


  bool sudoPlayer::readInfo(){
    std::string states = player::readStates();
    // put states into data structure 
    std::cout<< states <<std::endl;
    saveStates(states);
  }

  void sudoPlayer::writeInfo(std::string input){
    std::cout<< input <<std::endl;
    player::writeStates(input+"\n");
  }

  std::string sudoPlayer::analyze(){
    // analyze. write code here. 
    // testBoard();
    sudoPlayer::move dummyMove;

    // clear recMoves before calling minMax
    recMoves.clear();
    minMax(dummyMove, -1);
    if(recMoves.empty()){
      std::cout<< "lose!!!"<<std::endl;
      return "0 0 1";
    }
    // the first move.
    int x = recMoves[recMoves.size() -1].x;
    int y = recMoves[recMoves.size() -1].y;
    int num = recMoves[recMoves.size() -1].num;
    std::string retStr = IntToString(x)+" "+IntToString(y)+" "+IntToString(num);
    std::cout << "move to "<<retStr<<std::endl;
    return retStr;
  }
  

  void sudoPlayer::saveStates(std::string states){
    // save sates
    initStates.clear();
    playerMoves.clear();
    std::istringstream is(states,std::istringstream::in);
    std::string temp;
    int stage = 0;
    while(getline(is, temp)){
      if(0 == temp.compare("MOVE START")){
	stage = 1;
	continue;
      }else if(0 == temp.compare("-1 -1 -1")){
	stage = 2;
	continue;
      }else if(0 == temp.compare("MOVE END")){
	break;
      }else{
	assert(stage != 0);
	std::vector<int> li = stringToList(temp);
	assert(li[2] != 0);

	board[li[0]][li[1]] = li[2];
	if(stage == 1){
	  move s(li[0],li[1],li[2]);
	  initStates.push_back(s);
	  
	}else if(stage == 2){
	  move m(li[0],li[1],li[2]);
	  playerMoves.push_back(m); 
	}	

      }

    }    

  }

  
  ///////////////////////////////////////////////////////////////////
  ///  main algorithm
  int sudoPlayer::minMax(sudoPlayer::move m, int depth){
    // std::cout<< "---minmax---depth"<<depth<<" ";
    // std::cout << m.x <<"-"<< m.y <<"-"<<m.num <<std::endl;
    bool meFlag ;
    if(0 == depth%2){ // i move, max node 
      meFlag = true;  
    }else{ // adversary move
      meFlag = false;
    }
    
    std::vector<move> pMoves;
    if(-1 == depth){
      meFlag = false;
      pMoves = findInitPossibleMoves();
      std::cout <<"------" <<pMoves.size()<<std::endl;
      /*
      std::cout << "================";
      for(int k = 0; k<pMoves.size(); k++){
	std::cout<< pMoves[k].x << pMoves[k].y<<pMoves[k].num << "-";
      }
      std::cout <<std::endl; 
      */
      // OK
      
      if(pMoves.size() > 100){
	MAX_DEPTH = 1;
      }else {
	MAX_DEPTH = 2;
      }
     
      
    }else{
      // make a move m first 
      board[m.x][m.y] = m.num;
      pMoves = findPossibleMoves(m);  // get next possible moves 

    }

    
    if(pMoves.empty()){ // m is leaf 
      if(meFlag){ // m is max node, indicating after i move, no further possible moves
	// i win
	// cancel move before return 
	if(depth != -1){
	  board[m.x][m.y] = 0;
	}
	return 10;
      }else{
	// cancel move before return.
	if(depth != -1){
	  board[m.x][m.y] = 0;
	}
	return -10;
      }

    }
    

    if(depth >= MAX_DEPTH){//reach the depth bound
      
      if(depth != -1){
	board[m.x][m.y] = 0;
      }
      return getApproxScore();
      
    }



    if(meFlag){ // m is min node.
      // if it is my turn. get all possible moves of adversary. 
      // get and save min score. 
      int minScore = 10000;
      int recX, recY, recNum;
      for(int i = 0; i<pMoves.size(); ++i){
	int tempScore = minMax(pMoves[i],depth+1);
	if(tempScore == -10){
	  minScore = tempScore;
	  recX = pMoves[i].x;
	  recY = pMoves[i].y;
	  recNum = pMoves[i].num;
	  break;
	}
	if(tempScore < minScore){
	  minScore = tempScore;
	  // save pMoves[i]
	  recX = pMoves[i].x;
	  recY = pMoves[i].y;
	  recNum = pMoves[i].num;
	  
	}
      }
      assert(minScore != 10000);
      sudoPlayer::move recMove(recX, recY, recNum);
      recMoves.push_back(recMove);

      // canncel move 
      if(depth != -1){
	board[m.x][m.y] = 0;
      }
      return minScore;
    }else{
      int maxScore = -10000;
      int recX, recY, recNum;
      for(int i = 0; i<pMoves.size(); ++i){
	int tempScore = minMax(pMoves[i],depth+1);
	if(tempScore == 10){
	  maxScore = tempScore;
	  recX = pMoves[i].x;
	  recY = pMoves[i].y;
	  recNum = pMoves[i].num;
	  break;
	}
	if(tempScore > maxScore){
	  maxScore = tempScore;
	  // save 
	  recX = pMoves[i].x;
	  recY = pMoves[i].y;
	  recNum = pMoves[i].num;

	}
      }
      assert(maxScore != -10000);
      sudoPlayer::move recMove(recX, recY, recNum);
      recMoves.push_back(recMove);

      // canncel move
      if(depth != -1){
	board[m.x][m.y] = 0;
      }
      return maxScore;
    }

  }

  // OK
  std::vector<sudoPlayer::move> sudoPlayer::findPossibleMoves(sudoPlayer::move m){
    // preX, preY indicates previous x, y 
    std::vector<move> possibleMoves;
    int preX = m.x; 
    int preY = m.y;
    // std::cout << preX << " " << preY<<std::endl;
    for(int i=0; i<9; ++i){
      if(board[preX][i] == 0){
	std::vector<int> vals = getValuesForEntry(preX, i);
	for(int j = 0; j<vals.size(); ++j){
	  move pM;
	  pM.x = preX;
	  pM.y = i;
	  pM.num = vals[j];
	  possibleMoves.push_back(pM);
	}
      }

    }
    for(int i=0; i<9; ++i){
      if(board[i][preY] == 0){
	std::vector<int> vals = getValuesForEntry(i, preY);
	for(int j = 0; j<vals.size(); ++j){
	  move pM;
	  pM.x = i;
	  pM.y = preY;
	  pM.num = vals[j];
	  possibleMoves.push_back(pM);
	}
      }
    }
    return possibleMoves;

  }

  // OK
  std::vector<sudoPlayer::move> sudoPlayer::findInitPossibleMoves(){
    if(playerMoves.empty()){
      //  init moves.
      std::vector<move> initPossibleMoves;
      for(int i = 0; i<9; ++i){
	for(int j = 0; j<9; ++j){
	  if(board[i][j] == 0){
	    std::vector<int> vals = getValuesForEntry(i,j);
	    for(int k = 0; k<vals.size(); k++){
	      move pM;
	      pM.x = i;
	      pM.y = j;
	      pM.num = vals[k];
	      initPossibleMoves.push_back(pM);
	    }
	  }
	}
      }
      return initPossibleMoves;
    }else{
      //std::cout << playerMoves[playerMoves.size() -1].x;
      return findPossibleMoves(playerMoves[playerMoves.size() -1]);
    }
    
  }
  
  int sudoPlayer::getApproxScore(){
    // TODO 
    // check current state, get a heuristic score. 
    // TEST: let's assign a random number between -10 to 10
    int randomScore = rand()%21 -10; 
    return randomScore;
  }
 

} 
}





int main(int argc, char* argv[]){
  // char* host = argv[1];
  if(argc < 3){
    fprintf(stderr, "usage %s port name\n",argv[0]);
    exit(0);
  }
  int portno = atoi(argv[1]);
  char* name = argv[2];
  hps::sudoPlayer s("localhost",portno,name); 
  while(s.readInfo()){
    s.writeInfo(s.analyze());
  }

  return 0;
}
