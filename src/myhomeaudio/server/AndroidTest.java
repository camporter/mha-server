package myhomeaudio.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.google.gson.Gson;

import myhomeaudio.server.http.HTTPHeader;

public class AndroidTest {
	protected static int port = 8080;
	protected static String host = "localhost";
	private static Socket tcpSocket;
	static ClientConnect conn;
	static String msg;
	private static BufferedReader br;
	private static BufferedWriter bw;
	private static InputStream is;
	private static OutputStream os;
	private static String httpBody;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			System.out.println("Creating Connection");
			tcpSocket = new Socket(host, port);
			System.out.println("Android TCP Socket Created");
			
			is = tcpSocket.getInputStream();
			os = tcpSocket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(is));
			bw = new BufferedWriter(new OutputStreamWriter(os));
			
			String message = HTTPHeader.buildRequest("GET", "/song/list", false, "", 0);
			System.out.println(message);
			bw.write(message);
			bw.flush();
			System.out.println("Message Sent");
			message = br.readLine();
			System.out.println("Message Received");
			int count = 0;
			do {
				message = br.readLine();
				System.out.println(message + count++);
			} while (message != null && message.length() != 0);

			count = 0;
			if (message != null) {
				// Found the empty line, everything below is the body
				do {
					System.out.println(message + count++);
					message = br.readLine();
					httpBody += (message != null) ? (message + "\r\n") : "";
				} while (message != null && message.length() != 0);

			}
			
			System.out.println(httpBody);
			Gson gson = new Gson();
			ArrayList<String> songs = gson.fromJson(httpBody, ArrayList.class);
			
			
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
