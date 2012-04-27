package myhomeaudio.server.http.helper.client;

import java.util.ArrayList;

import org.apache.http.HttpStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import myhomeaudio.server.http.HTTPMimeType;
import myhomeaudio.server.http.StatusCode;
import myhomeaudio.server.http.helper.Helper;
import myhomeaudio.server.http.helper.HelperInterface;
import myhomeaudio.server.manager.ClientManager;
import myhomeaudio.server.manager.StreamManager;

/**
 * Handles any HTTP requests from client pertaining to Sources that the server
 * provides.
 * 
 * @author Cameron
 * 
 */
public class SourceHelper extends Helper implements HelperInterface, StatusCode {

	@Override
	public String getOutput(ArrayList<String> uriSegments, String data) {
		// Set the content-type
		this.contentType = HTTPMimeType.MIME_JSON;
		this.httpStatus = HttpStatus.SC_BAD_REQUEST;

		JSONObject body = new JSONObject();
		body.put("status", STATUS_FAILED);

		StreamManager sm = StreamManager.getInstance();
		ClientManager cm = ClientManager.getInstance();

		JSONObject jsonRequest = (JSONObject) JSONValue.parse(data);

		String method = uriSegments.get(1);

		if (jsonRequest.containsKey("session")
				&& cm.isValidClient((String) jsonRequest.get("session"))) {
			if (method.equals("list")) {
				// List the sources on the server
				JSONArray sourceListArray = sm.getSourceListJSON();
				body.put("sources", sourceListArray);

			} else if (method.equals("media")) {
				// Get all the media for a specific source

			}
		}

		return body.toString();
	}
}
