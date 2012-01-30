package myhomeaudio.server;

import myhomeaudio.server.discovery.DiscoveryService;
import myhomeaudio.server.handler.ClientHandler;
import myhomeaudio.server.handler.NodeHandler;
import myhomeaudio.server.manager.ClientManager;
import myhomeaudio.server.manager.NodeManager;
import myhomeaudio.server.manager.UserManager;
import myhomeaudio.server.songs.SongFiles;

public class Server {

	// port that nodes will open a socket on
	protected static int NODE_PORT = 9090;
	// port that clients (phones) will open a socket on
	protected static int CLIENT_PORT = 8080;

	static NodeHandler nodeHandler;
	static ClientHandler clientHandler;
	static DiscoveryService discoveryService;

	/**
	 * @param args
	 *            No parameters
	 */
	public static void main(String[] args) {

		// Create an instance of the NodeManager object, which keeps track of
		// nodes
		NodeManager nm = NodeManager.getInstance();
		// Create an instance of the ClientManager object, which keeps track of
		// clients
		ClientManager cm = ClientManager.getInstance();
		
		UserManager um = UserManager.getInstance();

		SongFiles songs = SongFiles.getInstance();
		songs.populateSongList();

		// Handles node requests
		System.out.println("*** Starting Node Handler...");
		startNodeHandler();

		// Handles client requests (android or iphone)
		System.out.println("*** Starting Client Handler...");
		startClientHandler();
		
		startDiscoveryService();

		while (true) {
			try {
				// Checks that threads are still alive every so often
				Thread.sleep(10000);
				if (!nodeHandler.isAlive()) {
					System.out.println("Node Handler Thread Dead !");
					System.out.println("Attempting to restart NodeHandler..");
					startNodeHandler();
				}
				if (!clientHandler.isAlive()) {
					System.out.println("Client Handler Thread Dead !");
					System.out.println("Unable to listen for new clients");
					startClientHandler();
				}
				if (!discoveryService.isAlive()) {
					System.out.println("Discovery Service Thread Dead !");
					System.out.println("Attempting to restart DiscoveryService...");
					startDiscoveryService();
				}
			} catch (InterruptedException e) {
				System.out.println("Exception: Server Exiting");
				System.exit(0);
			}
		}
	}

	/**
	 * Starts up a new NodeHandler thread
	 */
	public static void startNodeHandler() {
		nodeHandler = new NodeHandler(NODE_PORT);
		nodeHandler.setName("NodeHandler");
		nodeHandler.start();
	}

	/**
	 * Starts up a new ClientHandler thread
	 */
	public static void startClientHandler() {
		clientHandler = new ClientHandler(CLIENT_PORT);
		clientHandler.setName("ClientHandler");
		clientHandler.start();
	}
	
	public static void startDiscoveryService() {
		discoveryService = new DiscoveryService();
		discoveryService.setName("DiscoveryService");
		discoveryService.start();
	}
}
