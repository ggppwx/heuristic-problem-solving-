package nyu.hps;

import java.util.Deque;
import java.util.Iterator;

/**
 * A read-only container for the move history.
 */
public class ReadOnlyHistory implements Iterable<Move> {
  private final Deque<Move> moveHistory;
  
  public ReadOnlyHistory(Deque<Move> moveHistory) {
    this.moveHistory = moveHistory;
  }
  
  /**
   * @return the last move.
   */
  public Move getLastMove() {
    return moveHistory.peek();
  }
  
  /**
   * @return an ordered iterator starting from the first move.
   */
  public Iterator<Move> iterator() {
    return moveHistory.descendingIterator();
  }
}