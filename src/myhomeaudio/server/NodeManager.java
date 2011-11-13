package myhomeaudio.server;

import java.util.ArrayList;

import myhomeaudio.server.http.NodeWorker;
import myhomeaudio.server.node.Node;
import myhomeaudio.server.node.NodeCommands;

public class NodeManager implements NodeCommands {
	private static NodeManager instance = null;
	private ArrayList<Node> nodeList = new ArrayList<Node>();
	
	protected NodeManager() {
		
	}
	
	/**
	 * There is only one instance of NodeManager within program.
	 *  
	 * @return instance
	 * 		Creates a new instance of NodeManager if one does not exist or returns if one exists
	 */
	public static synchronized NodeManager getInstance() {
		if (instance == null) {
			instance = new NodeManager();
		}
		return instance;
	}
	
	/**
	 * Add Node, places nodes within network into an arrayList
	 * @param node
	 * 		Node to be added to the arrayList
	 */
	public synchronized void addNode(Node node) {
		nodeList.add(node);
	}
	
	/**
	 * Sends commands from the server the node
	 * @param command
	 * 		The command the server wants to send to the node
	 * @param data
	 * 		Necessary data the node would need to execute the command
	 */
	public synchronized void sendNodeCommand(int command, String data) {
		NodeWorker worker = new NodeWorker();
		worker.start();
		
	}
	
}
