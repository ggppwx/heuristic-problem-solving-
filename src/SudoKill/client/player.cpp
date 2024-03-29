#include "player.h"

namespace hps{
namespace sudokill{


  std::string player::readStates(){
    std::cout<<"reading ..."<<std::endl;
    char buffer[1256];
    int n;
    bzero(buffer, 1256);
    n = read(sockfd,buffer,1255);
    if(n < 0){
      error("error in reading socket");
    }
    return std::string(buffer);
  }


  void player::writeStates(std::string str){
    std::cout<<"writing ... "<<std::endl;
    char buffer[1256];
    int n;
    bzero(buffer, 1256);
    strcpy(buffer, str.c_str());
    n = write(sockfd, buffer, strlen(buffer));
    if( n < 0){
      error("error in writing socket");
    }
  }


}

}
