package myhomeaudio.server.locations;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import myhomeaudio.server.client.Client;
import myhomeaudio.server.database.object.DatabaseClient;
import myhomeaudio.server.database.object.DatabaseNode;
import myhomeaudio.server.locations.layout.DeviceObject;
import myhomeaudio.server.locations.layout.NodeSignalBoundary;
import myhomeaudio.server.manager.NodeManager;
import myhomeaudio.server.node.Node;

/**
 * Triangulates a client's location using the initial configuration signatures 
 * and current device signature readings
 * 
 * @author Ryan
 *
 */
public class Triangulation {
	
	/**
	 * Finds the node whose device signals match 
	 * a client's initial configuration signature
	 * 
	 * @param nodeSignatures Client signatures to check
	 * @param devices Array of device reading values
	 * @return Node whose readings match a signature, or null
	 */
	public static Node findLocation(
			ArrayList<NodeSignalBoundary> nodeSignatures,
			ArrayList<DeviceObject> devices) {
		
		NodeManager nm = NodeManager.getInstance();
		Iterator<DeviceObject> iterableDevices;
		Iterator<NodeSignalBoundary> iterableSignatures = nodeSignatures.iterator();
		DeviceObject device;
		NodeSignalBoundary signature;
		boolean match;
		
		while(iterableSignatures.hasNext()){
			signature = iterableSignatures.next();
			iterableDevices = devices.iterator();
			match = true;
			while(iterableDevices.hasNext()){
				device = iterableDevices.next();
				if(!signature.getNodeRange(device.id).checkRange(device.rssi)){
					match = false;
				}
			}
			if(match){
				return nm.getNodeById(signature.getNodeId()).getNode();
			}
		}
			
		return null;
	}
}

