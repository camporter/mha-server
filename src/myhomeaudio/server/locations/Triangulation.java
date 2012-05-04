package myhomeaudio.server.locations;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import myhomeaudio.server.client.Client;
import myhomeaudio.server.database.object.DatabaseClient;
import myhomeaudio.server.database.object.DatabaseNode;
import myhomeaudio.server.locations.layout.DeviceObject;
import myhomeaudio.server.locations.layout.NodeSignature;
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
	
	
	protected class Score {
		public NodeSignature signature;
		public int matches;
		
		public Score(NodeSignature signature, int matches) {
			this.signature = signature;
			this.matches = matches;
		}
	}
	
	/**
	 * Finds the node whose device signals match 
	 * a client's initial configuration signature
	 * 
	 * @param nodeSignatures Client signatures to check
	 * @param devices Array of device reading values
	 * @return Node whose readings match a signature, or null
	 */
	public Node findLocation(
			ArrayList<NodeSignature> nodeSignatures,
			ArrayList<DeviceObject> devices) {
		
		NodeManager nm = NodeManager.getInstance();
		
		ArrayList<Score> signatureScores = new ArrayList<Score>();
		
		Iterator<NodeSignature> iterableSignatures = nodeSignatures.iterator();
		DeviceObject device;
		
		
		while(iterableSignatures.hasNext()){
			NodeSignature signature = iterableSignatures.next();
			Iterator<DeviceObject> iterableDevices = devices.iterator();
			
			Score score = new Score(signature, 0);
			
			while(iterableDevices.hasNext()){
				device = iterableDevices.next();
				if(signature.getNodeRange(device.id).checkRange(device.rssi)){
					score.matches++;
				}
			}
			signatureScores.add(score);
		}
		
		Score chosenScore = null;
		
		for (Iterator<Score> i = signatureScores.iterator(); i.hasNext();) {
			Score nextScore = i.next();
			
			if (chosenScore == null) {
				chosenScore = nextScore;
			}
			else if (chosenScore.matches < nextScore.matches) {
				chosenScore = nextScore;
			}
			
		}
		
		return nm.getNodeById(chosenScore.signature.getNodeId()).getNode();
	}
}

