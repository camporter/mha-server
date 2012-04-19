package myhomeaudio.server.http.helper.client;

import java.util.ArrayList;
import myhomeaudio.server.http.HTTPMimeType;
import myhomeaudio.server.http.StatusCode;
import myhomeaudio.server.http.helper.Helper;
import myhomeaudio.server.http.helper.HelperInterface;
import myhomeaudio.server.manager.ClientManager;
import myhomeaudio.server.manager.NodeManager;
import myhomeaudio.server.node.NodeCommands;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class SongHelper extends Helper implements HelperInterface, NodeCommands, StatusCode {

	@Override
	public String getOutput(ArrayList<String> uriSegments, String data) {

		// Set the content-type
		this.contentType = HTTPMimeType.MIME_JSON;
		this.httpStatus = HttpStatus.SC_BAD_REQUEST;

		// Create the JSON object to represent the response
		JSONObject body = new JSONObject();
		body.put("status", STATUS_FAILED);

		ClientManager cm = ClientManager.getInstance();

		try {
			
			JSONObject jsonRequest = (JSONObject) JSONValue.parse(data);
			
			/*if (uriSegments.get(1).equals("list")) {
				// List the songs available
				SongFiles songs = SongFiles.getInstance();
				
				body.put("status", STATUS_OK);
				body.put("songs", songs.getSongList());
				this.httpStatus = HttpStatus.SC_OK;

			} else */ if (uriSegments.get(1).equals("play")) {
				// Play a defined song
				

				// Make sure a song to play is actually given
				if (jsonRequest.containsKey("song")) {
					this.httpStatus = HttpStatus.SC_OK;

					/*
					 * NodeManager nm = NodeManager.getInstance(); // TODO: GET
					 * RID OF THIS UGLY String songName =
					 * hasht.get("song").toString(); Client client =
					 * cm.getClient(); String closestNodeName =
					 * client.getClosestNodeName(); Node node =
					 * nm.getNodeByName(closestNodeName); String ipaddr =
					 * node.getIpAddress(); nm.sendNodeCommand(NODE_PLAY,
					 * ipaddr, hasht.get("song").toString());
					 * cm.getClient().setCurrentSong(songName);
					 */
				} else {
					// No song given, send a bad request response
					this.httpStatus = HttpStatus.SC_BAD_REQUEST;
				}

			} else if (uriSegments.get(1).equals("pause")) {
				// Pause the song playing
				this.httpStatus = HttpStatus.SC_OK;
				NodeManager nm = NodeManager.getInstance();

				// nm.sendNodeCommand(NODE_PAUSE, nm
				// .getNodeByName(cm.getClient().getClosestNodeName()).getIpAddress(),
				// "");
			}
		} catch (Exception e) {

		}
		return body.toString();
	}
}
