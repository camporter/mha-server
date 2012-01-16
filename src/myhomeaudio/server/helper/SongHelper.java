package myhomeaudio.server.helper;

import java.io.IOException;
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

public class SongHelper extends Helper implements HelperInterface, NodeCommands {

	@Override
	public String getOutput(String uri, String data) {
		String body = "";
		ClientManager cm = ClientManager.getInstance();

		StringTokenizer tokenizedUri = new StringTokenizer(uri, "/");
		tokenizedUri.nextToken(); // throw the first part away, throws /song
									// away

		if (tokenizedUri.hasMoreTokens()) {

			String method = tokenizedUri.nextToken(); // NoSuchElementException
			if (method.equals("list")) {
				// List the songs available
				SongFiles songs = SongFiles.getInstance();
				Gson gson = new Gson();

				body = gson.toJson(songs.getSongList());
				this.httpStatus = HttpStatus.SC_OK;

			} else if (method.equals("play")) {
				// Play a defined song
				Gson gson = new Gson();
				Hashtable hasht = gson.fromJson(data.trim(), Hashtable.class);

				// Make sure a song to play is actually given
				if (hasht != null && hasht.containsKey("song")) {
					this.httpStatus = HttpStatus.SC_OK;

					/*NodeManager nm = NodeManager.getInstance();
					// TODO: GET RID OF THIS UGLY
					String songName = hasht.get("song").toString();
					Client client = cm.getClient();
					String closestNodeName = client.getClosestNodeName();
					Node node = nm.getNodeByName(closestNodeName);
					String ipaddr = node.getIpAddress();
					nm.sendNodeCommand(NODE_PLAY, ipaddr, hasht.get("song").toString());
					cm.getClient().setCurrentSong(songName);
					*/
				} else {
					// No song given, send a bad request response
					this.httpStatus = HttpStatus.SC_BAD_REQUEST;
				}

			} else if (method.equals("pause")) {
				// Pause the song playing
				this.httpStatus = HttpStatus.SC_OK;
				NodeManager nm = NodeManager.getInstance();

				//nm.sendNodeCommand(NODE_PAUSE, nm
				//		.getNodeByName(cm.getClient().getClosestNodeName()).getIpAddress(), "");
			}

		} else {

		}
		return body;
	}
}
