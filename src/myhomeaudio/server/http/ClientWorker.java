package myhomeaudio.server.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpServerConnection;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;

import myhomeaudio.server.handler.ClientHandler;
import myhomeaudio.server.helper.Helper;
import myhomeaudio.server.helper.NodeHelper;
import myhomeaudio.server.helper.SongHelper;
import myhomeaudio.server.helper.StreamHelper;
import myhomeaudio.server.helper.UserHelper;

/**
 * ClientWorker is a thread that services a client's request.
 * 
 * @author cameron
 * 
 */
public class ClientWorker extends Thread implements HTTPStatus, HTTPMimeType {
	final static int BUF_SIZE = 2048;
	
	// buffer to use for requests
	byte[] buf;
	private Socket clientSocket;
	private ClientHandler clientHandler;
	
	//http parameters
	private final HttpParams params;

	public ClientWorker(ClientHandler clientHandler) {
		this.clientHandler = clientHandler;
		this.clientSocket = null;
		
        this.params = new SyncBasicHttpParams();
        this.params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
        this.params.setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024);
        this.params.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false);
        this.params.setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true);
        this.params.setParameter(CoreProtocolPNames.ORIGIN_SERVER, "Server: My Home Audio");
	}

	// Set the client socket
	synchronized public void setClientSocket(Socket socket) {
		this.clientSocket = socket;
		notify();
	}

	synchronized public void run() {
		while (true) {
			if (clientSocket == null) {
				// Don't have a socket yet!
				try {
					// Put the thread to sleep
					wait();
				} catch (InterruptedException e) {
					// ???
					continue;
				}
			}

			// The socket is ready, go ahead and start dealing with the client
			try {
				handleClient();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			clientSocket = null; // reset the socket for next use

			// Done using the ClientWorker, put it back in the ClientWorker pool
			synchronized (clientHandler) {
				if (!clientHandler.addWorker(this)) {
					// ClientWorker no longer needed, so end it
					return;
				}
			}
		}
	}

	/**
	 * Handles all HTTP requests that come in, and responds accordingly.
	 * 
	 * @throws IOException
	 */
	private void handleClient() throws IOException {
		try{
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(
					this.clientSocket.getInputStream()));
			DataOutputStream outputStream = new DataOutputStream(
					this.clientSocket.getOutputStream());
			
			
			HttpEntityEnclosingRequest entityRequest = null;
			DefaultHttpServerConnection serverConn = new DefaultHttpServerConnection();
			serverConn.bind(this.clientSocket, this.params);
			try{
				serverConn.receiveRequestEntity(entityRequest);
			}catch(HttpException e){
				System.out.println("kie");
			}catch(IOException e){
				System.out.println("dfs");
			}
			HttpEntity entity = entityRequest.getEntity();//throws httpException and IOException
			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
			
			String line = null;
			while((line = reader.readLine()) != null){
				System.out.println(line);
			}
			

			
			/*
			HttpResponse response;
			
			String t = this.host+"/song/list";
			//HttpGet httpGet = new HttpGet(this.host+"/song/list");
			System.out.println(t);
			HttpGet httpGet = new HttpGet(t);
			HttpClient httpClient;
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			
	
			System.out.println("Handling the client...");
	
			/*
			 * The final output we will send back to the client. We will slowly
			 * build it based off of what the client has requested.
			 *
			 *
			String output = "";
	
			String requestUri;
	
			// Get the HTTP header from the client
			String requestMessageLine = inputStream.readLine();
		
			StringTokenizer tokenizedRequestMessage = new StringTokenizer(
					requestMessageLine);
	
			String httpMethod = tokenizedRequestMessage.nextToken();
	
			String httpBody = "";
			if (httpMethod.equals("POST")) {
				// POST methods contain data, so we need to put it in httpBody
				String scan; // a string to read out individual lines
	
				// Keep reading lines until we find a blank line.
				// A blank line tells us when the HTTP header has ended and the HTTP
				// body begins.
				do {
					scan = inputStream.readLine();
				} while (scan != null && scan.length() != 0);
	
				if (scan != null) {
					// Found the empty line, everything below is the body
					do {
						scan = inputStream.readLine();
						httpBody += (scan != null) ? (scan + "\r\n") : "";
					} while (scan != null && scan.length() != 0);
	
				}
			}
			
			requestUri = tokenizedRequestMessage.nextToken();
			
			requestUri = requestUri.startsWith("/") ? requestUri.substring(1)
					: requestUri; // Get rid of starting slash
	
			Helper currentHelper;
			StringTokenizer tokenizedUri = new StringTokenizer(requestUri, "/");
	
			try {
				currentHelper = getCorrectHelper(tokenizedUri.nextToken());
	
			} catch (NoSuchElementException e) {
				// Client didn't specify a helper.
				// Do something else here
				currentHelper = new Helper();
	
			}
	
			// Give the helper the URI and body
			//System.out.println(requestUri);
			//System.out.println(httpBody);
			currentHelper.setData(requestUri, httpBody);
			// Get back the output it has generated
			output = currentHelper.getOutput();
	
			// System.out.println(output);
			// Spit out the output generated by our selected Helper to the client
			outputStream.writeBytes(output);

			// Clean up stuff
			clientSocket.close();
			requestUri = null;
			output = null;
			tokenizedRequestMessage = null;
			
			*/
		}catch(NullPointerException e){
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/* Uses root of URI directory to choose correct Helper to handle request
	 * 	/node/list   <- NodeHelper
	 *  /song/list   <- SongHelper
	 * 
	 * @param helperName
	 * 		Name of the Helper class required to handle the request
	 * 
	 * @return Helper
	 * 		Returns the specified Helper class
	 */
	private Helper getCorrectHelper(String helperName) {
		if (helperName.equals("node")) {
			System.out.println("Creating NodeHelper");
			return new NodeHelper();
		} else if (helperName.equals("song")) {
			System.out.println("Creating SongHelper");
			return new SongHelper();
		} else {
			// default
			return new Helper();
		}

	}
}
