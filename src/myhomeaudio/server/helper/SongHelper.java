package myhomeaudio.server.helper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Hashtable;
import java.util.Locale;
import java.util.StringTokenizer;

import myhomeaudio.server.manager.NodeManager;
import myhomeaudio.server.node.NodeCommands;
import myhomeaudio.server.songs.SongFiles;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

public class SongHelper extends Helper implements HelperInterface, NodeCommands {

	@Override
	public String getOutput(String uri, String data) {
		String body = "";

		StringTokenizer tokenizedUri = new StringTokenizer(uri, "/");
		tokenizedUri.nextToken(); // throw the first part away, throws /song
									// away

		if (tokenizedUri.hasMoreTokens()) {
			// Possible methods: list, play, pause
			String method = tokenizedUri.nextToken(); // NoSuchElementException
			if (method.equals("list")) {
				SongFiles songs = SongFiles.getInstance();
				Gson gson = new Gson();

				body = gson.toJson(songs.getSongList());
				this.statusCode = HttpStatus.SC_OK;
			} else if (method.equals("play")) {
				Gson gson = new Gson();
				Hashtable hasht = gson.fromJson(data.trim(), Hashtable.class);

				this.statusCode = HttpStatus.SC_OK;
				// TODO need to know ipaddress of node to send data to
				NodeManager nm = NodeManager.getInstance();
				nm.sendNodeCommand(NODE_PLAY, hasht.get("song").toString());
				// nm.sendNodeCommand(NODE_PLAY, "Buffalo For What.mp3");
			} else if (method.equals("pause")) {
				this.statusCode = HttpStatus.SC_OK;
				NodeManager nm = NodeManager.getInstance();
				nm.sendNodeCommand(NODE_PAUSE, "");
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
