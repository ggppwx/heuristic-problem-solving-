package nyu.hps;

/**
 * An abstract implementation of the Player interface that includes all the boiler plate
 * codes.
 */
public abstract class AbstractPlayer implements Player {
  protected long time = 0;
  protected String name = "";
  
  /**
   * @inheritDoc
   */
  public String getName() {
    return name;
  }
  
  /**
   * @inheritDoc
   */
  public void addTime(long time) {
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
}
