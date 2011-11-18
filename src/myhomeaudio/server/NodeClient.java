package myhomeaudio.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

//Client side
//TODO create multiple clients automatically with threads

public class NodeClient {
	protected static int serverPort = 9090;
	protected static int nodePort = 9091;
	protected static String host = "192.168.10.101";

	public static void main(String[] args) {
		try {
			System.out.println("Starting...");
			Socket serverDiscovery = new Socket(NodeClient.host,
					NodeClient.serverPort);
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			serverDiscovery.close();
			System.out.println("Server discovery complete.");

			ServerSocket listenSocket = new ServerSocket(NodeClient.nodePort);

			while (true) {
				Socket serverSocket = listenSocket.accept();
				System.out.println("Found a server request");
				// Server IP matches the IP address given
				BufferedReader inputStream = new BufferedReader(
						new InputStreamReader(serverSocket.getInputStream()));
				DataOutputStream outputStream = new DataOutputStream(
						serverSocket.getOutputStream());

				String output = "";
				String requestUri;
				String requestMessageLine = inputStream.readLine();

				StringTokenizer tokenizedRequestMessage = new StringTokenizer(
						requestMessageLine);

				String httpMethod = tokenizedRequestMessage.nextToken();

				String httpBody = "";
				if (httpMethod.equals("POST")) {
					// POST methods contain data, so we need to put it in
					// httpBody
					String scan; // a string to read out individual lines

					// Keep reading lines until we find a blank line.
					// A blank line tells us when the HTTP header has ended and
					// the HTTP
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
					requestUri = tokenizedRequestMessage.nextToken();
					requestUri = requestUri.startsWith("/") ? requestUri
							.substring(1) : requestUri;

					System.out.println(requestUri);
					//if (requestUri.equals("play")) {
						BufferedWriter songOut = new BufferedWriter(
								new FileWriter("song.mp3"));
						songOut.write(httpBody.trim());
						songOut.close();
					//}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}