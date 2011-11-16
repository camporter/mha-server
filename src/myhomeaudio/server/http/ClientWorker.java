package myhomeaudio.server.http;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;

import myhomeaudio.server.handler.ClientHandler;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpException;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpService;

public class ClientWorker extends Worker {
	//http parameters and services
	private final HttpParams params;
	private final HttpService httpService;
	private final DefaultHttpServerConnection conn;
	
	
	//Client networking
	private Socket clientSocket;
	private ClientHandler clientHandler;

	public ClientWorker(ClientHandler clientHandler) {
		super();
		this.params = super.getParams();
		this.httpService = super.getHttpService();
		this.conn = new DefaultHttpServerConnection();
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
					// Put the thread to sleep
					wait();
				} catch (InterruptedException e) {
					// ???
					continue;
				}
			}

			// The socket is ready, go ahead and start dealing with the client
			handleClient();

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

	private void handleClient() {
		while (!this.isInterrupted()) {
			try {
				// Set up HTTP connection
                this.conn.bind(clientSocket, this.params);
                
                HttpContext context = new BasicHttpContext(null);
                while (!Thread.interrupted() && this.conn.isOpen()) {
                	this.httpService.handleRequest(this.conn, context);
                } 
            }catch (InterruptedIOException ex) {
                break;
            }catch (ConnectionClosedException ex) {
                System.err.println("Client closed connection");
            }catch (IOException ex) {
                System.err.println("I/O error: " + ex.getMessage());
            }catch (HttpException ex) {
                System.err.println("Unrecoverable HTTP protocol violation: " + ex.getMessage());
            }finally {
                try {
                    this.conn.shutdown();
                } catch (IOException e) {}
            }
		}
	}

}
