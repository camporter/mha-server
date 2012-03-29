package myhomeaudio.server.http.helper;

import java.util.ArrayList;
import java.util.Iterator;

import myhomeaudio.server.client.Client;
import myhomeaudio.server.database.object.DatabaseClient;
import myhomeaudio.server.http.HTTPMimeType;
import myhomeaudio.server.http.StatusCode;
import myhomeaudio.server.locations.ClientInitialization;
import myhomeaudio.server.locations.Triangulation;
import myhomeaudio.server.locations.layout.NodeSignalRange;
import myhomeaudio.server.locations.layout.NodeSignalBoundary;
import myhomeaudio.server.manager.ClientManager;
import myhomeaudio.server.manager.NodeManager;
import myhomeaudio.server.manager.UserManager;
import myhomeaudio.server.node.Node;
import myhomeaudio.server.node.NodeCommands;
import myhomeaudio.server.user.User;

import org.apache.http.HttpStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class ClientHelper extends Helper implements HelperInterface, NodeCommands, StatusCode {

	class DeviceObject {

		public String name;
		public int rssi;

		public DeviceObject(String name, int rssi) {
			this.name = name;
			this.rssi = rssi;
		}
	}

	@SuppressWarnings("unchecked")
	public String getOutput(ArrayList<String> uriSegments, String data) {

		// Set the content-type
		this.contentType = HTTPMimeType.MIME_JSON;
		this.httpStatus = HttpStatus.SC_BAD_REQUEST;

		// Create the JSON object to represent the response
		JSONObject body = new JSONObject();
		body.put("status", STATUS_FAILED);

		ClientManager cm = ClientManager.getInstance();
		UserManager um = UserManager.getInstance();

		try {
			// Convert the request into a JSON object
			JSONObject jsonRequest = (JSONObject) JSONValue.parse(data);

			if (uriSegments.get(1).equals("login")) {
				System.out.println("Getting login from client");

				// Make sure we have all the required fields
				if (jsonRequest.containsKey("username") && jsonRequest.containsKey("password")
						&& jsonRequest.containsKey("ipaddress")
						&& jsonRequest.containsKey("macaddress")
						&& jsonRequest.containsKey("bluetoothname")) {
					User lUser = new User((String) jsonRequest.get("username"),
							(String) jsonRequest.get("password"));

					// Check that the login succeeded
					if (um.loginUser(lUser) == STATUS_OK) {
						Client lClient = new Client(lUser, (String) jsonRequest.get("macaddress"),
								(String) jsonRequest.get("ipaddress"),
								(String) jsonRequest.get("bluetoothname"));

						String sessionId = cm.addClient(lClient);
						
						body.put("status", STATUS_OK);
						body.put("session", sessionId);

						this.httpStatus = HttpStatus.SC_OK;
					}
				}

			} else if (uriSegments.get(1).equals("logout")) {
				System.out.println("Getting logout from client");

				if (jsonRequest.containsKey("session")) {
					if (cm.removeClient((String) jsonRequest.get("session"))) {
						this.httpStatus = HttpStatus.SC_OK;
					}
				}

			} else if (uriSegments.get(1).equals("locations")) {
				if (jsonRequest.containsKey("session") && jsonRequest.containsKey("locations")) {
					
					JSONArray locations = (JSONArray) jsonRequest.get("locations");
					
					cm.updateLocation((String) jsonRequest.get("session"), locations);
					
					//use Triangulation to find closest node
					//set closest node
					if(dClient.updateLocation((String)jsonRequest.get("locations"))) {
						body.put("status", STATUS_OK);
						this.httpStatus = HttpStatus.SC_OK;
						//TODO call triangulation 
					}	
				}
			} else if(uriSegments.get(1).equals("initialConfig")){
				//TODO store config information in db
				if (jsonRequest.containsKey("session") && jsonRequest.containsKey("entries")) {
					if(cm.isValidClient((String)jsonRequest.get("session"))){
					
						Triangulation tn = Triangulation.getInstance();
						NodeManager nm = NodeManager.getInstance();
						
						JSONArray actualNodes = (JSONArray)(jsonRequest.get("actualNodes"));
						
						ArrayList<NodeSignalBoundary> nodeSignatures = new ArrayList<NodeSignalBoundary>();
						Iterator<JSONObject> i = actualNodes.iterator();
						
						// For each node, get the signal ranges of all other nodes
						while (i.hasNext()) {
							Node node = nm.getNodeById((String)(i.next().get("id")));
							
							JSONArray foundNodes = (JSONArray)(i.next().get("foundNodes"));
							
							NodeSignalBoundary nodeSignalBoundary = new NodeSignalBoundary(node.getId());
							
							Iterator<JSONObject> j = foundNodes.iterator();
							
							// For each found node, get their id and max/min signal values
							while(j.hasNext()){
								JSONObject jObject = j.next();
								nodeSignalBoundary.addNodeRange(new NodeSignalRange((String)jObject.get("id"),
											((Long)jObject.get("min")).intValue(), ((Long)jObject.get("max")).intValue()));
							}
							
							nodeSignatures.add(nodeSignalBoundary);	
						}
						
						cm.changeClientInitialization((String)jsonRequest.get("session"), nodeSignatures);
						//tn.addNodeConfiguration(new ClientInitialization(dbc.getMacAddress(), nodeSignatures));
						body.put("status", STATUS_OK);
						this.httpStatus = HttpStatus.SC_OK;
					}
				}
			}
		} catch (Exception e) {
			// Do nothing for now
		}

		return body.toString();
	}

}
