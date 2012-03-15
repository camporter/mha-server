package myhomeaudio.server.stream;

import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import myhomeaudio.server.media.descriptor.MediaDescriptor;
import myhomeaudio.server.node.Node;
import myhomeaudio.server.source.Source;

/**
 * Abstract basis for
 * 
 * @author Cameron
 * 
 */
public class Stream implements JSONAware {

	protected int id;
	protected String name;
	protected Source source;
	protected MediaDescriptor currentMedia;
	protected ArrayList<Node> nodeList;
	protected Date currentMediaTime;

	// mediaState keep the state of the music into account.
	protected int mediaState;

	public Stream(String name) {
		this.name = name;
		this.id = -1;
	}

	public Stream(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public Stream(Stream s) {
		this.id = s.id();
		this.name = s.name();
	}

	public Stream(int id, Stream s) {
		this(s);
		this.id = id;
	}

	public int id() {
		return id;
	}

	public String name() {
		return name;
	}

	/**
	 * Adds a Node to be connected to this stream.
	 * 
	 * @param newNode
	 *            The new Node to be added.
	 * @return Returns true if the node is added successfully.
	 */
	public final boolean addNode(Node newNode) {

		if (newNode != null && nodeList.add(newNode))
			return true;

		return false;
	}

	/**
	 * Changes the Source for the Stream.
	 * 
	 * @param newSource
	 *            The new source for the stream to use.
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

	@Override
	public String toJSONString() {
		JSONObject result = new JSONObject();
		result.put("id", id);
		result.put("name", name);
		return result.toString();
	}

}