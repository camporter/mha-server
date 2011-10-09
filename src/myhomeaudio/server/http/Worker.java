package myhomeaudio.server.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;

import myhomeaudio.server.handler.ClientHandler;

/**
 * Worker is a thread that services a client's request.
 * 
 * @author cameron
 * 
 */
public class Worker extends Thread implements HTTPStatus {
	final static int BUF_SIZE = 2048;

	static final byte[] EOL = { (byte) '\r', (byte) '\n' };

	// buffer to use for requests
	byte[] buf;
	private Socket clientSocket;
	private ClientHandler clientHandler;

	public Worker(ClientHandler clientHandler) {
		this.clientHandler = clientHandler;
		this.buf = new byte[BUF_SIZE];
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
		InputStream inputStream = new BufferedInputStream(this.clientSocket.getInputStream());
		PrintStream printStream = new PrintStream(this.clientSocket.getOutputStream());
		
		this.clientSocket.setSoTimeout(clientHandler.timeout);
		this.clientSocket.setTcpNoDelay(true);
		
		// zero out the buffer from its last use
		for (int i=0; i<BUF_SIZE;i++) {
			buf[i] = 0;
		}
		try {
			int nread = 0, r = 0;
outerloop:
			while(nread < BUF_SIZE) {
				r = inputStream.read(buf, nread, BUF_SIZE - nread);
				if ( r == -1) {
					// EOF
					return;
				}
				int i = nread;
				nread += r;
				for (; i<nread; i++) {
					if (buf[i] == (byte)'\n' || buf[i] == (byte)'\r') {
						//read one line
						break outerloop;
					}
				}
			}
			
			// figure out which HTTP method we are doing, either GET or HEAD
			boolean doingGet;
			int index; // next byte to read
			
			if (buf[0] == (byte)'G' &&
					buf[1] == (byte)'E' &&
					buf[2] == (byte)'T' &&
					buf[3] == (byte)' ') {
				doingGet = true;
				index = 4;
			} else if(buf[0] == (byte)'H' &&
					buf[1] == (byte)'E' &&
					buf[2] == (byte)'A' &&
					buf[3] == (byte)'D' &&
					buf[4] == (byte)' ') {
				doingGet = false;
				index  = 5;
			} else {
				// Method not supported (yet)
				printStream.print("HTTP/1.0"+HTTP_BAD_METHOD+" unsupported method type: ");
				printStream.write(buf, 0, 5);
				printStream.write(EOL);
				printStream.flush();
				this.clientSocket.close();
				return;
			}
			
			// figure out what the URI is
			int i = 0;
			for (i = index; i<nread; i++) {
				if (buf[i] == (byte)' ') {
					// Found the end of the URI (can't have spaces)
					break;
				}
			}
			String uri = (new String(buf, index, i-index));
			boolean OK = printHeaders(uri, printStream);
			if (doingGet) {
				if (OK) {
					printStream.print("<html>hi there</html>");
					printStream.write(EOL);
				}
			}
		} finally {
			this.clientSocket.close();
		}
		
	}
	
	boolean printHeaders(String uri, PrintStream printStream) throws IOException {
		boolean ret = true;
		int rCode = 0;
		
		// not considering bad uris here yet
		
		rCode = HTTP_OK;
		printStream.print("HTTP/1.0 "+HTTP_OK+" OK");
		printStream.write(EOL);
		ret = true;
		
		System.out.println("From "+this.clientSocket.getInetAddress().getHostAddress()+": GET "+uri+" --> "+rCode);
		printStream.print("Server: My Home Audio");
		printStream.write(EOL);
		printStream.print("Date: "+(new Date()));
		printStream.write(EOL);
		if (ret) {
			printStream.print("Content-type: text/html");
			printStream.write(EOL);
		}
		return ret;
	}
}
