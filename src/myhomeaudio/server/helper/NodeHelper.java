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
	
	/* Constructor
	 * 
	 */
	public NodeHelper(){
	}
	
	private StreamInterface stream(){
		return null;
		
	}

}
