import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/* Type of message sent by nodes to server (temporary)
 * 
 * id# \n request \n userId'
 * Example: 8787\nUSER\n2
 * id# is the unique identifier for the particular node (use IP)
 * 	-Some sort of identification for node
 * request is the type of request the node is making
 * 		- "INIT" Initialization with the server
 * 		- "USER" Request data, user entered room
 * 		- "SWITCH" Request new data, new user entered room
 * 		- "HALT" Stop the streaming of data, user left room
 * 				TODO-Keep queue of users in room, if user of high priority leaves, then low priority takes over
 * 		- "DISCONNECT" Node leaves, thread allocated to it by the server is removed
 * userId is optional, used for USER and SWITCH, indicates the user who entered the room
 * 		Server checks the preferences to find the necessary data to send to node.
 * 		For INIT, HALT, and DISCONNT use userId = 0
 * 
 */

//TODO Remove excess System.out.println statements 
//TODO Stream audio

public class Server {
	protected static int port = 9090;
	
	/**
	 * @param args
	 * No parameters
	 */
	public static void main(String[] args) {
		//TODO Need to broadcast to nodes, have nodes join multicast group?
		//InitializeSetup move to new class?
		
		ServerSocket listenSocket;
		try
		{
			listenSocket = new ServerSocket(port);
			while(true)
			{
				System.out.println("Listening");
				
				Socket clientSocket = listenSocket.accept();
				System.out.println("Connection Found");
				
				NodeRequest request = new NodeRequest(clientSocket);
				
				Thread thread = new Thread(request);
				thread.start();
							
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}
}
	
final class NodeRequest implements Runnable {
	Socket socket;
	public NodeRequest(Socket socket){
		this.socket = socket;
		nodeRequest();
	}
	
	public void nodeRequest() {
		try {
			InputStream is = socket.getInputStream();
			DataOutputStream os = new DataOutputStream(socket.getOutputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			String temp = br.readLine();
			int nodeId = Integer.parseInt(temp);
			System.out.println("node: "+nodeId);
			String request  = br.readLine();
			System.out.println("request: "+request);
			temp = br.readLine();
			int userId = Integer.parseInt(temp);
			System.out.println("userid: " + userId);
			System.out.println("Info " + nodeId + " " + request + " " + userId);

			
			//TODO Remove test
			//Remove, Test that client receives response
			
			String returnMsg = "Info " + Integer.toString(nodeId) + " " + request 
			+ " " + Integer.toString(userId);
			
			os.writeBytes(returnMsg);
			
		} catch (IOException e) {
			System.out.println("NodeRequest run catch\n");
			e.printStackTrace();
		}
	}

	public void run() {
	}
}