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
import java.util.StringTokenizer;

import myhomeaudio.server.handler.ClientHandler;

/**
 * Worker is a thread that services a client's request.
 * 
 * @author cameron
 * 
 */
public class Worker extends Thread implements HTTPStatus, HTTPMimeType {
	final static int BUF_SIZE = 2048;

	// buffer to use for requests
	byte[] buf;
	private Socket clientSocket;
	private ClientHandler clientHandler;

	public Worker(ClientHandler clientHandler) {
		this.clientHandler = clientHandler;
		this.clientSocket = null;
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
					// Wait for the thread to wake back up
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

			// Done using the Worker, put it back in the Worker pool
			synchronized (clientHandler) {
				if (!clientHandler.addWorker(this)) {
					// Worker no longer needed, so end it
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
		BufferedReader inputStream = new BufferedReader(new InputStreamReader(
				this.clientSocket.getInputStream()));
		DataOutputStream outputStream = new DataOutputStream(
				this.clientSocket.getOutputStream());

		String output; /*
						 * The final output we will send back to the client. We
						 * will slowly build it based off of what the client has
						 * requested.
						 */
		String requestUri;
		String requestMessageLine = inputStream.readLine();

		StringTokenizer tokenizedRequestMessage = new StringTokenizer(
				requestMessageLine);

		String httpMethod = tokenizedRequestMessage.nextToken();
		if (httpMethod.equals("GET")) {
			// We have an HTTP GET method
			requestUri = tokenizedRequestMessage.nextToken();
			requestUri = requestUri.startsWith("/") ? requestUri.substring(1)
					: requestUri; // Get rid of starting slash

			
			String content = "<html><h1>I like puppies!!!!!!!!!!!!!!11111111111111</h1></html>";
			
			output = buildHeader(HTTP_OK, true, MIME_HTML, content.getBytes().length);
			output += content;
			
			outputStream.writeBytes(output);

		} else if (httpMethod.equals("POST")) {
			// We have an HTTP POST method
		}

		clientSocket.close();
		requestUri = null;
		output = null;
		tokenizedRequestMessage = null;

	}

	/**
	 * Builds the HTTP header that will be sent back to the client.
	 * 
	 * @param httpStatus
	 *            Status code of the response.
	 * @param hasContent
	 *            Indicates whether content is included in the response.
	 * @param mimeType
	 *            The mime-type of the content being served in the response. Not
	 *            needed if hasContent is false.
	 * @param contentLength
	 *            The size (in bytes) of the content being sent. Not needed if
	 *            hasContent is false.
	 * @return
	 * @throws IOException
	 */
	String buildHeader(int httpStatus, boolean hasContent, String mimeType,
			int contentLength) throws IOException {
		String ret = "HTTP/1.0 ";

		switch (httpStatus) {
		case HTTP_OK:
			ret += HTTP_OK + " OK\r\n";
			break;
		case HTTP_NOT_FOUND:
			ret += HTTP_NOT_FOUND + " Not Found\r\n";
			break;
		}

		// Add server software name
		ret += "Server: My Home Audio\r\n";

		// Add the date
		ret += "Date: " + (new Date()) + "\r\n";

		if (hasContent) {
			// Add the content type
			ret += "Content-type: " + mimeType + "\r\n";

			// Add the content length
			ret += "Content-length: " + contentLength + "\r\n";
		}
		
		ret += "\r\n";
		return ret;
	}
}
