package myhomeaudio.server.helper;

import myhomeaudio.server.interfaces.StreamInterface;

/* Filename: NodeHelper.java
 * 
 * Helps Worker.java handle client requests concerning nodes
 * 
 * Requests
 * -server/node/Stream
 *  	>GET, return stream interface of what is playing in the room
 * 
 * 
 */

public class NodeHelper {
	private int nodeID;
	//TODO what needs to be returned?
	//private StringInterface nodeStream;
	
	/* Constructor
	 * 
	 */
	public NodeHelper(int nodeID, String stringMethod){
		this.setNodeID(nodeID);
		if(stringMethod == "stream"){
			stream();
		}
		
	}
	
	public StreamInterface stream(){
		return null;
	}

	public void setNodeID(int nodeID) {
		this.nodeID = nodeID;
	}

	public int getNodeID() {
		return nodeID;
	}

}
