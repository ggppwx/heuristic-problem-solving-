#ifndef _HPS_SUDOKILL_PLAYER_H
#define _HPS_SUDOKILL_PLAYER_H
namespace hps{
namespace sudokill{
  class player{
  public:
    player();
    /// constructor. connect to socket. 
    player(char* host, int port, char* name);
    virtual ~player(){}
    readStates();
    writeStates();
  private:
    init();
  };
  
}
 using namespace sudokill;


}
#endif  // _HPS_SUDOKILL_PLAYER_H
