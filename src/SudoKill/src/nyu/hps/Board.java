package nyu.hps;

import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * A class representing a Sudoku board. The board uses the same coordinate convention with
 * computer graphics, where (0, 0) refers to the top left corner.
 */
public class Board {
  public static final int LENGTH = 9;
  public static final int WIDTH = 9;
  public static final int MAX_VAL = 9;
  private static final int BOARD_SIZE = LENGTH * WIDTH;
  
  private final Deque<Move> moveHistory = new LinkedList<Move>();
  private final int board[] = new int[BOARD_SIZE];
  
  public Board() {
    Arrays.fill(board, 0);
  }
  
  /**
   * Build a borad from a history of moves.
   * 
   * @param history The move history.
   */
  public Board(ReadOnlyHistory history) {
    Arrays.fill(board, 0);
    
    for (Move move: history) {
      set(move);
    }
  }
  
  /**
   * @return the last move performed on this board.  
   */
  public Move getLastMove() {
    return moveHistory.peek();
  }
  
  private int calcOffset(int x, int y) {
    return x + y * WIDTH;
  }
  
  /**
   * @return the history of moves performed on this board.
   */
  public ReadOnlyHistory viewHistory() {
    return new ReadOnlyHistory(moveHistory);
  }
  
  /**
   * Sets the moves on this board. Note that no checking is done on the validity of
   * the move other than whether it is out of bounds.
   * 
   * @param move The move to make.
   * 
   * @throw ArrayIndexOutOfBoundsException
   */
  public void set(Move move) {
    if (!move.equals(Move.Marker)) {
      final int x = move.x;
      final int y = move.y;
    
      if (x >= LENGTH || x < 0 || y >= WIDTH || y < 0) {
        throw new ArrayIndexOutOfBoundsException("(" + x + ", " + y + ") is out of bounds.");
      }
    
      board[calcOffset(x, y)] = move.n;
    }
    
    moveHistory.push(move);
  }
  
  /**
   * Gets the particular value on the given location of the board.
   * 
   * @param x
   * @param y
   * 
   * @return the number in the cell.
   * 
   * @throw ArrayIndexOutOfBoundsException
   */
  public int get(int x, int y) {
    if (x >= LENGTH || x < 0 || y >= WIDTH || y < 0) {
      throw new ArrayIndexOutOfBoundsException("(" + x + ", " + y + ") is out of bounds.");
    }
    
    return board[calcOffset(x, y)];
  }
  
  /**
   * @param x
   * @param y
   * 
   * @return true if the row and column of the given cell is already filled with
   *   numbers.
   */
  public boolean rowColFilled(int x, int y) {
    for (int i = 0; i < LENGTH; i++) {
      if (get(i, y) == 0) {
        return false;
      }
    }
    
    for (int j = 0; j < WIDTH; j++) {
      if (get(x, j) == 0) {
        return false;
      }
    }
    
    return true;
  }
  
  /**
   * Checks if the move that is about to be made is valid. This method assumes that
   * the current board state is valid. 
   * 
   * @param move The move.
   * @param checkAlignment Checks to see if the move aligns with the last move
   *   horizontally or vertically (except for cases when the horizontal and
   *   vertical cells are completely filled. 
   * 
   * @return true if valid.
   */
  public boolean isValid(Move move, boolean checkAlignment) {
    boolean isValid = false;
    
    if (move != null && move.n != 0 && get(move.x, move.y) == 0) {
      if (checkAlignment) {
        final Move lastMove = getLastMove();
            
        if (!lastMove.equals(Move.Marker) && !rowColFilled(lastMove.x, lastMove.y) &&
            move.x != lastMove.x && move.y != lastMove.y) {
          return false;
        }
      }
      
      final Set<Integer> numbers = getValidNumbers(move.x, move.y);
      isValid = numbers.contains(move.n);
    }
    
    return isValid;
  }
  
  /**
   * Checks if the move that is about to be made is valid. This method assumes that
   * the current board state is valid.
   * 
   * @param move The move.
   * 
   * @return true if valid.
   */
  public boolean isValid(Move move) {
    return isValid(move, false);
  }
  
  /**
   * @return the set of all valid numbers for Sudoku.
   */
  private static Set<Integer> validNumbers() {
    final Set<Integer> numbers = new HashSet<Integer>();
    
    for (int x = 1; x <= MAX_VAL; x++) {
      numbers.add(x);
    }
    
    return numbers;
  }
  
  /**
   * @param row
   * @return the set of valid numbers that can be used for a row without violating
   *   the Sudoku rule.
   */
  private Set<Integer> validNumbersForRow(int row) {
    final Set<Integer> numbers = validNumbers();
    
    for (int x = 0; x < WIDTH; x++) {
      numbers.remove(get(x, row));
    }
    
    return numbers;
  }
  
  /**
   * @param column
   * @return the set of valid numbers that can be used for a column without violating
   *   the Sudoku rule.
   */
  private Set<Integer> validNumbersForColumn(int column) {
    final Set<Integer> numbers = validNumbers();
    
    for (int y = 0; y < LENGTH; y++) {
      numbers.remove(get(column, y));
    }
    
    return numbers;
  }
  
  /**
   * @param x
   * @param y
   * 
   * @return the set of valid numbers within the 3x3 sector that contains x,y
   *   without violating the rules of Sudoku. 
   */
  private Set<Integer> validNumbersForSector(int x, int y) {
    final Set<Integer> numbers = validNumbers();
    
    final int startX = x / 3 * 3;
    final int endX = startX + 3;
    
    final int startY = y / 3 * 3;
    final int endY = startY + 3;
    
    for (int i = startX; i < endX; i++) {
      for (int j = startY; j < endY; j++) {
        numbers.remove(get(i, j));
      }
    }
    
    return numbers;
  }
  
  /**
   * Gets the set of numbers that can be set to a specific location in the board
   * without violating the rules in Sudoku. Note: the assumption here is that the
   * board is currently in a valid state. 
   * 
   * @param x
   * @param y
   * 
   * @return the set of numbers.
   */
  public Set<Integer> getValidNumbers(int x, int y) {
    final Set<Integer> numbers = validNumbersForRow(y);
    numbers.retainAll(validNumbersForColumn(x));
    numbers.retainAll(validNumbersForSector(x, y));
    
    return numbers;
  }
}
