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
			if (method.equals("play")) {
				// Play a new media on the stream

			} else if (method.equals("resume")) {
				// Resume playing the currently paused media

			} else if (method.equals("pause") && jsonRequest.containsKey("stream")) {
				
				// Pause the currently playing media
				
			} else if (method.equals("next")) {
				
				// Start the next media in the stream
			} else if (method.equals("previous")) {
				// Start the previous media in the stream
			} else if (method.equals("list")) {
				// List the streams on the server
				body.put("streams", sm.getListJSON());
				body.put("status", STATUS_OK);
				this.httpStatus = HttpStatus.SC_OK;
				
			} else if (method.equals("add") && jsonRequest.containsKey("stream")) {
				// Add a new stream to the server
				JSONObject streamObj = (JSONObject) jsonRequest.get("stream");
				Stream newStream = new Stream((String) streamObj.get("name"));
				
				body.put("status", sm.addStream(newStream));
				this.httpStatus = HttpStatus.SC_OK;
				
			} else if (method.equals("remove")) {
				// Remove a stream from the server
				JSONObject streamObj = (JSONObject) jsonRequest.get("stream");
				DatabaseStream removeStream = new DatabaseStream((Integer) streamObj.get("id"), (String) streamObj.get("name"));
				
				body.put("status", sm.removeStream(removeStream));
				this.httpStatus = HttpStatus.SC_OK;
			} else if (method.equals("assign")) {
				// Assign nodes to a stream
				JSONObject streamObj = (JSONObject) jsonRequest.get("stream");
				DatabaseStream stream = new DatabaseStream((Integer) streamObj.get("id"), (String) streamObj.get("name"));
				JSONArray nodeListArray = (JSONArray) jsonRequest.get("nodes");
				ArrayList<DatabaseNode> nodeList = new ArrayList<DatabaseNode>();
				nodeList.addAll(nodeListArray);
				
				body.put("status", sm.setNodes(stream, nodeList));
				
				// TODO: trigger a process to switch the audio
				
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
