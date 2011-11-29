#ifndef _HPS_SUDOKILL_SUDOPLAYER_H
#define _HPS_SUDOKILL_SUDOPLAYER_H
#include "player.h"

namespace hps{
namespace sudokill{
  class sudoPlayer : public player{
  public:
    sudoPlayer();
    sudoPlayer(int port, char* name);
    ~sudoPlayer(){}
  private:

  };

}
 using namespace sudokill;
}
#endif
