package myhomeaudio.server.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import java.util.StringTokenizer;

import myhomeaudio.server.client.Client;
import myhomeaudio.server.manager.ClientManager;
import myhomeaudio.server.manager.NodeManager;
import myhomeaudio.server.node.Node;
import myhomeaudio.server.node.NodeCommands;
import myhomeaudio.server.songs.SongFiles;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ClientHelper extends Helper implements HelperInterface, NodeCommands {
	
	class DeviceObject {
		public String name;
		public int rssi;
		public DeviceObject(String name, int rssi) {
			this.name = name;
			this.rssi = rssi;
		}
	}
	
	public String getOutput(String uri, String data) {
		String body = "";
		
		StringTokenizer tokenizedUri = new StringTokenizer(uri, "/");
		tokenizedUri.nextToken(); // throw the first part away, throws /song
									// away

		if (tokenizedUri.hasMoreTokens()) {
			
			String method = tokenizedUri.nextToken(); // NoSuchElementException
			 if (method.equals("rssi")) {
				 System.out.println("Getting rssi values from client");
				Gson gson = new Gson();
				
				JsonParser parser = new JsonParser();
				JsonArray deviceArray = parser.parse(data).getAsJsonArray();
				
				NodeManager nm = NodeManager.getInstance();
				
				String lowestDeviceName = "";
				int lowestDeviceRSSI = Integer.MIN_VALUE;
				for (JsonElement item : deviceArray)
				{
					DeviceObject device = gson.fromJson(item, DeviceObject.class);
					if (device != null && nm.isValidNode(device.name) && device.rssi > lowestDeviceRSSI) {
						lowestDeviceName = device.name;
						lowestDeviceRSSI = device.rssi;
					}
						
				}
				
				ClientManager cm = ClientManager.getInstance();
				
				Client client = cm.getClient();
				if (!client.getClosestNodeName().equals(lowestDeviceName)) {
					// Move song playing to new node
					nm.sendNodeCommand(NODE_PLAY, nm.getNodeByName(cm.getClient().getClosestNodeName()).getIpAddress(), client.getCurrentSong());
				}
				client.setClosestNodeName(lowestDeviceName);
				
				this.statusCode = HttpStatus.SC_OK;		
				
			 } else if (method.equals("start")) {
				 System.out.println("Getting start from client "+data);
				 ClientManager cm = ClientManager.getInstance();
				 Client client = new Client(data);
				 cm.addClient(client);
				 this.statusCode = HttpStatus.SC_OK;
				 
			 } else {
				 this.statusCode = HttpStatus.SC_BAD_REQUEST;
			 }

		} else {

		}
		
		return body;
	}
	
	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext context)
			throws HttpException, IOException {
		
		String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
		
        if (!method.equals("GET") && !method.equals("POST")) {
            throw new MethodNotSupportedException(method + " method not supported"); 
        }
        
        String requestData = "";
        if (request instanceof HttpEntityEnclosingRequest) {
            HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
            requestData = EntityUtils.toString(entity);
        }
        
		String uri = request.getRequestLine().getUri();
		StringEntity body = new StringEntity(this.getOutput(uri, requestData));
		response.setEntity(body);
		response.setStatusCode(this.statusCode);

	}
	
}