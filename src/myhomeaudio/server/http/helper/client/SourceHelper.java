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
				body.put("status", STATUS_OK);
				this.httpStatus = HttpStatus.SC_OK;
			} else if (method.equals("media")) {
				// Get all the media for a specific source
				Integer sourceId = ((Long) jsonRequest.get("source")).intValue();
				
				if (sourceId != null) {
					// The source id is a proper integer
					JSONArray mediaArray = sm.getSourceMediaJSON(sourceId);
					if (mediaArray != null) {
						// The media for the source was found
						body.put("media", mediaArray);
						body.put("status", STATUS_OK);
						this.httpStatus = HttpStatus.SC_OK;
					}
				}
			}
		}

		return body.toString();
	}
}
