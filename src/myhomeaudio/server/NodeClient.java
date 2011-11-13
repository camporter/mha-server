package myhomeaudio.server;

//Client side
//TODO create multiple clients automatically with threads

public class NodeClient {
	protected static int port = 9090;
	protected static String host = "localhost";
	static NodeClientConnect conn;
	static String msg;

	public NodeClient() {
	}

	public static void main(String[] args) {
		// TODO Remove hardcoded client
		// Hardcoded client connect, send/receive, and disconnect
		conn = new NodeClientConnect(port, host);
		conn.start();
		// conn.closeConnection();
		// msg = "4567\nINIT\n0";
		// conn.send(msg);
	}
}