package myhomeaudio.server.http;

import java.net.Socket;

import myhomeaudio.server.handler.ClientHandler;

import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpService;

public class ClientWorker1 extends Worker implements Runnable {
	//http parameters and services
	private final HttpParams params;
	private final HttpService httpService;
	
	//Client networking
	private Socket clientSocket;
	private ClientHandler clientHandler;

	public ClientWorker1(ClientHandler clientHandler) {
		super();
		this.params = super.getParams();
		this.httpService = super.getHttpService();
		this.clientHandler = clientHandler;
		this.clientSocket = null;
	}

	// Set the client socket
	synchronized public void setClientSocket(Socket socket) {
		this.clientSocket = socket;
		notify();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

		
		//close socket
	}

}
