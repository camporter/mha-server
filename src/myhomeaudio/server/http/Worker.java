package myhomeaudio.server.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;

import myhomeaudio.server.handler.ClientHandler;

/**
 * Worker is a thread that services a client's request.
 * 
 * @author cameron
 * 
 */
public class Worker extends Thread {
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
			
			if (buf[0] == (byte)'G' &&
					buf[1] == (byte)'E' &&
					buf[2] == (byte)'T' &&
					buf[3] == (byte)' ') {
				doingGet = true;
				//index = 4;
			}
			
		} finally {
			
		}
		
	}
}
