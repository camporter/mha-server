package myhomeaudio.server;

import java.net.InetAddress;
import myhomeaudio.server.discovery.DiscoveryDescription;
import myhomeaudio.server.discovery.DiscoveryResponder;
import myhomeaudio.server.discovery.DiscoveryUtil;
import myhomeaudio.server.handler.ClientHandler;
import myhomeaudio.server.handler.NodeHandler;
import myhomeaudio.server.manager.ClientManager;
import myhomeaudio.server.manager.NodeManager;
import myhomeaudio.server.manager.StreamManager;
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

	protected static NodeHandler nodeHandler;
	protected static ClientHandler clientHandler;
	protected static DiscoveryResponder discoveryResponder;
	//protected static StreamThread streamThread;

	/**
	 * @param args
	 *            No parameters
	 */
	public static void main(String[] args) {

		// Create an instance of the NodeManager object, which keeps track of
		// nodes
		NodeManager nm = NodeManager.getInstance();
		ClientManager cm = ClientManager.getInstance();
		UserManager um = UserManager.getInstance();
		StreamManager sm = StreamManager.getInstance();

		// SongFiles songs = SongFiles.getInstance();
		// songs.populateSongList();

		// Handles node requests
		System.out.println("*** Starting Node Handler...");
		startNodeHandler();

		// Handles client requests (android or iphone)
		System.out.println("*** Starting Client Handler...");
		startClientHandler();

		// startDiscoveryService();
		startDiscoveryService();

		//startStreamThread();

		while (true) {
			try {
				// Checks that threads are still alive every so often
				Thread.sleep(10000);
				if (!nodeHandler.isAlive()) {
					System.err.println("Node Handler Thread Dead !");
					System.out.println("Attempting to restart NodeHandler..");
					startNodeHandler();
				}
				if (!clientHandler.isAlive()) {
					System.err.println("Client Handler Thread Dead !");
					System.out.println("Unable to listen for new clients");
					startClientHandler();
				}
				if (!discoveryResponder.isAlive()) {
					System.err.println("Discovery Thread Dead !");
					System.out.println("Attempting to restart discovery...");
					discoveryResponder.startResponder();
				}
			} catch (InterruptedException e) {
				System.err.println("Exception: Server Exiting");
				System.exit(0);
			}
			System.out.println("Stream:"+ sm.getStreamListJSON().toString());
			System.out.println("Node:"+nm.getJSONArray().toJSONString());
			cm.getClientList();
			//System.out.println("Client:"+cm.getClientList().toJSONString());
			System.out.println("User:"+um.getUsersList().toJSONString());
		}
	}

	private static void startDiscoveryService() {
		System.out.println("** Starting discovery services...");
		DiscoveryDescription descriptor;
		InetAddress serverAddress = DiscoveryUtil.getServerAddress();
		descriptor = new DiscoveryDescription(
				"myhomeaudio", serverAddress.getHostAddress(), CLIENT_PORT, NODE_PORT);
		discoveryResponder = new DiscoveryResponder("myhomeaudio", descriptor);
		discoveryResponder.addShutdownHandler();
		discoveryResponder.startResponder();
	}

	/*public static void startStreamThread() {
		streamThread = new StreamThread();
		streamThread.setName("StreamThread");
		streamThread.start();

	}*/

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

	/*
	 * public static void startDiscoveryService() { discoveryService = new
	 * MDNSDiscoveryService(); discoveryService.setName("MDNSDiscoveryService");
	 * discoveryService.start(); }
	 */
}
