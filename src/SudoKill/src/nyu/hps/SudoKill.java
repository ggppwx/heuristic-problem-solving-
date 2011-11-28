package nyu.hps;

import java.util.ArrayList;
import java.awt.EventQueue;

/**
 * The SudoKill program.
 * 
 * @see http://cs.nyu.edu/courses/fall11/CSCI-GA.2965-001/sudokill.html
 */
public class SudoKill {
  /**
   * Program entry point.
   * 
   * 1st arg: the port number this program will listen to for new players.
   * 2nd arg: the number of cells to fill for the initial board.
   */
  public static void main(String args[]) {
    if (args.length != 2) {
      System.err.println("Usage: SudoKill <port> <# of filled cells>");
      System.exit(0);
    }
    
    final int port = Integer.parseInt(args[0]);
    final int filledCells = Integer.parseInt(args[1]);
    
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        PlayerLounge frame = new PlayerLounge(port, Constants.TOTAL_PLAYERS,
            new ArrayList<Player>(), filledCells);
        frame.setVisible(true);
      }
    });
  }
}
