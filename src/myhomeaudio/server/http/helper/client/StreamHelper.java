package myhomeaudio.server.http.helper.client;

import java.util.ArrayList;
import java.util.Iterator;

import myhomeaudio.server.database.object.DatabaseNode;
import myhomeaudio.server.database.object.DatabaseStream;
import myhomeaudio.server.http.HTTPMimeType;
import myhomeaudio.server.http.StatusCode;
import myhomeaudio.server.http.helper.Helper;
import myhomeaudio.server.http.helper.HelperInterface;
import myhomeaudio.server.manager.ClientManager;
import myhomeaudio.server.manager.StreamManager;
import myhomeaudio.server.stream.Stream;

import org.apache.http.HttpStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class StreamHelper extends Helper implements HelperInterface, StatusCode {

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

		// All of the Stream methods must have a session key
		// Make sure the session key exists and is valid
		if (jsonRequest.containsKey("session")
				&& cm.isValidClient((String) jsonRequest.get("session"))) {

			if (method.equals("list")) {

				// List the streams on the server
				body.put("streams", sm.getListJSON());
				body.put("status", STATUS_OK);
				this.httpStatus = HttpStatus.SC_OK;

			} else if (method.equals("add")
					&& jsonRequest.containsKey("stream")) {

				// Add a new stream to the server
				JSONObject streamObj = (JSONObject) jsonRequest.get("stream");
				Stream newStream = new Stream((String) streamObj.get("name"));

				body.put("status", sm.addStream(newStream));
				this.httpStatus = HttpStatus.SC_OK;

			} else if (method.equals("remove")) {

				// Remove a stream from the server
				Integer streamId = (Integer) jsonRequest.get("stream");

				if (streamId != null) {
					body.put("status", sm.removeStream(streamId));
				}
				this.httpStatus = HttpStatus.SC_OK;

			} else if (method.equals("assign")) {

				// Assign nodes to a stream
				Integer streamId = (Integer) jsonRequest.get("stream");
				JSONArray nodeListArray = (JSONArray) jsonRequest
						.get("assignedNodes");

				if (streamId != null) {
					// Make sure stream id was set
					ArrayList<Integer> nodeList = new ArrayList<Integer>();
					if (nodeList != null) {
						// make sure the node list was set
						nodeList.addAll(nodeListArray);

						body.put("status", sm.setNodes(streamId, nodeList));
					}
				}
				this.httpStatus = HttpStatus.SC_OK;

			} else if (method.equals("action")) {

				// Perform a media action on the stream (pause, resume,
				// previous, next)
				Integer streamId = (Integer) jsonRequest.get("stream");
				Integer action = (Integer) jsonRequest.get("action");

				if (streamId != null && action != null) {
					// Both the stream id an action exist
					body.put("status", sm.doAction(streamId, action));
				}
				this.httpStatus = HttpStatus.SC_OK;

			} else if (method.equals("play")) {

				// Play a new media on a specific stream
				
			} else if (method.equals("media")) {
				
				// Get the media for a specific stream
				
			} else {

				// Method not recognized
				body.put("status", STATUS_BAD_METHOD);

			}
		} else {

			// Bad session
			body.put("status", STATUS_BAD_SESSION);

		}
		return body.toString();
	}

}
