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
    /// user-defined data structure for move
    struct move{
      int x;
      int y;
      int num;
      move(){}
      move(int x1, int y1, int num1){
	x = x1;
	y = y1;
	num = num1;
      }
    };

    /// board information
    int board[9][9];


    /// init state
    std::vector<move> initStates;
    /// player moves
    std::vector<move> playerMoves;

    /// record guessing moves for each player. 
    /// should be cleared before invoking minMax()
    std::vector<move> recMoves;

    /// convert states string to some data structure. 
    void saveStates(std::string states);

    /// find all possible moves according to current state and previous 
    /// player's move, return a array of all possible moves 
    std::vector<move> findPossibleMoves(move m);
    
    /// find initial possible moves
    std::vector<move> findInitPossibleMoves();


    /// minMax algorithm to analyze possible moves.  
    /// return the scoreo
    /// depth starts from 0
    int minMax(move m, int depth);
    
    /// get an approximite score. 
    /// this score is calculated according to a heuristic method  
    int getApproxScore();




    ///////////////////////////////////////////////////////////////////
    /// helper functions .
    /// get possible values for an entry
    std::vector<int> getValuesForEntry(int x, int y){
      // TODO 
      std::vector<int> vals;
      // check the column
      int temp[10]; // from 1 to 9
      for(int i = 0; i<9; ++i){
	if(board[x][i] != 0){
	  temp[board[x][i]] = 1;
	}
      }
      // check the row 
      for(int i=0; i<9; ++i){
	if(board[i][y] !=0){
	  temp[board[i][y]] = 1;
	}
      }
      // check the square
      for(int i = x/3*3; i<x/3+3; ++i){
	for(int j = y/3*3; j<y/3+3; ++j){
	  if(board[i][j] != 0){
	    temp[board[i][j]] = 1;
	  }
	}
      }
      for(int i=1; i<=9; ++i){
	if(temp[i] != 0){
	  vals.push_back(i);
	}
      }
      return vals;
    }

    
    
    
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
