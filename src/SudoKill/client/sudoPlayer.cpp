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
