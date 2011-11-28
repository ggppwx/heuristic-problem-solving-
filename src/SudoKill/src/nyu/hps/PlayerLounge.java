package nyu.hps;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingWorker;
import java.awt.EventQueue;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.net.*;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Window frame ui for waiting for players to connect to this game server.
 */
public class PlayerLounge extends JFrame {
  /*
   * Class invariants:
   * 1. playerDisp should only contain references to outside players (instances of
   *    SocketPlayer) otherwise the player field should point to null.
   *    
   * 2. The socketSemaphore should always contain the number of available slots for
   *    outside players.  
   */
  
  // TODO: create player radio buttons dynamically 
  
  private static final long serialVersionUID = 1L;
  
  private final int port;
  private final int filledCells;
  private final Semaphore socketSemaphore;
  private final PortConnection portConnTask;
  
  // GUI Stuffs
  private JPanel contentPane;
  private final List<PlayerDisplayGUI> playerDisp = new ArrayList<PlayerDisplayGUI>();
  
  private static class PlayerDisplayGUI {
    public final JButton kickButton;
    public final JLabel label;
    public Player player;
    
    public PlayerDisplayGUI(Player player, JButton kickButton, JLabel label) {
      this.player = player;
      this.kickButton = kickButton;
      this.label = label;
    }
  }
  
  /**
   * A task that listens to the port in the background and creates a new socket for
   * every player who connects to this lounge.
   */
  private class PortConnection extends SwingWorker<Void, Socket> {
    private final int port;
    private static final int ACCEPT_TIMEOUT = 1000; // 1 sec

    public PortConnection(int port) {
      this.port = port;
    }
    
    public Void doInBackground() throws Exception {
      /*
       * Note: Always make sure that the serverSocket was closed properly. Otherwise,
       * the this worker thread will encounter an error when trying to open the port
       * next time.
       */
      
      ServerSocket serverSocket = new ServerSocket(port);
      serverSocket.setSoTimeout(ACCEPT_TIMEOUT);
      
      while (true) {
        try {
          socketSemaphore.acquire();
        } catch (InterruptedException ex) {
          System.out.println("Interrupted socket thread.");
          serverSocket.close();
          return null;
        }

        try {
          Socket socket = serverSocket.accept();
          socket.setKeepAlive(false);
          socket.setTcpNoDelay(true);
          publish(socket);
        } catch (SocketTimeoutException ex) {
          // release the sem since no socket was actually acquired
          socketSemaphore.release();
        }

        if (isCancelled()) {
          System.out.println("Cancelled socket thread.");
          serverSocket.close();
          return null;
        }
      }
    }
    
    public void process(List<Socket> newSockets) {
      for (Socket socket: newSockets) {
        try {
          if (!addPlayer(new SocketPlayer(socket))) {
            System.err.println("Cannot add new player: no empty slot.");
          }
        }
        catch (IllegalArgumentException ex) {
          System.err.println(ex.getMessage());
          ex.printStackTrace(System.err);
        }
        catch (IOException ex) {
          System.err.println(ex.getMessage());
          ex.printStackTrace(System.err);
        }
      }
      
      updatePlayerListView();
    }
  }
  
  private void updatePlayerListView() {
    for (PlayerDisplayGUI disp: playerDisp) {
      if (disp.player == null) {
        disp.label.setText(Constants.AI_PLAYER_NAME);
        disp.kickButton.setEnabled(false);
      }
      else {
        disp.label.setText(disp.player.getName());
        disp.kickButton.setEnabled(true);
      }
    }
  }
  
  /**
   * Adds a new player to the lounge.
   * 
   * @param player The new player.
   * 
   * @return true if there was an empty slot for the player to be added.
   */
  private boolean addPlayer(Player player) {
    boolean foundEmptySlot = false;
    
    for (PlayerDisplayGUI disp: playerDisp) {
      if (disp.player == null) {
        disp.player = player;
        foundEmptySlot = true;
        break;
      }
    }
    
    return foundEmptySlot;
  }
  
  /**
   * Kicks an outside player. This method should only be called on outside
   * players since this method will increase the value of the socketSemaphore.
   * 
   * @param idx The index of the player.
   */
  private void kickPlayer(int idx) {
    PlayerDisplayGUI disp = playerDisp.get(idx);
    Player player = disp.player;

    player.kick();
    
    disp.player = null;
    socketSemaphore.release();
    
    updatePlayerListView();
  }
  
  /**
   * Moves the specified player to the top of the list.
   * 
   * @param idx The index of the player.
   */
  private void setToFirst(int idx) {
    Player player = playerDisp.get(idx).player;
    final int maxSize = playerDisp.size(); 
    
    for (int x = 0; x < maxSize; x++) {
      PlayerDisplayGUI disp = playerDisp.get(x);
      Player temp = disp.player;
      disp.player = player;
      player = temp;
    }
    
    updatePlayerListView();
  }
  
  /**
   * Starts the game by closing this frame and opening the game window.
   */
  private void start() {
    portConnTask.cancel(true);
    
    final List<Player> playerList = new ArrayList<Player>();
    for (PlayerDisplayGUI disp: playerDisp) {
      if (disp.player == null) {
        playerList.add(new RandomPlayer());
      }
      else {
        playerList.add(disp.player);
      }
    }
    
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          SudoKillFrame frame = new SudoKillFrame(port, playerList, filledCells);
          frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    
    dispose();
  }
  
  /**
   * Creates the frame.
   * 
   * @param port The port number to listen for incoming players.
   * @param maxPlayers The maximum player allowed to join.
   * @param players Initial list of seed players.
   * @param filledCells The number of cells to fill in the initial board.
   */
  public PlayerLounge(int port, int maxPlayers, List<Player> players, int filledCells) {
    this.port = port;
    this.filledCells = filledCells;
    socketSemaphore = new Semaphore(maxPlayers);
    
    setTitle("Player Lounge");
    
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 290, 130);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    
    JLabel lblPlayer1 = new JLabel("Player1");
    JLabel lblPlayer2 = new JLabel("Player2");
    
    JButton btnStart = new JButton("Start");
    btnStart.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        start();
      }
    });
    
    JButton btnKickPlayer1 = new JButton("Kick");
    btnKickPlayer1.setEnabled(false);
    btnKickPlayer1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        kickPlayer(0);
      }
    });
    
    JButton btnKickPlayer2 = new JButton("Kick");
    btnKickPlayer2.setEnabled(false);
    btnKickPlayer2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        kickPlayer(1);
      }
    });
    
    JButton btnFirst = new JButton("First");
    btnFirst.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setToFirst(1);
      }
    });
    
    GroupLayout gl_contentPane = new GroupLayout(contentPane);
    gl_contentPane.setHorizontalGroup(
      gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_contentPane.createSequentialGroup()
          .addContainerGap()
          .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
            .addComponent(btnStart)
            .addGroup(gl_contentPane.createSequentialGroup()
              .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addComponent(lblPlayer1)
                .addComponent(lblPlayer2))
              .addGap(74)
              .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addComponent(btnKickPlayer2)
                .addComponent(btnKickPlayer1))
              .addPreferredGap(ComponentPlacement.RELATED)
              .addComponent(btnFirst)))
          .addContainerGap(55, Short.MAX_VALUE))
    );
    gl_contentPane.setVerticalGroup(
      gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_contentPane.createSequentialGroup()
          .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
            .addComponent(lblPlayer1)
            .addComponent(btnKickPlayer1))
          .addPreferredGap(ComponentPlacement.RELATED)
          .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
            .addComponent(lblPlayer2)
            .addComponent(btnKickPlayer2)
            .addComponent(btnFirst))
          .addPreferredGap(ComponentPlacement.UNRELATED)
          .addComponent(btnStart)
          .addContainerGap(42, Short.MAX_VALUE))
    );
    
    contentPane.setLayout(gl_contentPane);
    
    playerDisp.add(new PlayerDisplayGUI(null, btnKickPlayer1, lblPlayer1));
    playerDisp.add(new PlayerDisplayGUI(null, btnKickPlayer2, lblPlayer2));
    
    for (Player player: players) {
      if (!player.getName().equals(Constants.AI_PLAYER_NAME)) {
        try {
          socketSemaphore.acquire();
        }
        catch (InterruptedException ex) {
          System.err.println("Error acquiring sempahore at initialization!");
          ex.printStackTrace(System.err);
        }
        
        addPlayer(player);
      }
    }
    
    System.out.println("sem val: " + socketSemaphore.availablePermits());
    updatePlayerListView();
    
    portConnTask = new PortConnection(port);
    portConnTask.execute();
  }
}
