package nyu.hps;

import java.util.*;

/**
 * Simple class for coordinating the Sudokill game.
 */
public class SudoKillGame {
  private final List<Player> players;
  private final Board board;
  
  private boolean isGameOver = false;
  private int nextPlayerIdx = 0;
  
  /**
   * Creates a new game instance.
   * 
   * @param players The players for this game instance.
   * @param filledCells The number of cells to fill in the initial board.
   */
  public SudoKillGame(List<Player> players, int filledCells) {
    this.players = players;
    
    board = BoardFactory.create(filledCells);
    board.set(Move.Marker);
  }
  
  /**
   * Warning: This method is not idempotent.
   * 
   * @return the player whose turn is next.
   */
  private Player nextPlayer() {
    Player next = players.get(nextPlayerIdx++);
    nextPlayerIdx %= players.size(); 
    
    return next;
  }
  
  /**
   * Kicks a player away from this game.
   * 
   * @param player The player to remove.
   * @return true if player was in this game.
   */
  public boolean kickPlayer(Player player) {
    boolean success = players.remove(player);
    
    if (success) {
      player.kick();
    }
    
    return success;
  }
  
  /**
   * @return true if game is over.
   */
  public boolean isGaveOver() {
    return isGameOver;
  }
  
  public ReadOnlyHistory viewHistory() {
    return board.viewHistory();
  }
  
  /**
   * Performs a single step in the game by letting the next player execute their move.
   * If the resulting move of the player will make the board state invalid, the move
   * will not be taken and the game will be over.
   * 
   * @return the information for performed in this step - the move, the player who made
   *   the move and whether the game is over. If the game was over prior to calling this
   *   method, the move and player will be both null. 
   */
  public TurnState step() {
    ReadOnlyHistory history = board.viewHistory();
    
    if (!isGameOver) {
      Player next = nextPlayer();
      Move move = null;
      
      try {
        move = next.play(history);
        
        if (next.isOverTime() || !board.isValid(move, true)){
          isGameOver = true;
        }
        else {
          board.set(move);
        }
      }
      catch (IllegalArgumentException ex) {
        System.err.println("Player " + next.getName() + ": " + ex.getMessage());
        isGameOver = true;
      }
      catch (ArrayIndexOutOfBoundsException ex) {
        System.err.println("Player " + next.getName() + ": " + ex.getMessage());
        isGameOver = true;
      }
      
      return new TurnState(next, move, isGameOver);
    }
    else {
      return new TurnState(null, null, true);
    }
  }
  
  /**
   * @return the players in this game.
   */
  public Iterable<Player> getPlayers() {
    return players;
  }
}
