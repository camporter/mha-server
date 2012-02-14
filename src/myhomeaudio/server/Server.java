package myhomeaudio.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import myhomeaudio.server.discovery.DiscoveryDescription;
import myhomeaudio.server.discovery.DiscoveryResponder;
import myhomeaudio.server.discovery.MDNSDiscoveryService;
import myhomeaudio.server.handler.ClientHandler;
import myhomeaudio.server.handler.NodeHandler;
import myhomeaudio.server.manager.ClientManager;
import myhomeaudio.server.manager.NodeManager;
import myhomeaudio.server.manager.UserManager;

/**
 * Entry point for the My Home Audio Server.
 * 
 * We create a number of different threads from this class, which manage 
 * 
 * @author Cameron
 *
 */
public class Server {

	// port that nodes will open a socket on
	protected static int NODE_PORT = 9090;
	// port that clients (phones) will open a socket on
	protected static int CLIENT_PORT = 8080;

	static NodeHandler nodeHandler;
	static ClientHandler clientHandler;
	static MDNSDiscoveryService discoveryService;

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

		//SongFiles songs = SongFiles.getInstance();
		//songs.populateSongList();

		// Handles node requests
		System.out.println("*** Starting Node Handler...");
		startNodeHandler();

		// Handles client requests (android or iphone)
		System.out.println("*** Starting Client Handler...");
		startClientHandler();
		
		//startDiscoveryService();
		startNodeDiscoveryService();

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
				/*if (!discoveryService.isAlive()) {
					System.out.println("Discovery Service Thread Dead !");
					System.out.println("Attempting to restart MDNSDiscoveryService...");
					startDiscoveryService();
				}*/
			} catch (InterruptedException e) {
				System.out.println("Exception: Server Exiting");
				System.exit(0);
			}
		}
	}

	private static void startNodeDiscoveryService() {
		try {
			DiscoveryDescription descriptor = new DiscoveryDescription();
			descriptor.setAddress(InetAddress.getLocalHost());
			descriptor.setPort(NODE_PORT);
			descriptor.setInstanceName("myhomeaudioinstance");
			
			DiscoveryResponder responder = new DiscoveryResponder("myhomeaudio");
			responder.setDescriptor(descriptor);
			responder.addShutdownHandler();
			responder.startResponder();
		} catch (UnknownHostException e) {
			e.printStackTrace();
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
		discoveryService = new MDNSDiscoveryService();
		discoveryService.setName("MDNSDiscoveryService");
		discoveryService.start();
	}
}
