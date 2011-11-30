#ifndef _HPS_SUDOKILL_PLAYER_H
#define _HPS_SUDOKILL_PLAYER_H
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h> 
#include <iostream>

namespace hps{
namespace sudokill{
  class player{
  public:
    player(){}
    /// constructor. connect to socket. 
    player(const char* host, int port){
      init(host, port);
    }
    virtual ~player(){
      close(sockfd);
    }

  protected:
    std::string readStates();
    void writeStates(std::string str);

  private:
    void init(const char* host, int port){
      struct sockaddr_in serv_addr;
      struct hostent *server;

      sockfd = socket(AF_INET,SOCK_STREAM,0);
      if(sockfd < 0){
	error("could not get sock");
      }
      server = gethostbyname(host);
      if(server == NULL){
	error("could not ge the server");
      }
      bzero((char*)&serv_addr, sizeof(serv_addr));
      bcopy((char*)server->h_addr, (char*)&serv_addr.sin_addr.s_addr, server->h_length);
      serv_addr.sin_family = AF_INET;
      serv_addr.sin_port = htons(port);
      if(connect(sockfd, (struct sockaddr *)&serv_addr,sizeof(serv_addr)) < 0){
	error("could not connect to the server");
      }

    }
    int sockfd;
    void error(const char* msg){
      perror(msg);
      exit(0);
    }
    
  };
  
}
 using namespace sudokill;


}
#endif  // _HPS_SUDOKILL_PLAYER_H
