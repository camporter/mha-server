package myhomeaudio.server.stream;

import java.util.ArrayList;
import java.util.Date;

import myhomeaudio.server.media.descriptor.MediaDescriptor;
import myhomeaudio.server.node.Node;
import myhomeaudio.server.source.Source;

/**
 * Abstract basis for 
 * @author Cameron
 *
 */
public abstract class StreamBase {

	protected Source source;
	protected MediaDescriptor currentMedia;
	protected ArrayList<Node> nodeList;
	protected Date currentMediaTime;
	
	// mediaState keep the state of the music into account.
	protected int mediaState;
	
	protected StreamBase() {

	}

	/**
	 * Adds a Node to be tuned to this stream.
	 * 
	 * @param newNode
	 *            The new Node to be added.
	 * @return Returns true if the node is added successfully.
	 */
	public final boolean addNode(Node newNode) {
		
		if (newNode != null && nodeList.add(newNode)) return true;
		
		return false;
	}
	
	/**
	 * Changes the Source for the Stream.
	 * @param newSource The new source for the stream to use.
	 * @return Returns true if the new source is changed successfully.
	 */
	public boolean setSource(Source newSource) {
		if (newSource != null) {
			source = newSource;
			return true;
		}
		return false;
	}
	
	/**
	 * Updates the stream, such as the time and other details.
	 */
	public void update() {
	}

}
