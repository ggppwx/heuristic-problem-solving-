package nyu.hps;

/**
 * Represents a valid move in Sudokill. The x and y represents the coordinate
 * of the move. An x,y of 0,0 represents the upper left corner of the Sudoku board.
 * The n represents the number set to x,y.
 */
public class Move {
  /**
   * Special move token used for marking the move history. 
   */
  public static final Move Marker = new Move(-1, -1, -1);
  
  public final int x;
  public final int y;
  public final int n;
  
  public Move(int x, int y, int n) {
    this.x = x;
    this.y = y;
    this.n = n;
  }
  
  /**
   * Creates a new move instance using the following string format:
   * 
   * <x> <y> <n>
   * 
   * where x and y are the coordinates and n is the number to set. Ex.
   * 
   * Move move = new Move("1 3 6");
   * 
   * @param str The input in string format.
   * @throws IllegalArgumentException
   */
  public Move(String str) throws IllegalArgumentException {
    String split[] = str.split(" ");
    
    if (split.length != 3) {
      throw new IllegalArgumentException("Wrong string format. Should be: <x> <y> <n>");
    }
    else {
      x = Integer.parseInt(split[0]);
      y = Integer.parseInt(split[1]);
      n = Integer.parseInt(split[2]);
    }
  }
  
  @Override
  public String toString() {
    return x + " " + y + " " + n;
  }
  
  @Override
  public boolean equals(Object other) {
    boolean ret = false;
    
    if (other == this) {
      ret = true;
    }
    else if (other instanceof Move){
      Move otherMove = (Move) other;
      ret = (otherMove.x == x && otherMove.y == y && otherMove.n == n);
    }
    
    return ret;
  }
  
  @Override
  public int hashCode() {
    /*
     * Formula adopted from Effective Java 2nd Ed. by Joshua Bloch
     */
    int result = 17;
    
    result = 31 * result + x;
    result = 31 * result + y;
    result = 31 * result + n;
    
    return result;
  }
}

