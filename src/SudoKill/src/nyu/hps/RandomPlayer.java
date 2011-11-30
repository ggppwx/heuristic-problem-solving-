package nyu.hps;

import java.util.Random;
import java.util.Set;

/**
 * An automated player that selects a random location and picks a valid number to
 * set as much as possible.
 */
public class RandomPlayer extends AbstractPlayer {
  /**
   * @param timeLimit The time limit alloted for this player in milliseconds.
   */
  public RandomPlayer(long timeLimit) {
    super(timeLimit);
    name = Constants.AI_PLAYER_NAME;
  }
  
  /**
   * @inheritDoc
   */
  public void kick() {
    // Do nothing
  }
  
  /**
   * @inheritDoc
   */
  public Move play(ReadOnlyHistory moveHistory) {
    long startTime = System.currentTimeMillis();
    
    Board board = new Board(moveHistory);
    Random rand = new Random();
    
    int x = 0;
    int y = 0;
    
    final Move lastMove = board.getLastMove(); 
    
    do {
      if (lastMove == null || lastMove.equals(Move.Marker) ||
          board.rowColFilled(lastMove.x, lastMove.y)) {
        x = rand.nextInt(Board.LENGTH);
        y = rand.nextInt(Board.WIDTH);
      }
      else if (rand.nextInt(2) < 1) {
        x = rand.nextInt(Board.LENGTH);
        y = lastMove.y;
      }
      else {
        x = lastMove.x;
        y = rand.nextInt(Board.WIDTH);
      }
    } while (board.get(x, y) != 0);
    
    Set<Integer> validNumbers = board.getValidNumbers(x, y);
    
    int number = 0;
    if (!validNumbers.isEmpty()) {
      Integer[] numArr = {};
      numArr = validNumbers.toArray(numArr);
      
      number = numArr[rand.nextInt(numArr.length)];
    }
    else {
      number = rand.nextInt(Board.MAX_VAL) + 1;
    }
    
    addTime(System.currentTimeMillis() - startTime);
    return new Move(x, y, number);
  }
}
