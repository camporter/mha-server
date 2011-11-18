package myhomeaudio.server.node;

import java.util.ArrayList;

import myhomeaudio.server.http.NodeWorker;

public class NodeManager implements NodeCommands {
	private static NodeManager instance = null;
	private static int nodeCount = 0;
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
	
	//TODO add removeNode, checkNode to make sure no nodes have suddenly disconnected
	/**
	 * Add Node, places nodes within network into an arrayList
	 * @param node
	 * 		Node to be added to the arrayList
	 */
	public synchronized void addNode(Node node) {
		nodeList.add(node);
		nodeCount++;
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
		// TODO: THIS DEFAULTS TO THE FIRST NODE!
		worker.setRequestData(command, this.nodeList.get(0).getIpAddress(), data);
		worker.start();
		
	}
	
	public synchronized int getNodeCount(){
		return nodeCount;
	}
	
}
