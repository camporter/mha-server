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
	
	public static synchronized NodeManager getInstance() {
		if (instance == null) {
			instance = new NodeManager();
		}
		return instance;
	}
	
	public synchronized void addNode(Node node) {
		nodeList.add(node);
	}
	
	public synchronized void sendNodeCommand(int command, String data) {
		NodeWorker worker = new NodeWorker();
		worker.start();
		
	}
	
}
