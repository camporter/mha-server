package myhomeaudio.server.http;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import myhomeaudio.server.manager.NodeManager;
import myhomeaudio.server.node.NodeCommands;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * NodeWorker is a thread that sends commands to nodes and receives
 * confirmation.
 * 
 * @author Cameron
 * 
 */
public class NodeWorker extends Thread implements HTTPMimeType, NodeCommands {

	int command = -1;
	String ipAddress;
	String data;
	byte[] byteData;

	public NodeWorker() {

	}
	
	public void setInfoCommand(String ipAddress, String data) {
		this.command = NODE_INFO;
		this.ipAddress = ipAddress;
		this.data = data;
	}
	
	public void setPlayCommand(String ipAddress, byte[] data) {
		this.command = NODE_PLAY;
		this.ipAddress = ipAddress;
		this.byteData = data;
	}
	
	public void setPauseCommand(String ipAddress) {
		this.command = NODE_PAUSE;
		this.ipAddress = ipAddress;
	}

	synchronized public void run() {

		NodeManager nm = NodeManager.getInstance();

		HttpParams httpParams = new SyncBasicHttpParams();
		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(httpParams, "UTF-8");
		HttpProtocolParams.setUserAgent(httpParams, "MyHomeAudio");
		HttpProtocolParams.setUseExpectContinue(httpParams, true);

		HttpProcessor httpProcessor = new ImmutableHttpProcessor(
				new HttpRequestInterceptor[] { new RequestContent(),
						new RequestTargetHost(), new RequestConnControl(),
						new RequestUserAgent(), new RequestExpectContinue() });

		HttpRequestExecutor httpExecutor = new HttpRequestExecutor();

		HttpContext httpContext = new BasicHttpContext(null);
		HttpHost host = new HttpHost(this.ipAddress, 9091);

		DefaultHttpClientConnection connection = new DefaultHttpClientConnection();
		ConnectionReuseStrategy connStrategy = new DefaultConnectionReuseStrategy();

		httpContext.setAttribute(ExecutionContext.HTTP_CONNECTION, connection);
		httpContext.setAttribute(ExecutionContext.HTTP_TARGET_HOST, host);

		if (this.command == -1) {
			// request data hasn't been set yet
			System.out.println("NodeWorker: Request data hasn't been set");
			try {
				// put the thread to sleep
				wait();
			} catch (InterruptedException e) {
				// ???
			}
		}
		try {

			if (!connection.isOpen()) {
				Socket socket = new Socket(this.ipAddress, 9091);
				connection.bind(socket, httpParams);
			}

			BasicHttpRequest getRequest;
			BasicHttpEntityEnclosingRequest postRequest;
			HttpResponse response;

			switch (command) {
			case NODE_PLAY:
				// TODO: Fix for new stream stuff
				// SongFiles s = SongFiles.getInstance(); // Gets song list
				// System.out.println("Playing song " + this.data +
				// " to node...");
				byte[] songData = byteData;// s.getSongData(this.data); //
												// Gets byte[] of mp3 data

				postRequest = new BasicHttpEntityEnclosingRequest("POST",
						"play");
				postRequest.setParams(httpParams);

				postRequest.setEntity(new ByteArrayEntity(songData));

				try {
					httpExecutor.preProcess(postRequest, httpProcessor,
							httpContext);
					response = httpExecutor.execute(postRequest, connection,
							httpContext);
					response.setParams(httpParams);
					httpExecutor.postProcess(response, httpProcessor,
							httpContext);
				} catch (HttpException e) {
					e.printStackTrace();
				}

				break;

			case NODE_PAUSE:
				System.out.println("Pausing song on node...");
				postRequest = new BasicHttpEntityEnclosingRequest("POST",
						"pause");
				postRequest.setParams(httpParams);

				try {
					httpExecutor.preProcess(postRequest, httpProcessor,
							httpContext);
					response = httpExecutor.execute(postRequest, connection,
							httpContext);
					response.setParams(httpParams);
					httpExecutor.postProcess(response, httpProcessor,
							httpContext);
				} catch (HttpException e) {
					e.printStackTrace();
				}
				break;

			case NODE_INFO:
				System.out.println("Asking for node's name...");
				getRequest = new BasicHttpRequest("GET", "info");
				getRequest.setParams(httpParams);

				try {
					httpExecutor.preProcess(getRequest, httpProcessor,
							httpContext);
					response = httpExecutor.execute(getRequest, connection,
							httpContext);
					response.setParams(httpParams);
					httpExecutor.postProcess(response, httpProcessor,
							httpContext);

					// Get the corresponding node
					if (nm.isValidNodeByIpAddress(this.ipAddress)) {
						// Change the bluetooth name for the node
						String request = EntityUtils.toString(
								response.getEntity()).trim();
						JSONObject requestObject = (JSONObject) JSONValue
								.parse(request);
						nm.updateNodeInfo(this.ipAddress,
								(String) requestObject.get("bluetoothName"),
								(String) requestObject.get("bluetoothAddress"));
						System.out.println("Got info from node: "
								+ ((String) requestObject.get("bluetoothName"))
								+ ", "
								+ ((String) requestObject
										.get("bluetoothAddress")));
					}
				} catch (HttpException e) {
					e.printStackTrace();
				}
				break;

			}
			connection.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
