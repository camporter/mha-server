package myhomeaudio.server.locations.triangulate;

import java.util.ArrayList;

import myhomeaudio.server.locations.DeviceObject;
import myhomeaudio.server.node.Node;

public class RangeBoundary {
	private Node node;
	private ArrayList<DeviceObject> interference;
	
	public Node getNode(){
		return node;
	}
}
