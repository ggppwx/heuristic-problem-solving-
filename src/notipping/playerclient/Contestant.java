
import java.io.*;

class Contestant extends NoTippingPlayer {
	private static BufferedReader br;

	Contestant(int port) {
		super(port);
	}

	protected String process(String command) {
	        System.out.println(command);
		System.out.println("Enter move (position weight): ");
		try {
			return br.readLine();
		} catch (Exception ev) {
			System.out.println(ev.getMessage());
		}
		return "";			
	}

	public static void main(String[] args) throws Exception {
		br = new BufferedReader(new InputStreamReader(System.in));
		new Contestant(Integer.parseInt(args[0]));
	}
}
