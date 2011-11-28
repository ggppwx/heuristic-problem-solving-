package nyu.hps;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CancellationException;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * The window for displaying the game in progress.
 */
public class SudoKillFrame extends JFrame {
  private static final long serialVersionUID = 1L;
  
  private final int port;
  private final List<PlayerStats> playerStats = new ArrayList<PlayerStats>();
  private final SudoKillGame gameState;
  private final GameTask gameTask;
  private final int filledCells;
  
  // GUI stuffs
  private JPanel contentPane;
  private final JButton btnNewGame;
  private final BoardPanel boardPanel;
  
  private static class PlayerStats {
    public final Player player;
    public final JLabel lblName;
    public final JLabel lblTime;
    
    public PlayerStats(Player player) {
      this.player = player;
      lblName = new JLabel(player.getName());
      lblTime = new JLabel();
      refreshTime();
    }
    
    public void refreshTime() {
      lblTime.setText(" " + player.getTime() + " msec");
    }
  }
  
  /**
   * Worker task for running the game in the background.
   */
  private class GameTask extends SwingWorker<TurnState, TurnState> {
    private final SudoKillGame gameState;
    
    public GameTask(SudoKillGame gameState) {
      this.gameState = gameState;
    }
    
    public TurnState doInBackground() throws Exception {
      TurnState turn = null;
      
      do {
        turn = gameState.step();
        
        if (isCancelled()) {
          System.out.println("Cancelled game background thread.");
          return null;
        }
        
        publish(turn);
      } while (!turn.isGameOver);
      
      return turn;
    }
    
    public void process(List<TurnState> turns) {
      // TODO: rm - for debugging only
      for (TurnState turn: turns) {
        System.out.println(turn.player.getName() + ": " + turn.move);
      }
      
      boardPanel.repaint();
      refreshPlayerStats();
    }
    
    public void done() {
      try {
        displayLosingMessage(get());
      }
      catch (InterruptedException ex) {
        System.out.println("Cannot get result: Game thread was interrupted.");
      }
      catch (CancellationException ex) {
        System.out.println("Cannot get result: Game thread was cancelled.");
      }
      catch (ExecutionException ex) {
        System.err.println(ex.getMessage());
        ex.printStackTrace(System.err);
      }
      
      btnNewGame.setEnabled(true);
    }
    
    /**
     * Displays a dialog box containing a brief message containing the reason for losing.
     * 
     * @param lastTurn The last turn executed when the game was over.
     */
    public void displayLosingMessage(TurnState lastTurn) {
      String playerName = lastTurn.player.getName();
      
      if (lastTurn.isGameOver) {
        if (lastTurn.player.getTime() > Constants.TIME_LIMIT) {
          JOptionPane.showMessageDialog(null, playerName + " timed out!");
        }
        else {
          JOptionPane.showMessageDialog(null, playerName
              + " performed an illegal move: " + lastTurn.move);
        }
      }
    }
  }
  
  /**
   * GUI component for the Sudoku Board.
   */
  private static class BoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private final SudoKillGame gameState;
    private final int width;
    private final int minorCellWidth;
    private final int cellCenterPt;
    
    public BoardPanel(SudoKillGame gameState, int width) {
      this.gameState = gameState;
      this.width = width;
      minorCellWidth = width / 9;
      cellCenterPt = minorCellWidth / 2;
      
      setFont(new Font("Courier", Font.BOLD, (int)(minorCellWidth * 0.7)));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
      final Color origColor = g.getColor();
      
      drawEmptyBoard(g);
      
      g.setColor(Color.BLACK);
      for (Move move: gameState.viewHistory()) {
        drawMove(g, move);
      }
      
      g.setColor(origColor);
    }
    
    private void drawMove(Graphics g, Move move) {
      if (!move.equals(Move.Marker)) {
        final FontMetrics metrics = g.getFontMetrics();
        final String numberStr = String.valueOf(move.n);
      
        final int xOffset = cellCenterPt - metrics.stringWidth(numberStr) / 2;
        final int yOffset = cellCenterPt + metrics.getAscent() / 2;
      
        g.drawString(numberStr, move.x * minorCellWidth + xOffset,
            move.y * minorCellWidth + yOffset);
      }
    }
    
    private void drawEmptyBoard(Graphics g) {
      final int majorCellWidth = width / 3; 
      
      g.setColor(Color.WHITE);
      g.fillRect(0, 0, width, width);
      
      g.setColor(Color.CYAN);
      
      for (int nthMajorCell = 0; nthMajorCell < 9; nthMajorCell += 2) {
        int row = nthMajorCell / 3;
        int column = nthMajorCell % 3;
        
        g.fillRect(row * majorCellWidth, column * majorCellWidth,
            majorCellWidth, majorCellWidth);
      }
      
      g.setColor(Color.BLACK);
      
      // Draw grid lines
      for (int x = minorCellWidth; x < width; x += minorCellWidth) {
        g.drawLine(x, 0, x, width);
      }
      
      for (int y = minorCellWidth; y < width; y += minorCellWidth) {
        g.drawLine(0, y, width, y);
      }
    }
  }
  
  /**
   * Creates the frame.
   * 
   * @param port The port number used for listening for new users to be used
   *   when re-opening the player lounge.
   * @param playerList List of players playing.
   * @param filledCells The number of cells to fill in the initial board.
   */
  public SudoKillFrame(int port, List<Player> playerList, int filledCells) {
    this.port = port;
    this.filledCells = filledCells;
    
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 630, 730);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(new BorderLayout(0, 0));
    setContentPane(contentPane);
    
    Box verticalBox = Box.createVerticalBox();
    contentPane.add(verticalBox, BorderLayout.NORTH);
    
    btnNewGame = new JButton("New Game");
    btnNewGame.setEnabled(false);
    btnNewGame.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        newGame();
      }
    });
    contentPane.add(btnNewGame, BorderLayout.SOUTH);
    
    gameState = new SudoKillGame(playerList, Constants.TIME_LIMIT, filledCells);
    
    boardPanel = new BoardPanel(gameState, 630);
    contentPane.add(boardPanel);
    
    for (Player player: playerList) {
      PlayerStats newPlayer = new PlayerStats(player);
      playerStats.add(newPlayer);
      
      Box horizontalBox = Box.createHorizontalBox();
      horizontalBox.add(newPlayer.lblName);
      horizontalBox.add(newPlayer.lblTime);
      
      verticalBox.add(horizontalBox, BorderLayout.NORTH);
    }
    
    gameTask = new GameTask(gameState);
    gameTask.execute();
  }

  /**
   * Opens the player lounge window pre-populated with the current players. 
   */
  private void newGame() {
    gameTask.cancel(true);
    
    final List<Player> externalPlayers = new ArrayList<Player>(); 
    
    for (PlayerStats stat: playerStats) {
      Player player = stat.player;
      player.resetTime();
      
      if (!player.getName().equals(Constants.AI_PLAYER_NAME)) {
        externalPlayers.add(player);
      }
    }
    
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        PlayerLounge frame = new PlayerLounge(port, Constants.TOTAL_PLAYERS,
            externalPlayers, filledCells);
        frame.setVisible(true);
      }
    });
    
    dispose();
  }
  
  /**
   * Refreshes the timer display for all players.
   */
  private void refreshPlayerStats() { 
    for (PlayerStats player: playerStats) {
      player.refreshTime();
    }
  }
}
