package myhomeaudio.server.helper;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.StringTokenizer;

import myhomeaudio.server.client.Client;
import myhomeaudio.server.http.StatusCode;
import myhomeaudio.server.manager.ClientManager;
import myhomeaudio.server.manager.UserManager;
import myhomeaudio.server.node.NodeCommands;
import myhomeaudio.server.user.User;

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

public class ClientHelper extends Helper implements HelperInterface, NodeCommands, StatusCode {

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

			ClientManager cm = ClientManager.getInstance();
			UserManager um = UserManager.getInstance();

			String method = tokenizedUri.nextToken(); // NoSuchElementException

			Gson gson = new Gson();
			Hashtable hasht = gson.fromJson(data, Hashtable.class);

			if (hasht == null) {
				// hasht empty, request failed
				body = "{\"status\":"+STATUS_FAILED+"}";
			} else if (method.equals("rssi")) {
				/*
				 * System.out.println("Getting rssi values from client"); Gson
				 * gson = new Gson();
				 * 
				 * JsonParser parser = new JsonParser(); JsonArray deviceArray =
				 * parser.parse(data).getAsJsonArray();
				 * 
				 * NodeManager nm = NodeManager.getInstance();
				 * 
				 * String lowestDeviceName = ""; int lowestDeviceRSSI =
				 * Integer.MIN_VALUE; for (JsonElement item : deviceArray) {
				 * DeviceObject device = gson.fromJson(item,
				 * DeviceObject.class); if (device != null &&
				 * nm.isValidNode(device.name) && device.rssi >
				 * lowestDeviceRSSI) { lowestDeviceName = device.name;
				 * lowestDeviceRSSI = device.rssi; }
				 * 
				 * }
				 * 
				 * ClientManager cm = ClientManager.getInstance();
				 * 
				 * Client client = cm.getClient(); if
				 * (!client.getClosestNodeName().equals(lowestDeviceName)) { //
				 * Move song playing to new node String nodeName =
				 * client.getClosestNodeName(); Node closeNode =
				 * nm.getNodeByName(nodeName); String ipaddr =
				 * closeNode.getIpAddress(); nm.sendNodeCommand(NODE_PLAY,
				 * ipaddr, client.getCurrentSong()); }
				 * client.setClosestNodeName(lowestDeviceName);
				 * 
				 * this.statusCode = HttpStatus.SC_OK;
				 */

			} else if (method.equals("login")) {
				System.out.println("Getting start from client " + data);

				if (hasht.containsKey("username") && hasht.containsKey("password")
						&& hasht.containsKey("ipaddress") && hasht.containsKey("macaddress")
						&& hasht.containsKey("bluetoothname")) {
					User lUser = new User((String) hasht.get("username"),
							(String) hasht.get("password"));

					if (um.loginUser(lUser) == STATUS_FAILED) {
						body = "{\"status\":"+STATUS_FAILED+"}";
					} else {
						Client client = new Client(lUser, (String) hasht.get("macaddress"),
								(String) hasht.get("ipaddress"),
								(String) hasht.get("bluetoothname"));
						
						cm.addClient(client);
					}
				} else {
					// username and/or password not available, fail
					body = "{\"status\":"+STATUS_FAILED+"}";
				}
				this.httpStatus = HttpStatus.SC_OK;
			} else if (method.equals("logout")) {

			} else {
				this.httpStatus = HttpStatus.SC_BAD_REQUEST;
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
		response.setStatusCode(this.httpStatus);

	}

}
