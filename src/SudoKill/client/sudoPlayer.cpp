#include "sudoPlayer.h"
#include "assert.h"
namespace{
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
  }


  bool sudoPlayer::readInfo(){
    std::string states = player::readStates();
    // TODO put states into data structure 
    std::cout<< states <<std::endl;
    saveStates(states);
  }

  void sudoPlayer::writeInfo(std::string input){
    player::writeStates(input);
  }

  std::string sudoPlayer::analyze(){
    // TODO analyze. write code here. 
    testBoard();
    




    return "";
  }
  

  void sudoPlayer::saveStates(std::string states){
    // TODO save sates
    std::istringstream is(states,std::istringstream::in);
    std::string temp;
    int stage = 0;
    while(getline(is, temp)){
      if(0 == temp.compare("MOVE START")){
	stage = 1;
      }else if(0 == temp.compare("-1 -1 -1")){
	stage = 2;
      }else if(0 == temp.compare("MOVE END")){
	break;
      }else{
	assert(stage != 0);
	std::vector<int> li = stringToList(temp);
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
    }else{
      // TODO make a move m first 
      board[m.x][m.y] = m.num;
      pMoves = findPossibleMoves(m);  // get next possible moves 

    }

    
    if(pMoves.empty()){ // m is leaf 
      if(meFlag){ // m is max node, indicating after i move, no further possible moves
	// i win
	// TODO cancel move before return 
	board[m.x][m.y] = 0;
	return 10;
      }else{
	// TODO cancel move before return.
	board[m.x][m.y] = 0;
	return -10;
      }

    }
    
    if(depth > 5){//reach the depth bound
      board[m.x][m.y] = 0;
      return getApproxScore();
    }



    if(meFlag){ // m is min node.
      // if it is my turn. get all possible moves of adversary. 
      // get and save min score. 
      int minScore = 10000;
      for(int i = 0; i<pMoves.size(); ++i){
	int tempScore = minMax(pMoves[i],depth+1);

	if(tempScore < minScore){
	  minScore = tempScore;
	  // save pMoves[i]
	  
	}
      }
      assert(minScore != 10000);
      // TODO canncel move 
      board[m.x][m.y] = 0;
      return minScore;
    }else{
      int maxScore = -10000;
      for(int i = 0; i<pMoves.size(); ++i){
	int tempScore = minMax(pMoves[i],depth+1);
	if(tempScore > maxScore){
	  maxScore = tempScore;
	  // save 
	}
      }
      assert(maxScore != -10000);
      // TODO canncel move
      board[m.x][m.y] = 0;
      return maxScore;
    }

  }

  
  std::vector<sudoPlayer::move> sudoPlayer::findPossibleMoves(sudoPlayer::move m){
    // TODO preX, preY indicates previous x, y 
    std::vector<move> possibleMoves;
    int preX = m.x; 
    int preY = m.y;
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

  
  std::vector<sudoPlayer::move> sudoPlayer::findInitPossibleMoves(){
    if(playerMoves.empty()){
      // TODO init moves.
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
      return findPossibleMoves(playerMoves[playerMoves.size() -1]);
    }
    
  }
  
  int sudoPlayer::getApproxScore(){
    // TODO 
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
