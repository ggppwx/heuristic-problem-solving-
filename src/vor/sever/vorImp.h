int get_line(char buffer[1024], char line[100], int *pos){
  memset( line, 0, sizeof(char)*100);
  int start = *pos;
  int end = start;
  if (buffer[start] == '\0'){
    return 0; 
  }
  while(buffer[end] != '\n' && buffer[end] != '\0'){
    end ++;
  }
  *pos = end + 1;
  strncpy(line, &buffer[start],end - start+1);
  return 1;
}



int analyze(int board[1000][1000],
	    int player_score[20],
	    int Player_num,   // my player number
	    int totalpalyer , 
	    int N);  // total turns 
/*
strategy:




*/
