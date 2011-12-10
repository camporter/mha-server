package myhomeaudio.server.manager;

import java.util.ArrayList;

import myhomeaudio.server.http.NodeWorker;
import myhomeaudio.server.node.Node;
import myhomeaudio.server.node.NodeCommands;

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
	 * @return
	 * 		True - Item added to list, False - Item already in list
	 */
	public synchronized boolean addNode(Node node) {
		if(nodeList.contains(node)) {
			return false;
		}
		nodeList.add(node);
		nodeCount++;
		return true;
	}
	
	/**
	 * Sends commands from the server the node
	 * @param command
	 * 		The command the server wants to send to the node
	 * @param data
	 * 		Necessary data the node would need to execute the command
	 */
	public synchronized void sendNodeCommand(int command, String ipAddress, String data) {
		NodeWorker worker = new NodeWorker();
		
		worker.setRequestData(command, ipAddress, data);
		worker.start();
		
	}
	
	/**
	 * 
	 * @return nodeCount
	 * 		Number of nodes under NodeManager management
	 */
	public synchronized int getNodeCount(){
		return nodeCount;
	}
	
	public boolean isValidNode(String bluetoothName) {
		for (Node node : this.nodeList)
		{
			if (node.getBluetoothName().equals(bluetoothName))
			{
				return true;
			}
		}
		return false;
		
	}
	
	/**
	 * Get a Node object with the given IP address
	 * @param ipAddress The IP address of the node to return.
	 * @return The Node with the corresponding IP. If no nodes match, return null.
	 */
	public Node getNodeByIpAddress(String ipAddress) {
		for (Node item : nodeList) {
			if (item.getIpAddress().equals(ipAddress))
			{
				return item;
			}
		}
		return null;
	}
}
