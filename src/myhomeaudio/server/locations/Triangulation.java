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

public class Triangulation {
	public static Node findLocation(
			ArrayList<NodeSignalBoundary> nodeSignatures,
			ArrayList<DeviceObject> devices) {
		
		int[] tally = new int[nodeSignatures.size()];
		Iterator<DeviceObject> iterableDevices = devices.iterator();
		Iterator<NodeSignalBoundary> iterableSignatures;
		DeviceObject device;
		NodeSignalBoundary signature;
		for(int i=0; iterableDevices.hasNext(); i++){
			iterableSignatures = nodeSignatures.iterator();
			signature = iterableSignatures.next();
			while(iterableDevices.hasNext()){
				device = iterableDevices.next();
				if(signature.getNodeRange(device.id).checkRange(device.rssi)){
					tally[i]++;
				}
			}
		}
			
		return null;
	}
}

