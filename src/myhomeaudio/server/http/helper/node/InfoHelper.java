package myhomeaudio.server.http.helper.node;

import java.util.ArrayList;

import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;

import myhomeaudio.node.Configuration;
import myhomeaudio.server.http.HTTPMimeType;
import myhomeaudio.server.http.StatusCode;
import myhomeaudio.server.http.helper.Helper;
import myhomeaudio.server.http.helper.HelperInterface;
import myhomeaudio.server.node.NodeCommands;

public class InfoHelper extends Helper implements HelperInterface,
		NodeCommands, StatusCode {

	public String getOutput(ArrayList<String> uriSegments, String data) {

		// Set the content-type
		this.contentType = HTTPMimeType.MIME_JSON;
		this.httpStatus = HttpStatus.SC_BAD_REQUEST;

		// Create the JSON object to represent the response
		JSONObject body = new JSONObject();
		body.put("status", STATUS_FAILED);

		// Convert the request to JSON object
		Configuration config = Configuration.getInstance();
		
		// Put in the node's name and address
		body.put("bluetoothName", config.getBluetoothName());
		body.put("bluetoothAddress", config.getBluetoothAddress());
		this.httpStatus = HttpStatus.SC_OK;
		
		return body.toString();
	}
}
