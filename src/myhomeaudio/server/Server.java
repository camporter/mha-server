package myhomeaudio.server;

//import myhomeaudio.server.database.Database;
import myhomeaudio.server.handler.ClientHandler;
import myhomeaudio.server.handler.NodeHandler;
import myhomeaudio.server.manager.ClientManager;
import myhomeaudio.server.manager.NodeManager;
import myhomeaudio.server.songs.SongFiles;

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
		NodeManager nm = NodeManager.getInstance(); // Used to create initial
													// nodemanager instance
		ClientManager cm = ClientManager.getInstance(); //Create initial clientmanager
		SongFiles songs = SongFiles.getInstance();
		//TODO search database of stored music library directories
		//Add function to add new root directory
		//songDir[0] = "music";
		//songs.populateDirectoryList(songDir);
		songs.populateSongList();
		
		

		// Handles node requests
		NodeHandler nodeHandler = new NodeHandler(NODE_PORT);
		System.out.println("Starting Node Handler");
		nodeHandler.start();
		
		// Handles client requests ie android and iphone
		ClientHandler clientHandler = new ClientHandler(CLIENT_PORT);
		System.out.println("Starting Client Handler");
		clientHandler.start();
		
		while(true){
			try {
				Thread.sleep(20000); //Checks that threads are still alive
				if(!nodeHandler.isAlive()){
					//TODO Save server state, restart
					System.out.println("Node Handler Thread Dead");
					System.out.println("Unable to listen for new nodes");
					//System.exit(0);
				}
				if(!clientHandler.isAlive()){
					//TODO Save server state, restart
					System.out.println("Client Handler Thread Dead - Server Exiting");
					System.out.println("Unable to listen for new clients");
					//System.exit(0);
				}
			} catch (InterruptedException e) {
				System.out.println("Exception: Server Exiting");
				System.exit(0);
			}
		}
	}
}