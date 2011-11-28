package nyu.hps;

import java.net.Socket;
import java.io.*;

/**
 * An external player that is connected to a socket. The external player should follow these
 * protocols:
 * 
 * <ol>
 *  <li>
 *   The first thing it should do is perform a handshake by sending the <code>PLAYER_CODE</code>
 *   string.
 *  </li>
 *  
 *  <li>
 *   For every turn, the move history will be sent delimited with <code>MOVE_START</code> and
 *   <code>MOVE_END</code>. In between these two delimiters are a series of moves that has
 *   been done so far, in chronological order and separated by newlines. The format of the
 *   move conforms to the format in {@link Move}.
 *  </li>
 *  
 *  <li>
 *   For every turn, the player should respond with a string corresponding to its move while
 *   following the format used in {@link Move} after receiving the move history. 
 *  </li>
 * </ol>
 *
 * @see Move
 */
public class SocketPlayer extends AbstractPlayer {
  public static final String PLAYER_CODE = "SUDOKILL_PLAYER";
  public static final String MOVE_START = "MOVE START";
  public static final String MOVE_END = "MOVE END";
  
  private final Socket socket;
  private final PrintWriter out;
  private final BufferedReader in;
  
  /**
   * Creates a new player.
   * 
   * @param socket The socket associated with the player.
   * 
   * @throws IOException, IllegalArgumentException
   */
  public SocketPlayer(Socket socket) throws IOException, IllegalArgumentException {
    this.socket = socket;

    out = new PrintWriter(socket.getOutputStream(), true);
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    
    String handshakeStr = in.readLine();
    if (!handshakeStr.equals(PLAYER_CODE)) {
      throw new IllegalArgumentException("Failed to perform handshake.");
    }
    else {
      name = in.readLine();
    }
  }
    
  public Socket getSocket() {
    return socket;
  }
  
  /**
   * Serializes the move history into a String representation for sending across
   * the network.
   * 
   * @param moveHistory The move history.
   * 
   * @return the string representation.
   */
  private static String serialize(ReadOnlyHistory moveHistory) {
    final StringBuilder str = new StringBuilder();
    
    str.append(MOVE_START);
    str.append("\n");
    
    for (Move move: moveHistory) {
      str.append(move);
      str.append("\n");
    }
    
    str.append(MOVE_END);
    
    return str.toString();
  }
  
  /**
   * @inheritDoc
   */
  public Move play(ReadOnlyHistory moveHistory) {
    String moveStr = serialize(moveHistory);
    out.println(moveStr);
    String reply = "";
    
    try {
      reply = in.readLine();
    }
    catch (IOException ex) {
      System.err.println("Player " + name + ": " + ex.getMessage());
      ex.printStackTrace(System.err);
    }
    
    return new Move(reply);
  }
  
  /**
   * @inheritDoc
   */
  public void kick() {
    try {
      socket.close();
    } catch (IOException ex) {
      System.err.println("Error closing socket by Player :" + name);
    }
  }
}
