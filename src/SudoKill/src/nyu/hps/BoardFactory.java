package nyu.hps;

import java.util.Random;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

public class BoardFactory {
  private static final int solvedBoard[][][] = {
      {
        {3, 5, 7, 8, 4, 1, 2, 6, 9},
        {1, 6, 8, 2, 9, 3, 5, 4, 7},
        {4, 2, 9, 5, 6, 7, 1, 3, 8},
        {8, 9, 2, 3, 5, 6, 4, 7, 1},
        {5, 7, 3, 1, 2, 4, 8, 9, 6},
        {6, 1, 4, 9, 7, 8, 3, 2, 5},
        {2, 8, 1, 7, 3, 9, 6, 5, 4},
        {9, 4, 5, 6, 1, 2, 7, 8, 3},
        {7, 3, 6, 4, 8, 5, 9, 1, 2}
      },
      {
        {1, 2, 5, 3, 7, 8, 9, 4, 6},
        {3, 7, 8, 9, 6, 4, 2, 1, 5},
        {4, 9, 6, 1, 2, 5, 8, 3, 7},
        {2, 6, 9, 4, 5, 3, 1, 7, 8},
        {8, 4, 1, 7, 9, 2, 6, 5, 3},
        {5, 3, 7, 8, 1, 6, 4, 9, 2},
        {9, 1, 2, 5, 8, 7, 3, 6, 4},
        {6, 5, 3, 2, 4, 9, 7, 8, 1},
        {7, 8, 4, 6, 3, 1, 5, 2, 9}
      },
      {
        {4, 1, 3, 6, 2, 7, 5, 8, 9},
        {7, 8, 5, 9, 4, 1, 3, 2, 6},
        {2, 9, 6, 5, 3, 8, 4, 1, 7},
        {5, 7, 2, 8, 9, 6, 1, 4, 3},
        {9, 4, 1, 7, 5, 3, 2, 6, 8},
        {6, 3, 8, 4, 1, 2, 7, 9, 5},
        {3, 2, 9, 1, 6, 5, 8, 7, 4},
        {8, 5, 4, 2, 7, 9, 6, 3, 1},
        {1, 6, 7, 3, 8, 4, 9, 5, 2}
      }
  };
  
  /**
   * Creates a new Sudoku board with valid solution. 
   * 
   * @param filledCells The number of cells to be filled.
   * 
   * @return the generated board.
   * 
   * @throws IllegalArgumentException if filledCells is > 81 or is negative.
   */
  public static Board create(int filledCells) {
    if (filledCells > 81 || filledCells < 0) {
      throw new IllegalArgumentException();
    }
    
    Random rand = new Random();
    final int board[][] = solvedBoard[rand.nextInt(solvedBoard.length)];
    final List<Move> moves = new ArrayList<Move>();
    
    for (int x = 0; x < 9; x++) {
      for (int y = 0; y < 9; y++) {
        moves.add(new Move(x, y, board[x][y]));
      }
    }
    
    Collections.shuffle(moves);
    
    final Board newBoard = new Board();
    for (int x = 0; x < filledCells; x++) {
      newBoard.set(moves.remove(0));
    }
    
    return newBoard;
  }
}
