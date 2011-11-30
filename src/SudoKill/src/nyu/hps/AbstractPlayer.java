package nyu.hps;

/**
 * An abstract implementation of the Player interface that includes all the boiler plate
 * codes. The child classes should be responsible in updating the time when performing a
 * move.
 */
public abstract class AbstractPlayer implements Player {
  protected long time = 0;
  protected final long timeLimit;
  protected String name = "";
  
  /**
   * @param timeLimit The time limit alloted for this player.
   */
  public AbstractPlayer(long timeLimit) {
    this.timeLimit = timeLimit;
  }
  
  /**
   * @inheritDoc
   */
  public String getName() {
    return name;
  }
  
  /**
   * Adds a time to this player.
   * 
   * @param time The amount of time to add in milliseconds.
   */
  protected void addTime(long time) {
    this.time += time;
  }
  
  /**
   * @inheritDoc
   */
  public long getTime() {
    return time;
  }
  
  /**
   * @inheritDoc
   */
  public void resetTime() {
    time = 0;
  }
  
  /**
   * @inheritDoc
   */
  public boolean isOverTime() {
    return time > timeLimit;
  }
}
