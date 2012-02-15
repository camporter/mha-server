package myhomeaudio.server.http.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import java.util.StringTokenizer;

import myhomeaudio.server.client.Client;
import myhomeaudio.server.http.HTTPMimeType;
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

			}
		} catch (Exception e) {
			// Do nothing for now
		}

		return body.toString();
	}
	
}