package nyu.hps;

public class TurnState {
  public final Player player;
  public final Move move;
  public final boolean isGameOver;
  
  public TurnState(Player player, Move move, boolean isGameOver) {
    this.player = player;
    this.move = move;
    this.isGameOver = isGameOver;
  }
}

