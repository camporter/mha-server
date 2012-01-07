package myhomeaudio.server.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
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

import myhomeaudio.server.manager.NodeManager;
import myhomeaudio.server.node.Node;
import myhomeaudio.server.node.NodeCommands;
import myhomeaudio.server.songs.SongFiles;

/**
 * NodeWorker is a thread that sends commands to nodes and receives
 * confirmation.
 * 
 * @author Cameron
 * 
 */
public class NodeWorker extends Thread implements HTTPStatus, HTTPMimeType, NodeCommands {

	int command = -1;
	String ipAddress;
	String data;

	public NodeWorker() {

	}

	/**
	 * Initializes NodeWorker to handle request to node
	 * 
	 * @param command
	 *            Command for server to execute to node
	 * @param ipAddress
	 *            Address of node
	 * @param data
	 *            Data to be sent to the node
	 */
	synchronized public void setRequestData(int command, String ipAddress, String data) {
		this.command = command;
		this.ipAddress = ipAddress;
		this.data = data;
		notify();
	}

	synchronized public void run() {

		NodeManager nm = NodeManager.getInstance();

		HttpParams httpParams = new SyncBasicHttpParams();
		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(httpParams, "UTF-8");
		HttpProtocolParams.setUserAgent(httpParams, "MyHomeAudio");
		HttpProtocolParams.setUseExpectContinue(httpParams, true);

		HttpProcessor httpProcessor = new ImmutableHttpProcessor(new HttpRequestInterceptor[] {
				new RequestContent(), new RequestTargetHost(), new RequestConnControl(),
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
				SongFiles s = SongFiles.getInstance(); // Gets song list
				System.out.println("Playing song " + this.data + " to node...");
				byte[] songData = s.getSongData(this.data); // Gets byte[] of
															// mp3 data

				postRequest = new BasicHttpEntityEnclosingRequest("POST", "play");
				postRequest.setParams(httpParams);

				postRequest.setEntity(new ByteArrayEntity(songData));

				try {
					httpExecutor.preProcess(postRequest, httpProcessor, httpContext);
					response = httpExecutor.execute(postRequest, connection, httpContext);
					response.setParams(httpParams);
					httpExecutor.postProcess(response, httpProcessor, httpContext);
				} catch (HttpException e) {
					e.printStackTrace();
				}

				/*
				 * 
				 * outputStream.writeBytes(HTTPHeader.buildRequest("POST",
				 * "play", true, MIME_MP3, songData.length));//Puts songdata in
				 * http request
				 * 
				 * outputStream.write(songData);
				 */
				break;

			case NODE_PAUSE:
				System.out.println("Pausing song on node...");
				getRequest = new BasicHttpRequest("GET", "pause");
				getRequest.setParams(httpParams);

				try {
					httpExecutor.preProcess(getRequest, httpProcessor, httpContext);
					response = httpExecutor.execute(getRequest, connection, httpContext);
					response.setParams(httpParams);
					httpExecutor.postProcess(response, httpProcessor, httpContext);
				} catch (HttpException e) {
					e.printStackTrace();
				}
				break;

			case NODE_NAME:
				System.out.println("Asking for node's name...");
				getRequest = new BasicHttpRequest("GET", "name");
				getRequest.setParams(httpParams);

				try {
					httpExecutor.preProcess(getRequest, httpProcessor, httpContext);
					response = httpExecutor.execute(getRequest, connection, httpContext);
					response.setParams(httpParams);
					httpExecutor.postProcess(response, httpProcessor, httpContext);

					// Get the corresponding node
					Node node = nm.getNodeByIpAddress(this.ipAddress);
					if (node != null) {
						// Change the bluetooth name for the node
						String name = EntityUtils.toString(response.getEntity()).trim();
						node.setName(name);
						System.out.println("Got name from node: " + name);
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
