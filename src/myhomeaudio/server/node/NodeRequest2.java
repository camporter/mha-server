package myhomeaudio.server.node;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.swing.Timer;

public class NodeRequest2 extends Thread implements NodeRequestCommands {

	protected Socket tcpSocket = null;

	final static int serverId = 10;
	Node node;

	// Stream variables
	BufferedReader inputStream = null;
	DataOutputStream outputStream = null;
	// MP3 I/O
	boolean isMP3Playing = false; /* Whether MP3 data is being sent. */
	File mp3File = new File("music/Buffalo For What.mp3");
	FileInputStream mp3Stream = null;
	byte[] mp3Buffer;

	public NodeRequest2(Socket socket) {
		this.tcpSocket = socket;

		// Open a stream for the mp3 so we can read it
		try {
			mp3Stream = new FileInputStream(mp3File);
			mp3Buffer = new byte[1024];
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parses requests made by the node.
	 * 
	 * @return The type of request being made.
	 */
	private int parseRequest(String requestLine) {
		int request = -1;

		// Make sure request is not empty
		if (requestLine == null) {
			return request;
		}

		StringTokenizer tokenizedRequestMessage = new StringTokenizer(requestLine);

		String requestState;
		try {
			// Try getting the first string token
			requestState = tokenizedRequestMessage.nextToken();
		} catch (Exception e) {
			// Request is messed up, ignore it
			return request;
		}

		if (requestState.equals("INIT")) {
			request = INIT;
		} else if (requestState.equals("PLAY")) {
			request = PLAY;
		} else if (requestState.equals("SWITCH")) {
			request = SWITCH;
		} else if (requestState.equals("STOP")) {
			request = STOP;
		} else if (requestState.equals("DISCONNECT")) {
			request = DISCONNECT;
		} else if (requestState.equals("RECEIVED")) {
			request = RECEIVED;
		} else {
			request = -1;
		}
		return request;
	}

	public void run() {
		// Initialize server state
		System.out.println("Running");
		try {
			this.inputStream = new BufferedReader(new InputStreamReader(
					this.tcpSocket.getInputStream()));
			this.outputStream = new DataOutputStream(this.tcpSocket.getOutputStream());

			boolean isReady = false; /*
									 * Whether we are ready to respond to other
									 * commands
									 */

			/*
			 * Get the initial request from the client, should be an INIT
			 * request
			 */
			while (!isReady) {
				if (parseRequest(inputStream.readLine()) == INIT) {
					isReady = true;
					// send the ready command back
					sendResponse("READY");
				}
			}
			while (true) {
				if (inputStream.ready()) {
					// Ready to get any commands from the client
					int requestCommand = parseRequest(inputStream.readLine());

					if (requestCommand == PLAY) {
						isMP3Playing = true;
					} else if (requestCommand == STOP) {
						isMP3Playing = false;
					} else {
						// Something bad happened
						return;
					}
				}

				if (isMP3Playing) {
					sendResponse("MP3");
					try {
						NodeRequest2.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			// Socket has been broken
			return;
		}
	}

	/**
	 * Sends responses to the Node.
	 * 
	 * @param responseCommand
	 *            The command to send.
	 * @throws IOException
	 */
	private void sendResponse(String responseCommand) throws IOException {
		// Server is ready for commands from the Node
		if (responseCommand.equals("READY")) {
			// READY <serverID> <nodeID>
			String output = "READY\r\n";
			this.outputStream.writeBytes(output);
		}
		// Server is sending MP3 data to the Node
		else if (responseCommand.equals("MP3")) {

			int numberBytesRead = 0;
			// Loop through reading 1024 bytes of mp3 data
			while (numberBytesRead < 1023) {
				int readByte = this.mp3Stream.read(); // get the byte
				if (readByte == -1) {
					// Didn't read a byte, we are at the end of the file
					this.isMP3Playing = false; // Stop streaming
					break;
				} else {
					// We read a byte, so put it in the buffer
					this.mp3Buffer[numberBytesRead] = (byte) readByte;
				}
				++numberBytesRead;
			}

			// MP3
			// <data>
			// this.outputStream.writeBytes("MP3\r\n");
			this.outputStream.write(mp3Buffer);
		}
	}
}
