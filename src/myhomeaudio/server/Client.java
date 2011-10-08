package myhomeaudio.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.Timer;

//Client side
//TODO create multiple clients automatically with threads

public class Client {
	protected static int port = 9090;
	protected static String host = "localhost";
	static ClientConnect conn;
	static String msg;

	public Client() {
	}

	public static void main(String[] args) {
		// TODO Remove hardcoded client
		// Hardcoded client connect, send/receive, and disconnect
		conn = new ClientConnect(port, host);
		conn.start();
		// conn.closeConnection();
		// msg = "4567\nINIT\n0";
		// conn.send(msg);
	}
}