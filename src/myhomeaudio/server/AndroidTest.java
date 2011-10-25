package myhomeaudio.server;

import myhomeaudio.server.implement.ServerImpl;

public class AndroidTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerImpl si = new ServerImpl();
		si.connect();

	}

}
