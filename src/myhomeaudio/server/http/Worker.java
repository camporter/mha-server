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
public class Worker extends Thread implements HTTPStatus {
	final static int BUF_SIZE = 2048;

	static final String EOL = "\r\n";

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
		while(true) {
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
			
			try {
				handleClient();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			clientSocket = null; // reset the socket for next use
			
			// Done using the Worker, put it back in the Worker pool
			synchronized(clientHandler) {
				if (!clientHandler.addWorker(this)) {
					// Worker no longer needed
					return;
				}
			}
		}
	}

	private void handleClient() throws IOException {
		BufferedReader inputStream = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
		DataOutputStream outputStream = new DataOutputStream(this.clientSocket.getOutputStream());
		
		String output;
		String uri = "";
		String requestMessageLine = inputStream.readLine();
		
		StringTokenizer tokenizedLine = new StringTokenizer(requestMessageLine);
		
		if (tokenizedLine.nextToken().equals("GET")) {
			// We have an HTTP GET method
			uri = tokenizedLine.nextToken();
			uri = uri.startsWith("/") ? uri.substring(1) : uri; // Get rid of starting slash
			
			output = "HTTP/1.0 200 OK\r\n";
			output += "Content-type: text/html\r\n";
			output += "\r\n";
			output += "<html><h1>I like puppies</h1></html>";
			
			outputStream.writeBytes(output);
			
		}
		
		clientSocket.close();
		uri = null;
		output = null;
		tokenizedLine = null;
		/*
		this.clientSocket.setSoTimeout(clientHandler.timeout);
		this.clientSocket.setTcpNoDelay(true);
		
			
			// figure out what the URI is
			int i = 0;
			for (i = index; i<nread; i++) {
				if (buf[i] == (byte)' ') {
					// Found the end of the URI (can't have spaces)
					break;
				}
			}
			String uri = (new String(buf, index, i-index));
			output += printHeaders(uri, printStream);
			if (doingGet) {
				output += "<html>hi there</html>";
				//printStream.print("<html>hi there</html>");
				//printStream.write(EOL);
			}
			
		} finally {
			printStream.print(output);
			inputStream.close();
			printStream.close();
			this.clientSocket.close();
		}
		*/
		
	}
	
	String printHeaders(String uri, PrintStream printStream) throws IOException {
		String ret = "";
		int rCode = 0;
		
		// not considering bad uris here yet
		
		rCode = HTTP_OK;
		//printStream.print("HTTP/1.0 "+HTTP_OK+" OK");
		//printStream.write(EOL);
		ret = "HTTP/1.0 " +HTTP_OK+" OK"+EOL;
		
		System.out.println("From "+this.clientSocket.getInetAddress().getHostAddress()+": GET "+uri+" --> "+rCode);
		/*printStream.print("Server: My Home Audio");
		printStream.write(EOL);
		printStream.print("Date: "+(new Date()));
		printStream.write(EOL);
		printStream.print("Content-type: text/html");
		printStream.write(EOL);
		*/
		
		ret += "Server: My Home Audio" + EOL + "Date: "+(new Date()) + EOL + "Content-type: text/html" + EOL;
		
		return ret;
	}
}
