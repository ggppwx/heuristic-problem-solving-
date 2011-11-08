#include "nanomunchers_serialize_gtest.h"
#include "data_file_gtest.h"
#include "gtest/gtest.h"
#include "my_muncher.h"
#include <iostream>



#ifdef WIN32
#include <time.h>
#elif __APPLE__
#include <time.h>
#else
#include <sys/time.h>
#endif
using namespace _hps_nanomunchers_data_file_gtest_h_;

int main(int argc, char** argv)
{
  
  /*
  testing::InitGoogleTest(&argc, argv);
  return RUN_ALL_TESTS();
  */
  if(argc != 2){
    std::cerr<<"arguments not correct"<<std::endl;
    return 1;
  }

  my_muncher::My_munchers m;
  m.loadData(argv[1]);
  // m.analyze();
  // FOR TEST: OK
  // PrintNodeInfo(m.getGraph(), 0);
  // FOR TEST:
  
  // m.MYTEST();
  //m.MYSINGLETEST();
  //m.MYMULTTEST();
  // m.TEST_getGroups();
  m.analyze();
  // m.newAnalyze();
  m.TEST_my_muncher_list();
  m.writeData(argv[1]);
  return 0;
}
