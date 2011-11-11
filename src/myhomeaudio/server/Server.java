package myhomeaudio.server;

//import myhomeaudio.server.database.Database;
import myhomeaudio.server.handler.ClientHandler;
import myhomeaudio.server.handler.NodeHandler;

public class Server {
	protected static int NODE_PORT = 9090; // port that nodes will open a socket
											// on.
	protected static int CLIENT_PORT = 8080; // port that clients (phones) will
												// open a socket on.

	/**
	 * @param args
	 *            No parameters
	 */
	public static void main(String[] args) {

		// Database db = Database.getInstance();
		NodeManager nm = NodeManager.getInstance(); // Used to create inital
													// nodemanager instance
		Songs songs = Songs.getInstance();
		songs.populateSongList();

		// Handles node requests
		NodeHandler nodeHandler = new NodeHandler(NODE_PORT);
		System.out.println("Starting Node Handler");
		nodeHandler.start();

		// Handles client requests ie android and iphone
		ClientHandler clientHandler = new ClientHandler(CLIENT_PORT);
		System.out.println("Starting Client Handler");
		clientHandler.start();

	}
}