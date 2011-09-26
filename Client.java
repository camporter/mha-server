import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

//Client side, emulates nodes
//TODO create multiple clients automatically with threads


public class Client {
	protected static int port = 9090;
	protected static String host = "localhost";
	static ClientConnect conn;
	static String msg;

	public static void main(String[] args) {
		//TODO Remove hardcoded client
		//Hardcoded client connect, send/receive, and disconnect
		
		conn = new ClientConnect(port, host);
		conn.start();
		
		msg = "8787\nINIT\n0\n";
		conn.send(msg);
		//msg = conn.receive();
		//System.out.println(msg);
		conn.closeConnection();

	}

}

//Class for the client to connect to the server

class ClientConnect extends Thread {
	int port;
	String host;
	String msg;
	
	Socket socket;
	
	InputStream inStream;
	DataInputStream inDataStream;
	OutputStream outStream;
	DataOutputStream outDataStream;
	
	ClientConnect(int port, String host){
		this.port = port;
		this.host = host;
		clientConnect();
	}
	public void run(){
		
	}
	public void clientConnect() {
		try {
			//TODO Remove println
			System.out.println("Creating Connection");
			socket = new Socket(host, port);
		    
			//outStream = socket.getOutputStream ();
		    //outDataStream = new DataOutputStream ( outStream );
		    //inStream = socket.getInputStream ();
		    //inDataStream = new DataInputStream ( inStream );
		    System.out.println("Client Socket Created");
		} catch (UnknownHostException e) {
			System.out.println("Error Connecting");
			System.out.println("Client clientConnect UnknownHostException\n");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error Connecting");
			System.out.println("Client clientServer IOException\n");
			e.printStackTrace();
		}
		
		
	}

	public void send(String msg){
		try {
			DataOutputStream os = new DataOutputStream(socket.getOutputStream());
			os.writeBytes(msg);
			System.out.println("Message sent\n");
		} catch (IOException e) {
			System.out.println("Client clientConnect send\n");
			e.printStackTrace();
		}
		
	}
	
	public String receive(){
		try {
			InputStream is = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			System.out.println("Waiting to receive\n");
			msg = br.readLine();
			System.out.println("Message received\n");
		} catch (IOException e) {
			System.out.println("Client clientConnect receive\n");
			e.printStackTrace();
		}
		return msg;
	}
	
	public void closeConnection() {
		try {
			socket.close();
			System.out.println("Connection Closed");
		} catch (IOException e) {
			System.out.println("Error Closing Connection");
			e.printStackTrace();
		}
	}
	
}