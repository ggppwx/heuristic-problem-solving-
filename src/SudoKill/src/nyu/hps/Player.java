package nyu.hps;

public interface Player {
  /**
   * @return the name of this player.
   */
  public String getName();
  
  /**
   * @return the accumulated time for this player.
   */
  public long getTime();
  
  /**
   * Resets the accumulated time of this player.
   */
  public void resetTime();

  /**
   * Kicks this player from the game. This is primarily used for performing resource
   * cleanup if needed. 
   */
  public void kick();
  
  /**
   * Performs a move based on the moves that has been done so far.
   * 
   * @param moveHistory The list of moves that has been done prior to this
   *   player's turn.
   *    
   * @return the move this player will take.
   */
  public Move play(ReadOnlyHistory moveHistory);
  
  /**
   * @return true if the player is already over the time limit.
   */
  public boolean isOverTime();
}
