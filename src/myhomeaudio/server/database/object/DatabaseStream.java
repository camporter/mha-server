package myhomeaudio.server.database.object;

import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import myhomeaudio.server.media.descriptor.MediaDescriptor;
import myhomeaudio.server.source.Source;
import myhomeaudio.server.stream.Stream;


public class DatabaseStream extends DatabaseObject<Stream> implements JSONAware {
	
	private ArrayList<DatabaseNode> assignedNodeList;
	private MediaDescriptor currentMedia;
	private long currentMediaTime = 0;
	private int currentState = -1;
	private Source source;
	
	public DatabaseStream(int id, Stream stream, Source source) {
		super(id, new Stream(stream));
		this.assignedNodeList = new ArrayList<DatabaseNode>();
		this.source = source;
	}
	
	public DatabaseStream(int id, String name, Source source) {
		super(id, new Stream(name));
		this.source = source;
	}
	
	public DatabaseStream(DatabaseStream dbStream) {
		super(dbStream.getId(), new Stream(dbStream.name()));
		this.assignedNodeList = dbStream.getAssignedNodes();
		this.currentMedia = dbStream.getCurrentMedia();
		this.currentMediaTime = dbStream.getCurrentMediaTime();
		this.currentState = dbStream.getCurrentState();
		this.source = dbStream.getSource();
	}
	
	public String name() {
		return object.name();
	}
	
	public ArrayList<DatabaseNode> getAssignedNodes() {
		return new ArrayList<DatabaseNode>(assignedNodeList);
	}
	
	/**
	 * Sets the nodes assigned to this stream.
	 * 
	 * @param nodes The list of nodes in the database to assign.
	 * @return
	 */
	public boolean setAssignedNodes(ArrayList<DatabaseNode> nodes) {
		if (nodes != null) {
			this.assignedNodeList.clear();
			this.assignedNodeList.addAll(nodes);
			return true;
		}
		return false;
	}
	
	/**
	 * Changes the Source for the stream.
	 * 
	 * @param newSource
	 *            The new source for the stream to use.
	 * @return Returns true if the new source is changed successfully.
	 */
	public boolean setSource(Source newSource) {
		if (newSource != null) {
			this.source = newSource;
			return true;
		}
		return false;
	}
	
	public int getCurrentState() {
		return currentState;
	}
	
	public void setCurrentState(int newState) {
		this.currentState = newState;
	}

	public MediaDescriptor getCurrentMedia() {
		// TODO Auto-generated method stub
		return new MediaDescriptor(currentMedia);
	}
	
	public long getCurrentMediaTime() {
		return currentMediaTime;
	}

	public Source getSource() {
		return source;
	}
	
	/**
	 * Updates the stream, such as the time and other details.
	 */
	public void update() {
		// TODO: write update code
	}
	
	@Override
	public String toJSONString() {
		JSONObject result = new JSONObject();
		result.put("id", id);
		result.put("name", object.name());
		result.put("currentMediaTime", currentMediaTime);
		
		return result.toString();
	}
}
