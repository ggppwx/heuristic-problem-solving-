package client;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class Client {
	
	private BufferedReader br;
	
	private void ReadWrite(BufferedReader in, PrintWriter out) throws IOException{
		
		while(true){
			String line;
			String[] command = new String[4];
			for(int i=0; i<4;++i){
				line = in.readLine();
				if(line.equals("END")){
					return;
				}
				command[i] = line;
				System.out.println(line);
			}
			process(command);
			// out.println(process(command));
			out.println(br.readLine());
		}
		
	}
	
	public Client(String host, int port) {
		// TODO Auto-generated constructor stub
		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		br = new BufferedReader(new InputStreamReader(System.in));
		
		try{
			socket = new Socket(host,port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(),true);
			ReadWrite(in, out);
		}catch(IOException ev){
			System.err.println(ev.getMessage());
		}

		out.close();
		try{
			in.close();
			socket.close();

		}catch(IOException e){
			System.err.println(e.getMessage());
		}
	}
	
	protected abstract String process(String[] command);
	


}
