package myhomeaudio.server.helper;

import myhomeaudio.server.http.StatusCode;
import myhomeaudio.server.node.NodeCommands;

/* Filename: NodeHelper.java
 * 
 * Helps ClientWorker.java handle client requests concerning nodes
 * 
 * Requests
 * -server/node/Stream
 *  	>GET, return stream interface of what is playing in the room
 * 
 * 
 */

public class NodeHelper extends Helper implements HelperInterface, NodeCommands, StatusCode {

	public String getOutput() {
		String body = "{\"status\":"+STATUS_FAILED+"}";

		return body;
	}
}
