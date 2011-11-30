#ifndef _HPS_SUDOKILL_SUDOPLAYER_H
#define _HPS_SUDOKILL_SUDOPLAYER_H
#include "player.h"
#include <sstream>
#include <string>
#include <vector>

namespace hps{
namespace sudokill{
  class sudoPlayer : public player{
  public:
    sudoPlayer(){}
    sudoPlayer(const char* host, int port, const char* name);
    ~sudoPlayer(){}
    bool readInfo();
    void writeInfo(std::string input);

    /// the main logic. analyze the states information,
    /// return the next move
    std::string analyze();

  private:
    /// data structure to store infomations

    /// board information
    int board[9][9];

    /// user-defined data structure for move
    struct move{
      int x;
      int y;
      int num;
      move(int x1, int y1, int num1){
	x = x1;
	y = y1;
	num = num1;
      }
    };

    /// init state
    std::vector<move> initStates;
    /// player moves
    std::vector<move> playerMoves;

    /// convert states string to some data structure. 
    void saveStates(std::string states);

    /////////////////////////////////////////////////////////////////
    /// TEST input.
    /// TEST: OK
    void testBoard(){
      std::cout << "--------------" <<std::endl;
      for(int i =0; i<initStates.size(); ++i){
	std::cout<<initStates[i].x<<"-"<<initStates[i].y<<"-"<<initStates[i].num
		 <<std::endl;
      }
      std::cout << "--------------" <<std::endl;
      for(int i =0; i<playerMoves.size(); ++i){
	std::cout<<playerMoves[i].x<<"-"<<playerMoves[i].y<<"-"<<playerMoves[i].num
		 <<std::endl;
      }
      
      std::cout << "test board" << std::endl;
      for(int i = 0; i<9; ++i){
	for(int j=0; j<9; ++j){
	  std::cout << board[j][i];
	}
	std::cout << std::endl;
      }
      std::cout << "--------------" <<std::endl;
      
    }
    
    
  };

}
 using namespace sudokill;
}
#endif
